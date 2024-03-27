package org.come.action.Fly;

import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.bean.FlyResult;
import org.come.entity.Fly;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.List;

public class FlyGetAction implements IAction {
    public void action(ChannelHandlerContext ctx, String message){
        Fly fly = GsonUtil.getGsonUtil().getgson().fromJson(message, Fly.class);
        AllServiceUtil.getFlyService().insertFly(fly);
        BigDecimal roleID = GameServer.getAllLoginRole().get(ctx).getRole_id();
        List<Fly> flys = AllServiceUtil.getFlyService().selectFlyByRoleID(roleID);
     FlyResult flyResult = new FlyResult();
        flyResult.setFlys(flys);

        String msg = Agreement.getAgreement().FlyAgreement(GsonUtil.getGsonUtil().getgson().toJson(flyResult));
        SendMessage.sendMessageToSlef(ctx, msg);
    }
}
