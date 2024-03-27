package come.tool.fly;

import come.tool.Role.RolePool;
import come.tool.Stall.AssetUpdate;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.come.action.IAction;
import org.come.action.role.RoleChangeAction;
import org.come.bean.FlyConfig;
import org.come.bean.LoginResult;
import org.come.entity.Fly;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.service.FlyService;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 人物的物品使用
 *
 * @author Administrator
 */
public class FlyAction implements IAction {


    @Override
    public void action(ChannelHandlerContext ctx, String message) {

        //空参过滤
        if (StringUtils.isBlank(message))
            return;

        if (message.startsWith("tz")) {
            String[] vs = message.split("&");
            String[] split = vs[1].split(",");
            for (String s : split) {
                ChannelHandlerContext teamCtx = GameServer.getRoleNameMap().get(s);
                SendMessage.sendMessageToSlef(teamCtx, Agreement.getAgreement().PromptAgreement("#R 队长要开车了#35 请尽快装备飞行器#43"));
            }
        }

        if (!message.contains("=="))
            return;


        String[] params = message.split("==");
        String fid = params[1];
        FlyService flyService = AllServiceUtil.getFlyService();
        Fly fly = flyService.selectFlyByFID(fid);

        if (fly == null)
            return;

        LoginResult loginResult = GameServer.getAllLoginRole().get(ctx);

        //胡乱修改过滤
        if (!fly.getRoleid().toString().equals(loginResult.getRole_id().toString())) {
            return;
        }

        if (message.startsWith("lx")) {
            FlyConfig flyConfig = GameServer.getFlyConfig();
            List<Integer> integerList = flyConfig.getIntegerList();
            Integer lvl = integerList.get(fly.getStairs());
            if (fly.getCurrFlyLevel() < lvl) {
                xl(fly, ctx, loginResult, flyService);
            } else {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("#R您的飞行器等级已满,飞行器修炼失败#75"));
                return;
            }
        } else if (message.startsWith("fx")) {
            if (message.length() != 1) {
                Fly currFly = flyService.selectFlyByFID(loginResult.getFly_id().toString());
                String[] vs = message.split("==");
                if (vs[2].equals("1") || vs[2].equals("2")) {
                    String team = loginResult.getTeam();
                    String[] itemRoles = team.split("\\|");
                    for (String itemRole : itemRoles) {
                        ChannelHandlerContext teamCtx = GameServer.getRoleNameMap().get(itemRole);
                        if (teamCtx != null) {
                            //修改每个角色的飞行数据
                            LoginResult teamItemLoginResult = GameServer.getAllLoginRole().get(teamCtx);
                            Fly teamRoleFly = flyService.selectFlyByFID(teamItemLoginResult.getFly_id().toString());
                            teamItemLoginResult.getRoleShow().setFly_id(teamRoleFly.getFlyId());


                            teamItemLoginResult.getRoleShow().setFlySpeed(BigDecimal.valueOf(0.24).multiply(BigDecimal.valueOf(teamRoleFly.getFlySpeed())).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            teamItemLoginResult.getRoleShow().setFlyType(0);
                            SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(teamItemLoginResult.getRoleShow())));
                        }
                    }
                } else if (vs[2].equals("3")) {
                    //变更飞行状态
                    loginResult.getRoleShow().setFlyType(1);
                    loginResult.getRoleShow().setFlyY(150);
                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
                } else if (vs[2].equals("4")) {
                    loginResult.getRoleShow().setFlyType(-1);
                    loginResult.getRoleShow().setFly_id(0);
                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
                }
            }
//
        } else if (message.startsWith("jl")) {

            String team = loginResult.getTeam();
            if (StringUtils.isNotBlank(team)) {
                String[] itemRoles = team.split("\\|");
                for (String itemRole : itemRoles) {
                    ChannelHandlerContext teamCtx = GameServer.getRoleNameMap().get(itemRole);
                    if (teamCtx != null) {
                        //修改每个角色的飞行数据
                        LoginResult teamItemLoginResult = GameServer.getAllLoginRole().get(teamCtx);
                        Fly teamRoleFly = flyService.selectFlyByFID(teamItemLoginResult.getFly_id().toString());
//                        teamItemLoginResult.getRoleShow().setFly_id(0);
                        teamItemLoginResult.getRoleShow().setFlyType(2);
                        teamItemLoginResult.getRoleShow().setFlySpeed(0.24 + teamRoleFly.getStairs() * 0.01);
                        teamItemLoginResult.getRoleShow().setFlyY(0);
                        SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(teamItemLoginResult.getRoleShow())));
                    }
                }
                return;
            }
//            RoleShow roleShow = new RoleShow();
//            roleShow.setRolename(loginResult.getRolename());
//            SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowFly("jl=" + loginResult.getRolename()));
        } else if (message.startsWith("zb")) {
            if (message.length() != 1) {
                String[] vs = message.split("==");
                loginResult.setFly_id(new BigDecimal(vs[1]));
                RolePool.getRoleData(loginResult.getRole_id()).setFid(Integer.parseInt(vs[1]));
                loginResult.getRoleShow().setEquipmentFlyId(fly.getFlyId());
            }
            SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
            AssetUpdate assetUpdate = new AssetUpdate();
            assetUpdate.updata("Fly=" + loginResult.getFly_id());
            assetUpdate.setMsg("装备飞行器成功#53");
            assetUpdate.setType(AssetUpdate.FLYXL);
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
            SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowFly("zb=" + fly.getFlyId() + "=" + loginResult.getRolename()));
        } else if (message.startsWith("ldz")) {

            //更新飞行器属性
            fly.setCurrLdz(fly.getCurrLdz() + Integer.parseInt(params[2]));
            flyService.updateFly(fly);

            AssetUpdate assetUpdate = new AssetUpdate();
            assetUpdate.setMsg("#Y千年寒石 #G采集成功,增加 #Y灵动值" + params[2] + "#G 点#53");
            assetUpdate.setType(AssetUpdate.FLYXL);
            assetUpdate.setFlys(fly);
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
        } else if (message.startsWith("rlz")) {
            //更新飞行器属性
            fly.setFuel(fly.getFuel() - fly.getStairs());
            AssetUpdate assetUpdate = new AssetUpdate();
            assetUpdate.setMsg("#Y" + fly.getName() + "消耗燃料 #G" + fly.getStairs() + " #Y点#53");
            if (fly.getFuel() <= 0) {
                fly.setFuel(0);
                flightLanding(loginResult, flyService);
                assetUpdate.setMsg("#Y" + fly.getName() + "已经没有燃料了，快去补充燃石吧#46");
            }

            flyService.updateFly(fly);
            assetUpdate.setType(AssetUpdate.FLYXL);
            assetUpdate.setFlys(fly);
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
        } else if (message.startsWith("rs")) {
            Goodstable goodstable = AllServiceUtil.getGoodsTableService().getGoodsByRgID(new BigDecimal(params[2]));
            if (goodstable != null&&goodstable.getRole_id().compareTo(loginResult.getRole_id()) == 0&&goodstable.getUsetime() > 0) {

                //更新飞行器属性
                int fuel = 500;
                if (StringUtils.isNotBlank(goodstable.getValue())) {
                    fuel = Integer.parseInt(goodstable.getValue());
                }
                fly.setFuel(fly.getFuel() + fuel);
                flyService.updateFly(fly);
                AssetUpdate assetUpdate = new AssetUpdate();
                RoleChangeAction.useGood(goodstable, 1);
                AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
                assetUpdate.updata("G" + goodstable.getRgid() + "=" + goodstable.getUsetime());
                assetUpdate.setMsg("#Y" + fly.getName() + "补充燃料 #G" + fuel + " #Y点#53");
                assetUpdate.setType(AssetUpdate.FLYXL);
                assetUpdate.setFlys(fly);
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
            }
        } else if (message.startsWith("sj")) {
            if (loginResult.getRoleShow().getFly_id() != null && loginResult.getRoleShow().getFly_id() != 0) {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("飞行中无法升阶,请先停止飞行#32"));
                return;
            }

            FlyConfig flyConfig = GameServer.getFlyConfig();

            List<Integer> ldzList = flyConfig.getLdzList();
            List<Integer> integerList = flyConfig.getIntegerList();

            //获取升级所需灵动值
            Integer ldz = ldzList.get(fly.getStairs() - 1);
            //获取升级所需灵兔绒
            Integer lvl = integerList.get(fly.getStairs());

            //验证升级所需 灵动值\灵图绒\飞行等级
            if (fly.getCurrLdz() < ldz) {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("#R您的灵动值不足,请先去 #Y月宫#R 采集千年寒石头#32"));
                return;
            }
            List<Goodstable> goodsByRoleID = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(loginResult.getRole_id());

            List<Goodstable> ltrGood = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(loginResult.getRole_id(), new BigDecimal("356"));//灵兔绒ID

            if (ltrGood == null || ltrGood.size() == 0) {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("#R您的灵兔绒不足,飞行器升阶失败#75"));
                return;
            }

            if (fly.getCurrFlyLevel() < lvl) {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("#R您的飞行等级不足,飞行器升阶失败#75"));
                return;
            }

            if (fly.getFlyId() % 5 != 0) {
                Fly fly1 = GameServer.getAllFly().get(fly.getFlyId() + 1);
                fly.setFlyId(fly1.getFlyId());
                fly.setFlySpeed(fly1.getFlySpeed());
                fly.setStairs(fly1.getStairs());
                fly.setSkin(fly1.getSkin());
                fly.setName(fly1.getName());
            } else {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("#R飞行器已经出神入化了,无法继续升阶了#76"));
                return;
            }


            //减去升阶属性
            fly.setCurrLdz(fly.getCurrLdz() - ldz);
            fly.setFlyLevel(fly.getFlyLevel() - lvl);
            flyService.updateFly(fly);
            Goodstable goodstable = ltrGood.get(0);
            goodstable.setUsetime(goodstable.getUsetime() - lvl);
            AllServiceUtil.getGoodsTableService().updateGoodRedis(ltrGood.get(0));
            AssetUpdate assetUpdate = new AssetUpdate();
            assetUpdate.setMsg("#Y飞行器升阶成功#53");
            assetUpdate.setType(AssetUpdate.FLYXL);
            assetUpdate.updata("G" + goodstable.getRgid() + "=" + goodstable.getUsetime());
            assetUpdate.setFlys(fly);
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
        }
    }

    private void flightLanding(LoginResult loginResult, FlyService flyService) {
        String team = loginResult.getTeam();
        if (StringUtils.isNotBlank(team)) {
            String[] itemRoles = team.split("\\|");
            for (String itemRole : itemRoles) {
                ChannelHandlerContext teamCtx = GameServer.getRoleNameMap().get(itemRole);
                if (teamCtx != null) {
                    //修改每个角色的飞行数据
                    LoginResult teamItemLoginResult = GameServer.getAllLoginRole().get(teamCtx);
                    Fly teamRoleFly = flyService.selectFlyByFID(teamItemLoginResult.getFly_id().toString());
//                        teamItemLoginResult.getRoleShow().setFly_id(0);
                    teamItemLoginResult.getRoleShow().setFlyType(2);
                    teamItemLoginResult.getRoleShow().setFlyY(149);
                    teamItemLoginResult.getRoleShow().setFlySpeed(0.24 + teamRoleFly.getStairs() * 0.01);
                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(teamItemLoginResult.getRoleShow())));
                }
            }
            return;
        }
    }

    /**
     * 修炼业务
     *
     * @param fly
     * @param //channelHandlerContext
     * @param loginResult
     */
    private void xl(Fly fly, ChannelHandlerContext ctx, LoginResult loginResult, FlyService flyService) {
        //获取当前升级经验
        Integer requiredTrainingExperience = getRequiredTrainingExperience(fly);
        //获取当前升级金钱
        Integer requiredTrainingMoney = getRequiredTrainingMoney(fly);
        //金钱不足不足
        if (loginResult.getGold().compareTo(new BigDecimal(requiredTrainingMoney)) < 0) {
            SendMessage.sendMessageByRoleName(loginResult.getRolename(), Agreement.getAgreement().PromptAgreement("#R你的金钱不足无法满足本次修炼所需#132"));
            return;
        }
        //经验不足
        if (loginResult.getExperience().compareTo(new BigDecimal(requiredTrainingExperience)) < 0) {
            SendMessage.sendMessageByRoleName(loginResult.getRolename(), Agreement.getAgreement().PromptAgreement("#R你的经验不足无法满足本次修炼所需#132"));
            return;
        }

        //更新飞行器属性
        fly.setCurrFlyLevel(fly.getCurrFlyLevel() + 1);
        flyService.updateFly(fly);

        AssetUpdate assetUpdate = new AssetUpdate();
        assetUpdate.updata("D=-" + requiredTrainingMoney);
        loginResult.setGold(loginResult.getGold().add(new BigDecimal(-requiredTrainingMoney)));

        loginResult.setExperience(new BigDecimal(loginResult.getExperience().longValue() - requiredTrainingExperience));
        assetUpdate.updata("R" + loginResult.getGrade() + "=" + loginResult.getExperience());
        assetUpdate.setMsg("#G修炼成功#23");
        assetUpdate.setType(AssetUpdate.FLYXL);
        assetUpdate.setFlys(fly);
        SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));

    }


    //修炼经验
    private Integer getRequiredTrainingExperience(Fly fly) {
        FlyConfig flyConfig = GameServer.getFlyConfig();
        Integer flylvl = fly.getCurrFlyLevel();
        Integer initLevelUpExperience = flyConfig.getInitLevelUpExperience();
        return flylvl * initLevelUpExperience;
    }

    //所需金钱
    private Integer getRequiredTrainingMoney(Fly fly) {
        FlyConfig flyConfig = GameServer.getFlyConfig();
        Integer flylvl = fly.getCurrFlyLevel();
        Integer initLevelUpMoney = flyConfig.getInitLevelUpMoney();
        return flylvl * initLevelUpMoney;
    }


}
