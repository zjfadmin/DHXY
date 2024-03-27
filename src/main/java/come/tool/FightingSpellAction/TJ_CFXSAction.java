package come.tool.FightingSpellAction;

import come.tool.FightingData.*;
import org.come.model.Skill;
import org.come.server.GameServer;

import java.util.ArrayList;
import java.util.List;

public class TJ_CFXSAction implements SpellAction{

	@Override
	public void spellAction(ManData manData, FightingSkill skill,FightingEvents events, Battlefield battlefield) {
		//获取技能表格
		Skill skillXls=GameServer.getSkill("1256");
		//增加冷却
		//新增技能 藏锋蓄势
//		if (battlefield.CurrentRound < 10) {return;}
		List<FightingState> Accepterlist=new ArrayList<>();
		List<ManData> datas=MixDeal.getjieshou(events,skill,manData,Accepterlist,battlefield,(int) skillXls.getValue1());
		FightingState Originator=events.getOriginator();
		SummonType.xianzhi(manData, skill);
		Originator.setStartState("法术攻击");
		Originator.setSkillsy(skill.getSkillname());
		String type=skill.getSkilltype();
		for (int j = 0; j < datas.size(); j++) {
			ManData data=datas.get(j);
			if(data.getType() == 0) {
				FightingState Accepter=new FightingState();
				ChangeFighting changeFighting=new ChangeFighting();
				changeFighting.setChangetype("清除异常状态");
				data.ChangeData(changeFighting, Accepter);
				changeFighting.setChangetype("隐身");
				changeFighting.setChangesum(2);
				data.ChangeData(changeFighting, Accepter);
				Originator.setText("藏锋蓄势#2");
				Accepter.setSkillskin("112731");
				FightingPackage.ChangeProcess(changeFighting,manData,data,Accepter,type,Accepterlist,battlefield);
			}
		}
		if (events.getOriginator()!=null) {
			Accepterlist.add(Originator);
		}
		events.setOriginator(null);
		if (Accepterlist.size()!=0) {
			events.setAccepterlist(Accepterlist);
			battlefield.NewEvents.add(events);
		}
	}
}