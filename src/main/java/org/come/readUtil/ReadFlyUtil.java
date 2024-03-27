package org.come.readUtil;

import org.come.entity.Fly;
import org.come.handler.MainServerHandler;
import org.come.readBean.AllFly;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;
import org.come.until.GsonUtil;

import java.util.concurrent.ConcurrentHashMap;

public class ReadFlyUtil {
    /**
     * 读取新加飞行器表格
     * @param path
     * @param buffer
     * @return
     */
    public static ConcurrentHashMap<Integer, Fly> getAllFly(String path, StringBuffer buffer){
        ConcurrentHashMap<Integer, Fly> getAllFly = new ConcurrentHashMap<Integer,Fly>();
        String[][] result = ReadExelTool.getResult("config/"+path+".xls");
        for (int i = 1; i < result.length; i++) {
            if (result[i][0].equals("")) {continue;}
            Fly fly = new Fly();
            for (int j = 0; j < result[i].length; j++) {
                try {
                    SettModelMemberTool.setReflectRelative(fly, result[i][j], j);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j],MainServerHandler.getErrorMessage(e));
                    return null;
                }
            }
            try {
                if (fly.getFlyId() == null) {continue;}
                Fly fly1 = getAllFly.get(Integer.parseInt(fly.getFlyId().toString()));
                if (fly1==null) {
                    getAllFly.put(Integer.parseInt(fly.getFlyId().toString()), fly);
                }

            } catch (Exception e) {
                // TODO: handle exception
                UpXlsAndTxtFile.addStringBufferMessage(buffer, i, 0,"解析错误",MainServerHandler.getErrorMessage(e));
                return null;
            }
        }
        return getAllFly;
    }

    /**获取飞行器表txt数据*/
    public static String createTxtFly(ConcurrentHashMap<Integer, Fly> map){
        AllFly allFly = new AllFly();
        allFly.setAllFly(map);
        String msgString = GsonUtil.getGsonUtil().getgson().toJson(allFly);
        return msgString;
    }
}
