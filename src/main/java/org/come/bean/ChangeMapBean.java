package org.come.bean;

import org.come.server.GameServer;

/**
 * 传送地图bean
 * @author 叶豪芳
 *
 */
public class ChangeMapBean {
	// 地图ID
	private String mapid;
	// 坐标
	private int mapx;
	private int mapy;
	private int type;
	public ChangeMapBean() {
		// TODO Auto-generated constructor stub
	}
	public ChangeMapBean(long mapid, int mapx, int mapy) {
		this.mapid = String.valueOf(mapid);
		this.mapx = mapx + random();
		this.mapy = mapy + random();
	}

	public ChangeMapBean(String mapid, int mapx, int mapy) {
		super();
		this.mapid = mapid;
		this.mapx = mapx;
		this.mapy = mapy;
	}

	private int random() {
		return 60 - GameServer.random.nextInt(121);
	}

	public String getMapid() {
		return mapid;
	}
	public int getMapx() {
		return mapx;
	}
	public void setMapx(int mapx) {
		this.mapx = mapx;
	}
	public int getMapy() {
		return mapy;
	}
	public void setMapy(int mapy) {
		this.mapy = mapy;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	
}
