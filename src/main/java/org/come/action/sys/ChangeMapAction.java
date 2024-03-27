package org.come.action.sys;

import come.tool.BangBattle.BangBattlePool;
import come.tool.BangBattle.BangFight;
import come.tool.Role.RoleShow;
import come.tool.Scene.LTS.LTSScene;
import come.tool.Scene.Scene;
import come.tool.Scene.SceneUtil;
import come.tool.Stall.StallPool;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import org.come.action.IAction;
import org.come.bean.ChangeMapBean;
import org.come.bean.GetClientUserMesageBean;
import org.come.bean.LoginResult;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.redis.RedisCacheUtil;
import org.come.server.GameServer;
import org.come.task.MapMonsterBean;
import org.come.task.MonsterUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**传送地图，客户端发来地图ID，返回npc集合和地图所有角色*/
public class ChangeMapAction implements IAction{
	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		ChangeMapBean changeMapBean = GsonUtil.getGsonUtil().getgson().fromJson(message, ChangeMapBean.class);
		ChangeMap(changeMapBean, ctx);
	}	
	/***/
	public static void ChangeMap(ChangeMapBean changeMapBean,ChannelHandlerContext ctx){
		// 获得角色信息
		LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);
		if (roleInfo==null) {return;}		
		long oldMapId=roleInfo.getMapid();
		// 地图ID转格式
		long mapid = Long.parseLong(changeMapBean.getMapid());
		//要传送的角色
		String[] roles = roleInfo.getTeam().split("\\|");
        if (changeMapBean.getType()==4) {//判断对外是否都是同一帮派
        	BigDecimal gangid=roleInfo.getGang_id();
        	if (gangid.intValue()==0) {
        		SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("你没有帮派"));
        		return;
			}
			for (int i = 1; i < roles.length; i++) {
				ChannelHandlerContext ctx2=GameServer.getRoleNameMap().get(roles[i]);
				LoginResult changRole = ctx2!=null?GameServer.getAllLoginRole().get(ctx2):null;
				if (changRole!=null&&gangid.compareTo(changRole.getGang_id()) != 0){
					SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("不能携带非本帮派成员进入"));
	        		return;
				}
			}
		}
		MapMonsterBean monsterBean=MonsterUtil.getFollowMonster(roles);
		if (monsterBean!=null&&changeMapBean.getType()==1) {
			SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("飞行棋使用限制"));
			return;
		}
		Map<String, ChannelHandlerContext> mapRoleMap =GameServer.getMapRolesMap().get(roleInfo.getMapid());
		boolean ismap=(mapid!=oldMapId);
		//判断是否到了新地图
		if (ismap) {
			// 清除地图里集合
			for (int i = 0; i < roles.length; i++){
				mapRoleMap.remove(roles[i]);
			}
			//退出地图
			SendMessage.sendMessageToMapRoles(roleInfo.getMapid(),Agreement.getAgreement().UserRetreatAgreement(roleInfo.getTeam()));
			if (monsterBean!=null) {
				SendMessage.sendMessageToMapRoles(roleInfo.getMapid(),Agreement.getAgreement().battleStateAgreement("M"+monsterBean.getI()+"^2"));
			}
		}
		// 储存修改后的传送角色信息
		List<RoleShow> roleShows = new ArrayList<RoleShow>();

//		for (int b = 0; b<= RedisCacheUtil.STALL_BOT.size()-1; b++) {
//			LoginResult changRole=RedisCacheUtil.jiqiren.get(b);
//			if (changRole!=null) {
//				roleShows.add(RedisCacheUtil.STALL_BOT.get(b).getRoleShow());
//			}
//		}

		for (int i = 0; i < roles.length; i++) {
//			// 获得需要传送的角色信息
			ChannelHandlerContext ctx2=GameServer.getRoleNameMap().get(roles[i]);
			if (ctx2==null) {
				for (int g=0;g<= RedisCacheUtil.jiqiren.size()-1;g++){
					if (roles[i].equals(RedisCacheUtil.jiqiren.get(g).getRolename()))
					{
						LoginResult changRole=RedisCacheUtil.jiqiren.get(g);
						if (changRole!=null) {
							// 修改自己的地图ID
							changRole.setMapid(mapid);
							// 修改坐标
							changRole.setX(new Long(changeMapBean.getMapx()));
							changRole.setY(new Long(changeMapBean.getMapy()));
							changRole.getRoleShow().getPlayer_Paths().clear();
							roleShows.add(changRole.getRoleShow());
						}
					}
				}

			}else {
				LoginResult changRole = GameServer.getAllLoginRole().get(ctx2);
				if (changRole!=null) {
					// 修改自己的地图ID
					changRole.setMapid(mapid);
					// 修改坐标
					changRole.setX(new Long(changeMapBean.getMapx()));
					changRole.setY(new Long(changeMapBean.getMapy()));
					changRole.getRoleShow().getPlayer_Paths().clear();
					roleShows.add(changRole.getRoleShow());
				}
			}
		}
		String mes2=null;
		if (monsterBean!=null) {
			monsterBean.setX(changeMapBean.getMapx());
			monsterBean.setY(changeMapBean.getMapy());	
			if (ismap) {
				MonsterUtil.getList(monsterBean.getMap(),monsterBean.getRobotid()).remove(monsterBean);
				monsterBean.setMap(mapid);
				MonsterUtil.getList(monsterBean.getMap(),monsterBean.getRobotid()).add(monsterBean);
				StringBuffer buffer = new StringBuffer();
				buffer.append(monsterBean.getRobotid());
				buffer.append("#");
				buffer.append(monsterBean.getRobotname());
				buffer.append("#");
				buffer.append(monsterBean.getRobotskin());
				buffer.append("#");
				buffer.append(monsterBean.getRobotType()); 
				buffer.append("#");
				buffer.append(monsterBean.getI());
				buffer.append("^");
				buffer.append(monsterBean.getX());
				buffer.append("^");
                buffer.append(monsterBean.getY());
                if (monsterBean.getFollow()!=null&&monsterBean.getFollow().getFollowID()!=null) {
			        buffer.append("^G");
			        buffer.append(monsterBean.getFollow().getFollowID());
				}
                mes2=Agreement.getAgreement().MonsterRefreshAgreement(buffer.toString());
			}
		}
		mapRoleMap =GameServer.getMapRolesMap().get(mapid);
		GetClientUserMesageBean getClientUserMesageBean = new GetClientUserMesageBean();
		getClientUserMesageBean.setRoleShows(roleShows);
		String mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));
		Iterator<Map.Entry<String, ChannelHandlerContext>> entries =mapRoleMap.entrySet().iterator(); 
		//遍历需要的条件
		BigDecimal gang_id = null;
		if (mapid == 3000)gang_id=roleInfo.getGang_id();
		BangFight bangFight = null;
		if (mapid == 3315)bangFight=BangBattlePool.getBangBattlePool().getBangFight(roleInfo.getGang_id());
		while (entries.hasNext()) {
			Entry<String, ChannelHandlerContext> entrys = entries.next();
			ChannelHandlerContext value=entrys.getValue();
			LoginResult loginResult=GameServer.getAllLoginRole().get(value);
			if (loginResult==null){
				for (int g=0;g<= RedisCacheUtil.jiqiren.size()-1;g++){
					if (entrys.getKey().equals(RedisCacheUtil.jiqiren.get(g).getRolename()))//eq(
					{
						loginResult=RedisCacheUtil.jiqiren.get(g);

					}
				}
			}
			if (value==null||loginResult==null) {continue;}
			if (gang_id!=null&&gang_id.compareTo(loginResult.getGang_id()) != 0){continue;}
			if (bangFight!=null&&bangFight.getMap(loginResult.getGang_id())==null){continue;}
			SendMessage.sendMessageToSlef(value,mes);	
			//添加新地图角色
			if (ismap){
				roleShows.add(loginResult.getRoleShow());	
				if (mes2!=null) {SendMessage.sendMessageToSlef(value,mes2);}
			}
		}
		//添加传送的人进入新map
		if (ismap) {
			boolean isScene=SceneUtil.isSceneMsg(oldMapId, mapid);
			// 新地图所有怪物
			getClientUserMesageBean.setIsmap(1);
			getClientUserMesageBean.setMonster(MonsterUtil.getMapMonster(mapid,roleInfo.getGang_id()));		
			// 摆摊列表
			getClientUserMesageBean.setStallBeans(StallPool.getPool().getmap(roleInfo.getMapid().intValue()));
			if (!isScene) {
				mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));
			}
			for (int i = 0; i < roles.length; i++){
				ChannelHandlerContext ctx2=GameServer.getRoleNameMap().get(roles[i]);
				if (ctx2==null) {for (int g=0;g<= RedisCacheUtil.jiqiren.size()-1;g++){
					if (roles[i].equals(RedisCacheUtil.jiqiren.get(g).getRolename()))//eq(
					{  ChannelHandlerContext context=new ChannelHandlerContext() {
						@Override
						public Channel channel() {
							return null;
						}

						@Override
						public EventExecutor executor() {
							return null;
						}

						@Override
						public String name() {
							return null;
						}

						@Override
						public ChannelHandler handler() {
							return null;
						}

						@Override
						public boolean isRemoved() {
							return false;
						}

						@Override
						public ChannelHandlerContext fireChannelRegistered() {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelUnregistered() {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelActive() {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelInactive() {
							return null;
						}

						@Override
						public ChannelHandlerContext fireExceptionCaught(Throwable throwable) {
							return null;
						}

						@Override
						public ChannelHandlerContext fireUserEventTriggered(Object o) {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelRead(Object o) {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelReadComplete() {
							return null;
						}

						@Override
						public ChannelHandlerContext fireChannelWritabilityChanged() {
							return null;
						}

						@Override
						public ChannelHandlerContext read() {
							return null;
						}

						@Override
						public ChannelHandlerContext flush() {
							return null;
						}

						@Override
						public ChannelPipeline pipeline() {
							return null;
						}

						@Override
						public ByteBufAllocator alloc() {
							return null;
						}

						@Override
						public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
							return null;
						}

						@Override
						public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
							return false;
						}

						@Override
						public ChannelFuture bind(SocketAddress socketAddress) {
							return null;
						}

						@Override
						public ChannelFuture connect(SocketAddress socketAddress) {
							return null;
						}

						@Override
						public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
							return null;
						}

						@Override
						public ChannelFuture disconnect() {
							return null;
						}

						@Override
						public ChannelFuture close() {
							return null;
						}

						@Override
						public ChannelFuture deregister() {
							return null;
						}

						@Override
						public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture disconnect(ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture close(ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture deregister(ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture write(Object o) {
							return null;
						}

						@Override
						public ChannelFuture write(Object o, ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
							return null;
						}

						@Override
						public ChannelFuture writeAndFlush(Object o) {
							return null;
						}

						@Override
						public ChannelPromise newPromise() {
							return null;
						}

						@Override
						public ChannelProgressivePromise newProgressivePromise() {
							return null;
						}

						@Override
						public ChannelFuture newSucceededFuture() {
							return null;
						}

						@Override
						public ChannelFuture newFailedFuture(Throwable throwable) {
							return null;
						}

						@Override
						public ChannelPromise voidPromise() {
							return null;
						}
					};
						mapRoleMap.put(roles[i], context);
					}
				}}else {
					mapRoleMap.put(roles[i], ctx2);
				}

				if (isScene) {
					LoginResult login = GameServer.getAllLoginRole().get(ctx2);
					if (login!=null) {
						getClientUserMesageBean.setSceneMsg(SceneUtil.getSceneMsg(login,oldMapId,mapid));
					}else {
						getClientUserMesageBean.setSceneMsg(null);
					}
					mes = Agreement.getAgreement().intogameAgreement(GsonUtil.getGsonUtil().getgson().toJson(getClientUserMesageBean));
				}
				SendMessage.sendMessageToSlef(ctx2,mes);
			}	
			if (mapid==3333) {
				Scene scene=SceneUtil.getScene(SceneUtil.LTSID);
				if (scene!=null) {
                    LTSScene ltsScene=(LTSScene)scene;
                    mes=Agreement.getAgreement().duelBoradDataAgreement(ltsScene.getRanking());
                    for (int i = 0; i < roles.length; i++) {
                    	SendMessage.sendMessageByRoleName(roles[i],mes);
					}
				}
			}
		}
	}
}
