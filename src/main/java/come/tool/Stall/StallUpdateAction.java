package come.tool.Stall;

import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.server.GameServer;
import org.come.until.GsonUtil;

public class StallUpdateAction implements IAction {

    @Override
    public void action(ChannelHandlerContext ctx, String message) {
        Stall stall = GsonUtil.getGsonUtil().getgson().fromJson(message, Stall.class);
        if(stall.getStallType()==1){
            Stall queryStall = StallPool.getPool().getStallByRoleName(stall.getRole());
            LoginResult loginResult = GameServer.getAllLoginRole().get(ctx);
            if(queryStall==null){
                SendMessage.sendMessageByRoleName(loginResult.getRolename(), Agreement.getAgreement().PromptAgreement("该摊位已经不存在了"));
            }else{
                stall.setId(queryStall.getId());
                StallPool.getPool().getAllStall().put(stall.getId(), stall);
                SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().stallAgreement(GsonUtil.getGsonUtil().getgson().toJson(stall)));
            }
        }
    }
}
