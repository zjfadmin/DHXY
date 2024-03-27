package org.come.model;

import java.math.BigDecimal;

public class GolemActive {

    private BigDecimal id;
    private String name; // 活动名称 GolemBooth
    private String guide; // 引导位置
    private String lvl; // 参与等级
    private int type; // 活动类型 0击杀野怪任务 1击杀指定怪物任务 2单人任务 3非领取任务的野怪
    private int sum; // 完成次数
    private int tasksetId;
    private String week; // 开放时间

    private transient int[] lvls;

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

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
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

    public int getTasksetId() {
        return tasksetId;
    }

    public void setTasksetId(int tasksetId) {
        this.tasksetId = tasksetId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int[] getLvls() {
        return lvls;
    }

    public void setLvls(int[] lvls) {
        this.lvls = lvls;
    }
}
