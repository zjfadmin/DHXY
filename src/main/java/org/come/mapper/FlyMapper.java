package org.come.mapper;

import org.apache.ibatis.annotations.Param;
import org.come.annotation.MyBatisAnnotation;
import org.come.entity.Fly;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新加飞行器
 */
@MyBatisAnnotation
public interface FlyMapper {

    void saveBatch(List<Fly> addList);

    List<Fly> selectAllFlys();

    List<Fly> selectFlysByRoleID();

    Integer insertFlyToSql(Fly fly);

    Integer deleteFlysByFidList(List<BigDecimal> delList);

    Integer deleteFlysByFid(BigDecimal fid);

    Integer updateFlyList(List<Fly> addList);

    Integer updateFly(@Param("fly") Fly fly);
}