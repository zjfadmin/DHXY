package org.come.action.role;

import come.tool.BangBattle.BangBattlePool;
import come.tool.BangBattle.BangFight;
import come.tool.BangBattle.Member;
import come.tool.PK.PKPool;
import come.tool.PK.PkMatch;
import come.tool.Role.RolePool;
import come.tool.Scene.DNTG.DNTGRole;
import come.tool.Scene.DNTG.DNTGScene;
import come.tool.Scene.LTS.LTSArena;
import come.tool.Scene.LTS.LTSScene;
import come.tool.Scene.Scene;
import come.tool.Scene.SceneUtil;
import come.tool.newTeam.TeamBean;
import come.tool.newTeam.TeamRole;
import come.tool.newTeam.TeamUtil;
import io.netty.channel.ChannelHandlerContext;
import org.come.bean.LoginResult;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.redis.RedisCacheUtil;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class People {
    public static List<TeamBean>teamBeanList=new ArrayList<>();
    public static List<TeamBean>yuwai=new ArrayList<>();
    public static List<TeamBean>xiuluo=new ArrayList<>();
    public static List<TeamBean>guiwang=new ArrayList<>();
    public static List<TeamBean>tianting=new ArrayList<>();
    public static Map<Integer,String>ID=new HashMap<>();
    public static List<Integer>id=new ArrayList<>();
    public static void creatteam(){
//        if (RedisCacheUtil.jiqiren.size()!=0){
//            for (int i=0;i<=RedisCacheUtil.jiqiren.size()-1;i=i+3){
//                LoginResult loginResult=RedisCacheUtil.jiqiren.get(i);
//                TeamBean teamBean=TeamUtil.getTeam(loginResult.getTroop_id());
//                if (teamBean!=null) {
//                    SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("你已经有队伍了"));
//                    return;
//                }
//                String[] k=CreatPeople.lvl2(loginResult.getGrade()).split("-");
//                if (Integer.parseInt(k[1])<110&&Integer.parseInt(k[1])>102)
//                    continue;
//                teamBean= TeamUtil.addTeam(loginResult.getTeamRole());
//                loginResult.setTroop_id(teamBean.getTeamId());
//                loginResult.setTeamInfo(teamBean.getTeamInfo());
//                int lvl=Integer.parseInt(CreatPeople.lvl2(loginResult.getGrade()).split("-")[1]);
//                if (lvl>70&&lvl<=102){
//                    tianting.add(teamBean);
//                    ID.put(teamBean.getTeamId().intValue(),"tianting");
//                    id.add(teamBean.getTeamId().intValue());
//                }else if (lvl>=110&&lvl<=140){
//                    xiuluo.add(teamBean);
//                    ID.put(teamBean.getTeamId().intValue(),"xiuluo");//这两个反了
//                    id.add(teamBean.getTeamId().intValue());
//                }else if (lvl>140){
//                    yuwai.add(teamBean);
//                    ID.put(teamBean.getTeamId().intValue(),"yuwai");
//                    id.add(teamBean.getTeamId().intValue());
//                }
//                //发送给地图
//                StringBuffer buffer=new StringBuffer();
//                buffer.append(teamBean.getTeamId());
//                buffer.append("#");
//                buffer.append(teamBean.getTeamInfo());
//                String msg= Agreement.getAgreement().team3Agreement(buffer.toString());
//                SendMessage.sendMessageToMapRoles(loginResult.getMapid(), msg);
//                //发送给自己
//                SendMessage.sendMessageByRoleName(loginResult.getRolename(), Agreement.getAgreement().team1Agreement(GsonUtil.getGsonUtil().getgson().toJson(teamBean)));
//            }
//        }
    }
    public static void addteam(TeamBean bean,LoginResult loginResult,BigDecimal applyId,ChannelHandlerContext ctx){
        if (bean.getTeamSize()>=5) {
            SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("队伍已满"));
            return;
        }
        /*if (loginResult.getMapid()!=1207){
            SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("请去长安组队"));
            return;
        }*/
        /*for (int j=0;j<=id.size()-1;j++){
            if (bean.getTeamId().intValue()==id.get(j)){
                if (CreatPeople.lvl(loginResult.getGrade())>=70&&CreatPeople.lvl(loginResult.getGrade())<=102){
                    if (!ID.get(bean.getTeamId().intValue()).equals("tianting"))
                    {
                        SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("天庭队伍对，请确认等级！"));
                        return;
                    }
                }else if (CreatPeople.lvl(loginResult.getGrade())>=110&&CreatPeople.lvl(loginResult.getGrade())<=165){
                    if (!ID.get(bean.getTeamId().intValue()).equals("xiuluo"))
                    {
                        SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("修罗队伍对，请确认等级！"));
                        return;
                    }
                }else if (CreatPeople.lvl(loginResult.getGrade())>=140&&CreatPeople.lvl(loginResult.getGrade())<=200){
                    if (!ID.get(bean.getTeamId().intValue()).equals("yuwai"))
                    {
                        SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("域外队伍对，请确认等级！"));
                        return;
                    }
                }
            }
        }
*/
        TeamRole teamRole=bean.getApply(applyId);
        if (teamRole==null) {return;}
        LoginResult login= RolePool.getLoginResult(teamRole.getRoleId());
        if (login==null) {
            SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("玩家已经离线"));
            return;
        }
        TeamBean teamBean=TeamUtil.getTeam(login.getTroop_id());
        if (teamBean!=null) {
            SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("玩家已经有队伍了"));
            return;
        }
        if (login.getMapid() == 3315) {
            BangFight bangFight = BangBattlePool.getBangBattlePool().getBangFight(login.getGang_id());
            if (bangFight != null) {
                Member member = bangFight.getrole(login.getRolename());
                if (member != null) {
                    if (member.getState() != 0) {
                        SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("申请人处于忙碌状态"));
                        return;
                    }
                    Member member2 = bangFight.getrole(loginResult.getRolename());
                    if (member2 != null) {
                        if (member2.getCamp().compareTo(member.getCamp()) != 0) {
                            SendMessage.sendMessageToSlef(ctx,Agreement.getAgreement().PromptAgreement("申请人和你不是同个阵营"));
                            return;
                        }
                    }
                }
            }
        } else if (login.getMapid() == DNTGScene.mapIdF || login.getMapid() == DNTGScene.mapIdZ
                ||loginResult.getMapid() == DNTGScene.mapIdF || loginResult.getMapid() == DNTGScene.mapIdZ) {
            Scene scene = SceneUtil.getScene(SceneUtil.DNTGID);
            if (scene != null) {
                DNTGScene dntgScene = (DNTGScene) scene;
                DNTGRole role1 = dntgScene.getRole(loginResult.getRole_id());
                DNTGRole role2 = dntgScene.getRole(login.getRole_id());
                if (role1 == null || role2 == null || role1.getCamp() != role2.getCamp()) {
                    SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("申请人和你不是同个阵营"));
                    return;
                }
            }
        } else if ((loginResult.getMapid() >= 3329 && loginResult.getMapid() <= 3332)||(login.getMapid() >= 3329 && login.getMapid() <= 3332)) {
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("禁止组队"));
            return;
        }
        PkMatch match = PKPool.getPkPool().seekPkMatch(login.getRole_id());
        if (match != null) {
            SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("申请人是携带战书没法加入队伍"));
            return;
        }
        match = PKPool.getPkPool().seekPkMatch(loginResult.getRole_id());
        if (match != null && match.getType() == 11) {
            Scene scene = SceneUtil.getScene(SceneUtil.LTSID);
            if (scene != null) {
                LTSScene ltsScene = (LTSScene) scene;
                LTSArena arena = ltsScene.getLZ(loginResult.getRole_id());
                if (arena != null) {
                    if (login.getGrade() > arena.getMaxLvl()) {
                        SendMessage.sendMessageToSlef(ctx, Agreement.getAgreement().PromptAgreement("申请人超过当前擂台等级限制"));
                        return;
                    }
                }
            }
        }

        teamRole=login.getTeamRole();
        bean.addTeamRole(teamRole,loginResult.getMapid().longValue()==login.getMapid().longValue()?0:-1);
    }
}
