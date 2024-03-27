package org.come.action.qiandao;

import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.bean.ApplyQianDao;
import org.come.bean.LoginResult;
import org.come.bean.QIanDaoBean;
import org.come.entity.Goodstable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
* 类说明:打开签到面板
 */
public class APPQIanDaoAction implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// TODO Auto-generated method stub
		LoginResult loginResult= GameServer.getAllLoginRole().get(ctx); //获取当前对象
		ApplyQianDao applyQianDao=GsonUtil.getGsonUtil().getgson().fromJson(message, ApplyQianDao.class); 
		
		if(applyQianDao.getType()!=1) return;
		
		
		//设置签到的日期
		applyQianDao.setChpujiangjihe(loginResult.getQIANDAOCHOUJIANG());
		
		//设置抽奖的次数
		applyQianDao.setQiandaoday(loginResult.getQIANDAO());
		
		//获取对应的抽奖信息
		//getQiandaoCHoujiang
		applyQianDao.setChoujianBean(getQiandaoCHoujiang());
		
		//返还给客户端
		SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().APPQIANDAOAgreement(GsonUtil.getGsonUtil().getgson().toJson(applyQianDao)) );
		
	}
	/**
	 * 获取对应的抽奖信息
	 */
   public static  Map<Integer, List<Goodstable>>  getQiandaoCHoujiang(){
      Map<Integer, List<Goodstable>> returnGoods=new HashMap<Integer, List<Goodstable>>();
	   for (Entry<String, QIanDaoBean> entry  : GameServer.qiandaoMap.entrySet()) {
		   if(entry.getKey().endsWith("qdid")) continue;
           if(Integer.valueOf(entry.getKey())>31){
        	   
        	   returnGoods.put(Integer.valueOf(entry.getKey()), getGoodstable(entry.getValue()));
        	   
           }
       }
	     return returnGoods;
   }
   
   //根据对象返回对应的bean 
   public static List<Goodstable> getGoodstable(QIanDaoBean daoBean){
	   List<Goodstable> lisget=new ArrayList<>();
	   
	   String mes[]=daoBean.getJiangliMap().split("\\&");
	   for (int i = 0; i < mes.length; i++) {
		String mesg[]=mes[i].split("\\$");
		if(mesg.length!=3) continue;
		//根据id 进行返回对象 -
		 String goodsM[]=mesg[0].split("\\-");
		 for (int j = 0; j < goodsM.length; j++) {

			 Goodstable goodstable=GameServer.getAllGoodsMap().get(new BigDecimal(goodsM[j]));
			 if(goodstable==null) continue;
			 
			 lisget.add(goodstable);
		}
		
	  }
	return lisget;
	   
   }
}
