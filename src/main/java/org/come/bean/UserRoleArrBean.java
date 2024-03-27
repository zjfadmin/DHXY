package org.come.bean;

import java.util.List;
/**
 * 返回该用户下所有角色的信息
 * @author 叶豪芳
 * @date : 2017年11月26日 下午8:46:11
 */
public class UserRoleArrBean {
	private List<LoginResult>  list;
	private int phonestatues;
	private int index;
	private String seasonInfo;
	private String currSeason;

	public UserRoleArrBean() {
		// TODO Auto-generated constructor stub
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public List<LoginResult> getList() {
		return list;
	}
	public void setList(List<LoginResult> list) {
		this.list = list;
	}
	public int getPhonestatues() {
		return phonestatues;
	}
	public void setPhonestatues(int phonestatues) {
		this.phonestatues = phonestatues;
	}

	public String getSeasonInfo() {
		return seasonInfo;
	}

	public void setSeasonInfo(String seasonInfo) {
		this.seasonInfo = seasonInfo;
	}

	public String getCurrSeason() {
		return currSeason;
	}

	public void setCurrSeason(String currSeason) {
		this.currSeason = currSeason;
	}
}
