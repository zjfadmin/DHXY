package org.come.redis;

import come.tool.Stall.Commodity;
import come.tool.Stall.Stall;
import come.tool.Stall.StallPool;
import org.apache.commons.lang.StringUtils;
import org.come.bean.LoginResult;
import org.come.entity.BaiTan;
import org.come.entity.Goodstable;
import org.come.readUtil.ReadBtUtil;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.BotUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StallBotTask implements Runnable {

    @Override
    public void run() {
        Map<Integer, List<BaiTan>> integerListMap = initStallGoods();
//        List<LoginResult> stallBot = RedisCacheUtil.STALL_BOT;

        int x = 0;
        for (Map.Entry<Integer, List<BaiTan>> integerListEntry : integerListMap.entrySet()) {

            Integer key = integerListEntry.getKey();

            if(BotUtil.SELL_BOT.get(key) != null){
                continue;
            }
            List<BaiTan> value = integerListEntry.getValue();
            BaiTan baiTan = value.get(0);
            LoginResult loginResult = BotUtil.addStallBot((long) baiTan.getMapId(), baiTan.getMap_x(), baiTan.getMap_y());
//            RedisCacheUtil.STALL_BOT.add(loginResult);

//            LoginResult loginResult = stallBot.get(x);
            Stall stall = new Stall();
            stall.setStall(StringUtils.isBlank(baiTan.getStallName()) ? loginResult.getRolename() : baiTan.getStallName());
            stall.setMapid(baiTan.getMapId());
            stall.setState(0);
            stall.setX((int)(loginResult.getX()-50));
            stall.setY((int)(loginResult.getY()-120));
            System.out.printf("摆摊BOT信息 地图id = %s 坐标 x = %s 坐标 y = %s%n", stall.getMapid() ,stall.getX() ,stall.getY());
            stall.setId(key);
            stall.setGoodstables(initCommodityList(loginResult.getRole_id(), value));
            Commodity[] pets = new Commodity[5];
            stall.setPets(pets);
            stall.setRoleid(loginResult.getRole_id());
            stall.setRole(loginResult.getRolename());
            StallPool.getPool().addStall(stall, null);
            loginResult.setBooth_id(new BigDecimal(stall.getId()));
            StallPool.getPool().updateState(loginResult.getBooth_id(), StallPool.MANAGE, loginResult.getRole_id());
            BotUtil.SELL_BOT.put(stall.getId(),loginResult.getRolename());
        }

    }

    private Map<Integer,List<BaiTan>> initStallGoods(){
        Map<Integer,List<BaiTan>> stMap = new HashMap<>();
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, BaiTan>> allBaiTan = GameServer.getAllBaiTan();
        for (Map.Entry<Integer, ConcurrentHashMap<Integer, BaiTan>> integerConcurrentHashMapEntry : allBaiTan.entrySet()) {
            ConcurrentHashMap<Integer, BaiTan> value = integerConcurrentHashMapEntry.getValue();
            for (Map.Entry<Integer, BaiTan> integerBaiTanEntry : value.entrySet()) {
                BaiTan value1 = integerBaiTanEntry.getValue();
                List<BaiTan> baiTans = stMap.get(value1.getStallId());
                if(baiTans == null || baiTans.isEmpty()){

                    List<BaiTan> list = new ArrayList<>();
                    list.add(value1);

                    stMap.put(value1.getStallId(),list);
                }else{
                    baiTans.add(value1);
                    stMap.put(value1.getStallId(),baiTans);
                }
            }
        }

        return stMap;
    }

    private Commodity[] initCommodityList(BigDecimal roleId, List<BaiTan> value){
        Commodity[] goodsTables = new Commodity[24];
        for (int i = 0; i < value.size(); i++) {
            goodsTables[i] = createGood(roleId, value.get(i));
        }
        return goodsTables;
    }

    private Commodity createGood(BigDecimal roleId, BaiTan baiTan) {
        Goodstable goodstable = GameServer.getAllGoodsMap().get(baiTan.getGoodsId());
        goodstable.setRole_id(roleId);
        BigDecimal goods_pk = RedisCacheUtil.getGoods_pk();
        goodstable.setUsetime1(baiTan.getUseTime());
        goodstable.setRgid(goods_pk);
        RedisCacheUtil.BOT_RID.add(goods_pk);
        AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
        Commodity commodity = new Commodity();
        commodity.setGood(goodstable);
        commodity.setCurrency("金钱");
        commodity.setMoney(baiTan.getMoney());
        return commodity;
    }
}
