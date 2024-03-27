package come.tool.FightingSpellAction;

import come.tool.FightingData.*;
import org.come.tool.CustomFunction;

import java.util.ArrayList;
import java.util.List;

public class BB_BCJF implements SpellAction{
    public void spellAction(ManData manData, FightingSkill skill, FightingEvents events, Battlefield battlefield){

        String skilltype=skill.getSkilltype();
        FightingSkill tz_yg=null;FightingSkill tz_cb=null;
        FightingSkill tz_ph=null;FightingSkill tz_xy=null;FightingSkill ksys=null;
        FightingSkill gqlx=null;  //TODO 灵听技能归去来兮
        FightingSkill bcjf=null;
        FightingSkill yqcs=null;FightingSkill bb_e_ttym=null;FightingSkill bb_e_flbd=null;
        for (int i = manData.getSkills().size()-1; i >=0; i--) {
            FightingSkill skill2=manData.getSkills().get(i);
            String lei=skill2.getSkilltype();
            if (lei.equals(TypeUtil.TZ_YGZG)) {
                tz_yg = skill2;
            } else if (lei.equals(TypeUtil.TZ_CBNX)) {
                tz_cb = skill2;
            } else if (lei.equals(TypeUtil.TZ_PHQY)) {
                tz_ph = skill2;
            } else if (lei.equals(TypeUtil.TZ_XYXG)) {
                tz_xy = skill2;
            } else if (lei.equals(TypeUtil.BB_KSYS)) {
                ksys = skill2;
            } else if (lei.equals(TypeUtil.BB_YQCS)) {
                yqcs = skill2;
            } else if (lei.equals(TypeUtil.BB_E_TTYM)) {
                bb_e_ttym = skill2;
            } else if (lei.equals(TypeUtil.BB_E_FLBD)) {
                bb_e_flbd = skill2;
            }else  if (lei.equals(TypeUtil.BB_GQLX)){
                gqlx=skill2;
            }else  if (lei.equals(TypeUtil.BB_百草竞发)){
                bcjf=skill2;
            }
        }
        int bb_size=0;
        if (bb_e_ttym!=null) {
            if (Battlefield.isV(bb_e_ttym.getSkillhurt())) {bb_size+=2;}
            else if (Battlefield.isV(bb_e_ttym.getSkillgain())) {bb_size+=1;}
        }else if (bb_e_flbd!=null) {
            if (Battlefield.isV(bb_e_flbd.getSkillhurt())) {bb_size+=2;}
            else if (Battlefield.isV(bb_e_flbd.getSkillgain())) {bb_size+=1;}
        }
        List<FightingState> Accepterlist=new ArrayList<>();
        skill.setSkillsum(skill.getSkillsum()+bb_size);
        List<ManData> datas=MixDeal.getjieshou(events, skill, manData,Accepterlist,battlefield);//次数2
        skill.setSkillsum(skill.getSkillsum()-bb_size);
        datas.clear();
        for (int i=0;i<=battlefield.fightingdata.size()-1;i++){
            ManData data11=battlefield.fightingdata.get(i);
            for (int k=0;k<=data11.getAddStates().size()-1;k++){
                if (data11.getAddStates().get(k).getStatename().equals("1241")){
                    datas.add(data11);
                }
            }
        }
        if (datas.size()==0)return;//整个去掉
        if (datas.size()==0){
            FightingState Originator=events.getOriginator();
            if (manData.daijia(skill,Originator,battlefield)) {return;}
            Originator.setStartState("法术攻击");
            Originator.setSkillsy(skill.getSkillname());
            events.setOriginator(null);
            Accepterlist.add(Originator);
            events.setAccepterlist(Accepterlist);
            battlefield.NewEvents.add(events);
            return;
        }

        if (manData.ymjr) {
            FightingState ace = events.getOriginator();
            ace.setCamp(manData.getCamp());
            ace.setMan(manData.getMan());
            ace.setText("看我的#G一鸣惊人");
            Accepterlist.add(ace);
        }

        double dds=1D;
        double jc=manData.getSpellJC();
        double wg=manData.getWGTB();
        if (bb_size!=0&&manData.getShanghai()>=450) {
            if (bb_e_ttym!=null) {
                wg+=(Battlefield.random.nextInt(1000)+400)*40;
            }else if (bb_e_flbd!=null) {
                wg+=manData.getShanghai()*40;
            }
        }
        double kbl=0;
        if (skill.getSkillid()==1049) {
//			9261|雷奔云谲|增加电闪雷鸣伤害（3%*等级）
//			9264|雷厉风行|增加电闪雷鸣狂暴几率（2.4%*等级）
            FightingSkill skill2=manData.skillId(9261);
            if (skill2!=null) {
                jc*=(1+skill2.getSkillhurt()/100D);
            }
            skill2=manData.skillId(9264);
            if (skill2!=null) {
                kbl+=skill2.getSkillhurt();
            }
        }
        List<FightingSkill> skills=ControlAction.getSkills(manData, skill,battlefield.BattleType);

        boolean isTZ= tz_yg != null || tz_cb != null || tz_ph != null || tz_xy != null;
        //判断连击次数
        int sum=1;
        if (!skilltype.equals(TypeUtil.GH)) {//判断是否有仙法连
            if (Battlefield.isV(manData.getQuality().getRolexfljl())) {
                sum+=manData.getQuality().getRolexfljs();
                dds=0.85;
            }
        }


        if (skill.getSkillid() >= 1040 && skill.getSkillid() <= 1065 && manData.executeAbbs(battlefield)) {
            FightingState ace = new FightingState();
            ace.setCamp(manData.getCamp());
            ace.setMan(manData.getMan());
            ace.setText("看我的#G哀兵必败");
            Accepterlist.add(ace);
            sum++;
        }

        long totalHurt=0;
        FightingSkill skill2=null;
        if (PK_MixDeal.isPK(battlefield.BattleType)) {
            if (skill.getSkillid()==1050) {
                skill2=manData.skillId(9271);
                if (skill2!=null&&!Battlefield.isV(skill2.getSkillhurt())) {skill2=null;}
            }else if (skill.getSkillid()==1055) {
                skill2=manData.skillId(9288);
                if (skill2!=null&&!Battlefield.isV(skill2.getSkillhurt())) {skill2=null;}
                AddState addState=manData.xzstate(TypeUtil.TY_S_LPYJ);
                if (addState!=null) {skills=ControlAction.addSkill(manData.skillId(9286), skills);}
            }else if (skill.getSkillid()==1045) {
                skill2=manData.skillId(9308);
                if (skill2!=null&&!Battlefield.isV(skill2.getSkillhurt())) {skill2=null;}
                AddState addState=manData.xzstate(TypeUtil.TY_F_CFWL);
                if (addState!=null) {
                    FightingSkill skill3=manData.skillId(9307);
                    skills=ControlAction.addSkill(skill3, skills);
                    if (skill3!=null) {
                        jc*=skill3.getSkillhurt()/(datas.size()<5?5:datas.size())/100D;
                        skill3.setSkillgain(1);
                        if (Battlefield.isV((skill3.getSkillhurt()-350)/5*2)) {
                            skill3.setSkillgain(2);
                        }
                    }
                }
            }else if (skill.getSkillid()==1060) {
                skill2=manData.skillId(9328);
                if (skill2!=null&&!Battlefield.isV(skill2.getSkillhurt())) {skill2=null;}
            }else if (skill.getSkillid()>=1061&&skill.getSkillid()<=1065) {
                skill2=manData.skillId(9364);
            }
        }else {
            if (skill.getSkillid()==1065) {
                AddState addState=manData.xzstate(TypeUtil.TY_GH_XXYX);
                if (addState!=null) {
                    jc=1-(addState.getStateEffect()*0.05+(addState.getStateEffect()<=2?addState.getStateEffect()*0.05:0.1));
                }
            }
        }
        if (skill.getSkillid()==1055) {
            AddState addState=manData.xzstate(TypeUtil.TY_S_HSCS);
            if (addState!=null) {sum=2;dds=1D;jc*=addState.getStateEffect()/100D;}
        }
        if (datas.size()>1) {
            ManData data=datas.remove(0);
            datas.add(data);
        }
        boolean isyqcs=false;

        // 灵犀-青云直上伤害倍数
        double shbs = 1;
        if (sum > 1 && skill.getSkillid() >= 1040 && skill.getSkillid() <= 1065) {
            shbs += manData.executeQyzs() / 100.0;
        }

        for (int i = 0; i < sum; i++) {
            if (manData.getStates()!=0) {break;}
            if (i!=0) {Accepterlist=new ArrayList<>();}
            FightingState Originator=new FightingState();
            Originator.setEndState(skill.getSkillname());
            if (i==0) {
                if (bb_size!=0) {Originator.setText(bb_e_ttym!=null?"听天由命#2":"法力波动#2");}
                if (!manData.isLicense(skill)) {break;}
                if (manData.daijia(skill,Originator,battlefield)) {return;}
            }else {
                Originator.setCamp(manData.getCamp());
                Originator.setMan(manData.getMan());
            }

            // 群体法术记录最高的一次伤害
            long maxHurt = 0;

            String skin=null;
            isyqcs=(yqcs!=null&&Battlefield.isV(yqcs.getSkillhitrate()));
            for (int j = datas.size()-1; j>=0; j--) {//次数
                ManData data=datas.get(j);
                if (data.getStates()!=0) {
                    data=MixDeal.getjieshou(skill, manData, datas, battlefield);
                    if (data!=null) {
                        datas.set(j,data);
                    }else {
                        datas.remove(j);
                        continue;
                    }
                }
                FightingState Accepter=MixDeal.DSMY(manData,data,skill.getSkillid(),battlefield);
                if (Accepter==null) {
                    data.addBear(skilltype);
                    Accepter=new FightingState();
                    if (isTZ) {HurtAction.addTZState(data, tz_yg, tz_cb, tz_ph, tz_xy,Accepter);isTZ=false;}//判断是否中了套装技能
                    double hurt=skill.getSkillhurt();

                    if (isyqcs&&j==datas.size()-1) {
                        skin=yqcs.getSkilltype();
                        if (manData.getMp_z()>100000) {hurt+=(CustomFunction.XS(manData.getMp_z(),4200)-15000)*(PK_MixDeal.isPK(battlefield.BattleType)?1:2);}
                    }
                    for (int k = 1; k < sum; k++) {hurt=hurt*dds;}

                    // 只对主目标判定伤害之前的第一次攻击触发
                    if (j == datas.size()-1 && i == 0) {
                        manData.executeSdef(battlefield);
                        manData.executeQfyx(data,Accepterlist);
                    }

                    if (sum > 1) {
                        hurt *= shbs;
                    }

                    long fashang = 0;
                    fashang = HurtAction.hurt((int)hurt, jc, wg, kbl, skills, Accepterlist, Accepter, manData, data, skill, battlefield);

                    totalHurt += fashang;

                    if (maxHurt < fashang) {
                        maxHurt = fashang;
                    }

                    // 只对主目标判定伤害之后
                    if (j == datas.size()-1) {
                        // 清者自清，反隐+清Buff判定
                        manData.executeQzzq(data, Accepter, Accepterlist, battlefield);
                        // 清风送河判定
                        manData.addFaDun(fashang, Originator);
                    }
                    // 锥心刺骨判定
                    if (data.getStates() == 0 && skill.getSkillid() > 1040 && skill.getSkillid() < 1065 && skill.getSkillid() % 5 == 4){
                        int zxcg = manData.executeZxcg(1);
                        if (zxcg > 0 && Battlefield.isV(zxcg + manData.getShanghai() / 66.6)) {
                            // 至圣伤害

                            FightingState say = new FightingState();
                            say.setCamp(manData.getCamp());
                            say.setMan(manData.getMan());
                            say.setText("看我的#G锥心刺骨");
                            Accepterlist.add(0,say);

                            int zssh = manData.executeZxcg(2) * manData.getMp_z() / 100;
                            if (data.getCamp()==manData.getCamp()){
                                zssh=-zssh;
                            }
                            ChangeFighting changeFighting2=new ChangeFighting();
                            changeFighting2.setChangehp(-zssh);
                            FightingState fState2=new FightingState();
                            FightingPackage.ChangeProcess(changeFighting2, null, data, fState2, data.getCamp()==manData.getCamp()?skilltype:"至圣", Accepterlist, battlefield);
                        }
                    }
                    if(manData.getSkillId(9369)!=null){

                        Accepter=new FightingState();
                        ChangeFighting changeFighting2=new ChangeFighting();
                        //hurt=manData.getFmsld()*1000;
                        if (data.getCamp()==manData.getCamp()){
                            hurt=-hurt;
                        }
                        changeFighting2.setChangehp((int) -hurt);
                        FightingState say = new FightingState();
                        say.setCamp(manData.getCamp());
                        say.setMan(manData.getMan());
                        //say.setText("看我的#G大开杀戒");

                        FightingPackage.ChangeProcess(changeFighting2, manData, data, Accepter, data.getCamp()==manData.getCamp()?skilltype:"至圣", Accepterlist, battlefield);


                    }
                    // 添加归来去兮，增强一下去疾在游戏中的效果，这里不做单一目标被标记的判断，改为每次攻击只要被技能打到必定被标记
                    if (i == sum - 1  && gqlx != null) {  //&& j == datas.size() - 1
                        AddState zt=data.xzstate(gqlx.getSkilltype());
                        if (zt == null) {
                            data.addBear(gqlx.getSkilltype());
                            AddState addState=new AddState();
                            addState.setStatename(gqlx.getSkilltype());
                            addState.setStateEffect (gqlx.getSkillhurt());
                            addState.setStateEffect2(gqlx.getSkillgain());
                            addState.setSurplus(gqlx.getSkillcontinued());
                            Accepter.setEndState_1(addState.getStatename());
                            data.getAddStates().add(addState);
                        } else {
                            // 如果目标未死亡并且已经存在标记则在触发一次 法力上限12% 的伤害*等级/100的伤害  绝对伤害
                            if (data.getStates() == 0) {
                                FightingState Accepter2 = new FightingState();
                                double gqlxbj = manData.getMp_z() * 0.12 * manData.getLvl() / 100;
                                HurtAction.hurt(0, jc, gqlxbj, kbl, skills, Accepterlist, Accepter2, manData, data, skill, battlefield);
                            }
                        }
                    }
                }else {
                    Accepterlist.add(Accepter);
                }
                if (data.getCamp()==manData.getCamp())
                    skin="1241B";
                else skin=null;
                Accepter.setSkillskin(skin!=null?skin:skill.getSkillid()+"");

                //判断是否触发音
                if (i==0) {
                    if (ksys!=null) {
                        double gl=ksys.getSkillhitrate();
                        if (skill.getSkilllvl()==2){gl*=1.2;}
                        else if (skill.getSkilllvl()==3){gl*=1.45;}
                        else if (skill.getSkilllvl()==4){gl*=2;}
                        if (gl>Battlefield.random.nextInt(100)) {//触发成功
                            List<ManData> ksyss=battlefield.getZW(data);
                            ChangeFighting fighting=new ChangeFighting();
                            int hurt=(int) (ksys.getSkillhurt()/100*manData.getMp_z()/ksyss.size());
                            for (int k = ksyss.size()-1; k >=0; k--) {
                                ManData data2=ksyss.get(k);
                                FightingState Accepter2=new FightingState();
                                fighting.setChangehp(-hurt);
                                int y=data2.getStates();
                                data2.ChangeData(fighting,Accepter2);
                                Accepterlist.add(Accepter2);
                                if (data2.getStates()==1&&y!=data2.getStates()) {
                                    //先判断是否能复活
                                    MixDeal.DeathSkill(data2,Accepter2,battlefield);
                                }
                            }
                        }
                    }else if (isyqcs&&j==datas.size()-1) {
                        int size=1;
                        for (int k = 0; k <4; k++) {
                            if (Battlefield.isV(yqcs.getSkillhitrate())) {size++;}
                        }
                        List<ManData> datas2=MixDeal.get(false,data,0,manData.getCamp(),0,0,0,0,size,-1,battlefield,0);
                        for (int k = 0; k < datas2.size(); k++) {
                            long hurt=(int) skill.getSkillhurt();
                            if (manData.getMp_z()>100000) {hurt+=(CustomFunction.XS(manData.getMp_z(),4200)-15000)*(PK_MixDeal.isPK(battlefield.BattleType)?1:2);}
                            for (int l =0; l<=k;l++) {hurt*=0.8;}
                            ManData data2=datas2.get(k);
                            FightingState Accepter2=new FightingState();
//							hurt=Calculation.getCalculation().xianfa(manData,data2,hurt,skill.getSkilltype(),manData.getCamp()==1?battlefield.MyDeath:battlefield.NoDeath);
                            hurt=Calculation.getCalculation().SMHurt(manData,data2,hurt,0,skill.getSkilltype(),manData.getCamp()==1?battlefield.MyDeath:battlefield.NoDeath);

                            ChangeFighting fighting=new ChangeFighting();
                            fighting.setChangehp((int) -hurt);
                            fighting.setChangemp((int)(-hurt*0.2));
                            Accepter2.setSkillskin(skin);
                            FightingPackage.ChangeProcess(fighting,manData,data2,Accepter2,skill.getSkilltype(),Accepterlist,battlefield);
                        }
                    }
                }
            }

            // 师门法术触发狂暴判定大开杀戒随机周围1的1个目标造成等量伤害
            if (manData.fskbbj && maxHurt > 0 && Battlefield.isV(manData.executeDksj(2))){
                ManData friend = battlefield.getZW1(datas.get(datas.size()-1));
                if (friend != null) {
                    FightingState Accepter=new FightingState();
                    Accepter.setSkillskin(skin);
                    ChangeFighting changeFighting2=new ChangeFighting();
                    changeFighting2.setChangehp((int) -maxHurt);

                    FightingState say = new FightingState();
                    say.setCamp(manData.getCamp());
                    say.setMan(manData.getMan());
                    say.setText("看我的#G大开杀戒");
                    Accepterlist.add(0,say);
                    FightingPackage.ChangeProcess(changeFighting2, manData, friend, Accepter, skill.getSkilltype(), Accepterlist, battlefield);
                }
            }


            // Originator.setStartState("法术攻击");
            Originator.setSkillsy(skill.getSkillname());
            Accepterlist.add(Originator);
            FightingEvents Events=new FightingEvents();
            Events.setAccepterlist(Accepterlist);
            battlefield.NewEvents.add(Events);
            if (Accepterlist.size()==1) {break;}
        }


        if (manData.ymjr) {
            manData.ymjr = false;
            // 触发一鸣惊人后判定是否有隐身技和飘忽不定
            manData.executePhbd(battlefield.NewEvents.get(battlefield.NewEvents.size() -1));
        }



        if (skill2!=null&&totalHurt>0&&manData.getStates()==0) {
            if (skill2.getSkillid()==9364) {//9364|月满西楼|鬼火造成伤害的（0.5%*等级）回复为自己的法力(仅在与玩家之间战斗有效。)
                ChangeFighting fighting=new ChangeFighting();
                FightingState Accepter2=new FightingState();
                Accepter2.setStartState("药");
                totalHurt*=skill2.getSkillhurt()/100D;
                fighting.setChangemp((int) totalHurt);
                manData.ChangeData(fighting,Accepter2);
                Accepterlist.add(Accepter2);
            }else {
                totalHurt*=0.2;if (totalHurt>manData.getHp_z()) {totalHurt=manData.getHp_z();}
                ChangeFighting fighting=new ChangeFighting();
                FightingState Accepter2=new FightingState();
                Accepter2.setStartState("代价");
                fighting.setChangetype(skill2.getSkilltype());
                fighting.setChangevlaue(totalHurt);
                if (skill2.getSkillid()==9271) {
                    fighting.setChangevlaue2((skill.getSkillhurt()-15)/2*2000);
                }else if (skill2.getSkillid()==9328) {
                    fighting.setChangevlaue2((skill.getSkillhurt()-15)/2*0.5);
                }else {
                    fighting.setChangevlaue2((skill.getSkillhurt()-15)/2*5);
                }
                manData.ChangeData(fighting,Accepter2);
                Accepterlist.add(Accepter2);
            }
        }


    }




}

