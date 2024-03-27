package come.tool.Stall;

import come.tool.newTrans.GoodTrans;
import come.tool.newTrans.TransRole;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.come.action.monitor.MonitorUtil;
import org.come.bean.LoginResult;
import org.come.entity.*;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.redis.RedisCacheUtil;
import org.come.server.GameServer;
import org.come.tool.EquipTool;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;
import org.come.redis.RedisCacheUtil;

import come.tool.Mixdeal.AnalysisString;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;

/**
 * 摆摊工具
 * @author Administrator
 */
public class StallPool {
	//税
//	private static long TAX;
	private static double TaxXs=0.05;
//	private static Object object=new Object();
	//1购买处理
	//试营业
	public static int PREPARE=0;
	//营业 
	public static int OFF=1;	
	//托管
	public static int MANAGE=2;		
	//关门
	public static int NO=3;	
	private static StallPool pool;
	public static StallPool getPool() {
		if (pool==null) {
			pool=new StallPool();
		}
		return pool;
	}
	public StallPool() {
		// TODO Auto-generated constructor stub
		this.allStall=new HashMap<>();
		this.allStallBean=new HashMap<>();
		this.mapStallBean=new HashMap<>();
		
	}
	//自增长的摊位的id
	  private int increasesum=100;
	  //记录所有摊位的详细信息
	  private Map<Integer,Stall> allStall;
      //记录所有的摊位
	  private Map<Integer,StallBean> allStallBean;
	  //根据地图id记录
	  private Map<Integer,List<StallBean>> mapStallBean; 
	  //根据地图id获取
	  public synchronized List<StallBean> getmap(int mapid){
		List<StallBean> beans=this.mapStallBean.get(mapid);
		if (beans==null) {
			  beans=new ArrayList<>();
			  this.mapStallBean.put(mapid,beans);
		}
		return beans;		  
	  }
	  //添加一个摊位
	  public StallBean addStall(Stall stall,ChannelHandlerContext ctx){
		  boolean is=false;
		  stall.setId(getIncreasesum());
		  this.allStall.put(stall.getId(),stall);
		  StallBean bean=new StallBean(stall);
		  this.allStallBean.put(bean.getId(), bean);
		  getmap(bean.getMapid()).add(bean);
		  //把资产隔离
		  Commodity[] goodstables=stall.getGoodstables();
		  Commodity[] pets=stall.getPets();
		  for (int i = 0; i < goodstables.length; i++) {
			  Commodity commodity=goodstables[i];
			  if (commodity!=null) {
				Goodstable good=commodity.getGood();
				int usetime=good.getUsetime();
				if (usetime<=0) {is=true;goodstables[i]=null;continue;}
				good=AllServiceUtil.getGoodsTableService().getGoodsByRgID(good.getRgid());
				if (good==null||good.getRole_id().compareTo(stall.getRoleid())!=0) {is=true;goodstables[i]=null;continue;}
				if (AnalysisString.jiaoyi(good.getQuality())) {
					is=true;
					goodstables[i]=null;
					StringBuffer buffer=new StringBuffer();
					buffer.append(stall.getRoleid());
					buffer.append("欲摆摊禁交易物品:");
					buffer.append(good.getRgid());
					buffer.append(":");
					buffer.append(good.getGoodsname());
					AllServiceUtil.getRecordService().insert(new Record(5,buffer.toString()));
					continue;
				}
				good=good.clone();
				commodity.setGood(good);
				if (EquipTool.canSuper(good.getType())) {//叠加
					// 判断该角色是否拥有这件物品
					Goodstable goodstable=good.clone();
					goodstable.setRole_id(stall.getRoleid());
					if (usetime>goodstable.getUsetime()) {usetime=goodstable.getUsetime();}
					goodstable.setUsetime(goodstable.getUsetime()-usetime);
					AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
					good.setUsetime(usetime);
					good.setRole_id(new BigDecimal(-stall.getRoleid().longValue()));
					AllServiceUtil.getGoodsTableService().insertGoods(good);
				}else {//不可叠加
					AllServiceUtil.getGoodsTableService().updateGoodsIndex(good,new BigDecimal(-stall.getRoleid().longValue()),null,null);
				}
			}
		  }
		  for (int i = 0; i < pets.length; i++) {
			  Commodity commodity=pets[i];
			  if (commodity!=null) {
				  RoleSummoning pet=commodity.getPet();
				  pet=AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRgID(pet.getSid());
				  if (pet==null||pet.getRoleid().compareTo(stall.getRoleid())!=0) {is=true;pets[i]=null;continue;}
				  commodity.setPet(pet);
				  AllServiceUtil.getRoleSummoningService().updateRoleSummoningIndex(pet,new BigDecimal(-stall.getRoleid().longValue()));
			  }
		  }
		  SendMessage.sendMessageToMapRoles(new Long(bean.getMapid()),Agreement.getAgreement().stallstateAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean)));         
		  if (is) {
			  SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("有部分物品属于违规已被取消上架"));
		  }
		  return bean;  
	  }
	  public synchronized int getIncreasesum() {
			this.increasesum++;
			if (this.increasesum>999999)this.increasesum=100;
			return this.increasesum;
	  }
	  //修改摊位状态
	  public boolean updateState(BigDecimal booth_id,int state,BigDecimal roleid){
		if (booth_id==null) {return false;}
		int id=booth_id.intValue();
	    Stall stall=this.allStall.get(id);
		StallBean bean=this.allStallBean.get(id);
		if (bean==null||stall==null||stall.getRoleid().compareTo(roleid)!=0) {
		  return false;
		}
		stall.setState(state);
		bean.setState(state);
		if (state==NO) {
		   this.allStall.remove(id);
		   List<StallBean> list=getmap(stall.getMapid());
		   list.remove(this.allStallBean.remove(id));
		}
		SendMessage.sendMessageToMapRoles(new Long(bean.getMapid()),Agreement.getAgreement().stallstateAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean)));
		return true;         
	  }
	  //购买摊位的物品和宝宝
	  public synchronized void BuyStall(LoginResult loginResult,StallBuy buy){
		  Stall stall=this.allStall.get(buy.getId());
		  if (stall==null) {
			  SendMessage.sendMessageByRoleName(loginResult.getRolename(), Agreement.getAgreement().PromptAgreement("该摊位已经不存在了"));	
			  return;
		  }
		 switch (buy.getType()) {
		  case 0:
			//购买物品
			BuyGood(loginResult,buy,stall);
			break;
		  case 1:
			//购买召唤兽
			BuyPet(loginResult,buy,stall);
			break;
		  case 2:
			//购买灵宝
			BuyLing(loginResult,buy,stall);
			break;
		}
	  }
	  /**购买物品*/
	  public void BuyGood(LoginResult buyRole,StallBuy buy,Stall stall){
		  Commodity commodity=stall.getGood(buy.getBuyid());
		  TransRole transRole = new TransRole();
		  transRole.setRole_id(stall.getRoleid());
		  transRole.setRolename(stall.getRole());
		  if (commodity==null||commodity.getGood().getUsetime()<buy.getSum()) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().PromptAgreement("下手慢了或者数量不足"));	
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));
		      return;
		  }else if (buy.getSum()<=0) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().PromptAgreement("购买数量不能为0"));	
			  return;
		  }
		  Goodstable goodstable=commodity.getGood();
		  if (goodstable.getRole_id().longValue()>0) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().PromptAgreement("摆摊物品异常"));	
			  return;
		  }
		  long price=buy.getSum()*commodity.getMoney();
		  if (price>buyRole.getGold().longValue()) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().PromptAgreement("金钱不足"));	
			  return;
		  }
		  //扣钱
		  buyRole.setGold(new BigDecimal(buyRole.getGold().longValue()-price));
		  //判断是添加还是修改
		  boolean add=true;
		  //判断是否叠加  叠加返回ture
		  boolean fold=EquipTool.canSuper(goodstable.getType());
		  goodstable.setUsetime(goodstable.getUsetime()-buy.getSum());
		  if (goodstable.getUsetime()<=0||!fold) {
			  add=false;
			  stall.Buy(commodity);		
		  }
		  GoodTrans goodTrans = new GoodTrans();
		  List<Goodstable> goodstables = new ArrayList<>();
		  for (int i = 0; i < buy.getSum(); i++) {
			  goodstables.add(goodstable);
		  }
		  goodTrans.setGoods(goodstables);
		  //摆摊记录
		  transRole.setGoodTrans(goodTrans);

		  Long transId = RedisCacheUtil.getTrans_pk();
		  inster(transId, buyRole, transRole, price);
		  AllServiceUtil.getGoodsrecordService().insert(goodstable,buyRole.getRole_id(),buy.getSum(),1);
		  //复制一个属性
		  Goodstable good=goodstable.clone();		
		  good.setUsetime(buy.getSum());
		  //给购买者添加物品
		  if (fold) {//叠加
			// 判断该角色是否拥有这件物品
				List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(buyRole.getRole_id(), good.getGoodsid());
				if(sameGoodstable.size() != 0){
					// 修改使用次数
					sameGoodstable.get(0).setUsetime(sameGoodstable.get(0).getUsetime() + good.getUsetime());
					// 修改数据库
					AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
					AllServiceUtil.getGoodsTableService().updateGoodRedis(sameGoodstable.get(0));
					good=sameGoodstable.get(0);
				}else{
					// 插入数据库
					if (add) {
						AllServiceUtil.getGoodsTableService().updateGoodRedis(goodstable);
						good.setRole_id(buyRole.getRole_id());
						AllServiceUtil.getGoodsTableService().insertGoods(good);			
					}else {
						AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, buyRole.getRole_id(), null, null);
					}
				}
		  }else {//不可叠加
			  AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, buyRole.getRole_id(), null, null);
		  }
		  //扣税
		  long s=(long) (price*TaxXs);
		  MonitorUtil.getStallM().add(s);
		  MonitorUtil.getMoney().useD(s);
		  //交易处理
		  AssetUpdate assetUpdate=new AssetUpdate();
		  assetUpdate.setType(AssetUpdate.STALLGET);
		  assetUpdate.setMsg(buy.getSum()+"个"+good.getGoodsname());
		  assetUpdate.updata("D="+(price-s));	  
		  LoginResult loginResult=null;
		  ChannelHandlerContext ctx=GameServer.getRoleNameMap().get(stall.getRole());
		  if (ctx!=null) {
			  loginResult=GameServer.getAllLoginRole().get(ctx);
		  }
		  if (loginResult!=null) {
			  loginResult.setGold(new BigDecimal(loginResult.getGold().longValue()+price-s));
			  SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
			  SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));
		  }else {
			  BigDecimal gold=AllServiceUtil.getRoleTableService().selectMoneyRoleID(stall.getRoleid());
			  if(gold==null){
				  gold= BigDecimal.valueOf(0);
			  }
			 			  gold=gold.add(new BigDecimal(price-s));
			  if( gold.compareTo(new BigDecimal("999999999999")) > 0 ){
				  gold=new BigDecimal("999999999999");
			  }
			  AllServiceUtil.getRoleTableService().updateMoneyRoleID(stall.getRoleid(),gold);
		  }
		  StringBuffer buffer=new StringBuffer();
		  buffer.append("摆摊金额流动:");
		  buffer.append(stall.getRoleid());
		  buffer.append("卖给");
		  buffer.append(buyRole.getRole_id());
		  buffer.append(buy.getSum()+"个"+good.getGoodsname());
		  buffer.append("物品id:"+good.getRgid());
		  buffer.append("金额");
		  buffer.append(price);
		  AllServiceUtil.getRecordService().insert(new Record(3, BigDecimal.valueOf(transId), buffer.toString()));
		  assetUpdate.setData(null);
		  assetUpdate.updata("D="+(-price));
		  assetUpdate.setGood(good);
		  assetUpdate.setType(AssetUpdate.STALLBUY);
		  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
		  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));	  
	  }
	  /**购买宠物*/
	  public void BuyPet(LoginResult buyRole,StallBuy buy,Stall stall){
		  Commodity commodity=stall.getPet(buy.getBuyid());
		  TransRole transRole = new TransRole();
		  transRole.setRole_id(stall.getRoleid());
		  transRole.setRolename(stall.getRole());
		  if (commodity==null) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().PromptAgreement("下手慢了"));	
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));
		      return;
		  }
		  RoleSummoning pet=commodity.getPet();
		  long price=commodity.getMoney();
		  if (price>buyRole.getGold().longValue()) {
			  SendMessage.sendMessageByRoleName(buyRole.getRolename(), Agreement.getAgreement().PromptAgreement("金钱不足"));	
			  return;
		  }	
		  //扣税
		  long s=(long) (price*TaxXs);
		  MonitorUtil.getStallM().add(s);
		  MonitorUtil.getMoney().useD(s);
		  //扣钱
		  buyRole.setGold(new BigDecimal(buyRole.getGold().longValue()-price));
		  //交易处理
		  AssetUpdate assetUpdate=new AssetUpdate();
		  assetUpdate.setType(AssetUpdate.STALLGET);
		  assetUpdate.setMsg(buy.getSum()+"个"+pet.getSummoningname());
		  assetUpdate.updata("D="+(price-s));	
		  stall.Buy(commodity);
		  LoginResult loginResult=null;
		  ChannelHandlerContext ctx=GameServer.getRoleNameMap().get(stall.getRole());
		  if (ctx!=null) {
			  loginResult=GameServer.getAllLoginRole().get(ctx);
		  }
		  if (loginResult!=null) {
			  loginResult.setGold(new BigDecimal(loginResult.getGold().longValue()+price-s));
			  SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
			  SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));
		  }else {
			  BigDecimal gold=AllServiceUtil.getRoleTableService().selectMoneyRoleID(stall.getRoleid());
			  gold=gold.add(new BigDecimal(price-s));
			  if( gold.compareTo(new BigDecimal("9999999999")) > 0 ){
					gold=new BigDecimal("9999999999");
			  }
			  AllServiceUtil.getRoleTableService().updateMoneyRoleID(stall.getRoleid(),gold);
		  }	
	      //清空亲密
		  pet.setFriendliness(0L);
		  //修改数据库
		  AllServiceUtil.getRoleSummoningService().updateRoleSummoningIndex(pet,buyRole.getRole_id());
		  assetUpdate.setPet(pet);
		  assetUpdate.setType(AssetUpdate.STALLBUY);
		  //转移召唤兽装备
		  List<BigDecimal> goods=pet.getGoods();
		  if (goods!=null) {
			  for (BigDecimal v :goods) {
				  Goodstable good = AllServiceUtil.getGoodsTableService().getGoodsByRgID(v);
				  if (good!=null) {
					  AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, buyRole.getRole_id(), null, null);
					  assetUpdate.setGood(good);
				  }else {
					  System.out.println("不见的id:"+v);
				  }	  
			  }
		  }
		  Long transId = RedisCacheUtil.getTrans_pk();
		  if (MonitorUtil.isUpMoney(3,price)) {
				StringBuffer buffer=new StringBuffer();
				buffer.append("摆摊大宗金额流动:");
				buffer.append(stall.getRoleid());
				buffer.append("卖给");
				buffer.append(buyRole.getRole_id());
				buffer.append("宠物id:"+pet.getSid());
				buffer.append("金额");
				buffer.append(price);
				AllServiceUtil.getRecordService().insert(new Record(3,buffer.toString()));
		  }
		  GoodTrans goodTrans = new GoodTrans();
		  goodTrans.setPets(Arrays.asList(pet));
		  transRole.setGoodTrans(goodTrans);
		  inster(transId, buyRole, transRole,price);
		  assetUpdate.setData(null);
		  assetUpdate.updata("D="+(-price));	
		  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));  
		  SendMessage.sendMessageByRoleName(buyRole.getRolename(),Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));	  
	  }
	  /**购买灵宝*/
	  public void BuyLing(LoginResult buyRole,StallBuy buy,Stall stall){
		  
	  }
	  //收摊处理
	  public void RetreatStall(int id){
		  Stall stall=this.allStall.remove(id);
		  if (stall==null) {
			return;
		  }
		  stall.setState(NO);
		  List<StallBean> list=getmap(stall.getMapid());
		  StallBean bean=this.allStallBean.remove(id);
		  bean.setState(NO);
		  list.remove(bean);	
		  SendMessage.sendMessageToMapRoles(new Long(stall.getMapid()),Agreement.getAgreement().stallstateAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean)));        
		  AssetUpdate assetUpdate=new AssetUpdate();
		  assetUpdate.setType(AssetUpdate.STALLRET);
		  Commodity[] goodstables=stall.getGoodstables();
		  Commodity[] pets=stall.getPets();
		for (int i = 0; i < goodstables.length; i++) {
			Commodity commodity = goodstables[i];
			if (commodity != null) {
				Goodstable good = commodity.getGood();
				if (EquipTool.canSuper(good.getType())) {// 叠加
					// 判断该角色是否拥有这件物品
					List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(stall.getRoleid(),good.getGoodsid());
					if (sameGoodstable.size() != 0) {
						// 修改使用次数
						sameGoodstable.get(0).setUsetime(sameGoodstable.get(0).getUsetime()+good.getUsetime());
						// 修改数据库
						AllServiceUtil.getGoodsTableService().updateGoodRedis(sameGoodstable.get(0));
						AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(good.getRgid());
						good = sameGoodstable.get(0);
					} else {// 插入数据库
						AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, stall.getRoleid(), null, null);
					}
				} else {// 不可叠加
					AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, stall.getRoleid(), null, null);
				}
				assetUpdate.setGood(good);
			}
		}
		for (int i = 0; i < pets.length; i++) {
			 Commodity commodity=pets[i];
			 if (commodity!=null) {
				RoleSummoning pet=commodity.getPet();
                AllServiceUtil.getRoleSummoningService().updateRoleSummoningIndex(pet,stall.getRoleid());
				assetUpdate.setPet(pet);
			 }
		}
		SendMessage.sendMessageByRoleName(stall.getRole(),Agreement.getAgreement().assetAgreement(GsonUtil.getGsonUtil().getgson().toJson(assetUpdate)));       
	}
	public Map<Integer, Stall> getAllStall() {
		return this.allStall;
	}
	public void setAllStall(Map<Integer, Stall> allStall) {
		this.allStall = allStall;
	}

	public Stall getStallByRoleName(String roleName){
		Stall retStall = null;
		for (Integer integer : this.allStall.keySet()) {
			Stall stall = this.allStall.get(integer);
			if(stall.getRole().equals(roleName)){
				retStall = stall;
				break;
			}
		}
		return retStall;
	}

	 ///关闭服务器处理
	public void guanbi() {
		for (Stall stall : this.allStall.values()) {
			RoleData roleData = RolePool.getRoleData(stall.getRoleid());
			AssetUpdate assetUpdate = new AssetUpdate();
			assetUpdate.setType(AssetUpdate.STALLRET);
			Commodity[] goodstables = stall.getGoodstables();
			Commodity[] pets = stall.getPets();
			for (int i = 0; i < goodstables.length; i++) {
				Commodity commodity = goodstables[i];
				if (commodity != null) {
					Goodstable good = commodity.getGood();
					if (EquipTool.canSuper(good.getType())) {// 叠加
						// 判断该角色是否拥有这件物品
						List<Goodstable> sameGoodstable = AllServiceUtil.getGoodsTableService().selectGoodsByRoleIDAndGoodsID(stall.getRoleid(), good.getGoodsid());
						if (sameGoodstable.size() != 0) {
							// 修改使用次数
							sameGoodstable.get(0).setUsetime(sameGoodstable.get(0).getUsetime()+ good.getUsetime());
							// 修改数据库
							AllServiceUtil.getGoodsTableService().updateGoodRedis(sameGoodstable.get(0));
							AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(good.getRgid());
							good = sameGoodstable.get(0);
						} else {// 插入数据库
							AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, stall.getRoleid(),null, null);
						}
					} else {// 不可叠加
						AllServiceUtil.getGoodsTableService().updateGoodsIndex(good, stall.getRoleid(), null, null);
					}
					assetUpdate.setGood(good);
				}
			}
			for (int i = 0; i < pets.length; i++) {
				Commodity commodity = pets[i];
				if (commodity != null) {
					RoleSummoning pet = commodity.getPet();
					AllServiceUtil.getRoleSummoningService().updateRoleSummoningIndex(pet, stall.getRoleid());
					assetUpdate.setPet(pet);
				}
			}
		}
	}

	//记录交易信息
	private static void inster(Long transId, LoginResult buyRole, TransRole goodTrans,Long price) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String transTime = sdf.format(new Date());
		String buyRoleName = buyRole.getRolename();
		String roleName = goodTrans.getRolename();
		List<Goodstable> goods1 = goodTrans.getGoodTrans().getGoods();
		List<Lingbao> lingbaos1 = goodTrans.getGoodTrans().getLingbaos();
		List<RoleSummoning> pets1 = goodTrans.getGoodTrans().getPets();
		List<TransInfoVo> list = new ArrayList<>();
		if (price != null) {
			TransInfoVo transInfoVo = new TransInfoVo(3,transId,transTime);
			transInfoVo.setFromBy(roleName);
			transInfoVo.setToBy(buyRoleName);
			transInfoVo.setItemInfo("金钱：" + price);
			list.add(transInfoVo);
		}
		if(goods1!=null) {
			for (Goodstable goodstable : goods1) {
				TransInfoVo transInfoVo = new TransInfoVo(3,transId,transTime);
				transInfoVo.setFromBy(roleName);
				transInfoVo.setToBy(buyRoleName);
				transInfoVo.setItemInfo("道具："+goodstable.getGoodsname() + "[" + goodstable.getGoodsid() + "]");
				list.add(transInfoVo);
			}
		}
		if(lingbaos1!=null) {
			for (Lingbao lingbao : lingbaos1) {
				TransInfoVo transInfoVo = new TransInfoVo(3,transId,transTime);
				transInfoVo.setFromBy(roleName);
				transInfoVo.setToBy(buyRoleName);
				transInfoVo.setItemInfo("灵宝：" + lingbao.getBaoname());
				list.add(transInfoVo);
			}
		}
		if(pets1!=null) {
			for (RoleSummoning roleSummoning : pets1) {
				TransInfoVo transInfoVo = new TransInfoVo(3,transId,transTime);
				transInfoVo.setFromBy(roleName);
				transInfoVo.setToBy(buyRoleName);
				transInfoVo.setItemInfo("召唤兽："+roleSummoning.getSummoningname() + "[" + roleSummoning.getSummoningid() + "]");
				list.add(transInfoVo);
			}
		}
		if(list.size()>0){
			File dir = new File("交易记录");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//将文件夹的路径和日期+交易记录.txt拼接起来，作为文件名
			String fileName = dir.getPath() + File.separator  + "交易记录.txt"; //File.separator表示系统的路径分隔符
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
					String info = vo.toString(); //假设TransInfoVo类有重写toString方法，返回对象的信息字符串
					bw.write(info); //将信息字符串写入缓存中
					bw.write(lineSeparator); //换行
				}
				bw.flush(); //将缓存中的数据刷新到文件中
			} catch (IOException e) {
				//捕获异常，并打印错误信息
				e.printStackTrace();
			}


		}
		}
	}

