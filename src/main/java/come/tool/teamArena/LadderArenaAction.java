package come.tool.teamArena;

import come.tool.Good.DropUtil;
import come.tool.newTeam.TeamBean;
import come.tool.newTeam.TeamUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.bean.TtModel;
import org.come.handler.SendMessage;
import org.come.model.Dorp;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.service.TtModelService;
import org.come.until.AllServiceUtil;

import java.util.List;
import java.util.stream.Collectors;

public class LadderArenaAction implements IAction{
	//后端发前端
	//D 关闭关闭  提示标语                                                                 String
	//O 打开界面  出现10秒倒计时  如果都没全部同意 就退出    List<TeamRole> teams
	//A 修改状态                                                                                   String
	//E 获得匹配队伍信息  并进入5秒倒计时                                  List<TeamRole> teams
	//3 + 7 入门/进阶/精锐/英杰/豪侠/宗师/武圣/王者
	//前端发后端
	//O 申请匹配
	//A 同意
	//D 取消
	@Override
	public void action(ChannelHandlerContext ctx, String message) {

		LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);

		if (message.equals("4") || message.equals("5") || message.equals("6")) {
			if (StringUtils.isBlank(roleInfo.getTTJIANGLI())) {
				//初始化天梯胜场奖励
				roleInfo.setTTJIANGLI("0|0|0");
			}
//            roleInfo.setTTJIANGLI("0|0|0");

			int i = Integer.parseInt(message);

			String[] ttAward = roleInfo.getTTJIANGLI().split("\\|");

			if (i == 4) {
				if (roleInfo.getTtVictory() < 1) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你的胜利场次不足,无法领取奖励"));
					return;
				}
				// 过滤重复领奖
				if (ttAward[0].equals("1")) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你已经领取过该奖励,请勿重复领取"));
					return;
				}

				Dorp dorp = GameServer.getDorp("7004");
				DropUtil.getDrop4(roleInfo, dorp.getDorpValue(), "#G{角色名}#Y领取了天梯1胜奖励#90", 25, 1D, null, "", "", null);
				ttAward[0] = 1+"";
				String join = StringUtils.join(ttAward, "|");
				roleInfo.setTTJIANGLI(join);
			}

			if (i == 5) {
				if (roleInfo.getTtVictory() < 10) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你的胜利场次不足,无法领取奖励"));
					return;
				}
				//过滤重复领奖
				if (ttAward[1].equals("1")) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你已经领取过该奖励,请勿重复领取"));
					return;
				}

				Dorp dorp = GameServer.getDorp("7005");
				DropUtil.getDrop4(roleInfo, dorp.getDorpValue(), "#G{角色名}#Y领取了天梯10胜奖励#90", 25, 1D, null, "", "", null);
				ttAward[1] = 1+"";
				String join = StringUtils.join(ttAward, "|");
				roleInfo.setTTJIANGLI(join);
			}

			if (i == 6) {
				if (roleInfo.getTtVictory() < 20) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你的胜利场次不足,无法领取奖励"));
					return;
				}
				// 过滤重复领奖
				if (ttAward[2].equals("1")) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你已经领取过该奖励,请勿重复领取"));
					return;
				}

				Dorp dorp = GameServer.getDorp("7006");
				DropUtil.getDrop4(roleInfo, dorp.getDorpValue(), "#G{角色名}#Y领取了天梯20胜奖励#90", 25, 1D, null, "", null, null);
				ttAward[2] = 1+"";
				String join = StringUtils.join(ttAward, "|");
				roleInfo.setTTJIANGLI(join);
			}

			return;

		}


		// TODO Auto-generated method stub
		if (LadderArenaUtil.teamArenaThread == null) {
			//天梯开启时间限制
			TtModelService ttModelService = AllServiceUtil.getTtModelService();
			List<TtModel> ttConfig = ttModelService.getTtConfig();
			//获取开启的赛季
			List<TtModel> openTT = ttConfig.stream().filter(item -> item.getIsOpen() == 1).collect(Collectors.toList());
			if (openTT.size() == 0) {
				SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("当前赛季已经关闭#32"));
				return;
			}

			String time = openTT.get(0).getStartHour() + ":00-" + (openTT.get(0).getStartHour() )+ 1 + ":00";
			SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("天梯巅峰战#G还未开始!#R开放时间为:" + time));
			LadderArenaUtil.teamArenaOpen();
			return;
		}
		if (roleInfo == null) {
			return;
		}
		TeamBean bean = TeamUtil.getTeam(roleInfo.getTroop_id());
		if (bean == null) {
			SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你还没有队伍无法参与"));
			return;
		}

		if (message.equals("O")) {//申请匹配
			if (!bean.isCaptian(roleInfo.getRole_id())) {
				SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你不是队长"));
				return;
			}
			LadderArenaUtil.addAffirm(ctx, bean);
		} else if (message.equals("A")) {//同意
			LadderArenaUtil.confirm(bean, roleInfo, true);
		} else if (message.equals("D")) {//取消
			LadderArenaUtil.confirm(bean, roleInfo, false);
		}
	}
}
