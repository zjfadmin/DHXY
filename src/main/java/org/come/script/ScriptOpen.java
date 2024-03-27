package org.come.script;

public class ScriptOpen {
    private int type;//0寻路 1传送 2领取任务 3执行击杀任务 4执行问候给予任务

    private int mapId;
    private int x;
    private int y;

    private int robotId;
    private String taskId;
    private String alias;
    private int killType; // 任务类型 0击杀野怪 1自定义野怪 2击杀NPC 3问候NPC 4给与物品 5护送任务

    public ScriptOpen(int x, int y) {
        this.type = 0;
        this.x = x;
        this.y = y;
    }

    public ScriptOpen(int mapId, int x, int y) {
        this.type = 1;
        this.mapId = mapId;
        this.x = x;
        this.y = y;
    }

    public ScriptOpen(String taskId) {
        this.type = 2;
        this.taskId = taskId;
    }

    public ScriptOpen(String alias, int robotId, int killType) {
        this.type = 3;
        this.alias = alias;
        this.robotId = robotId;
        this.killType = killType;
    }

    public ScriptOpen(String alias, String taskId, int robotId, int killType) {
        this.type = 4;
        this.alias = alias;
        this.taskId = taskId;
        this.robotId = robotId;
        this.killType = killType;
    }


    public int getType() {
        return type;
    }
    public int getMapId() {
        return mapId;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getRobotId() {
        return robotId;
    }
    public String getTaskId() {
        return taskId;
    }
    public String getAlias() {
        return alias;
    }
    public int getKillType() {
        return killType;
    }
}
