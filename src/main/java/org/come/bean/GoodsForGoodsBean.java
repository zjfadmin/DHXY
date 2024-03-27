package org.come.bean;

import org.come.entity.Goodstable;

import java.util.List;

public class GoodsForGoodsBean {
	
	private int id; // 兑换id
	
	private int itemid; // 物品
	
	private String nameString=null; // 名称
	
	
	private int type;//兑换物品类型  1 杂物  2 套装  3 仙器皿  4 宠物   0 表示全部类别
	
	private String delte;//消耗的物品集合
	
	private String getGoods;//获取的物品集合
	
	
	private List<Goodstable> getApplyGoods;//获取到得物品
	
	private  List<Goodstable> delGoodstables;//消耗得物品

	
	
	public List<Goodstable> getGetApplyGoods() {
		return getApplyGoods;
	}

	public void setGetApplyGoods(List<Goodstable> getApplyGoods) {
		this.getApplyGoods = getApplyGoods;
	}

	public List<Goodstable> getDelGoodstables() {
		return delGoodstables;
	}

	public void setDelGoodstables(List<Goodstable> delGoodstables) {
		this.delGoodstables = delGoodstables;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getNameString() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDelte() {
		return delte;
	}

	public void setDelte(String delte) {
		this.delte = delte;
	}

	public String getGetGoods() {
		return getGoods;
	}

	public void setGetGoods(String getGoods) {
		this.getGoods = getGoods;
	}
	
	
	

}
