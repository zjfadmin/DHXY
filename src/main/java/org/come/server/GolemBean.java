package org.come.server;

import come.tool.Battle.BattleMixDeal;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Role.RoleShow;
import come.tool.Stall.Stall;
import come.tool.newTeam.TeamBean;
import come.tool.newTeam.TeamUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.come.bean.LoginResult;
import org.come.bean.PathPoint;
import org.come.bean.RoleMoveBean;
import org.come.entity.Fly;
import org.come.handler.SendMessage;
import org.come.model.*;
import org.come.protocol.Agreement;
import org.come.script.XLPath;
import org.come.task.MapMonsterBean;
import org.come.task.RefreshMonsterTask;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class GolemBean {
    private ChannelHandlerContext ctx;
    private LoginResult loginResult;

    private Long idleTime;      // 闲置时间
    private Long stallTime; // 摆摊时间
    private GolemActive target;  // 活动目标
    private GolemScript script; // 自动脚本
    private Stall stall; // 摊位信息
    private int stallState; // 摆摊验证状态 -1已验证 0未验证 1摆摊

    private Map<Integer, Integer> robotsMap; //robotsId 剩余击杀次数
    private String robot;                    //分配到的怪物信息 怪物名称-I-mapID-X-Y
    private Point point;                     //闲置区域信息

    public GolemBean(LoginResult loginResult,ChannelHandlerContext ctx) {
        this.robotsMap = new HashMap<>();

        this.loginResult = loginResult;
        this.ctx = ctx;
    }

    /** 是否在战斗中 **/
    public boolean isFighting() {
        return loginResult.getFighting() != 0;
    }

    /** 获取当前正在进行的有效任务 **/
    public GolemActive getTask() {
        if (target != null && taskConditionValidate(target)) {
            return target;
        }
        return null;
    }

    /**
     * 获取新的未完成的任务
     *
     * @return
     */
    public GolemActive getNewTask() {
        this.target = getNewTask(0,1,2);
        return target;
    }

    /**
     * 获取新的未完成的任务
     *
     * @param types 类型集合
     * @return
     */
    public GolemActive getNewTask(Integer... types) {
        // 任务条件筛选
        List<GolemActive> golemActives = GameServer.getGolemActives().stream()
                .filter(t -> taskConditionValidate(t, types))
                .collect(Collectors.toList());

        Collections.shuffle(golemActives);
        return golemActives.size() > 0 ? golemActives.get(0) : null;
    }

    /** 任务是否满足条件 **/
    public boolean taskConditionValidate(GolemActive active) {
        return taskConditionValidate(active,0,1,2);
    }
    private boolean taskConditionValidate(GolemActive active, Integer... types) {
        // 类型筛选
        if (!Arrays.asList(types).contains(active.getType())) {
            return false;
        }
        // 时间验证
        if (StringUtils.isNotBlank(active.getWeek())&&!RefreshMonsterTask.getWeek().equals(active.getWeek())) {
            return false;
        }
        // 等级验证
        if (BattleMixDeal.isLvl(loginResult.getGrade(), active.getLvls()) != null) {
            return false;
        }
        // 完成次数验证
        if (getTaskCount(active) <= 0) {
            return false;
        }
        return true;
    }

    /** 初始化可击杀怪物及数量限制 **/
    public void initRobotsMap() {
        robotsMap.clear();
        List<GolemActive> golemActives = GameServer.getGolemActives(3);
        for (int i = 0; i < golemActives.size(); i++) {
            GolemActive active = golemActives.get(i);
            if (BattleMixDeal.isLvl(loginResult.getGrade(), active.getLvls()) != null) continue;
            robotsMap.put(active.getTasksetId(), active.getSum());
        }
    }

    /** 更新可击杀怪物及数量限制 **/
    public void updateRobotsMap(Integer robotId) {
        Integer sum = robotsMap.get(robotId);
        if (sum != null && sum > 0) {
            sum--;
            robotsMap.put(robotId, sum);
        }
    }

    /** 更新可击杀怪物及数量限制 **/
    public void updateRobotsMap() {
        try {
            List<GolemActive> golemActives = GameServer.getGolemActives(3);
            for (int i = 0; i < golemActives.size(); i++) {
                GolemActive active = golemActives.get(i);
                if (robotsMap.get(active.getTasksetId()) != null) {
                    if (BattleMixDeal.isLvl(loginResult.getGrade(), active.getLvls()) != null) {
                        robotsMap.remove(active.getTasksetId());
                    }
                    continue;
                }
                if (BattleMixDeal.isLvl(loginResult.getGrade(), active.getLvls()) != null) {
                    continue;
                }
                robotsMap.put(active.getTasksetId(), active.getSum());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 获取任务未完成次数 **/
    private int getTaskCount(GolemActive target) {
        int count = 0;
        if (target != null) {
            RoleData roleData = RolePool.getRoleData(loginResult.getRole_id());
            int num = roleData.getTaskWC(target.getTasksetId());
            count = target.getSum() - num;
        }
        return count;
    }

    /** 机器人移动 **/
    public long move(int x, int y, double sp) {
        RoleShow roleShow = loginResult.getRoleShow();
        RoleMoveBean roleMoveBean = new RoleMoveBean();
        List<PathPoint> points = new ArrayList<>();
        points.add(new PathPoint(roleShow.getX().intValue(), roleShow.getY().intValue()));
        points.add(new PathPoint(x, y));
        roleMoveBean.setPaths(points);
        loginResult.setX(new Long(x));
        loginResult.setY(new Long(y));
        roleMoveBean.setRole(getRoleName());

        String msg = Agreement.getAgreement().moveAgreement(GsonUtil.getGsonUtil().getgson().toJson(roleMoveBean));
        SendMessage.sendMessageToMapRoles(loginResult.getMapid(), msg);
        return XLPath.getMoveTime(points, sp) + System.currentTimeMillis();
    }

    /** 怪物可击杀数量验证 **/
    public boolean isAssignedRobot(int robot) {
        Integer count = robotsMap.get(robot);
        return count != null && count > 0;
    }

    /** 比较两个目标哪个离机器人更近 如果monsterBean1更近返回-1 monsterBean2更近返回1**/
    public int compare(MapMonsterBean monsterBean1, MapMonsterBean monsterBean2) {
        if (monsterBean1 == null || monsterBean1.getMap() != getMapId()) {
            return 1;
        }
        if (monsterBean1 == null || monsterBean2.getMap() != getMapId()) {
            return -1;
        }

        int distance1 = Math.abs(getX() - monsterBean1.getX()) + Math.abs(getY() - monsterBean1.getX());
        int distance2 = Math.abs(getX() - monsterBean2.getX()) + Math.abs(getY() - monsterBean2.getX());
        return (distance1 < distance2) ? -1 : ((distance1 == distance2) ? 0 : 1);
    }

    /** 距离 **/
    public boolean nearby() {
        if (target == null) {
            return false;
        }

        String[] positions = target.getGuide().split("-");
        int mapId = Integer.parseInt(positions[0]);
        int x = Integer.parseInt(positions[1]);
        int y = Integer.parseInt(positions[2]);
        return nearby(mapId, x, y);
    }

    /** 是否在目标位置 **/
    public boolean nearby(int mapId, int x, int y) {
        if (getMapId() != mapId) {
            return false;
        }
        if (getX() < x - 100 || x + 100 < getX()) {
            return false;
        }
        if (getY() < y - 100 || y + 100 < getY()) {
            return false;
        }
        return true;
    }

    /** 机器人移动 **/
    public void move(int x, int y) {
        loginResult.setX(new Long(x));
        loginResult.setY(new Long(y));
    }

    /** 机器人移动速度 **/
    public double getSp() {
        return loginResult.getRoleShow().getFlySpeed();
    }

    /** 飞行器操作 0装备 1飞行 2降落 **/
    public void flyAction(int tyep) {
        if (tyep == 0) {
            List<Fly> flys = AllServiceUtil.getFlyService().selectFlyByRoleID(loginResult.getRole_id());
            if (flys != null && flys.size() > 0) {
                Fly fly = flys.get(0);
                loginResult.setFly_id(fly.getFid());
                RolePool.getRoleData(loginResult.getRole_id()).setFid(fly.getFid().intValue());
                loginResult.getRoleShow().setEquipmentFlyId(fly.getFlyId());
                SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
                SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowFly("zb=" + fly.getFlyId() + "=" + loginResult.getRolename()));
            }
        } else if (tyep == 1) {
            if (loginResult.getFly_id() == null || loginResult.getFly_id().compareTo(BigDecimal.ZERO) == 0) {
                flyAction(0);
            }

            if (loginResult.getFly_id() != null) {
                Fly fly = AllServiceUtil.getFlyService().selectFlyByFID(loginResult.getFly_id().toString());
                if (fly != null) {
                    loginResult.getRoleShow().setFly_id(fly.getFlyId());

                    loginResult.getRoleShow().setFlyType(1);
                    loginResult.getRoleShow().setFlyY(150);
                    loginResult.getRoleShow().setFlySpeed(BigDecimal.valueOf(0.24).multiply(BigDecimal.valueOf(fly.getFlySpeed())).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
                }
            }
        } else if (tyep == 2){
            loginResult.getRoleShow().setFly_id(0);

            loginResult.getRoleShow().setFlyType(-1);
            loginResult.getRoleShow().setFlyY(0);
            loginResult.getRoleShow().setFlySpeed(0.24d);
            SendMessage.sendMessageToMapRoles(loginResult.getMapid(), Agreement.getAgreement().upRoleShowAgreement(GsonUtil.getGsonUtil().getgson().toJson(loginResult.getRoleShow())));
        }
    }

    public TeamBean getTeamBean() {
        return TeamUtil.getTeamRole(getRoleId());
    }

    public BigDecimal getRoleId() {
        return loginResult.getRole_id();
    }
    public String getRoleName() {
        return loginResult.getRolename();
    }
    public RoleData getRoleData() {
        return RolePool.getRoleData(getRoleId());
    }
    public RoleShow getRoleShow() {return loginResult.getRoleShow();}
    public Integer getGrade() {
        return loginResult.getGrade();
    }
    public BigDecimal getSpeciesId() {
        return loginResult.getSpecies_id();
    }
    public BigDecimal getSummoningId() {
        return loginResult.getSummoning_id();
    }

    public Integer getTaskSetId() {
        if (target != null) {
            return target.getTasksetId();
        }
        return null;
    }

    public BigDecimal getTeamId() {
        return loginResult.getTroop_id();
    }
    public String getTeam() {
        return loginResult.getTeam();
    }
    public int getMapId() {
        return loginResult.getMapid().intValue();
    }
    public int getX() {
        return loginResult.getX().intValue();
    }
    public int getY() {
        return loginResult.getY().intValue();
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
    public LoginResult getLoginResult() {
        return loginResult;
    }
    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }
    public Integer getIdleTime() {
        if (idleTime != null) {
            long currentTime = System.currentTimeMillis();
            long time = currentTime - idleTime;
            return (int) (time / (60 * 1000));
        }
        return null;
    }
    public void setIdleTime(Long idleTime) {
        this.idleTime = idleTime;
    }

    public Long getStallTime() {
        return stallTime;
    }

    public void setStallTime(Long stallTime) {
        this.stallTime = stallTime;
    }

    public GolemActive getTarget() {
        return target;
    }
    public void setTarget(GolemActive target) {
        this.target = target;
    }
    public GolemScript getScript() {
        return script;
    }
    public void setScript(GolemScript script) {
        this.script = script;
    }
    public Stall getStall() {
        return stall;
    }
    public void setStall(Stall stall) {
        this.stall = stall;
    }
    public int getStallState() {
        return stallState;
    }
    public void setStallState(int stallState) {
        this.stallState = stallState;
    }

    public String getRobot() {
        return robot;
    }
    public void setRobot(String robot) {
        this.robot = robot;
    }

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
}
