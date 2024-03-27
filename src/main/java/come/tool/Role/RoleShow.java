package come.tool.Role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.come.bean.LoginResult;
import org.come.bean.PathPoint;
import org.come.entity.RoleSummoning;

public class RoleShow {
	// 所在坐标
	private Long x;
	private Long y;
	// 所在地图
	private Long mapid;
	private List<PathPoint> Player_Paths;
	
	// 角色ID
	private BigDecimal role_id;
	private BigDecimal liang_id;
    //角色表里的帮派ID
	private BigDecimal gang_id;
	// 帮派名称
	private String gangname;
	// 种类ID
	private BigDecimal species_id;
	// 摆摊ID
	private BigDecimal booth_id;
    //任务id 现在是徽章id
    private Integer skill_id;
    //转生标识字段（0转为0，1转为1以此类推）
	private int TurnAround=0;
	// 等级
	private Integer grade;
	// 角色名字
	private String rolename;
	// 角色称谓
	private String title;	
	//判断是否在战斗中(1、战斗中)
	private Integer fighting;
	// 坐骑ID
	private Integer mount_id;
	
	//队伍ID
	private BigDecimal troop_id;	
	//队伍信息（队长名字|队员一|队员二|.......）
    private String teamInfo;
	//额外皮肤
	private String skin;
	//新加飞行器ID
	private Integer fly_id;
	//新加飞行器皮肤
	private  String flyskin;
	//新加 飞行器速度
	private Double flySpeed= 0.24d;
	//新加飞行器状态 0 = 起飞中 1 = 飞行 2 = 降落
	private Integer flyType = -1;

	private int flyX = 0, flyY = 0;
	// 新加 装备飞行器
	private int equipmentFlyId = 0;

  	public RoleShow() {
		// TODO Auto-generated constructor stub
	}
	public RoleShow(LoginResult loginResult) {
		super();
		if (Player_Paths==null) {
			Player_Paths=new ArrayList<>();
		}else {
			Player_Paths.clear();
		}
		this.x = loginResult.getX();
		this.y = loginResult.getY();
		this.mapid = loginResult.getMapid();
		this.role_id = loginResult.getRole_id();
		this.liang_id = loginResult.getLiang_id();
		this.gang_id = loginResult.getGang_id();
		this.gangname = loginResult.getGangname();
		this.species_id = loginResult.getSpecies_id();
		this.booth_id = loginResult.getBooth_id();
		this.skill_id = loginResult.getSkill_id();
		TurnAround = loginResult.getTurnAround();
		this.grade = loginResult.getGrade();
		this.rolename = loginResult.getRolename();
		this.title    = loginResult.getTitle();
		this.fighting = loginResult.getFighting();
		this.mount_id = loginResult.getMount_id();
		this.troop_id = loginResult.getTroop_id();
		this.teamInfo = loginResult.getTeamInfo();
		this.skin = loginResult.getSkin();
		if(loginResult.getFly_id() != null && loginResult.getFly_id().intValue() != 0)
			this.equipmentFlyId = loginResult.getTemFlyId();
		else
			this.equipmentFlyId = 0;
	}
	public List<PathPoint> getPlayer_Paths() {
		return Player_Paths;
	}
	public void setPlayer_Paths(List<PathPoint> player_Paths) {
		Player_Paths = player_Paths;
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

	public BigDecimal getGang_id() {
		return gang_id;
	}
	public void setGang_id(BigDecimal gang_id) {
		this.gang_id = gang_id;
	}
	public String getGangname() {
		return gangname;
	}
	public void setGangname(String gangname) {
		this.gangname = gangname;
	}
	public BigDecimal getSpecies_id() {
		return species_id;
	}
	public void setSpecies_id(BigDecimal species_id) {
		this.species_id = species_id;
	}
	public BigDecimal getTroop_id() {
		return troop_id;
	}
	public void setTroop_id(BigDecimal troop_id) {
		this.troop_id = troop_id;
	}
	public BigDecimal getBooth_id() {
		return booth_id;
	}
	public void setBooth_id(BigDecimal booth_id) {
		this.booth_id = booth_id;
	}
	public Integer getSkill_id() {
		return skill_id;
	}
	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public int getTurnAround() {
		return TurnAround;
	}
	public void setTurnAround(int turnAround) {
		TurnAround = turnAround;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public Long getX() {
		return x;
	}
	public void setX(Long x) {
		this.x = x;
	}
	public Long getY() {
		return y;
	}
	public void setY(Long y) {
		this.y = y;
	}
	public Long getMapid() {
		return mapid;
	}
	public void setMapid(Long mapid) {
		this.mapid = mapid;
	}
	public String getTeamInfo() {
		return teamInfo;
	}
	public void setTeamInfo(String teamInfo) {
		this.teamInfo = teamInfo;
	}
	public Integer getFighting() {
		return fighting;
	}
	public void setFighting(Integer fighting) {
		this.fighting = fighting;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public Integer getMount_id() {
		return mount_id;
	}
	public void setMount_id(Integer mount_id) {
		this.mount_id = mount_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public  String getFlyskin(){return flyskin;}

	public  void setFlyskin(String flyskin){this.flyskin=flyskin;}


	public Integer getFly_id() {
		return fly_id;
	}

	public void setFly_id(Integer fly_id) {
		this.fly_id = fly_id;
	}

	public Double getFlySpeed() {
		return flySpeed;
	}

	public void setFlySpeed(Double flySpeed) {
		this.flySpeed = flySpeed;
	}

	public Integer getFlyType() {
		return flyType;
	}

	public void setFlyType(Integer flyType) {
		this.flyType = flyType;
	}

	public int getFlyX() {
		return flyX;
	}

	public void setFlyX(int flyX) {
		this.flyX = flyX;
	}

	public int getFlyY() {
		return flyY;
	}

	public void setFlyY(int flyY) {
		this.flyY = flyY;
	}

	public int getEquipmentFlyId() {
		return equipmentFlyId;
	}

	public void setEquipmentFlyId(int equipmentFlyId) {
		this.equipmentFlyId = equipmentFlyId;
	}
}
