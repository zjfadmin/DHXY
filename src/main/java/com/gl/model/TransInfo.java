package com.gl.model;

public class TransInfo {

    private Long transId; // 1:交易 2：给与 3：摆摊
    private Integer type; // 1:交易 2：给与 3：摆摊
    private String fromBy;//物品出处人
    private String itemInfo;//物品
    private String toBy;//物品接收人
    private String transTime;//交易时间


    private int count;
    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFromBy() {
        return fromBy;
    }

    public void setFromBy(String fromBy) {
        this.fromBy = fromBy;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getToBy() {
        return toBy;
    }

    public void setToBy(String toBy) {
        this.toBy = toBy;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
