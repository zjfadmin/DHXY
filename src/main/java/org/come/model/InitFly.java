package org.come.model;

public class InitFly {
    // 种族ID
    private String raceid;
    // 飞行ID
    private String flyid;
    // 飞行名称
    private String flyname;
    //飞行器阶位
    private  String flystate;
    // 飞行等级
    private String flylvl;
    // 体力
    private String live;
    private String skin;
    // 经验
    private String exp;
    public String getRaceid() {
        return raceid;
    }
    public void setRaceid(String raceid) {
        this.raceid = raceid;
    }
    public String getflyid() {
        return flyid;
    }
    public  String getSkin(){
        return skin;
    }
    public  void setSkin(String skin){this.skin=skin;}
    public void setflyid(String flyid) {
        this.flyid = flyid;
    }
    public String getflyname() {
        return flyname;
    }
    public String getFlystate(){return  flystate;};
    public void  setFlystate(String flystate){this.flystate=flystate;}
    public void setflyname(String flyname) {
        this.flyname = flyname;
    }
    public String getflylvl() {
        return flylvl;
    }
    public void setflylvl(String flylvl) {
        this.flylvl = flylvl;
    }
    public String getLive() {
        return live;
    }
    public void setLive(String live) {
        this.live = live;
    }

    public String getExp() {
        return exp;
    }
    public void setExp(String exp) {
        this.exp = exp;
    }
}
