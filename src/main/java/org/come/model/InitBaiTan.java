package org.come.model;

public class InitBaiTan {
    private String id;
    private String stallId;
    private String stallName;
    private String stallType;
    private String goodsId;
    private String goodsName;
    private String money;
    // 使用次数
    private String useTime;
    //摊位所在的地图id
    private String mapId;

    private String map_x;

    private String map_y;
    //货币
    private String currency;

    private String auto_sj;

    //自动捕获周期 单位分钟
    private String zhouQi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStallId() {
        return stallId;
    }

    public void setStallId(String stallId) {
        this.stallId = stallId;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getStallType() {
        return stallType;
    }

    public void setStallType(String stallType) {
        this.stallType = stallType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMap_x() {
        return map_x;
    }

    public void setMap_x(String map_x) {
        this.map_x = map_x;
    }

    public String getMap_y() {
        return map_y;
    }

    public void setMap_y(String map_y) {
        this.map_y = map_y;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAuto_sj() {
        return auto_sj;
    }

    public void setAuto_sj(String auto_sj) {
        this.auto_sj = auto_sj;
    }

    public String getZhouQi() {
        return zhouQi;
    }

    public void setZhouQi(String zhouQi) {
        this.zhouQi = zhouQi;
    }
}
