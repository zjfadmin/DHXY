package com.gl.model;

/**
 * 前端请求参数封装
 *
 * @author sinmahod
 */
public class UpPetParam {

    private String sid;
    private int grade;
    private int turnRount;
    private long friendliness;
    private int openSeal;
    private Integer lx;
    private String skill;
    private String nds;


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getTurnRount() {
        return turnRount;
    }

    public void setTurnRount(int turnRount) {
        this.turnRount = turnRount;
    }

    public long getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(long friendliness) {
        this.friendliness = friendliness;
    }

    public int getOpenSeal() {
        return openSeal;
    }

    public void setOpenSeal(int openSeal) {
        this.openSeal = openSeal;
    }

    public Integer getLx() {
        return lx;
    }

    public void setLx(Integer lx) {
        this.lx = lx;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getNds() {
        return nds;
    }

    public void setNds(String nds) {
        this.nds = nds;
    }
}


