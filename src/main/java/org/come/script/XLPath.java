package org.come.script;

import org.come.bean.PathPoint;
import org.come.model.Door;
import org.come.model.Gamemap;
import org.come.model.Npctable;
import org.come.server.GameServer;
import org.come.tool.SplitStringTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XLPath {
    private List<XXX> openList;
    private List<Integer> openInts;
    private Map<Integer, XLMap> map;

    public XLPath() {
        openList = new ArrayList<>();
        openInts = new ArrayList<>();
        map = new HashMap<>();
    }

    public static long getMoveTime(List<PathPoint> list, double sp) {
        long time = 0;
        while (list.size() > 1) {
            PathPoint point = list.get(0);
            PathPoint point2 = list.get(1);
            int dx = point2.getX()-point.getX();
            int dy = point2.getY()-point.getY();
            time += getTime(dx, dy, sp);
            list.remove(0);
            break;
        }
        return (long) (time * 1.3);
    }

    public static long getTime(long l, long m, double sp) {
        double move = Math.sqrt(l * l + m * m);
        return (long) (move / sp);
    }

    public XLMap getXLMap(int mapId) {
        XLMap xlMap = map.get(mapId);
        if (xlMap == null) {
            xlMap = new XLMap(mapId);
            map.put(mapId, xlMap);
        }
        return xlMap;
    }

    public boolean move(List<ScriptOpen> list, int mapId, int x, int y, int newMapId, int newX, int newY) {
        openInts.clear();
        openList.clear();
        XXX termPos = null;
        if (mapId != newMapId) {
            openList.add(new XXX(mapId, x, y));
            do {
                XXX currentPos = openList.get(0);
                for (int i = openList.size() - 1; i >= 0; i--) {
                    if (currentPos.G > openList.get(i).G) {
                        currentPos = openList.get(i);
                    }
                }
                openList.remove(currentPos);
                XLMap xlMap = getXLMap(currentPos.map);
                List<XLP> xlps = xlMap.xlpOne;
                if (xlps == null) continue;
                boolean isMap = true;
                for (int i = xlps.size() - 1; i >= 0; i--) {
                    XLP p = xlps.get(i);
                    if (isMap && p.map == newMapId) {
                        isMap = false;
                    }
                    if (currentPos.isDoor(p.door) || currentPos.isMap(p.map) || openInts.contains(p.map)) {
                        continue;
                    }
                    XXX tmpPos = new XXX(p, currentPos);
                    openList.add(tmpPos);
                }
                if (isMap) {
                    openInts.add(currentPos.map);
                }
                for (int j = openList.size() - 1; j >= 0; j--) {
                    if (termPos != null && termPos.G <= openList.get(j).G) {
                        openList.remove(j);
                    } else if (openList.get(j).map == newMapId) {
                        if (termPos == null) {
                            termPos = openList.get(j);
                        } else {
                            termPos = openList.get(j);
                        }
                    }
                }
            } while (openList.size() != 0);
        } else {
            termPos = new XXX(mapId, x, y);
        }
        // 目的不可达
        if (termPos == null) return false;

        ScriptOpen scriptOpen = new ScriptOpen(newX, newY);
        list.add(scriptOpen);

        XXX end = new XXX(newMapId, newX, newY);
        end.fa = termPos;
        while (end.fa != null) {
            if (end.xlp == null) {
                if (end.map == end.fa.map) {
                    XXX xxx = mapShort(end.fa, end);
                    addScript(xxx, list);
                }
            } else if (end.getMap() == end.fa.getMap()) {
                XXX xxx = mapShort(end.fa.recordXXX(), end.recordXXX());
                addScript(xxx, list);
            }
            end = end.fa;
            if (end.xlp != null) {
                if (end.xlp.npc != 0) {
                    scriptOpen = new ScriptOpen(end.xlp.map, end.xlp.x, end.xlp.y);
                    list.add(scriptOpen);
                    scriptOpen = new ScriptOpen(end.xlp.dx, end.xlp.dy);
                    list.add(scriptOpen);
                } else if (end.xlp.door != 0) {
                    scriptOpen = new ScriptOpen(end.xlp.map, end.xlp.x, end.xlp.y);
                    list.add(scriptOpen);
                    scriptOpen = new ScriptOpen(end.xlp.dx, end.xlp.dy);
                    list.add(scriptOpen);
                } else {
                    scriptOpen = new ScriptOpen(end.xlp.dx, end.xlp.dy);
                    list.add(scriptOpen);
                }
            }
        }
        return true;
    }

    private XXX mapShort(XXX one, XXX two) {
        openList.clear();
        XXX termPos = null;
        openList.add(new XXX(one.map, one.x, one.y));
        XLMap xlMap = getXLMap(one.map);
        List<XLP> list = xlMap.xlpTwo;
        do {
            XXX currentPos = openList.get(0);
            for (int i = openList.size() - 1; i >= 0; i--) {
                if (currentPos.G > openList.get(i).G) {
                    currentPos = openList.get(i);
                }
            }
            openList.remove(currentPos);
            if (currentPos.x != two.x || currentPos.y != two.y) {
                if (list != null) {
                    for (int i = list.size() - 1; i >= 0; i--) {
                        XLP p = list.get(i);
                        if (!currentPos.isDoor(p.door)) {
                            XXX tmpPos = new XXX(p, currentPos);
                            tmpPos.G += 10000;
                            openList.add(tmpPos);
                        }
                    }
                }
                openList.add(new XXX(two.map, two.x, two.y, currentPos));
            }
            for (int j = openList.size() - 1; j >= 0; j--) {
                if (termPos != null && termPos.G <= openList.get(j).G) {
                    openList.remove(j);
                } else if (openList.get(j).x == two.x && openList.get(j).y == two.y) {
                    if (termPos == null) {
                        termPos = openList.get(j);
                    } else {
                        termPos = openList.get(j);
                    }
                }
            }
        } while (openList.size() != 0);
        return termPos;
    }

    public void addScript(XXX xxx, List<ScriptOpen> list) {
        if (xxx == null) return;
        ScriptOpen scriptOpen;
        xxx = xxx.fa;
        while (xxx.fa != null) {
            if (xxx.xlp.npc != 0) {
                scriptOpen = new ScriptOpen(xxx.xlp.map, xxx.xlp.x, xxx.xlp.y);
                list.add(scriptOpen);
                scriptOpen = new ScriptOpen(xxx.xlp.dx, xxx.xlp.dy);
                list.add(scriptOpen);
            } else if (xxx.xlp.door != 0) {
                scriptOpen = new ScriptOpen(xxx.xlp.map, xxx.xlp.x, xxx.xlp.y);
                list.add(scriptOpen);
                scriptOpen = new ScriptOpen(xxx.xlp.dx, xxx.xlp.dy);
                list.add(scriptOpen);
            } else {
                scriptOpen = new ScriptOpen(xxx.xlp.dx, xxx.xlp.dy);
                list.add(scriptOpen);
            }
            xxx = xxx.fa;
        }
    }

    /** 两点间的直接距离 **/
    private int calcD(int x, int y, int tx, int ty) {
        x -= tx;
        y -= ty;
        x *= x;
        y *= y;
        return x + y;
    }

    private class XXX {
        private int map, x, y;//触发之后的位置
        private XLP xlp;//触发前的位置
        private int G;//总代价
        private transient XXX fa;

        public XXX(int map, int x, int y) {
            this.map = map;
            this.x = x;
            this.y = y;
        }

        public XXX(XLP xlp, XXX pos) {
            this.map = xlp.map;
            this.x = xlp.x;
            this.y = xlp.y;
            this.xlp = xlp;
            this.fa = pos;
            this.G = pos.G + calcD(pos.x, pos.y, xlp.dx, xlp.dy) + 10000;
        }

        public XXX(int map, int x, int y, XXX pos) {
            this.map = map;
            this.x = x;
            this.y = y;
            this.fa = pos;
            this.G = pos.G + calcD(pos.x, pos.y, x, y);
        }

        public boolean isDoor(int door) {
            if (xlp == null) {
                return false;
            } else if (xlp.door == door) {
                return true;
            } else if (fa != null) {
                return fa.isDoor(door);
            }
            return false;
        }

        public boolean isMap(int map) {
            if (xlp == null) {
                return false;
            } else if (xlp.dmap == map) {
                return true;
            } else if (fa != null) {
                return fa.isMap(map);
            }
            return false;
        }

        public int getMap() {
            return xlp != null ? xlp.dmap : map;
        }

        public XXX recordXXX() {
            if (xlp != null) {
                return new XXX(xlp.dmap, xlp.dx, xlp.dy);
            } else {
                return this;
            }
        }
    }

    private class XLP {
        private int door;
        private int npc;
        private int map, x, y;// 触发之后的位置
        private int dmap, dx, dy;// 触发前的位置

        public XLP(Door door, int dmap) {
            init(door);
            int[] rect = door.getRects();
            this.dmap = dmap;
            this.dx = (rect[0] + rect[1]) / 2;
            this.dy = (rect[2] + rect[3]) / 2;
        }

        public XLP(Door door, Npctable npctable, int dmap) {
            init(door);
            this.dmap = dmap;
            this.dx = Integer.parseInt(npctable.getTx());
            this.dy = Integer.parseInt(npctable.getTy());
            this.npc = Integer.parseInt(npctable.getNpcid());
        }

        public void init(Door door) {
            this.door = Integer.parseInt(door.getDoorid());
            this.map = Integer.parseInt(door.getDoormap());
            String[] point = door.getDoorpoint().split("\\|");
            this.x = Integer.parseInt(point[0]);
            this.y = Integer.parseInt(point[1]);
        }
    }

    private class XLMap {
        private int mapid;
        private List<XLP> xlpOne;//其他地图的传送
        private List<XLP> xlpTwo;//本地图内的传送

        public XLMap(int mapID) {
            this.mapid = mapID;
            Gamemap gamemap = GameServer.getGameMap().get(mapID + "");
            if (gamemap != null) {
                if (gamemap.getMapway() != null && !gamemap.getMapway().equals("")) {
                    List<String> doorIds = SplitStringTool.splitString(gamemap.getMapway());
                    for (int i = 0; i < doorIds.size(); i++) {
                        Door door = GameServer.getDoor(Integer.parseInt(doorIds.get(i)));
                        if (door != null && door.getRects() != null) {
                            addXLP(new XLP(door, mapID));
                        }
                    }
                }
                if (gamemap.getMapnpc() != null && !gamemap.getMapnpc().equals("")) {
                    List<String> npcIds = SplitStringTool.splitString(gamemap.getMapnpc());
                    for (int i = 0; i < npcIds.size(); i++) {
                        Npctable npctable = GameServer.getNpc(npcIds.get(i));
                        if (npctable != null && npctable.getNpctype().equals("2")) {
                            if (npctable.getNpcway() != null && !npctable.getNpcway().equals("")) {
                                List<String> doorIds = SplitStringTool.splitString(npctable.getNpcway());
                                for (int j = 0; j < doorIds.size(); j++) {
                                    Door door = GameServer.getDoor(Integer.parseInt(doorIds.get(j)));
                                    if (door != null) {
                                        addXLP(new XLP(door, npctable, mapID));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void addXLP(XLP xlp) {
            if (xlp.map != mapid) {
                if (xlpOne == null) {
                    xlpOne = new ArrayList<>();
                }
                xlpOne.add(xlp);
            } else {
                if (xlpTwo == null) {
                    xlpTwo = new ArrayList<>();
                }
                xlpTwo.add(xlp);
            }
        }
    }
}
