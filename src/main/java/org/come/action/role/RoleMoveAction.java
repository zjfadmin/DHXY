package org.come.action.role;

import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.bean.PathPoint;
import org.come.bean.RoleMoveBean;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.redis.RedisCacheUtil;
import org.come.server.GameServer;
import org.come.until.GsonUtil;

import come.tool.Role.RoleData;
import come.tool.Role.RolePool;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色移动
 * @author 叶豪芳
 * @date : 2017年11月30日 下午4:01:59
 */
public class RoleMoveAction implements IAction{

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// 获得人物信息
		LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);
		if (roleInfo==null) {
		     return;
		}
		String [] mes11=null;
		if (roleInfo.getTeamInfo()!=null){
			mes11=roleInfo.getTeamInfo().split("\\|");
		}
		List<LoginResult> loginResults=new ArrayList<>();
//		for (int m = 0; m<= RedisCacheUtil.jiqiren.size()-1; m++){
//
//			if (mes11!=null){
//				for (int k=0;k<=mes11.length-1;k++){
//					if (mes11[k].equals( RedisCacheUtil.jiqiren.get(m).getRolename()))
//						loginResults.add(RedisCacheUtil.jiqiren.get(m));
//				}
//			}
//		}
		RoleData roleData=RolePool.getRoleData(roleInfo.getRole_id());
		if (roleData==null) {
			return;
		}		
		// 客户端发来的人物移动信息
		RoleMoveBean roleMoveBean = GsonUtil.getGsonUtil().getgson().fromJson(message, RoleMoveBean.class);
		// 更新自己的角色信息
		PathPoint point=roleMoveBean.getPaths().get(roleMoveBean.getPaths().size()-1);
		roleInfo.setX(new Long(point.getX()));
		roleInfo.setY(new Long(point.getY()));
		// 添加移动人物名字
		roleMoveBean.setRole(roleInfo.getRolename());
		// 遍历map,根据地图ID获得用户输出流
		String msg = Agreement.getAgreement().moveAgreement(GsonUtil.getGsonUtil().getgson().toJson(roleMoveBean));
		SendMessage.sendMessageToMapRoles(roleInfo.getMapid(), msg);
		if (loginResults.size()!=0){
			for (int j=0;j<=loginResults.size()-1;j++){

				roleMoveBean.setRole(loginResults.get(j).getRolename());
				loginResults.get(j).setX((long) point.getX());
				loginResults.get(j).setY((long) point.getY());
				msg = Agreement.getAgreement().moveAgreement(GsonUtil.getGsonUtil().getgson().toJson(roleMoveBean));
				SendMessage.sendMessageToMapRoles(loginResults.get(j).getMapid(), msg);


			}
		}
	}

}
