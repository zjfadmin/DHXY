package org.come.action.goodsExchange;

import come.tool.Good.UsePetAction;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Stall.AssetUpdate;
import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.action.qiandao.QIanDaoChoujaingAction;
import org.come.bean.FreshPackBean;
import org.come.bean.GoodsForGoodsBean;
import org.come.bean.LoginResult;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
       
* 类说明:物品兑换物品
 */
public class GoodsForGoodsChangeAction implements IAction{
	static String CHECKTS1="兑换物品不足";
	static String CHECKTS2=Agreement.getAgreement().PromptAgreement("兑换物品不足");
	static String CHECKTS3=Agreement.getAgreement().PromptAgreement("兑换物品不足");	
    /**
     * messages封装格式   pxgoods=id 标识id    
     */
	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// 获取登录角色信息
		LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);
		GoodsForGoodsBean goodsbean =GsonUtil.getGsonUtil().getgson().fromJson(message, GoodsForGoodsBean.class);
		//获取兑换的物品

		//String meq[]=message.split("\\=");
		
		//if(meq.length<=1) return;
		
		GoodsForGoodsBean goodsBean=initForGoodsID(goodsbean.getId()+"");
		if(goodsBean==null) return;
        
		/**
		 * 获取消耗物品 信息
		 *  goodsBean.getDelte() ==>   204=1|205=1|206=1
		 *  xiaohao[] = {201=1,205=1,206=1}
		 */
		String xiaohao[]=goodsBean.getDelte().split("\\|");
		/**
		 * 获取兑换奖品信息
		 * goodsBean.getGetGoods() ==> 42=1|41=1
		 * huode[] = {42=1,41=1}
		 */
		String huode[] = goodsBean.getGetGoods().split("\\|");
		
		if (xiaohao.length == 0) {
			return;
		}
		for (int i = 0; i < xiaohao.length; i++) {
			String[] needGoods = xiaohao[i].split("\\=");
			// 查询数据库里物品是否足够
			List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(roleInfo.getRole_id(), new BigDecimal(needGoods[0]) );
			if(sameGoodstable.size()<=0){
				SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement(CHECKTS1));
			return;}
			Goodstable good = sameGoodstable.get(0);
			if (good==null||(good.getUsetime() < Integer.parseInt(needGoods[1]))) {
				// 兑换物品不够
				SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement(CHECKTS1));
				return;
			}
			// 消耗物品
			 UsePetAction.useGood(good, Integer.parseInt(needGoods[1]));
		}
		
		// 获得物品
		 for (int j = 0; j < huode.length; j++) {
			 String[] getGoods = huode[j].split("\\=");
			 Goodstable goodstable = GameServer.getAllGoodsMap().get(new BigDecimal(getGoods[0]));
			 goodstable.setUsetime(Integer.parseInt(getGoods[1]));
			 goodstable=goodstable.clone();
			 goodstable.setRole_id(roleInfo.getRole_id());
			// 插入数据库
		//	AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
			// 发送客户端
			AssetUpdate assetUpdate = new AssetUpdate();
			//assetUpdate.setGood(goodstable);
			
			//更新物品  13 兑换物品获得
			QIanDaoChoujaingAction.diejiaGoods(goodstable, assetUpdate, 13, roleInfo.getRole_id());
			assetUpdate.setMsg(Integer.parseInt(getGoods[1]) +"个"+goodstable.getGoodsname());
			assetUpdate.setType(AssetUpdate.INTEGRATION);
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate))); 
		}
		 
		 
		 //更新背包信息
		 freshBack(roleInfo.getRole_id(), ctx);
	
		
		//判断对应的消耗物品是否满足
//		Goodstable good = AllServiceUtil.getGoodsTableService().getGoodsByRgID(new BigDecimal(vs[0]));
//		SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("您已经购买过该礼包!"));
		//判断消耗的物品够不够
	
		//查询库里面物品是否足够	Goodstable good = AllServiceUtil.getGoodsTableService().getGoodsByRgID(new BigDecimal(vs[0]));
		//物品使用  UsePetAction.useGood(good, 1);
		
		//获得物品使用
		// 获得该物品（推广礼盒）
//		Goodstable goodstable = GameServer.getAllGoodsMap().get(new BigDecimal(exchange.getGoodsid()));
	    //goodstable=goodstable.clone();
		//goodstable.setRole_id(roleInfo.getRole_id());
		
		// 插入数据库
		//AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
		
		// 发送客户端
		//AssetUpdate assetUpdate = new AssetUpdate();
		//assetUpdate.setGood(goodstable);
		//assetUpdate.setMsg(1+"个"+goodstable.getGoodsname());
		//assetUpdate.setType(AssetUpdate.INTEGRATION);
		//SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  

	}
	
	
	/**
	 * 根据id 返回对应的bean
	 */
	public GoodsForGoodsBean initForGoodsID(String godis){
		
		//遍历所有的物品‘
		for (int i = 0; i < GameServer.GoodsForGoods.size(); i++) {
			
			if(GameServer.GoodsForGoods.get(i).getId()==Integer.valueOf(godis)) return GameServer.GoodsForGoods.get(i);
		}
		return null;
	
	}
     
	/**
	 * 
	 * @param roleid,通道
	 */
	
	public static void freshBack(BigDecimal roleid,ChannelHandlerContext ctx){
		 //更新背包信息
		 List<Goodstable> goods = AllServiceUtil.getGoodsTableService().getGoodsByRoleID(roleid);
		 FreshPackBean fresh=new FreshPackBean();
		 
		 fresh.setGoods(goods);
		 
		 RoleData data = RolePool.getRoleData(roleid);
		 fresh.setPackRecord(data.getPackRecord());
		 SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().FRESHPACkAgreement(GsonUtil.getGsonUtil().getgson().toJson(fresh))); 


	}
}
