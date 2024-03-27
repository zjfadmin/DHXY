package org.come.action.role;

import io.netty.channel.ChannelHandlerContext;
import org.come.action.IAction;
import org.come.bean.LoginResult;
import org.come.entity.RoleSummoning;
import org.come.redis.RedisControl;
import org.come.server.GameServer;
import org.come.until.AllServiceUtil;

import java.math.BigDecimal;

//锁定首发
public class LockIndexPetAction implements IAction {

@Override
public void action(ChannelHandlerContext ctx, String message) {

System.out.println(message);

// 获得角色信息
LoginResult roleInfo = GameServer.getAllLoginRole().get(ctx);

String[] split = message.split("\\|");
String tag = split[0];
String petId = split[1];

RoleSummoning roleSummoning = AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRgID(new BigDecimal(petId));
if(roleSummoning == null){
System.out.println("宠物不存在");
return;
}
if(!roleSummoning.getRoleid().toString().equals(roleInfo.getRole_id().toString())){
System.out.println("宠物不是你的");
return;
}

if(tag.equals("1")){
RedisControl.insertKey(GameServer.area+"_indexPet_"+roleInfo.getRole_id(), roleInfo.getRole_id().toString(), petId);
roleInfo.setSummoning_id(new BigDecimal(petId));
}else{
RedisControl.delForKey(GameServer.area+"_indexPet_"+roleInfo.getRole_id(), roleInfo.getRole_id().toString());
roleInfo.setSummoning_id(new BigDecimal(petId));
}
}
}
