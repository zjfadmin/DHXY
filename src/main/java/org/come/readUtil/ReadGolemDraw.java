package org.come.readUtil;

import org.come.handler.MainServerHandler;
import org.come.model.GolemDraw;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReadGolemDraw {
    public static ConcurrentHashMap<String, List<GolemDraw>> getGolemDraw(String path, StringBuffer buffer) {
        ConcurrentHashMap<String, List<GolemDraw>> allGolemDraw = new ConcurrentHashMap();
        String[][] result = ReadExelTool.getResult("config/"+path+".xls");

        for (int i = 1; i < result.length; i++) {
            if (result[i][0].equals("")) {continue;}
            List<GolemDraw> golemTasks = allGolemDraw.get(result[i][0]);
            if (golemTasks == null) {
                golemTasks = new ArrayList<>();
                allGolemDraw.put(result[i][0], golemTasks);
            }
            GolemDraw golemDraw = new GolemDraw();
            for (int j = 1; j < result[i].length; j++) {
                try {
                    SettModelMemberTool.setReflect(golemDraw, result[i][j], j - 1);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j], MainServerHandler.getErrorMessage(e));
                    return null;
                }
            }
            golemTasks.add(golemDraw);
        }
        return allGolemDraw;
    }
}
