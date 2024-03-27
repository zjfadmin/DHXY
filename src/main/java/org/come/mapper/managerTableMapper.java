package org.come.mapper;


import java.math.BigDecimal;
import java.util.List;

import org.come.annotation.MyBatisAnnotation;
import org.come.bean.managerTable;
@MyBatisAnnotation
public interface managerTableMapper {
    /**
     *
     * @mbggenerated 2018-04-09
     * 
     */
    int deleteByPrimaryKey(BigDecimal manaeid);

    /**
     *
     * @mbggenerated 2018-04-09
     */
    int insert(managerTable record);

    /**
     *
     * @mbggenerated 2018-04-09
     */
    int insertSelective(managerTable record);

    /**
     *
     * @mbggenerated 2018-04-09
     */
    managerTable selectByPrimaryKey(BigDecimal manaeid);

    /**
     *
     * @mbggenerated 2018-04-09
     */
    int updateByPrimaryKeySelective(managerTable record);

    /**
     *
     * @mbggenerated 2018-04-09
     */
    int updateByPrimaryKey(managerTable record);
    
    /*
     * 依据用户名查对象
     */
    managerTable selectByUsername(managerTable record);
    
    
    List<managerTable> selectManageForPage(int pageNumber);
}