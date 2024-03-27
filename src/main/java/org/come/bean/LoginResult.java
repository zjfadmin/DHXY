package org.come.bean;

import come.tool.Calculation.BaseMeridians;
import come.tool.Calculation.BaseQl;
import come.tool.Calculation.BaseXingpans;
import come.tool.FightingData.Sepcies_MixDeal;
import come.tool.Role.PrivateData;
import come.tool.Role.RoleData;
import come.tool.Role.RoleShow;
import come.tool.Scene.LaborDay.LaborRole;
import come.tool.newTeam.TeamRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.come.model.Alchemy;
import org.come.model.Skill;
import org.come.protocol.ParamTool;
import org.come.server.GameServer;
import org.come.tool.JmSum;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登入返回角色实体类
 *
 * @author 叶豪芳
 * @date : 2017年11月23日 下午2:58:19
 */
public class LoginResult {

    private Integer ttVictory;
    private Integer ttFail;

    private String TTJIANGLI;
    private BigDecimal ttRecord;

    private String QIANDAOCHOUJIANG = null;
    private Integer fmsld;//法门选定
    private Integer fmsld2;
    private Integer fmsld3;
    private Integer dangqianFm;


    private LinkedHashMap<Integer, BaseXingpans> xingpansMap;
    private int subDouble;
    private String xingpans;
    //签到天数
    private String QIANDAO = null;
    // 帮派名称
    private String gangname;
    // 角色ID
    private BigDecimal role_id;
    // 靓号ID
    private BigDecimal liang_id;
    // 种族名称
    private String race_name;
    // 用户ID
    private BigDecimal user_id;
    // 种类ID
    private BigDecimal species_id;
    // 召唤兽ID
    private BigDecimal summoning_id;
    // 参战伙伴id
    private String pals;
    // 角色表里的帮派ID
    private BigDecimal gang_id;
    // 坐骑ID
    private Integer mount_id;
    //飞行器ID
    private BigDecimal fly_id;
    // 队伍ID
    private BigDecimal troop_id;
    // 种族ID
    private BigDecimal race_id;
    // 摆摊ID
    private BigDecimal booth_id;
    //任务id 现在是徽章id
    private Integer skill_id;
    // 血量
    private BigDecimal hp;
    // 扩充背包数量
    private int attachPack;
    // 蓝量
    private BigDecimal mp;
    // 金币
    private BigDecimal gold;
    // 绑玉
    private BigDecimal savegold;
    // 点卡
    private BigDecimal codecard;
    // 经验
    private BigDecimal experience;
    // 等级
    private Integer grade;
    // 声望
    private BigDecimal prestige;
    // 战绩
    private BigDecimal pkrecord;
    // 角色名字
    private String rolename;
    // 角色称谓
    private String title;
    // 角色本名
    private String localname;
    // 用户名
    private String userName;
    // 密码
    private String userPwd;
    // 状态
    private Integer activity;
    // 性别
    private String sex;
    // 所在坐标
    private Long x;
    private Long y;
    // 所在地图
    private Long mapid;
    // 帮派职务
    private String gangpost;
    // 帮派成就
    private BigDecimal achievement;
    // 帮派贡献
    private BigDecimal contribution;
    // 天梯积分

    // 根骨
    private BigDecimal SkillS_Id;
    private Integer bone;
    // 灵性
    private Integer spir;
    // 力量
    private Integer power;
    // 敏捷
    private Integer speed;
    // 定力
    private Integer calm;
    // 使用数量
    private Integer addSum;//属性丹
    // 修为点
    private Integer xiuwei;
    // 已兑换属性点
    private String extraPoint;
    // 判断是否在战斗中(1、战斗中)
    private Integer fighting;
    // 下线时间
    private String uptime;
    // 背包密码
    private String password;
    // 是否有待产宝宝（为空没有  1、有）
    private Integer havebaby;
    // 洞房时间
    private long makeloveTime;
    //结婚的对象
    private String marryObject;
    //宝宝id
    private BigDecimal babyId;
    //宝宝状态
    private String babyState;
    // 累计充值金额
    private Integer money;
    //队伍信息（队长名字|队员一|队员二|.......）
    private String teamInfo;
    //技能集合 技能名字=熟练度|技能名字=熟练度
    private String Skills;
    // 定时物品效果使用
    private String TimingGood;
    // 转生标识字段（0转为0，1转为1以此类推）
    private int TurnAround;
    // 坐牢标志      PK点数=身份标志=做天牢次数=每周坐牢次数
    private String taskDaily;
    private String born;
    //帮派守护主副抗性字段(抗性)  存储形式(主属性=值|副属性=值)
    private String resistance;
    //角色服务器区号
    private String serverMeString;
    //记录完成次数
    private String taskComplete;
    //记录任务数据
    private String taskData;
    //记录使用的双倍时间 剩余精确到秒 6小时 21600秒
    private Integer DBExp;
    //记录积分
    private String Score;
    //记录击杀记录
    private String Kill;
    // 抽奖时间
    private Date drawing;
    //记录帮派积分
    private Integer bangScore;
    //额外皮肤
    private String skin;
    private String flyskin;//新加飞行器皮肤
    private Integer temFlyId;//新加飞行器ID
    private BigDecimal Paysum;// 角色的总充值积分，
    private BigDecimal Daypaysum;// 角色的日累计充值
    private BigDecimal Dayandpayorno;//连续充值标识 1-7
    private int Dayfirstinorno;//是否最新叠加冲值标识 0 表示没 有叠加，1 表示今日已经叠加
    private BigDecimal Daygetorno;// 每日特惠领取与否 1 表示领取 2 表示没有
    private Integer vipDayGet; // 月卡每日领取   1 表示领取  2表示没有
    private String Vipget;// 特权领取规则 1=1|2|3&&2=1|2|3其中1=表示vip特权,2=表示每日充 值领取等级包，1 表示 1 级，2 表示 2 级，以此类推。
    private int lowOrHihtpack;//小资礼包/土豪礼包获取 权限 1 表示小资礼包，2 表示土豪礼包。
    private int consumeActive;//消耗活跃度
    //展示
    private String meridians; //完整经脉系统
    private transient RoleShow roleShow;
    private transient TeamRole teamRole;
    private transient int[] dailys;
    private transient RoleData roleData;
    private transient LaborRole laborRole;
    private BigDecimal tiantipkrecord;
    private Integer tiantiyisheng;
    private Integer tiantisansheng;
    private Integer tiantilingqu;
    private transient Integer denglubiaoji;
    private LinkedHashMap<Integer, BaseMeridians> meridiansMap;
    static BigDecimal MAX = new BigDecimal("99999999999");
    static BigDecimal MIN = new BigDecimal(0);
    // 战绩
    private int hjmax;
    // 剩余点卡数量
    private int dianka;
    //存放1-12经脉
    private String fmSkills;
    private String selectfm;

    private String qianQian;//ED=1,10&1,7
    private Long loginTime;
    private Long onlineTime;
    //锁定首发
    private Boolean petIndex;
    private LoginQD loginQD = new LoginQD();
    private Map<String, String> randWord = new HashMap<>();

    private Integer currentattribute;//当前属性编号 //属性切换

    private String MD5;

    private boolean isGolem;
    private Integer golemLvl;

    public Map<String, String> getRandWord() {
        return randWord;
    }

    public void setRandWord(Map<String, String> randWord) {
        this.randWord = randWord;
    }


    public BigDecimal getTiantipkrecord() {
        return tiantipkrecord;
    }

    public void setTiantipkrecord(BigDecimal tiantipkrecord) {
        this.tiantipkrecord = tiantipkrecord;
    }


    public Integer getTiantilingqu() {
        return this.tiantilingqu == null ? Integer.valueOf(0) : this.tiantilingqu;
    }

    public void setTiantilingqu(Integer tiantilingqu) {
        this.tiantilingqu = tiantilingqu;
    }

    public Integer getDenglubiaoji() {
        return this.denglubiaoji == null ? Integer.valueOf(0) : this.denglubiaoji;
    }

    public void setDenglubiaoji(Integer denglubiaoji) {
        this.denglubiaoji = denglubiaoji;
    }

    public Integer getTiantiyisheng() {
        return this.tiantiyisheng == null ? Integer.valueOf(0) : this.tiantiyisheng;
    }

    public void setTiantiyisheng(Integer tiantiyisheng) {
        this.tiantiyisheng = tiantiyisheng;
    }

    public Integer getTiantisansheng() {
        return this.tiantisansheng == null ? Integer.valueOf(0) : this.tiantisansheng;
    }

    public void setTiantisansheng(Integer tiantisansheng) {
        this.tiantisansheng = tiantisansheng;
    }

    public String getQianQian() {
        return qianQian;
    }

    public LoginQD getQianQianObject() {
        if (loginQD == null) {
            loginQD = new LoginQD();
        }
        loginQD.init();
        return loginQD;
    }

    public void setQianQian(String qianQian) {
        this.qianQian = qianQian;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }


    private int maxDoubleTime;
    private int maxLvl;
    private int maxfLvl;
    private int maxf3Lvl;
    private int maxf6Lvl;
    private int max9Lvl;
    private int petMax;
    private int canPetMax;
    private int petNeidan;
    private int petLonggu;
    private int petFeidan;
    private int petLongyan;
    private int petLianyao;
    private String initlvl;

    public LoginResult() {
//		petMax = 17;
//		maxLvl = RoleLevelMaxServlet.get_role_max_level();
//		max9Lvl = kangxinshangxian.get_kangxingshangxian();
//
//		maxfLvl = fashumubiao.get_fsmb_counterfeit();
//		maxf3Lvl = fashumubiao.get_wu_counterfeit();
//		maxf6Lvl = fashumubiao.get_fa_counterfeit();
//		initlvl = PlayerService.getBirthLevel();
//		canPetMax = PetService.getPetCount();
//		maxDoubleTime = GameService.getDoubleTime();
//		petNeidan = PetService.getNeidanCount();
//		petLonggu = PetService.getLongguCount();
//		petFeidan = PetService.getFeidanCount();
//		petLongyan = PetService.getLongyan();
//		petLianyao = PetService.getLianyao();
    }

    public int[] getDaily() {
        if (dailys == null) {
            dailys = new int[4];
            try {
                String[] v = taskDaily.split("\\|");
                dailys[0] = Integer.parseInt(v[0]);
                dailys[0] = Integer.parseInt(v[1]);
                dailys[0] = Integer.parseInt(v[2]);
                dailys[0] = Integer.parseInt(v[3]);
            } catch (Exception e) {

            }
        }
        return dailys;
    }

    public String upDaily(int[] dailys) {
        if (dailys.length != 4) {
            return "0|0|0|0";
        }
        this.dailys = dailys;
        this.taskDaily = dailys[0] + "|" + dailys[1] + "|" + dailys[2] + "|" + dailys[3];
        return taskDaily;
    }

    /**
     * 获取人物战斗皮肤
     */
    public String getBattleSkin(long weapon, int newGW) {
        StringBuffer buffer = new StringBuffer();
        boolean is = true;
        if (skin != null && !skin.equals("")) {
            String[] vs = skin.split("\\|");
            for (int i = 0; i < vs.length; i++) {
                if (vs[i].startsWith("X") || vs[i].startsWith("P")) {
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    String[] ts = vs[i].substring(1).split("_");
                    buffer.append("tx/tx");
                    buffer.append(ts[0]);
                    buffer.append("_");
                    buffer.append(ts[1]);
                } else if (vs[i].startsWith("G")) {
                    is = false;
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    buffer.append(vs[i].substring(1));
                    buffer.append("_2_7");
                } else if (vs[i].startsWith("S")) {
                    is = false;
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    buffer.append(vs[i].substring(1));
                    buffer.append("_1_7");
                } else if (vs[i].startsWith("B")) {
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    String cb = vs[i].substring(1);
                    buffer.append("tx/");
                    buffer.append(cb);
                    buffer.append("0_-5&tx/");
                    buffer.append(cb);
                    buffer.append("1_5");
                }
            }
        }
        if (is) { // 新光武素材id= species_id + "00" + weapon
            if (buffer.length() != 0) {
                buffer.append("&");
            }
            if (newGW != 0) {
                Integer roleWeapon = Sepcies_MixDeal.getRoleWeapon(newGW);
                if (roleWeapon != null) {
                    if (roleWeapon == 0) {
                        weapon = newGW % 100;
                        buffer.append((weapon << 32) | species_id.longValue());
                        buffer.append("_1_7");
                        buffer.append("&");
                        buffer.append("G");
                        buffer.append(newGW);
                        buffer.append("_2_7");
                        is = false;
                    } else if (roleWeapon == 1){
                        buffer.append(newGW);
                        buffer.append("_1_7");
                        is = false;
                    }
                }
            }
            if (is) {
                if (weapon != 0) {
                    buffer.append((weapon << 32) | species_id.longValue());
                    buffer.append("_1_7");
                } else {
                    buffer.append(species_id);
                    buffer.append("_1_7");
                }
            }
        }
        if (getSkill_id() != null) {
            buffer.append("&W");
            buffer.append(getSkill_id());
        }
        return buffer.toString();
    }
    public String getBattleSkin(long weapon) {
        StringBuffer buffer = new StringBuffer();
        boolean is = true;
        if (skin != null && !skin.equals("")) {
            String[] vs = skin.split("\\|");
            for (int i = 0; i < vs.length; i++) {
                if (vs[i].startsWith("X") || vs[i].startsWith("P")) {
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    String[] ts = vs[i].substring(1).split("_");
                    buffer.append("tx/tx");
                    buffer.append(ts[0]);
                    buffer.append("_");
                    buffer.append(ts[1]);
                } else if (vs[i].startsWith("G")) {
                    is = false;
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    buffer.append(vs[i].substring(1));
                    buffer.append("_2_7");
                } else if (vs[i].startsWith("S")) {
                    is = false;
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    buffer.append(vs[i].substring(1));
                    buffer.append("_1_7");
                } else if (vs[i].startsWith("B")) {
                    if (buffer.length() != 0) {
                        buffer.append("&");
                    }
                    String cb = vs[i].substring(1);
                    buffer.append("tx/");
                    buffer.append(cb);
                    buffer.append("0_-5&tx/");
                    buffer.append(cb);
                    buffer.append("1_5");
                }
            }
        }
        if (is) {
            if (buffer.length() != 0) {
                buffer.append("&");
            }
            if (weapon != 0) { //战斗中角色切换处理
                buffer.append((weapon << 32) | species_id.longValue());
            } else {
                buffer.append(species_id);
            }
            buffer.append("_1_7");
        }
        if (getSkill_id() != null) {
            buffer.append("&W");
            buffer.append(getSkill_id());
        }
        return buffer.toString();
    }

    /**
     * 赋值
     */
    public void transfer(LoginResult login) {

        this.species_id = login.species_id;
        this.summoning_id = login.summoning_id;
        this.mount_id = login.mount_id;
        this.race_id = login.race_id;
        this.race_name = login.race_name;
        this.localname = login.localname;
        this.sex = login.sex;
        this.skill_id = login.skill_id;
        this.title = login.title;
        this.x = login.x;
        this.y = login.y;
        this.mapid = login.mapid;
        this.achievement = login.achievement;
        this.contribution = login.contribution;

        this.troop_id = login.troop_id;
        this.fighting = login.fighting;

        this.savegold = login.savegold;
        this.password = login.password;

        this.hp = login.hp;
        this.mp = login.mp;
        this.bone = login.bone;
        this.spir = login.spir;
        this.power = login.power;
        this.speed = login.speed;
        this.calm = login.calm;
        this.xiuwei = login.xiuwei;
        this.extraPoint = login.extraPoint;


        if (this.getGold().compareTo(login.getGold()) > 0) {
            this.gold = login.gold;
        }
        this.experience = login.experience;
        this.grade = login.grade;
        this.TurnAround = login.TurnAround;

        this.havebaby = login.havebaby;
        this.makeloveTime = login.makeloveTime;
        this.marryObject = login.marryObject;
        this.babyId = login.babyId;
        this.babyState = login.babyState;
        this.teamInfo = login.teamInfo;
        this.taskDaily = login.taskDaily;
        this.resistance = login.resistance;
        this.drawing = login.drawing;
        this.skin = login.skin;
        this.meridians = login.meridians;
        this.xingpans = login.xingpans;
        this.hjmax = login.hjmax;
        this.species_id = login.species_id;
        this.summoning_id = login.summoning_id;
        this.mount_id = login.mount_id;
        this.race_id = login.race_id;
        this.race_name = login.race_name;
        this.localname = login.localname;
        this.sex = login.sex;
        this.skill_id = login.skill_id;
        this.title = login.title;
        this.x = login.x;
        this.y = login.y;
        this.mapid = login.mapid;
        this.achievement = login.achievement;
        this.contribution = login.contribution;

        this.troop_id = login.troop_id;
        this.fighting = login.fighting;

        this.savegold = login.savegold;
        this.password = login.password;

        this.hp = login.hp;
        this.mp = login.mp;
        this.bone = login.bone;
        this.spir = login.spir;
        this.power = login.power;
        this.speed = login.speed;
        this.calm = login.calm;
        this.xiuwei = login.xiuwei;
        this.extraPoint = login.extraPoint;
        this.flyskin=login.flyskin;

        if (this.getGold().compareTo(login.getGold()) > 0) {
            this.gold = login.gold;
        }
        this.experience = login.experience;
        this.grade = login.grade;
        this.TurnAround = login.TurnAround;

        this.havebaby = login.havebaby;
        this.makeloveTime = login.makeloveTime;
        this.marryObject = login.marryObject;
        this.babyId = login.babyId;
        this.SkillS_Id = login.SkillS_Id;
        this.babyState = login.babyState;
        this.teamInfo = login.teamInfo;
        this.taskDaily = login.taskDaily;
        this.resistance = login.resistance;
        this.drawing = login.drawing;
        this.skin = login.skin;
        this.hjmax = login.hjmax;
        this.meridians = login.meridians;
        this.xingpans = login.xingpans;


    }


    public double getKilltype(String type) {
        if (Kill == null || Kill.equals("")) return 0;
        String[] v = Kill.split("\\|");
        for (int i = 0; i < v.length; i++) {
            String[] v2 = v[i].split("=");
            if (v2[0].equals(type))
                return Double.parseDouble(v2[1]);
        }
        return 0;
    }

    public int getJQId(String type) {
        if (taskComplete == null || taskComplete.equals("")) return 0;
        String[] v = taskComplete.split("\\|");
        for (int i = 0; i < v.length; i++) {
            if (v[i].startsWith(type)) {
                return Integer.parseInt(v[i].substring(1));
            }
        }
        return 0;
    }


    public Integer getBangScore() {
        return bangScore;
    }

    public void setBangScore(Integer bangScore) {
        this.bangScore = bangScore;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getKill() {
        return Kill;
    }

    public void setKill(String kill) {
        Kill = kill;
    }

    public Integer getDBExp() {
        return DBExp;
    }

    public void setDBExp(Integer dBExp) {
        DBExp = dBExp;
    }

    public String getGangname() {
        return gangname;
    }

    public void setGangname(String gangname) {
        this.gangname = gangname;
        if (roleShow != null) {
            roleShow.setGangname(gangname);
        }
    }

    public BigDecimal getRole_id() {
        return role_id;
    }

    public void setRole_id(BigDecimal role_id) {
        this.role_id = role_id;
    }

    public BigDecimal getLiang_id() {
        return liang_id;
    }

    public void setLiang_id(BigDecimal liang_id) {
        this.liang_id = liang_id;
    }

    public String getRace_name() {
        return race_name;
    }

    public void setRace_name(String race_name) {
        this.race_name = race_name;
    }

    public BigDecimal getUser_id() {
        return user_id;
    }

    public void setUser_id(BigDecimal user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(BigDecimal species_id) {
        this.species_id = species_id;
        if (roleShow != null) {
            roleShow.setSpecies_id(species_id);
        }
        if (teamRole != null) {
            teamRole.setSpeciesId(species_id);
        }
    }

    public BigDecimal getSummoning_id() {
        return summoning_id;
    }

    public void setSummoning_id(BigDecimal summoning_id) {
        this.summoning_id = summoning_id;
    }

    public BigDecimal getGang_id() {
        return gang_id;
    }

    public void setGang_id(BigDecimal gang_id) {
        this.gang_id = gang_id;
        if (roleShow != null) {
            roleShow.setGang_id(gang_id);
        }
    }

    public Integer getMount_id() {
        return mount_id;
    }

    public void setMount_id(Integer mount_id) {
        this.mount_id = mount_id;
        if (roleShow != null) {
            roleShow.setMount_id(mount_id);
        }
    }

    public BigDecimal getTroop_id() {
        return troop_id;
    }

    public void setTroop_id(BigDecimal troop_id) {
        this.troop_id = troop_id;
        if (roleShow != null) {
            roleShow.setTroop_id(troop_id);
        }
    }

    public BigDecimal getRace_id() {
        return race_id;
    }

    public void setRace_id(BigDecimal race_id) {
        this.race_id = race_id;
    }

    public BigDecimal getBooth_id() {
        return booth_id;
    }

    public void setBooth_id(BigDecimal booth_id) {
        this.booth_id = booth_id;
        if (roleShow != null) {
            roleShow.setBooth_id(booth_id);
        }
    }

    public BigDecimal getHp() {
        return hp;
    }

    public void setHp(BigDecimal hp) {
        this.hp = hp;
    }

    public BigDecimal getMp() {
        return mp;
    }

    public void setMp(BigDecimal mp) {
        this.mp = mp;
    }

    public BigDecimal getGold() {
        return new BigDecimal(JmSum.MZ(gold.longValue()));
    }


    public void setGold(BigDecimal gold) {
        if (gold.compareTo(MAX) > 0) {
            gold = new BigDecimal("99999999999");
        } else if (gold.compareTo(MIN) < 0) {
            if (userName != null && GameServer.getInlineUserNameMap().get(userName) != null) {
                this.gold = new BigDecimal(JmSum.ZM(gold.longValue()));
                ParamTool.ACTION_MAP.get("accountstop").action(GameServer.getInlineUserNameMap().get(userName), userName);
                return;
            }
        }
        this.gold = new BigDecimal(JmSum.ZM(gold.longValue()));
    }

    public BigDecimal getCodecard() {
        return new BigDecimal(JmSum.MZ(codecard.longValue()));
    }

    public void setCodecard(BigDecimal codecard) {
        // 仙玉小于0时，直接封号
        if (codecard.compareTo(new BigDecimal(0)) < 0) {
            if (userName != null && GameServer.getInlineUserNameMap().get(userName) != null) {
                ParamTool.ACTION_MAP.get("accountstop").action(GameServer.getInlineUserNameMap().get(userName), userName);
                ;
            }
        }
        this.codecard = new BigDecimal(JmSum.ZM(codecard.longValue()));
    }

    public BigDecimal getExperience() {
        return new BigDecimal(JmSum.MZ(experience.longValue()));
    }

    public void setExperience(BigDecimal experience) {
        this.experience = new BigDecimal(JmSum.ZM(experience.longValue()));
    }

    public Integer getGrade() {
        return (int) JmSum.MZ(grade.longValue());
    }

    public void setGrade(Integer grade) {
        this.grade = (int) JmSum.ZM(grade.longValue());
        if (roleShow != null) {
            roleShow.setGrade(grade);
        }
        if (teamRole != null) {
            teamRole.setGrade(grade);
        }
        if (laborRole != null) {
            laborRole.setLvl(grade);
        }
    }

    public BigDecimal getPrestige() {
        return prestige;
    }

    public void setPrestige(BigDecimal prestige) {
        this.prestige = prestige;
    }

    public BigDecimal getPkrecord() {
        return pkrecord;
    }

    public void setPkrecord(BigDecimal pkrecord) {
        this.pkrecord = pkrecord;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
        if (roleShow != null) {
            roleShow.setRolename(rolename);
        }
        if (teamRole != null) {
            teamRole.setName(rolename);
        }
        if (laborRole != null) {
            laborRole.setName(rolename);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (roleShow != null) {
            roleShow.setTitle(title);
        }
    }

    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
        if (roleShow != null) {
            roleShow.setX(x);
        }
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
        if (roleShow != null) {
            roleShow.setY(y);
        }
    }

    public Long getMapid() {
        return mapid;
    }

    public void setMapid(Long mapid) {
        this.mapid = mapid;
        if (roleShow != null) {
            roleShow.setMapid(mapid);
        }
    }

    public String getGangpost() {
        return gangpost;
    }

    public void setGangpost(String gangpost) {
        this.gangpost = gangpost;
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(BigDecimal achievement) {
        this.achievement = achievement;
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    public void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }

    public Integer getBone() {
        return (int) JmSum.MZ(bone.longValue());
//		return bone;
    }

    public void setBone(Integer bone) {
        this.bone = (int) JmSum.ZM(bone.longValue());
//		this.bone = bone;
    }

    public Integer getSpir() {
        return (int) JmSum.MZ(spir.longValue());
//		return spir;
    }

    public void setSpir(Integer spir) {
        this.spir = (int) JmSum.ZM(spir.longValue());
//		this.spir = spir;
    }

    public Integer getPower() {
        return (int) JmSum.MZ(power.longValue());
//		return power;
    }

    public void setPower(Integer power) {
        this.power = (int) JmSum.ZM(power.longValue());
//		this.power = power;
    }

    public Integer getSpeed() {
        return (int) JmSum.MZ(speed.longValue());
//		return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = (int) JmSum.ZM(speed.longValue());
//		this.speed = speed;
    }

    public Integer getFighting() {
        return fighting;
    }

    public void setFighting(Integer fighting) {
        this.fighting = fighting;
        if (roleShow != null) {
            roleShow.setFighting(fighting);
        }
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public BigDecimal getSavegold() {
        return savegold;
    }

    public void setSavegold(BigDecimal savegold) {
        this.savegold = savegold;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getHavebaby() {
        return havebaby;
    }

    public void setHavebaby(Integer havebaby) {
        this.havebaby = havebaby;
    }

    public long getMakeloveTime() {
        return makeloveTime;
    }

    public void setMakeloveTime(long makeloveTime) {
        this.makeloveTime = makeloveTime;
    }

    public String getMarryObject() {
        return marryObject;
    }

    public void setMarryObject(String marryObject) {
        this.marryObject = marryObject;
    }

    public BigDecimal getBabyId() {
        return babyId;
    }

    public void setBabyId(BigDecimal babyId) {
        this.babyId = babyId;
    }

    public String getBabyState() {
        return babyState;
    }

    public void setBabyState(String babyState) {
        this.babyState = babyState;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(String teamInfo) {
        this.teamInfo = teamInfo;
        if (roleShow != null) {
            roleShow.setTeamInfo(teamInfo);
        }
    }

    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }

    public String getTimingGood() {
        return TimingGood;
    }

    public void setTimingGood(String timingGood) {
        TimingGood = timingGood;
    }

    public int getTurnAround() {
        return TurnAround;
    }

    public void setTurnAround(int turnAround) {
        TurnAround = turnAround;
        if (roleShow != null) {
            roleShow.setTurnAround(turnAround);
        }
    }

    public String getTaskDaily() {
        return taskDaily;
    }

    public void setTaskDaily(String taskDaily) {
        this.taskDaily = taskDaily;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getResistance() {
        return resistance;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public String getServerMeString() {
        return serverMeString;
    }

    public void setServerMeString(String serverMeString) {
        this.serverMeString = serverMeString;
    }

    public String getTaskComplete() {
        if (taskComplete == null) {
            taskComplete = "";
        }
        return taskComplete;
    }

    public void setTaskComplete(String taskComplete) {
        this.taskComplete = taskComplete;
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public Date getDrawing() {
        return drawing;
    }

    public void setDrawing(Date drawing) {
        this.drawing = drawing;
    }

    public BigDecimal getScoretype(String type) {
        if (Score == null || Score.equals("")) return new BigDecimal(0);
        String[] v = Score.split("\\|");
        for (int i = 0; i < v.length; i++) {
            String[] v2 = v[i].split("=");
            if (v2[0].equals(type))
                return new BigDecimal(v2[1]);
        }
        return new BigDecimal(0);
    }

    //获取队伍
    public String getTeam() {
        if (teamInfo == null || teamInfo.equals("") || teamInfo.equals("|")) {
            return rolename;
        } else {
            return teamInfo;
        }
    }

    public Integer getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Integer skill_id) {
        this.skill_id = skill_id;
        if (roleShow != null) {
            roleShow.setSkill_id(skill_id);
        }
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
        if (roleShow != null) {
            roleShow.setSkin(skin);
        }
    }

    public Integer getCalm() {
        return (int) JmSum.MZ(calm.longValue());
    }

    public void setCalm(Integer calm) {
        this.calm = (int) JmSum.ZM(calm.longValue());
    }

    public Integer getXiuwei() {
        return (int) JmSum.MZ(xiuwei.longValue());
    }

    public void setXiuwei(Integer xiuwei) {
        this.xiuwei = (int) JmSum.ZM(xiuwei.longValue());
    }

    public String getExtraPoint() {
        return extraPoint;
    }

    public void setExtraPoint(String extraPoint) {
        this.extraPoint = extraPoint;
    }

    public RoleShow getRoleShow() {
        return roleShow;
    }

    public void setRoleShow(RoleShow roleShow) {
        this.roleShow = roleShow;
    }

    public TeamRole getTeamRole() {
        if (teamRole == null) {
            teamRole = new TeamRole(this);
        }
        return teamRole;
    }

    public void initTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
        this.teamRole.upTeamRole(this);
    }

    /**
     * 获取帮派修炼属性
     */
    public String[] getResistance(String type) {
        if (resistance == null || resistance.equals("")) {
            return null;
        }
        String[] vs = resistance.split("\\|");
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].startsWith(type)) {
                String[] v = vs[i].split("#");
                v[0] = v[0].substring(1);
                return v;
            }
        }
        return null;
    }

    public String setResistance(int type, String data) {
        String QZ = type == 1 ? "X" : "D";
        if (resistance == null || resistance.equals("")) {
            if (data == null) {
                return "";
            }
            resistance = QZ + data;
            return resistance;
        }
        String[] vs = resistance.split("\\|");
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].startsWith(QZ)) {
                vs[i] = null;
                break;
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < vs.length; i++) {
            if (vs[i] != null) {
                if (buffer.length() != 0) {
                    buffer.append("|");
                }
                buffer.append(vs[i]);
            }
        }
        if (data != null) {
            if (buffer.length() != 0) {
                buffer.append("|");
            }
            buffer.append(QZ);
            buffer.append(data);
        }
        resistance = buffer.toString();
        return resistance;

    }

    /**
     * F额外分配属性点  T天演策修炼进度   X小成修炼点 D大成修炼点
     */
    public int getExtraPointInt(String type) {
        if (extraPoint == null || extraPoint.equals("")) {
            return 0;
        }
        String[] vs = extraPoint.split("\\|");
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].startsWith(type)) {
                return Integer.parseInt(vs[i].substring(1));
            }
        }
        return 0;
    }

    public int getExtraPointInt() {
        if (extraPoint == null || extraPoint.equals("")) {
            return 0;
        }
        String[] vs = extraPoint.split("\\|");
        int p = 0;
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].startsWith("F")) {
                p += Integer.parseInt(vs[i].substring(1));
            }
        }
        return p;
    }

    public void setExtraPoint(String type, int p) {
        type = type.substring(0, 1);
        if (extraPoint == null || extraPoint.equals("")) {
            this.extraPoint = type + p;
        }
        String[] vs = extraPoint.split("\\|");
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].startsWith(type)) {
                vs[i] = type + (Integer.parseInt(vs[i].substring(1)) + p);
                StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < vs.length; j++) {
                    if (buffer.length() != 0) {
                        buffer.append("|");
                    }
                    buffer.append(vs[j]);
                }
                this.extraPoint = buffer.toString();
                return;
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < vs.length; j++) {
            if (buffer.length() != 0) {
                buffer.append("|");
            }
            buffer.append(vs[j]);
        }
        if (buffer.length() != 0) {
            buffer.append("|");
        }
        buffer.append(type);
        buffer.append(p);
        this.extraPoint = buffer.toString();
    }

    public BigDecimal getPaysum() {
        return Paysum;
    }

    public void setPaysum(BigDecimal paysum) {
        Paysum = paysum;
    }

    public BigDecimal getDaypaysum() {
        return Daypaysum;
    }

    public void setDaypaysum(BigDecimal daypaysum) {
        Daypaysum = daypaysum;
    }

    public BigDecimal getDayandpayorno() {
        return Dayandpayorno;
    }

    public void setDayandpayorno(BigDecimal dayandpayorno) {
        Dayandpayorno = dayandpayorno;
    }

    public BigDecimal getDaygetorno() {
        return Daygetorno;
    }

    public void setDaygetorno(BigDecimal daygetorno) {
        Daygetorno = daygetorno;
    }

    public Integer getVipDayGet() {
        return vipDayGet;
    }

    public void setVipDayGet(Integer vipDayGet) {
        this.vipDayGet = vipDayGet;
    }

    public String getVipget() {
        return Vipget;
    }

    public void setVipget(String vipget) {
        Vipget = vipget;
    }

    //	1=1|2|3&&2=1|2|3其中1=表示vip特权,2=表示每日充 值领取等级包，1 表示 1 级，2 表示 2 级，以此类推
    public void removeVipget(String type) {
        if (Vipget == null || Vipget.equals("")) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        String[] vs = Vipget.split("&&");
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].split("=")[0].equals(type)) {
                vs[i] = null;
            } else {
                if (buffer.length() != 0) {
                    buffer.append("&&");
                }
                buffer.append(vs[i]);
            }
        }
        if (buffer.length() == 0) {
            Vipget = null;
        } else {
            Vipget = buffer.toString();
        }
    }

    public int getLowOrHihtpack() {
        return lowOrHihtpack;
    }

    public void setLowOrHihtpack(int lowOrHihtpack) {
        this.lowOrHihtpack = lowOrHihtpack;
    }

    public int getDayfirstinorno() {
        return Dayfirstinorno;
    }

    public String getPals() {
        return pals;
    }

    public void setPals(String pals) {
        this.pals = pals;
    }

    public void setDayfirstinorno(int dayfirstinorno) {
        Dayfirstinorno = dayfirstinorno;
    }

    public RoleData getRoleData() {
        return roleData;
    }

    public void setRoleData(RoleData roleData) {
        this.roleData = roleData;
    }

    public void setLaborRole(LaborRole laborRole) {
        this.laborRole = laborRole;
    }

    public String getMeridians() {
        return meridians;
    }

    public void setMeridians(String meridians) {
        this.meridians = meridians;
    }


    /**
     * 计算当前经脉属性返回BaseQl提供战斗包计算
     *
     * @return
     */
    public BaseQl[] getBaseQl() {
        if (meridiansMap == null || meridiansMap.size() == 0) {
            return null;
        }
        BaseQl[] base = new BaseQl[meridiansMap.size()];

        for (int i = 0; i < meridiansMap.size(); i++) {
            base[i] = new BaseQl(meridiansMap.get(i + 1).getKey(), meridiansMap.get(i + 1).getKeyValue());
        }
        return base;
    }


    /**
     * 返回某一条经脉的值
     *
     * @param key
     * @return
     */
    public double getMeridiansValue(String key) {
        if (meridiansMap == null) {
            return 0;
        }
        double v = 0;
        for (BaseMeridians meridian : meridiansMap.values()) {
            if (meridian.getKey().equals(key)) {
                v += meridian.getKeyValue();
            }
        }
        return v;
    }


    public int getHjmax() {
        return hjmax;
    }

    public void setHjmax(int hjmax) {
        this.hjmax = hjmax;
    }

    public int getDianka() {
        return dianka;
    }

    public void setDianka(int dianka) {
        this.dianka = dianka;
    }


    private List<Alchemy> randStartList;

    public List<Alchemy> getRandStartList() {
        return randStartList;
    }

    public void setRandStartList(List<Alchemy> randStartList) {
        this.randStartList = randStartList;
    }


    private ConcurrentHashMap<String, List<String>> XianqiTypeValue = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, List<String>> getXianqiTypeValue() {
        return XianqiTypeValue;
    }

    public void setXianqiTypeValue(ConcurrentHashMap<String, List<String>> xianqiTypeValue) {
        XianqiTypeValue = xianqiTypeValue;
    }

    public List<String> getXianqiTypeValue(String js) {

        return XianqiTypeValue.get(js);
    }

    private Integer fixXQ;

    public Integer getFixXQ() {
        return fixXQ;
    }

    public void setFixXQ(Integer fixXQ) {
        this.fixXQ = fixXQ;
    }

    private List<Alchemy> hsfList;

    public List<Alchemy> getHsfList() {
        return hsfList;
    }

    public void setHsfList(List<Alchemy> hsfList) {
        this.hsfList = hsfList;
    }

    private Map<String, List<Alchemy>> lxMap = new HashMap<>();


    private Object score;

    public Map<String, List<Alchemy>> getLxMap() {
        return lxMap;
    }

    public void setLxMap(String key, List<Alchemy> alchemyList) {
        lxMap.put(key, alchemyList);
    }


    public String getFmSkills() {
        return this.fmSkills;
    }

    public void setFmSkills(String fmSkills) {
        this.fmSkills = fmSkills;
    }


    public String getSelectfm() {
        return this.selectfm;
    }

    public void setSelectfm(String selectfm) {
        this.selectfm = selectfm;
    }

    public void updQianQian(String day) {
        //1号处理

        //2 更新
    }

    public LoginQD getLoginQD() {
        return loginQD;
    }

    public void setLoginQD(LoginQD loginQD) {
        this.loginQD = loginQD;
    }

    public void setCurrentattribute(int parseInt) {//属性切换
    }

    public class LoginQD {
        private HashSet<Integer> ed = new HashSet<>();

        private HashSet<Integer> lq = new HashSet<>();

        private void init() {
            if (StringUtils.isEmpty(qianQian)) {
                return;
            }
            String[] split = qianQian.split("&");
            String[] eds = split[0].split("=");

            if (eds.length > 1) {
                ed = new HashSet<>();
                for (String s : eds[1].split(",")) {
                    ed.add(Integer.valueOf(s));
                }
            }

            String[] lqs = split[1].split("=");
            if (lqs.length > 1) {
                lq = new HashSet<>();
                for (String s : lqs[1].split(",")) {
                    lq.add(Integer.valueOf(s));
                }
            }
        }

        private String end() {
            if (CollectionUtils.isEmpty(ed) && CollectionUtils.isEmpty(lq)) {
                return null;
            }
            StringBuffer os = new StringBuffer("ED=");
            if (CollectionUtils.isNotEmpty(ed)) {
                ed.forEach(e -> {
                    os.append(e + ",");
                });
            }
            os.append("&LQ=");
            if (CollectionUtils.isNotEmpty(lq)) {
                lq.forEach(e -> {
                    os.append(e + ",");
                });
            }
            return os.toString().substring(0, os.length() - 1);
        }

        public HashSet<Integer> getEd() {
            return ed;
        }

        public HashSet<Integer> getLq() {
            return lq;
        }

        public void setEd(HashSet<Integer> ed) {
            this.ed = ed;
        }

        public void setLq(HashSet<Integer> lq) {
            this.lq = lq;
        }
    }

    public void saveQiandao() {
        qianQian = loginQD.end();
    }

    public String getQIANDAOCHOUJIANG() {
        return QIANDAOCHOUJIANG;
    }

    public void setQIANDAOCHOUJIANG(String qIANDAOCHOUJIANG) {
        QIANDAOCHOUJIANG = qIANDAOCHOUJIANG;
    }

    public String getQIANDAO() {
        return QIANDAO;
    }

    public void setQIANDAO(String qIANDAO) {
        QIANDAO = qIANDAO;
    }

    public int getAttachPack() {
        return attachPack;
    }

    public void setAttachPack(int attachPack) {
        this.attachPack = attachPack;
    }

    public String getXingpans() {
        return xingpans;
    }

    public void setXingpans(String xingpans) {
        this.xingpans = xingpans;
        upXingpans(xingpans);
    }

    public void setXingpans(int num, BaseXingpans xingpans) {
        xingpansMap.put(num, xingpans);
        StringBuilder temp = new StringBuilder();
        for (BaseXingpans xingpan : xingpansMap.values()) {
            if (temp.length() > 0) {
                temp.append("|");
            }
            temp.append(xingpan.toString());
        }
        this.xingpans = temp.toString();
    }

    public BaseXingpans getXingpans(int num) {
        if (xingpansMap == null) {
            xingpansMap = new LinkedHashMap<>();
        }
        return xingpansMap.get(num);
    }

    public void upXingpans(String xingpans) {
        if (xingpansMap == null) {
            xingpansMap = new LinkedHashMap<>();
        }
        if (xingpans == null || xingpans.equals("")) {
            return;
        }
        BaseXingpans baseXingpansR = null;
        String[] vs = xingpans.split("\\|");
        for (int i = 0; i < vs.length; i++) {
            String[] vss = vs[i].split("_");
            if (vss.length != 7) {
                continue;
            }
            int bh = Integer.parseInt(vss[0]);
            baseXingpansR = new BaseXingpans(bh,
                    vss[1], Integer.parseInt(vss[2]),
                    vss[3], Double.parseDouble(vss[4]),
                    vss[5], Double.parseDouble(vss[6]));
            xingpansMap.put(bh, baseXingpansR);
        }
    }


    /**
     * 计算当前星盘属性返回BaseQl提供战斗包计算 星盘
     *
     * @return
     */
    public BaseQl[] getBaseQl1() {
        if (xingpans == null) {
            return null;
        }


        String[] vs = xingpans.split("\\|");
        BaseQl[] base = new BaseQl[(vs.length * 2)];
        for (int i = 0; i < base.length; i++) {
            if (i < vs.length) {
                String[] vss = vs[i].split("_");
                int bh = Integer.parseInt(vss[0]);
                base[i] = new BaseQl(xingpansMap.get(bh).getKey1(), xingpansMap.get(bh).getKeyValue1());
            } else {
                String[] vss = vs[(i - vs.length)].split("_");
                int bh = Integer.parseInt(vss[0]);
                base[i] = new BaseQl(xingpansMap.get(bh).getKey(), xingpansMap.get(bh).getKeyValue());
            }

        }
        return base;
    }


    /**
     * 返回某一条星盘的值
     *
     * @param key
     * @return
     */
    public double getXingpansValue(String key) {
        if (xingpansMap == null) {
            return 0;
        }
        double v = 0;
        for (BaseXingpans xingpan : xingpansMap.values()) {
            if (xingpan.getKey().equals(key)) {
                v += xingpan.getKeyValue();
            }
        }
        return v;
    }


    public BigDecimal getSkillS_Id() {
        return SkillS_Id;
    }

    public void setSkillS_Id(BigDecimal s) {
        this.SkillS_Id = SkillS_Id;
    }


    public int getMaxDoubleTime() {
        return maxDoubleTime;
    }

    public void setMaxDoubleTime(int maxDoubleTime) {
        this.maxDoubleTime = maxDoubleTime;
    }


    public int getPetLonggu() {
        return petLonggu;
    }

    public void setPetLonggu(int petLonggu) {
        this.petLonggu = petLonggu;
    }


    public int getPetNeidan() {
        return petNeidan;
    }

    public void setPetNeidan(int petNeidan) {
        this.petNeidan = petNeidan;
    }

    public int getPetFeidan() {
        return petFeidan;
    }

    public void setPetFeidan(int petFeidan) {
        this.petFeidan = petFeidan;
    }

    public int getPetLongyan() {
        return petLongyan;
    }

    public void setPetLongyan(int petLongyan) {
        this.petLongyan = petLongyan;
    }

    public int getPetLianyao() {
        return petLianyao;
    }

    public void setPetLianyao(int petLianyao) {
        this.petLianyao = petLianyao;
    }


    public void setInitlvl(String initlvl) {
        this.initlvl = initlvl;
    }

    public int getPetMax() {
        return petMax;
    }

    public void setPetMax(int petMax) {
        this.petMax = petMax;
    }

    public int getSubDouble() {
        return this.subDouble;
    }

    public void setSubDouble(int subDouble) {
        this.subDouble = subDouble;
    }


    public int getCanPetMax() {
        return canPetMax;
    }

    public void setCanPetMax(int canPetMax) {
        this.canPetMax = canPetMax;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }


    public int getMax9Lvl() {
        return max9Lvl;
    }

    public void setMax9Lvl(int max9Lvl) {
        this.max9Lvl = max9Lvl;
    }


    public int getMaxfLvl() {
        return maxfLvl;
    }

    public void setMaxfLvl(int maxfLvl) {
        this.maxfLvl = maxfLvl;
    }


    public int getMaxf3Lvl() {
        return maxf3Lvl;
    }

    public void setMaxf3Lvl(int maxf3Lvl) {
        this.maxf3Lvl = maxf3Lvl;
    }


    public int getMaxf6Lvl() {
        return maxf6Lvl;
    }

    public void setMaxf6Lvl(int maxf6Lvl) {
        this.maxf6Lvl = maxf6Lvl;
    }


    public String getInitlvl() {
        return initlvl;
    }

    // 用户名
    public Integer getFmsld() {
        return fmsld;
    }

    public void setFmsld(Integer fmsld) {
        this.fmsld = fmsld;
    }

    public Integer getTtVictory() {
        return ttVictory;
    }

    public void setTtVictory(Integer ttVictory) {
        this.ttVictory = ttVictory;
    }

    public Integer getTtFail() {
        return ttFail;
    }

    public void setTtFail(Integer ttFail) {
        this.ttFail = ttFail;
    }

    public String getTTJIANGLI() {
        return TTJIANGLI;
    }

    public void setTTJIANGLI(String TTJIANGLI) {
        this.TTJIANGLI = TTJIANGLI;
    }

    public BigDecimal getTtRecord() {
        return ttRecord;
    }

    public void setTtRecord(BigDecimal ttRecord) {
        this.ttRecord = ttRecord;
    }

    public int getConsumeActive() {
        return consumeActive;
    }

    public void setConsumeActive(int consumeActive) {
        this.consumeActive = consumeActive;
    }

    public Integer getFmsld2() {
        return fmsld2;
    }

    public void setFmsld2(Integer fmsld2) {
        this.fmsld2 = fmsld2;
    }

    public Integer getFmsld3() {
        return fmsld3;
    }

    public void setFmsld3(Integer fmsld3) {
        this.fmsld3 = fmsld3;
    }

    public Integer getDangqianFm() {
        if (dangqianFm == null) {
            PrivateData data= getRoleData().getPrivateData();
            String[] vs=data.getSkill("F");
            if (vs!=null) {
                for (int i = 0; i < vs.length; i++) {
                    Skill skill =  GameServer.getSkill(vs[i].split("_")[0]);
                    if (skill != null) {
                        if (race_id.compareTo(BigDecimal.valueOf(10001)) == 0) {
                            if (skill.getSkillname().equals("清心静气")) {
                                dangqianFm = 1;break;
                            } else if (sexisMan(sex)&&skill.getSkillname().equals("利刃加身")||!sexisMan(sex)&&skill.getSkillname().equals("神不守舍")) {
                                dangqianFm = 2;break;
                            } else if(sexisMan(sex)&&skill.getSkillname().equals("神不守舍")||!sexisMan(sex)&&skill.getSkillname().equals("神力加身")) {
                                dangqianFm = 3;break;
                            }
                        } else if (race_id.compareTo(new BigDecimal("10002")) == 0){
                            if (sexisMan(sex)&&skill.getSkillname().equals("幻影迷踪")||!sexisMan(sex)&&skill.getSkillname().equals("刚柔兼备")) {
                                dangqianFm = 1;break;
                            } else if (sexisMan(sex)&&skill.getSkillname().equals("力挽狂澜")||!sexisMan(sex)&&skill.getSkillname().equals("势如破竹")) {
                                dangqianFm = 2;break;
                            } else if(skill.getSkillname().equals("兽魂俯首")) {
                                dangqianFm = 3;break;
                            }
                        } else if (race_id.compareTo(new BigDecimal("10003")) == 0) {
                            if (skill.getSkillname().equals("凝神一击")) {
                                dangqianFm = 1;break;
                            } else if (skill.getSkillname().equals("无坚不摧")) {
                                dangqianFm = 2;break;
                            } else if(skill.getSkillname().equals("气吞山河")) {
                                dangqianFm = 3;break;
                            }
                        } else if (race_id.compareTo(new BigDecimal("10004")) == 0) {
                            if (sexisMan(sex)&&skill.getSkillname().equals("法魂护体")||!sexisMan(sex)&&skill.getSkillname().equals("失魂落魄")) {
                                dangqianFm = 1;break;
                            } else if (skill.getSkillname().equals("无坚不摧")) {
                                dangqianFm = 2;break;
                            } else if(sexisMan(sex)&&skill.getSkillname().equals("气聚神凝")||!sexisMan(sex)&&skill.getSkillname().equals("刚柔兼备")) {
                                dangqianFm = 3;break;
                            }
                        } else if (race_id.compareTo(new BigDecimal("10005")) == 0) {
                            if (skill.getSkillname().equals("鱼龙潜跃")) {
                                dangqianFm = 1;break;
                            } else if (skill.getSkillname().equals("神龙摆尾")) {
                                dangqianFm = 2;break;
                            } else if(skill.getSkillname().equals("行气如虹")) {
                                dangqianFm = 3;break;
                            }
                        }
                    }
                }
            }
        }
        return dangqianFm;
    }
    public boolean sexisMan(String sex) {
        return "男".equals(sex);
    }

    public void setDangqianFm(Integer dangqianFm) {
        this.dangqianFm = dangqianFm;
    }

    public Integer getAddSum() {
        if (addSum == null) {
            addSum = 0;
        }
        return addSum;
    }

    public void setAddSum(Integer addSum) {//属性丹结束
        this.addSum = addSum;
    }

    public Boolean getPetIndex() {
        return petIndex;
    }

    public void setPetIndex(Boolean petIndex) {
        this.petIndex = petIndex;
    }

    public Integer getCurrentattribute() {//属性切换
        return currentattribute;
    }

    public void setCurrentattribute(Integer currentattribute) {//属性切换
        this.currentattribute = currentattribute;
    }

    public BigDecimal getFly_id() {
        return fly_id;
    }

    public void setFly_id(BigDecimal fly_id) {
        this.fly_id = fly_id;
    }

    public Integer getTemFlyId() {
        return temFlyId;
    }

    public void setTemFlyId(Integer temFlyId) {
        this.temFlyId = temFlyId;
    }

    public  String getFlyskin(){return  flyskin;}

    public  void setFlyskin(String flyskin){this.flyskin=flyskin;
        if (roleShow!=null)
            roleShow.setFlyskin(flyskin);
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public Integer getGolemLvl() {
        if (golemLvl == null) {
            golemLvl = getGrade();
        }
        return golemLvl;
    }

    public void setGolemLvl(Integer golemLvl) {
        this.golemLvl = golemLvl;
    }

    public boolean isGolem() {
        return isGolem;
    }

    public void setGolem(boolean golem) {
        isGolem = golem;
    }
}
