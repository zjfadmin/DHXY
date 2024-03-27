package org.come.entity;

import java.math.BigDecimal;

public class FlyRoleUser {//新加飞行器

    private BigDecimal mid; // 表ID
    private BigDecimal flyid; // 飞行器ID
    private String flyname; // 飞行器名称
    private String flylvl; // 飞行器等级
    private String exp; // 飞行器经验
    private BigDecimal roleid; // 角色ID
    private String rolename;
    private BigDecimal user_id;
    private String username;
    private Integer start;
    private Integer end;
    private Integer pageNum;
    private String orderBy;

    public FlyRoleUser() {
        // TODO Auto-generated constructor stub
    }

    public BigDecimal getMid() {
        return mid;
    }

    public void setMid(BigDecimal mid) {
        this.mid = mid;
    }

    public BigDecimal getFlyid() {
        return flyid;
    }

    public void setFlyid(BigDecimal flyid) {
        this.flyid = flyid;
    }

    public String getFlyname() {
        return flyname;
    }

    public void setFlyname(String flyname) {
        this.flyname = flyname;
    }

    public String getFlylvl() {
        return flylvl;
    }

    public void setFlylvl(String flylvl) {
        this.flylvl = flylvl;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public BigDecimal getRoleid() {
        return roleid;
    }

    public void setRoleid(BigDecimal roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public BigDecimal getUser_id() {
        return user_id;
    }

    public void setUser_id(BigDecimal user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
