
package com.gl.controller;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.come.action.monitor.MonitorUtil;
import org.come.bean.ApplyBean;
import org.come.bean.ApplyPayBean;
import org.come.bean.BackRoleInfo;
import org.come.bean.ExpensesReceiptsInfo;
import org.come.bean.LoginResult;
import org.come.bean.UseCardBean;
import org.come.entity.ExpensesReceipts;
import org.come.entity.Goodsrecord;
import org.come.entity.Haters;
import org.come.entity.Mount;
import org.come.entity.PayvipBean;
import org.come.entity.Record;
import org.come.entity.RoleTable;
import org.come.entity.UserTable;
import org.come.extInterBean.Goodsrecord2;
import org.come.handler.MainServerHandler;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.protocol.ParamTool;
import org.come.redis.RedisPoolUntil;
import org.come.server.GameServer;
import org.come.tool.WriteOut;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;
import org.quartz.impl.jdbcjobstore.HSQLDBDelegate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.gl.model.Param;
import com.gl.model.Result;
import com.gl.service.PlayerService;
import com.gl.service.ResultFactory;
import com.gl.token.UserToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import come.tool.Role.PrivateData;
import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import come.tool.Scene.LaborDay.LaborScene;
import io.netty.channel.ChannelHandlerContext;
import redis.clients.jedis.Jedis;

@RestController
public class PlayerController {
   public PlayerController() {
   }

   @UserToken
   @PostMapping({"/api/role"})
   public Result roles(Param param) {
      PlayerService service = new PlayerService();
      return ResultFactory.success(service.getRole(param));
   }

   @UserToken
   @PostMapping({"/api/lockpwd"})
   public Result lockpwd(Param param) {
      PlayerService service = new PlayerService();
      return service.editLockPassword(param) ? ResultFactory.success(null) : ResultFactory.fail("操作失败");
   }

   @UserToken
   @PostMapping({"/api/roleoperation"})
   public Result operation(Param param) {
      PlayerService service = new PlayerService();
      return service.operation(param) ? ResultFactory.success(null) : ResultFactory.fail("操作失败，请确认该玩家是否存在");
   }

   @UserToken
   @PostMapping({"/api/recharge"})
   public Result recharge(Param param) {
      PlayerService service = new PlayerService();
      return service.rechargeCallBack(param) ? ResultFactory.success(null) : ResultFactory.fail("操作失败");
   }

   @UserToken
   @PostMapping({"/api/rechargeinfo"})
   public Result rechargeinfo(Param param) {
      PlayerService service = new PlayerService();
      return ResultFactory.success(service.getReceipts(param));
   }
   /**
    * 查询交易记录
    * @param param
    * @return
    */
   @UserToken
   @PostMapping(value = "/api/selectGoodsRecord")
   public Result selectGoodsRecord(Param param) {
      PlayerService service = new PlayerService();
      return ResultFactory.success(service.selectGoodsRecord(param));
   }

}
