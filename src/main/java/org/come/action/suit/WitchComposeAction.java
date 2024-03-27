package org.come.action.suit;

import com.gl.service.GameService;
import come.tool.FightingData.Sepcies_MixDeal;
import come.tool.Stall.AssetUpdate;
import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.action.buy.AddGoodUtil;
import org.come.bean.LoginResult;
import org.come.bean.NpcComposeBean;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.model.*;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.tool.Goodtype;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 巫铸
 *
 */
public class WitchComposeAction implements IAction {

    private static Random random= new Random();
    static Map<String,String> map=new HashMap<>();

    @Override
    public void action(ChannelHandlerContext ctx, String message) {
        LoginResult loginResult=GameServer.getAllLoginRole().get(ctx);
        if (loginResult==null) {return;}
        // 接收客户端发来的数据
        NpcComposeBean npcComposeBean = GsonUtil.getGsonUtil().getgson().fromJson(message, NpcComposeBean.class);
        // 需要操作的装备集合（第一个为主装备）
        List<BigDecimal> goodlist = npcComposeBean.getGoodstables();
        List<BigDecimal> goodstables = new ArrayList<>();
        for (int i = 0; i < goodlist.size(); i++) {
            if (!goodstables.contains(goodlist.get(i))) {
                goodstables.add(goodlist.get(i));
            }
        }
        Goodstable[] goods=new Goodstable[goodstables.size()];
        if (goods.length<4) {return;}
        for (int i = 0; i < goodstables.size(); i++) {
            Goodstable good=AllServiceUtil.getGoodsTableService().getGoodsByRgID(goodstables.get(i));
            if (good==null||good.getUsetime()<=0||good.getRole_id().compareTo(loginResult.getRole_id())!=0) {return;}
            goods[i]=good;

        }

        int zblvl=Integer.parseInt(goods[0].getValue().split("\\|")[0].split("=")[1]);//装备等级
        int kslvl = Integer.parseInt(goods[1].getValue().split("=")[1]);
        boolean up=false;
        if (zblvl>=10&&zblvl<=13) {
            if (kslvl!=8&&kslvl!=9) {return;}
            else if (kslvl==9) {up=true;}
        }else if (zblvl==14) {
            if (kslvl!=9&&kslvl!=10) {return;}
            else if (kslvl==10) {up=true;}
        }else if (zblvl==15) {
            if (kslvl!=10&&kslvl!=11) {return;}
            else if (kslvl==11) {up=true;}
        }else if (zblvl==16) {
            if (kslvl!=11) {return;}
        }else {
            return;
        }

        zblvl = zblvl+(up?1:0);
        int wzcl = 1;
        String typeStr = getEquipType(goods[0].getType());
        if(typeStr == "武器" || typeStr == "衣服" ||  typeStr == "帽子" ){      //武器衣服帽子
            if(zblvl == 15 ){
                wzcl = 4;
            }
            if(zblvl == 16){
                wzcl = 25;
            }
        }else if(typeStr == "鞋子" || typeStr == "项链"){       //鞋子项链
            if(zblvl == 15 ){
                wzcl = 3;
            }
            if(zblvl == 16){
                wzcl = 16;
            }
        }

        for(int i = 0;i<goods.length;i++){
            if (i!=0 && i!=3) {goods[i].goodxh(1);}
            else if(i==3){
                goods[i].goodxh(wzcl);
            }else {goods[i].setUsetime(1);}
        }

        String type=npcComposeBean.getComposetype();
        if (type.equals("巫铸")) {
            Compose_wuzhu(goods, ctx, loginResult);
        }
    }
    /**
     * 我巫铸
     */
    public void Compose_wuzhu(Goodstable[] goods, ChannelHandlerContext ctx, LoginResult login) {
        if (!Goodtype.OrdinaryEquipment(goods[0].getType())) {
            return;
        }

        if (!Goodtype.Ore(goods[1].getType())) {
            return;
        }
        if (goods[2].getType() != 499) {
            return;
        }
        if (!Goodtype.Wuzhu(goods[3].getType())) {
            return;
        }
        int goodid = goods[0].getGoodsid().intValue();//装备物品id
        int zblvl = Integer.parseInt(goods[0].getValue().split("\\|")[0].split("=")[1]);//装备等级
        if(zblvl<15){
            SendMessage.sendMessageByRoleName(login.getRolename(),Agreement.getAgreement().PromptAgreement("#R只有15级以上装备才能巫铸#1"));


        }
        int kslvl = Integer.parseInt(goods[1].getValue().split("=")[1]);
        Goodstable good = null;
        if (zblvl < 10) {
            BigDecimal id = new BigDecimal(goodid - zblvl + kslvl + 1);
            good = GameServer.getGood(id);// 从物品表获取该仙器物品
            BigDecimal nsid = good.getGoodsid();
            good.setRgid(goods[0].getRgid());
            good.setRole_id(login.getRole_id());
            good.setQuality(goods[0].getQuality());
            good.setGoodsid(goods[0].getGoodsid());
            good.setStatus(goods[0].getStatus());
            AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, null, nsid, null);
            AllServiceUtil.getGoodsrecordService().insert(good, null, 1, 8);
            goods[0] = null;
        } else {
            boolean up = false;
            if (zblvl >= 10 && zblvl <= 13) {
                if (kslvl != 8 && kslvl != 9) {
                    return;
                } else if (kslvl == 9) {
                    up = true;
                }
            } else if (zblvl == 14) {
                if (kslvl != 9 && kslvl != 10) {
                    return;
                } else if (kslvl == 10) {
                    up = true;
                }
            } else if (zblvl == 15) {
                if (kslvl != 10 && kslvl != 11) {
                    return;
                } else if (kslvl == 11) {
                    up = true;
                }

            } else if (zblvl == 16) {
                if (kslvl != 11) {
                    return;
                }
            } else {
                return;
            }
            if (up) {
                goodid++;
            }
            BigDecimal id = new BigDecimal(goodid);
            good = GameServer.getGood(id);// 从物品表获取该仙器物品
            BigDecimal nsid = good.getGoodsid();
            good.setRgid(goods[0].getRgid());
            good.setRole_id(login.getRole_id());
            good.setValue(good.getValue() + getNewequip(good, goods, login, zblvl));
            good.setQuality(goods[0].getQuality());
            good.setGoodsid(goods[0].getGoodsid());
            good.setStatus(goods[0].getStatus());
            AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, null, nsid, null);
            AllServiceUtil.getGoodsrecordService().insert(good, null, 1, 8);
            goods[0] = null;
        }
        updata(goods);
        AddGoodUtil.addGood(ctx, good);
    }




    // 获得单个属性
    public static String getAttribute( String decorateType , int i){
        // 获得的属性
        String typees = "";
        // 如果能拆分属性
        if( decorateType.indexOf("|") != -1 ){
            // 拆分属性
            String[] decorateTypes = decorateType.split("\\|");
            int[] a = randomArray(0, decorateTypes.length-1, i);
            for (int j = 0; j < a.length; j++) {
                // 获得的属性
                typees = typees + "|" + decorateTypes[a[j]];
            }
            return typees;
        }else {
            return "|" + decorateType;
        }
    }




    // 区分装备类型
    public static String getEquipType( Long type ){
        // 物品类型
        String goodsType = null;
        if((type==601)||(type==600)){
            goodsType = "帽子";// 帽子
        }else if((type==603)){
            goodsType = "项链";// 项链类型
        }else if((type==605)||(type==604)){
            goodsType = "衣服";// 衣服类型
        }else if((type==800)){
            goodsType = "武器";    // 武器类型
        }else if((type==602)){
            goodsType = "鞋子";// 鞋子类型
        }else if(type==612){// 护身符类型
            goodsType = "护身符";
        }
        return goodsType;
    }
    // 获得装备附加属性
    public String getNewequip(Goodstable goods, Goodstable[] goodstables, LoginResult login, int zblvl) {
        StringBuffer buffer = new StringBuffer();

        if (zblvl == 15 || zblvl == 16) {
            //获取种族
            String raceSting = "人族";
            if (goodstables[3].getType()==810)
                raceSting = "人族";
            if (goodstables[3].getType()==811)
                raceSting = "魔族";
            if (goodstables[3].getType()==812)
                raceSting = "鬼族";
            if (goodstables[3].getType()==813)
                raceSting = "仙族";
            if (goodstables[3].getType()==814)
                raceSting = "龙族";
            if (goodstables[3].getType()==815)
                raceSting = "力族";


            List<WzAlchemy> wzAlchemies = GameServer.getAllWzAlchemy().get(raceSting);
            String equipmentTypeString = Goodtype.getEquipmentTypeString(goodstables[0].getType()) + zblvl;
            ConcurrentHashMap<String, List<Newequip>> sameNewequipMap = GameServer.getRaceSameNewequipMap().get(raceSting);
            // 获得所有的升级物品
//            Map<String, List<Newequip>> sameNewequipMap = GameServer.getSameNewequipMap();
            // 获得装备的类型
            Long type = goods.getType();
            // 获取装备等级
            String value = goods.getValue();
            String[] goodsInfo = value.split("\\|");
            // 装备等级
            String grade = goodsInfo[0].split("=")[1];
            // 物品类型
            String goodsType = getEquipType(type);
            // 获取升级的信息
            List<Newequip> hatList = sameNewequipMap.get(goodsType + grade);
            Newequip newequip = null;
            if (hatList.size() != 0) {
                // 随机调一条属性
                newequip = hatList.get(random.nextInt(hatList.size()));
            } else {
                System.err.println("打造装备报错" + goodsType + grade);
                return "";
            }

            // 获得装备要求
            String types = getEquipRequire(newequip.getEquiptype());
            String[] vs = new String[12];
            List<String> vss = new ArrayList<>();
            for (int i = 0; i < vs.length; i++) {
                String v = null;
                switch (i) {
                    case 0:
                        v = newequip.getEquipfq();
                        break;
                    case 1:
                        v = newequip.getEquipfskb();
                        break;
                    case 2:
                        v = newequip.getEquipkwl();
                        break;
                    case 3:
                        v = newequip.getEquipwla();
                        break;
                    case 4:
                        v = newequip.getEquipkrf();
                        break;
                    case 5:
                        v = newequip.getEquipqsc();
                        break;
                    case 6:
                        v = newequip.getEquipksc();
                        break;
                    case 7:
                        v = newequip.getEquipkxf();
                        break;
                    case 8:
                        v = newequip.getEquipfsh();
                        break;
                    case 9:
                        v = newequip.getEquipwle();
                        break;
                    case 10:
                        v = newequip.getEquipqhp();
                        break;
                    case 11:
                        v = newequip.getEquipqmp();
                        break;
                }
                if (v == null || v.equals("")) {
                    continue;
                }
                // 拆分属性
                String[] attrStrings = v.split("\\|");
                if (attrStrings.length != 1 && random.nextInt(100) <= Integer.parseInt(attrStrings[0])) {
                    // 获得的属性
                    String[] num = attrStrings[1].split("-");
                    // 随机取一个数值
//                    int[] a = randomArray(Integer.parseInt(num[0]), Integer.parseInt(num[1]), 1);
                    double a = SuitComposeAction.getDouble(Double.parseDouble(num[0]), Double.parseDouble(num[1]), 1);

                    // 可能获得的属性
                    String[] attrbutes = attrStrings[2].split("&");
                    String attrs = attrbutes[random.nextInt(attrbutes.length)];
                    //判断重复
                    int k = 0;
                    while (vss.contains(attrs) || (map.get(attrs) != null && vss.contains(map.get(attrs)))) {
                        attrs = attrbutes[random.nextInt(attrbutes.length)];
                        k++;
                        if (k > 10000) {
                            break;
                        }
                    }
                    vss.add(attrs);
//                    if (a == null) {
//                        System.out.println(GsonUtil.getGsonUtil().getgson().toJson(newequip));
//                    }
                    vs[i] = attrs + "=" + a;
                }
            }
            buffer.append(types);
            for (int i = 0; i < vs.length; i++) {
                if (vs[i] == null || vs[i].equals("")) {
                    continue;
                }
                buffer.append("|");
                buffer.append(vs[i]);
            }

            List<WzAlchemy> cwzAlchemies = new ArrayList<>();
            for (WzAlchemy wzAlchemy : wzAlchemies) {
                if (wzAlchemy.getAlchemyBw().equals(equipmentTypeString)) {
                    cwzAlchemies.add(wzAlchemy);
                }
            }
            WzAlchemy wzAlchemy = cwzAlchemies.get(new Random().nextInt(cwzAlchemies.size()));

            Double aDouble = queryHongBao(Double.parseDouble(wzAlchemy.getAlchemysv()), Double.parseDouble(wzAlchemy.getAlchemymv()));
            buffer.append("|巫铸&" + wzAlchemy.getAlchemykey() + "=" + aDouble);
        }

        return buffer.toString();

    }
    public static Double queryHongBao(double MAX, double MIN) {
        Random rand = new Random();
        double result = 0;
        for (int i = 0; i < 10; i++) {
            result = MIN + (rand.nextDouble() * (MAX - MIN));
            result = (double) Math.round(result * 100) / 100;
            System.out.println(result);
        }
        return result;
    }



    // 获取装备要求
    public String getEquipRequire( String equipTypes ){
        // 获得装备要求
        String[] types = equipTypes.split("\\|");
        String type = new String("|");
        if( types.length != 1 ){
            // 拆分装备要求
            String[] requString = types[1].split("&");
            int[] a = randomArray(0,requString.length-1,Integer.parseInt(types[0]));
            for (int j = 0; j < a.length; j++) {
                type = type + requString[a[j]];
            }
            return type;
        }
        return type;
    }
    /**
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     * @param max  指定范围最大值
     * @param min  指定范围最小值
     * @param n  随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min,int max,int n){
        int len = max-min+1;
        if(max < min || n > len){
            return null;
        }
        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }
        int[] result = new int[n];
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(random.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
    /**保存属性*/
    public void updata(Goodstable[] goods){
        for (int i = 0; i < goods.length; i++) {
            if (goods[i]!=null) {
                AllServiceUtil.getGoodsTableService().updateGoodRedis(goods[i]);
                if (i==0) {
                    AllServiceUtil.getGoodsrecordService().insert(goods[i], null, 1, 8);
                }else {
                    long gType=goods[i].getType();
                    if (gType!=212&&!(gType>=497&&gType<=500)&&gType!=505&&gType!=915&&gType!=923) {// 添加记录
                        AllServiceUtil.getGoodsrecordService().insert(goods[i], null, 1, 6);
                    }
                }
            }
        }
    }
}
