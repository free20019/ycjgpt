package mvc.service;

import helper.JacksonUtil;
import helper.LogUtil;

import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 应急指挥处置系统
 * @author xianlehuang
 * @date 2018/12/20 
 */

@Service
public class YjzhServics {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate = null;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
   
   	public String findxll(String table,String field) {
   		String sql = "select distinct "+field+" from "+table+" where "+field+" <> '0'";
   		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
   		return jacksonUtil.toJson(list);
   	}
   	
   	public String findzbb() {
   		LogUtil.log("应急接入-值班", "应急接入-值班");
   		
   		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		Date d = new Date();
		String now = sdf.format(d);
		String sql = "select * from TB_YJZH_ZBB  where zbsj = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,now);
		
		//得到当前一周的日期
		Calendar cal =Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
	    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
		String tj="'"+sdf.format(cal.getTime())+"',";
		for(int i=1;i<7;i++){
			cal.add(Calendar.DATE, 1);
			if(i<6){					
				tj +="'"+sdf.format(cal.getTime())+"',";
			}else{
				tj +="'"+sdf.format(cal.getTime())+"'";
			}
		}
	    String sql1 = "select * from TB_YJZH_ZBB where zbsj in ("+tj+") ORDER BY TO_DATE(REPLACE(REPLACE(zbsj, '月', '-'), '日', ''), 'mm-dd')";
	    List<Map<String, Object>> listweek = jdbcTemplate.queryForList(sql1);
	    Map<String,Object> map = new HashMap<String,Object>();
		map.put("NOW",list);
		map.put("WEEK",listweek);
		return jacksonUtil.toJson(map);
   	}
   	public String fingyjsjjr(String sjzt) {
   		LogUtil.log("应急接入-接入/启动报送/信息生成/信息发布/查询与统计", "应急接入-接入/启动报送/信息生成/信息发布/查询与统计");
   		List <Object> queryList=new  ArrayList<Object>();
   		String tj="";
		if(sjzt!=null&&!sjzt.isEmpty()&&!sjzt.equals("null")&&sjzt.length()>0&&!sjzt.equals("主题")){
			tj +=" and sjzt like ?";
			queryList.add("%" + sjzt + "%");
		}
		String sql = "select * from TB_YJZH_YJSJ where 1=1 ";
		sql += tj;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		return jacksonUtil.toJson(list);
   	}
   	public Integer jrsave(String sjbh, String sjzt, String fsdz, String jwdxx, String bjr, String bjdh, String jjr, String sjjl, String bjnr, String bjfs, String sjjb) {
		String[] xx=null;
		if(jwdxx!=null&&jwdxx.length()>0&&!jwdxx.equals("null")){
			xx=jwdxx.split(",");
		}
		String sql = "insert into TB_YJZH_YJSJ (SJBH,SJZT,TIME,ADDRESS,SJNR,BJR,DJDH,JJR,BJFS,SJJL,PX,PY,SJJB) values ("
				+ "'"+sjbh+"','"+sjzt+"',NOW(),'"+fsdz+"','"+bjnr.trim()+"','"+bjr+"','"+bjdh+"','"+jjr+"','"+bjfs+"','"+sjjl.trim()+"','"+xx[0]+"','"+xx[1]+"','"+sjjb+"')";
		
		LogUtil.log("应急事件接入添加", sql);
		
		String sql1 = "insert into TB_YJZH_YJSJ (SJBH,SJZT,TIME,ADDRESS,SJNR,"
				+ "BJR,DJDH,JJR,BJFS,SJJL,PX,PY,SJJB) values ("
				+ "?,?,NOW(),?,?,?,?,?,?,?,?,?,?)";
		
		Integer msg = jdbcTemplate.update(sql1);
		return msg;
   	}
    public Integer jrUpdate(String sjbh, String sjzt, String fsdz, String jwdxx, String bjr, String bjdh, String jjr, String sjjl, String bjnr, String bjfs, String sjjb, String id) {
		String[] xx=null;
		if(jwdxx!=null&&jwdxx.length()>0&&!jwdxx.equals("null")){
			xx=jwdxx.split(",");
		}
		String sql = "update TB_YJZH_YJSJ set SJBH = '"+sjbh+"',"
				+ "SJZT='"+sjzt+"',TIME=NOW(),ADDRESS='"+fsdz+"'"
				+ ",SJNR='"+bjnr+"',"
				+ "BJR='"+bjr+"',DJDH='"+bjdh+"',JJR='"+jjr+"',"
				+ "BJFS='"+bjfs+"',SJJL='"+sjjl+"',PX='"+xx[0]+"',PY='"+xx[1]+"',SJJB='"+sjjb+"' "
				+ " where id = '"+id+"'";
		
		LogUtil.log("应急事件接入修改", sql);
		
		String sql1 = "update TB_YJZH_YJSJ set SJBH = ?,"
				+ "SJZT=?,TIME=NOW(),ADDRESS=?"
				+ ",SJNR=?,"
				+ "BJR=?,DJDH=?,JJR=?,"
				+ "BJFS=?,SJJL=?,PX=?,PY=?,SJJB=? "
				+ " where id = ?";
		
		Integer msg = jdbcTemplate.update(sql1,sjbh,sjzt,fsdz,bjnr,bjr,bjdh,jjr,bjfs,sjjl,xx[0],xx[1],sjjb,id);
		return msg;
    }
    public Integer jrRzsh(String id) {
    	String sql = "update TB_YJZH_YJSJ set sh='1' where id = ?";
    	LogUtil.log("应急事件接入审核", "update TB_YJZH_YJSJ set sh='1' where id = '"+id+"'");
    	Integer msg = jdbcTemplate.update(sql,id);
    	return msg;
    }
    public Integer jrDelete(String id) {
    	String sql = "delete from TB_YJZH_YJSJ where id = ?";
    	LogUtil.log("应急事件接入删除", "delete from TB_YJZH_YJSJ where id = '"+id+"'");
    	Integer msg = jdbcTemplate.update(sql,id);
    	return msg;
    }
   	public String getAllNames(String table) {
   		LogUtil.log("资源库-应急预案/法律法规/案例库", "资源库-应急预案/法律法规/案例库");
   		
   		String sql = "select * from "+table+"";
   		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
   		return jacksonUtil.toJson(list);
   	}
   	public String getContent(String table,String id) {
   		LogUtil.log("资源库文件内容查询", "资源库文件内容查询");
   		String sql = "select CONTENT from "+table+" where id= ?";
   		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
   		return jacksonUtil.toJson(list);
   	}
    public void saveContent(HttpServletRequest request) {
    	LogUtil.log("资源库文件添加", "资源库文件添加");
    	String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    	Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
    	String table=request.getParameter("table");
    	String name=request.getParameter("name");
    	String content=request.getParameter("content");
    	
    	boolean a = sqlPattern.matcher(table).find();
    	boolean s = sqlPattern.matcher(name).find();
    	
    	if(!a && !s){
    		Writer outStream = null;
        	try {
    			Class.forName("oracle.jdbc.driver.OracleDriver");
    			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@172.18.30.1:9999:orcl", "hzwyc", "tw85461212");
    			con.setAutoCommit(false);
    			Statement st = con.createStatement();
    			st.executeUpdate("insert into "+table+"(NAME, CONTENT) values ('"+name+"', empty_clob())");
    			ResultSet rs = st.executeQuery("select CONTENT from "+table+" where NAME= '"+name+"' for update");
    			if (rs.next()){
    				oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob("CONTENT");
    				outStream = clob.getCharacterOutputStream();
    				char[] c = content.toCharArray();
    				outStream.write(c, 0, c.length);
    			}
    			outStream.flush();
    			outStream.close();
    			con.commit();
    			con.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    public void editContent(HttpServletRequest request) {
    	LogUtil.log("资源库文件修改", "资源库文件修改");
    	String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    	Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
    	
    	String table=request.getParameter("table");
    	String id=request.getParameter("id");
    	String name=request.getParameter("name");
    	String content=request.getParameter("content");
    	
    	boolean a = sqlPattern.matcher(table).find();
    	boolean s = sqlPattern.matcher(id).find();
    	boolean d = sqlPattern.matcher(name).find();
    	
    	if(!a && !s && !d){
    		Writer outStream = null;
        	try {
    			Class.forName("oracle.jdbc.driver.OracleDriver");
    			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@172.18.30.1:9999:orcl", "hzwyc", "tw85461212");
    			con.setAutoCommit(false);
    			Statement st = con.createStatement();
    			st.executeUpdate("update "+table+" set NAME = '"+name+"' ,CONTENT = empty_clob() where ID = '"+id+"'");
    			ResultSet rs = st.executeQuery("select CONTENT from "+table+" where ID= '"+id+"' for update");
    			if (rs.next()){
    				oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob("CONTENT");
    				outStream = clob.getCharacterOutputStream();
    				char[] c = content.toCharArray();
    				outStream.write(c, 0, c.length);
    			}
    			outStream.flush();
    			outStream.close();
    			con.commit();
    			con.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    }
   	public String findclsj(String info) {
   		LogUtil.log("事件处理-指挥调度", "事件处理-指挥调度");
    	List<Map<String, Object>> list =null;
    	if(info.equals("")){
    		String sql = "select t1.COMP_NAME,t1.OWN_NAME,t1.OWN_TEL,t1.VEHI_NO,t2.PX,t2.PY from TB_YJZH_SJCL t1,TB_MDT_STATUS t2 where t1.VEHI_NO = t2.VEHI_NUM";
    		list = jdbcTemplate.queryForList(sql);
    	}else{
    		String sql = "select t1.COMP_NAME,t1.OWN_NAME,t1.OWN_TEL,t1.VEHI_NO,t2.PX,t2.PY from TB_YJZH_SJCL t1,TB_MDT_STATUS t2 where t1.VEHI_NO = t2.VEHI_NUM and t1.VEHI_NO like '%?%'";
    		list = jdbcTemplate.queryForList(sql,info);
    	}
    	return jacksonUtil.toJson(list);
   	}
   	public String findjtsj(String sjbh) {
   		Map<String,Object> map = new HashMap<String,Object>();
    	if(sjbh!=null&&!"".equals(sjbh)){
    		String sql = "select t1.SJBH,t1.SJZT,t1.TIME,t1.ADDRESS,t1.SJNR,t1.PX,t1.PY,t2.count from TB_YJZH_YJSJ t1,(select SJBH,count(1) count from TB_YJZH_SJCL GROUP BY SJBH) t2 where t1.SJBH = t2.SJBH and t1.SJBH = ?";
    		String sql1 = "select * from TB_YJZH_SJCL where SJBH = ?";
    		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,sjbh);
    		List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1,sjbh);
        	map.put("list", list);
    		map.put("list1", list1);
    	}else{
    		String sql = "select t1.SJBH,t1.SJZT,t1.TIME,t1.ADDRESS,t1.SJNR,t1.PX,t1.PY,t2.count from TB_YJZH_YJSJ t1,(select SJBH,count(1) count from TB_YJZH_SJCL GROUP BY SJBH) t2 where t1.SJBH = t2.SJBH";
    		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    		map.put("list", list);
    	}
    	return jacksonUtil.toJson(map);
   	}
}
