package org.come.action.give;

import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.come.action.IAction;
import org.come.action.monitor.MonitorUtil;
import org.come.action.monster.ClickMonsterAction;
import org.come.bean.Bbuy;
import org.come.bean.GiveGoodsBean;
import org.come.bean.LoginResult;
import org.come.entity.Goodstable;
import org.come.entity.Record;
import org.come.entity.TransInfoVo;
import org.come.handler.SendMessage;
import org.come.model.Lshop;
import org.come.model.Robots;
import org.come.protocol.Agreement;
import org.come.redis.RedisCacheUtil;
import org.come.server.GameServer;
import org.come.task.MapMonsterBean;
import org.come.task.MonsterUtil;
import org.come.tool.EquipTool;
import org.come.tool.Goodtype;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.Battle.BattleMixDeal;
import come.tool.Mixdeal.AnalysisString;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Stall.AssetUpdate;


/**
 * 给于,客户端发送GiveGoodsBean,返回GoodsListResultBean
 * @author 叶豪芳
 * @date 2017年12月20日 上午11:39:43
 * 
 */ 
public class GiveAction implements IAction{

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		LoginResult loginResult=GameServer.getAllLoginRole().get(ctx);
		if (loginResult==null) {return;}
		GiveGoodsBean giveBean = GsonUtil.getGsonUtil().getgson().fromJson(message, GiveGoodsBean.class);
		if (giveBean.getType()==0) {
			ChannelHandlerContext ctx2=GameServer.getRoleNameMap().get(giveBean.getOtherName());
			LoginResult otherRole=ctx2!=null?GameServer.getAllLoginRole().get(ctx2):null;
			if (otherRole==null||otherRole.getRole_id().compareTo(loginResult.getRole_id())==0) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("对方不在线"));
				return;
			}	
			RoleData roleData=RolePool.getRoleData(otherRole.getRole_id());
			if (roleData.getRoleSystem().getIsGood()==0) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("对方关闭物品接收功能"));
				return;
			}
			Goodstable goodstable=null;
			if (giveBean.getRgid()!=null) {
				goodstable=getGiveGood(giveBean.getRgid(), loginResult.getRole_id(), giveBean.getSum(),true);
				if (goodstable==null) {
					SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你给与的物品处于异常"));
					return;
				}
			}
			if (giveBean.getGold()!=null) {
				if (giveBean.getGold().longValue()==0) {
					giveBean.setGold(null);
				}else if (giveBean.getGold().longValue()<0) {
					AllServiceUtil.getRecordService().insert(new Record(5,"给与异常:角色id:"+loginResult.getRole_id()+"_"+message));
					return;
				}else if (loginResult.getGold().longValue()<giveBean.getGold().longValue()) {
					SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你给与的金钱不足"));
					return;
				}			
			}
			
			if (roleData.isGoodFull()) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("给予失败，对方的背包已满"));
				return;
			}

			AssetUpdate giveData=new AssetUpdate(AssetUpdate.USEGOOD);//给与者
			giveData.setMsg("给与物品成功");
			AssetUpdate assetUpdate=new AssetUpdate(AssetUpdate.USEGOOD);//被接收者
			StringBuffer assetBuffer=new StringBuffer();
			assetBuffer.append("#G");
			assetBuffer.append(loginResult.getRolename());
			assetBuffer.append("#Y给你");
			Long transId = RedisCacheUtil.getTrans_pk();
			if (giveBean.getGold()!=null) {
				assetBuffer.append(giveBean.getGold());
	    		assetBuffer.append("大话币,");

				loginResult.setGold(loginResult.getGold().subtract(giveBean.getGold()));
	    		otherRole.setGold(otherRole.getGold().add(giveBean.getGold()));
	    		assetUpdate.updata("D="+giveBean.getGold().longValue());
	    		giveData.updata("D=-"+giveBean.getGold().longValue());
				StringBuffer buffer=new StringBuffer();
				buffer.append("给与金额流动:");
				buffer.append(loginResult.getRole_id());
				buffer.append("送给");
				buffer.append(otherRole.getRole_id());
				buffer.append("金额");
				buffer.append(giveBean.getGold().longValue());
				AllServiceUtil.getRecordService().insert(new Record(2, BigDecimal.valueOf(transId), buffer.toString()));
			}
			if (goodstable!=null) {
				assetBuffer.append(giveBean.getSum());
	    		assetBuffer.append("个");
	    		assetBuffer.append(goodstable.getGoodsname());
	    		
				if(EquipTool.canSuper(goodstable.getType())){//可重叠
					goodstable.goodxh(giveBean.getSum());
					AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
					giveData.updata("G"+goodstable.getRgid()+"="+goodstable.getUsetime());
					// 判断该角色是否拥有这件物品
					List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(otherRole.getRole_id(),goodstable.getGoodsid());
					if (sameGoodstable.size() != 0) {
						sameGoodstable.get(0).setUsetime(sameGoodstable.get(0).getUsetime()+ giveBean.getSum());
						AllServiceUtil.getGoodsTableService().updateGoodRedis(sameGoodstable.get(0));
						assetUpdate.setGood(sameGoodstable.get(0));
					} else {
						Goodstable good = goodstable.clone();
						good.setRgid(null);
						good.setStatus(0);
						good.setRole_id(otherRole.getRole_id());
						good.setUsetime(giveBean.getSum());
						AllServiceUtil.getGoodsTableService().insertGoods(good);
						assetUpdate.setGood(good);
					}
				}else {
					AllServiceUtil.getGoodsTableService().updateGoodsIndex(goodstable, otherRole.getRole_id(), null, null);
					assetUpdate.setGood(goodstable);
					giveData.updata("G"+goodstable.getRgid()+"=0");
				}
				Goodstable JLGood=goodstable.clone();
				JLGood.setRole_id(loginResult.getRole_id());
				AllServiceUtil.getGoodsrecordService().insert(JLGood,otherRole.getRole_id(),giveBean.getSum(),2);//添加记录
			}
			inster(transId, loginResult, otherRole, goodstable, giveBean.getGold());
			assetUpdate.setMsg(assetBuffer.toString());
			SendMessage.sendMessageToSlef(ctx2,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(giveData)));
			return;
		}
		if (giveBean.getRgid()==null) {return;}
		Goodstable goodstable=getGiveGood(giveBean.getRgid(), loginResult.getRole_id(), giveBean.getSum(),false);
		if (goodstable==null) {
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你给与的物品处于异常"));
			return;
		}
		if (giveBean.getType()==1) {//金钱回收
			Bbuy bbuy=GameServer.getBbuy(goodstable.getGoodsid());
			if (bbuy==null|| StringUtils.isBlank(bbuy.getPrice1())) {return;}
			long money = 0;
			String[] prices = bbuy.getPrice1().split("\\|");
			if (Goodtype.GodEquipment_God(goodstable.getType())) {
				String[] vs = goodstable.getValue().split("\\|");
				int lvl = Integer.parseInt(vs[0].split("=")[1]);
				if (lvl <= prices.length) {
					money = Long.parseLong(prices[lvl - 1]);
				}
			} else {
				money = Long.parseLong(prices[0]);
			}
			if (money < 0) {return;}
			int num=giveBean.getSum();
			if (giveBean.getSum()>25) {giveBean.setSum(25);}
			giveBean.setSum(bbuy.addNum(giveBean.getSum()));
			if (giveBean.getSum()<=0||goodstable.getUsetime()<giveBean.getSum()) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("今天回收的材料已经够用了,明天再来吧"));
				return;
			}
			goodstable.setUsetime(goodstable.getUsetime()-giveBean.getSum());
			AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
			AssetUpdate assetUpdate=new AssetUpdate();
			assetUpdate.setType(AssetUpdate.USEGOOD);

			money *= giveBean.getSum();
			assetUpdate.updata("D="+money);
    		assetUpdate.updata("G"+goodstable.getRgid()+"="+goodstable.getUsetime());
    		if (num>25) {assetUpdate.setMsg("收购获得"+money+"银两|单次收购最大数25个");}
      		else{assetUpdate.setMsg("收购获得"+money+"银两");}
    		loginResult.setGold(loginResult.getGold().add(new BigDecimal(money)));
			MonitorUtil.getMoney().addD(money, 3);
    		SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
		}else if (giveBean.getType()==2) {//绑玉回收
			Bbuy bbuy=GameServer.getBbuy(goodstable.getGoodsid());
			if (bbuy==null||StringUtils.isBlank(bbuy.getPrice2())) {return;}
			long money = 0;
			String[] prices = bbuy.getPrice2().split("\\|");
			if (Goodtype.GodEquipment_God(goodstable.getType())) {
				String[] vs = goodstable.getValue().split("\\|");
				int lvl = Integer.parseInt(vs[0].split("=")[1]);
				if (lvl <= prices.length) {
					money = Long.parseLong(prices[lvl - 1]);
				}
			} else {
				money = Long.parseLong(prices[0]);
			}
			if (money < 0) {return;}
			// 修复回收仙玉数量修改
			int sum = giveBean.getSum();
			if (goodstable.getUsetime() <= 0) {
				return;
			}
			int num=MonitorUtil.addBY(loginResult.getRole_id(),giveBean.getSum(), Long.parseLong(bbuy.getPrice2()));
			if (num<=0) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("超过单日绑玉最大获取量将收购数量修改为0个"));
				return;
			}
			goodstable.goodxh(sum);
			AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
			AssetUpdate assetUpdate=new AssetUpdate();
			assetUpdate.setType(AssetUpdate.USEGOOD);
			money *= giveBean.getSum();
			assetUpdate.updata("S="+money);
    		assetUpdate.updata("G"+ goodstable.getRgid()+"="+goodstable.getUsetime());
//            if (num!=giveBean.getSum()) {
//            	assetUpdate.setMsg("超过单日绑玉最大获取量将收购数量修改为"+num+"个|收购获得"+money+"绑玉");
//			}

				assetUpdate.setMsg("收购获得"+money+"绑玉");

    		loginResult.setSavegold(loginResult.getSavegold().add(new BigDecimal(money)));
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
		}else if (giveBean.getType()==3) {//限时回收
			MapMonsterBean bean=MonsterUtil.getMonster(giveBean.getOtherID().intValue());
			if (bean==null||bean.getRobotType()!=3) {SendMessage.sendMessageToSlef(ctx,ClickMonsterAction.CHECKTS2);return;}
			Lshop lshop=bean.getShops().get(goodstable.getGoodsid().toString());
			if (lshop==null) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("不属于回收范围"));
				return;
			}
			Robots robots=GameServer.getAllRobot().get(bean.getRobotid()+"");	
			if (robots==null) {return;}
			if (robots.getLvls()!=null) {
				String value=BattleMixDeal.isLvl(loginResult.getGrade(), robots.getLvls());
        		if (value!=null) {SendMessage.sendMessageToSlef(ctx,value);return;}
			}
			String v=ClickMonsterAction.isTime20s(loginResult.getRole_id());
			if (v!=null) {SendMessage.sendMessageToSlef(ctx,v);return;}
			AssetUpdate assetUpdate=new AssetUpdate();
			assetUpdate.setType(AssetUpdate.USEGOOD);
			String msg=null;
			if (giveBean.getSum()>lshop.getlNum()) {
				msg="单次最大购买数量"+lshop.getlNum();
				giveBean.setSum(lshop.getlNum());
			}
			giveBean.setSum(lshop.addNum(giveBean.getSum()));
			if (giveBean.getSum()==0) {
				SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("回收的材料已经够用了"));
				return;
			}
			goodstable.setUsetime(goodstable.getUsetime()-giveBean.getSum());
			AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
			assetUpdate.updata("G"+goodstable.getRgid()+"="+goodstable.getUsetime());
			if (lshop.getType()==0) {
				long money=(lshop.getMoney().longValue()*giveBean.getSum());
				loginResult.setGold(loginResult.getGold().add(new BigDecimal(money)));
				assetUpdate.updata("D="+money);
				MonitorUtil.getMoney().addD(money, 3);
				if (msg!=null) {msg=msg+"|收购获得"+money+"银两";}
				else{msg="收购获得"+money+"银两";}
			}else if (lshop.getType()==1) {
				long money=(lshop.getMoney().longValue()*giveBean.getSum());
				loginResult.setCodecard(loginResult.getCodecard().add(new BigDecimal(money)));
				MonitorUtil.getMoney().addX(money, 2);
				assetUpdate.updata("X="+money);
			    if (msg!=null) {msg=msg+"|收购获得"+money+"仙玉";}
				else{msg="收购获得"+money+"仙玉";}
			}
			assetUpdate.setMsg(msg);
    		SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  	
		}
		AllServiceUtil.getGoodsrecordService().insert(goodstable, null, giveBean.getSum(), 2);//添加记录
	}
	/**给与物品判断*/
	public Goodstable getGiveGood(BigDecimal rgid,BigDecimal roleid,int sum,boolean isJY){
		if (sum<0) {
			AllServiceUtil.getRecordService().insert(new Record(5,"给与异常:id:"+rgid+"_角色:"+roleid+"_数量:"+sum));
			return null;
		}
		Goodstable goodstable=AllServiceUtil.getGoodsTableService().getGoodsByRgID(rgid);
	    if (goodstable==null) {
	    	AllServiceUtil.getRecordService().insert(new Record(5,"给与异常:id:"+rgid+"_角色:"+roleid+"_数量:"+sum));
			return null;
		}
		if (goodstable.getRole_id().compareTo(roleid)!=0||goodstable.getUsetime()<sum) {
	    	StringBuffer buffer=new StringBuffer();
	    	buffer.append("给与异常:id:"+rgid+"_角色:"+roleid+"_数量:"+sum);
	    	buffer.append("_物品属性:");
	    	buffer.append(GsonUtil.getGsonUtil().getgson().toJson(goodstable));
	    	AllServiceUtil.getRecordService().insert(new Record(5,buffer.toString()));
			return null;
	    }
		if (isJY&&AnalysisString.jiaoyi(goodstable.getQuality())) {
			StringBuffer buffer=new StringBuffer();
	    	buffer.append("给与异常物品绑定:id:"+rgid+"_角色:"+roleid+"_数量:"+sum);
	    	buffer.append("_物品属性:");
	    	buffer.append(GsonUtil.getGsonUtil().getgson().toJson(goodstable));
	    	AllServiceUtil.getRecordService().insert(new Record(5,buffer.toString()));
			return null;
		}
	    return goodstable;
	}



	private static void inster(Long transId, LoginResult result1, LoginResult result2, Goodstable goodstable, BigDecimal Money) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String transTime = sdf.format(new Date());
		List<TransInfoVo> list = new ArrayList<>();
		if (Money != null) {
			TransInfoVo transInfoVo = new TransInfoVo(2, transId, transTime);
			transInfoVo.setFromBy(result1.getRolename());
			transInfoVo.setToBy(result2.getRolename());
			transInfoVo.setItemInfo("金钱:" + Money.longValue());
			list.add(transInfoVo);
		}

		if (goodstable != null) {
			TransInfoVo transInfoVo = new TransInfoVo(2, transId, transTime);
			transInfoVo.setFromBy(result1.getRolename());
			transInfoVo.setToBy(result2.getRolename());
			transInfoVo.setItemInfo(goodstable.getGoodsname() + "[" + goodstable.getGoodsid() + "]");
			list.add(transInfoVo);
		}

		if (list.size() > 0) {
			AllServiceUtil.getGoodsTableService().transInfo(list);
		}


		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); //yyyy-MM-dd表示年-月-日的格式
//获取当前的日期，并转换为字符串
		String date = sdf1.format(new Date()); //Date()表示当前的日期
//创建一个名为交易记录的文件夹，如果它不存在的话
		File dir = new File("交易记录");
		if (!dir.exists()) {
			dir.mkdirs();
		}
//将文件夹的路径和日期+给与记录.txt拼接起来，作为文件名
		String fileName = dir.getPath() + File.separator + date + "给与记录.txt"; //File.separator表示系统的路径分隔符
		try (
				//创建一个FileWriter对象，用于向文件中写入数据
				FileWriter writer = new FileWriter(fileName, true); //true表示追加写入
				//创建一个BufferedWriter对象，用于缓存数据
				BufferedWriter bw = new BufferedWriter(writer);
		) {
			//创建一个txt文件，命名为fileName
			File file = new File(fileName);
			//如果文件不存在，就创建一个新的文件
			if (!file.exists()) {
				file.createNewFile();
			}
			//获取系统的换行符
			String lineSeparator = System.lineSeparator();
			//遍历list，将每个TransInfoVo对象的信息转换为字符串，并写入文件中
			for (TransInfoVo vo : list) {
				String info = vo.toString(); //假设TransInfoVo类有重写toString方法，返回对象的信息
				bw.write(info + lineSeparator); //写入一行数据，并换行
			}
			//刷新缓冲区，将数据写入文件
			bw.flush();
		} catch (IOException e) {
			//捕获异常，并打印错误信息
			e.printStackTrace();
		}



	}
}
