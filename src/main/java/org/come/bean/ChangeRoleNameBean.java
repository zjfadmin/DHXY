package org.come.bean;

import java.math.BigDecimal;

/**
 * 修改名字
 * @author Administrator
 *
 */
public class ChangeRoleNameBean {
	// 靓号ID
	private BigDecimal liangID;

	// 旧名字
	private String oldName;
	
	// 新名字
	private String newName;
	
	// 成功标识
	private boolean flag;
	
	// 消耗物品
	private BigDecimal rgid;

	public BigDecimal getLiangID() {
		return liangID;
	}

	public void setLiangID(BigDecimal liangID) {
		this.liangID = liangID;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public BigDecimal getRgid() {
		return rgid;
	}

	public void setRgid(BigDecimal rgid) {
		this.rgid = rgid;
	}

}
