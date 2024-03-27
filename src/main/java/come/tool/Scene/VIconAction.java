package come.tool.Scene;

import io.netty.channel.ChannelHandlerContext;

import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.handler.SendMessage;
import org.come.server.GameServer;

import come.tool.Scene.LaborDay.LaborScene;

public class VIconAction implements IAction{
	
	@Override
	public void action(ChannelHandlerContext ctx, String message) {
		LoginResult login = GameServer.getAllLoginRole().get(ctx);
		if (login==null) {return;}
		if (message.equals("1")) {
			String msg=LaborScene.request(login,"0");
			if (msg!=null) { SendMessage.sendMessageToSlef(ctx,msg); }
		}
	}

}
