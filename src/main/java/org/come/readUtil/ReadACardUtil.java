package org.come.readUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.come.handler.MainServerHandler;
import org.come.model.aCard;
import org.come.readBean.AllACard;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;
import org.come.until.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReadACardUtil {
	public	static List<String> pifeng=new ArrayList<>();
	public	static	List<String>guajian=new ArrayList<>();
	public	static	List<String>yaodai=new ArrayList<>();
	public	static	List<String>mianju=new ArrayList<>();
	public	static   Map<String,String>shuxing1=new HashMap<>();
	public	static	Map<String,String>shuxing2=new HashMap<>();
	public 	static List<String>jiezhi=new ArrayList<>();
	/** 扫描怪兽文件，获得全部怪兽信息*/
	public static ConcurrentHashMap<Integer,aCard> selectACards(String path, StringBuffer buffer){
		ConcurrentHashMap<Integer,aCard> allACard=new ConcurrentHashMap<>();
		String[][] result = ReadExelTool.getResult("config/"+path+".xls");
		for (int i = 2; i < result.length; i++) {
			if (result[i][0].equals("")) {continue;}
			aCard aCard=new aCard();
			for (int j = 0; j < result[i].length; j++) {
				try {
					SettModelMemberTool.setReflectRelative(aCard, result[i][j], j);
				} catch (Exception e) {
					UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j],MainServerHandler.getErrorMessage(e));
					return null;
				}
			}
			if (aCard.getId()!=0) {
				allACard.put(aCard.getId(), aCard);
			}
		}
		readdiancui();
		wuzhu();
		return allACard;
	}
	public static String createACards(ConcurrentHashMap<Integer, aCard> map) {
		AllACard aCard = new AllACard();
		Map<Integer, aCard> aMap=new HashMap<>();
		for (aCard card:map.values()) {
			aMap.put(card.getId(), card);
		}
		aCard.setaMap(aMap);
		String msgString = GsonUtil.getGsonUtil().getgson().toJson(aCard);
		return msgString;
	}
	public static void readdiancui(){
		String[][] result = ReadExelTool.getResult("config/"+"diancui"+".xls");
		if (result==null)return;
		for (int i = 1; i < result.length; i++) {
			if (result[i][0].equals("")) {continue;}
			try {
				if (i<=13){
					guajian.add(result[i][0]);
					yaodai.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);
				}else if (i<=25){
					mianju.add(result[i][0]);
					pifeng.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);
				}else if (i<=35){
					jiezhi.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);
				}else if (i<=41){
					guajian.add(result[i][0]);
					yaodai.add(result[i][0]);
					mianju.add(result[i][0]);
					pifeng.add(result[i][0]);
					jiezhi.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);
				}else if (i<=43){
					yaodai.add(result[i][0]);
					mianju.add(result[i][0]);
					pifeng.add(result[i][0]);
					jiezhi.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);
				}else {

					guajian.add(result[i][0]);
					yaodai.add(result[i][0]);
					mianju.add(result[i][0]);
					pifeng.add(result[i][0]);
					jiezhi.add(result[i][0]);
					shuxing1.put(result[i][0],result[i][7]);
					shuxing2.put(result[i][0],result[i][8]);


				}


			} catch (Exception e) {
				e.printStackTrace();
			}


		}

	}
	public  static Map<String, Double> T_yf_15_min=new HashMap<>();
	public  static Map<String, Double> T_yf_15_max=new HashMap<>();
	public  static Map<String, Double> T_mz_15_min=new HashMap<>();
	public  static Map<String, Double> T_mz_15_max=new HashMap<>();
	public  static Map<String, Double> T_wq_15_min=new HashMap<>();
	public  static Map<String, Double> T_wq_15_max=new HashMap<>();
	public  static Map<String, Double> T_xl_15_min=new HashMap<>();
	public  static Map<String, Double> T_xl_15_max=new HashMap<>();
	public  static Map<String, Double> T_xz_15_min=new HashMap<>();
	public  static Map<String, Double> T_xz_15_max=new HashMap<>();

	public  static Map<String, Double> R_yf_15_min=new HashMap<>();
	public  static Map<String, Double> R_yf_15_max=new HashMap<>();
	public  static Map<String, Double> R_mz_15_min=new HashMap<>();
	public  static Map<String, Double> R_mz_15_max=new HashMap<>();
	public  static Map<String, Double> R_wq_15_min=new HashMap<>();
	public  static Map<String, Double> R_wq_15_max=new HashMap<>();
	public  static Map<String, Double> R_xl_15_min=new HashMap<>();
	public  static Map<String, Double> R_xl_15_max=new HashMap<>();
	public  static Map<String, Double> R_xz_15_min=new HashMap<>();
	public  static Map<String, Double> R_xz_15_max=new HashMap<>();

	public  static Map<String, Double> M_yf_15_min=new HashMap<>();
	public  static Map<String, Double> M_yf_15_max=new HashMap<>();
	public  static Map<String, Double> M_mz_15_min=new HashMap<>();
	public  static Map<String, Double> M_mz_15_max=new HashMap<>();
	public  static Map<String, Double> M_wq_15_min=new HashMap<>();
	public  static Map<String, Double> M_wq_15_max=new HashMap<>();
	public  static Map<String, Double> M_xl_15_min=new HashMap<>();
	public  static Map<String, Double> M_xl_15_max=new HashMap<>();
	public  static Map<String, Double> M_xz_15_min=new HashMap<>();
	public  static Map<String, Double> M_xz_15_max=new HashMap<>();

	public  static Map<String, Double> X_yf_15_min=new HashMap<>();
	public  static Map<String, Double> X_yf_15_max=new HashMap<>();
	public  static Map<String, Double> X_mz_15_min=new HashMap<>();
	public  static Map<String, Double> X_mz_15_max=new HashMap<>();
	public  static Map<String, Double> X_wq_15_min=new HashMap<>();
	public  static Map<String, Double> X_wq_15_max=new HashMap<>();
	public  static Map<String, Double> X_xl_15_min=new HashMap<>();
	public  static Map<String, Double> X_xl_15_max=new HashMap<>();
	public  static Map<String, Double> X_xz_15_min=new HashMap<>();
	public  static Map<String, Double> X_xz_15_max=new HashMap<>();

	public  static Map<String, Double> G_yf_15_min=new HashMap<>();
	public  static Map<String, Double> G_yf_15_max=new HashMap<>();
	public  static Map<String, Double> G_mz_15_min=new HashMap<>();
	public  static Map<String, Double> G_mz_15_max=new HashMap<>();
	public  static Map<String, Double> G_wq_15_min=new HashMap<>();
	public  static Map<String, Double> G_wq_15_max=new HashMap<>();
	public  static Map<String, Double> G_xl_15_min=new HashMap<>();
	public  static Map<String, Double> G_xl_15_max=new HashMap<>();
	public  static Map<String, Double> G_xz_15_min=new HashMap<>();
	public  static Map<String, Double> G_xz_15_max=new HashMap<>();

	public  static Map<String, Double> L_yf_15_min=new HashMap<>();
	public  static Map<String, Double> L_yf_15_max=new HashMap<>();
	public  static Map<String, Double> L_mz_15_min=new HashMap<>();
	public  static Map<String, Double> L_mz_15_max=new HashMap<>();
	public  static Map<String, Double> L_wq_15_min=new HashMap<>();
	public  static Map<String, Double> L_wq_15_max=new HashMap<>();
	public  static Map<String, Double> L_xl_15_min=new HashMap<>();
	public  static Map<String, Double> L_xl_15_max=new HashMap<>();
	public  static Map<String, Double> L_xz_15_min=new HashMap<>();
	public  static Map<String, Double> L_xz_15_max=new HashMap<>();

	public  static Map<String, Double> T_yf_16_min=new HashMap<>();
	public  static Map<String, Double> T_yf_16_max=new HashMap<>();
	public  static Map<String, Double> T_mz_16_min=new HashMap<>();
	public  static Map<String, Double> T_mz_16_max=new HashMap<>();
	public  static Map<String, Double> T_wq_16_min=new HashMap<>();
	public  static Map<String, Double> T_wq_16_max=new HashMap<>();
	public  static Map<String, Double> T_xl_16_min=new HashMap<>();
	public  static Map<String, Double> T_xl_16_max=new HashMap<>();
	public  static Map<String, Double> T_xz_16_min=new HashMap<>();
	public  static Map<String, Double> T_xz_16_max=new HashMap<>();

	public  static Map<String, Double> R_yf_16_min=new HashMap<>();
	public  static Map<String, Double> R_yf_16_max=new HashMap<>();
	public  static Map<String, Double> R_mz_16_min=new HashMap<>();
	public  static Map<String, Double> R_mz_16_max=new HashMap<>();
	public  static Map<String, Double> R_wq_16_min=new HashMap<>();
	public  static Map<String, Double> R_wq_16_max=new HashMap<>();
	public  static Map<String, Double> R_xl_16_min=new HashMap<>();
	public  static Map<String, Double> R_xl_16_max=new HashMap<>();
	public  static Map<String, Double> R_xz_16_min=new HashMap<>();
	public  static Map<String, Double> R_xz_16_max=new HashMap<>();

	public  static Map<String, Double> M_yf_16_min=new HashMap<>();
	public  static Map<String, Double> M_yf_16_max=new HashMap<>();
	public  static Map<String, Double> M_mz_16_min=new HashMap<>();
	public  static Map<String, Double> M_mz_16_max=new HashMap<>();
	public  static Map<String, Double> M_wq_16_min=new HashMap<>();
	public  static Map<String, Double> M_wq_16_max=new HashMap<>();
	public  static Map<String, Double> M_xl_16_min=new HashMap<>();
	public  static Map<String, Double> M_xl_16_max=new HashMap<>();
	public  static Map<String, Double> M_xz_16_min=new HashMap<>();
	public  static Map<String, Double> M_xz_16_max=new HashMap<>();

	public  static Map<String, Double> X_yf_16_min=new HashMap<>();
	public  static Map<String, Double> X_yf_16_max=new HashMap<>();
	public  static Map<String, Double> X_mz_16_min=new HashMap<>();
	public  static Map<String, Double> X_mz_16_max=new HashMap<>();
	public  static Map<String, Double> X_wq_16_min=new HashMap<>();
	public  static Map<String, Double> X_wq_16_max=new HashMap<>();
	public  static Map<String, Double> X_xl_16_min=new HashMap<>();
	public  static Map<String, Double> X_xl_16_max=new HashMap<>();
	public  static Map<String, Double> X_xz_16_min=new HashMap<>();
	public  static Map<String, Double> X_xz_16_max=new HashMap<>();

	public  static Map<String, Double> G_yf_16_min=new HashMap<>();
	public  static Map<String, Double> G_yf_16_max=new HashMap<>();
	public  static Map<String, Double> G_mz_16_min=new HashMap<>();
	public  static Map<String, Double> G_mz_16_max=new HashMap<>();
	public  static Map<String, Double> G_wq_16_min=new HashMap<>();
	public  static Map<String, Double> G_wq_16_max=new HashMap<>();
	public  static Map<String, Double> G_xl_16_min=new HashMap<>();
	public  static Map<String, Double> G_xl_16_max=new HashMap<>();
	public  static Map<String, Double> G_xz_16_min=new HashMap<>();
	public  static Map<String, Double> G_xz_16_max=new HashMap<>();

	public  static Map<String, Double> L_yf_16_min=new HashMap<>();
	public  static Map<String, Double> L_yf_16_max=new HashMap<>();
	public  static Map<String, Double> L_mz_16_min=new HashMap<>();
	public  static Map<String, Double> L_mz_16_max=new HashMap<>();
	public  static Map<String, Double> L_wq_16_min=new HashMap<>();
	public  static Map<String, Double> L_wq_16_max=new HashMap<>();
	public  static Map<String, Double> L_xl_16_min=new HashMap<>();
	public  static Map<String, Double> L_xl_16_max=new HashMap<>();
	public  static Map<String, Double> L_xz_16_min=new HashMap<>();
	public  static Map<String, Double> L_xz_16_max=new HashMap<>();
	public static  void wuzhu() {
		String[][] result = ReadExelTool.getResult("config/" + "wuzhu" + ".xls");
		if (result == null) return;
		for (int i = 1; i < result.length; i++) {
			if (result[i][0].equals("")) {
				continue;
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("衣服15")){
				T_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("帽子15")){
				T_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("项链15")){
				T_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("鞋子15")){
				T_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("武器15")){
				T_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("衣服15")){
				L_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("帽子15")){
				L_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("项链15")){
				L_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("鞋子15")){
				L_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("武器15")){
				L_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("衣服15")){
				R_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("帽子15")){
				R_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("项链15")){
				R_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("鞋子15")){
				R_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("武器15")){
				R_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("衣服15")){
				M_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("帽子15")){
				M_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("项链15")){
				M_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("鞋子15")){
				M_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("武器15")){
				M_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("衣服15")){
				G_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("帽子15")){
				G_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("项链15")){
				G_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("鞋子15")){
				G_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("武器15")){
				G_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("衣服15")){
				X_yf_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_yf_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("帽子15")){
				X_mz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_mz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}

			if (result[i][0].equals("仙族")&&result[i][1].equals("项链15")){
				X_xl_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_xl_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("鞋子15")){
				X_xz_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_xz_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("武器15")){
				X_wq_15_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_wq_15_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("衣服16")){
				T_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("帽子16")){
				T_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("项链16")){
				T_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("鞋子16")){
				T_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("通用")&&result[i][1].equals("武器16")){
				T_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				T_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("衣服16")){
				L_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("帽子16")){
				L_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("项链16")){
				L_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("鞋子16")){
				L_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("龙族")&&result[i][1].equals("武器16")){
				L_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				L_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("衣服16")){
				R_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("帽子16")){
				R_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("项链16")){
				R_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("鞋子16")){
				R_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("人族")&&result[i][1].equals("武器16")){
				R_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				R_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("衣服16")){
				M_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("帽子16")){
				M_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("项链16")){
				M_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("鞋子16")){
				M_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("魔族")&&result[i][1].equals("武器16")){
				M_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				M_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("衣服16")){
				G_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("帽子16")){
				G_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("项链16")){
				G_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("鞋子16")){
				G_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("鬼族")&&result[i][1].equals("武器16")){
				G_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				G_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("衣服16")){
				X_yf_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_yf_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("帽子16")){
				X_mz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_mz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("项链16")){
				X_xl_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_xl_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("鞋子16")){
				X_xz_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_xz_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}
			if (result[i][0].equals("仙族")&&result[i][1].equals("武器16")){
				X_wq_16_max.put(result[i][2],Double.parseDouble(result[i][4]) );
				X_wq_16_min.put(result[i][2],Double.parseDouble(result[i][3]) );
			}

		}
	}

}
