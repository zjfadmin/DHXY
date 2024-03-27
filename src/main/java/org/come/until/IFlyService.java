package org.come.until;

import org.come.entity.Fly;
import org.come.entity.FlyRoleUser;
import org.come.entity.Mount;
import org.come.entity.MountRoleUser;

import java.math.BigDecimal;
import java.util.List;

public interface IFlyService {
    // 查找所有飞行
    List<Fly> selectAllFlys();
    /**
     * 查找角色所有飞行
     */
    List<Fly> selectFlysByRoleID( BigDecimal roleID );

    /**
     * 查找角色飞行
     */
    Fly selectFlysByMID( BigDecimal mid );

    /**
     * 删除角色飞行
     */
    void deleteFlysByMid( BigDecimal roleID );

    /**修改飞行属性*/
    void updateFly( Fly fly);
    /**修改飞行属性刚从redis取出*/
    void updateFlyRedis(Fly fly);
    /**
     * 添加飞行
     * @param fly
     */
    void insertFly( Fly fly);
    void deleteFlysByMidsql(BigDecimal roleID);
    void updateFlysql(Fly fly);
    void insertFlysql(Fly fly);
    BigDecimal selectMaxID();


    List<FlyRoleUser> selectFly(FlyRoleUser mru);

    Integer selectFlyCount(FlyRoleUser mru);

    /** HGC-2020-01-20 */
    /** 批量删除角色飞行 */
    void deleteFlysByMidList(List<BigDecimal> roleID);

    /** 批量修改飞行属性 */
    void updateFlyList(List<Fly> fly);

    /** 批量添加飞行 */
    void insertFlyList(List<Fly> fly);

    /** 单条插入 */
    void insertFlySingle(Fly fly);
}
