package org.come.readUtil;

import org.apache.commons.lang.StringUtils;
import org.come.server.GameServer;
import org.come.tool.ReadExelTool;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class ReadGolemConfig {
    public static void getGolemConfig(String path, StringBuffer buffer) {
        ConcurrentHashMap<String, String> configs = new ConcurrentHashMap();

        String[][] result = ReadExelTool.getResult("config/"+path+".xls");
        for (int i = 1; i < result.length; i++) {
            if (StringUtils.isBlank(result[i][0])) {
                continue;
            }
            configs.put(result[i][0],result[i][1]);
        }
        GameServer.getGolemConfig().setConfigs(configs);
    }

    public static void getGolemStall(String path, StringBuffer buffer) {
        ConcurrentHashMap<BigDecimal, BigDecimal> goodsPrices = new ConcurrentHashMap();
        ConcurrentHashMap<BigDecimal, BigDecimal> petPrices = new ConcurrentHashMap();

        String[][] result = ReadExelTool.getResult("config/"+path+".xls");
        for (int i = 1; i < result.length; i++) {
            try {
                switch (result[i][0]) {
                    case "0":
                        goodsPrices.put(new BigDecimal(result[i][1]),new BigDecimal(result[i][2]));
                        break;
                    case "1":
                        goodsPrices.put(new BigDecimal(result[i][1]),new BigDecimal(result[i][2]));
                        break;
                }
            } catch (Exception e) {}
        }
        GameServer.getGolemConfig().setGoodsPrices(goodsPrices);
        GameServer.getGolemConfig().setPetPrices(petPrices);
    }
}
