//package com.gl.controller;
//
//import cn.hutool.core.util.ArrayUtil;
//import com.github.pagehelper.util.StringUtil;
//import com.gl.model.Param;
//import com.gl.model.Result;
//import com.gl.model.UpPetParam;
//import com.gl.model.User;
//import com.gl.service.*;
//import com.gl.token.UserToken;
//import come.tool.Good.NPCDialogBean;
//import come.tool.Role.PrivateData;
//import come.tool.Role.RoleData;
//import come.tool.Role.RolePool;
//import come.tool.Scene.LaborDay.LaborScene;
//import come.tool.Stall.AssetUpdate;
//import io.netty.channel.ChannelHandlerContext;
//import org.apache.commons.httpclient.util.DateUtil;
//import org.apache.commons.lang.math.NumberUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.come.action.monitor.MonitorUtil;
//import org.come.bean.*;
//import org.come.entity.*;
//import org.come.handler.MainServerHandler;
//import org.come.handler.SendMessage;
//import org.come.model.Skill;
//import org.come.protocol.Agreement;
//import org.come.redis.RedisPoolUntil;
//import org.come.server.GameServer;
//import org.come.tool.Arith;
//import org.come.tool.ReadExelTool;
//import org.come.tool.WriteOut;
//import org.come.until.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import redis.clients.jedis.Jedis;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static come.tool.Good.UsePetAction.mathDouble;
////新后台关键处
///**
// * @author Big Green
// * @mail sinmahod@qq.com
// */
//@RestController
//public class AdminController {
//
//    private ConcurrentHashMap<String, Goodstable> nds = new ConcurrentHashMap<String, Goodstable>();
//    private String agentGoodsIds;
//    private static final int PageSize = 10;
//
//
//
//    @UserToken
//    @PostMapping(value = "/api/getUserGood")
//    public Result getUserGood(Param param) {
//        GoodsService service = new GoodsService();
//        List<Goodstable> goods = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(new BigDecimal(param.getValue1()));
//        return ResultFactory.success(goods);
//    }
//
//
//
//    @UserToken
//    @PostMapping(value = "/api/getUserPet")
//    public Result getUserPet(Param param) {
//        List<RoleSummoning> roleSummonings = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRoleID(new BigDecimal(param.getValue1()));
//        return ResultFactory.success(roleSummonings);
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/getUserMount")
//    public Result getUserMount(Param param) {
//        List<Mount> mounts = AllServiceUtil.getMountService().selectMountsByRoleID(new BigDecimal(param.getValue1()));
//        return ResultFactory.success(mounts);
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/getUserLing")
//    public Result getUserLing(Param param) {
//        List<Lingbao> lingbaos = AllServiceUtil.getLingbaoService().selectLingbaoByRoleID(new BigDecimal(param.getValue1()));
//        return ResultFactory.success(lingbaos);
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/updUserLing")
//    public Result updUserLing(Lingbao param) {
//
//        LoginResult loginResult = AllServiceUtil.getRoleTableService().selectRoleByRoleId(param.getRoleid());
//        if (loginResult == null)
//            return ResultFactory.fail("角色不存在！");
//        Lingbao lingbao = AllServiceUtil.getLingbaoService().selectLingbaoByID(param.getBaoid());
//
//        if (lingbao == null)
//            return ResultFactory.fail("灵宝不存在！");
//
//        lingbao.setBaoactive(param.getBaoactive());
//        lingbao.setBaospeed(param.getBaospeed());
//        lingbao.setAssistance(param.getAssistance());
//        lingbao.setGoodskill(param.getGoodskill());
//        lingbao.setLingbaolvl(param.getLingbaolvl());
//        lingbao.setSkills(param.getSkills());
//        lingbao.setTianfuskill(param.getTianfuskill());
//        lingbao.setSkillsum(param.getSkillsum());
//        lingbao.setFusum(param.getFusum());
//        AllServiceUtil.getLingbaoService().updateLingbaoRedis(lingbao);
//        if (loginResult != null) {
//            ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//            if (channelHandlerContext != null) {
//                AssetUpdate update = new AssetUpdate();
//                // 添加返回bean
//                List<Lingbao> lingbaos = new ArrayList<>();
//                lingbaos.add(lingbao);
//                update.setLingbaos(lingbaos);
//                update.setType(AssetUpdate.USEGOOD);
//                update.setMsg(":#R69后台修改灵宝成功#23");
//                // 发送前端取回的东西
//                SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//            }
//        }
//
//        return ResultFactory.success(null);
//    }
//
//    @UserToken
//    @PostMapping(value = "/api/getUserBaby")
//    public Result getUserBaby(Param param) {
//        List<Baby> babys = AllServiceUtil.getBabyService().selectBabyByRolename(new BigDecimal(param.getValue1()));
//        return ResultFactory.success(babys);
//    }
//
//
//
//
//
//
//
//
//    @UserToken
//    @GetMapping(value = "/api/getAgentSendGoods")
//    public Result getAgendSendGoods(HttpServletRequest httpServletRequest) {
//        if (StringUtils.isBlank(agentGoodsIds)) {
//            Properties properties = new Properties();
//            InputStream in = GameServer.class.getClassLoader().getResourceAsStream("agent.properties");
//            try {
//                properties.load(in);// 使用properties对象加载输入流
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            agentGoodsIds = properties.get("agentGoods").toString();
//        }
//        return ResultFactory.success(agentGoodsIds);
//
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/upAgentSendGoods")
//    public Result upAgentSendGoods(Param param) {
//
//        if (StringUtils.isNotBlank(param.getValue1())) {
//            agentGoodsIds = param.getValue1();
//            try {
//                byte[] bs = agentGoodsIds.getBytes();
//                CreateTextUtil.createFile(ReadExelTool.class.getResource("/").getPath() + "agentGoods.txt", bs);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        return ResultFactory.success(true);
//
//    }
//
//    @UserToken
//    @PostMapping(value = "/api/agentSendGoods")
//    public Result agentSendGoods(Param param) {
//        GoodsService service = new GoodsService();
//        if (service.sendGoods(param)) {
//            return ResultFactory.success(null);
//        }
//        return ResultFactory.fail("物品发送失败，请确认玩家或物品是否存在");
//    }
//
//    @UserToken
//    @GetMapping(value = "/api/getMenuList")
//    public Result getMenuList(Param param, HttpServletRequest httpServletRequest) {
//
//        HttpSession session = httpServletRequest.getSession();
//        User user = (User) session.getAttribute(UserService.USERNAME);
//        String up = ReadTxtUtil.readFile1(ReadExelTool.class.getResource("/").getPath() + "manager.db");
//        String[] nameAndPwd = up.split("\\|&\\|");
//        if (user.getUserName().equals(nameAndPwd[0])) {
//            //admin 返回所有权限
//            return ResultFactory.success("admin");
//        } else {
//            //查询代理表.获取指定代理权限
//            return ResultFactory.success("test");
//        }
//    }
//
//
//
//    @UserToken
//    @GetMapping(value = "/api/getOpenAll")
//    public Result getOpenAll(HttpServletRequest httpServletRequest) {
//
//
//        List<Openareatable> openareatables = AllServiceUtil.getOpenareatableService().selectAllOpenareatable();
//        //admin 返回所有权限
//        return ResultFactory.success(openareatables);
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/updUserBaby")
//    public Result updUserBaby(Baby baby) {
//        LoginResult loginResult = AllServiceUtil.getRoleTableService().selectRoleByRoleId(baby.getRoleid());
//        if (loginResult == null)
//            return ResultFactory.fail("角色不存在！");
//
//        Baby dbBaby = AllServiceUtil.getBabyService().selectBabyById(baby.getBabyid());
//
//
//        if (dbBaby == null)
//            return ResultFactory.fail("孩子不存在！");
//        dbBaby.setTalents(baby.getTalents());
//        dbBaby.setQizhi(baby.getQizhi());
//        dbBaby.setNeili(baby.getNeili());
//        dbBaby.setZhili(baby.getZhili());
//        dbBaby.setNaili(baby.getNaili());
//        dbBaby.setMingqi(baby.getMingqi());
//        dbBaby.setDaode(baby.getDaode());
//        dbBaby.setPanni(baby.getPanni());
//        dbBaby.setWanxing(baby.getWanxing());
//        dbBaby.setQingmi(baby.getQingmi());
//        dbBaby.setXiaoxin(baby.getXiaoxin());
//        dbBaby.setWenbao(baby.getWenbao());
//        dbBaby.setBabyage(baby.getBabyage());
//        dbBaby.setOutcome(baby.getOutcome());
//        dbBaby.setQingmi(baby.getQingmi());
//        dbBaby.setNaili(baby.getNaili());
//        AllServiceUtil.getBabyService().updateBaby(dbBaby);
//        if (loginResult != null) {
//            ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//            if (channelHandlerContext != null) {
//                AssetUpdate update = new AssetUpdate();
//                // 添加返回bean
//                List<Baby> babies = new ArrayList<>();
//                babies.add(dbBaby);
//                update.setBabys(babies);
//                update.setType(AssetUpdate.USEGOOD);
//                update.setMsg(":#R69后台修改孩子成功#23");
//                // 发送前端取回的东西
//                SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//            }
//        }
//        return ResultFactory.success(null);
//    }
//
//    @UserToken
//    @PostMapping(value = "/api/updUserMount")
//    public Result updUserMount(Mount mount) {
//        LoginResult loginResult = AllServiceUtil.getRoleTableService().selectRoleByRoleId(mount.getRoleid());
//        if (loginResult == null)
//            return ResultFactory.fail("角色不存在！");
//
//        Mount dbMount = AllServiceUtil.getMountService().selectMountsByMID(mount.getMid());
//        dbMount.setExp(mount.getExp());
//        dbMount.setMountlvl(mount.getMountlvl());
//        dbMount.setPower(mount.getPower());
//        dbMount.setBone(mount.getBone());
//        dbMount.setSpri(mount.getSpri());
//        dbMount.setLive(mount.getLive());
//        dbMount.setProficiency(mount.getProficiency());
//        AllServiceUtil.getMountService().updateMount(dbMount);
//        if (loginResult != null) {
//            ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//            if (channelHandlerContext != null) {
//                AssetUpdate update = new AssetUpdate();
//                // 添加返回bean
//                List<Mount> mounts = new ArrayList<>();
//                mounts.add(dbMount);
//                update.setMounts(mounts);
//                update.setType(AssetUpdate.USEGOOD);
//                update.setMsg(":#R69后台修改坐骑成功#23");
//                // 发送前端取回的东西
//                SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//            }
//        }
//        return ResultFactory.success(null);
//    }
//
////修改召唤兽
//    @UserToken
//    @PostMapping(value = "/api/updUserPet")
//    public Result updUserPet(UpPetParam param) {
//
//        RoleSummoning roleSummoning = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRgID(new BigDecimal(param.getSid()));
//        if (roleSummoning == null) {
//            return ResultFactory.fail("未找到对应的召唤兽！");
//        }
//        RoleSummoning pet = GameServer.getPet(new BigDecimal(roleSummoning.getSummoningid()));
//        roleSummoning.setTurnRount(param.getTurnRount());
//        roleSummoning.setGrowlevel(pet.getGrowlevel());
//        for (int i = 0; i < param.getTurnRount(); i++) {
//            BigDecimal grow = mathDouble(Double.parseDouble(roleSummoning.getGrowlevel()), 0.1);
//            roleSummoning.setGrowlevel(Arith.xiaoshu3(grow.doubleValue()));
//        }
//
//        Integer petLvl = getPetLvl(param.getTurnRount());
//        roleSummoning.setFriendliness(param.getFriendliness());
//        roleSummoning.setGrade(param.getGrade() + petLvl + 1);
//        roleSummoning.setOpenSeal(param.getOpenSeal());
//        roleSummoning.setBone(param.getGrade());
//        roleSummoning.setSpir(param.getGrade());
//        roleSummoning.setPower(param.getGrade());
//        roleSummoning.setSpeed(param.getGrade());
//        List<Goodstable> eqGoods = null;
//        LoginResult loginResult = AllServiceUtil.getRoleTableService().selectRoleByRoleId(roleSummoning.getRoleid());
//
//        if (StringUtils.isNotBlank(param.getSkill())) {
//
//            if (StringUtils.isNotBlank(roleSummoning.getStye())) {
//                eqGoods = new ArrayList<>();
//                String[] v = roleSummoning.getStye().split("\\|");
//                for (int i = 1; i < v.length; i++) {
//                    String[] vs = v[i].split("-");
//                    if (vs.length >= 2) {
//                        Goodstable good = AllServiceUtil.getGoodsTableService().getGoodsByRgID(new BigDecimal(vs[1]));
//                        eqGoods.add(good);
//                    }
//                }
//            }
//
//            for (Goodstable eqGood : eqGoods) {
//                String[] val = eqGood.getValue().split("\\|");
//                int index = -1;
//                for (int i = 0; i < val.length; i++) {
//                    if (val[i].startsWith("觉醒技")) {
//                        index = i;
//                        break;
//                    }
//                }
//                String jxSkill = "";
//                if (index != -1) {
//                    String[] split = val[index].split("&");
//                    split[1] = param.getSkill();
//                    jxSkill = ArrayUtil.join(split, "&");
//                }
//                val[index] = jxSkill;
//                eqGood.setValue(ArrayUtil.join(val, "|"));
//                AllServiceUtil.getGoodsTableService().updateGoodRedis(eqGood);
//                if (loginResult != null) {
//                    ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//                    if (channelHandlerContext != null) {
//                        AssetUpdate update = new AssetUpdate();
//                        // 添加返回bean
//                        List<Goodstable> goodstables = new ArrayList<>();
//                        goodstables.add(eqGood);
//                        update.setGoods(goodstables);
//                        update.setType(AssetUpdate.GIVE);
//                        update.setMsg(":#R召唤兽修改成功#23");
//                        // 发送前端取回的东西
//                        SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//                    }
//                }
//            }
//        }
//        AssetUpdate update = new AssetUpdate();
//
//        if (param.getLx() != null) {
//            roleSummoning.setLingxi(getLx(param.getLx() - 1));
//        }
//
//        List<Goodstable> goodstables = new ArrayList<>();
//        if (StringUtils.isNotBlank(param.getNds())) {
//            String[] split = param.getNds().split("\\|");
//            for (String nd : split) {
//                GameServer.getAllGoodsMap().forEach((k, v) -> {
//                    if (v.getGoodsname().equals(nd)) {
//                        nds.put(nd, v);
//                    }
//                });
//            }
//            //原来的内丹删掉
//            if (StringUtils.isNotBlank(roleSummoning.getInnerGoods())) {
//                for (String s : roleSummoning.getInnerGoods().split("\\|")) {
//                    Goodstable dbGood = AllServiceUtil.getGoodsTableService().getGoodsByRgID(new BigDecimal(s));
//                    dbGood.goodxh(1);
//                    AllServiceUtil.getGoodsTableService().updateGoodRedis(dbGood);
//                    goodstables.add(dbGood);
//                }
//            }
//            String[] ndIds = new String[split.length];
//            //替换新的内丹
//            for (int i = 0; i < split.length; i++) {
//                Goodstable goodstable = nds.get(split[i]);
//                String[] split1 = goodstable.getValue().split("\\|");
//                split1[2] = "内丹等级=4转180";
//                goodstable.setValue(ArrayUtil.join(split1, "|"));
//
//                goodstable.setUsetime(1);
//                goodstable.setRole_id(loginResult.getRole_id());
//                goodstable.setStatus(1);
//                AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
//                ndIds[i] = goodstable.getRgid().toString();
//                goodstables.add(goodstable);
//            }
//            String join = ArrayUtil.join(ndIds, "|");
//            roleSummoning.setInnerGoods(join);
//            update.setGoods(goodstables);
//
//        }
//
//
//        AllServiceUtil.getRoleSummoningService().updateRoleSummoning(roleSummoning);
//        if (loginResult != null) {
//            ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//            if (channelHandlerContext != null) {
//                // 添加返回bean
//                List<RoleSummoning> roleSummonings = new ArrayList<>();
//
//                roleSummonings.add(roleSummoning);
//                update.setPets(roleSummonings);
//                update.setType(AssetUpdate.USEGOOD);
//                update.setMsg(":#R69后台修改宠物成功#23");
//                // 发送前端取回的东西
//                SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//            }
//        }
//        return ResultFactory.success(null);
//    }
//
//
//    /**
//     * 召唤兽转生 点化
//     */
//    /**
//     * 召唤兽转生 点化
//     */
//    public void DHPet(RoleSummoning pet) {
//        int petTurn = pet.getTurnRount();
//        int lvl = pet.getGrade();
//        lvl++;
//        petTurn++;
//
//        //设置这只召唤兽的根骨、灵性、力量、敏捷、经验为0
//        pet.setBone(0);
//        pet.setSpir(0);
//        pet.setPower(0);
//        pet.setSpeed(0);
//        pet.setCalm(0);
//        pet.setExp(new BigDecimal(0));
//        //等级
//        pet.setGrade(lvl);
//        pet.setTurnRount(petTurn);
//        //设置忠诚度为100
//        pet.setFaithful(100);
//        //成长率加0.1
//        BigDecimal grow = mathDouble(Double.parseDouble(pet.getGrowlevel()), 0.1);
//        pet.setGrowlevel(Arith.xiaoshu3(grow.doubleValue()));
//
//        pet.setBasishp(0);
//        pet.setBasismp(0);
//        AllServiceUtil.getRoleSummoningService().updatePetRedis(pet);
//    }
//
//    private String getLx(Integer type) {
//        String lx = "";
//        if (type == 0) {
//            lx = "11001_0|11002_0|11003_0|11004_0|11005_0|11006_0|11007_0|11026_0|11045_0|11046_0" + "|11008_0" + "|11009_0" + "|11010_0" + "|11011_0" + "|11012_0" + "|11013_0" + "|11014_0" + "|11015_0" + "|11016_0" + "|11017_0" + "|11018_0" + "|11019_0" + "|11020_0" + "|11021_0" + "|11022_0" + "|11023_0" + "|11024_0" + "|11025_0" + "|11047_0" + "|11049_0" + "|11051_0" + "|11053_0" + "|11055_0" + "|11057_0" + "|11062_0" + "|11063_0" + "|11061_0" + "|11060_0" + "|11058_0" + "|11059_0" + "|11056_0" + "|11054_0" + "|11052_0" + "|11050_0" + "|11048_0" + "|11027_0" + "|11028_0" + "|11029_0" + "|11031_0" + "|11032_0" + "|11033_0" + "|11034_0" + "|11035_0" + "|11036_0" + "|11030_0" + "|11037_0" + "|11042_0" + "|11039_0" + "|11043_0" + "|11044_0" + "|11040_0" + "|11041_0";
//        } else if (type == 1) {
//            lx = "11003_0|11001_0|11004_0|11005_0|11006_0|11007_0|11008_0|11009_0|11010_0|11011_0|11012_0|11013_0|11016_0|11018_0|11013_0|11015_0|11017_0|11019_0|11020_0|11020_0|11021_0|11022_0|11023_0|11024_0|11025_0";
//        } else if (type == 2) {
//            lx = "11001_0|11004_0|11002_0|11005_0|11007_0|11026_0|11027_0|11028_0|11029_0|11031_0|11033_0|11035_0|11036_0|11032_0|11034_0|11030_0|11037_0|11039_0|11040_0|11041_0|11042_0|11043_0|11044_0";
//        } else if (type == 3) {
//            lx = "11001_0|11004_0|11002_0|11005_0|11046_0|11047_0|11048_0|11049_0|11050_0|11052_0|11054_0|11056_0|11051_0|11053_0|11055_0|11057_0|11058_0|11059_0|11060_0|11061_0|11062_0|11063_0";
//        }
//        String[] lhHead = {"Lx=0", "Lv=0", "Point=0", "Open="};
//        String[] skillIds = lx.split("\\|");
//        String[] lxs = new String[skillIds.length];
//        int count = 0;
//        for (int i = 0; i < skillIds.length; i++) {
//            Skill skill = GameServer.getSkill(skillIds[i].split("_")[0]);
//            lxs[i] = skill.getSkillid() + "_" + (int) (skill.getValue());
//            count += (int) (skill.getValue());
//        }
//        lhHead[2] = "Point=" + count;
//        lhHead[0] = "Lx=" + type;
//        String join = ArrayUtil.join(lxs, "|");
//        String join1 = ArrayUtil.join(lhHead, "&");
//        return join1 + join;
//    }
//
//    public Integer getPetLvl(int zs) {
//        if (zs == 1) {
//            return 100;
//        } else if (zs == 2) {
//            return 221;
//        } else if (zs == 3) {
//            return 362;
//        } else if (zs == 4) {
//            return 543;
//        } else if (zs == 0) {
//            return 0;
//        }
//        return 0;
//    }
//
//
//    @UserToken
//    @PostMapping(value = "/api/updUserGood")
//    public Result updUserGood(Goodstable goodstable) {
//        GoodsService service = new GoodsService();
//        Goodstable dbGoods = AllServiceUtil.getGoodsTableService().getGoodsByRgID(goodstable.getRgid());
//        if (dbGoods != null) {
//            dbGoods.setValue(goodstable.getValue());
//            AllServiceUtil.getGoodsTableService().updateGoodRedis(dbGoods);
//            LoginResult loginResult = AllServiceUtil.getRoleTableService().selectRoleByRoleId(dbGoods.getRole_id());
//            if (loginResult != null) {
//                ChannelHandlerContext channelHandlerContext = GameServer.getRoleNameMap().get(loginResult.getRolename());
//                if (channelHandlerContext != null) {
//                    AssetUpdate update = new AssetUpdate();
//                    // 添加返回bean
//                    List<Goodstable> goodstables = new ArrayList<>();
//                    goodstables.add(dbGoods);
//                    update.setGoods(goodstables);
//                    update.setType(AssetUpdate.GIVE);
//                    update.setMsg(":#R69后台修改物品修改成功#23");
//                    // 发送前端取回的东西
//                    SendMessage.sendMessageToSlef(channelHandlerContext, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(update)));
//                }
//            }
//        }
//
//
//        return ResultFactory.success("111111111");
//    }
//
//    public BackRoleInfo getRole(Param param) {
//
//        String type = param.getValue1();
//        String value = param.getValue2();
//
//        int pageNum = param.getPageNum();
//        int status = param.getStatus();
//        int size = param.getPageSize();
//
//        if (size < 10) {
//            size = PageSize;
//        }
//
//        BackRoleInfo list = null;
//
//        RoleTable roleTable = new RoleTable();
//        if (StringUtils.isNotBlank(param.getValue3()))
//            roleTable.setQid(new BigDecimal(param.getValue3()));
//        else
//            roleTable.setQid(null);
//        roleTable.setStart((pageNum - 1) * size);
//        roleTable.setEnd(pageNum * size);
//
//
//        switch (status) {
//            case 3:
//                roleTable.setUnknown("1");
//                break;
//            case 4:
//                roleTable.setActivity(new Short(1 + ""));
//                break;
//            case 5:
//                roleTable.setActivity(new Short(0 + ""));
//                break;
//            default:
//                roleTable.setActivity(null);
//                break;
//        }
//        if (StringUtil.isNotEmpty(type) && !"undefined".equals(type) && StringUtil.isNotEmpty(value) && !"undefined".equals(value)) {
//            //设置角色名
//            if (type.equals("1") && NumberUtils.isDigits(value)) {
//                roleTable.setRole_id(new BigDecimal(value));
//            } else if (type.equals("2")) {
//                roleTable.setRolename(value);
//            } else if (type.equals("3")) {
//                roleTable.setLocalname(value);
//            }
//        }
//
//        //查询总区域得玩家信息
//        int total = AllServiceUtil.getUserTableService().selectSumForRoleUserHaterNumber(roleTable);
//        //总页数
//        int page = total / size;
//        if (total % size > 0) {
//            page++;
//        }
//        roleTable.setUserString(" Order By role_id ASC");
//        //查询状态下的角色
//        List<RoleTable> listall = AllServiceUtil.getUserTableService().selectSumForRoleUserHaterListyj(roleTable);
//
//        list = new BackRoleInfo();
//        //进行状态实例化
//        for (RoleTable roleInfo : listall) {
//            if (org.apache.commons.lang.StringUtils.isBlank(roleInfo.getRolename())) {
//                continue;
//            }
//            // 玩家状态1、在线 2、下线 3、禁言 4、封号5、未封号  6、未禁言
//            if (GameServer.getRoleNameMap().get(roleInfo.getRolename()) != null) {
//                roleInfo.setStatues("在线");
//            } else {
//                roleInfo.setStatues("离线");
//            }
//            roleInfo.setUnknown(StringUtil.isEmpty(roleInfo.getUnknown()) ? "0" : roleInfo.getUnknown());
//            // 清空密码，不将用户密码传到前端
//            roleInfo.setPassword(null);
//        }
//
//
//        list.setList(listall);
//        list.setPages(page);
//        list.setPageNum(pageNum);
//        list.setTotal(total);
//        return list;
//    }
//
//
//
//
//
//}