package org.come.model;

import java.math.BigDecimal;

public class GolemTask {
    private BigDecimal id;
    private String name; // 活动名称
    private String guide; // 引导位置
    private int lvl; // 参与等级
    private int type; // 活动类型
    private int sum; // 完成次数
    private int taskSetID; // tasksetID
    private String value;

    public BigDecimal getId() {
        return id;
    }
    public void setId(BigDecimal id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGuide() {
        return guide;
    }
    public void setGuide(String guide) {
        this.guide = guide;
    }
    public int getLvl() {
        return lvl;
    }
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getSum() {
        return sum;
    }
    public void setSum(int sum) {
        this.sum = sum;
    }
    public int getTaskSetID() {
        return taskSetID;
    }
    public void setTaskSetID(int taskSetID) {
        this.taskSetID = taskSetID;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
