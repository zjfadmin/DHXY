package org.come.action.suit;

import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.bean.QualityClBean;
import org.come.bean.UseCardBean;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.tool.Goodtype;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Stall.AssetUpdate;

import java.util.ArrayList;
import java.util.List;

public class QualityCAciton implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// TODO Auto-generated method stub
		QualityClBean clBean = GsonUtil.getGsonUtil().getgson().fromJson(message,QualityClBean.class);
		clBean=QualityCPool.getcPool().getExtra(clBean.getType(), clBean.getRgid());
		if (clBean==null) {return;}
		if (clBean.getType()>=70&&clBean.getType()<=79) {
			RoleData roleData=RolePool.getRoleData(clBean.getRgid());
			if (roleData==null) {return;}
			int lvl=clBean.getType()-70;
			UseCardBean cardBean=roleData.getLimit("单人竞技场");
			if (cardBean==null||cardBean.getQls().length<lvl) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你还未解锁对应的称谓"));
				return;
			}
			cardBean.upValue(clBean.getNewAttr(), lvl-1);
			AssetUpdate assetUpdate = new AssetUpdate(AssetUpdate.USEGOOD);
			assetUpdate.setUseCard(cardBean);
			assetUpdate.upmsg("替换属性成功");
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
			AllServiceUtil.getGoodsrecordService().insertGoodsrecord(clBean.getRgid(), null, 50201, clBean.getRgid(), "单人竞技场属性", cardBean.getValue(), 1);
		}else if (clBean.getType()==8||clBean.getType()==9){
			Goodstable good=AllServiceUtil.getGoodsTableService().getGoodsByRgID(clBean.getRgid());
			if (good==null)return;
			String []value=good.getValue().split("\\|");
			List<String> value1=new ArrayList<>();
			for (String s : value) {
				if (!s.startsWith("点粹属性")) {
					value1.add(s);
				}
			}
			value1.removeIf(bb -> bb.equals(""));
			StringBuilder mes=null;
			for (String vv : value1) {
				if (vv.startsWith("培养")) {
					assert mes != null;
					mes.append("|");mes.append(clBean.getNewAttr());mes.append("|");
				}
				if (mes==null)
					mes = new StringBuilder(vv);
				else mes.append("|").append(vv);
			}
			assert mes != null;
			good.setValue(mes.toString());
			AllServiceUtil.getGoodsTableService().updateGoodRedis(good);
			AllServiceUtil.getGoodsrecordService().insert(good, null, 1, 13);//添加记录
		}else if (clBean.getType()==1) {
			Goodstable goodstable=AllServiceUtil.getGoodsTableService().getGoodsByRgID(clBean.getRgid());
			if (goodstable==null) {return;}
			goodstable.setValue(SuitComposeAction.newExtra(goodstable.getValue().split("\\|"), clBean.getType()-1, clBean.getNewAttr(), Goodtype.GodEquipment(goodstable.getType())));
			AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
			AllServiceUtil.getGoodsrecordService().insert(goodstable, null, 1, 13);//添加记录
		}else {
			Goodstable goodstable=AllServiceUtil.getGoodsTableService().getGoodsByRgID(clBean.getRgid());
			if (goodstable==null) {return;}
			goodstable.setValue(SuitComposeAction.newExtra(goodstable.getValue().split("\\|"), clBean.getType()-1, clBean.getNewAttr(), Goodtype.GodEquipment(goodstable.getType())));
			AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
			AllServiceUtil.getGoodsrecordService().insert(goodstable, null, 1, 13);//添加记录
		}
	}

}
