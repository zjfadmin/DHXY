package org.come.bean;

import javax.xml.crypto.Data;
import java.util.Date;

public class TtModel {

    //天梯开放时间 时
    private Integer startHour;
    //天梯结束时间 时
    private Integer endHour;

    //天梯开放时间 分
    private Integer startMinute;
    //天梯开放时间 分
    private Integer endMinute;

    //赛季开始时间
    private Date seasonStartTime;
    //赛季结束时间
    private Date seasonEndTime;

    //当前赛季
    private Integer currentSeason;
    //是否开启赛季
    private Integer isOpen;


    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Date getSeasonStartTime() {
        return seasonStartTime;
    }

    public void setSeasonStartTime(Date seasonStartTime) {
        this.seasonStartTime = seasonStartTime;
    }

    public Date getSeasonEndTime() {
        return seasonEndTime;
    }

    public void setSeasonEndTime(Date seasonEndTime) {
        this.seasonEndTime = seasonEndTime;
    }

    public Integer getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Integer currentSeason) {
        this.currentSeason = currentSeason;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }
}
