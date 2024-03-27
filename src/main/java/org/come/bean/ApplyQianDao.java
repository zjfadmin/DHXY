package org.come.bean;

import org.come.entity.Goodstable;

import java.util.List;
import java.util.Map;

/**
 * 
* 类说明:签到对象操作bean
 */
public class ApplyQianDao {
	
	private int type;//操作类型  1 表示请求签到对象跟抽奖对象   2 签到    3  补签  4 表示抽奖
	
	
	private  int dayri;//签到的日期 1-31  签到跟补签的时候才有     抽奖的时候表示抽奖对应的日期+31 为对象Id
	
	//inter 按照 1 3 7 13 31  顺序   
	private Map<Integer, List<Goodstable>> choujianBean;
	
	
	//签到集合 日期|日期
	private String Qiandaoday;
	
	//抽奖集合  1=0|3=1|7=0|13=0|31=0  0 表示没有抽奖 1表示已经抽奖 
	private String chpujiangjihe;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDayri() {
		return dayri;
	}

	public void setDayri(int dayri) {
		this.dayri = dayri;
	}

	public Map<Integer, List<Goodstable>> getChoujianBean() {
		return choujianBean;
	}

	public void setChoujianBean(Map<Integer, List<Goodstable>> choujianBean) {
		this.choujianBean = choujianBean;
	}

	public String getQiandaoday() {
		return Qiandaoday;
	}

	public void setQiandaoday(String qiandaoday) {
		Qiandaoday = qiandaoday;
	}

	public String getChpujiangjihe() {
		return chpujiangjihe;
	}

	public void setChpujiangjihe(String chpujiangjihe) {
		this.chpujiangjihe = chpujiangjihe;
	}

}
