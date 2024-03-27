package org.come.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlyConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    //兔绒消耗技术
    private Integer ltr;
    //兔绒消耗阶梯
    private Integer ltrUpgradeMultiplier;
    //等级限制
    private List<Integer> integerList = new ArrayList<Integer>(Arrays.asList(1, 16, 32, 48, 64));
    //飞行速度配置
    private List<Double> flySpeed = new ArrayList<Double>();
    //升级灵动值列表
    private List<Integer> ldzList;
    //初始化灵动值列表
    private Integer initLdz;
    //初始升级经验值
    private Integer initLevelUpExperience = 1000;
    //初始化升级金钱
    private Integer initLevelUpMoney = 100;
    //递增倍率
    private Double upgradeIncrementRate = 0.01;
    //固定递增倍率
    private Integer upgradeIncrementNumber = 5000;
    //固定递增升级金额
    private Integer upgradeIncrementMoney = 5000;
    //升级方式 1.倍率 2.指定递增
    private Integer isType;

    public Integer getLtr() {
        return ltr;
    }

    public void setLtr(Integer ltr) {
        this.ltr = ltr;
    }

    public Integer getLtrUpgradeMultiplier() {
        return ltrUpgradeMultiplier;
    }

    public void setLtrUpgradeMultiplier(Integer ltrUpgradeMultiplier) {
        this.ltrUpgradeMultiplier = ltrUpgradeMultiplier;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public Integer getInitLevelUpExperience() {
        return initLevelUpExperience;
    }

    public void setInitLevelUpExperience(Integer initLevelUpExperience) {
        this.initLevelUpExperience = initLevelUpExperience;
    }

    public Double getUpgradeIncrementRate() {
        return upgradeIncrementRate;
    }

    public void setUpgradeIncrementRate(Double upgradeIncrementRate) {
        this.upgradeIncrementRate = upgradeIncrementRate;
    }

    public Integer getUpgradeIncrementNumber() {
        return upgradeIncrementNumber;
    }

    public void setUpgradeIncrementNumber(Integer upgradeIncrementNumber) {
        this.upgradeIncrementNumber = upgradeIncrementNumber;
    }

    public Integer getUpgradeIncrementMoney() {
        return upgradeIncrementMoney;
    }

    public void setUpgradeIncrementMoney(Integer upgradeIncrementMoney) {
        this.upgradeIncrementMoney = upgradeIncrementMoney;
    }

    public Integer getInitLevelUpMoney() {
        return initLevelUpMoney;
    }

    public void setInitLevelUpMoney(Integer initLevelUpMoney) {
        this.initLevelUpMoney = initLevelUpMoney;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIsType() {
        return isType;
    }

    public void setIsType(Integer isType) {
        this.isType = isType;
    }

    public List<Integer> getLdzList() {
        return ldzList;
    }

    public void setLdzList(List<Integer> ldzList) {
        this.ldzList = ldzList;
    }

    public Integer getInitLdz() {
        return initLdz;
    }

    public void setInitLdz(Integer initLdz) {
        this.initLdz = initLdz;
    }

    public List<Double> getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(List<Double> flySpeed) {
        this.flySpeed = flySpeed;
    }

    public static void main(String[] args) {

    }
}
