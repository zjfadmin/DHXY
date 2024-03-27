package org.come.mapper;


import org.come.annotation.MyBatisAnnotation;
import org.come.bean.TtModel;
import org.come.entity.Friend;

import java.util.List;

/**
 * 天天配置
 * @author Administrator
 *
 */
@MyBatisAnnotation
public interface TtModelMapper {


	List<TtModel> getTtConfig();

	void updateTtConfig(TtModel ttModel);
}
