package org.come.action.sys;

import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.bean.TtModel;
import org.come.bean.UserRoleArrBean;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.service.TtModelService;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;
/**
 * 排行榜
 * @author 叶豪芳
 *
 */
public class OrderByRoleAction implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {

		// 获取排行榜类型
		if (message == null) return;
		Integer type = Integer.parseInt(message);

		UserRoleArrBean bean = new UserRoleArrBean();

		// 获取排行榜内容
		List<LoginResult> list = GameServer.allBangList.get(type);
		//天梯赛季信息
		if (type == 8) {
			TtModelService ttModelService = AllServiceUtil.getTtModelService();
			List<TtModel> ttConfig = ttModelService.getTtConfig();
			//获取开启的赛季
			if (ttConfig.size() != 0) {
				String seasonInfo = "";
				TtModel ttModel = ttConfig.get(0);
				String str = "第 " + ttModel.getCurrentSeason() + " 赛季";
				bean.setCurrSeason(str);
				Date seasonStartTime = ttModel.getSeasonStartTime();
				Date seasonEndTime = ttModel.getSeasonEndTime();

				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(seasonStartTime);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(seasonEndTime);
				int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
				int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

				seasonInfo = new SimpleDateFormat("yyyy年MM月dd日").format(seasonStartTime) +
						" - " + new SimpleDateFormat("yyyy年MM月dd日").format(seasonEndTime);
				if (ttModel.getIsOpen() == 1)
					seasonInfo = seasonInfo + ",当前赛季距离结束还有" + (day2 - day1) + "天";
				else if (ttModel.getIsOpen() == 3)
					seasonInfo = seasonInfo + ",当前赛季已结束";
				bean.setSeasonInfo(seasonInfo);
				System.out.println(seasonInfo);
			}
		}

		// 返回客户端
		bean.setList(list);
		bean.setIndex(type);
		String msg = Agreement.getAgreement().pankinglistAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean));
		SendMessage.sendMessageToSlef(ctx, msg);

	}

}
