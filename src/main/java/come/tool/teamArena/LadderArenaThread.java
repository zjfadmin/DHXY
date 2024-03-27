package come.tool.teamArena;

import come.tool.teamArena.TeamArenaUtil;
import org.come.bean.NChatBean;
import org.come.handler.SendMessage;
import org.come.protocol.Agreement;
import org.come.until.GsonUtil;

public class LadderArenaThread implements Runnable{
	private long time;//活动开启的时间
	@Override
	public void run() {
		// TODO Auto-generated method stub
		NChatBean bean=new NChatBean();
		bean.setId(5);
		bean.setMessage("#R天梯开始了,想要参加的玩家通过天梯参加！");
		String msg = Agreement.getAgreement().chatAgreement(GsonUtil.getGsonUtil().getgson().toJson(bean));
		SendMessage.sendMessageToAllRoles(msg);

		time=System.currentTimeMillis();
		while (true) {//800毫秒检测一次
			try {
				Thread.sleep(800);
				long time2=System.currentTimeMillis();
				if (time2-time> TeamArenaUtil.TIME) {break;}//活动应该结束了
				LadderArenaUtil.confirmTimeOut(time2);//确认队列超时处理
				LadderArenaUtil.match(time2);//匹配队列处理
				LadderArenaUtil.prepare(time2);//准备队列处理
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		//结束处理
		LadderArenaUtil.teamArenaEnd();
	}
}
