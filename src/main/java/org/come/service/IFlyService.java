package org.come.service;

import org.come.entity.Fly;
import org.come.entity.FlyRoleUser;

import java.math.BigDecimal;
import java.util.List;

public interface IFlyService {

    // 查找 新加所有飞行器
    List<Fly> selectAllFlys();
    /**
     * 查找角色所有飞行器
     */
    List<Fly> selectFlysByRoleID( BigDecimal roleID );

    /**
     * 查找角色飞行器
     */
    Fly selectFlysByMID( BigDecimal mid );

    /**
     * 删除角色飞行器
     */
    void deleteFlysByMid( BigDecimal roleID );

    /**修改飞行器属性*/
    void updateFly( Fly fly );
    /**修改飞行器属性刚从redis取出*/
    void updateFlyRedis(Fly fly);
    /**
     * 添加飞行器
     * @param fly
     */
    void insertFly( Fly fly );
    void deleteFlysByMidsql(BigDecimal roleID);
    void updateFlysql(Fly fly);
    void insertFlysql(Fly fly);
    BigDecimal selectMaxID();


    List<FlyRoleUser> selectFly(FlyRoleUser mru);

    Integer selectFlyCount(FlyRoleUser mru);

    /** HGC-2020-01-20 */
    /** 批量删除角色飞行器 */
    void deleteFlysByMidList(List<BigDecimal> roleID);

    /** 批量修改飞行器属性 */
    void updateFlyList(List<Fly> fly);

    /** 批量添加飞行器 */
    void insertFlyList(List<Fly> fly);

    /** 单条插入 */
    void insertFlySingle(Fly fly);
}
