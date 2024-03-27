package org.come.service;

import org.come.bean.TtModel;
import org.come.entity.AppVersion;
import org.come.extInterBean.Goodsrecord2;
import org.come.extInterBean.ShopBuyRecordReqBean;
import org.come.extInterBean.ShopBuyRecordResultBean;
import org.come.extInterBean.ShopBuyTypeResult;

import java.math.BigDecimal;
import java.util.List;

// 三端
public interface TtModelService {


	List<TtModel> getTtConfig();

	void updateTtConfig(TtModel ttModel);
}
