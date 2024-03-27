package org.come.action.sys;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.come.action.IAction;
import org.come.action.role.CreatPeople;
import org.come.bean.*;
import org.come.entity.*;
import org.come.handler.SendMessage;
import org.come.model.Title;
import org.come.protocol.Agreement;
import org.come.protocol.AgreementUtil;
import org.come.protocol.ParamTool;
import org.come.redis.RedisCacheUtil;
import org.come.redis.RedisControl;
import org.come.server.GameServer;
import org.come.task.MonsterUtil;
import org.come.task.RefreshMonsterTask;
import org.come.tool.Goodtype;
import org.come.tool.WriteOut;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.BangBattle.BangBattlePool;
import come.tool.BangBattle.BangFight;
import come.tool.Battle.BattleData;
import come.tool.Battle.BattleThreadPool;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Role.RoleShow;
import come.tool.Scene.Scene;
import come.tool.Scene.SceneUtil;
import come.tool.Scene.DNTG.DNTGScene;
import come.tool.Scene.LaborDay.LaborScene;
import come.tool.Stall.StallPool;
import come.tool.Title.TitleUtil;
import come.tool.newTeam.TeamBean;
import come.tool.newTeam.TeamRole;
import come.tool.newTeam.TeamUtil;
import org.come.until.PetValid;

public class enterGameAction implements IAction {
    static BigDecimal ZERO = new BigDecimal(0);

    @Override
    public void action(ChannelHandlerContext ctx, String message) {
        // TODO Auto-generated method stub
        String IP = LoginAction.getIP(ctx);
        BigDecimal role_id = null;
        String[] ms;
        LoginResult logins = GameServer.getAllLoginRole().get(ctx);
        if(message.contains("phone")){
           ms = message.split("&");
            role_id= new BigDecimal(ms[0]);
        }else{
           ms = message.split("\\|");
            role_id= new BigDecimal(ms[0]);
        }

        LoginResult roleInfo = null;
        synchronized (GameServer.userLock) {
            RoleData data = RolePool.getRoleData(role_id);
            if (data != null) {//先判断是否为掉线重连或者顶号
                ChannelHandlerContext ctx1 = GameServer.getRoleNameMap().get(data.getLoginResult().getRolename());
                if (ctx1 != null) {
                    roleInfo = GameServer.getAllLoginRole().get(ctx1);
                    GameServer.userDownTwos(ctx1);
                }
            }
            if (roleInfo == null) {
                roleInfo = AllServiceUtil.getRoleTableService().selectRoleID(role_id);
            }
        }
        if (roleInfo == null) {
            return;
        }
        //锁定首发
        String vStr = RedisControl.getVStr(GameServer.area + "_indexPet_" + roleInfo.getRole_id(), roleInfo.getRole_id().toString(), String.class);
        if(StrUtil.isNotBlank(vStr)){
            roleInfo.setPetIndex(true);
        }else{
            roleInfo.setPetIndex(false);
        }
        if (ms.length == 1) {//判断是否是冒名顶替
            ChannelHandlerContext ctx1 = GameServer.getInlineUserNameMap().get(roleInfo.getUserName());
            if (ctx1 == null || ctx1 != ctx) {
                WriteOut.addtxt("冒名顶替:" + GameServer.getSocketUserNameMap().get(ctx), 9999);
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().inlineLoginAgreement());
                return;
            }
        } else {//重连验证
            String userName = ms[1];
            String userPwd = null;
            for (int i = 2; i < ms.length; i++) {
                if (i != 2) {
                    userPwd += "|";
                    userPwd += ms[i];
                } else {
                    userPwd = ms[i];
                }
            }
            if (userPwd == null) {
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().inlineLoginAgreement());
                return;
            }
            UserTable userTable = AllServiceUtil.getUserTableService().findUserByUserNameAndUserPassword(userName, userPwd);
            if (userTable == null || userTable.getUser_id().compareTo(roleInfo.getUser_id()) != 0) {
                WriteOut.addtxt("冒名顶替:" + message, 9999);
                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().inlineLoginAgreement());
                return;
            }
        }
        UUID uuid = UUID.randomUUID();
        String replace = uuid.toString().replace("-", "");
        roleInfo.setMD5(replace);

//        String clientIP = LoginAction.getIP(ctx);
//        IpConfig ipConfig = GameServer.getLoginIps().get(clientIP);

//        //限制登录
//        if(ipConfig == null){
//            ipConfig = new IpConfig();
//            GameServer.getLoginIps().put(clientIP, ipConfig);
//            ipConfig.setCurrCount(ipConfig.getCurrCount() + 1);
//        }else{
//            if(ipConfig.getMaxCount() != GameServer.maxIpCount ? ipConfig.getCurrCount() >= ipConfig.getMaxCount(): ipConfig.getCurrCount() >= GameServer.maxIpCount){
//                SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().inlineLoginAgreement("登录数量超限,请联系管理员开放"));
//                return;
//            }
//            ipConfig.setCurrCount(ipConfig.getCurrCount() + 1);
//        }
        //队伍中角色退出
        TeamBean teamBean = TeamUtil.getTeam(roleInfo.getTroop_id());
        TeamRole teamRole = teamBean != null ? teamBean.getTeamRole(roleInfo.getRole_id()) : null;
        roleInfo.setTeamInfo(null);
        roleInfo.setTroop_id(null);
        if (teamRole != null) {
            roleInfo.setTroop_id(teamBean.getTeamId());
            roleInfo.initTeamRole(teamRole);
            if (teamRole.getState() == -2) {
                LoginResult login = null;
                if (teamBean.isCaptian(roleInfo.getRole_id())) {
                    login = roleInfo;
                } else {
                    String teamName = teamBean.getTeamName();
                    ChannelHandlerContext tCtx = GameServer.getRoleNameMap().get(teamName);
                    login = tCtx != null ? GameServer.getAllLoginRole().get(tCtx) : null;
                }
                if (login != null && login.getMapid().longValue() == roleInfo.getMapid().longValue()) {
                    teamBean.stateCome(teamRole);
                    roleInfo.setTeamInfo(teamBean.getTeamInfo());
                }
            }
        }
        //先判断任务重置 在把下线时间 同步和服务器同步时间
        String lastDownTime = roleInfo.getUptime();
        Reset(roleInfo, System.currentTimeMillis());
        roleInfo.setUserPwd(null);
        //roleInfo.setServerMeString(null);
        if (roleInfo.getFighting() != 0) {
            BattleData battleData = BattleThreadPool.BattleDatas.get(roleInfo.getFighting());
            if (battleData == null) {
                roleInfo.setFighting(0);
            }
        }
        //地图默认称谓
        BangFight bangFight = null;
        if (roleInfo.getMapid() == 3315) {
            roleInfo.setTitle(roleInfo.getGangname() + "帮" + roleInfo.getGangpost());
            bangFight = BangBattlePool.getBangBattlePool().getBangFight(roleInfo.getGang_id());
        } else if (roleInfo.getMapid() == DNTGScene.mapIdZ || roleInfo.getMapid() == DNTGScene.mapIdF) {
            Scene scene = SceneUtil.getScene(SceneUtil.DNTGID);
            if (scene == null || !scene.isEnd() || ((DNTGScene) scene).getRole(roleInfo.getRole_id()) == null) {
                roleInfo.setMapid(1207L);
                roleInfo.setX(4294L);
                roleInfo.setY(2887L);
            }
        }
        // 查询摆摊
        if (!StallPool.getPool().updateState(roleInfo.getBooth_id(), StallPool.OFF, roleInfo.getRole_id())) {
            roleInfo.setBooth_id(null);
        }
        // 根据角色ID查询物品
        List<Goodstable> goods = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(roleInfo.getRole_id());
        /****非武器类装备含有炼器属性**/
        if (goods!=null&&goods.size()>0) {
            for (Goodstable goodstable : goods) {
                if (!Goodtype.Weapons(goodstable.getType())) {
                    String[] v = goodstable.getValue().split("\\|");
                    for (int i = 0; i < v.length; i++) {
                        if (v[i] != null && v[i].contains("炼器属性")) {
                            AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(goodstable.getRgid());
                            ParamTool.ACTION_MAP.get("accountstop").action(GameServer.getInlineUserNameMap().get(logins.getUserName()), logins.getUserName());
                            WriteOut.addtxt("非法物品" + roleInfo.getRolename() + roleInfo.getRole_id() + goodstable.getGoodsname(), 6666);
                        }
                    }
                }
                if (goodstable.getType() == 729) {//非法彩晶石属性
                    String[] v = goodstable.getValue().split("\\|");
                    if (v.length > 8) {
//							AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(goodstable.getRgid());
                        WriteOut.addtxt("非法物品" + roleInfo.getRolename() + roleInfo.getRole_id() + goodstable.getGoodsname(), 6666);
                    }

                }
            }
        }
        // 获得全部召唤兽
        List<RoleSummoning> pets = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRoleID(roleInfo.getRole_id());
        boolean feifa = false;
        if(pets!=null && pets.size()>0) {
            for(RoleSummoning summoning:pets) {
                //检测属性是否非法
                if(PetValid.validLysKx(summoning, roleInfo)){
                    feifa = true;
                    break;
                }
            }

        }
        if(feifa){
            WriteOut.addtxt("玩家 "+roleInfo.getRolename()+" 登录召唤兽数据异常,封号:"+GameServer.getSocketUserNameMap().get(ctx),9999);
            ParamTool.ACTION_MAP.get("accountstop").action(GameServer.getInlineUserNameMap().get(logins.getUserName()), logins.getUserName());
            return;
        }
        // 获得角色所有的灵宝
        List<Lingbao> lingbaos = AllServiceUtil.getLingbaoService().selectLingbaoByRoleID(roleInfo.getRole_id());
        // 返回该角色所有宝宝
        List<Baby> babys = AllServiceUtil.getBabyService().selectBabyByRolename(roleInfo.getRole_id());
        // 获得角色所有坐骑
        List<Mount> mounts = AllServiceUtil.getMountService().selectMountsByRoleID(roleInfo.getRole_id());
        // 获取角色所有的伙伴
        List<Pal> pals = AllServiceUtil.getPalService().selectPalByRoleID(roleInfo.getRole_id());
        //获取所有飞行器
        List<Fly> flys = AllServiceUtil.getFlyService().selectFlyByRoleID(roleInfo.getRole_id());
        //获取多属性数据
        RoleAttribute roleAttribute = AllServiceUtil.getRoleTableService().selectRoleAttributeRoleId(roleInfo.getRole_id());//属性切换
        RoleData roleData = null;
        synchronized (GameServer.userLock) {
            // 添加用户信息，将socket与角色信息匹配
            roleData = RolePool.addRoleData(roleInfo, goods, pets, lingbaos, babys, mounts, flys);
            roleData.setIP(IP);
            GameServer.addOuts(ctx, roleInfo);
        }
        GameServer.LogIn(IP, roleInfo.getRolename(), true);
        if (GameServer.inlineNum.get() > GameServer.inlineMax) {
            GameServer.inlineMax = GameServer.inlineNum.get();
        }

        // 储存修改后的传送角色信息
        List<RoleShow> roleShows = new ArrayList<>();
        roleShows.add(roleInfo.getRoleShow());
        GetClientUserMesageBean getClientUserMesageBean = new GetClientUserMesageBean();
        getClientUserMesageBean.setRoleShows(roleShows);
        String mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));
        roleShows.clear();
        //遍历需要的条件
        BigDecimal gang_id = null;
        // 地图ID转格式
        long mapid = roleInfo.getMapid();
        Map<String, ChannelHandlerContext> mapRoleMap = GameServer.getMapRolesMap().get(roleInfo.getMapid());
		if(mapRoleMap == null) {
			roleInfo.setMapid(1207L);
			mapid = 1207;
			mapRoleMap =GameServer.getMapRolesMap().get(mapid);
		}
        Iterator<Map.Entry<String, ChannelHandlerContext>> entries = mapRoleMap.entrySet().iterator();
        if (mapid == 3000) {
            gang_id = roleInfo.getGang_id();
        }
        while (entries.hasNext()) {
            Entry<String, ChannelHandlerContext> entrys = entries.next();
            ChannelHandlerContext value = entrys.getValue();
            LoginResult loginResult = GameServer.getAllLoginRole().get(value);
            if (value == null || loginResult == null) {
                continue;
            }
            if (gang_id != null && gang_id.compareTo(loginResult.getGang_id()) != 0) {
                continue;
            }
            if (bangFight != null && bangFight.getMap(loginResult.getGang_id()) == null) {
                continue;
            }
            SendMessage.sendMessageToSlef(value, mes);
            //添加新地图角色
            roleShows.add(loginResult.getRoleShow());
        }
//		if (RedisCacheUtil.jiqiren.size()!=0){
//			for (int i=0;i<=RedisCacheUtil.jiqiren.size()-1;i++) {
//				roleShows.add(RedisCacheUtil.jiqiren.get(i).getRoleShow());
//			}
//
//			for (int i=0;i<=RedisCacheUtil.STALL_BOT.size()-1;i++) {
//				roleShows.add(RedisCacheUtil.STALL_BOT.get(i).getRoleShow());
//			}
//		}

		mapRoleMap.put(roleInfo.getRolename(),ctx);
		if (roleInfo.getRolename().equals("123456")){
			CreatPeople.jiqirenpet=pets.get(0);
		}
        enterGameBean gameBean = new enterGameBean();
        gameBean.setLoginResult(roleInfo);
        gameBean.setPrivateData(roleData.getPrivateData());
        //玩家数据
        gameBean.setRoleShows(roleShows);
        // 物品
        gameBean.setList(goods);
        // 角色召唤兽
        pets = roleData.getPackRecord().petOrder(pets);
        gameBean.setRoleSummonings(pets);
        // 角色坐骑
        gameBean.setMounts(mounts);
        // 角色新加飞行器
        gameBean.setFlys(flys);
        // 角色灵宝
        gameBean.setLingbaos(lingbaos);
        // 角色宝宝
        gameBean.setBabys(babys);
        // 角色伙伴
        gameBean.setPals(pals);
        // 多属性
        gameBean.setRoleAttribute(roleAttribute);//属性切换
        // 摆摊列表
        gameBean.setStallBeans(StallPool.getPool().getmap(roleInfo.getMapid().intValue()));
        // 摆摊数据
        if (roleInfo.getBooth_id() != null) {
            gameBean.setStall(StallPool.getPool().getAllStall().get(roleInfo.getBooth_id().intValue()));
        }
        // 怪物列表
        gameBean.setMonster(MonsterUtil.getMapMonster(mapid, roleInfo.getGang_id()));
        //一些记录数据
        gameBean.setPackRecord(roleData.getPackRecord());
        //系统设置
        gameBean.setRoleSystem(roleData.getRoleSystem());
        gameBean.setSceneMsg(SceneUtil.getSceneMsg(roleInfo, 0, roleInfo.getMapid()));
        // 单独返回的消息
        String messages = Agreement.getAgreement().enterGameAgreement(GsonUtil.getGsonUtil().getgson().toJson(gameBean));
        SendMessage.sendMessageToSlef(ctx, messages);
        ParamTool.ACTION_MAP.get(AgreementUtil.friend).action(ctx, roleInfo.getRole_id().toString());
        if (bangFight != null) {
            if (bangFight.BangState == 1) {
                bangFight.addMember(roleInfo.getRolename(), roleInfo.getGang_id());
                bangFight.getzk(roleInfo.getRolename(), true);//送进帮战
            }
        }
        //判断是否处于战斗中
        if (roleInfo.getFighting() != 0) {
            BattleThreadPool.addConnection(ctx, roleInfo.getFighting(), roleInfo.getRolename());
        } else if (StringUtil.isNotEmpty(lastDownTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			String sd = sdf.format(new Date(Long.parseLong(lastDownTime)));
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你上次的离开时间为#G" + sd + "#Y祝你游戏开心#9"));
        }
        //补发数据
        StringBuffer buffer = null;
        if (teamRole != null) {
            if (buffer == null) {
                buffer = new StringBuffer();
            }
            buffer.append(Agreement.getAgreement().team1Agreement(GsonUtil.getGsonUtil().getgson().toJson(teamBean)));
        }
        //添加劳动节图标
        if (LaborScene.I != 0) {
            LaborScene.bindRole(roleInfo);
            if (buffer == null) {
                buffer = new StringBuffer();
            }
            buffer.append(LaborScene.LABOR);
        }
        if (buffer != null) {
            SendMessage.sendMessageToSlef(ctx, buffer.toString());
        }
        /**
         * 新加飞行器升级参数
         */
        StringBuilder sb = new StringBuilder();
        sb.append("flyLvlConfig").append("&").append(GsonUtil.getGsonUtil().getgson().toJson(GameServer.getFlyConfig()));
        SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().getGameConfig(sb.toString()));
        // 发送玩家上线信息
        NChatBean chatBean = new NChatBean();
        StringBuffer msg = new StringBuffer();
        boolean is = true, isSys = false;
        chatBean.setId(5);
        String msg2 = null;
        if (is) {// 获取玩家水陆冠军 #R水陆冠军#G【电风扇】#n，上线了！
            List<LoginResult> results = AllServiceUtil.getRoleTableService().selectSLDH();
            if (results != null) {
                for (int i = 0; i < results.size() && i < 1; i++) {
                    if (results.get(i).getRole_id().compareTo(roleInfo.getRole_id()) == 0) {
                        switch (i) {
                            case 0:
                                isSys = true;
                                msg.append("#c00FFFF水陆大会冠军");
                                break;
                            case 1:
                                msg.append("#c00FFFF水陆大会亚军");
                                break;
                            case 2:
                                msg.append("#c00FFFF水陆大会季军");
                                break;
                        }
                        is = false;
                        msg2 = "#c00FFFF降临，众生膜拜！！！#167";
                    }
                }
            }
        }
        if (is) {
            List<LoginResult> ttList = GameServer.allBangList.get(8);
            if (ttList != null) {
                for (int i = 0; i < ttList.size() && i < 3; i++) {
                    if (ttList.get(i).getRole_id().compareTo(roleInfo.getRole_id()) == 0) {
                        switch (i) {
                            case 0:
                                isSys = true;
                                msg.append("#c00FFFF天梯比武冠军");
                                break;
                            case 1:
                                msg.append("#c00FFFF天梯比武亚军");
                                break;
                            case 2:
                                msg.append("#c00FFFF天梯比武季军");
                                break;
                        }
                        msg2 = "#c00FFFF降临，众生膜拜！！！#167";
                        is = false;
                    }
                }
            }
        }
        if (is) {
            PayvipBean vipBean = GameServer.getVIP(roleInfo.getPaysum().longValue());
            if (vipBean != null && vipBean.getGrade() > 0) {
                msg.append("#Y尊贵的神豪#GVIP");
                msg.append("(");
                msg.append(vipBean.getGrade());
                msg.append(")#23#Y玩家#G");
                is = false;
            }
        }
        if (is) {
            msg.append("#R萌新玩家");
        }

        msg.append("#G【");
        msg.append(roleInfo.getRolename());
        msg.append("】");
        PayvipBean vipBean = GameServer.getVIP(roleInfo.getPaysum().longValue());
        if(vipBean != null && vipBean.getGrade() > 0){
            msg2 = "#Y登录本服务器！#1欢迎您#1";
        }
        if (msg2 != null) {
            msg.append(msg2);
        } else {
            msg.append("#Y,已上线！");
        }

        chatBean.setMessage(msg.toString());
        SendMessage.sendMessageToAllRoles(Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(chatBean)));
        if (isSys) {
            chatBean.setId(9);
            SendMessage.sendMessageToAllRoles(Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(chatBean)));
        }
    }

    /**
     * 重置判断
     * 第一个时间是离线时间
     */
    public static void Reset(LoginResult loginResult, long time2) {
        /** 最近作用时间 */
        try {
            Title title = GameServer.getTitle(loginResult.getTitle());
            if (title != null) {
                if (title.getExist() != null && !title.getExist().equals("")) {
                    if (title.getExist().startsWith("称谓任务")) {
                        int value = Integer.parseInt(title.getExist().split("=")[1]);
                        int max = loginResult.getJQId("S");
                        if (max < value) {
                            loginResult.setTitle(null);
                        }
                    } else if (title.getExist().startsWith("击杀煞星")) {
                        int value = Integer.parseInt(title.getExist().split("=")[1]);
                        double max = loginResult.getKilltype("击杀煞星");
                        if (max < value) {
                            loginResult.setTitle(null);
                        }
                    } else if (title.getExist().startsWith("充值")) {
                        if (AllServiceUtil.getTitletableService().selectRoleTitle(loginResult.getRole_id(), title.getTitlename()) == null) {
                            loginResult.setTitle(null);
                        }
                    } else if (!TitleUtil.isTitle(loginResult.getTitle(), loginResult.getRole_id())) {
                        loginResult.setTitle(null);
                    }
                } else if (AllServiceUtil.getTitletableService().selectRoleTitle(loginResult.getRole_id(), title.getTitlename()) == null) {
                    loginResult.setTitle(null);
                }
            }
            if (loginResult.getMapid() >= 3329 && loginResult.getMapid() <= 3332) {
                loginResult.setMapid(new Long(1207));
                loginResult.setX(new Long(4294));
                loginResult.setY(new Long(2887));
            }
            int type = 0;
            int type2 = 0;
            long time1 = 0;
            if (loginResult.getUptime() != null && !loginResult.getUptime().equals("")) {
                time1 = Long.parseLong(loginResult.getUptime());
            }
//			if (loginResult.getPaysum().longValue()>=30) {
//				ExchangeUtil.addCompensation(loginResult.getRole_id(),time1);	
//			}
            Calendar hc = Calendar.getInstance();
            hc.setTimeInMillis(time2);
            hc.set(Calendar.HOUR_OF_DAY, 0);
            hc.set(Calendar.MINUTE, 0);
            hc.set(Calendar.SECOND, 0);
            hc.set(Calendar.MILLISECOND, 0);
            Calendar qc = Calendar.getInstance();
            qc.setTimeInMillis(time1);
            qc.set(Calendar.HOUR_OF_DAY, 0);
            qc.set(Calendar.MINUTE, 0);
            qc.set(Calendar.SECOND, 0);
            qc.set(Calendar.MILLISECOND, 0);
            //相差日期数
            long cha = (hc.getTime().getTime() - qc.getTime().getTime()) / (1000 * 60 * 60 * 24);
            if (cha > 0) {
                type = 1;
                if (cha > 1) {
                    type2 = 1;
                }//超过一天时间间隔
                int week = qc.get(Calendar.DAY_OF_WEEK);
                for (int i = 0; i < cha; i++) {
                    week++;
                    if (week > 7) {
                        week = 1;
                    } else if (week == 2) {//每逢周一
                        type = 2;
                        break;
                    }
                }
            }
            taskReset(loginResult, type, type2);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        loginResult.setUptime(time2 + "");
    }

    /**
     * 重置
     */
    public static void taskReset(LoginResult loginResult, int type, int type2) {
        if (type == 0) {
            return;
        }
        if (type2 == 0) {//隔天上线
            //如果每日充值为0 中断连冲 //连冲大于等于7天 中断连冲
            if (loginResult.getDayfirstinorno() == 0 || loginResult.getDayandpayorno().longValue() >= 7) {
                loginResult.setDayandpayorno(new BigDecimal(0));
                loginResult.removeVipget("3");
            }
        } else {//超过一天未上线
            //中断连冲
            loginResult.setDayandpayorno(new BigDecimal(0));
            loginResult.removeVipget("3");
        }
        loginResult.setDayfirstinorno(0);//清空连充叠加标志
        loginResult.setDaypaysum(new BigDecimal(0));//清空每日充值
        loginResult.setDaygetorno(new BigDecimal(2));//清空每日特惠领取
        loginResult.setVipDayGet(2);
        loginResult.removeVipget("2");//清空每日充值领取
        loginResult.setConsumeActive(0);//消耗活跃度清零
        loginResult.setTaskComplete(RefreshMonsterTask.ResetTask(loginResult.getTaskComplete(), type));
        loginResult.setDBExp(0);//百分百重置双倍时间
    }

    /**
     * 获取皮肤字符串
     */
    public static String getskin(String skin, List<String> txs, String title, String cb) {
//		S:皮肤 X:特效 P:装饰品 J:足迹
//		S1231|X1230_1|P123_1|J12312
        StringBuffer buffer = new StringBuffer();
        if (skin != null && !skin.equals("")) {
            buffer.append("S");
            buffer.append(skin);
        }
        if (txs != null) {
            for (int i = 0; i < txs.size(); i++) {
                int id = Integer.parseInt(txs.get(i));
                RoleTxBean bean = GameServer.getTxBean(id);
                if (bean != null) {
                    if (buffer.length() != 0) {
                        buffer.append("|");
                    }
//					/**类型（1特效2装饰品3足迹）*/
                    if (bean.getRdType() == 1) {
                        buffer.append("X");
                    } else if (bean.getRdType() == 2) {
                        buffer.append("P");
                    } else {
                        buffer.append("J");
                    }
                    buffer.append(bean.getRdId());
                    if (bean.getRdType() == 1 || bean.getRdType() == 2) {
                        buffer.append("_");
                        buffer.append(bean.getRdStatues() - bean.getRdType());
                    }
                }
            }
        }
        if (title != null) {
            Title te = GameServer.getTitle(title);
            if (te != null && te.getSkin() != null) {
                if (buffer.length() != 0) {
                    buffer.append("|");
                }
                buffer.append("C");
                buffer.append(te.getSkin());
            }
        }
        if (cb != null) {
            if (buffer.length() != 0) {
                buffer.append("|");
            }
            buffer.append("B");
            buffer.append(cb);
        }
        if (buffer.length() == 0) {
            return null;
        }
        return buffer.toString();
    }
}
