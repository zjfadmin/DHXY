package org.come.action.role;

import come.tool.Calculation.BaseValue;
import come.tool.FightingData.Battlefield;
import come.tool.FightingData.Sepcies_MixDeal;
import come.tool.Role.RolePool;
import come.tool.Role.RoleShow;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import org.come.bean.GetClientUserMesageBean;
import org.come.bean.LoginResult;
import org.come.bean.UseCardBean;
import org.come.entity.*;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.tool.EquipTool;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreatPeople {
    public static RoleSummoning jiqirenpet = new RoleSummoning();

    public static LoginResult creatpeople(String name, int h, boolean mount , int aaa) {

        ChannelHandlerContext cxt = new ChannelHandlerContext() {
            @Override
            public Channel channel() {
                return null;
            }

            @Override
            public EventExecutor executor() {
                return null;
            }

            @Override
            public String name() {
                return "1";
            }

            @Override
            public ChannelHandler handler() {

                return null;
            }

            @Override
            public boolean isRemoved() {
                return false;
            }

            @Override
            public ChannelHandlerContext fireChannelRegistered() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelUnregistered() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelActive() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelInactive() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireExceptionCaught(Throwable throwable) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireUserEventTriggered(Object o) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelRead(Object o) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelReadComplete() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelWritabilityChanged() {
                return null;
            }

            @Override
            public ChannelHandlerContext read() {
                return null;
            }

            @Override
            public ChannelHandlerContext flush() {
                return null;
            }

            @Override
            public ChannelPipeline pipeline() {
                return null;
            }

            @Override
            public ByteBufAllocator alloc() {
                return null;
            }

            @Override
            public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
                return null;
            }

            @Override
            public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
                return false;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
                return null;
            }

            @Override
            public ChannelFuture disconnect() {
                return null;
            }

            @Override
            public ChannelFuture close() {
                return null;
            }

            @Override
            public ChannelFuture deregister() {
                return null;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture disconnect(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture close(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture deregister(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture write(Object o) {
                return null;
            }

            @Override
            public ChannelFuture write(Object o, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object o) {
                return null;
            }

            @Override
            public ChannelPromise newPromise() {
                return null;
            }

            @Override
            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            @Override
            public ChannelFuture newSucceededFuture() {
                return null;
            }

            @Override
            public ChannelFuture newFailedFuture(Throwable throwable) {
                return null;
            }

            @Override
            public ChannelPromise voidPromise() {
                return null;
            }
        };
        String[] mes11 = {"八荒雷动·傲视群雄",
                "天下第一盟",
                "天下第一盟主",
                "天下谁人不识君",
                "君临天下·武道至尊",
                "纵横三界·王者风云",
                "君临天下·震古烁今",
                "比武大会20连冠",
                "乱江山·醉卧美人膝",
                "梦境·杀神",
                "倾天下·始得为君眸",
                "仙路漫漫·有你相伴",
                "月满天·人间小团圆",
                "月为灯·邀君云中舞",
                "中秋佳节·如意平安",
                "群雄霸主",
                "当杀世间一切敌",
                "独孤求败",
                "侠之大者",
                "万古天帝",
                "厉害了我的哥",
                "龙魂",
                "任逍遥",
                "世间无我这般人",
                "万古如长夜",
                "十步杀一人",
                "有钱任性",
                "爷就是有钱",
                "至尊神豪",
                "天下谁人不识君",
                "联赛冠军",
                "联赛亚军",
                "联赛季军",
                "富甲名流",
                "一掷千金",
                "爷是有钱人",
                "厉害了我的哥",
                "初学乍练",
                "初窥门径",
                "略有小成",
                "融会贯通",
                "心领神会",
                "炉火纯青",
                "横扫六合",
                "天下第一",
                "人族大弟子",
                "不灭金身",
                "君临天下",
                "问鼎皇图",
                "有钱就是任性",
                "哥是高富帅",
                "黄图大富翁",
                "金玉天下",
                "珠光宝气",
                "富可敌国",
                "富甲天下"
        };//逍遥生 20001  剑侠客20002
        int l = Battlefield.random.nextInt(5) + 1;
        int p = 0;
        switch (l) {
            case 1:
                p = 20001 + Battlefield.random.nextInt(7);
                break;
            case 2:
                p = 21001 + Battlefield.random.nextInt(7);
                break;
            case 3:
                p = 22001 + Battlefield.random.nextInt(7);
                break;
            case 4:
                p = 23001 + Battlefield.random.nextInt(4);
                break;
            case 5:
                p = 24001 + Battlefield.random.nextInt(4);
                break;

        }
        LoginResult loginResult = new LoginResult();
        loginResult.setRolename(name);
        loginResult.setUser_id(BigDecimal.valueOf(800000 + h));
        loginResult.setSpecies_id(BigDecimal.valueOf(p));
        loginResult.setRace_id(BigDecimal.valueOf(Sepcies_MixDeal.getRace(loginResult.getSpecies_id())));
        loginResult.setRole_id(BigDecimal.valueOf(10000 + h));
//        loginResult.setTitle(mes11[Battlefield.random.nextInt(8)]);//....chenghao....
        loginResult.setLocalname(Sepcies_MixDeal.getLocalName(loginResult.getSpecies_id().intValue()));
        loginResult.setSex(Sepcies_MixDeal.getSex(loginResult.getSpecies_id()) == 0 ? "女" : "男");
        loginResult.setResistance("主-|副-");
        loginResult.setServerMeString("100");
        loginResult.setSavegold(BigDecimal.valueOf(0));
        loginResult.setPower(0);
        loginResult.setHavebaby(0);
        loginResult.setFighting(0);
        loginResult.setMakeloveTime(0);
        loginResult.setMoney(0);//
        loginResult.setDayfirstinorno(0);
        loginResult.setAttachPack(0);
        loginResult.setHjmax(0);
        loginResult.setLowOrHihtpack(0);
        loginResult.setSkin("");
        //坐骑
//        loginResult.setMount_id(Battlefield.random.nextInt(5)+1);
        //转生的 自己改  这个是啥？
//假人等级
        int c = 71;
        String mes111 = lvl2(c);
        String[] lvl1 = mes111.split("-");
        while (Integer.parseInt(lvl1[1]) > 200 || Integer.parseInt(lvl1[1]) < 70) {
            c = 71;
            mes111 = lvl2(c);
            lvl1 = mes111.split("-");
        }
        if(h<100){
            loginResult.setMount_id(Battlefield.random.nextInt(5)+1);
            loginResult.setTurnAround(Integer.parseInt(lvl1[0]));
            loginResult.setGrade(c);
        }else{
            loginResult.setTurnAround(0);
            loginResult.setGrade(1);
        }



        loginResult.setSkills(getskill(loginResult.getSex(), loginResult.getSpecies_id()));
        RoleTable role = AllServiceUtil.getRoleTableService().selectRoleTableByRoleName(loginResult.getRolename());
        loginResult.setHp(new BigDecimal(BaseValue.getRoleValue(loginResult.getRace_id(), 0, 0, 0) + 90000000));
        loginResult.setMp(new BigDecimal(BaseValue.getRoleValue(loginResult.getRace_id(), 0, 0, 0) + 10000000));

        String belongId = AllServiceUtil.getOpenareatableService().selectBelong(loginResult.getServerMeString());
        List<RoleSummoning> pets = new ArrayList<>();
        RoleSummoning pet1 = new RoleSummoning();
        pets.add(pet1);
        loginResult.setServerMeString(belongId);
        {
            List<Present> presents = GameServer.getPresents();
            for (int i = presents.size() - 1; i >= 0; i--) {
                Present present = presents.get(i);
                if (present.getType() == 0) {
                    RoleSummoning pet = GameServer.getPet(present.getId());
                    if (pet != null) {
                        pet.setBasishp(pet.getHp());
                        pet.setBasismp(pet.getMp());
                        pet.setRoleid(loginResult.getRole_id());
                        // pets.add(pet);
                        //AllServiceUtil.getRoleSummoningService().insertRoleSummoning(pet);
                    }
                } else if (present.getType() == 1) {
                    Goodstable goodstable = GameServer.getGood(present.getId());
                    goodstable.setRole_id(loginResult.getRole_id());
                    if (goodstable != null) {
                        if (EquipTool.canSuper(goodstable.getType())) {
                            goodstable.setUsetime(present.getNum());
                            //AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
                        } else {
                            goodstable.setUsetime(1);
                            for (int j = 0; j < present.getNum(); j++) {
                                // AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
                            }
                        }
                    }
                }
            }

            System.out.println(loginResult.getRole_id());

            // 通过新创建角色的id 查询角色信息

            System.out.println(GsonUtil.getGsonUtil().getgson().toJson(loginResult));
            // 返回成功

        }
        List<Goodstable> goods = new ArrayList<>();

        // 获得角色所有的灵宝
        List<Lingbao> lingbaos = new ArrayList<>();
        // 返回该角色所有宝宝
        List<Baby> babys = new ArrayList<>();
        // 获得角色所有坐骑
        List<Mount> mounts = new ArrayList<>();
        // 获取角色所有的伙伴
        List<Pal> pals = new ArrayList<>();
        List<Fly> flys = new ArrayList<>();
        loginResult.setPaysum(BigDecimal.valueOf(0));
        // loginResult.setGrade(441);
        RolePool.addRoleData(loginResult, goods, pets, lingbaos, babys, mounts, flys);
        loginResult.setTitle("天下无双一员");
        //这个就是设置出生地的地方的
        if (h>100){//这个天庭得 你就控制下等级 在70到210之间
            loginResult.setMapid(1208L);
            loginResult.setX(540 + Battlefield.random.nextInt(25) * 30L);
            loginResult.setY(220 + Battlefield.random.nextInt(15) * 30L);
        }else{
            String pointX = "204.139,211.129,217.129,208.123,201.131,189.139,196.150,209.155,215.149,227.148,235.152,237.142,177.157,168.151,167.137,168.126,172.123,189.126,229.123,236.121,225.117,214.119,206.125,201.131,262.130,287.78";

            String[] split = pointX.split(",");
            int x = Battlefield.random.nextInt(split.length-1)+1;
            String str = split[x];
            String[] pointXY = str.split("\\.");
            int xx = Integer.parseInt(pointXY[0])*20;
            int yy = Integer.parseInt(pointXY[1])*20;

            loginResult.setMapid(1207L);
            loginResult.setX(xx + Battlefield.random.nextInt(30) * 10L);
            loginResult.setY(yy + Battlefield.random.nextInt(30) * 10L);
        }
         /*else if (h<=70){
            loginResult.setX(11
            loginResult.setMapid(1208L);70 + Battlefield.random.nextInt(20) * 30L);
            loginResult.setY(2630 + Battlefield.random.nextInt(20) * 30L);
        }else {
            loginResult.setMapid(1207L);
            loginResult.setX(5350 + Battlefield.random.nextInt(20) * 30L);
            loginResult.setY(1970 + Battlefield.random.nextInt(20) * 30L);

        }*/



//        loginResult.setX(4070 + Battlefield.random.nextInt(30) * 50L);
//        loginResult.setY(2770 + Battlefield.random.nextInt(30) * 50L);
        List<RoleShow> roleShows = new ArrayList<>();
        roleShows.add(loginResult.getRoleShow());
        GetClientUserMesageBean getClientUserMesageBean = new GetClientUserMesageBean();
        getClientUserMesageBean.setRoleShows(roleShows);
        roleShows.clear();
        Map<String, ChannelHandlerContext> mapRoleMap = GameServer.getMapRolesMap().get(loginResult.getMapid());

        mapRoleMap.put(loginResult.getRolename(), cxt);

        // 怪物列表
        // gameBean.setMonster(MonsterUtil.getMapMonster(mapid,loginResult.getGang_id()));
        //一些记录数据

        //系统设置
        long time = 1000L * 60L * 60L * 24L * 30L;
        UseCardBean limit = loginResult.getRoleData().getLimit("VIP");
        limit = new UseCardBean("VIP", "VIP", "1", System.currentTimeMillis() + time, "掉落率=1|经验加成=5|召唤兽死亡不掉忠诚,血法|人物死亡惩罚减半");
        loginResult.getRoleData().addLimit(limit);
        limit = new UseCardBean("超级六脉化神丸_月", "超级六脉化神丸_月", null, (Integer.parseInt("20") * time) + System.currentTimeMillis(), null);
        loginResult.getRoleData().addLimit(limit);//超级玉枢返虚丸_月
        limit = new UseCardBean("超级玉枢返虚丸_月", "超级玉枢返虚丸_月", null, (Integer.parseInt("20") * time) + System.currentTimeMillis(), null);
        loginResult.getRoleData().addLimit(limit);
        String mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));

        SendMessage.sendMessageToAllRoles(mes);

        return loginResult;
    }

    public static String getskill(String sex, BigDecimal id) {
        String skill = null;
        if (getRaceSting(id).equals("人"))
            if (sex.equals("男")) {
                skill = "S1005_25000";
                // skill="S1001_25000#1002_25000#1004_25000#1005_25000#1006_25000#1007_25000#1008_25000#1009_25000#1010_2500011#1012_25000#1013_25000#1014_25000#1015_25000";
            } else {
                skill = "S1020_25000";
                //skill="S1016_25000#1017_25000#1019_25000#1020_25000#1006_25000#1007_25000#1008_25000#1009_25000#1010_2500011#1012_25000#1013_25000#1014_25000#1015_25000";
            }
        else if (getRaceSting(id).equals("魔")) {
            if (sex.equals("男")) {
                skill = "S1025_25000";
                //skill="S1021_25000#1022_25000#1024_25000#1025_25000#1026_25000#1027_25000#1028_25000#1029_25000#1030_2500011#1037_25000#1038_25000#1039_25000#1040_25000";
            } else {
                skill = "S1025_25000";
                //skill="S1021_25000#1022_25000#1024_25000#1025_25000#1026_25000#1027_25000#1028_25000#1029_25000#1030_2500011#1032_25000#1033_25000#1034_25000#1035_25000";
            }

        } else if (getRaceSting(id).equals("仙")) {
            if (sex.equals("男")) {
                skill = "S1055_25000";
                //skill="S1041_25000#1042_25000#1044_25000#1045_25000#1046_25000#1047_25000#1048_25000#1049_25000#1050_2500011#1052_25000#1053_25000#1054_25000#1055_25000";
            } else {
                skill = "S1055_25000";
                //skill="S1056_25000#1057_25000#1059_25000#1060_25000#1046_25000#1047_25000#1048_25000#1049_25000#1050_2500011#1052_25000#1053_25000#1054_25000#1055_25000";
            }

        } else if (getRaceSting(id).equals("鬼")) {
            if (sex.equals("男")) {
                skill = "S1070_25000";
                //skill="S1061_25000#1062_25000#1064_25000#1065_25000#1066_25000#1067_25000#1068_25000#1069_25000#1070_2500011#1072_25000#1073_25000#1074_25000#1075_25000";
            } else {
                skill = "S1065_25000";
                //skill="S1061_25000#1062_25000#1064_25000#1065_25000#1076_25000#1077_25000#1078_25000#1079_25000#1080_2500011#1072_25000#1073_25000#1074_25000#1075_25000";
            }

        } else {
            if (sex.equals("男")) {
                skill = "S1085_25000";
                //skill="S1081_25000#1082_25000#1084_25000#1085_25000#1096_25000#1097_25000#1098_25000#1099_25000#1100_2500011#1092_25000#1093_25000#1094_25000#1095_25000";
            } else {
                skill = "S1085_25000";
                //skill="S1081_25000#1082_25000#1084_25000#1085_25000#1086_25000#1087_25000#1088_25000#1089_25000#1090_2500011#1092_25000#1093_25000#1094_25000#1095_25000";
            }
        }
        return skill;
    }

    public static int lvl(int lvl) {
        lvl = lvl - 41;
        int k = 0;
        if (lvl <= 102) {
            k = lvl;
            return k;
        } else if (lvl <= 210) {
            k = (lvl - 102 + 14);
            return k;
        } else if (lvl <= 338) {
            k = (lvl - 210 + 14);
            return k;
        } else if (lvl <= 504) {
            k = (lvl - 338 + 59);
            return k;
        } else {
            k = (lvl - 459 + 139);
            return k;
        }
    }

    public static int lvltrue(int lvl) {
        if (lvl <= 102) {
            return 0;
        } else if (lvl <= 210) {
            return 1;
        } else if (lvl <= 338) {
            return 2;
        } else if (lvl <= 504) {
            return 3;
        } else {
            return 4;
        }
    }

    public static String getRaceSting(BigDecimal se) {
        if (se == null) {
            return "";
        }
        int id = se.intValue();
        if (id >= 20001 && id <= 20010) {
            return "人";
        } else if (id >= 21001 && id <= 21010) {
            return "魔";
        } else if (id >= 22001 && id <= 22010) {
            return "仙";
        } else if (id >= 23001 && id <= 23010) {
            return "鬼";
        } else {
            return "龙";
        }
    }

    public static String lvl2(int lvl) {
        if (lvl <= 102) {
            return "0-" + lvl;
        } else if (lvl <= 210) {
            return "1-" + (lvl - 102 + 14);
        } else if (lvl <= 338) {
            return "2-" + (lvl - 210 + 14);
        } else {
            return lvl <= 459 ? "3-" + (lvl - 338 + 59) : "4-" + (lvl - 459 + 139);
        }
    }

    private static ChannelHandlerContext getChannelHandlerContext() {
        return new ChannelHandlerContext() {
            @Override
            public Channel channel() {
                return null;
            }

            @Override
            public EventExecutor executor() {
                return null;
            }

            @Override
            public String name() {
                return "1";
            }

            @Override
            public ChannelHandler handler() {

                return null;
            }

            @Override
            public boolean isRemoved() {
                return false;
            }

            @Override
            public ChannelHandlerContext fireChannelRegistered() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelUnregistered() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelActive() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelInactive() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireExceptionCaught(Throwable throwable) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireUserEventTriggered(Object o) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelRead(Object o) {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelReadComplete() {
                return null;
            }

            @Override
            public ChannelHandlerContext fireChannelWritabilityChanged() {
                return null;
            }

            @Override
            public ChannelHandlerContext read() {
                return null;
            }

            @Override
            public ChannelHandlerContext flush() {
                return null;
            }

            @Override
            public ChannelPipeline pipeline() {
                return null;
            }

            @Override
            public ByteBufAllocator alloc() {
                return null;
            }

            @Override
            public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
                return null;
            }

            @Override
            public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
                return false;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
                return null;
            }

            @Override
            public ChannelFuture disconnect() {
                return null;
            }

            @Override
            public ChannelFuture close() {
                return null;
            }

            @Override
            public ChannelFuture deregister() {
                return null;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture disconnect(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture close(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture deregister(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture write(Object o) {
                return null;
            }

            @Override
            public ChannelFuture write(Object o, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object o) {
                return null;
            }

            @Override
            public ChannelPromise newPromise() {
                return null;
            }

            @Override
            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            @Override
            public ChannelFuture newSucceededFuture() {
                return null;
            }

            @Override
            public ChannelFuture newFailedFuture(Throwable throwable) {
                return null;
            }

            @Override
            public ChannelPromise voidPromise() {
                return null;
            }
        };
    }

    public static LoginResult creatStallBot(String name, int h, Long mapId, int x, int y) {

        ChannelHandlerContext cxt = getChannelHandlerContext();
        String[] mes11 = {"八荒雷动·傲视群雄",
                "天下第一盟",
                "天下第一盟主",
                "天下谁人不识君",
                "君临天下·武道至尊",
                "纵横三界·王者风云",
                "君临天下·震古烁今",
                "比武大会20连冠",
                "乱江山·醉卧美人膝",
                "梦境·杀神",
                "倾天下·始得为君眸",
                "仙路漫漫·有你相伴",
                "月满天·人间小团圆",
                "月为灯·邀君云中舞",
                "中秋佳节·如意平安",
                "群雄霸主",
                "当杀世间一切敌",
                "独孤求败",
                "侠之大者",
                "万古天帝",
                "厉害了我的哥",
                "龙魂",
                "任逍遥",
                "世间无我这般人",
                "万古如长夜",
                "十步杀一人",
                "有钱任性",
                "爷就是有钱",
                "至尊神豪",
                "天下谁人不识君",
                "联赛冠军",
                "联赛亚军",
                "联赛季军",
                "富甲名流",
                "一掷千金",
                "爷是有钱人",
                "厉害了我的哥",
                "初学乍练",
                "初窥门径",
                "略有小成",
                "融会贯通",
                "心领神会",
                "炉火纯青",
                "横扫六合",
                "天下第一",
                "人族大弟子",
                "不灭金身",
                "君临天下",
                "问鼎皇图",
                "有钱就是任性",
                "哥是高富帅",
                "黄图大富翁",
                "金玉天下",
                "珠光宝气",
                "富可敌国",
                "富甲天下"
        };//逍遥生 20001  剑侠客20002
        int l = Battlefield.random.nextInt(5) + 1;
        int p = 0;
        switch (l) {
            case 1:
                p = 20001 + Battlefield.random.nextInt(10);
                break;
            case 2:
                p = 21001 + Battlefield.random.nextInt(10);
                break;
            case 3:
                p = 22001 + Battlefield.random.nextInt(10);
                break;
            case 4:
                p = 23001 + Battlefield.random.nextInt(6);
                break;
            case 5:
                p = 24001 + Battlefield.random.nextInt(6);
                break;

        }
        LoginResult loginResult = new LoginResult();
        loginResult.setRolename(name);
        loginResult.setUser_id(BigDecimal.valueOf(800000 + h));
        loginResult.setSpecies_id(BigDecimal.valueOf(p));
        loginResult.setRace_id(BigDecimal.valueOf(Sepcies_MixDeal.getRace(loginResult.getSpecies_id())));
        loginResult.setRole_id(BigDecimal.valueOf(3000000 + h));
//        loginResult.setTitle(mes11[Battlefield.random.nextInt(8)]);//....chenghao....
        loginResult.setLocalname(Sepcies_MixDeal.getLocalName(loginResult.getSpecies_id().intValue()));
        loginResult.setSex(Sepcies_MixDeal.getSex(loginResult.getSpecies_id()) == 0 ? "女" : "男");
        loginResult.setResistance("主-|副-");
        loginResult.setServerMeString("100");
        loginResult.setSavegold(BigDecimal.valueOf(0));
        loginResult.setPower(0);
        loginResult.setHavebaby(0);
        loginResult.setFighting(0);
        loginResult.setMakeloveTime(0);
        loginResult.setMoney(0);//
        loginResult.setDayfirstinorno(0);
        loginResult.setAttachPack(0);
        loginResult.setHjmax(0);
        loginResult.setLowOrHihtpack(0);
        loginResult.setSkin("");

        //转生的 自己改  这个是啥？
        int c = Battlefield.random.nextInt(500) + 70;
        String mes111 = lvl2(c);
        String[] lvl1 = mes111.split("-");
        while (Integer.parseInt(lvl1[1]) > 200 || Integer.parseInt(lvl1[1]) < 70) {
            c = Battlefield.random.nextInt(500) + 70;
            mes111 = lvl2(c);
            lvl1 = mes111.split("-");
        }
        loginResult.setGrade(c);
        loginResult.setTurnAround(Integer.parseInt(lvl1[0]));

        loginResult.setSkills(getskill(loginResult.getSex(), loginResult.getSpecies_id()));
        RoleTable role = AllServiceUtil.getRoleTableService().selectRoleTableByRoleName(loginResult.getRolename());
        loginResult.setHp(new BigDecimal(BaseValue.getRoleValue(loginResult.getRace_id(), 0, 0, 0) + 90000000));
        loginResult.setMp(new BigDecimal(BaseValue.getRoleValue(loginResult.getRace_id(), 0, 0, 0) + 10000000));

        String belongId = AllServiceUtil.getOpenareatableService().selectBelong(loginResult.getServerMeString());
        List<RoleSummoning> pets = new ArrayList<>();
        RoleSummoning pet1 = new RoleSummoning();
        pets.add(pet1);
        loginResult.setServerMeString(belongId);
        {
            List<Present> presents = GameServer.getPresents();
            for (int i = presents.size() - 1; i >= 0; i--) {
                Present present = presents.get(i);
                if (present.getType() == 0) {
                    RoleSummoning pet = GameServer.getPet(present.getId());
                    if (pet != null) {
                        pet.setBasishp(pet.getHp());
                        pet.setBasismp(pet.getMp());
                        pet.setRoleid(loginResult.getRole_id());
                        // pets.add(pet);
                        //AllServiceUtil.getRoleSummoningService().insertRoleSummoning(pet);
                    }
                } else if (present.getType() == 1) {
                    Goodstable goodstable = GameServer.getGood(present.getId());
                    goodstable.setRole_id(loginResult.getRole_id());
                    if (goodstable != null) {
                        if (EquipTool.canSuper(goodstable.getType())) {
                            goodstable.setUsetime(present.getNum());
                            //AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
                        } else {
                            goodstable.setUsetime(1);
                            for (int j = 0; j < present.getNum(); j++) {
                                // AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
                            }
                        }
                    }
                }
            }

            System.out.println(loginResult.getRole_id());

            // 通过新创建角色的id 查询角色信息

            System.out.println(GsonUtil.getGsonUtil().getgson().toJson(loginResult));
            // 返回成功

        }
        List<Goodstable> goods = new ArrayList<>();

        // 获得角色所有的灵宝
        List<Lingbao> lingbaos = new ArrayList<>();
        // 返回该角色所有宝宝
        List<Baby> babys = new ArrayList<>();
        // 获得角色所有坐骑
        List<Mount> mounts = new ArrayList<>();
        // 获取角色所有的伙伴
        List<Pal> pals = new ArrayList<>();
        List<Fly> flys = new ArrayList<>();
        loginResult.setPaysum(BigDecimal.valueOf(0));
        // loginResult.setGrade(441);
        RolePool.addRoleData(loginResult, goods, pets, lingbaos, babys, mounts, flys);

        loginResult.setMapid(mapId);


        loginResult.setX((long)x*20);
        loginResult.setY((long)y*20);

        System.out.println("loginResult.x = " + loginResult.getX() + " loginResult.y = " + loginResult.getY() + " mapId = " + mapId);
        List<RoleShow> roleShows = new ArrayList<>();
        roleShows.add(loginResult.getRoleShow());
        GetClientUserMesageBean getClientUserMesageBean = new GetClientUserMesageBean();
        getClientUserMesageBean.setRoleShows(roleShows);
        roleShows.clear();
        Map<String, ChannelHandlerContext> mapRoleMap = GameServer.getMapRolesMap().get(loginResult.getMapid());

        mapRoleMap.put(loginResult.getRolename(), cxt);

        // 怪物列表
        // gameBean.setMonster(MonsterUtil.getMapMonster(mapid,loginResult.getGang_id()));
        //一些记录数据

        //系统设置
        long time = 1000L * 60L * 60L * 24L * 30L;
        UseCardBean limit = loginResult.getRoleData().getLimit("VIP");
        limit = new UseCardBean("VIP", "VIP", "1", System.currentTimeMillis() + time, "掉落率=1|经验加成=5|召唤兽死亡不掉忠诚,血法|人物死亡惩罚减半");
        loginResult.getRoleData().addLimit(limit);
        limit = new UseCardBean("超级六脉化神丸_月", "超级六脉化神丸_月", null, (Integer.parseInt("20") * time) + System.currentTimeMillis(), null);
        loginResult.getRoleData().addLimit(limit);//超级玉枢返虚丸_月
        limit = new UseCardBean("超级玉枢返虚丸_月", "超级玉枢返虚丸_月", null, (Integer.parseInt("20") * time) + System.currentTimeMillis(), null);
        loginResult.getRoleData().addLimit(limit);
//        String mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));
//
//        SendMessage.sendMessageToAllRoles(mes);

        return loginResult;
    }
}
