package org.come.readUtil;

import org.come.handler.MainServerHandler;
import org.come.model.QianQian;
import org.come.readBean.AllQianQian;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;
import org.come.until.GsonUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReadQianQianUtil
{
	/**扫描文件，获得全部npc信息*/
	public static ConcurrentHashMap<Integer,QianQian> selectQianQian(String path, StringBuffer buffer){
		ConcurrentHashMap<Integer,QianQian> qianQianConcurrentHashMap= new ConcurrentHashMap<>();
		String[][] result = ReadExelTool.getResult("config/"+path+".xls");
		for (int i = 2; i < result.length; i++) {
			if (result[i][0].equals(""))continue;
			QianQian scheme=new QianQian();
			for (int j = 0; j < result[i].length; j++) {
				try {
                    SettModelMemberTool.setReflectRelative(scheme, result[i][j], j);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j],MainServerHandler.getErrorMessage(e));
                    return null;
                }
			}
			qianQianConcurrentHashMap.put(scheme.getId(),scheme);

		}

		return qianQianConcurrentHashMap;
	}
	public static String createQianQian(ConcurrentHashMap<Integer, QianQian> map) {
		AllQianQian allTalent = new AllQianQian();
		Map<BigDecimal, QianQian> allMap=new HashMap<>();
		for (QianQian QianQian:map.values()) {
			allMap.put(BigDecimal.valueOf(QianQian.getId()), QianQian);
		}
		allTalent.setAllTalent(allMap);
		String msgString = GsonUtil.getGsonUtil().getgson().toJson(allTalent);
		return msgString;
	}
}
