package org.come.bean;

import java.util.List;

import org.come.entity.Mount;

public class MountResult {
	private boolean isShow;
	private List<Mount> mounts;

	public List<Mount> getMounts() {
		return mounts;
	}
	public void setMounts(List<Mount> mounts) {
		this.mounts = mounts;
	}
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean show) {
		isShow = show;
	}
}
