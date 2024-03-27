package org.come.action.mount;

import io.netty.channel.ChannelHandlerContext;

import java.math.BigDecimal;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.entity.Mount;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
/**
 * 放生坐骑
 * @author 叶豪芳
 *
 */
//修复删除坐骑
public class MountReleaseAction implements IAction {

	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		LoginResult loginResult = GameServer.getAllLoginRole ().get (ctx);
		if(loginResult==null){return;}
		Mount mount=AllServiceUtil.getMountService ().selectMountsByMID (new BigDecimal (message));
		if (mount==null) {return;}
		if (mount.getRoleid ().compareTo (loginResult.getRole_id())!=0) {return;}
		// 删除坐骑
		AllServiceUtil.getMountService().deleteMountsByMid(new BigDecimal(message));
		
		// 删除该坐骑下的法术
		AllServiceUtil.getMountskillService().deleteMountskills(new BigDecimal(message));

	}

}
