package org.come.server;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class GolemConfig {

    private ConcurrentHashMap<String,String> configs;
    private ConcurrentHashMap<BigDecimal, BigDecimal> goodsPrices;
    private ConcurrentHashMap<BigDecimal, BigDecimal> petPrices;

    private Point[] stallPoints;
    private Point[] idlePoints;

    public String get(String key) {
        return configs.get(key);
    }

    public String get(String key,String defaultValue) {
        if (configs.get(key) != null) {
            defaultValue = configs.get(key);
        }
        return defaultValue;
    }

    public int getToInt(String key,int defaultValue) {
        if (configs.get(key) != null) {
            try {
                defaultValue = Integer.parseInt(configs.get(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public boolean stallContains(int map, int x, int y) {
        return contains(getStallPoints(), map, x, y);
    }

    public boolean idleContains(int map, int x, int y) {
        return contains(getIdlePoints(), map, x, y);
    }

    public Point getStallRandomPoint() {
        return getRandomPoint(getStallPoints());
    }

    public Point getIdleRandomPoint() {
        return getRandomPoint(getIdlePoints());
    }

    public boolean contains(Point[] points, int map, int x, int y) {
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                if (points[i].contains(map, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Point getRandomPoint(Point[] points) {
        if (points != null) {
            return points[GameServer.random.nextInt(points.length)].generatePointsInRegion();
        }
        return null;
    }

    public Point[] getStallPoints() {
        if (stallPoints == null) {
            String value = configs.get("摆摊坐标");
            if (StringUtils.isNotBlank(value)) {
                String[] vals = value.split("\\|");
                stallPoints = new Point[vals.length];
                for (int i = 0; i < vals.length; i++) {
                    stallPoints[i] = new Point();
                    String[] vs = vals[i].split("=");
                    stallPoints[i].setMapId(Integer.parseInt(vs[0]));
                    for (int j = 1; j < vs.length; j++) {
                        String[] p = vs[j].split(",");
                        stallPoints[i].addPoint(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                    }
                }
            }
        }
        return stallPoints;
    }

    public Point[] getIdlePoints() {
        if (idlePoints == null) {
            String value = configs.get("闲置坐标");
            if (StringUtils.isNotBlank(value)) {
                String[] vals = value.split("\\|");
                idlePoints = new Point[vals.length];
                for (int i = 0; i < vals.length; i++) {
                    idlePoints[i] = new Point();
                    String[] vs = vals[i].split("=");
                    idlePoints[i].setMapId(Integer.parseInt(vs[0]));
                    for (int j = 1; j < vs.length; j++) {
                        String[] p = vs[j].split(",");
                        idlePoints[i].addPoint(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                    }
                }
            }
        }
        return idlePoints;
    }

    public BigDecimal getGoodsPrice(BigDecimal goodsId) {
        return goodsPrices.get(goodsId);
    }

    public BigDecimal getPetPrice(BigDecimal goodsId) {
        return goodsPrices.get(goodsId);
    }


    public void setConfigs(ConcurrentHashMap<String, String> configs) {
        this.configs = configs;
    }

    public void setGoodsPrices(ConcurrentHashMap<BigDecimal, BigDecimal> goodsPrices) {
        this.goodsPrices = goodsPrices;
    }

    public void setPetPrices(ConcurrentHashMap<BigDecimal, BigDecimal> petPrices) {
        this.petPrices = petPrices;
    }
}
