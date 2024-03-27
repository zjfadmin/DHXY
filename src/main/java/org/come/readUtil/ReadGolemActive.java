package org.come.readUtil;

import org.apache.commons.lang.StringUtils;
import org.come.handler.MainServerHandler;
import org.come.model.GolemActive;
import org.come.model.GolemTask;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReadGolemActive {

    public static List<GolemActive> getGolemActive(String path, StringBuffer buffer) {
        List<GolemActive> golemActives = new ArrayList<>();
        String[][] result = ReadExelTool.getResult("config/"+path+".xls");

        for (int i = 1; i < result.length; i++) {
            if (result[i][0].equals("")) {continue;}
            GolemActive golemActive = new GolemActive();
            for (int j = 0; j < result[i].length; j++) {
                try {
                    SettModelMemberTool.setReflect(golemActive, result[i][j], j);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j], MainServerHandler.getErrorMessage(e));
                    return null;
                }
            }
            int[] lvls = new int[] { 0, 0, 4, 200, 200 };
            if (StringUtils.isNotBlank(golemActive.getLvl())) {
                String[] vals = golemActive.getLvl().split("\\|");
                String[] vs = vals[0].split("\\-");
                lvls[1] = Integer.parseInt(vs[0]);
                lvls[3] = Integer.parseInt(vs[1]);
                if (vals.length >= 2) {
                    lvls[0] = Integer.parseInt(vals[1]);
                }
                if (vals.length == 3) {
                    vs = vals[2].split("\\-");
                    lvls[2] = Integer.parseInt(vs[0]);
                    lvls[4] = Integer.parseInt(vs[1]);
                }
            }
            golemActive.setLvls(lvls);
            golemActive.setLvl(null);
            golemActives.add(golemActive);
        }
        return golemActives;
    }
}
