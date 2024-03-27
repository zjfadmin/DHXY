package come.tool.FightingSpellAction;

import come.tool.FightingData.*;
import come.tool.FightingDataAction.DataAction;

import java.util.ArrayList;
import java.util.List;

public class BB_ZYJL implements DataAction {
    public void analysisAction(ManData manData, FightingEvents fightingEvents, String type, Battlefield battlefield){

        FightingEvents gjEvents=new FightingEvents();
        List<FightingState> zls=new ArrayList<>();
        ChangeFighting hf=new ChangeFighting();
        hf.setChangetype("清除异常状态");
        FightingState ace=new FightingState();
        manData.ChangeData(hf, ace);
        ace.setStartState(TypeUtil.JN);
        zls.add(ace);
        gjEvents.setAccepterlist(zls);
        gjEvents.getAccepterlist().get(0).setSkillskin("1243");
        battlefield.NewEvents.add(gjEvents);



    }
}
