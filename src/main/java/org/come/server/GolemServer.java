package org.come.server;

import com.google.gson.Gson;
import come.tool.Battle.BattleMixDeal;
import come.tool.Calculation.BaseValue;
import come.tool.Calculation.RoleReborn;
import come.tool.FightingData.Battlefield;
import come.tool.FightingData.Sepcies_MixDeal;
import come.tool.Good.UsePetAction;
import come.tool.Mixdeal.AnalysisString;
import come.tool.Mixdeal.CreepsMixdeal;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Stall.Commodity;
import come.tool.Stall.Stall;
import come.tool.Stall.StallPool;
import come.tool.newTask.Task;
import come.tool.newTeam.TeamBean;
import come.tool.newTeam.TeamRole;
import come.tool.newTeam.TeamUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import org.apache.commons.lang.StringUtils;
import org.come.action.role.RoleTransAction;
import org.come.action.summoning.SummonPetAction;
import org.come.action.sys.enterGameAction;
import org.come.bean.*;
import org.come.entity.*;
import org.come.handler.SendMessage;
import org.come.model.*;
import org.come.protocol.Agreement;
import org.come.protocol.AgreementUtil;
import org.come.protocol.ParamTool;
import org.come.redis.RedisControl;
import org.come.redis.RedisParameterUtil;
import org.come.task.MapMonsterBean;
import org.come.tool.Arith;
import org.come.tool.Goodtype;
import org.come.until.AllServiceUtil;
import org.come.until.AutoNameUtil;
import org.come.until.GsonUtil;
import org.come.until.StringUtil;

import java.math.BigDecimal;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GolemServer extends Thread{
    public static boolean OPEN; // 机器人开关 true表示运行 false表示关闭

    private ConcurrentHashMap<String, String> userNameMap;// 日在线机器人账号集合
    private ConcurrentHashMap<String, GolemBean> loginGolems; // 在线机器人
    private ConcurrentHashMap<Integer, List<BigDecimal>> taskTeams; // 执行任务的队伍ID

    private String areaName; // 区名称
    private String tuijian;  // 推荐码
    private int num;         // 最大数量
    private int updateNum;   // 更新数量
    private int lvl;         // 初始化等级

    public static GolemServer initAIServer() {
        GolemServer golemServer = null;
        GolemConfig config = GameServer.getGolemConfig();
        golemServer = new GolemServer(config);
        if (OPEN = config.get("开关").equals("开")) {
            golemServer.start();
        }
        return golemServer;
    }


    private GolemServer(GolemConfig config) {
        areaName = config.get("区名称");
        tuijian = config.get("推荐码");
        num = config.getToInt("数量",100);
        updateNum = config.getToInt("更新数量",5);
        lvl = config.getToInt("初始等级",70);

        userNameMap = new ConcurrentHashMap<>();
        loginGolems = new ConcurrentHashMap<>();
        taskTeams = new ConcurrentHashMap<>();
    }

    /** 分配机器人 GolemScript **/
    public void assignGolem() {
        int num = this.num - loginGolems.size();
        if (num > 0) {
            if (num > this.updateNum) num = this.updateNum;
            List<UserTable> userTables = AllServiceUtil.getUserTableService().selectGolemUser();
            String[] userNames = new String[num];
            if (userTables != null) {
                int x = 0;
                for (int i = 0; i < userTables.size() && x < num; i++) {
                    if (!userNameMap.keySet().contains(userTables.get(i).getUsername())) {
                        userNames[x++] = userTables.get(i).getUsername();
                    }
                }
            }
            for (int i = 0; i < userNames.length; i++) {
                String userName = userNames[i];
                if (userName == null)
                    userName = StringUtil.generateUniqueString(15, 4, i + loginGolems.size());
                addGolem(userName);
            }
        }
    }

    /** 添加机器人 **/
    private void addGolem(String userName) {
        try {
            ChannelHandlerContext ctx = new MyChannelHandlerContext(userName);
            // 登录
            LoginUserBean loginUserBean = new LoginUserBean(userName, userName);
            UserTable userTable = AllServiceUtil.getUserTableService().findUserByUserNameAndUserPassword(loginUserBean.getUsername(), loginUserBean.getPassword());
            if (userTable == null) {
                userTable = new UserTable();
                userTable.setType(1);
                userTable.setUsername(userName);
                userTable.setUserpwd(userName);
                userTable.setSafety(userName);
                userTable.setTuiji(tuijian);
                // 注册
                String mes = GsonUtil.getGsonUtil().getgson().toJson(userTable);
                ParamTool.ACTION_MAP.get(AgreementUtil.register).action(ctx, mes);
                userTable = AllServiceUtil.getUserTableService().findUserByUserNameAndUserPassword(loginUserBean.getUsername(), loginUserBean.getPassword());
                if (userTable == null) {return;}
            }
            LoginUserBean userBean = new LoginUserBean();
            userBean.setUsername(userName);
            userBean.setPassword(userName);
            ParamTool.ACTION_MAP.get(AgreementUtil.ACCOUNT_GOLOGIN).action(ctx, GsonUtil.getGsonUtil().getgson().toJson(userBean));

            List<LoginResult> allLogin = AllServiceUtil.getRoleTableService().selectRoleByUserid(userTable.getUser_id(), BigDecimal.ZERO.subtract(userTable.getUser_id()));
            boolean init = false;
            if (allLogin == null || allLogin.size() <= 0) {
                // 创建角色
                LoginResult createRole = initGolemRoleInfo(areaName, userTable.getUser_id(), "100");
                createRole.setGolem(true);
                String mes = GsonUtil.getGsonUtil().getgson().toJson(createRole);
                ParamTool.ACTION_MAP.get(AgreementUtil.createrole).action(ctx, mes);
                allLogin = AllServiceUtil.getRoleTableService().selectRoleByUserid(userTable.getUser_id(), BigDecimal.ZERO.subtract(userTable.getUser_id()));
                init = true;
            }

            // 进入游戏
            if (allLogin != null && allLogin.size() > 0) {
                LoginResult loginResult = allLogin.get(0);
                String roleId = loginResult.getRole_id().toString();
                ParamTool.ACTION_MAP.get(AgreementUtil.enterGame).action(ctx, roleId);

                loginResult = GameServer.getAllLoginRole().get(ctx);
                loginResult.setGolem(true);
                userNameMap.put(userName,loginResult.getRolename());

                GolemBean golemBean = new GolemBean(loginResult, ctx);
                golemBean.setIdleTime(System.currentTimeMillis());
                RoleData roleData = RolePool.getRoleData(loginResult.getRole_id());
                if (loginResult.getGrade() == 0) {
                    loginResult.setGrade(lvl);
                }
                if (StringUtils.isBlank(roleData.getPrivateData().getSkills())) {
                    roleData.getPrivateData().setSkills("S", RoleTransAction.changeSkill(null, loginResult.getSpecies_id(), 10000));
                    roleData.setSkills(BaseValue.reSkill(roleData.getPrivateData(),loginResult));
                }

                if (init) {
                    // 发放物资
                    loginResult.setGolemLvl(-1);
                    provideMaterials(loginResult);
                    provideFly(loginResult);
                }
                List<Fly> flys = AllServiceUtil.getFlyService().selectFlyByRoleID(loginResult.getRole_id());
                if (flys == null || flys.size()<=0) {
                    provideFly(loginResult);
                }
                golemBean.flyAction(1);
                loginGolems.put(loginResult.getRolename(), golemBean);
                golemBean.initRobotsMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 机器人下线 **/
    public void deletGolem(String golemName) {
        deletGolem(loginGolems.get(golemName));
    }

    private long count = 0;
    /** 其他操作 **/
    public void otherOperation() {
        for (GolemBean golemBean : loginGolems.values()) {
            if (golemBean.isFighting()) continue;
            if (count % 12 == 0) {
                cleanUpPack(golemBean);
            }
            if (count % 2 == 0) {
        //更新golemBean的属性
        updateProperty(golemBean, golemBean.getTeamBean() == null);
        //更新golemBean的召唤物属性
        updatePetProperty(golemBean.getSummoningId());
        //提供golemBean的登录结果
        provideMaterials(golemBean.getLoginResult());
        //更新golemBean的机器人地图
        golemBean.updateRobotsMap();
            }
        }
        count++;
    }

    /** 0点重置 **/
    public void reset() {
        for (GolemBean golemBean : loginGolems.values()) {
            try {
                // 重置击杀野怪数量限制
                golemBean.initRobotsMap();
                // 重置摆摊验证
                golemBean.setStallState(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clearUserNames();
    }

    /** 结束战斗 **/
    public void endFighting(String golemName, String value) {
        GolemBean golemBean = loginGolems.get(golemName);
        if (golemBean == null) return;
        golemBean.getScript().endFighting();
        if (StringUtils.isNotBlank(value)) {
            System.out.println(golemName + ":结束战斗 - " + value);
        }
    }

    /** 添加队伍完成次数 **/
    public void addSumLimit(String golemName, int taskSetId) {
        GolemBean golemBean = loginGolems.get(golemName);
        if (golemBean == null) return;
        GolemScript script = golemBean.getScript();
        if (script == null) return;
        script.completeTask(taskSetId);
    }

    /** 添加队伍击杀怪物次数 **/
    public void addSumLimit(String golemName, String robotId, boolean isLeading) {
        try {
            GolemBean golemBean = loginGolems.get(golemName);
            if (golemBean == null) return;
            golemBean.updateRobotsMap(Integer.valueOf(robotId));

            if (isLeading) {
                String robot = golemBean.getRobot();
                if (StringUtils.isNotBlank(robot)) {
                    String[] vals = robot.split("-");
                    if (vals[0].equals(robotId)) {
                        System.out.println(golemBean.getRoleName() + ":击杀怪物【" + vals[1] + "】");
                        golemBean.setRobot(null);
                        golemBean.getScript().createScript(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 根据机器人名称获取机器人 **/
    public GolemBean getGolemByName(String golemName) {
        return loginGolems.get(golemName);
    }

    public LoginResult getLoginResultByName(String golemName) {
        GolemBean golemBean = loginGolems.get(golemName);
        if (golemBean == null) return null;
        return golemBean.getLoginResult();
    }

    /** 添加队伍任务记录 **/
    private void addTaskTeams(BigDecimal teamId, Integer taskSetId) {
        List<BigDecimal> teams = taskTeams.get(taskSetId);
        if (teams == null) {
            taskTeams.put(taskSetId, teams = new ArrayList<>());
        }
        if (!teams.contains(teamId)) {
            teams.add(teamId);
        }
    }

    /** 移除队伍任务记录 **/
    private boolean removeTaskTeams(BigDecimal teamId, GolemActive active) {
        if (active != null) {
            List<BigDecimal> teams = taskTeams.get(active.getTasksetId());
            if (teams != null && teams.contains(teamId)) {
                return teams.remove(teamId);
            }
        }
        return false;
    }

    /** 队伍转生监听 **/
    public void transListener(TeamBean teamBean) {
        List<GolemBean> list = new ArrayList<>();
        List<TeamRole> teams = teamBean.getTeams();
        int size = 0;
        for (int i = teamBean.getTeams().size()-1; i > 0; i--) {
            TeamRole teamRole = teamBean.getTeams().get(i);
            GolemBean golem = loginGolems.get(teamRole.getName());
            if (golem == null) {
                continue;
            }
            int lvl = BattleMixDeal.lvlint(golem.getGrade());
            int zs = BattleMixDeal.lvltrue(golem.getGrade());
            if ((zs == 0 && lvl == 102)
                    || (zs == 1 && lvl == 122)
                    || (zs == 2 && lvl == 142)
                    || (zs == 3 && lvl == 180)) {
                list.add(golem);
            }
            size++;
        }
        if (list.size() == size) {
            for (int i = 0; i < list.size(); i++) {
                updateProperty(list.get(i), true);
            }
        }
    }

    /**
     * 队伍配置监听
     *
     * @param teamBean 队伍星系
     * @param applyTeam 申请的组队信息
     * @param is 任务目标不满足条件时是否重新获取其他任务目标
     * @return
     */
    public boolean teamListener(TeamBean teamBean, TeamRole applyTeam, boolean is) {
        GolemBean golemBean = loginGolems.get(teamBean.getTeamName());
        GolemActive active = golemBean.getTask(); // 获取当前有效的任务目标
        // 队长验证
        if (active == null) {
            if (applyTeam == null) {
                if (teamBean !=null) {
                    // 清除任务队伍记录
                    removeTaskTeams(teamBean.getTeamId(), golemBean.getTarget());
                    if (is) {
                        // 获取新的任务
                        active = golemBean.getNewTask();
                        if (active != null) {
                            addTaskTeams(teamBean.getTeamId(), active.getTasksetId());
                            golemBean.getScript().createScript(active);
                            return false;
                        }
                    }

                    // 解散队伍
                    List<TeamRole> teams = teamBean.getTeams();
                    for (int i = 0; i < teams.size(); i++) {
                        GolemBean golem = GameServer.golemServer.getGolemByName(teams.get(i).getName());
                        if (golem == null) continue;
                        golem.setIdleTime(System.currentTimeMillis());
                    }
                    teamBean.dismissTeam();
                    return false;
                } else {
                    // 没有任务 设置为闲置
                    golemBean.setIdleTime(System.currentTimeMillis());
                }
            }
            return false;
        }

        Map<String, Integer> speciesMap = new HashMap<>();
        speciesMap.put(RoleTransAction.getSepciesN(golemBean.getSpeciesId()), 1);

        // 成员验证
        List<TeamRole> teamRoles = teamBean.getTeams();
        for (int i = teamRoles.size() - 1; i > 0; i--) {
            TeamRole teamRole = teamRoles.get(i);
            GolemBean golem = getGolemByName(teamRole.getName());
            boolean isAdd;
            if (golem == null) {
                ChannelHandlerContext ctx = GameServer.getRoleNameMap().get(teamRole.getName());
                LoginResult loginResult = null;
                if (ctx != null) {
                    loginResult = GameServer.getAllLoginRole().get(ctx);
                }
                // 玩家只做等级验证
                isAdd = loginResult != null && teamRole.getState() >= 0 && BattleMixDeal.isLvl(loginResult.getGrade(), active.getLvls()) == null;
            } else {
                // 假人队员任务验证
                isAdd = teamRole.getState() >= 0 && golem.taskConditionValidate(active);
            }

            if (isAdd) {
                // 记录种族数量
                String race = RoleTransAction.getSepciesN(teamRole.getSpeciesId());
                Integer count = speciesMap.get(race);
                if (count == null) {
                    count = 0;
                }
                count++;
                speciesMap.put(race, count);
            } else {
                // 不在线、不满足任务条件的队员踢出队伍
                teamBean.removeTeamRole(teamRole.getRoleId());
            }
        }

        if (applyTeam == null) {
            // 队伍条件验证
            if (teamRoles.size() < 5) {
                addTaskTeams(teamBean.getTeamId(), active.getTasksetId());
                return false;
            } else {
                removeTaskTeams(teamBean.getTeamId(), active);
                return true;
            }
        } else {
            // 队伍申请条件验证
            if (teamRoles.size() < 5) {
                if (BattleMixDeal.lvlint(applyTeam.getGrade()) >= lvl) {
                    String race = RoleTransAction.getSepciesN(applyTeam.getSpeciesId());
                    if (speciesMap.get(race) == null) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /** 分配怪物给机器人 **/
    public void assignedRobot(MapMonsterBean monsterBean) {
        try {
            List<GolemBean> golemBeans = loginGolems.values().stream()
                    .filter(golemBean -> isAssignedRobot(golemBean, monsterBean.getRobotid()))
                    .collect(Collectors.toList());
            if (golemBeans.size() == 0) {
                return;
            }
            GolemBean golemBean = golemBeans.get(GameServer.random.nextInt(golemBeans.size()));

            StringBuffer buffer = new StringBuffer();
            buffer.append(monsterBean.getRobotid());
            buffer.append("-");
            buffer.append(monsterBean.getRobotname());
            buffer.append("-");
            buffer.append(monsterBean.getI());
            buffer.append("-");
            buffer.append(monsterBean.getMap());
            buffer.append("-");
            buffer.append(monsterBean.getX());
            buffer.append("-");
            buffer.append(monsterBean.getY());
            golemBean.setRobot(buffer.toString());
            GolemScript script = golemBean.getScript();
            if (script.isEnd()) {
                // 没有执行脚本的立即执行脚本
                script.createScript(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 修改机器人属性点 **/
    public void updateProperty(GolemBean golemBean, boolean isTrans) {
        try {
            LoginResult loginResult = golemBean.getLoginResult();
            int lvl = BattleMixDeal.lvlint(loginResult.getGrade());
            int zs = BattleMixDeal.lvltrue(loginResult.getGrade());
            // 转生
            if (isTrans) {
                if ((zs == 0 && lvl == 102)
                        || (zs == 1 && lvl == 122)
                        || (zs == 2 && lvl == 142)
                        || (zs == 3 && lvl == 180)) {
                    zs++;
                    loginResult.setGrade(loginResult.getGrade() + 1);
                    lvl = BattleMixDeal.lvlint(loginResult.getGrade());
                    int l = this.lvl - lvl;
                    if (l > 0) loginResult.setGrade(loginResult.getGrade() + l);
                    loginResult.setTurnAround(zs);

                    RoleData roleData=RolePool.getRoleData(loginResult.getRole_id());
                    roleData.setGoodMax(24 * 6);
                    if (zs <= 3) {// 重置转生抗性
                        String v = RoleReborn.reborn(roleData.getPrivateData().getSkill("S"), roleData.getPrivateData().getBorn());
                        roleData.getPrivateData().setBorn(v);
                        roleData.setBorns(BaseValue.reborn(roleData.getPrivateData().getBorn()));
                    }
                    Integer skilled = 10000 + (zs <= 3 ? zs * 5000 : 15000);
                    roleData.getPrivateData().setSkills("S", RoleTransAction.changeSkill(roleData.getPrivateData().getSkill("S"), loginResult.getSpecies_id(), skilled));
                    roleData.getPrivateData().setSkills("F", null);
                    roleData.setSkills(BaseValue.reSkill(roleData.getPrivateData(),loginResult));
                    loginResult.setHp(new BigDecimal(0));
                    loginResult.setMp(new BigDecimal(0));

                    lvl = BattleMixDeal.lvlint(loginResult.getGrade());
                    flyUpLvl(golemBean, zs + 1); // 飞行器升级
                }
            }
            String race = RoleTransAction.getSepciesN(loginResult.getSpecies_id());
            switch (race) {
                case "男人":
                case "女人":
                    loginResult.setBone(lvl * 4);
                    loginResult.setSpir(lvl * 4);
                    loginResult.setPower(lvl * 1);
                    loginResult.setSpeed(lvl * 3);
                    break;
                case "男魔":
                case "女魔":
                    loginResult.setBone(lvl * 3);
                    loginResult.setSpir(lvl * 3);
                    loginResult.setPower(lvl * 1);
                    loginResult.setSpeed(lvl * 5);
                    break;
                case "男仙":
                case "女仙":
                case "女鬼":
                    loginResult.setBone(lvl * 3);
                    loginResult.setSpir(lvl * 4);
                    loginResult.setPower(lvl * 1);
                    loginResult.setSpeed(lvl * 4);
                    break;
                case "男鬼":
                    loginResult.setBone(lvl * 5);
                    loginResult.setSpir(lvl * 5);
                    loginResult.setPower(lvl * 1);
                    loginResult.setSpeed(lvl * 1);
                    break;
                case "男龙":
                case "女龙":
                    loginResult.setBone(lvl * 4);
                    loginResult.setSpir(lvl * 4);
                    loginResult.setPower(lvl * 5);
                    loginResult.setSpeed(lvl * 4);
                    break;
            }
            loginResult.setCalm(lvl * 1);
            if (zs == 4)
                loginResult.setSpeed(lvl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<GolemBean> tempList = new ArrayList<>();
    @Override
    public void run() {
        while (true) {
            if (!GameServer.OPEN) {
                if (loginGolems.size() > 0) {
                    try {
                        Thread.sleep(500); // 每秒执行一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    tempList.addAll(loginGolems.values());
                    Collections.shuffle(tempList);
                    for (GolemBean golemBean : tempList) {
                        try {
                            // 摆摊验证 正在摆摊
                            if (golemBean.getStall() != null && golemBean.getStallTime() != null && golemBean.getStallTime() > System.currentTimeMillis()) {
                                continue;
                            }

                            GolemScript script = golemBean.getScript();
                            if (script == null) golemBean.setScript(script = new GolemScript(golemBean));

                            int probability = loginGolems.size() / 3;
                            if (GameServer.random.nextInt(loginGolems.size()) < probability) continue;
                            if (script.isEnd()) { // 脚本停止
                                handle(golemBean);
                            } else { // 执行脚本
                                if (golemBean.isFighting()) continue;
                                String[] names = golemBean.getTeam().split("\\|");
                                List<GolemBean> golemBeans = new ArrayList<>();
                                for (int i = 0; i < names.length; i++) {
                                    GolemBean golem = loginGolems.get(names[i]);
                                    if (golem != null) golemBeans.add(golem);
                                }
                                script.handle(golemBeans.toArray(new GolemBean[golemBeans.size()]));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tempList.clear();
                }
            }
        }
    }

    private void handle(GolemBean golemBean) {
        Integer idleTime = golemBean.getIdleTime();
        if (idleTime == null) {// 忙碌状态
            try {
                GolemActive active = golemBean.getTask();
                if (active != null) {
                    TeamBean teamBean = golemBean.getTeamBean();
                    if (active.getType() == 2) {
                        // 单人任务处理
                        if (teamBean != null) {
                            if (golemBean.getRoleName().equals(teamBean.getTeamName())) {
                                // 是队长把其他队员踢出
                                List<TeamRole> teams = teamBean.getTeams();
                                for (int i = 1; i < teams.size(); i++) {
                                    teamBean.removeTeamRole(teams.get(i).getRoleId());
                                }
                            } else {
                                // 离开队伍
                                teamBean.removeTeamRole(golemBean.getRoleId());
                            }
                        }
                        golemBean.getScript().createScript(true);
                        return;
                    } else {
                        if (teamBean != null) {
                            // 组队任务处理
                            if (golemBean.getRoleName().equals(teamBean.getTeamName())) {
                                handleTeamApply(golemBean);// 处理队伍申请
                                teamBean.removeApply();// 清空队伍申请
                                // 队伍是否满足条件
                                if (teamListener(teamBean, null, true)) {
                                    golemBean.getScript().createScript(true);
                                } else if (golemBean.getTeamBean() != null) {
                                    if (!golemBean.nearby()) {
                                        golemBean.getScript().createScript(active);
                                    } else {
                                        // 如果没有解散队伍并一直不满足条件则考虑解散队伍
                                        List<BigDecimal> teamIds = taskTeams.get(golemBean.getTaskSetId());
                                        int count = 0;
                                        if (teamIds != null) {
                                            for (int i = 0; i < teamIds.size(); i++) {
                                                TeamBean team = TeamUtil.getTeam(teamIds.get(i));
                                                if (team == null) {
                                                    removeTaskTeams(teamIds.get(i),active);
                                                    continue;
                                                }
                                                if (team.getTeamSize() < 5) {
                                                    TeamRole teamRole = new TeamRole();
                                                    teamRole.setRoleId(golemBean.getRoleId());
                                                    teamRole.setName(golemBean.getRoleName());
                                                    teamRole.setGrade(golemBean.getGrade());
                                                    teamRole.setSpeciesId(golemBean.getSpeciesId());
                                                    if (team.getTeamSize()>=teamBean.getTeamSize()&&teamListener(team, teamRole, false)) {
                                                        count++;
                                                    }
                                                }
                                            }
                                        }
                                        if (!teamIds.contains(teamBean.getTeamId())) {
                                            addTaskTeams(teamBean.getTeamId(),active.getTasksetId());
                                        }
                                        if (count >= 1) {
                                            disbandTeam(golemBean);
                                        }
                                    }
                                }
                            }
                            return;
                        }
                    }
                }
                golemBean.setIdleTime(System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // 闲置状态
            GolemActive active = golemBean.getTask();
            if (active == null) {
                // 任务分配
                active = golemBean.getNewTask();
                if (active == null) {
                    // 解散队伍
                    if (golemBean.getTeamBean() != null) disbandTeam(golemBean);
                    // 是否满足摆摊条件
                    GolemConfig config = GameServer.getGolemConfig();
                    if (config.get("摆摊开关").equals("开")) {
                        Stall stall = stallValidate(golemBean, config);
                        if (stall != null && stall.getState() == StallPool.PREPARE) {
                            // 是否在摆摊位置
                            if (stall.pointContains(golemBean.getMapId(), golemBean.getX(), golemBean.getY())) {
                                // 摆摊
                                stall(golemBean, config, stall);
                                int minute = config.getToInt("摆摊时间", 180);
                                golemBean.setStallTime(System.currentTimeMillis() + minute * 60 * 1000);
                            } else {
                                // 移动到摆摊位置
                                golemBean.getScript().createScript(stall.getMapid(), stall.getX(), stall.getY(), true);
                                golemBean.getScript().setAccurate(false);
                            }
                            return;
                        }
                    }
                    if (config.getIdlePoints() != null) {// 有指定闲置区域
                        if (!config.idleContains(golemBean.getMapId(), golemBean.getX(), golemBean.getY())) {
                            // 移动到指定闲置区域
                            Point point = config.getIdleRandomPoint();
                            golemBean.getScript().createScript(point.getMapId(), point.getX(), point.getY(), true);
                            golemBean.getScript().setAccurate(false);
                            return;
                        }
                    }
                    Integer flyType = golemBean.getLoginResult().getRoleShow().getFlyType();
                    if (flyType != null && flyType == 1) {
                        golemBean.flyAction(2);
                    }
                    // 等待掉线
                    if (idleTime >= 10) deletGolem(golemBean);
                } else {
                    // 移动到NPC处
                    golemBean.getScript().createScript(active);
                    if (active.getType() == 2) {
                        golemBean.setIdleTime(null); // 设置为忙碌
                    }
                }
            } else {
                TeamBean teamBean = golemBean.getTeamBean();
                if (teamBean == null) {
                    List<BigDecimal> teamIds = taskTeams.get(active.getTasksetId());
                    List<BigDecimal> roleIds = new ArrayList<>();
                    if (teamIds != null) {
                        if (idleTime <= 2) {
                            TeamRole teamRole = new TeamRole();
                            teamRole.setRoleId(golemBean.getRoleId());
                            teamRole.setName(golemBean.getRoleName());
                            teamRole.setGrade(golemBean.getGrade());
                            teamRole.setSpeciesId(golemBean.getSpeciesId());
                            for (int i = 0; i < teamIds.size(); i++) {
                                TeamBean team = TeamUtil.getTeam(teamIds.get(i));
                                if (team == null) {
                                    teamIds.remove(teamIds.get(i));
                                    continue;
                                }
                                // 队伍是否可以加入
                                if (teamListener(team, teamRole, false)) {
                                    roleIds.add(team.getTeams().get(0).getRoleId());
                                }
                            }
                        }
                    }
                    roleIds = roleIds.stream()
                            .sorted((roleId1, roleId2) -> teamCompare(roleId1, roleId2))
                            .collect(Collectors.toList());
                    if (roleIds.size() == 0) {// 创建队伍
                        creatTeam(golemBean, active.getTasksetId());
                    } else {
                        for (int i = 0; i < roleIds.size(); i++) {
                            teamApply(golemBean.getCtx(), roleIds.get(i).toString());
                        }
                    }
                } else {
                    // 如果是队长则检查执行任务的队伍ID列表是否存在该队伍
                    if (active.getType() != 2 && golemBean.getRoleName().equals(teamBean.getTeamName())) {
                        addTaskTeams(teamBean.getTeamId(), active.getTasksetId());
                    }
                    golemBean.setIdleTime(null); // 设置为忙碌
                }
            }
        }
    }

    /** 可申请队伍排序 队伍人数多的排前面 **/
    private int teamCompare(BigDecimal roleId1, BigDecimal roleId2) {
        TeamBean teamRole1 = TeamUtil.getTeamRole(roleId1);
        if (teamRole1 == null) {
            return 1;
        }
        TeamBean teamRole2 = TeamUtil.getTeamRole(roleId2);
        if (teamRole2 == null) {
            return -1;
        }
        return teamRole1.getTeams().size() > teamRole2.getTeams().size() ? -1 : teamRole1.getTeams().size() == teamRole2.getTeams().size() ? 0 : 1;
    }

    /** 分配怪物条件 队长并且成员都满足条件的队长有获得怪物分配的资格 **/
    private boolean isAssignedRobot(GolemBean golemBean, int robot) {
        String[] names = golemBean.getTeam().split("\\|");
        // 不是队长
        if (!names[0].equals(golemBean.getRoleName())) {
            return false;
        }
        // 队伍成员不满足五人
        if (names.length < 5) {
            return false;
        }
        // 队长有未击杀的怪物
        if (StringUtils.isNotBlank(golemBean.getRobot())) {
            return false;
        }
        for (int i = 0; i < names.length; i++) {
            GolemBean golem = loginGolems.get(names[i]);
            if (golem == null) {
                continue;
            }
            // 成员不满足击杀要求
            if (!golem.isAssignedRobot(robot)) {
                return false;
            }
        }
        return true;
    }

    /** 创建队伍 **/
    private void creatTeam(GolemBean golemBean, Integer taskSetId) {
        try {
            ChannelHandlerContext cxt = golemBean.getCtx();
            ParamTool.ACTION_MAP.get(AgreementUtil.team1).action(cxt, "");
            TeamBean teamBean = TeamUtil.getTeamRole(golemBean.getRoleId());
            if (teamBean != null) {
                addTaskTeams(teamBean.getTeamId(), taskSetId);
                golemBean.setIdleTime(null); // 设置为忙碌
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** 申请队伍 **/
    private void teamApply(ChannelHandlerContext cxt, String roleId) {
        ParamTool.ACTION_MAP.get(AgreementUtil.team2).action(cxt, roleId);
    }

    /** 处理组队申请 **/
    private void handleTeamApply(GolemBean golemBean) {
        TeamBean teamBean = golemBean.getTeamBean();
        List<TeamRole> applyTeams = teamBean.getApplyTeams();
        if (applyTeams != null) {
            for (int i = 0; i < applyTeams.size(); i++) {
                if (teamListener(teamBean, applyTeams.get(i), false)) {
                    ParamTool.ACTION_MAP.get(AgreementUtil.team5).action(golemBean.getCtx(), "A" + applyTeams.get(i).getRoleId());
                } else {
                    ParamTool.ACTION_MAP.get(AgreementUtil.team5).action(golemBean.getCtx(), "R" + applyTeams.get(i).getRoleId());
                }

            }
        }
    }

    /** 离开、解散队伍 **/
    private void disbandTeam(GolemBean golemBean) {
        try {
            TeamBean teamBean = TeamUtil.getTeamRole(golemBean.getRoleId());
            if (teamBean == null) return;
            if (teamBean.getTeamName().equals(golemBean.getRoleName())) {
                // 队长取消任务
                cancelTask(golemBean);
                // 解散队伍
                List<TeamRole> teams = teamBean.getTeams();
                for (int i = 0; i < teams.size(); i++) {
                    GolemBean golem = loginGolems.get(teams.get(i).getName());
                    if (golem == null) continue;
                    golem.setIdleTime(System.currentTimeMillis());
                }
                teamBean.dismissTeam();
            } else {
                GolemBean golem = loginGolems.get(teamBean.getTeamName());
                // 队长取消任务
                cancelTask(golem);
                // 设置闲置
                golem.setIdleTime(System.currentTimeMillis());
                golemBean.setIdleTime(System.currentTimeMillis());

                teamBean.removeTeamRole(golemBean.getRoleId());
            }
            ParamTool.ACTION_MAP.get(AgreementUtil.team5).action(golemBean.getCtx(), "D");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 队长取消任务**/
    private void cancelTask(GolemBean golemBean) {
        // 删除执行任务的队伍ID
        GolemActive target = golemBean.getTarget();
        if (target != null) {
            removeTaskTeams(golemBean.getTeamId(), target);
            if (target.getType() != 0) {
                RoleData roleData = RolePool.getRoleData(golemBean.getRoleId());
                Task task = roleData.getTaskBySetId(target.getTasksetId());
                if (task != null) ParamTool.ACTION_MAP.get(AgreementUtil.TASKN).action(golemBean.getCtx(), "E" + task.getTaskId());
            }
            golemBean.getScript().setEnd(true);
        }
    }

    /** 清空机器人登录信息 **/
    private void clearUserNames() {
        try {
            List<String> userNames = new ArrayList<>();
            for (String userName : userNameMap.keySet()) {
                if (loginGolems.get(userNameMap.get(userName)) == null) userNames.add(userName);
            }
            for (int i = 0; i < userNames.size(); i++) userNameMap.remove(userNames.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 修改召唤兽属性 **/
    private void updatePetProperty(BigDecimal petId) {
        try {
            if (petId == null) return;
            RoleSummoning pet = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRgID(petId);
            if (pet == null) return;
            int lvl=BattleMixDeal.petLvlint(pet.getGrade());
            int zs =BattleMixDeal.petTurnRount(pet.getGrade());
            // 转生
            if ((zs == 0 && lvl == 100) || (zs == 1 && lvl == 120) || (zs == 2 && lvl == 140) || (zs == 3 && lvl == 180)) {
                zs++;
                pet.setGrade(pet.getGrade() + 1);
                pet.setTurnRount(zs);
                //成长率加0.1
                BigDecimal grow = UsePetAction.mathDouble(Double.parseDouble(pet.getGrowlevel()), 0.1);
                pet.setGrowlevel(Arith.xiaoshu3(grow.doubleValue()));

                pet.setBasishp(0);
                pet.setBasismp(0);
                pet.setExp(new BigDecimal(0));

                lvl = BattleMixDeal.petLvlint(pet.getGrade());
            }

            pet.setBone(lvl*2);
            pet.setSpir(lvl*1);
            pet.setPower(lvl*4);
            pet.setSpeed(lvl*2);
            if (zs==4)
                pet.setSpeed(lvl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 发放物资 **/
    private void provideMaterials(LoginResult login) {
        // 发放等级物资  发放并且装备  坐骑携带指定技能  召唤兽携带指定技能
        try {
            ConcurrentHashMap<String, List<GolemDraw>> golemDrawMap = GameServer.getAllGolemDraw();
            for (String value : golemDrawMap.keySet()) {
                int specifyLvl = AnalysisString.lvldirection(value); // 所需等级
                int receiveLvl = login.getGolemLvl();          // 已领取的最高等级
                int currentLvl = login.getGrade();             // 当前等级
                // 所需等级 > 已领取的最高等级 && 所需等级 <= 当前等级
                if (specifyLvl > receiveLvl && specifyLvl <= currentLvl) {
                    List<GolemDraw> golemDraws = golemDrawMap.get(value);
                    for (int i = 0; i < golemDraws.size(); i++) {
                        GolemDraw golemDraw = golemDraws.get(i);
                        // 匹配角色
                        if (golemDraw.isMatching(login.getSpecies_id())) {
                            switch (golemDraw.getType()) {
                                case "装备":
                                    provideEquip(login, golemDraw);
                                    break;
                                case "召唤兽":
                                    providePet(login, golemDraw);
                                    break;
                            }
                        }
                    }
                    login.setGolemLvl(specifyLvl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 发放装备 **/
    private void provideEquip(LoginResult login, GolemDraw golemDraw) {
        try {
            Goodstable goods = GameServer.getGood(golemDraw.getId());
            int type = Goodtype.EquipmentType(goods.getType());
            if (type == -1) return;
            goods.setRole_id(login.getRole_id());
            AllServiceUtil.getGoodsTableService().insertGoods(goods);

            RoleData data = RolePool.getRoleData(login.getRole_id());
            BigDecimal equipId = data.getGoodEquip()[type];
            if (equipId != null) {
                Goodstable equip = AllServiceUtil.getGoodsTableService().getGoodsByRgID(equipId);
                if (equip != null) {
                    // 删除旧的装备
                    AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(equip.getRgid());
                }
            }

            // 穿戴新的装备
            goods.setStatus(1);
            RedisControl.insertKeyT(RedisParameterUtil.GOODS, goods.getRgid().toString(), goods);
            RedisControl.insertController(RedisParameterUtil.GOODS, goods.getRgid().toString(), RedisControl.CALTER);
            String key = RedisParameterUtil.GOODSST + "_" + goods.getRole_id().toString();
            RedisControl.deletrValue(key, "0", goods.getRgid().toString());
            RedisControl.insertListRedis(key, "1", goods.getRgid().toString());
            data.CEquip(goods.getRgid(), type, true);

            if (Goodtype.EquipmentType(goods.getType()) == 0 || Goodtype.EquipmentType(goods.getType()) == 12) {
                String eSkin=null;  // 角色皮肤

                int newGW=0;
                long weaponSkin = 0;
                if (data.getGoodEquip()[0] != null) {
                    Goodstable weapon = AllServiceUtil.getGoodsTableService().getGoodsByRgID(data.getGoodEquip()[0]);
                    if (weapon != null) {
                        weaponSkin = CreepsMixdeal.good(Integer.parseInt(goods.getSkin()));
                    }
                }

                if (data.getGoodEquip()[14] != null) {
                    Goodstable weaponGW = AllServiceUtil.getGoodsTableService().getGoodsByRgID(data.getGoodEquip()[14]);
                    if (weaponGW != null&&StringUtils.isNumeric(weaponGW.getValue())) {
                        newGW = Integer.parseInt(weaponGW.getValue());
                    }
                }
                if (weaponSkin != 0) {
                    long se = login.getSpecies_id().longValue();
                    if ((weaponSkin == 1 && se == 20001 || weaponSkin == 2 && se == 20001 || weaponSkin == 1 && se == 20002 ||
                            weaponSkin == 3 && se == 20002 || weaponSkin == 4 && se == 20003 || weaponSkin == 5 && se == 20003 ||
                            weaponSkin == 9 && se == 20004 || weaponSkin == 8 && se == 20004 || weaponSkin == 10 && se == 20005 ||
                            weaponSkin == 7 && se == 20005 || weaponSkin == 10 && se == 20006 || weaponSkin == 12 && se == 20006 ||
                            weaponSkin == 1 && se == 20007 || weaponSkin == 5 && se == 20007 || weaponSkin == 1 && se == 20008 ||
                            weaponSkin == 10 && se == 20008 || weaponSkin == 2 && se == 20009 || weaponSkin == 6 && se == 20009 ||
                            weaponSkin == 8 && se == 20010 || weaponSkin == 1 && se == 20010 || weaponSkin == 12 && se == 21001 ||
                            weaponSkin == 7 && se == 21001 || weaponSkin == 10 && se == 21002 || weaponSkin == 13 && se == 21002 ||
                            weaponSkin == 10 && se == 21003 || weaponSkin == 12 && se == 21003 || weaponSkin == 9 && se == 21004 ||
                            weaponSkin == 10 && se == 21004 || weaponSkin == 7 && se == 21005 || weaponSkin == 1 && se == 21005 ||
                            weaponSkin == 14 && se == 21006 || weaponSkin == 8 && se == 21006 || weaponSkin == 12 && se == 21007 ||
                            weaponSkin == 4 && se == 21007 || weaponSkin == 10 && se == 21008 || weaponSkin == 11 && se == 21008 ||
                            weaponSkin == 10 && se == 21009 || weaponSkin == 4 && se == 21009 || weaponSkin == 14 && se == 21010 ||
                            weaponSkin == 9 && se == 21010 || weaponSkin == 12 && se == 2200 || weaponSkin == 3 && se == 22001 ||
                            weaponSkin == 14 && se == 22002 || weaponSkin == 1 && se == 22002 || weaponSkin == 7 && se == 22003 ||
                            weaponSkin == 14 && se == 22003 || weaponSkin == 10 && se == 22004 || weaponSkin == 5 && se == 22004 ||
                            weaponSkin == 7 && se == 22005 || weaponSkin == 16 && se == 22005 || weaponSkin == 1 && se == 22006 ||
                            weaponSkin == 12 && se == 22006 || weaponSkin == 12 && se == 22007 || weaponSkin == 14 && se == 22007 ||
                            weaponSkin == 11 && se == 22008 || weaponSkin == 16 && se == 22008 || weaponSkin == 1 && se == 22009 ||
                            weaponSkin == 13 && se == 22009 || weaponSkin == 16 && se == 22010 || weaponSkin == 17 && se == 22010 ||
                            weaponSkin == 1 && se == 23001 || weaponSkin == 10 && se == 23001 || weaponSkin == 12 && se == 23002 ||
                            weaponSkin == 5 && se == 23002 || weaponSkin == 13 && se == 23003 || weaponSkin == 6 && se == 23003 ||
                            weaponSkin == 9 && se == 23004 || weaponSkin == 8 && se == 23004 || weaponSkin == 17 && se == 23005L ||
                            weaponSkin == 11 && se == 23005 || weaponSkin == 11 && se == 23006 || weaponSkin == 16 && se == 23006 ||
                            weaponSkin == 1 && se == 24001 || weaponSkin == 6 && se == 24001 || weaponSkin == 12 && se == 24002 ||
                            weaponSkin == 10 && se == 24002 || weaponSkin == 18 && se == 24003 || weaponSkin == 11 && se == 24003 ||
                            weaponSkin == 9 && se == 24004 || weaponSkin == 3 && se == 24004 || weaponSkin == 18 && se == 24005 ||
                            weaponSkin == 12 && se == 24005 || weaponSkin == 1 && se == 24006 || weaponSkin == 17 && se == 24006)) {
                        weaponSkin += 18;
                    }
                    if (se != 20011 &&se != 20012
                            &&se != 21011&&se != 21012
                            &&se != 22011&&se != 22012
                            &&se!=23008 &&se!=24007
                            &&se!=24008&&se!=23007) {
                        eSkin = ((weaponSkin << 32) | se) + "";
                    }
                }

                String cb=null;     // 翅膀皮肤
                if (data.getGoodEquip()[12] != null) {
                    Goodstable wing = AllServiceUtil.getGoodsTableService().getGoodsByRgID(data.getGoodEquip()[12]);
                    if (wing != null) {
                        cb = wing.getSkin();
                    }
                }

                if (newGW != 0) {
                    Integer roleWeapon = Sepcies_MixDeal.getRoleWeapon(newGW);
                    if (roleWeapon != null) {
                        if (roleWeapon == 0) {
                            long weapon = newGW % 100;
                            long se = login.getSpecies_id().longValue();
                            eSkin = ((weapon << 32) | se) + "|G" + newGW;
                        } else if (roleWeapon == 1){
                            eSkin = String.valueOf(newGW);
                        }
                    }
                }
                login.setSkin(enterGameAction.getskin(eSkin, data.getPackRecord().getPutTX(), login.getTitle(), cb));
                // 群发给所有人
                String sendMes=Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(login.getRoleShow()));
                SendMessage.sendMessageToMapRoles(login.getMapid(),sendMes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 发放召唤兽 **/
    private void providePet(LoginResult login, GolemDraw golemDraw) {
        try {
            RoleSummoning pet = GameServer.getPet(golemDraw.getId());
            if (pet == null) return;

            pet.setBasishp(pet.getHp());
            pet.setBasismp(pet.getMp());
            // 设置忠诚
            pet.setFaithful(100);
            pet.setGrade(0);
            pet.setTurnRount(0);
            pet.setBone(0);
            pet.setSpir(0);
            pet.setPower(0);
            pet.setSpeed(0);
            pet.setCalm(0);
            pet.setDragon(0);
            pet.setSpdragon(0);
            pet.setAlchemynum(0);
            pet.setExp(new BigDecimal(0));
            pet.setOpenSeal(1);
            pet.setRoleid(login.getRole_id());

            String yb = pet.getResistance();
            if (yb == null|| yb.equals("")) {
                int p = GameServer.random.nextInt(SummonPetAction.kxs.length);
                int p2 = GameServer.random.nextInt(SummonPetAction.kxs.length);
                while (p2 == p) {
                    p2 = GameServer.random.nextInt(SummonPetAction.kxs.length);
                }
                pet.setResistance(SummonPetAction.kxs[p] + "|" + SummonPetAction.kxs[p2]);
            }
            AllServiceUtil.getRoleSummoningService().insertRoleSummoning(pet);

            // 添加技能
            String value = golemDraw.getValue();
            if (StringUtils.isNotBlank(value)) {
                List<String> skills = Arrays.asList(value.split("\\|"));
                skills = skills.stream().distinct().collect(Collectors.toList());
                pet.setOpenSeal(skills.size());
                pet.setSkillData(UsePetAction.skillData(skills));
                pet.setPetSkills(value);
                AllServiceUtil.getRoleSummoningService().updatePetRedis(pet);
            }
            // 设置参战
            login.setSummoning_id(pet.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 发放坐骑 **/
    private void provideMount(LoginResult login, GolemDraw golemDraw) {
        try {
            Mount mount=GameServer.getMount(Sepcies_MixDeal.getRace(login.getSpecies_id()), golemDraw.getId().intValue());
            if (mount == null) return;
            mount.setRoleid(login.getRole_id());
            AllServiceUtil.getMountService().insertMount(mount);

            // 添加技能
            String value = golemDraw.getValue();
            if (StringUtils.isNotBlank(value)) {
                List<String> skills = Arrays.asList(value.split("\\|"));
                skills = skills.stream().distinct().collect(Collectors.toList());
                for (int i = 0; i < skills.size(); i++) {
                    MountSkill mountSkill = new MountSkill();
                    mountSkill.setMid(mount.getMid());
                    mountSkill.setSkillname(skills.get(i));
                    mount.getMountskill().add(mountSkill);
                }
                AllServiceUtil.getMountService().updateMountRedis(mount);
            }
            // 设置乘骑
            login.setMount_id(mount.getMountid());
            RolePool.getRoleData(login.getRole_id()).setMid(mount.getMid());
            SendMessage.sendMessageToMapRoles(login.getMapid(),Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(login.getRoleShow())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] flyIds = {1, 6, 11, 16, 21, 26};
    /** 发放飞行器 **/
    private void provideFly(LoginResult login) {
        int flyId = flyIds[GameServer.random.nextInt(flyIds.length)];
        Fly fly = GameServer.getAllFly().get(flyId);
        if (fly == null) {
            return;
        }
        Gson gson = new Gson();
        fly = gson.fromJson(gson.toJson(fly), Fly.class);

        if (fly != null) {
            FlyConfig flyConfig = GameServer.getFlyConfig();
            fly.setRoleid(login.getRole_id());
            fly.setFlyLevel(1);
            fly.setCurrFlyLevel(0);
            fly.setLdz(flyConfig.getLdzList().get(fly.getStairs() - 1));
            fly.setCurrLdz(flyConfig.getInitLdz());
            fly.setFuel(9999);
            fly.setSkill(null);
            AllServiceUtil.getFlyService().insertFly(fly);

            int zs = BattleMixDeal.lvltrue(login.getGrade());
            if (zs > 0) {
                flyUpLvl(fly, zs + 1);
            }
        }
    }

    /** 飞行器升级 **/
    private void flyUpLvl(Fly fly, int lvl) {
        int type = fly.getFlyId() / 5;
        Fly fly1 = GameServer.getAllFly().get(type * 5 + lvl + 1);
        fly.setFlyId(fly1.getFlyId());
        fly.setFlySpeed(fly1.getFlySpeed());
        fly.setStairs(fly1.getStairs());
        fly.setSkin(fly1.getSkin());
        fly.setName(fly1.getName());
        AllServiceUtil.getFlyService().updateFly(fly);
    }

    private void flyUpLvl(GolemBean golemBean, int lvl) {
        List<Fly> flies = AllServiceUtil.getFlyService().selectFlyByRoleID(golemBean.getRoleId());
        if (flies != null) {
            LoginResult loginResult = golemBean.getLoginResult();
            for (int i = 0; i < flies.size(); i++) {
                Fly fly = flies.get(i);
                flyUpLvl(fly, lvl);
                if (loginResult.getFly_id() != null && loginResult.getFly_id().compareTo(fly.getFid()) == 0) {
                    if (loginResult.getRoleShow().getFlyType() == 1) {
                        loginResult.getRoleShow().setFly_id(fly.getFlyId());

                        loginResult.getRoleShow().setFlyType(1);
                        loginResult.getRoleShow().setFlyY(150);
                        loginResult.getRoleShow().setFlySpeed(BigDecimal.valueOf(0.24).multiply(BigDecimal.valueOf(fly.getFlySpeed())).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                        SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
                    }
                }
            }
        }
    }

    /** 清理背包 **/
    private void cleanUpPack(GolemBean golemBean) {
        try {
            List<Goodstable> goodsTables = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(golemBean.getRoleId());
            for (Goodstable goodsTable : goodsTables) {
                // 穿戴过滤
                if (goodsTable.getStatus() != null&&goodsTable.getStatus()!=0) {
                    continue;
                }
                // 可摆摊物品过滤
                if (GameServer.getGolemConfig().getPetPrice(goodsTable.getGoodsid()) != null) {
                    continue;
                }
                AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(goodsTable.getRgid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 初始化机器人角色 **/
    private LoginResult initGolemRoleInfo(String areaName, BigDecimal userId, String serverMe) {
        LoginResult createRole = new LoginResult();
        createRole.setServerMeString(serverMe);
        createRole.setUser_id(userId);
        createRole.setRolename(randomRoleName());
        createRole.setTitle(areaName + "一员");

        // 设置角色信息
        BigDecimal speciesId = randomRole(5);
        createRole.setSpecies_id(speciesId);
        createRole.setRace_id(BigDecimal.valueOf(Sepcies_MixDeal.getRace(speciesId)));
        createRole.setRace_name(Sepcies_MixDeal.getRaceString(speciesId));
        createRole.setLocalname(Sepcies_MixDeal.getLocalName(speciesId.intValue()));
        createRole.setSex(Sepcies_MixDeal.getSex(speciesId) == 0 ? "女" : "男");

        return createRole;
    }

    /** 获取随机角色 **/
    private BigDecimal randomRole(int roleNum) {
        BigDecimal speciesId = BigDecimal.ZERO;
        int index = Battlefield.random.nextInt(roleNum) + 1;
        switch (index){
            case 1: speciesId = BigDecimal.valueOf(20001 + Battlefield.random.nextInt(6));break;
            case 2: speciesId = BigDecimal.valueOf(21001 + Battlefield.random.nextInt(6));break;
            case 3: speciesId = BigDecimal.valueOf(22001 + Battlefield.random.nextInt(6));break;
            case 4: speciesId = BigDecimal.valueOf(23001 + Battlefield.random.nextInt(6));break;
            case 5: speciesId = BigDecimal.valueOf(24001 + Battlefield.random.nextInt(6));break;
        }
        return speciesId;
    }

    /** 获取随机角色名称 **/
    private String randomRoleName() {
        String roleName = AutoNameUtil.autoSurAndName();
        int size = 0;
        while (true) {
            RoleTable role = AllServiceUtil.getRoleTableService().selectRoleTableByRoleName(roleName);
            if (role == null) {break;}
            if (size < 100) {
                roleName = AutoNameUtil.autoSurAndName();
            } else {
                roleName += GameServer.random.nextInt(10000);
            }
            size++;
        }
        return roleName;
    }

    /** 上架处理 **/
    private void stall(GolemBean golemBean, GolemConfig config, Stall stall) {
        // 降落
        Integer flyType = golemBean.getLoginResult().getRoleShow().getFlyType();
        if (flyType != null && flyType == 1) {
            golemBean.flyAction(2);
        }

        stall.setGoodstables(getGoodsCommoditys(golemBean, config, 24));
        stall.setPets(getPetCommoditys(golemBean, config, 5));
        // 摊位调整
        stall.setX(stall.getX() - 50);
        stall.setY(stall.getY() - 120);
        stall(golemBean, stall, StallPool.OFF);
    }

    /** 摆摊处理 出摊、收摊**/
    private void stall(GolemBean golemBean, Stall stall, int state) {
        try {
            LoginResult loginResult = golemBean.getLoginResult();
            if (state == StallPool.OFF) {
                StallPool.getPool().addStall(stall, GameServer.getRoleNameMap().get(loginResult.getRolename()));
                loginResult.setBooth_id(new BigDecimal(stall.getId()));
                System.out.println(golemBean.getRoleName() + ":开始摆摊");
            } else if (state == StallPool.NO) {
                // 收摊
                int boothId = loginResult.getBooth_id().intValue();
                loginResult.setBooth_id(null);
                StallPool.getPool().RetreatStall(boothId);
                golemBean.setStallState(-1);
                System.out.println(golemBean.getRoleName() + ":收摊");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 摆摊处理验证 **/
    private Stall stallValidate(GolemBean golemBean, GolemConfig config) {
        Stall stall = golemBean.getStall();
        if (stall == null) {
            if (golemBean.getStallState() == 0) {
                golemBean.setStallState(1);

                if (stallConditionValidate(golemBean, config)) {
                    golemBean.setStall(stall = new Stall());
                    stall.setState(StallPool.PREPARE);
                    stall.setRoleid(golemBean.getRoleId());
                    stall.setRole(golemBean.getRoleName());
                    stall.setStall(golemBean.getRoleName() + "的摊位");
                    Point point = config.getStallRandomPoint();
                    stall.setMapid(point.getMapId());
                    stall.setX(point.getX());
                    stall.setY(point.getY());
                }
            }
        } else if (golemBean.getStallTime() != null) { // 正在摆摊
            if (golemBean.getStallTime() <= System.currentTimeMillis()) {// 摆摊过时
                stall(golemBean, null, StallPool.NO);
                golemBean.setStallTime(null);
                golemBean.setStall(null);

                // 飞行
                golemBean.flyAction(1);
            }
        }
        return stall;
    }

    /** 创建道具商品 **/
    private Commodity[] getGoodsCommoditys(GolemBean golemBean, GolemConfig config, Integer size) {
        List<Commodity> commodities = new ArrayList<>();
        List<Goodstable> roleGoods = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(golemBean.getRoleId());
        for (int i = 0; i < roleGoods.size(); i++) {
            Commodity commodity = stallUpdateValidate(golemBean, config, roleGoods.get(i));
            if (commodity !=null) {
                commodities.add(commodity);
            }
        }
        if (size == null) {
            size = commodities.size();
        }
        return commodities.toArray(new Commodity[size]);
    }

    /** 创建物品商品 **/
    private Commodity[] getPetCommoditys(GolemBean golemBean, GolemConfig config, Integer size) {
        List<Commodity> commodities = new ArrayList<>();
        List<RoleSummoning> rolePets = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRoleID(golemBean.getRoleId());
        for (int i = 0; i < rolePets.size(); i++) {
            Commodity commodity = stallUpdateValidate(golemBean, config, rolePets.get(i));
            if (commodity !=null) {
                commodities.add(commodity);
            }
        }

        if (size == null) {
            size = commodities.size();
        }
        return commodities.toArray(new Commodity[size]);
    }

    /** 物品上架条件验证 **/
    private Commodity stallUpdateValidate(GolemBean golemBean, GolemConfig config, Goodstable goodstable) {
        // 穿戴过滤
        if (goodstable.getStatus() != null&&goodstable.getStatus()!=0) {
            return null;
        }
        // 不可交易过滤
        if (AnalysisString.isJY(goodstable)) {
            return null;
        }
        BigDecimal money;
        if (config != null) {
            money = config.getPetPrice(goodstable.getGoodsid());
            if (money == null) {
                return null;
            }
        } else {
            money = BigDecimal.valueOf(1);
        }
        Commodity commodity = new Commodity();
        commodity.setGood(goodstable);
        commodity.setMoney(money.longValue());
        commodity.setCurrency("金钱");
        return commodity;
    }

    /** 召唤兽上架条件验证 **/
    private Commodity stallUpdateValidate(GolemBean golemBean, GolemConfig config, RoleSummoning pet) {
        // 参战过滤
        if (golemBean.getLoginResult().getSummoning_id().compareTo(pet.getSid()) == 0) {
            return null;
        }

        BigDecimal money;
        if (config != null) {
            money = config.getPetPrice(new BigDecimal(pet.getSummoningid()));
            if (money == null) {
                return null;
            }
        } else {
            money = BigDecimal.valueOf(1);
        }
        Commodity commodity = new Commodity();
        commodity.setPet(pet);
        commodity.setMoney(money.longValue());
        commodity.setCurrency("金钱");
        return commodity;
    }

    /** 摆摊条件验证 **/
    private boolean stallConditionValidate(GolemBean golemBean, GolemConfig config) {
        try {
            String condition = config.get("摆摊条件");
            if (StringUtils.isNotBlank(condition)) {
                String[] vals = condition.split("\\|");
                for (int i = 0; i < vals.length; i++) {
                    String[] vs = vals[i].split("=");
                    if (vs.length != 2) {
                        continue;
                    }

                    switch (vs[0]) {
                        case "最小等级":
                            int minLvl = AnalysisString.lvldirection(vs[1]); // 所需等级
                            if (golemBean.getGrade() >= minLvl) {
                                break;
                            }
                            return false;
                        case "最大等级":
                            int maxLvl = AnalysisString.lvldirection(vs[1]); // 所需等级
                            if (golemBean.getGrade() <= maxLvl) {
                                break;
                            }
                            return false;
                        case "最小可售物品数量":
                            int minGoodsSize = getGoodsCommoditys(golemBean, config, null).length;
                            if (minGoodsSize >= Integer.parseInt(vs[1])) {
                                break;
                            }
                            return false;
                        case "最小可售召唤兽数量":
                            int minPetSize = getPetCommoditys(golemBean, config, null).length;
                            if (minPetSize >= Integer.parseInt(vs[1])) {
                                break;
                            }
                            return false;
                        case "最小物品数量":
                            int goodsSize = getGoodsCommoditys(golemBean, null, null).length;
                            if (goodsSize >=Integer.parseInt(vs[1])) {
                                break;
                            }
                            return false;
                        case "最小召唤兽数量":
                            int petSize = getPetCommoditys(golemBean, null, null).length;
                            if (petSize >= Integer.parseInt(vs[1])) {
                                break;
                            }
                            return false;
                        case "概率":
                            if (GameServer.random.nextInt(100) < Integer.parseInt(vs[1])) {
                                break;
                            }
                            return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /** 机器人下线 **/
    private void deletGolem(GolemBean golemBean) {
        if (golemBean == null) return;
        GolemActive active = golemBean.getTask();
        if (active == null) {
            active = golemBean.getNewTask(0, 1, 2);
        }
        if (active != null) {
            TeamBean teamBean = golemBean.getTeamBean();
            if (teamBean != null && teamBean.getTeamName().equals(golemBean.getRoleName())) {
                removeTaskTeams(teamBean.getTeamId(), active);
            }
            System.out.println("机器人异常下线" + golemBean.getRoleName());
        }
        loginGolems.remove(golemBean.getRoleName());
        GameServer.userDown(golemBean.getCtx());
    }

    private class MyChannelHandlerContext implements ChannelHandlerContext {

        private String name;
        public MyChannelHandlerContext(String name) {this.name = name;}

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
            return null;
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
        public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
            return null;
        }

        @Override
        public ChannelHandlerContext fireUserEventTriggered(Object evt) {
            return null;
        }

        @Override
        public ChannelHandlerContext fireChannelRead(Object msg) {
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
        public ChannelFuture bind(SocketAddress localAddress) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
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
        public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture disconnect(ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture close(ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture deregister(ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelHandlerContext read() {
            return null;
        }

        @Override
        public ChannelFuture write(Object msg) {
            return null;
        }

        @Override
        public ChannelFuture write(Object msg, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelHandlerContext flush() {
            return null;
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
            return null;
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg) {
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
        public ChannelFuture newFailedFuture(Throwable cause) {
            return null;
        }

        @Override
        public ChannelPromise voidPromise() {
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
        public <T> Attribute<T> attr(AttributeKey<T> key) {
            return null;
        }

        @Override
        public <T> boolean hasAttr(AttributeKey<T> key) {
            return false;
        }
    }
}
