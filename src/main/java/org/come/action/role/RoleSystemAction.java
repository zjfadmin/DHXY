package org.come.action.role;

import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.bean.MountResult;
import org.come.entity.Mount;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Role.RoleSystem;

import java.util.List;

public class RoleSystemAction implements IAction{

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// TODO Auto-generated method stub
		// 获得角色信息
		LoginResult loginResult = GameServer.getAllLoginRole().get(ctx);
		RoleData roleData=RolePool.getRoleData(loginResult.getRole_id());
		RoleSystem roleSystem = GsonUtil.getGsonUtil().getgson().fromJson(message, RoleSystem.class);
		RoleSystem system=roleData.getRoleSystem();
		if (system.getIsNewRole() != roleSystem.getIsNewRole()) {
			List<Mount> mounts = AllServiceUtil.getMountService().selectMountsByRoleID(loginResult.getRole_id());
			// 放进坐骑返回的bean中
			MountResult mountResult = new MountResult();
			mountResult.setMounts(mounts);
			// 发送给客户端
			String msg = Agreement.getAgreement().MountAgreement(GsonUtil.getGsonUtil().getgson().toJson(mountResult));
			SendMessage.sendMessageToSlef(ctx, msg);
		}
		system.set(roleSystem);
//		roleData.setRoleSystem(roleSystem);
	}

}
