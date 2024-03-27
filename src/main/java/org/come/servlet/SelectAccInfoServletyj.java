package org.come.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.come.ApiValid;
import org.come.bean.Account;
import org.come.bean.managerTable;
import org.come.entity.UserTable;
import org.come.redis.RedisCacheUtil;
import org.come.until.AllServiceUtil;
import org.come.until.GsonUtil;

// 三端(/servlet/selectAccInfo)
public class SelectAccInfoServletyj extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public SelectAccInfoServletyj() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//修复刷仙玉BUG和盗号
		managerTable manege= (managerTable)request.getSession().getAttribute("xy2o");
		String token = request.getHeader("manage_token");
		String VALID_NAME = request.getParameter(ApiValid.VALID_NAME);
		if(null == VALID_NAME || !VALID_NAME.equals(ApiValid.VALID_VALUE) || manege ==null || !ApiValid.vaildToken(token,manege.getUsername())){
			System.out.println("【PayvipBeanServlet】非法请求！！,已踢出");
			return ;
		}
		String type = request.getParameter("type");
		String userName;
		if (!"autoReg".equals(type)) {
			if (type.equals("all")) {
				List list = AllServiceUtil.getUserTableService().findAllUser();
				if (list == null) {
					list = new ArrayList();
				}

				PrintWriter pwPrintWriter = response.getWriter();
				pwPrintWriter.write(GsonUtil.getGsonUtil().getgson().toJson(list));
				pwPrintWriter.flush();
				pwPrintWriter.close();
			} else if (type.equals("one")) {
				userName = request.getParameter("userName");
				UserTable result = AllServiceUtil.getUserTableService().findUserByUserNameAndUserPassword(userName, null);
				List list = new ArrayList();
				if (result != null) {
					list.add(result);
				}

				PrintWriter pwPrintWriter = response.getWriter();
				pwPrintWriter.write(GsonUtil.getGsonUtil().getgson().toJson(list));
				pwPrintWriter.flush();
				pwPrintWriter.close();
			}
		} else {
			userName = request.getParameter("info");
			Account ac = GsonUtil.getGsonUtil().getgson().fromJson(userName, Account.class);
			// 数据获取
			String username = ac.getAc_account();// 账号
			String password = ac.getAc_pasw();// 密码
			String safely = ac.getAc_safely();// 安全码
			String userflag = ac.getAc_flag();// 账号标识
			String tuiji = ac.getAc_tuijian();// 推荐码
			String phone = ac.getAc_phone();// 手机
			String registerip = ac.getAc_regip();
			UserTable usertable = AllServiceUtil.getUserTableService().selectByFlag(userflag);
			if (usertable == null) {
				while(true) {
					UserTable result = AllServiceUtil.getUserTableService().findUserByUserNameAndUserPassword(username, null);
					if (result == null) {
						List sid = AllServiceUtil.getOpenareatableService().selectTuijiNum(tuiji);
			// 设置账号信息
						UserTable userTable = new UserTable();
						userTable.setUser_id(RedisCacheUtil.getUser_pk());
						userTable.setUsername(username);
						userTable.setUserpwd(password);
						userTable.setSafety(safely);
						userTable.setFlag(userflag);
						userTable.setTuiji(tuiji);
						userTable.setQid((BigDecimal)sid.get(0));
						userTable.setPhonenumber(phone);
						userTable.setRegisterip(registerip);
			// 注册新用户
						int isSuccess = AllServiceUtil.getUserTableService().insertIntoUser(userTable);
						String res = "";
						if (isSuccess <= 0) {
							res = "no";
						} else {
							res = "yes";
						}

						PrintWriter pwPrintWriter = response.getWriter();
						pwPrintWriter.write(res);
						pwPrintWriter.flush();
						pwPrintWriter.close();
						return;
					}

					username = username + "zr";
				}
			}

			// 返回给用户信息
			PrintWriter pwPrintWriter = response.getWriter();
			pwPrintWriter.write("exist");
			pwPrintWriter.flush();
			pwPrintWriter.close();
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

}
