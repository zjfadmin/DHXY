package org.come.entity;

import org.come.server.GameServer;
//限制登录
public class IpConfig {
    private Integer maxCount;
    private Integer currCount;

    public IpConfig() {
//        this.maxCount = GameServer.maxIpCount;
//        this.currCount = 0;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getCurrCount() {
        return currCount;
    }

    public void setCurrCount(Integer currCount) {
        this.currCount = currCount;
    }
}
