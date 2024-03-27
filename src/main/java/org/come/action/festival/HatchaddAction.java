package org.come.action.festival;

import com.gl.util.EggUtil;
import come.tool.Good.DropUtil;
import come.tool.Role.RolePool;
import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.bean.NChatBean;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.model.Dorp;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 添加孵化值
 * @author Administrator
 *
 */
public class HatchaddAction implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		
		// 孵化值加1
		int hatch = HatchvalueAction.hatch.addAndGet(1);

		// 如果孵化值达到50，发放物品
		for (int i = 1; i < 500000; i++) {
			if (hatch == i) {
				// 发送公告
				NChatBean bean = new NChatBean();
				bean.setId(4);
//				bean.setMessage("孵蛋成功，请注意接收奖励！！！");
//				String msg = Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean));
//				SendMessage.sendMessageToAllRoles(msg);
				LoginResult loginResult = GameServer.getAllLoginRole().get(ctx);
				BigDecimal roleid = loginResult.getRole_id();
				List<Goodstable> goodsList = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(roleid, EggUtil.EGG_ID);
				if (goodsList.size() > 0) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你已经有一个了，带着它去战斗先把它孵化出来吧"));
					return;
				}else {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("领取元气蛋成功，请注意接收奖励！！！"));

				}
				// 查询玩家是否已经拥有元气蛋，如果有则提示
				if (RolePool.getRoleData(roleid).isGoodFull()) {
					SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("你的物品栏已经满了，请留出至少一个空位"));
					return;
				}
				Dorp dorp = GameServer.getDorp("99999");
				// 发送随机奖励给所有玩家
				DropUtil.getDrop(loginResult, dorp.getDorpValue(), "元旦孵蛋奖励", 25, 1D, null);
			}

		}
	}

}
