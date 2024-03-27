package org.come.action.monster;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.come.action.IAction;
import org.come.handler.SendMessage;
import org.come.model.Boos;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.task.MapMonsterBean;
import org.come.task.MonsterUtil;

import java.util.List;

public class SlelectMonsterAction implements IAction {
    @Override
    public void action(ChannelHandlerContext ctx, String message) {
        if (StringUtils.isNotBlank(message)) {
            String[] vs = message.split("-");
            try {
                for (int i = 1; i < vs.length; i++) {
                    Boos boos = GameServer.boosesMap.get(vs[i]);
                    if (boos == null) continue;
                    long mapId=GameServer.getMapIds(boos.getBoosmapname());
                    List<MapMonsterBean> list = MonsterUtil.getList(mapId, Integer.parseInt(boos.getBoosrobot()));
                    if (list != null && list.size() > 0) {
                        MapMonsterBean mapMonsterBean = list.get(GameServer.random.nextInt(list.size()));
                        // 发送数据
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(mapMonsterBean.getMap());
                        buffer.append("-");
                        buffer.append(mapMonsterBean.getX());
                        buffer.append("-");
                        buffer.append(mapMonsterBean.getY());
                        buffer.append("-");
                        buffer.append(mapMonsterBean.getI());
                        SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().zhuShouAgreement(buffer.toString()));
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().zhuShouAgreement(vs[0]));
        }
    }
}
