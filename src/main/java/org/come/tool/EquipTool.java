package org.come.tool;

import java.util.List;

/**
 * 判断是否为可叠加  物品叠加
 * @author 叶豪芳
 * @date 2017年12月24日 下午12:31:36
 *
 */
public class EquipTool {
	static List<Long> strings = SplitStringTool.splitLong("0-1|8|49-52|88|99|100|111|112|118|119|120|123-127|189|190" +
			"|191|212|492-505|507|513-515|521-524|715|716|721|901-910|2040-2043|2053|2070-2079|2080|2113-2116|" +
			"7005|7010|801-802|1002|1003|1005|1006|1008|2323|2324|728|556|557|889|888|891|744|8883-8887|" +
			"60003|7500-7502|7511|28955|915|918|10086|919|667|666|923|932|933|935|938|939|9002|8002|8003|" +
			"7878|8004|2327|924|8891|8887|8890|8891|8892|8893|60002|960|961|965|1103-1105|8800|66685|" +
			"779|725|810-815");
	public static boolean canSuper( long goodstype ){// 可重叠返回true
		return strings.contains(goodstype);
	}
	public static boolean contains(String[] vs,String key){
		if (vs!=null) {
			for (int i = 0; i < vs.length; i++) {
				if (vs[i].equals(key)) {
					return true;
				}
			}
		}
		return false;
	}
}
