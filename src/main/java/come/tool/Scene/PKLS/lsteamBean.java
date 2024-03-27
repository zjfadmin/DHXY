package come.tool.Scene.PKLS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import come.tool.Role.RoleData;
import come.tool.Role.RolePool;
import org.come.bean.LoginResult;
import org.come.entity.*;
import org.come.until.AllServiceUtil;

import come.tool.Scene.LTS.UserData;

public class lsteamBean {

	private List<LSTeam> LSTeams;
    
	private List<UserData> userDatas;
    
	private String teams;//报名数据
	
	public void initUserData(){
		System.out.println("读取玩家报名数据");
		userDatas=new ArrayList<>();
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < LSTeams.size(); i++) {
			if (buffer.length()!=0) {
				buffer.append("\n");
			}
			LSTeam lsTeam=LSTeams.get(i);
			for (int j = 0; j < lsTeam.getRoleids().length; j++) {
				 try {Thread.sleep(10);} catch (Exception e) {}
			     if (j!=0) {buffer.append("|");}
				 BigDecimal role_id=lsTeam.getRoleids()[j];
				 LoginResult loginResult=AllServiceUtil.getRoleTableService().selectRoleID(role_id);
				 buffer.append(loginResult.getRolename());
				 UserTable userTable=AllServiceUtil.getUserTableService().selectByPrimaryKey(loginResult.getUser_id());
				 try {Thread.sleep(10);} catch (Exception e) {}
			     List<Goodstable> goodstables=AllServiceUtil.getGoodsTableService().getGoodsByRoleID(role_id);
			     try {Thread.sleep(10);} catch (Exception e) {}
			     List<RoleSummoning> pets=AllServiceUtil.getRoleSummoningService().selectRoleSummoningsByRoleID(role_id);
				 RoleData roleData = RolePool.getRoleData(role_id);
				 pets = roleData.getPackRecord().petOrder(pets);
			     try {Thread.sleep(10);} catch (Exception e) {}
			     List<Mount> mounts=AllServiceUtil.getMountService().selectMountsByRoleID(role_id);
			     try {
			     	Thread.sleep(10);
				 }catch (Exception e){}
			     List<Fly>flys=AllServiceUtil.getFlyService().selectFlyByRoleID(role_id);//新加飞行器
			     try {Thread.sleep(10);} catch (Exception e) {}
			     List<Lingbao> lingbaos=AllServiceUtil.getLingbaoService().selectLingbaoByRoleID(role_id);
			     try {Thread.sleep(10);} catch (Exception e) {}
			     List<Baby> babys=AllServiceUtil.getBabyService().selectBabyByRolename(role_id);
			     UserData userData=new UserData(userTable, loginResult, goodstables, pets, mounts, lingbaos, babys,flys);
				 userDatas.add(userData);
			}
		}
		teams=buffer.toString();
	}
	public List<LSTeam> getLSTeams() {
		return LSTeams;
	}
	public void setLSTeams(List<LSTeam> lSTeams) {
		LSTeams = lSTeams;
	}
	public List<UserData> getUserDatas() {
		return userDatas;
	}
	public void setUserDatas(List<UserData> userDatas) {
		this.userDatas = userDatas;
	}
	public String getTeams() {
		return teams;
	}
	public void setTeams(String teams) {
		this.teams = teams;
	}	
}
