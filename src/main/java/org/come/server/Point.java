package org.come.server;

import java.awt.*;

public class Point extends Polygon {
    private int mapId;
    private int x;
    private int y;

    public Point generatePointsInRegion() {
        int minX = getBounds().x;
        int minY = getBounds().y;
        while (true) {
            int x = GameServer.random.nextInt(getBounds().width + 1) + minX;
            int y = GameServer.random.nextInt(getBounds().height + 1) + minY;
            if (contains(x,y)) {
                this.x = x;
                this.y = y;
                break;
            }
        }
        return this;
    }

    public boolean contains(int mapId,int x,int y) {
        return this.mapId == mapId && contains(x, y);
    }

    public int getMapId() {
        return mapId;
    }
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
}
