package org.come.action.Fly;

import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;

public class FlyCarryAction implements IAction {
    public void action(ChannelHandlerContext ctx, String message) {

//        if( message != null ){
//            // 修改角色信息
//            GameServer.getAllLoginRole().get(ctx).setFly_id(Integer.parseInt(message));
//        }
//        // 休息坐骑
//        else{
//            // 修改角色信息
//            GameServer.getAllLoginRole().get(ctx).setRace_id(null);
//        }
//
//        // 广播角色信息
//        SendMessage.sendMessageToMapRoles(GameServer.getAllLoginRole().get(ctx).getMapid(), Agreement.getAgreement().FlyAgreement(GsonUtil.getGsonUtil().getgson().toJson(GameServer.getAllLoginRole().get(ctx))));
    }



}
