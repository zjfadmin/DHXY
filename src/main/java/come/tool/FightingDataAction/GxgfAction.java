package come.tool.FightingDataAction;

import cn.hutool.core.util.RandomUtil;
import come.tool.FightingData.*;

import java.util.List;

public class GxgfAction implements GxgfInterface{
    //护盾类
    @Override
    public void gxgf(FightingState org2, ManData mydata, ManData data, ChangeFighting changeFighting,
                     Battlefield battlefield, List<FightingState> list) {

        if (changeFighting.getChangehp()>=0) return;
        AddState addState=null;
        FightingSkill skill=null;
        if (PK_MixDeal.isPK(battlefield.BattleType)) {
//    		9232|苦海慈航|释放一个强力乾坤借速，    为所有被速的单位增加一个护盾，当被速目标气血百分比低于（20%*等级）时，该护盾可以抵御义词致死伤害，护盾生效时清除被速目标身上的速法效果，护盾最多存在3回合。此技能全队全场只能用一次。（仅在与玩家之间战斗有效）
            skill=data.getAppendSkill(9232);
            if (skill!=null&&data.getvalue(0)<skill.getSkillhurt()/100.0) {
                if (-changeFighting.getChangehp()>=data.getHp()) {
                    data.RemoveAbnormal(TypeUtil.JS);
                    org2.setProcessState("免疫");
                    org2.setStartState("防御");
                    org2.setEndState_2(TypeUtil.JS);
                    changeFighting.setChangehp(0);
                    data.RemoveAppendSkill(9232);
                    return;
                }
            }
            addState=data.xzstate(TypeUtil.TY_KX_JJCS);
            if (addState!=null) {
                if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                    addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                    org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                    org2.setStartState("防御");
                    changeFighting.setChangehp(0);
                }else {
                    data.RemoveAbnormal(addState.getStatename());
                    FightingState org=new FightingState();
                    org.setCamp(data.getCamp());
                    org.setMan(data.getMan());
                    org.setStartState("防御");
                    org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                    list.add(org);
                    changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    FightingSkill skill2=addState.getSkill(9247);
                    if (skill2!=null&&Battlefield.isV(skill2.getSkillhurt())) {
                        data.RemoveAbnormal(org,ManData.values2);
                        org.setEndState_2("清除异常状态");
                    }
                    if (mydata!=null&&mydata.getType()!=3&&mydata.getType()!=4) {
                        skill2=addState.getSkill(9244);
                        if (skill2!=null&&Battlefield.isV(skill2.getSkillhurt())) {
                            ChangeFighting change=new ChangeFighting();
                            change.setChangehp(-(int) (skill2.getSkillhurt()*800));
                            FightingState org3=new FightingState();
                            org3.setCamp(mydata.getCamp());
                            org3.setMan(mydata.getMan());
                            org2.setStartState("被攻击");
                            FightingPackage.ChangeProcess(change, data,mydata, org3, "盾破", list, battlefield);
                        }
                    }
                }
                return;
            }
            addState=data.xzstate(TypeUtil.TY_MH_RSSQ);
            if (addState!=null) {
                int maxHurt=(int) (addState.getStateEffect2()-addState.getStateEffect());
                if (maxHurt>0) {
                    if (maxHurt>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        addState.setStateEffect(addState.getStateEffect()+maxHurt);
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+(maxHurt));
                        list.add(org);
                        changeFighting.setChangehp(changeFighting.getChangehp()+maxHurt);
                    }
                }
                return;
            }
            if (Sepcies_MixDeal.getRace(data.getSe())==10003) {
                addState=data.xzstate(TypeUtil.TY_L_ZXLT);
                if (addState!=null) {
                    //9271|紫霄雷霆|在使用天课地灭时有（15%+2%*等级）几率将造成伤害之和的20%(最大不超过自己的气血上限)转化为自己的护盾,
                    //护盾存在期间每次受到一次敌方的攻击，都向敌方释放一道电波，造成（2000*等级）点 气血或者法力伤害，护盾最多持续3回合。( 仅在与玩家之间战斗有效)
                    if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        data.RemoveAbnormal(addState.getStatename());
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                        org.setEndState_2(addState.getStatename());
                        list.add(org);
                        changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    }
                    if (mydata!=null) {
                        ChangeFighting fighting=new ChangeFighting();
                        FightingState Accepter2=new FightingState();
                        Accepter2.setStartState("代价");
                        if (Battlefield.isV(50)) {fighting.setChangehp(-(int)addState.getStateEffect2());}
                        else {fighting.setChangemp(-(int)addState.getStateEffect2());}
                        mydata.ChangeData(fighting,Accepter2);
                        list.add(Accepter2);
                    }
                    return;
                }
                addState=data.xzstate(TypeUtil.TY_S_XCWM);
                if (addState!=null) {
                    //9288|仙尘帷幔|在使用九龙冰封时有（15%+2%*等级）几率将造成伤害之和的20%(最大不超过自己的气血上限)转化为自己的护盾,
                    //护盾存在期间自己有（5%*等级）的几率免疫混冰睡忘和金箍控制,护盾消失时随机解除自身一个异常状态,护盾最多持续卖3回合。仅在与玩家之间战斗有效
                    if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        data.RemoveAbnormal(addState.getStatename());
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                        org.setEndState_2(addState.getStatename());
                        list.add(org);
                        changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    }
                    return;
                }
                addState=data.xzstate(TypeUtil.TY_F_YYFQ);
                if (addState!=null) {
                    //9308|云涌风飞|在使用袖里乾坤时有（15%+2%*等级）几率将造成伤害之和的20%(最大不超过自己的气血上限)转化为自己的护盾,
                    //护盾存在期间对自己造成伤害的敌方人物单位有（5%*等级）的几率在下回合使用多体师门法术时目标数减1,护盾最多持续3回合。(仅在与玩家之间战斗有效)
                    if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        data.RemoveAbnormal(addState.getStatename());
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                        org.setEndState_2(addState.getStatename());
                        list.add(org);
                        changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    }
                    if (mydata!=null&&Battlefield.isV(addState.getStateEffect2())) {
                        mydata.addAddState(TypeUtil.TY_F_YYFQ_S,0,0,2);
                    }
                    return;
                }
                addState=data.xzstate(TypeUtil.TY_H_ZSMH);
                if (addState!=null) {
                    //9328|照世明火|在使用九阴纯火时有（15%+2%*等级）几率将造成伤害之和的20%(最大不超过自己的气血上限)转化为自己的护盾,
                    //护盾存在期间每个攻击自己的敌方目标都会受到灼烧,处于该状态下的目标下回合开始前随机受到自已气血上限（0.5%*等级）~（1.5%*等级）的伤害,护盾最多持续3回合。(仅在与玩家之间战斗有效
                    if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        data.RemoveAbnormal(addState.getStatename());
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                        org.setEndState_2(addState.getStatename());
                        list.add(org);
                        changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    }
                    if (mydata!=null) {
                        mydata.addAddState(TypeUtil.TY_H_ZSMH_S,addState.getStateEffect2(),0,2);
                    }
                    return;
                }
            }
        }else {
//    		9262|销声匿迹|给自己释放一个可以持续3回合的护盾，该护盾消耗法力会抵挡所受的气血伤害。每回合最多吸收最大法力值（10%+5%*等级）的伤害。（仅在与NPC之间战斗有效）
            addState=data.xzstate(TypeUtil.TY_L_XSNJ);
            if (addState!=null) {
                if (addState.getStateEffect()>0) {
                    if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                        addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                        org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                        org2.setStartState("防御");
                        changeFighting.setChangehp(0);
                    }else {
                        FightingState org=new FightingState();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        org.setStartState("防御");
                        org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                        list.add(org);
                        changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    }
                }
                if (mydata!=null) {
                    FightingSkill skill2=addState.getSkill(9265);
                    if (skill2!=null) {
                        ChangeFighting change2=new ChangeFighting();
                        int hurt=(int)skill2.getSkillhurt()-5000;
                        if (hurt>0) {hurt=Battlefield.random.nextInt(hurt);}
                        hurt+=skill2.getSkillhurt();
                        FightingSkill skill3=addState.getSkill(9268);
                        if (skill3!=null&&Battlefield.isV(skill3.getSkillhurt())) {
                            hurt+=data.getHp()*0.1;
                        }
                        change2.setChangehp(-(hurt));
                        FightingState org3=new FightingState();
                        org3.setCamp(mydata.getCamp());
                        org3.setMan(mydata.getMan());
                        org3.setStartState("被攻击");
                        FightingPackage.ChangeProcess(change2,null,mydata,org3,TypeUtil.TY_L_DZXY,list,battlefield);
                    }
                    if (changeFighting.getChangehp()<0) {
                        skill2=addState.getSkill(9302);
                        if (skill2!=null) {
                            ChangeFighting change2=new ChangeFighting();
                            int hurt=(int)(changeFighting.getChangehp()*skill2.getSkillhurt()/100.0);
                            if (hurt<0) {
                                change2.setChangehp(hurt);
                                FightingState org3=new FightingState();
                                org3.setCamp(mydata.getCamp());
                                org3.setMan(mydata.getMan());
                                org3.setStartState("被攻击");
                                FightingPackage.ChangeProcess(change2,null,mydata,org3,TypeUtil.TY_L_DZXY,list,battlefield);
                            }
                        }
                    }
                    skill2=addState.getSkill(9305);
                    if (skill2!=null&&Battlefield.isV(skill2.getSkillhurt())) {
                        data.addAddState(TypeUtil.TY_F_WHSF, 20, 0, 2);
                    }
                    skill2=addState.getSkill(9322);
                    if (skill2!=null) {
                        data.addAddState(TypeUtil.TY_H_JSYY, skill2.getSkillhurt(), 0, 2);
                    }
                    skill2=addState.getSkill(9325);
                    if (skill2!=null) {
                        data.addAddState(TypeUtil.TY_H_YHRJ, skill2.getSkillhurt(), 0, 2);
                    }
                }
            }
        }
        addState=data.xzstate("法魂护体");
        if (addState!=null) {
            if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                org2.setStartState("防御");
                changeFighting.setChangehp(0);
            }else {
                FightingState org=new FightingState();
                org.setCamp(data.getCamp());
                org.setMan(data.getMan());
                org.setStartState("防御");
                org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                org.setEndState_2(addState.getStatename());
                list.add(org);
                changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                data.RemoveAbnormal(addState.getStatename());
            }
            return;
        }

        addState=data.xzstate("血蛊护盾");
        if (addState!=null) {
            if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                org2.setStartState("防御");
                changeFighting.setChangehp(0);
            }else {
                FightingState org=new FightingState();
                org.setCamp(data.getCamp());
                org.setMan(data.getMan());
                org.setStartState("防御");
                org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                org.setEndState_2(addState.getStatename());
                list.add(org);
                changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                data.RemoveAbnormal(addState.getStatename());
            }
            return;
        }

        addState=data.xzstate("骨盾");
        if (addState==null) {
            addState=data.xzstate(TypeUtil.TY_L_GL_YMFZ);
        }
        if (addState!=null) {
            if (addState.getStateEffect()>-changeFighting.getChangehp()) {
                addState.setStateEffect(addState.getStateEffect()+changeFighting.getChangehp());
                org2.setProcessState("吸收  "+(-changeFighting.getChangehp()));
                org2.setStartState("防御");
                changeFighting.setChangehp(0);
            }else {
                data.RemoveAbnormal(addState.getStatename());
                FightingState org=new FightingState();
                org.setCamp(data.getCamp());
                org.setMan(data.getMan());
                org.setStartState("防御");
                org.setProcessState("吸收  "+((int)addState.getStateEffect()));
                org.setEndState_2(addState.getStatename());
                list.add(org);
                if (addState.getStatename().equals("骨盾")) {
                    changeFighting.setChangehp((int)(changeFighting.getChangehp()+addState.getStateEffect()));
                    if (mydata!=null&&mydata.getType()!=3&&mydata.getType()!=4) {
                        ChangeFighting change=new ChangeFighting();
                        change.setChangehp((int)-addState.getStateEffect2());
                        FightingState org3=new FightingState();
                        org3.setCamp(mydata.getCamp());
                        org3.setMan(mydata.getMan());
                        org3.setStartState("被攻击");
                        FightingPackage.ChangeProcess(change, data,mydata, org3, "盾破", list, battlefield);
                    }
                }
            }
            return;
        }

        addState=data.xzstate(TypeUtil.BB_E_百战重生);
        if (addState != null) {
            if (addState.getSurplus() > 0) {
                FightingState org=new FightingState();
                org.setCamp(data.getCamp());
                org.setMan(data.getMan());
//				System.out.println(addState.getStateEffect());
//				double eff = addState.getStateEffect();
//				int effect = (int)(0.01*eff*changeFighting.getChangehp());
                org.setStartState("防御");
                org.setProcessState("吸收  "+changeFighting.getChangehp());
//				org.setEndState_2(addState.getStatename());
                list.add(org);
                changeFighting.setChangehp(-(changeFighting.getChangehp()));
                addState.setSurplus(addState.getSurplus() - 1);
                if(addState.getSurplus() <= 0) {
                    data.deleteState(TypeUtil.BB_E_百战重生);
                }
            }
            return;
        }

        //一御当千
        addState=data.xzstate(TypeUtil.BB_YYDQ);
        FightingSkill skill_BB_E_百战重生 = data.getSkillType(TypeUtil.BB_E_百战重生);
        if (addState!=null) {
            FightingState org=new FightingState();
            org.setCamp(data.getCamp());
            org.setMan(data.getMan());
//			System.out.println(addState.getStateEffect());
            double eff = addState.getStateEffect();
            if (skill_BB_E_百战重生 != null) {
                eff += skill_BB_E_百战重生.getSkillhurt();
            }
            int effect = (int)(0.01*eff*changeFighting.getChangehp());
            org.setStartState("防御");
            org.setProcessState("吸收  "+(Math.abs(effect)));
//			org.setEndState_2(addState.getStatename());
            list.add(org);
            changeFighting.setChangehp((int)(changeFighting.getChangehp()+Math.abs(effect)));

            data.addAddState(TypeUtil.BB_E_百战重生, 0, 0, 2);

            return;
        }else {
            FightingSkill yydq = data.getSkillType(TypeUtil.BB_YYDQ);
            if (yydq != null) {
                //一御当千
                if (Math.abs(changeFighting.getChangehp()) > data.getHp()*0.3f && Math.abs(changeFighting.getChangehp()) < data.getHp()) {
                    double r = yydq.getSkillgain();
                    if (skill_BB_E_百战重生 != null) {
                        r += skill_BB_E_百战重生.getSkillgain();
                    }
                    if (RandomUtil.randomInt(100) < r) {
//				System.out.println(yydq.getSkillcontinued());
                        //产生护盾
                        FightingState org=new FightingState();//fightingEvents.getOriginator();
                        org.setCamp(data.getCamp());
                        org.setMan(data.getMan());
                        //org.setEndState(data.getCamp()+"|"+data.getMan()+ "|" + PhyAttack.getdir(3));
                        org.setStartState(TypeUtil.JN);
                        org.setSkillskin(TypeUtil.BB_YYDQ);
                        org.setEndState_1(TypeUtil.BB_YYDQ);
                        list.add(org);
                        data.addAddState(TypeUtil.BB_YYDQ, yydq.getSkillhurt(), 0, yydq.getSkillcontinued());
                    }
                }
            }
        }
    }

}
