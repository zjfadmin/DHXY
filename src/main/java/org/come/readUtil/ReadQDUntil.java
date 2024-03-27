package org.come.readUtil;

import org.come.bean.QIanDaoBean;
import org.come.handler.MainServerHandler;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;

import java.util.HashMap;

/**
 * 

* 类说明:签到
 */
public class ReadQDUntil {

	public static HashMap<String, QIanDaoBean> getQianDaoBean(String path, StringBuffer buffer){
		HashMap<String, QIanDaoBean> getGoodsForGoodsBean = new HashMap<String, QIanDaoBean>();
		String[][] result = ReadExelTool.getResult("config/"+path+".xls");
		for (int i = 1; i < result.length; i++) {
			if (result[i][0].equals("")) {continue;}
			QIanDaoBean good = new QIanDaoBean();
			for (int j = 0; j < result[i].length; j++) {
				try {
					SettModelMemberTool.setReflectRelative(good, result[i][j], j);
				} catch (Exception e) {
					UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j], MainServerHandler.getErrorMessage(e));
					return null;
				}
				
			}
			getGoodsForGoodsBean.put(good.getDay(), good);
		}
		return getGoodsForGoodsBean;
	}



}
