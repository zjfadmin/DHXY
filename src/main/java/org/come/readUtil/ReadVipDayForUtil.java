package org.come.readUtil;

import org.apache.commons.lang3.StringUtils;
import org.come.entity.VipDayFor;
import org.come.handler.MainServerHandler;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;
import org.come.until.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReadVipDayForUtil {
    public static ConcurrentHashMap<String, List<VipDayFor>> getVipDayFor(String path, StringBuffer buffer){
        ConcurrentHashMap<String, List<VipDayFor>> getVipDayFor = new ConcurrentHashMap<>();
        String[][] result = ReadExelTool.getResult("config/"+path+".xls");
        List<VipDayFor> vipDayFors = new ArrayList<>(result.length);
        for (int i = 1; i < result.length; i++) {
            if (result[i][0].equals("")) {continue;}
            VipDayFor vipDayFor = new VipDayFor();
            for (int j = 0; j < result[i].length; j++) {
                try {
                    SettModelMemberTool.setReflect(vipDayFor, result[i][j], j);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j], MainServerHandler.getErrorMessage(e));
                    return null;
                }
            }
            if (StringUtils.isNotEmpty(vipDayFor.getGoodId()) && vipDayFor.getCount() > 0) {
                vipDayFors.add(vipDayFor);
                getVipDayFor.put("vip", vipDayFors);
            }
        }
        return getVipDayFor;
    }

    public static String createVipDayGoods(ConcurrentHashMap<String, List<VipDayFor>> map) {
        Map<String, Object> m = new HashMap<>();
        m.put("allItemExchange", map);
        String msgString = GsonUtil.getGsonUtil().getgson().toJson(m);
        return msgString;
    }
}
