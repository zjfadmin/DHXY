package come.tool.Role;

import come.tool.Calculation.BaseValue;

public class BaseMeridians {
	//	经脉编号_修炼进度_品质_属性_值
	private int bh;//经脉编号
	private int exp;//进度
	private int xs;//品质
	private String key;
	private double value;

	public BaseMeridians(int bh, int exp, int xs, String key, double value) {
		super();
		this.bh = bh;
		this.exp= exp;
		init(xs, key, value);
	}
	public void init(int xs, String key, double value){
		this.xs = xs;
		this.key = key;
		this.value = value;
	}
	/**获取经脉编号*/
	public int getBh() { return bh; }
	/**获取key*/
	public int getLvl() { return BaseValue.getMeridiansLvl(exp); }
	/**获取key*/
	public String getKey() { return key; }
	/**获取总加成*/
	public double getKeyValue(){ return value*getLvl()*xs/100D; }
	/**品质*/
	public int getXs() {return xs;}
	/**获取当前经验*/
	public int getExp() {return exp;}
	public int getUpExp(int lvl){
		return (exp -BaseValue.getMeridiansTotalExp(lvl-1));
	}
	/**获取下一级经验*/
	public int getNextExp(int lvl){
		return BaseValue.getMeridiansExp(lvl);
	}
	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setXs(int xs) {
		this.xs = xs;
	}

	public void setBh(int bh) {
		this.bh = bh;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String toString() {
		return this.bh + "_" + this.exp + "_" + this.xs + "_" + this.key + "_" + this.value;
	}
}
