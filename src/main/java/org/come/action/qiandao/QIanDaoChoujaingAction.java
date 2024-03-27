package org.come.action.qiandao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.action.goodsExchange.GoodsForGoodsChangeAction;
import org.come.bean.ApplyQianDao;
import org.come.bean.LoginResult;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.tool.EquipTool;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.Good.UsePetAction;
import come.tool.Stall.AssetUpdate;

public class QIanDaoChoujaingAction implements IAction {
	LoginResult loginResult;
	ApplyQianDao applyQianDao;
	ChannelHandlerContext ctx1;
	Random random=new Random();
	static String CHECKTS1=Agreement.getAgreement().PromptAgreement("不符合抽奖条件");
	static String CHECKTS2=Agreement.getAgreement().PromptAgreement("补签卡不足");
	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// TODO Auto-generated method stub
		ctx1=ctx;
		loginResult= GameServer.getAllLoginRole().get(ctx); //获取当前对象
		applyQianDao=GsonUtil.getGsonUtil().getgson().fromJson(message, ApplyQianDao.class);

		//判断执行方法  2 签到    3  补签  4 表示抽奖
		if(applyQianDao.getType()==2){
			applyQianDao.setQiandaoday(qiandaoDay(applyQianDao.getDayri()));
			//设置抽奖部分
			applyQianDao.setChoujianBean(APPQIanDaoAction.getQiandaoCHoujiang());
			//设置签到的日期
			applyQianDao.setChpujiangjihe(loginResult.getQIANDAOCHOUJIANG());

		} if(applyQianDao.getType()==3){
			//补签
			//判断是否拥有补签物品
			// 查询数据库里物品是否足够
			List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(loginResult.getRole_id(), new BigDecimal(66666) );
			if(sameGoodstable.size()<=0){
				SendMessage.sendMessageToSlef(ctx, CHECKTS2);
				return;
			}else{
				//进行补签
				Goodstable good = sameGoodstable.get(0);
				// 消耗物品
				UsePetAction.useGood(good, 66666);
				//进行补签
				applyQianDao.setQiandaoday(qiandaoDay(applyQianDao.getDayri()));
				//设置抽奖部分
				applyQianDao.setChoujianBean(APPQIanDaoAction.getQiandaoCHoujiang());
				//设置签到的日期
				applyQianDao.setChpujiangjihe(loginResult.getQIANDAOCHOUJIANG());
			}
			GoodsForGoodsChangeAction.freshBack(loginResult.getRole_id(), ctx);
		} if(applyQianDao.getType()==4){
			//设置抽奖信息
			//判断是否符合抽奖等级
			if(sureQianDaoOrNo(applyQianDao.getDayri(), loginResult.getQIANDAO())){
				applyQianDao.setChpujiangjihe(choujainginit(applyQianDao.getDayri()));

				applyQianDao.setChpujiangjihe(loginResult.getQIANDAOCHOUJIANG());
				//设置抽奖的次数
				applyQianDao.setQiandaoday(loginResult.getQIANDAO());
				applyQianDao.setChoujianBean(APPQIanDaoAction.getQiandaoCHoujiang());
			}else{
				SendMessage.sendMessageToSlef(ctx, CHECKTS1);
				return;
			}
		}
		//返回当前信息
		SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().APPQIANDAOAgreement(GsonUtil.getGsonUtil().getgson().toJson(applyQianDao)) );


	}


	/**
	 * 判断是否满足抽奖条件  1 3 7 13 311
	 */
	public boolean sureQianDaoOrNo(int type,String qiandaotianshu){

		if(qiandaotianshu==null) return false;

		String mes[]=qiandaotianshu.split("\\|");

		if(type==1){
			if(mes.length>=1) return true;
			else return false;
		}else if(type==2){
			if(mes.length>=3) return true;
			else return false;
		}else if(type==3){
			if(mes.length>=7) return true;
			else return false;
		}else if(type==4){
			if(mes.length>=13) return true;
			else return false;
		}else if(type==5){
			if(mes.length>=30) return true;
			else return false;
		}
		return false;




	}

	/**
	 * 签到
	 */
	public String qiandaoDay(int dayri){
		if(dayri<=0) return null;
		if(dayri>31) return null;
		//判断当前日期是否已经签到 否则进行签到
		if(!(loginResult.getQIANDAO()==null)){
			Boolean b = false;
			String loString[]=loginResult.getQIANDAO().split("\\|");
			for (int i = 0; i < loString.length; i++) {
				if(Integer.valueOf(loString[i])==dayri){
					b = true;
					break;
				}
			}
			if(b)
				return loginResult.getQIANDAO();
		}
		//否则进行签到
		if(!(loginResult.getQIANDAO()==null))
			loginResult.setQIANDAO(loginResult.getQIANDAO()+"|"+dayri);
		else loginResult.setQIANDAO(dayri+"");
		//根据签到的天数返回对应的物品
		forGoods(GameServer.getQiandaoMap().get(dayri+"").getJiangliMap());
		return loginResult.getQIANDAO();
	}

	/**
	 * 补签
	 */



	/**
	 * 抽奖 抽奖的级别   1 3 7 13 30  (1 2 3 4 5)
	 */
	public String choujainginit(int dayri){
		//判断是否抽奖
		if(!(loginResult.getQIANDAOCHOUJIANG()==null)){
			String choujianday[]=loginResult.getQIANDAOCHOUJIANG().split("\\|");

			for (int i = 0; i < choujianday.length; i++) {
				if(Integer.valueOf(choujianday[i])==dayri){
					break;
				}
			}}
		//没有抽奖过 则进行抽奖
		if(!(loginResult.getQIANDAOCHOUJIANG()==null))
			loginResult.setQIANDAOCHOUJIANG(loginResult.getQIANDAOCHOUJIANG()+"|"+dayri);
		else loginResult.setQIANDAOCHOUJIANG(dayri+"");
		//进行物品派送
		forGoods(GameServer.getQiandaoMap().get((31+dayri)+"").getJiangliMap());

		//返回此时抽奖的状态

		return loginResult.getQIANDAOCHOUJIANG();
	}

	/**
	 * 根据对象进行派发物品  掉落
	 */
	public void  forGoods(String daropMes){
		String goodmes[]=daropMes.split("\\&");//解析物品

		for (int i = 0; i < goodmes.length; i++) {
			//根据概率进行解析  7075-81188$1$0.6
			String goodis[]=goodmes[i].split("\\$");
			//判断概率是否获得
			if(SureOrNo(goodis[2])){
				//根据概率随机获得集合里面的物品
				for (int j = 0; j < Integer.valueOf(goodis[1]); j++) {

					String goodismes[]=goodis[0].split("\\-");

					//	for (int k = 0; k < goodismes.length; k++) {
					//根据信息玩家获得签到物品

					Goodstable goodstable = GameServer.getAllGoodsMap().get(new BigDecimal(goodismes[random.nextInt(goodismes.length-1)]));
					if(goodstable != null){
						goodstable.setUsetime(1);
						goodstable=goodstable.clone();
						goodstable.setRole_id(loginResult.getRole_id());



						// 发送客户端
						AssetUpdate assetUpdate = new AssetUpdate();
						//签到获得type  12
						diejiaGoods(goodstable, assetUpdate, 12,loginResult.getRole_id());
						assetUpdate.setMsg("1个"+goodstable.getGoodsname());
						assetUpdate.setType(AssetUpdate.INTEGRATION);
						SendMessage.sendMessageToSlef(ctx1,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
					}

					//}
				}
			}

		}

	}

	/**
	 * 判断物品是否可叠加
	 */
	public static  void diejiaGoods(Goodstable goodstable,AssetUpdate assetUpdate,int type,BigDecimal roleBigDecimal){
		if (EquipTool.canSuper(goodstable.getType())) {// 可叠加
			int sum = goodstable.getUsetime();
			// 判断该角色是否拥有这件物品
			List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(roleBigDecimal, goodstable.getGoodsid());
			if (sameGoodstable.size() != 0) {
				// 修改使用次数
				int uses = sameGoodstable.get(0).getUsetime() + sum;
				sameGoodstable.get(0).setUsetime(uses);
				// 修改数据库
				AllServiceUtil.getGoodsTableService().updateGoodRedis(sameGoodstable.get(0));
				assetUpdate.setGood(sameGoodstable.get(0));
				AllServiceUtil.getGoodsrecordService().insert(sameGoodstable.get(0), null,sum, type);
			} else {
				goodstable.setUsetime(sum);
				// 插入数据库
				AllServiceUtil.getGoodsTableService().insertGoods(goodstable);
				assetUpdate.setGood(goodstable);
				AllServiceUtil.getGoodsrecordService().insert(goodstable, null, sum, type);
			}

		}
	}

	/**
	 * 根据概率判断是否获得
	 */
	public boolean SureOrNo(String gailv){

		Double double1=Double.valueOf(gailv)*100;

		if(random.nextInt(100)>(100-double1.intValue())) return true;
		else  return false;

	}
}
