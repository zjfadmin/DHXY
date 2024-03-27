package org.come.readUtil;

import org.apache.commons.lang.StringUtils;
import org.come.entity.BaiTan;
import org.come.handler.MainServerHandler;
import org.come.model.InitBaiTan;
import org.come.servlet.UpXlsAndTxtFile;
import org.come.tool.ReadExelTool;
import org.come.tool.SettModelMemberTool;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ReadBtUtil {
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, BaiTan>> getAllBt(String path, StringBuffer buffer) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, BaiTan>> getAllBt = new ConcurrentHashMap<>();
        String[][] result = ReadExelTool.getResult("config/" + path + ".xls");
        for (int i = 1; i < Objects.requireNonNull(result).length; i++) {
            if (result[i][0].equals("")) {
                continue;
            }

            InitBaiTan initBaiTan = new InitBaiTan();
            for (int j = 0; j < result[i].length; j++) {
                try {
                    SettModelMemberTool.setReflectRelative(initBaiTan, result[i][j], j);
                } catch (Exception e) {
                    UpXlsAndTxtFile.addStringBufferMessage(buffer, i, j, result[i][j], MainServerHandler.getErrorMessage(e));
                    return null;
                }
            }
            try {
                if (initBaiTan.getId().equals("")) {
                    continue;
                }
                ConcurrentHashMap<Integer, BaiTan> hashMap = getAllBt.get(Integer.parseInt(initBaiTan.getId()));
                if (hashMap == null) {
                    hashMap = new ConcurrentHashMap<>();
                    getAllBt.put(Integer.parseInt(initBaiTan.getId()), hashMap);
                }
                BaiTan baiTan = new BaiTan();
                baiTan.setId(Integer.parseInt(initBaiTan.getId()));
                baiTan.setStallId(Integer.parseInt(initBaiTan.getStallId()));
                baiTan.setStallName(StringUtils.isBlank(baiTan.getStallName()) ?  initBaiTan.getStallName(): baiTan.getStallName());
                baiTan.setStallType(initBaiTan.getStallType());
                baiTan.setGoodsId(new BigDecimal(initBaiTan.getGoodsId()));
                baiTan.setGoodsName(initBaiTan.getGoodsName());
                baiTan.setMoney(Long.parseLong(initBaiTan.getMoney()));
                baiTan.setUseTime(Integer.parseInt(initBaiTan.getUseTime()));
                baiTan.setMapId(Integer.parseInt(initBaiTan.getMapId()));
                baiTan.setMap_x(Integer.parseInt(initBaiTan.getMap_x()));
                baiTan.setMap_y(Integer.parseInt(initBaiTan.getMap_y()));
                baiTan.setCurrency(initBaiTan.getCurrency());
                baiTan.setAuto_sj(initBaiTan.getAuto_sj());
                baiTan.setZhouQi(Integer.parseInt(initBaiTan.getZhouQi()));
                hashMap.put(Integer.parseInt(initBaiTan.getId()), baiTan);

            } catch (Exception e) {
                // TODO: handle exception
                UpXlsAndTxtFile.addStringBufferMessage(buffer, i, 0, "解析错误", MainServerHandler.getErrorMessage(e));
                return null;
            }
        }

        return getAllBt;
    }
}
