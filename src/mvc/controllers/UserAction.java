package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserAction {
	
	@Autowired
	private UserService userService;
	private DownloadAct downloadAct = new DownloadAct();

	// 用户登录
	@RequestMapping("/login")
	@ResponseBody
	public String login(HttpServletRequest request){
		String msg = userService.login(request);
		return msg;
	}
	
	// 添加用户
	@RequestMapping("/addUser")
	@ResponseBody
	public String addUser(HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String msg = userService.addUser(request,username,password,name,phone);
		return msg;
	}
	
	// 用户名校验
	@RequestMapping("/checkUsername")
	@ResponseBody
	public String checkUsername(HttpServletRequest request){
		String username = request.getParameter("username");
		String msg = userService.checkUsername(username);
		return msg;
	}
	
	// 添加权限
	@RequestMapping("/addPower")
	@ResponseBody
	public String addPower(HttpServletRequest request){
		String qx = request.getParameter("qx");
		String cnqx = request.getParameter("cnqx");
		String username = request.getParameter("username");
		String qxList = request.getParameter("qxList");
		String msg = userService.addPower(request,qx,cnqx,username,qxList);
		return msg;
	}
	
	// 获取用户列表
	@RequestMapping("/getUserList")
	@ResponseBody
	public String getUserList(HttpServletRequest request){
		return userService.getUserList(request);
	}
	
	// 修改用户
	@RequestMapping("/updateUser")
	@ResponseBody
	public String updateUser(HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String id = request.getParameter("id");
		String msg = userService.updateUser(request,username,password,name,phone,id);
		return msg;
	}
	
	// 删除用户
	@RequestMapping("/deleteUser")
	@ResponseBody
	public String deleteUser(HttpServletRequest request){
		String username = request.getParameter("username");
		return userService.deleteUser(request,username);
	}
	
	// 修改用户权限
	@RequestMapping("/updatePower")
	@ResponseBody
	public String updatePower(HttpServletRequest request){
		String username = request.getParameter("username");
		String qx = request.getParameter("qx");
		String cnqx = request.getParameter("cnqx");
		String qxList = request.getParameter("qxList");
		return userService.updatePower(request,username,qx,cnqx,qxList);
	}
	
	// 获取用户权限
	@RequestMapping("/getUserPower")
	@ResponseBody
	public String getUserPower(HttpServletRequest request){
		String username = request.getParameter("username");
		return userService.getUserPower(username);
	}
	
	// 获取日志
	@RequestMapping("/getLog")
	@ResponseBody
	public String getLog(HttpServletRequest request){
		String condition = request.getParameter("condition");
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String username = request.getParameter("username");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		return userService.getLog(request,condition,page,pageSize,username,stime,etime);
	}
	
	@RequestMapping("/getLogExcel")
	@ResponseBody
	public String getLogExcel(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String condition = request.getParameter("condition");
		String username = request.getParameter("username");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		String page = "1";
		String pageSize = "999999";
		String a[] = {"用户名","访问模块","时间"};
        String b[] = {"USERNAME","HANDLE","DBTIME"};
        String gzb = "日志导出";
        List<Map<String, Object>> list = userService.getLogExcel(request,condition,page,pageSize,username,stime,etime);
        downloadAct.download(request,response,a,b,gzb,list);
        return null;
		
	}
	
	@RequestMapping("/makerSuerLogin")
	@ResponseBody
	public String makerSuerLogin(HttpServletRequest request){
		HttpSession session = request.getSession();
		String username = String.valueOf(session.getAttribute("username"));
		if(username != null && !"".equals(username) && !"null".equals(username)){ // 说明处于登录状态
			return "0";
		}else{
			return "1";
		}
	}
	
	@RequestMapping("/loginOut")
	@ResponseBody
	public void loginOut(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.invalidate();
	}}
