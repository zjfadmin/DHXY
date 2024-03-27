package org.come.until;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import come.tool.Mixdeal.AnalysisString;
import org.apache.commons.lang.StringUtils;
import org.come.action.gl.LxAction;
import org.come.bean.LoginResult;
import org.come.entity.RoleSummoning;
import org.come.entity.UserTable;
import org.come.handler.SendMessage;
import org.come.model.Skill;
import org.come.protocol.Agreement;
import org.come.protocol.ParamTool;
import org.come.server.GameServer;
import org.come.tool.WriteOut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetValid {

    public static List<String> lykBase = new ArrayList<>();

    static {
        lykBase.add("抗水");
        lykBase.add("抗水法");
        lykBase.add("抗火");
        lykBase.add("抗火法");
        lykBase.add("抗雷");
        lykBase.add("抗雷法");
        lykBase.add("抗风");
        lykBase.add("抗风法");
        lykBase.add("抗昏睡");
        lykBase.add("抗混乱");
        lykBase.add("抗封印");
        lykBase.add("抗遗忘");
        lykBase.add("抗鬼火");
        lykBase.add("抗中毒");
        lykBase.add("物理吸收");
        lykBase.add("抗物理");
    }


    public static boolean validLingXi(RoleSummoning roleSummoning, RoleSummoning pet, LoginResult loginResult) {
        String rolename = loginResult.getRolename();
        BigDecimal role_id = loginResult.getRole_id();

        String lx = pet.getLingxi();


        if (StringUtil.isEmpty(lx)) {
            return false;
        }
        String newLingxi = roleSummoning.getLingxi();

        String[] svs=lx.split("&");


        if (svs.length != 4) {
            SendMessage.sendMessageByRoleName(rolename, Agreement.getAgreement().PromptAgreement("#R召唤兽信息出错，请联系管理员，并提供加点方案及召唤兽信息"));
            accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
            return true;
        }

        String[] vs=newLingxi.split("&");
        if (vs.length < 2) {
            return true;
        }

        int mb = Integer.parseInt(vs[2].split("=")[1]);  // 所选面板
        if (mb == 0) {
            SendMessage.sendMessageByRoleName(rolename,Agreement.getAgreement().PromptAgreement("#R该召唤兽还没有选择灵犀技能类型"));
            accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
            return true;
        }

        int smb = Integer.parseInt(svs[0].split("=")[1]);
        // 灵犀点数
        int point = Integer.parseInt(svs[2].split("=")[1]);
        // 原灵犀技能
        String srcLx = svs[3].split("=")[1];
        // 新灵犀技能
        String newLx = vs[3];

        // -----------对比灵犀面板：前后要一致，如果之前为0则后面可以换其他类型
//		if (smb == 0){
//			svs[0] = "Lx=" + mb;
//		} else {
//			if (smb != mb) {
//				SendMessage.sendMessageByRoleName(rolename,Agreement.getAgreement().PromptAgreement("#R该召唤兽已选择了其他灵犀类型，如要改变类型请先清空之前的技能点数"));
//				accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
//				return true;
//			} else {
//				svs[0] = "Lx=" + mb;
//			}
//		}

        String[] srcJN = srcLx.split("\\|");
        String[] newJN = newLx.split("\\|");

        LxAction.XSkillLx[] xskills = new LxAction.XSkillLx[srcJN.length];
        for (int i = 0; i < srcJN.length ; i++) {
            String[] sjn = srcJN[i].split("_");
            Skill skill = GameServer.getSkill(sjn[0]);
            if (skill == null) {
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R召唤兽技能信息出错，请联系管理员，并提供加点方案及召唤兽信息"));
                accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                return true;
            }
            // 原技能点数
            int count = Integer.parseInt(sjn[1]);
            xskills[i] = LxAction.getXSkillLx(skill.getSkillid(), mb);
            if (xskills[i] == null) {
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R召唤兽技能信息出错，请联系管理员，并提供加点方案及召唤兽信息"));
                accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                return true;
            }

            xskills[i].setNowPoint(count);
            xskills[i].setSKill(skill);

            if (xskills[i].type != mb) {
                xskills[i].setNowPoint(0);
                continue;
            }

            for (String jn2 : newJN) {
                String[] njn = jn2.split("_");
                // 新技能点数
                int count2 = Integer.parseInt(njn[1]);
                if (sjn[0].equals(njn[0]) && xskills[i].nowPoint < count2) {
                    // 点数更新了则重新计算
                    xskills[i].setNowPoint(count2);
                }
            }
        }

        // -----------校验灵犀点，不能超出所拥有的灵犀点
        if (LxAction.calPoint(xskills) > point) {
            SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R当前召唤兽的灵犀点数不足"));
            accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
            return true;
        }

        // 校验
        for (LxAction.XSkillLx xskill : xskills) {
            if (xskill.type != mb) {
                continue;
            }
            //未加点的技能项跳过
            if (xskill.nowPoint == 0) {
                continue;
            }

            if (xskill.skill.getDielectric() > 0) {
                // 有前置要求则判断
                if (LxAction.downPoint(xskills,xskill.col) < xskill.skill.getDielectric()) {
                    SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R技能" + xskill.skill.getSkillname() + "不满足前置要求无法保存"));
                    accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                    return true;
                }
            }

            //校验等级
            int lv = pet.getGrade() - 544;
            if (xskill.skill.getSkilllevel() != 0 && lv < xskill.skill.getSkilllevel()) {
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R召唤兽等级不满足条件，无法保存"));
                accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                return true;
            }

            //判断技能互斥
            if (StringUtils.isNotEmpty(xskill.skill.getSkillralation()) && LxAction.getCountTemp(xskills , Integer.parseInt(xskill.skill.getSkillralation())) > 0) {
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R存在互斥关系，无法保存"));
                accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                return true;
            }
            //判断6选2
            if (!LxAction.selectMax(xskills, xskill.id, xskill.type)) {
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R超出可同时修炼限制，此召唤兽不可修炼此技能"));
                accountStopAndWriteTxt(loginResult, newLingxi, rolename, role_id, "roleId = %s PET 数据异常 LX = %s");
                return true;
            }
        }
        return false;
    }

    public static boolean validLysKx(RoleSummoning roleSummoning, LoginResult loginResult) {
        String rolename = loginResult.getRolename();
        BigDecimal role_id = loginResult.getRole_id();
        String lyk = roleSummoning.getLyk();

        if(roleSummoning.getGrade() > 744){
            SendMessage.sendMessageByRoleName(rolename,Agreement.getAgreement().PromptAgreement("#R召唤兽信息出错，请联系管理员，并提供加点方案及召唤兽信息"));
            accountStopAndWriteTxt(loginResult, roleSummoning.getGrade().toString(), rolename, role_id, String.format("roleId = %s PET 数据异常 data = %s", role_id , lyk));
            return true;
        }

        if(roleSummoning.getAlchemynum() <= 0 && StrUtil.isNotBlank(lyk)){
            SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R数据异常，封号处理"));
            accountStopAndWriteTxt(loginResult, lyk, rolename, role_id, String.format("roleId = %s PET 数据异常 data = %s", role_id , lyk));
            return true;
        }

        if(StrUtil.isNotBlank(lyk)){
            String[] v=lyk.split("\\|");
            if(v.length > 11){
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R数据异常，封号处理"));
                accountStopAndWriteTxt(loginResult, lyk, rolename, role_id, String.format("roleId = %s PET 数据异常 data = %s", role_id , lyk));
                return true;
            }

            boolean feifa = false;
            String[] strings = new String[ v.length];
            for (int i = 0; i < v.length; i++) {
                String[] v1=v[i].split("=");
                if(StrUtil.isBlank(v1[0])){
                    continue;
                }
                if(!lykBase.contains(v1[0])){
                    feifa = true;
                    break;
                }

                if(Double.parseDouble(v1[1]) > 75){
                    v1[1] = "75";
                }
                strings[i] = v1[0]+"="+v1[1];
            }
            if(v.length>0){
                String arrayString = Arrays.toString(strings);
                arrayString = arrayString.substring(1, arrayString.length() - 1);
                arrayString = arrayString.replace(", ", "|");
                roleSummoning.setLyk(arrayString);
            }

            if(feifa){
                SendMessage.sendMessageByRoleName(loginResult.getRolename(),Agreement.getAgreement().PromptAgreement("#R数据异常，封号处理"));
                accountStopAndWriteTxt(loginResult, lyk, rolename, role_id, String.format("roleId = %s PET 数据异常 data = %s", role_id , lyk));
                return true;
            }
        }

        if(roleSummoning.getBasishp() > 12000000 || roleSummoning.getBasismp() > 14000000){
            accountStop(loginResult,loginResult.getRolename(), String.format(" 血量蓝量数值超了 血 = %s 蓝 = %s", roleSummoning.getBasishp(), roleSummoning.getBasismp()));
            return true;
        }

        int[] pets2 = new int[5];

        double grow = Double.parseDouble(roleSummoning.getGrowlevel());
        pets2[0] = roleSummoning.getHp();
        pets2[1] = roleSummoning.getMp();
        pets2[2] = roleSummoning.getAp();
        pets2[3] = roleSummoning.getSp();
        pets2[4] = 0;

        int[] pets3 = getPetHMASp(roleSummoning);
        if (grow >= 99.99 || pets2[0] > 999999 || pets2[1] > 999999 || pets2[2] > 999999 || pets2[3] > 999999 || pets3[2] > 300000 || pets3[3] > 80000) {
            String pets2String = Arrays.toString(pets2);
            // 去除首尾的方括号
            pets2String = pets2String.substring(1, pets2String.length() - 1);

            String pets3String = Arrays.toString(pets3);
            // 去除首尾的方括号
            pets3String = pets3String.substring(1, pets3String.length() - 1);

            accountStop(loginResult,loginResult.getRolename(), String.format(" 属性点异常 %s" , pets2String + pets3String));
            return true;
        }

//        System.out.println(String.format("名字 = %s hp = %s mp = %s ap = %s sp = %s ", roleSummoning.getSummoningname(), roleSummoning.getBasishp(),
//                roleSummoning.getBasismp(), pets3[2], pets3[3]));
        return false;
    }

    static String[] evs=new String[]{"根骨=","灵性=","力量=","敏捷=","炼化属性","增加气血","增加法力","增加攻击"};

    public static int[] getPetHMASp(RoleSummoning pet) {
        int[] pets = new int[5];
        if (pet == null) {
            return pets;
        }
        int lvl = AnalysisString.petLvlint(pet.getGrade());
        double grow = Double.parseDouble(pet.getGrowlevel());
        pets[0] = pet.getHp();
        pets[1] = pet.getMp();
        pets[2] = pet.getAp();
        pets[3] = pet.getSp();
        pets[4] = 0;
//		if (grow >= 99.99 || pets[0] > 99999 || pets[1] > 99999 || pets[2] > 99999 || pets[3] > 99999) {
//			JmSum.xiugaiqi();//召唤兽属性上限
//		}
        int zBone = pet.getBone();
        int zSpir = pet.getSpir();
        int zPower = pet.getPower();
        int zSpeed = pet.getSpeed();
        int zCalm = pet.getCalm();
        int addhp = 0, addmp = 0, addap = 0;

        pets[0] = getRoleValue(lvl, zBone, grow, pets[0], 0) + addhp;
        pets[1] = getRoleValue(lvl, zSpir, grow, pets[1], 1) + addmp;
        pets[2] = getRoleValue(lvl, zPower, grow, pets[2], 2) + addap;
        pets[3] = getRoleValue(lvl, zSpeed, grow, pets[3], 3);
        pets[4] = getRoleValue(lvl, zCalm, grow, pets[4], 4);
        //获取四维的数值加成
        pet.getSI(pets);
        //获取灵犀加成
        pet.getLX(pets);
        if (pet.getPetSkills() != null) {
            if (pet.getPetSkills().contains("1248")) {
                pets[2] *= 1.3;
            }
        }
        return pets;
    }

    public static int getRoleValue(int lvl, int P, double G, int base, int type) {
        if (type == 0 || type == 1) {
            return (int) (lvl * P * G) + (int) ((0.7 * lvl * G + 1) * base);
        } else if (type == 2) {
            return (int) (lvl * P * G / 5)
                    + (int) ((0.14 * lvl * G + 1) * base);
        }else if (type == 3) {
            return (int) ((base + P) * G);
        }else {
            return P;
        }
    }

    private static void accountStopAndWriteTxt(LoginResult loginResult, String newLingxi, String rolename, BigDecimal role_id, String s) {
        WriteOut.addtxt(String.format(s, role_id, newLingxi), 8888);
        accountStop(loginResult, rolename,s);
    }

    //封号
    public static void accountStop(LoginResult loginResult, String rolename,String s) {
        UserTable userInfo = AllServiceUtil.getUserTableService().selectByPrimaryKey(loginResult.getUser_id());

        if (GameServer.getRoleNameMap().get(rolename) != null) {
            ParamTool.ACTION_MAP.get("accountstop").action(GameServer.getRoleNameMap().get(rolename), userInfo.getUsername());
        } else {
            // 获取账号名
            UserTable table = new UserTable();
            table.setUsername(userInfo.getUsername());
            table.setActivity((short) 1);
            // 修改用户信息
            AllServiceUtil.getUserTableService().updateUser(table);
            AllServiceUtil.getUserTableService().addRufenghaoControl(userInfo, loginResult.getRolename(),"PET 数据异常" + s,"系统",1);

        }
    }
}
