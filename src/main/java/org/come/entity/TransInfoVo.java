package org.come.entity;

//交易信息对象 
public class TransInfoVo {
    private Long transId;
    private Integer type; // 1:交易 2：给与 3：摆摊
    private String fromBy;//物品出处人
    private String itemInfo;//物品
    private String toBy;//物品接收人
    private String transTime;//交易时间

    private Integer sum; //物品数量

    public TransInfoVo() {

    }
    public TransInfoVo(int type, long transId, String transTime) {
        this.transId = transId;
        this.type = type;
        this.transTime = transTime;
    }

    public TransInfoVo(int type, long transId, String transTime, int itemCount) {
        this.transId = transId;
        this.type = type;
        this.transTime = transTime;
        this.sum = itemCount;
    }








    @Override
    public String toString() {
        return "用户名：" + fromBy +
                " 将： " + itemInfo +
                " 数量：" + sum +
                " 给与 用户名：" + toBy +
             "交易时间:: "   +  transTime ;
    }



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
}

