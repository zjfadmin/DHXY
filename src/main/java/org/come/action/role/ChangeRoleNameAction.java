package org.come.action.role;

import io.netty.channel.ChannelHandlerContext;

import org.apache.commons.lang.StringUtils;
import org.come.action.IAction;
import org.come.bean.ChangeRoleNameBean;
import org.come.bean.LoginResult;
import org.come.entity.RoleTable;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.redis.RedisControl;
import org.come.redis.RedisParameterUtil;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import come.tool.Role.RoleData;
import come.tool.Role.RolePool;

import java.math.BigDecimal;

/**
 * 修改角色名字
 * @author Administrator
 *
 */
public class ChangeRoleNameAction implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		// 获取客户端发来的消息
		ChangeRoleNameBean changeRoleNameBean = GsonUtil.getGsonUtil().getgson().fromJson(message, ChangeRoleNameBean.class);
		// 新靓号
		BigDecimal liangId = changeRoleNameBean.getLiangID();
		// 新名字
		String newName = changeRoleNameBean.getNewName();
		// 旧名字
		String oldName = changeRoleNameBean.getOldName();
		RoleTable role ;
		boolean is = false;
		if (liangId == null) {
			// 判断角色名称是否存在
			is = AllServiceUtil.getRoleTableService().selectRoleTableByRoleName(newName) == null;
		} else {
			String value = RedisControl.getV(RedisParameterUtil.LIANGID, liangId.toString());
			is = StringUtils.isNotBlank(value) && !value.equals("0");// 值不为空 并且不为0则可使用
			if (is) {// 加强判断是否存在该靓号
				is = AllServiceUtil.getRoleTableService().selectRoleTableByLiangID(liangId) == null;
			}
		}
		if (is) {
			// 获得角色信息
			LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);
			RoleData data=RolePool.getRoleData(roleInfo.getRole_id());
			if (liangId == null) {// 修改角色名字
				roleInfo.setRolename(newName);
				data.setLoginResult(roleInfo);
				// 添加角色名map
				GameServer.getRoleNameMap().put(roleInfo.getRolename(), ctx);
				// 根据地图ID存集合
				GameServer.getMapRolesMap().get(roleInfo.getMapid()).put(roleInfo.getRolename(), ctx);
				// 清除旧名字存的map
				GameServer.getRoleNameMap().remove(oldName);
				GameServer.getMapRolesMap().get(roleInfo.getMapid()).remove(oldName);
			} else {// 修改靓号
				roleInfo.setLiang_id(liangId);
				data.setLoginResult(roleInfo);
				RedisControl.insertKey(RedisParameterUtil.LIANGID, liangId.toString(),"0");
			}

			// 删除消耗物品
			AllServiceUtil.getGoodsTableService().deleteGoodsByRgid(changeRoleNameBean.getRgid());

			// 修改成功
			changeRoleNameBean.setFlag(true);

			// 群发给其他用户
			String msg = Agreement.getAgreement().ChangeRoleNameAgreement(GsonUtil.getGsonUtil().getgson().toJson(changeRoleNameBean));
			SendMessage.sendMessageToMapRoles(roleInfo.getMapid(), msg);
			RoleTable roleTable=new RoleTable();
			roleTable.setRole_id(roleInfo.getRole_id());
			roleTable.setRolename(roleInfo.getRolename());
			roleTable.setTitle(roleInfo.getTitle());
			// 修改玩家数据
			AllServiceUtil.getRoleTableService().updateByPrimaryKey(roleTable);
			RedisControl.userController("R",roleInfo.getRole_id().toString(),RedisControl.CALTER,GsonUtil.getGsonUtil().getgson().toJson(roleInfo));

			if (liangId == null) {
				// 判断结婚对象存在且不在线的时候
				if( roleInfo.getMarryObject() != null && GameServer.getRoleNameMap().get(roleInfo.getMarryObject()) == null ){
					// 查找结婚对象信息
					RoleTable marryRole = AllServiceUtil.getRoleTableService().selectRoleTableByRoleName(roleInfo.getMarryObject());
					// 如果结婚对象的称谓为它对象的名字也要修改
					if( marryRole.getTitle() != null && marryRole.getTitle().indexOf(oldName) != -1 ){
						marryRole.setTitle(marryRole.getTitle().replace(oldName, newName));
					}
					// 修改结婚对象新的结婚对象
					marryRole.setMarryObject(newName);
					// 修改结婚对象数据
					AllServiceUtil.getRoleTableService().updateByPrimaryKey(marryRole);
				}
			}
		} else {
			// 修改失败
			changeRoleNameBean.setFlag(false);

			// 返回客户端
			String msg = Agreement.getAgreement().ChangeRoleNameAgreement(GsonUtil.getGsonUtil().getgson().toJson(changeRoleNameBean));
			SendMessage.sendMessageToSlef(ctx, msg);
		}
	}
}
