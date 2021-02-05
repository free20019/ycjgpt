package mvc.service;

import helper.JacksonUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	protected JdbcTemplate jdbcTemplate = null;
	protected JdbcTemplate jdbcTemplate1 = null;
	protected JdbcTemplate jdbcTemplate2 = null;
	protected JdbcTemplate jdbcTemplate3 = null;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public JdbcTemplate getJdbcTemplate1() {
		return jdbcTemplate1;
	}
	@Autowired
	public void setJdbcTemplate1(JdbcTemplate jdbcTemplate1) {
		this.jdbcTemplate1 = jdbcTemplate1;
	}
	
	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}
	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}
	
	public JdbcTemplate getJdbcTemplate3() {
		return jdbcTemplate3;
	}
	@Autowired
	public void setJdbcTemplate3(JdbcTemplate jdbcTemplate3) {
		this.jdbcTemplate3 = jdbcTemplate3;
	}
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = format.format(new Date());
	private void log(HttpServletRequest request,String model,String s){
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,?)";
//		jdbcTemplate.update(sql,username,model,s,time);
	}

	public String login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
			HttpSession session = request.getSession();
			List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select * from TB_USER where USERNAME = ?",username)); // 判断是否存在这个账号
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    
			String lockFlag = "";
			String failureNum = "";
			String loginDate = "";
			
			if(list != null && list.size() > 0 && !list.isEmpty()){ // 存在这个账号
				List<Map<String, Object>> list1 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select * from TB_USER where USERNAME = ? and PASSWORD = ?", username,password)); // 判断账号密码是否正确
				if(list1 != null && list1.size() > 0 && !list1.isEmpty()){ // 账号密码正确
					lockFlag = String.valueOf(list1.get(0).get("LOCK_FLAG"));// 用户是否被锁,0未锁,1锁定
					loginDate = String.valueOf(list1.get(0).get("LOGIN_DATE")); // 被锁上的时间
					if("1".equals(lockFlag)){ // 用户被锁定
						int time = 0;
						try {
							Date j = formatter.parse(loginDate);
							int between = (int)(System.currentTimeMillis() - j.getTime())/1000;
							time = between/60; // 被锁定时间(分钟)
						} catch (ParseException e) {
							e.printStackTrace();
						}// 被锁时间
						if(time >= 3){ // 用户解锁
							jdbcTemplate.update("update TB_USER set LOCK_FLAG = '0',FAILURE_NUM = '0',LOGIN_DATE = ? where USERNAME = ?",formatter.format(new Date()),username);
							session.setAttribute("username", username);
							session.setAttribute("id", String.valueOf(list1.get(0).get("ID")));
							session.setMaxInactiveInterval(30*60);
							log(request, "用户登录", "用户登录");
							return "登陆成功";
						}else{
							return "用户被锁定,剩余时间："+Integer.valueOf(3 - time)+"分钟";
						}
					}else{// 用户未被锁定
						jdbcTemplate.update("update TB_USER set LOCK_FLAG = '0',FAILURE_NUM = '0',LOGIN_DATE = ? where USERNAME = ?",formatter.format(new Date()),username);
						session.setAttribute("username", username);
						session.setAttribute("id", String.valueOf(list1.get(0).get("ID")));
						session.setMaxInactiveInterval(30*60);
						log(request, "用户登录", "用户登录");
						return "登陆成功";
					}
				}else{ // 密码错误
					List<Map<String, Object>> list2 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select * from TB_USER where USERNAME = ?",username)); // 判断是否存在这个账号
					int time = 0;
					try {
						Date j = formatter.parse(String.valueOf(list2.get(0).get("LOGIN_DATE")));
						int between = (int)(new Date().getTime() - j.getTime())/1000;
						time = between/60; // 被锁定时间(分钟)
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if("1".equals(String.valueOf(list2.get(0).get("LOCK_FLAG")))){ // 用户处于被锁定状态
						if(time >= 3){ // 锁定解除
							jdbcTemplate.update("update TB_USER set LOCK_FLAG = '0',FAILURE_NUM = '0',LOGIN_DATE = ? where USERNAME = ?",formatter.format(new Date()),username);
							List<Map<String, Object>> list3 = jdbcTemplate.queryForList("select * from TB_USER where USERNAME = '?'",username);
							failureNum = String.valueOf(list3.get(0).get("FAILURE_NUM"));// 登录失败次数
							int m = Integer.valueOf(failureNum) + 1;
							jdbcTemplate.update("update TB_USER set FAILURE_NUM = ? , LOGIN_DATE = ? where USERNAME = ?",m,formatter.format(new Date()),username);
							return "密码错误,还有"+Integer.valueOf(3-m)+"次机会";
						}else{ // 用户被锁定并且锁定没有接触,告知解锁时间
							return "用户被锁定,剩余时间："+Integer.valueOf(3 - time)+"分钟";
						}
					}else{ // 用户未被锁定,密码输入错误
						failureNum = String.valueOf(list2.get(0).get("FAILURE_NUM"));// 登录失败次数
						int m = Integer.valueOf(failureNum) + 1;
						jdbcTemplate.update("update TB_USER set FAILURE_NUM = ? , LOGIN_DATE = ? where USERNAME = ?",m,formatter.format(new Date()),username);
						if(m >= 3){ // 登录失败次数
							jdbcTemplate.update("update TB_USER set LOCK_FLAG = '1' where USERNAME = ?",username);
							return "用户被锁定,剩余时间："+Integer.valueOf(3 - time)+"分钟";
						}else{
							return "密码错误,还有"+Integer.valueOf(3-m)+"次机会";
						}
					}
				}
			}else{ // 该账号不存在
				return "该账号不存在";
			}
	}

	public String addUser(HttpServletRequest request, String username, String password, String name, String phone) {
		String sql = "insert into TB_USER (USERNAME,PASSWORD,XM,PHONE) values (?,?,?,?)";
		
		log(request, "添加用户", "insert into TB_USER (USERNAME,PASSWORD,XM,PHONE) values ("+username+","+password+","+name+","+phone+")");
		int msg = jdbcTemplate.update(sql,username,password,name,phone);
		return String.valueOf(msg);
	}

	public String checkUsername(String username) {
		String sql = "select * from TB_USER where USERNAME = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,username);
		if(list != null && list.size() > 0 && !list.isEmpty()){ // 该用户名已存在
			return "1";
		}else{ // 该用户名可用
			return "0";
		}
	}

	public String addPower(HttpServletRequest request, String qx, String cnqx, String username, String qxList) {
		String sql = "update TB_USER set QX = ?,CNQX = ?, QXLIST = ? where USERNAME = ?";
		log(request, "添加用户权限", "update TB_USER set QX = '"+qx+"',CNQX = '"+cnqx+"', QXLIST = '"+qxList+"' where USERNAME = '"+username+"'");
		int msg = jdbcTemplate.update(sql,qx,cnqx,qxList,username);
		return String.valueOf(msg);
	}

	public String getUserList(HttpServletRequest request) {
		log(request, "用户查询", "用户查询");
		String xm  = String.valueOf(request.getParameter("xm"));
		String sql = "select * from TB_USER where 1 = 1 ";
		if(xm != null && !"".equals(xm) &&  !"null".equals(xm)){
			sql +=" and XM  like '%"+ xm +"%'";
		}
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}

	public String updateUser(HttpServletRequest request, String username, String password, String name, String phone, String id) {
		String sql = "update TB_USER set USERNAME = ?,PASSWORD = ?,XM = ?, PHONE = ? where ID = ?";
		log(request, "修改用户信息", "update TB_USER set USERNAME = '"+username+"',PASSWORD = '"+password+"',XM = '"+name+"', PHONE = '"+phone+"' where ID = '"+id+"'");
		int msg = jdbcTemplate.update(sql,username,password,name,phone,id);
		return String.valueOf(msg);
	}

	public String deleteUser(HttpServletRequest request, String username) {
		String sql = "delete from TB_USER where USERNAME = ?";
		log(request, "删除用户", "delete from TB_USER where USERNAME = '"+username+"'");
		int msg = jdbcTemplate.update(sql,username);
		return String.valueOf(msg);
	}

	public String updatePower(HttpServletRequest request, String username, String qx, String cnqx, String qxList) {
		String sql = "update TB_USER set QX = ?,CNQX = ?,QXLIST = ? where USERNAME = ?";
		log(request, "修改用户权限", "update TB_USER set QX = '"+qx+"',CNQX = '"+cnqx+"',QXLIST = '"+qxList+"' where USERNAME = '"+username+"'");
		int msg = jdbcTemplate.update(sql,qx,cnqx,qxList,username);
		return String.valueOf(msg);
	}

	public String getUserPower(String username) {
		String sql = "select QX,XM from TB_USER where USERNAME = ?";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,username));
		return jacksonUtil.toJson(list);
	}

	public String getLog(HttpServletRequest request, String condition, String page, String pageSize, String username, String stime, String etime) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			log(request, "查看日志", "查看日志");
			Map<String,Object> map = new HashMap<String,Object>();
			if("0".equals(condition)){
				List <Object> queryList=new  ArrayList<Object>();
				List <Object> queryList1=new  ArrayList<Object>();
				String sql = "select * from (select A.* from (select * from JGPT_HANDLE_LOG where username != 'null'";  // 网约车监管服务平台
				String countSql = "select count(*) count from JGPT_HANDLE_LOG where username != 'null'";
				if(username != null && !"".equals(username)){
					sql += " and USERNAME = ?";
					countSql += " and USERNAME = ?";
					queryList.add(username);
					queryList1.add(username);
				}
				if(stime != null && !"".equals(stime)){
					sql += " and DBTIME > ? ";
					countSql += " and DBTIME > ? ";
					queryList.add(stime);
					queryList1.add(stime);
				}
				if(etime != null && !"".equals(etime)){
					sql += " and DBTIME < ? ";
					countSql += " and DBTIME < ? ";
					queryList.add(etime);
					queryList1.add(etime);
				}
				
				sql += " ORDER BY DBTIME DESC) A ) AA limit ?, ?";

				queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
				queryList.add(Integer.valueOf(pageSize));

				list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
				List<Map<String, Object>> queryForList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(countSql,queryList1.toArray()));
				map.put("list", list);
				map.put("count", queryForList.get(0).get("count"));
				
			}
			if("1".equals(condition)){
				List <Object> queryList=new ArrayList<Object>();
				List <Object> queryList1=new ArrayList<Object>();
				String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,type TYPE,username USERNAME from ks_handle_log where type = 0"; // 考试管理系统
				String countSql = "select count(*) count from ks_handle_log where type = 0";
				if(username != null && !"".equals(username) && !"null".equals(username)){
					sql += " and username = ?";
					countSql += " and username = ?";
					queryList.add(username);
					queryList1.add(username);
				}
				if(stime != null && !"".equals(stime)){
					sql += " and dbtime > ? ";
					countSql += " and dbtime > ? ";
					queryList.add(stime);
					queryList1.add(stime);
				}
				if(etime != null && !"".equals(etime)){
					sql += " and dbtime < ? ";
					countSql += " and dbtime < ? ";
					queryList.add(etime);
					queryList1.add(etime);
				}
				
				sql += " ORDER BY DBTIME DESC limit ?,?";
				queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
				queryList.add(Integer.valueOf(pageSize));
				
				list = jdbcTemplate2.queryForList(sql,queryList.toArray());
				List<Map<String, Object>> queryForList = jdbcTemplate2.queryForList(countSql,queryList1.toArray());
				map.put("list", list);
				map.put("count", queryForList.get(0).get("count"));
				
			}
			if("2".equals(condition)){
				List <Object> queryList=new ArrayList<Object>();
				List <Object> queryList1=new ArrayList<Object>();
				String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,type TYPE,username USERNAME from ks_handle_log where type = 1"; // 申报信息管理系统
				String countSql = "select count(*) count from ks_handle_log where type = 1";
				if(username != null && !"".equals(username) && !"null".equals(username)){
					sql += " and username = ?";
					countSql += " and username = ?";
					queryList.add(username);
					queryList1.add(username);
				}
				if(stime != null){
					sql += " and dbtime > ? ";
					countSql += " and dbtime > ? ";
					queryList.add(stime);
					queryList1.add(stime);
				}
				if(etime != null){
					sql += " and dbtime < ? ";
					countSql += " and dbtime < ? ";
					queryList.add(etime);
					queryList1.add(etime);
				}
				
				sql += " ORDER BY DBTIME DESC limit ?,?";
				queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
				queryList.add(Integer.valueOf(pageSize));
				
				list = jdbcTemplate2.queryForList(sql,queryList.toArray());
				List<Map<String, Object>> queryForList = jdbcTemplate2.queryForList(countSql,queryList1.toArray());
				map.put("list", list);
				map.put("count", queryForList.get(0).get("count"));
				
			}
			if("3".equals(condition)){
				List <Object> queryList=new ArrayList<Object>();
				List <Object> queryList1=new ArrayList<Object>();
				String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,username USERNAME from tb_log where 1 = 1";
				String countSql = "select count(*) count from tb_log where 1 = 1";
				if(username != null && !"".equals(username) && !"null".equals(username)){
					sql += " and username = ?";
					countSql += " and username = ?";
					queryList.add(username);
					queryList1.add(username);
				}
				if(stime != null){
					sql += " and dbtime > ? ";
					countSql += " and dbtime > ? ";
					queryList.add(stime);
					queryList1.add(stime);
				}
				if(etime != null){
					sql += " and dbtime < ? ";
					countSql += " and dbtime < ? ";
					queryList.add(etime);
					queryList1.add(etime);
				}
				
				sql += " ORDER BY DBTIME DESC limit ?,?";
				queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
				queryList.add(Integer.valueOf(pageSize));
				
				list = jdbcTemplate3.queryForList(sql,queryList.toArray());
				List<Map<String, Object>> queryForList = jdbcTemplate3.queryForList(countSql,queryList1.toArray());
				map.put("list", list);
				map.put("count", queryForList.get(0).get("count"));

			}
			return jacksonUtil.toJson(map);
	}
	
	public List<Map<String, Object>> getLogExcel(HttpServletRequest request, String condition, String page, String pageSize, String username, String stime, String etime) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		log(request, "查看日志", "查看日志");
		Map<String,Object> map = new HashMap<String,Object>();
		if("0".equals(condition)){
			List <Object> queryList=new  ArrayList<Object>();
			List <Object> queryList1=new  ArrayList<Object>();
			String sql = "select * from (select A.* from (select * from JGPT_HANDLE_LOG where 1 = 1";  // 网约车监管服务平台
			String countSql = "select count(*) count from JGPT_HANDLE_LOG where 1 = 1";
			if(username != null){
				sql += " and USERNAME = ?";
				countSql += " and USERNAME = ?";
				queryList.add(username);
				queryList1.add(username);
			}
			if(stime != null){
				sql += " and DBTIME > ? ";
				countSql += " and DBTIME > ? ";
				queryList.add(stime);
				queryList1.add(stime);
			}
			if(etime != null){
				sql += " and DBTIME < ? ";
				countSql += " and DBTIME < ? ";
				queryList.add(etime);
				queryList1.add(etime);
			}
			
			sql += " ORDER BY DBTIME DESC) A ) AA limit ?, ?";

			queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
			queryList.add(Integer.valueOf(pageSize));

			list = jdbcTemplate.queryForList(sql,queryList.toArray());
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(countSql,queryList1.toArray());
			map.put("list", list);
			map.put("count", queryForList.get(0).get("count"));
			
		}
		if("1".equals(condition)){
			List <Object> queryList=new  ArrayList<Object>();
			List <Object> queryList1=new  ArrayList<Object>();
			String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,type TYPE,username USERNAME from ks_handle_log where type = 0"; // 考试管理系统
			String countSql = "select count(*) count from ks_handle_log where type = 0";
			if(username != null){
				sql += " and username = ?";
				countSql += " and username = ?";
				queryList.add(username);
				queryList1.add(username);
			}
			if(stime != null){
				sql += " and dbtime > ? ";
				countSql += " and dbtime > ? ";
				queryList.add(stime);
				queryList1.add(stime);
			}
			if(etime != null){
				sql += " and dbtime < ? ";
				countSql += " and dbtime < ? ";
				queryList.add(etime);
				queryList1.add(etime);
			}
			
			sql += " ORDER BY DBTIME DESC limit ?,?";
			queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
			queryList.add(Integer.valueOf(pageSize));
			
			list = jdbcTemplate2.queryForList(sql,queryList.toArray());
			List<Map<String, Object>> queryForList = jdbcTemplate2.queryForList(countSql,queryList1.toArray());
			map.put("list", list);
			map.put("count", queryForList.get(0).get("count"));
			
		}
		if("2".equals(condition)){
			List <Object> queryList=new  ArrayList<Object>();
			List <Object> queryList1=new  ArrayList<Object>();
			String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,type TYPE,username USERNAME from ks_handle_log where type = 1"; // 申报信息管理系统
			String countSql = "select count(*) count from ks_handle_log where type = 1";
			if(username != null){
				sql += " and username = ?";
				countSql += " and username = ?";
				queryList.add(username);
				queryList1.add(username);
			}
			if(stime != null){
				sql += " and dbtime > ? ";
				countSql += " and dbtime > ? ";
				queryList.add(stime);
				queryList1.add(stime);
			}
			if(etime != null){
				sql += " and dbtime < ? ";
				countSql += " and dbtime < ? ";
				queryList.add(etime);
				queryList1.add(etime);
			}
			
			sql += " ORDER BY DBTIME DESC limit ?,?";
			queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
			queryList.add(Integer.valueOf(pageSize));
			
			list = jdbcTemplate3.queryForList(sql,queryList.toArray());
			List<Map<String, Object>> queryForList = jdbcTemplate3.queryForList(countSql,queryList1.toArray());
			map.put("list", list);
			map.put("count", queryForList.get(0).get("count"));
			
		}
		if("3".equals(condition)){
			List <Object> queryList=new ArrayList<Object>();
			List <Object> queryList1=new ArrayList<Object>();
			String sql = "select content CONTENT,dbtime DBTIME,handle HANDLE,id ID,username USERNAME from tb_log where 1 = 1";
			String countSql = "select count(*) count from tb_log where 1 = 1";
			if(username != null && !"".equals(username) && !"null".equals(username)){
				sql += " and username = ?";
				countSql += " and username = ?";
				queryList.add(username);
				queryList1.add(username);
			}
			if(stime != null){
				sql += " and dbtime > ? ";
				countSql += " and dbtime > ? ";
				queryList.add(stime);
				queryList1.add(stime);
			}
			if(etime != null){
				sql += " and dbtime < ? ";
				countSql += " and dbtime < ? ";
				queryList.add(etime);
				queryList1.add(etime);
			}
			
			sql += " ORDER BY DBTIME DESC limit ?,?";
			queryList.add((Integer.valueOf(page)-1)*Integer.valueOf(pageSize));
			queryList.add(Integer.valueOf(pageSize));
			
			list = jdbcTemplate3.queryForList(sql,queryList.toArray());
			List<Map<String, Object>> queryForList = jdbcTemplate3.queryForList(countSql,queryList1.toArray());
			map.put("list", list);
			map.put("count", queryForList.get(0).get("count"));

		}
		return list;
}

}
