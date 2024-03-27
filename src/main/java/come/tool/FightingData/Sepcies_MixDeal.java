package come.tool.FightingData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.come.entity.Species;

/**种族杂项处理*/
public class Sepcies_MixDeal {
	/**判断异性 非异性 0不对比 1同性 2不同性*/
	public static int isSex(BigDecimal se1, BigDecimal se2) {
		if (se1 == null || se2 == null) {
			return 0;
		}
		return getSex(se1) == getSex(se2) ? 1 : 2;
	}
	//根据id换取性别0女 1男
	public static int getSex(BigDecimal se) {
		if (se.intValue() == 20012 || se.intValue() == 21012 || se.intValue() == 22012)
			return 0;
		int id = se.intValue() % 10;
//		System.out.println("人物编号：" + se + "%10ID：" + id);
		return (id == 1 || id == 2 || id == 3 || id == 7 || id == 9) ? 1 : 0;
	}
	//获取种族
	//获取种族   青阳史  用于转换宗族出错问题
	public static int getRace(BigDecimal se) {
		if (se == null) {
			return 0;
		}
		int id = se.intValue();
		if (id >= 20001 && id <= 20012) {
			return 10001;
		} else if (id >= 21001 && id <= 21012) {
			return 10002;
		} else if (id >= 22001 && id <= 22012) {
			return 10003;
		} else if (id >= 23001 && id <= 23010) {
			return 10004;
		} else {
			return 10005;
		}
	}
	//获取种族
	public static String getRaceString(BigDecimal se) {
		int id = se.intValue();
		if (id >= 20001 && id <= 20012) {
			return "人族";
		} else if (id >= 21001 && id <= 21012) {
			return "魔族";
		} else if (id >= 22001 && id <= 22012) {
			return "仙族";
		} else if (id >= 23001 && id <= 23012) {
			return "鬼族";
		} else {
			return "龙族";
		}
	}
	static Map<Integer,String> localNameMap;
	static Map<Integer, Integer> roleWeapon;
	static {
		localNameMap = new HashMap<>();
		localNameMap.put(20001, "逍遥生");
		localNameMap.put(20002, "剑侠客");
		localNameMap.put(20003, "猛壮士");
		localNameMap.put(20004, "飞燕女");
		localNameMap.put(20005, "英女侠");
		localNameMap.put(20006, "俏千金");
		localNameMap.put(20007, "飞剑侠");
		localNameMap.put(20008, "燕山雪");
		localNameMap.put(20009, "纯阳子");
		localNameMap.put(20010, "红拂女");
		localNameMap.put(20011, "神秀生");
		localNameMap.put(20012, "玲珑女");

		localNameMap.put(21001, "虎头怪");
		localNameMap.put(21002, "夺命妖");
		localNameMap.put(21003, "巨魔王");
		localNameMap.put(21004, "小蛮妖");
		localNameMap.put(21005, "骨精灵");
		localNameMap.put(21006, "狐美人");
		localNameMap.put(21007, "逆天魔");
		localNameMap.put(21008, "媚灵狐");
		localNameMap.put(21009, "混天魔");
		localNameMap.put(21010, "九尾狐");
		localNameMap.put(21011, "绝影魔");
		localNameMap.put(21012, "霜月灵");

		localNameMap.put(22001, "神天兵");
		localNameMap.put(22002, "智圣仙");
		localNameMap.put(22003, "龙战将");
		localNameMap.put(22004, "精灵仙");
		localNameMap.put(22005, "舞天姬");
		localNameMap.put(22006, "玄剑娥");
		localNameMap.put(22007, "武尊神");
		localNameMap.put(22008, "玄天姬");
		localNameMap.put(22009, "紫薇神");
		localNameMap.put(22010, "霓裳仙");
		localNameMap.put(22011, "青阳使");
		localNameMap.put(22012, "云中君");


		localNameMap.put(23001, "祭剑魂");
		localNameMap.put(23002, "猎魂引");
		localNameMap.put(23003, "无崖子");
		localNameMap.put(23004, "墨衣行");
		localNameMap.put(23005, "夜溪灵");
		localNameMap.put(23006, "幽梦影");
		localNameMap.put(23007, "南冠客");
		localNameMap.put(23008, "镜花影");

		localNameMap.put(24001, "沧浪君");
		localNameMap.put(24002, "龙渊客");
		localNameMap.put(24003, "忘忧子");
		localNameMap.put(24004, "骊珠儿");
		localNameMap.put(24005, "木兰行");
		localNameMap.put(24006, "莫解语");
		localNameMap.put(24007, "游无极");
		localNameMap.put(24008, "临九渊");

		roleWeapon = new HashMap<>();
		roleWeapon.put(230020012,0); //猎魂引 枪
		roleWeapon.put(230020112,1); //猎魂引 枪
		roleWeapon.put(220010012,0); //神天兵 枪
		roleWeapon.put(220070012,0); //武尊神 枪
		roleWeapon.put(220070112,1); //武尊神 枪
		roleWeapon.put(220070212,1); //武尊神 枪
		roleWeapon.put(210050007,0); //骨精灵 棍
		roleWeapon.put(220050007,1); //舞天姬 棍
		roleWeapon.put(220030007,0); //龙战将 棍
		roleWeapon.put(210010007,0); //虎头怪 棍
		roleWeapon.put(200020003,1); //剑侠客 锤
		roleWeapon.put(200020103,1); //剑侠客 锤
		roleWeapon.put(200100001,1); //红拂女 剑
		roleWeapon.put(200100101,1); //红拂女 剑
		roleWeapon.put(210070012,1); //逆天魔 枪
		roleWeapon.put(200010001,1); //逍遥生 剑
	}
	public static String getLocalName(int id){
		return localNameMap.get(id);
	}
	public static Integer getRoleWeapon(int id){
		return roleWeapon.get(id);
	}
	/**获取Species*/
	public static Species getSpecies(BigDecimal species_Id){
		Species species=new Species();
		species.setSpecies_id(species_Id);
		species.setSex(getSex(species_Id)==0?"女":"男");
		species.setRace_id(new BigDecimal(getRace(species_Id)));
		species.setLocalname(getLocalName(species_Id.intValue()));
		return species;
	}
}
