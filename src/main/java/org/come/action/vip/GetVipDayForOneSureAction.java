package org.come.action.vip;

import com.gl.model.Param;
import com.gl.service.GoodsService;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.entity.VipDayFor;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;

import java.util.List;

public class GetVipDayForOneSureAction implements IAction {

    @Override
    public void action(ChannelHandlerContext ctx, String message) {
        // 获取该角色信息
        LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);
        RoleData roleData = RolePool.getRoleData(roleInfo.getRole_id());
        // 判断是否已经领取对应的礼包
        Integer vipDayGet = roleInfo.getVipDayGet();
        if (roleData.getLimit("VIP") == null) {
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("您当前尚未拥有月卡哦!"));
            return;
        }
        if (vipDayGet.equals(1)) {
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("您已经领取过月卡奖励!"));
            return;
        }
        // 属于VIP并且第一次领取就发送物品
        List<VipDayFor> vipDayGoods = GameServer.getVipDayGet().get("vip");
        vipDayGoods.forEach(v -> {
            GoodsService goodsService = new GoodsService();
            Param param = new Param();
            param.setValue1(roleInfo.getRolename());
            param.setValue2(v.getGoodId());
            param.setValue3(v.getCount() + "");
            goodsService.sendGoods(param);
        });
        roleInfo.setVipDayGet(1);
    }
}
