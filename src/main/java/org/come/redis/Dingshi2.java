//package org.come.redis;
//
//import org.come.action.role.People;
//import org.come.bean.LoginResult;
//import org.come.bean.NChatBean;
//import org.come.handler.SendMessage;
//import org.come.protocol.Agreement;
//import org.come.until.GsonUtil;
//
//import java.math.BigDecimal;
//
//public class Dingshi2 extends Thread {
//
//    public Dingshi2() {
//
//    }
//
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(1000 * 5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String name = null;
//            NChatBean nChatBean = new NChatBean();
//
//            for (int i = 0; i <= People.tianting.size() - 1; i++) {
//                name = People.tianting.get(i).getTeamName();
//                if (People.tianting.get(i).getTeamSize() >= 4) {
//
//                    {
//                        SendMessage.sendMessageByRoleName(People.tianting.get(i).getTeams().get(3).getName(), Agreement.getAgreement().JiqirenAgreement("天庭-" + People.tianting.get(i).getTeamName()));
//                        People.tianting.remove(i);
//                        break;
//                    }
//
//                }
//                BigDecimal roleid = People.tianting.get(i).getTeamId();
//                nChatBean.setId(0);
//                nChatBean.setRoleId(roleid);
//                nChatBean.setRole(name);
//                LoginResult loginResult = null;
//                for (int h = 0; h <= RedisCacheUtil.jiqiren.size() - 1; h++) {
//                    if (name.equals(RedisCacheUtil.jiqiren.get(h).getRolename())) {
//                        loginResult = RedisCacheUtil.jiqiren.get(h);
//                        //break;
//                    }
//                }
//                String mes = "天庭队伍，来小号，有没有来的！3=2++++++++++++#35";
//                nChatBean.setMessage(mes);
////                String msg = Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(nChatBean));
////                if (loginResult != null)
////                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), msg);
//            }
//            try {
//                Thread.sleep(1000 * 5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            name = null;
//            nChatBean = new NChatBean();
//
//            for (int i = 0; i <= People.xiuluo.size() - 1; i++) {
//                name = People.xiuluo.get(i).getTeamName();
//                if (People.xiuluo.get(i).getTeamSize() >= 4) {
//
//                    {
//                        SendMessage.sendMessageByRoleName(People.xiuluo.get(i).getTeams().get(3).getName(), Agreement.getAgreement().JiqirenAgreement("修罗-" + People.xiuluo.get(i).getTeamName()));
//                        People.xiuluo.remove(i);
//                        break;
//                    }
//
//                }
//                BigDecimal roleid = People.xiuluo.get(i).getTeamId();
//                nChatBean.setId(0);
//                nChatBean.setRoleId(roleid);
//                nChatBean.setRole(name);
//                LoginResult loginResult = null;
//                for (int h = 0; h <= RedisCacheUtil.jiqiren.size() - 1; h++) {
//                    if (name.equals(RedisCacheUtil.jiqiren.get(h).getRolename())) {
//                        loginResult = RedisCacheUtil.jiqiren.get(h);
//                        //break;
//                    }
//                }
//                String mes = "修罗队伍队伍，来人，有没有来的！3=2++++++++++++#35";
//                nChatBean.setMessage(mes);
////                String msg = Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(nChatBean));
////                if (loginResult != null)
////                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), msg);
//            }
//            try {
//                Thread.sleep(1000 * 5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            name = null;
//            nChatBean = new NChatBean();
//
//            for (int i = 0; i <= People.yuwai.size() - 1; i++) {
//                name = People.yuwai.get(i).getTeamName();
//                if (People.yuwai.get(i).getTeamSize() >= 4) {
//
//                    {
//                        SendMessage.sendMessageByRoleName(People.yuwai.get(i).getTeams().get(3).getName(), Agreement.getAgreement().JiqirenAgreement("域外-" + People.yuwai.get(i).getTeamName()));
//                        People.yuwai.remove(i);
//                        break;
//                    }
//
//                }
//                BigDecimal roleid = People.yuwai.get(i).getTeamId();
//                nChatBean.setId(0);
//                nChatBean.setRoleId(roleid);
//                nChatBean.setRole(name);
//                LoginResult loginResult = null;
//                for (int h = 0; h <= RedisCacheUtil.jiqiren.size() - 1; h++) {
//                    if (name.equals(RedisCacheUtil.jiqiren.get(h).getRolename())) {
//                        loginResult = RedisCacheUtil.jiqiren.get(h);
//                        //break;
//                    }
//                }
//                String mes = "域外队伍队伍，来人，有没有来的！3=2++++++++++++#35";
//                nChatBean.setMessage(mes);
////                String msg = Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(nChatBean));
////                if (loginResult != null)
////                    SendMessage.sendMessageToMapRoles(loginResult.getMapid(), msg);
//            }
//
//        }
//
//
//    }
//}
