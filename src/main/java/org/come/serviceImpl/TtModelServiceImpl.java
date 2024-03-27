package org.come.serviceImpl;

import org.come.bean.TtModel;
import org.come.entity.AppVersion;
import org.come.extInterBean.Goodsrecord2;
import org.come.extInterBean.ShopBuyRecordReqBean;
import org.come.extInterBean.ShopBuyRecordResultBean;
import org.come.extInterBean.ShopBuyTypeResult;
import org.come.handler.MainServerHandler;
import org.come.mapper.AppVersionMapper;
import org.come.mapper.TtModelMapper;
import org.come.service.AppVersionService;
import org.come.service.TtModelService;
import org.come.tool.WriteOut;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;
import org.come.until.MybatisUntil;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 三端
public class TtModelServiceImpl implements TtModelService {

	private TtModelMapper ttModelMapper;


	public TtModelServiceImpl() {
		ApplicationContext ctx = MybatisUntil.getApplicationContext();
		// id为类名且首字母小写才能被自动扫描扫到
		ttModelMapper = (TtModelMapper) ctx.getBean("ttModelMapper");
	}

	@Override
	public List<TtModel> getTtConfig() {

		return ttModelMapper.getTtConfig();
	}

	@Override
	public void updateTtConfig(TtModel ttModel) {
		ttModelMapper.updateTtConfig(ttModel);
	}
}
