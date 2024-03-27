package org.come.action.summoning;

import come.tool.Good.AddGoodAction;
import come.tool.Stall.AssetUpdate;
import io.netty.channel.ChannelHandlerContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.come.action.IAction;
import org.come.action.buy.AddGoodUtil;
import org.come.bean.LoginResult;
import org.come.bean.XXGDBean;
import org.come.entity.Goodstable;
import org.come.entity.RoleSummoning;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

/**
 * 宠物放生,客户端发来该宠物的标识，删除该宠物
 * @author 叶豪芳
 * @date 2018年1月4日 上午10:42:21
 * 
 */
//修复放生召唤兽
public class PetReleaseAction implements IAction {
	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		LoginResult loginResult = GameServer.getAllLoginRole().get(ctx);
		if(loginResult==null){return;}
		String[] vs = message.split("=");//葫芦娃
		RoleSummoning pet = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRgID(new BigDecimal(vs[1]));
		if(pet==null){return;}
		if(pet.getRoleid().compareTo(loginResult.getRole_id())!=0){return;}
		// 删除召唤兽
		AllServiceUtil.getRoleSummoningService().deleteRoleSummoningBySid(new BigDecimal(vs[1]));
		// 化莲
		if (vs[0].equals("1")) {//葫芦娃
			String id = null;
			switch (pet.getSummoningid()) {
				case "754": id = "364";break;
				case "755": id = "365";break;
				case "756": id = "359";break;
				case "757": id = "360";break;
				case "758": id = "361";break;
				case "759": id = "362";break;
				case "760": id = "363";break;
			}
			if (id != null) {
				XXGDBean bean = new XXGDBean();
				bean.setId(id);
				bean.setSum(1);
				Goodstable good = GameServer.getGood(new BigDecimal(id));
				if( good==null ){return;}
				AssetUpdate assetUpdate=new AssetUpdate();
				assetUpdate.setMsg(bean.getSum()+"个"+good.getGoodsname());
				AddGoodAction.addGood(assetUpdate, good, loginResult, loginResult.getRoleData(), bean, 3);
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
			}

		}//葫芦娃
	}
}
