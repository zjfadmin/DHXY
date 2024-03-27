package come.tool.FightingDataAction;

import java.util.ArrayList;
import java.util.List;

import come.tool.FightingData.AddState;
import come.tool.FightingData.Battlefield;
import come.tool.FightingData.ChangeFighting;
import come.tool.FightingData.FightingEvents;
import come.tool.FightingData.FightingManData;
import come.tool.FightingData.FightingPackage;
import come.tool.FightingData.FightingSkill;
import come.tool.FightingData.FightingState;
import come.tool.FightingData.ManData;
import come.tool.FightingData.MixDeal;
import come.tool.FightingData.TypeUtil;

/**
 * 召唤兽召唤召回
 * @author Administrator
 *
 */
public class PetZhZh implements DataAction{
	@Override
	public void analysisAction(ManData manData, FightingEvents fightingEvents,
							   String type,Battlefield battlefield) {
		// TODO Auto-generated method stub
		if (manData==null) {
			return;
		}
		int camp=manData.getCamp();
		int man=manData.getMan();
		if (type.equals("召回")) {
			zhaohui(manData,battlefield);
		}else {
			//召唤和闪现
			int path=battlefield.Datapathhuo(camp, man);
			if (path!=-1) {
				ManData data=battlefield.fightingdata.get(path);
				zhaohui(data,battlefield);
			}
			battlefield.Addbb(manData, manData.getCamp(),manData.getMan());
			zhaohuan(manData,type,battlefield);
		}
	}
	/**
	 * 创造召唤指令
	 */
	public void zhaohuan(ManData manData,String type,Battlefield battlefield){
		FightingEvents fightingEvents=new FightingEvents();
		FightingState fightingState=new FightingState();

		// 是否触发了卷土重来
		if (manData.jtcl) {
			manData.loadLingXiSkill();
			fightingState.setText("看我的#G卷土重来");
		} else {
			manData.executeYzsg(null,fightingState);
		}

		fightingState.setStartState(type);
		fightingState.setCamp(manData.getCamp());
		fightingState.setMan(manData.getMan());
		manData.setStates(0);
		FightingManData fightingManData=new FightingManData();
		fightingManData.setModel(manData.getModel());
		fightingManData.setManname(manData.getManname());
		fightingManData.setCamp(manData.getCamp());
		fightingManData.setMan(manData.getMan());
		fightingManData.setHp_Current(manData.getHp());
		fightingManData.setHp_Total(manData.getHp_z());
		fightingManData.setMp_Current(manData.getMp());
		fightingManData.setMp_Total(manData.getMp_z());
		fightingManData.setState_1(manData.xz());
		fightingManData.setType(manData.getType());
		fightingManData.setManname(manData.getManname());
		fightingManData.setZs(manData.getZs());
		fightingManData.setMsg(manData.getMsg());
		fightingManData.setYqz(manData.getYqz());
		fightingManData.setNqz(manData.getNqz());
		fightingManData.setStates(manData.ztstlist(fightingManData));
		fightingManData.setId(manData.getId());
		if (manData.getSkillType("隐身")!=null) {
			fightingState.setEndState_1("隐身");
			AddState addState=new AddState();
			addState.setStatename("隐身");
			addState.setSurplus(3);
			manData.getAddStates().add(addState);
			fightingManData.setAlpha(0.3f);
		}

		// 检查存活单位，若对方人多则触发与子同仇判定
		int my = 0;
		int it = 0;
		for (ManData data : battlefield.fightingdata) {
			if (data.getStates() == 0 && data.getType() < 3) {
				if (data.getCamp() == manData.getCamp()) {
					my++;
				} else {
					it++;
				}
			}
		}

		if (my < it) {
			// 灵犀与子同仇判定
			manData.executeYztc(null,fightingState);
		}


		fightingState.setFightingManData(fightingManData);
		fightingEvents.setOriginator(fightingState);
		MixDeal.Approach(manData,fightingState,battlefield);
		battlefield.NewEvents.add(fightingEvents);


		if(Battlefield.isV(manData.executeBfbd(1))) {

			int wei = battlefield.Datapathhuo(manData.getCamp(), manData.getMan() - 5);
			if (wei != -1) {
				ManData master = battlefield.fightingdata.get(wei);

				int kx = manData.executeBfbd(2);

				int i = Battlefield.random.nextInt(12) + 1;

				switch (i) {
					case 1:
						master.getQuality().setRolekwl(master.getQuality().getRolekwl() + kx);//抗物理
						break;
					case 2:
						master.getQuality().setRolekzs(master.getQuality().getRolekzs() + kx);//抗震慑
						break;
					case 3:
						master.getQuality().setRolekff(master.getQuality().getRolekff() + kx);//抗风
						break;
					case 4:
						master.getQuality().setRoleklf(master.getQuality().getRoleklf() + kx);//抗雷
						break;
					case 5:
						master.getQuality().setRoleksf(master.getQuality().getRoleksf() + kx);//坑水
						break;
					case 6:
						master.getQuality().setRolekhf(master.getQuality().getRolekhf() + kx);//坑火
						break;
					case 7:
						master.getQuality().setRolekhl(master.getQuality().getRolekhl() + kx);//抗混乱
						break;
					case 8:
						master.getQuality().setRolekhs(master.getQuality().getRolekhs() + kx);//抗昏睡
						break;
					case 9:
						master.getQuality().setRolekfy(master.getQuality().getRolekfy() + kx);//抗封印
						break;
					case 10:
						master.getQuality().setRolekzd(master.getQuality().getRolekzd() + kx);//抗中毒
						break;
					case 11:
						master.getQuality().setRolekyw(master.getQuality().getRolekyw() + kx);//抗遗忘
						break;
					default:
						master.getQuality().setRolekgh(master.getQuality().getRolekgh() + kx);//抗鬼火
						break;
				}

				List<FightingState> zls = new ArrayList<>();
				FightingState ace = new FightingState();
				ace.setCamp(manData.getCamp());
				ace.setMan(manData.getMan());
				ace.setText("看我的#G八风不动");
				zls.add(ace);

				FightingEvents event=new FightingEvents();
				event.setAccepterlist(zls);
				battlefield.NewEvents.add(event);
			}
		}


		//判断是否有进场的回血技能
//		仙风道骨	进场时回复气血百分比最低友方单位70%气血和10%法力（第一回合除外）
//		妙手仁心	进场时回复法力百分比最低友方单位70%法力和10%气血（第一回合除外）
		FightingSkill skill=manData.getSkillType(TypeUtil.BB_XFDG);
		if (skill!=null) {
			List<ManData> datas=MixDeal.get(false,null, 0, battlefield.nomy(manData.getCamp()), 1, 0, 1, 0, 1, 1, battlefield, 1);
			if (datas.size()==0) {return;}
			ManData data=datas.get(0);
			FightingEvents fe2=new FightingEvents();
			FightingState fs2=new FightingState();
			fs2.setStartState(TypeUtil.JN);
			fs2.setSkillskin(skill.getSkilltype());
			List<FightingState> ac2=new ArrayList<>();
			ChangeFighting fighting=new ChangeFighting();
			fighting.setChangehp((int) (data.getHp_z()*0.7));
			fighting.setChangemp((int) (data.getMp_z()*0.1));
			FightingPackage.ChangeProcess(fighting, null, data, fs2, TypeUtil.JN, ac2, battlefield);
			fe2.setAccepterlist(ac2);
			battlefield.NewEvents.add(fe2);
		}
		skill=manData.getSkillType(TypeUtil.BB_MSRX);
		if (skill!=null) {
			List<ManData> datas=MixDeal.get(false,null, 0, battlefield.nomy(manData.getCamp()), 1, 0, 1, 0, 1, 3, battlefield, 1);
			if (datas.size()==0) {return;}
			ManData data=datas.get(0);
			FightingEvents fe2=new FightingEvents();
			FightingState fs2=new FightingState();
			fs2.setStartState(TypeUtil.JN);
			fs2.setSkillskin(skill.getSkilltype());
			List<FightingState> ac2=new ArrayList<>();
			ChangeFighting fighting=new ChangeFighting();
			fighting.setChangehp((int) (data.getHp_z()*0.1));
			fighting.setChangemp((int) (data.getMp_z()*0.7));
			FightingPackage.ChangeProcess(fighting, null, data, fs2, TypeUtil.JN, ac2, battlefield);
			fe2.setAccepterlist(ac2);
			battlefield.NewEvents.add(fe2);
		}
	}
	/**
	 * 创造召回指令
	 */
	public void zhaohui(ManData manData,Battlefield battlefield){
		// 雪中送炭
		manData.executeXzst(battlefield);
		manData.executeBhnl(battlefield);
		manData.executeYcbb(battlefield);

		FightingEvents fightingEvents=new FightingEvents();
		FightingState fightingState=new FightingState();
		fightingState.setStartState("召回");
		fightingState.setCamp(manData.getCamp());
		fightingState.setMan(manData.getMan());
		manData.setStates(2);
		fightingEvents.setOriginator(fightingState);
		battlefield.NewEvents.add(fightingEvents);
		MixDeal.zhaohui(manData,fightingState,battlefield);
	}
}
