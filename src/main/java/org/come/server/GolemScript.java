package org.come.server;

import come.tool.Battle.FightingForesee;
import come.tool.Role.RoleData;
import come.tool.newTask.Task;
import come.tool.newTask.TaskAction;
import come.tool.newTeam.TeamBean;
import org.apache.commons.lang.StringUtils;
import org.come.bean.ChangeMapBean;
import org.come.bean.UseCardBean;
import org.come.model.*;
import org.come.protocol.AgreementUtil;
import org.come.protocol.ParamTool;
import org.come.script.ScriptOpen;
import org.come.script.XLPath;
import org.come.task.MapMonsterBean;
import org.come.task.MonsterUtil;
import org.come.tool.SplitStringTool;
import org.come.until.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GolemScript {
    private GolemBean golemBean;

    private XLPath xlPath;
    private List<ScriptOpen> scripts;
    private ScriptOpen script;

    private boolean isEnd;
    private boolean isFighting;
    private boolean isAccurate; // 是否为范围坐标

    public GolemScript(GolemBean golemBean) {
        this.golemBean = golemBean;
        isEnd = true;

        this.xlPath = new XLPath();
        this.scripts = new ArrayList<>();
        this.isAccurate = true;
    }

    public void start() {
        isEnd = false;
    }

    public void clear() {
        this.scripts.clear();
        this.script = null;

        this.isEnd = true;
        this.isFighting = false;
        this.isAccurate = true;
        // 设置为闲置状态
        golemBean.setIdleTime(System.currentTimeMillis());
    }

    /** 脚本是否为停止状态 **/
    public boolean isEnd() {
        if (!isEnd) {
            if (!isWait() && script == null && scripts.size() <= 0) {
                clear();
            }
        }
        return isEnd;
    }

    /** 执行脚本 **/
    public void handle(GolemBean... golemBeans) {
        if (golemBean.isFighting() || isFighting) return;
        if (isWait()) return;
        if (isEnd()) return;
        if (script == null) {
            script = scripts.remove(scripts.size() - 1);
        }
        switch (script.getType()) {
            case 0: // 寻路
                int moveX = script.getX();
                int moveY = script.getY();
                if (isAccurate) {
                    moveX += 100 - GameServer.random.nextInt(200);
                    moveY += 100 - GameServer.random.nextInt(200);
                }
                waitTime = golemBean.move(moveX, moveY, golemBean.getSp());
                for (int i = 1; i < golemBeans.length; i++) {
                    golemBeans[i].move(moveX, moveY);
                }
                break;
            case 1: // 传送
                ChangeMapBean change;
                if (isAccurate) {
                    change = new ChangeMapBean(String.valueOf(script.getMapId()), script.getX(), script.getY());
                } else {
                    change = new ChangeMapBean(script.getMapId(), script.getX(), script.getY());
                }
                ParamTool.ACTION_MAP.get(AgreementUtil.changemap).action(golemBean.getCtx(), GsonUtil.getGsonUtil().getgson().toJson(change));
                break;
            case 2: // 领取任务
                ParamTool.ACTION_MAP.get(AgreementUtil.TASKN).action(golemBean.getCtx(), "L" + script.getTaskId());
                Task task = getSetTask();
                if (task == null) {
                    clear();
                    break;
                }
                System.out.println(golemBean.getRoleName() + ":领取任务【" + task.getTaskData().getTaskName()+"】");
                createScript(golemBean.getTarget(), true);

                // 领取buff
                /*for (int i = 0; i < golemBeans.length; i++) {
                    RoleData roleData = golemBeans[i].getRoleData();
                    UseCardBean limit = roleData.getLimit("经验");
                    if (limit == null) {
                        limit = new UseCardBean("双倍经验", "经验", "shuang", 0, null);
                        roleData.addLimit(limit);
                    }
                }*/
                break;
            case 3: // 击杀任务
                FightingForesee fightingForesee = new FightingForesee();
                fightingForesee.setAlias(script.getAlias());
                fightingForesee.setYidui(golemBean.getTeam());
                if (script.getKillType() == 0) { // 击杀野怪
                    MapMonsterBean bean= MonsterUtil.getMonster(script.getRobotId());
                    if (bean == null || bean.getType() != 0) { // 怪物正在忙碌则放弃击杀
                        break;
                    }
                    fightingForesee.setType(1);
                    fightingForesee.setI(bean.getI());
                } else {
                    Robots robots = GameServer.getAllRobot().get(String.valueOf(script.getRobotId()));
                    fightingForesee.setType(2);
                    fightingForesee.setRobotid(robots.getRobotid());
                    fightingForesee.setNd(0);
                }

                ParamTool.ACTION_MAP.get(AgreementUtil.fightingforesee).action(golemBean.getCtx(), GsonUtil.getGsonUtil().getgson().toJson(fightingForesee));
                isFighting = golemBean.isFighting();
                if (isFighting) {
                    System.out.println(golemBean.getRoleName() + ":击杀【" + fightingForesee.getAlias() + "】");
                } else {
                    System.out.println(golemBean.getRoleName() + ":击杀【" + fightingForesee.getAlias() + "】出现错误");
                    createScript(golemBean.getTask(), false);
                }
                return;
            case 4: // 问候 给予物品
                TaskData taskData = GameServer.getTaskData(Integer.parseInt(script.getTaskId()));
                TaskAction.MMM(
                        taskData,
                        golemBean.getTeam().split("\\|"),
                        script.getAlias(),
                        script.getRobotId(),
                        script.getKillType() == 3 ? "问候" : "给予物品");
                completeTask(taskData.getTaskSetID());
                return;
        }
        script = null;
    }

    /** 完成任务 **/
    public void completeTask(int taskSetId) {
        GolemActive active = golemBean.getTarget();
        if (active != null && active.getTasksetId() == taskSetId) {
            createScript(false);
            System.out.println(golemBean.getRoleName() + ":完成任务【" + active.getName() + "】");
        }
    }

    /** 结束战斗 **/
    public void endFighting() {
        isFighting = false;
    }

    /** 是否完成任务 **/
    public boolean isCompleteTask() {
        GolemActive task = golemBean.getTask();
        if (task != null) return false;
        return isEnd;
    }

    /** 创建脚本 **/
    public void createScript(boolean isMove) {
        GolemActive active = golemBean.getTask();
        if (active == null && StringUtils.isBlank(golemBean.getRobot())) {
            clear();
            return;
        }
        createScript(active,isMove);
    }

    /** 创建脚本 自动寻路**/
    public void createScript(GolemActive active) {
        String[] positions = active.getGuide().split("-");
        createScript(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]), Integer.parseInt(positions[2]), true);
    }

    /** 创建脚本 自动寻路**/
    public void createScript(int mapId, int x, int y, boolean isMove) {
        scripts.clear(); // 清空脚本
        if (isMove) {
            // 步行到目的地
            xlPath.move(scripts, golemBean.getMapId(), golemBean.getX(), golemBean.getY(), mapId, x, y);
        } else {
            // 传送到目的地
            scripts.add(new ScriptOpen(mapId, x, y));
        }
        if (scripts.size() > 0) isEnd = false;
    }

    private long waitTime;
    /** 执行等待 **/
    private boolean isWait() {
        if (waitTime != 0) {
            if (waitTime > System.currentTimeMillis()) return true;
            waitTime = 0;
        }
        return false;
    }

    /** 创建脚本 **/
    private void createScript(GolemActive active, boolean isMove) {
        script = null;
        scripts.clear(); // 清空脚本

        String robot = golemBean.getRobot();
        if (StringUtils.isNotBlank(robot)){
            String[] positions = robot.split("-");
            scripts.add(new ScriptOpen(positions[1], Integer.parseInt(positions[2]),0));
            // 步行到目的地
            xlPath.move(scripts,
                    golemBean.getMapId(), golemBean.getX(), golemBean.getY(),
                    Integer.parseInt(positions[3]), Integer.parseInt(positions[4]), Integer.parseInt(positions[5])
            );
        } else {
            Task task = getSetTask();
            if (task != null) { // 执行任务
                List<TaskProgress> progressList = task.getProgress();
                for (int i = 0; i < progressList.size(); i++) {
                    TaskProgress taskProgress = progressList.get(i);
                    if (taskProgress.getSum() >= taskProgress.getMax()) {
                        continue;
                    }

                    int robotId = taskProgress.getDId();
                    int map = taskProgress.getMap();
                    int x = taskProgress.getX();
                    int y = (int)taskProgress.getY();
                    if (taskProgress.getType() == 3 || taskProgress.getType() == 4) {
                        int sum = taskProgress.getType() == 3 ? 1 : taskProgress.getSum();
                        scripts.add(new ScriptOpen(taskProgress.getDName(), task.getTaskId() + "", sum, taskProgress.getType()));
                    } else {
                        if (taskProgress.getType()==0) {
                            if (map == 0) {
                                map = golemBean.getMapId();
                            }
                            List<MapMonsterBean> list = MonsterUtil.getList(robotId, (long) map);
                            // 过滤正在忙碌的怪物
                            list = list.stream()
                                    .filter(m -> m.getType() == 0)
                                    .sorted((p1, p2) -> golemBean.compare(p1, p2))
                                    .collect(Collectors.toList());
                            if (list.size() == 0) {
                                continue;
                            }
                            // 取最近的怪物
                            MapMonsterBean mapMonsterBean = list.get(0);
                            robotId = mapMonsterBean.getI();
                            map = mapMonsterBean.getMap().intValue();
                            x = mapMonsterBean.getX();
                            y = mapMonsterBean.getY();
                        } else if (taskProgress.getType() == 1 || taskProgress.getType() == 2 || taskProgress.getType() == 3) {
                            if (taskProgress.getMap() == 0) {
                                continue;
                            }

                            if (taskProgress.getType() == 2||taskProgress.getType() == 3) {
                                robotId = Integer.parseInt(GameServer.getNpc(String.valueOf(taskProgress.getDId())).getBinding());
                            }
                        }

                        scripts.add(new ScriptOpen(taskProgress.getDName(), robotId, taskProgress.getType()));
                    }
                    xlPath.move(scripts, golemBean.getMapId(), golemBean.getX(), golemBean.getY(), map, x, y);
                    break;
                }
            } else {
                // 队伍条件验证
                TeamBean teamBean = golemBean.getTeamBean();
                boolean isClaimTask = true;
                if (active.getType() != 2) {
                    if (teamBean != null) {
                        GameServer.golemServer.transListener(teamBean);
                        isClaimTask = GameServer.golemServer.teamListener(teamBean,null,true);
                    } else {
                        isClaimTask = false;
                    }
                }

                if (isClaimTask) {
                    String[] positions = active.getGuide().split("-");
                    Npctable npc = GameServer.getNpc(positions[3]);
                    if (npc != null) {
                        String npcway = npc.getNpcway();
                        if (StringUtils.isNotBlank(npcway)) {
                            String[] vs = npcway.split(" ");
                            if (vs != null) {
                                for (int i = 0; i < vs.length; i++) {
                                    List<String> taskids = SplitStringTool.splitString(vs[i]);
                                    TaskData taskData = GameServer.getTaskData(Integer.parseInt(taskids.get(0)));
                                    if (taskData == null) continue;
                                    if (active.getTasksetId() == taskData.getTaskSetID()) {
                                        // 领取任务
                                        scripts.add(new ScriptOpen(taskids.get(GameServer.random.nextInt(taskids.size()))));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    int mapId = Integer.parseInt(positions[0]);
                    int x = Integer.parseInt(positions[1]);
                    int y = Integer.parseInt(positions[2]);
                    if (!golemBean.nearby(mapId,x,y)) {
                        if (isMove) {
                            // 步行到目的地
                            xlPath.move(scripts, golemBean.getMapId(), golemBean.getX(), golemBean.getY(), mapId, x, y);
                        } else {
                            // 传送到目的地
                            scripts.add(new ScriptOpen(mapId, x, y));
                        }
                    }
                }
            }
        }
        if (scripts.size() > 0) isEnd = false;
    }

    /** 根据setID 获取任务 **/
    private Task getSetTask() {
        GolemActive target = golemBean.getTarget();
        if (target != null) {
            return golemBean.getRoleData().getTaskBySetId(target.getTasksetId());
        }
        return null;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }
}
