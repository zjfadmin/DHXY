package org.come.bean;

import org.come.entity.Goodstable;
import org.come.entity.PackRecord;

import java.util.List;

/**
 * 
 * @author Administrator：背包刷新
 *
 */
public class FreshPackBean {
	
	private List<Goodstable> goods;
	//背包记录数据
		private PackRecord packRecord;
	public List<Goodstable> getGoods() {
		return goods;
	}

	public void setGoods(List<Goodstable> goods) {
		this.goods = goods;
	}

	public PackRecord getPackRecord() {
		return packRecord;
	}

	public void setPackRecord(PackRecord packRecord) {
		this.packRecord = packRecord;
	}
	
	
	

}
