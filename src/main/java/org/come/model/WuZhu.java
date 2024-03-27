package org.come.model;

public class WuZhu {
    //物品id
    private String wuzhuid;
    //装备类型
    private String wuzhutype;
    //属性键
    private String wuzhukey;
    //最小值
    private String wuzhusv;

    public WuZhu() {
    }

    //最大值
    private String wuzhumv;

    public String getWuzhuid() {
        return wuzhuid;
    }

    public void setWuzhuid(String wuzhuid) {
        this.wuzhuid = wuzhuid;
    }

    public String getWuzhutype() {
        return wuzhutype;
    }

    public void setWuzhutype(String wuzhutype) {
        this.wuzhutype = wuzhutype;
    }

    public String getWuzhukey() {
        return wuzhukey;
    }

    public void setWuzhukey(String wuzhukey) {
        this.wuzhukey = wuzhukey;
    }

    public String getWuzhusv() {
        return wuzhusv;
    }

    public void setWuzhusv(String wuzhusv) {
        this.wuzhusv = wuzhusv;
    }

    public String getWuzhumv() {
        return wuzhumv;
    }

    public void setWuzhumv(String wuzhumv) {
        this.wuzhumv = wuzhumv;
    }

    public WuZhu(String wuzhuid, String wuzhutype, String wuzhukey, String wuzhusv, String wuzhumv) {
        this.wuzhuid = wuzhuid;
        this.wuzhutype = wuzhutype;
        this.wuzhukey = wuzhukey;
        this.wuzhusv = wuzhusv;
        this.wuzhumv = wuzhumv;
    }
}
