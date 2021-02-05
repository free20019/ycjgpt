package mvc.service;

import helper.Client;
import helper.GeometryHandler;
import helper.HanyuPinyinUtil;
import helper.JacksonUtil;
import helper.LogUtil;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.entity.TbCompany;
import mvc.mapper.mapDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Array;
import com.vividsolutions.jts.geom.Geometry;


@Service
public class mapServer{
	protected JdbcTemplate jdbcTemplate = null;
	protected JdbcTemplate jdbcTemplate1 = null;
	protected JdbcTemplate jdbcTemplate2 = null;
	@Autowired
	protected JdbcTemplate jdbcTemplate4 = null;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	Logger logger = Logger.getLogger(this.getClass());
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

    public String getRequest(HttpServletRequest request, String field) {
        return String.valueOf(request.getParameter(field));
    }
	public String getMapLayered(HttpServletRequest request){
		Integer level =  Integer.parseInt(request.getParameter("level"));
		String comp_id = request.getParameter("comp_id");
		int l = 6;
		switch (level) {
		case 0:
			l = 0;
			break;
		case 1:
			l = 0;
			break;
		case 2:
			l = 0;
			break;
		case 3:
			l = 1;
			break;
		case 4:
			l = 1;
			break;
		case 5:
			l = 2;
			break;
		case 6:
			l = 2;
			break;
		case 7:
			l = 3;
			break;
		case 8:
			l = 3;
			break;
		default:
			break;
		}
		String sql = "";
		sql = "select * from TB_CLUSTER_COMPANY where areatype = ? and companyid = ?";
		System.out.println(sql+" "+l+"  "+comp_id);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,l,comp_id));
		if(level == 0||level == 1||level == 2){
			String num = "select * from TB_ONLINE where companyid = ?";
			List<Map<String, Object>> numlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(num,comp_id));
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("AREANUM", numlist.get(0).get("ONLINE_NUM"));
			}
		}
		return jacksonUtil.toJson(list);
	}
	public String excessive(){
		return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select * from T_MDT_STATUS")));
	}
	public String test(){
		String sql = "select * from tb_company";
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String getGet(HttpServletRequest request) {
		String px = request.getParameter("px");
		String py = request.getParameter("py");
		String level = request.getParameter("level");
		String comp_id = request.getParameter("comp_id");
		String type = request.getParameter("type");//0：实时监控中最大层点   1：区域监管中区域内车辆信息
		String areaid = request.getParameter("areaid");
		
		
		String result = "";
    		try {
				//http://11.0.15.148:6001/api/?longi=120.2003&lati=30.401&zoom=7&companyid=all
				URL url = null;
				if(type.equals("0")) url = new URL("http://11.0.15.148:6001/api/?longi="+px+"&lati="+py+"&zoom="+level+"&companyid="+comp_id);
				else if(type.equals("1"))  url = new URL("http://11.0.15.148:6001/api/?areaid="+areaid+"&companyid="+comp_id);
				else if(type.equals("2")) url = new URL("http://11.0.15.148:6001/api/?id="+areaid);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1096.1 Safari/536.6");
				conn.setRequestProperty("content-type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				conn.setDoOutput(true); // 需要输出
				if (conn.getResponseCode() == 302) {
					System.out.println(302);
					return null;
				}
				if (conn.getResponseCode() == 200) {
					System.out.println(200);
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				StringBuffer sb = new StringBuffer();
				String s = "";
				while ((s = rd.readLine()) != null) {
					sb.append(s);
				}
				if (sb.length() == 0) {
					sb.append("[]");
				}
				result = sb.toString();
				// System.out.println(result);
				rd.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
	}

	public String getHistor(HttpServletRequest request){
		LogUtil.log("营运动态监测-历史轨迹回放", "营运动态监测-历史轨迹回放");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		String vehi_no = request.getParameter("vehi_no");
		System.out.println(vehi_no);
		String vhic = vehi_no.split("\\(")[0];
		String name = vehi_no.split("\\(")[1].replaceAll("\\)", "");
		String table = stime.substring(2, 4) + stime.substring(5, 7);
		String sql = "select t.*,c.abb_name from tb_taxi_gps_"+table+" t,TB_GLOBAL_COMPANY c"
				+ " where CONVERT(c.company_id USING utf8) COLLATE utf8_unicode_ci= t.companyid and"
				+ " ct >= ? and"
				+ " ct <= ? and"
				+ " t.vehicleno = ? and c.abb_name = ? and longitude >=100 and longitude<=121 order by ct";
		System.out.println(sql+ " "+stime+"  "+etime+"  "+vehi_no+"  "+name+vhic);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,stime,etime,vhic,name));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("POSITIONTIME",list.get(i).get("CT"));
		}
//		Set<String> set = new HashSet<String>();
//		for (int i = 0; i < list.size(); i++) {
//			set.add(String.valueOf(list.get(i).get("ORDERID")));
//		}
//		String orderid = "";
//		for (String value : set) {
//		     orderid += "'" + value + "',";
//		 }
		List<Map<String, Object>> order = new ArrayList<Map<String, Object>>();
		if(list.size()>1){
			String ordersql = "select o.*,t.abb_name from TB_OPERATEPAY_"+table+" o,TB_GLOBAL_COMPANY t where o.companyid=t.company_id" +
//					" and o.orderid in ("+orderid.substring(0, orderid.length()-1)+")";
					" and o.VehicleNo = '"+vhic+"' and o.DestTime >= '"+stime+"' and o.DepTime <= '"+etime+"'";
			System.out.println(ordersql);
			order = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ordersql));

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("monitor", list);
		map.put("order", order);
		return jacksonUtil.toJson(map);
	}
	public String getOrder(HttpServletRequest request){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String table = time.substring(2, 4) + time.substring(5, 7);
		String stime = sdf.format(new Date(new Date().getTime() - 1000*60*10));
		String sql = "select o.*,t.abb_name from TB_OPERATEPAY_"+table+" o,TB_GLOBAL_COMPANY t where o.companyid=t.company_id "
				+ "and deptime>='"+stime+"'";
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}
	public String getVhic(HttpServletRequest request){
		String vhic = request.getParameter("vhic");
		String sql = "select VEHICLENO,abb_name from tb_taxi_gps_realtime g,TB_GLOBAL_COMPANY c" +
                " where g.companyid=c.company_id and vehicleno like ?";
		System.out.println(sql+"  "+"%"+vhic.toUpperCase()+"%");
		return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,"%"+vhic.toUpperCase()+"%")));
	}
	public String getCompany(){
//		String sql = "select  g.company_id,g.abb_name,g.icon,o.* from tb_global_company g" +
//				" left join tb_online o on o.companyid = g.company_id where g.operating = '0'";
		String sql = "select  g.company_id,g.abb_name from tb_global_company g ";
		List<Map<String, Object>> list = jacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
//		String allsql = "select * from tb_online where companyid = 'all'";
//		List<Map<String, Object>> alllist = jacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(allsql));
		List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
		DecimalFormat df = new DecimalFormat("0.00");
//		for (int i = 0; i < alllist.size(); i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("ABB_NAME", "全部");
//			map.put("COMPANYID", "all");
//			map.put("ONLINE_NUM", alllist.get(i).get("ONLINE_NUM"));
//			map.put("HEAVY_NUM", alllist.get(i).get("HEAVY_NUM"));
//			map.put("EMAPY_NUM", alllist.get(i).get("EMAPY_NUM"));
//			map.put("ICON", "icon-clsh");
//			map.put("LV", df.format(Double.parseDouble(String.valueOf(alllist.get(i).get("HEAVY_NUM")))/Double.parseDouble(String.valueOf(alllist.get(i).get("ONLINE_NUM")))*100)+"%");
//			list1.add(map);
//		}
		for (int i = 0; i < list.size(); i++) {
				if(String.valueOf(list.get(i).get("COMPANYID")).equals("null")) {
					list.get(i).put("ONLINE_NUM", 0);
					list.get(i).put("HEAVY_NUM", 0);
					list.get(i).put("EMAPY_NUM", 0);
					list.get(i).put("LV", "0%");
				}else {
//					list.get(i).put("LV", df.format(Double.parseDouble(String.valueOf(list.get(i).get("HEAVY_NUM")))/Double.parseDouble(String.valueOf(list.get(i).get("ONLINE_NUM")))*100)+"%");
				}
				list1.add(list.get(i));
		}
		return jacksonUtil.toJson(list1);
	}
	public String getVehicle(HttpServletRequest request){
		//		LogUtil.log("营运动态监测-车辆动态监控", "营运动态监测-车辆动态监控");
		String vhic = String.valueOf(request.getParameter("vhic"));
		String name = String.valueOf(request.getParameter("name"));
		int page = Integer.parseInt(String.valueOf(request.getParameter("page")));
		int pageSize = 1000000;
		long time =  System.currentTimeMillis() - 1000*60*30;
		Date date=new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(date);
		String sql = "select * from (select t.*,g.abb_name,(select count(1) from TB_VEHICLE_GPS_STATUS t,"
				+ " TB_GLOBAL_COMPANY g where t.companyid = g.company_id ";
		if(!vhic.equals("null")&&!vhic.isEmpty()){
			sql += " and vehicleno like '%"+vhic+"%'";
		}
		if(!name.equals("全部")&&!name.isEmpty()){
			sql += " and abb_name = '"+name+"'";
		}
		sql += " " +
//						" and positiontime > '"+d+"' and vehicleno like '宁A%'" +
				") c from TB_VEHICLE_GPS_STATUS t, TB_GLOBAL_COMPANY g" +
				" where t.companyid = g.company_id" +
//						" and positiontime >'"+d+"' and vehicleno like '宁A%'" +
				"";
		if(!vhic.equals("null")&&!vhic.isEmpty()){
			sql += "and vehicleno like '%"+vhic+"%'";
		}
		if(!name.equals("全部")&&!name.isEmpty()){
			sql += " and abb_name = '"+name+"'";
		}
		sql += " ) tt limit "+(page-1)*pageSize+","+pageSize;
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
//			if(String.valueOf(list.get(i).get("COMPANYID")).equals("yidao"))
//				map.put("icon", "icon-ydzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("caocao"))
//				map.put("icon", "icon-cczc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("wanshun"))
//				map.put("icon", "icon-wszc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shouyue"))
//				map.put("icon", "icon-sqzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shenzhou"))
//				map.put("icon", "icon-szzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("didi"))
//				map.put("icon", "icon-ddzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("yangguangchedao"))
//				map.put("icon", "icon-ygzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("aa"))
//				map.put("icon", "icon-aazc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("all"))
//				map.put("icon", "icon-clsh");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("TJHT"))
//				map.put("icon", "icon-xcyc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("hhyc"))
//				map.put("icon", "icon-hhdc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tongcheng"))
//				map.put("icon", "icon-mzdc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("dccx"))
//				map.put("icon", "icon-dccx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiehua"))
//				map.put("icon", "icon-xhcx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("weixing"))
//				map.put("icon", "icon-ycx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tmcx"))
//				map.put("icon", "icon-tmcx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiangdao"))
//				map.put("icon", "icon-xdcx");
//			map.put("direction", list.get(i).get("DIRECTION"));
			map.put("bizStatus", list.get(i).get("BIZSTATUS"));
			map.put("speed", list.get(i).get("SPEED"));
			map.put("mdt_no", list.get(i).get("COMPANYID"));
			map.put("dateTime", list.get(i).get("POSITIONTIME"));
			map.put("longi", list.get(i).get("LONGITUDE"));
			map.put("vehino", list.get(i).get("VEHICLENO"));
			map.put("vehName", list.get(i).get("ABB_NAME"));
			map.put("lati", list.get(i).get("LATITUDE"));
			map.put("TYPE", String.valueOf(list.get(i).get("POSITIONTIME")).equals("null")?true:jisuan(String.valueOf(list.get(i).get("POSITIONTIME"))));
//			map.put("C", list.get(i).get("C"));
			map.put("legal", list.get(i).get("LEGAL"));
//			map.put("legalupdatetime", String.valueOf(list.get(i).get("LEGALUPDATETIME")));
			list1.add(map);
//			System.out.println(map.get("icon"));
		}
		return jacksonUtil.toJson(list1);


////		LogUtil.log("营运动态监测-车辆动态监控", "营运动态监测-车辆动态监控");
//		String vhic = String.valueOf(request.getParameter("vhic"));
//		String name = String.valueOf(request.getParameter("name"));
//		int page = Integer.parseInt(String.valueOf(request.getParameter("page")));
//		int pageSize = 1000000;
//		long time =  System.currentTimeMillis() - 1000*60*30;
//		Date date=new Date(time);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String d = sdf.format(date);
//		String sql = "select * from (select t.*,g.abb_name,(select count(1) from TB_VEHICLE_GPS_STATUS t,"
//				+ " TB_GLOBAL_COMPANY g where t.companyid = g.company_id ";
//				if(!vhic.equals("null")&&!vhic.isEmpty()){
//					sql += "and vehicleno like '%"+vhic+"%'";
//				}
//				if(!name.equals("全部")&&!name.isEmpty()){
//					sql += " and abb_name = '"+name+"'";
//				}
//				sql += " " +
////						" and positiontime > '"+d+"' and vehicleno like '宁A%'" +
//						") c from TB_VEHICLE_GPS_STATUS t, TB_GLOBAL_COMPANY g" +
//						" where t.companyid = g.company_id" +
////						" and positiontime >'"+d+"' and vehicleno like '宁A%'" +
//						"";
//				if(!vhic.equals("null")&&!vhic.isEmpty()){
//					sql += "and vehicleno like '%"+vhic+"%'";
//				}
//				if(!name.equals("全部")&&!name.isEmpty()){
//					sql += " and abb_name = '"+name+"'";
//				}
//				sql += " ) tt limit "+(page-1)*pageSize+","+pageSize;
//		System.out.println(sql);
//		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
//		List<Map<String, Object>> list1 = new ArrayList<>();
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			if(String.valueOf(list.get(i).get("COMPANYID")).equals("yidao"))
//				map.put("icon", "icon-ydzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("caocao"))
//				map.put("icon", "icon-cczc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("wanshun"))
//				map.put("icon", "icon-wszc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shouyue"))
//				map.put("icon", "icon-sqzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shenzhou"))
//				map.put("icon", "icon-szzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("didi"))
//				map.put("icon", "icon-ddzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("yangguangchedao"))
//				map.put("icon", "icon-ygzc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("aa"))
//				map.put("icon", "icon-aazc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("all"))
//				map.put("icon", "icon-clsh");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("TJHT"))
//				map.put("icon", "icon-xcyc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("hhyc"))
//				map.put("icon", "icon-hhdc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tongcheng"))
//				map.put("icon", "icon-mzdc");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("dccx"))
//				map.put("icon", "icon-dccx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiehua"))
//				map.put("icon", "icon-xhcx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("weixing"))
//				map.put("icon", "icon-ycx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tmcx"))
//				map.put("icon", "icon-tmcx");
//			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiangdao"))
//				map.put("icon", "icon-xdcx");
//			map.put("direction", list.get(i).get("DIRECTION"));
//			map.put("bizStatus", list.get(i).get("BIZSTATUS"));
//			map.put("speed", list.get(i).get("SPEED"));
//			map.put("mdt_no", list.get(i).get("COMPANYID"));
//			map.put("dateTime", list.get(i).get("POSITIONTIME"));
//			map.put("longi", list.get(i).get("LONGITUDE"));
//			map.put("vehino", list.get(i).get("VEHICLENO"));
//			map.put("vehName", list.get(i).get("ABB_NAME"));
//			map.put("lati", list.get(i).get("LATITUDE"));
//			map.put("TYPE", String.valueOf(list.get(i).get("POSITIONTIME")).equals("null")?true:jisuan(String.valueOf(list.get(i).get("POSITIONTIME"))));
//			map.put("C", list.get(i).get("C"));
//			map.put("legal", list.get(i).get("LEGAL"));
//			map.put("legalupdatetime", String.valueOf(list.get(i).get("LEGALUPDATETIME")));
//			list1.add(map);
////			System.out.println(map.get("icon"));
//		}
//		return jacksonUtil.toJson(list1);
	}
	/*false 在线 ；  true  离线*/
	public static boolean jisuan(String date1){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double result=0;
		try {
			Date start = sdf.parse(date1);
			Date end = new Date();
			long cha = end.getTime() - start.getTime(); 
			result = cha * 1.0 / (1000 * 60); 
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(result<=30){ 
			return false; 
		}else{
			return true; 
		} 
    }
	public String getArea(){
//		LogUtil.log("营运动态监测-特定区域监测", "营运动态监测-特定区域监测");
		String sql = "select * from tb_area order by cast(area_id as unsigned int) desc";
		List<Map<String, Object>> area = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(area);
	}
	public String addArea(HttpServletRequest request) {
		String area_name = request.getParameter("area_name");
		String area_ms = request.getParameter("area_ms");
		String area_zb = request.getParameter("area_zb");
		String area_size = request.getParameter("area_size");
		String area_num = request.getParameter("area_num");
		String area_avg_num = request.getParameter("area_avg_num");
		String sql = "insert into tb_area (area_name,area_ms,area_zb,area_size,area_num,area_avg_num) values (?,?,?,?,?,?)";
		int c = jdbcTemplate.update(sql,area_name,area_ms,area_zb,area_size,area_num,area_avg_num);
//		LogUtil.log("营运动态监测-特定区域监测添加", "insert into tb_area (area_name,area_ms,area_zb,area_size) values ('"+area_name+",'"+area_ms+"','"+area_zb+"','"+area_size+"')");
		return jacksonUtil.toJson(c);
	}

	public String editArea(HttpServletRequest request) {
		String area_name = request.getParameter("area_name");
		String area_ms = request.getParameter("area_ms");
		String area_zb = request.getParameter("area_zb");
		String area_size = request.getParameter("area_size");
		String area_num = request.getParameter("area_num");
		String area_avg_num = request.getParameter("area_avg_num");
		String area_id = request.getParameter("area_id");
		String sql = "update tb_area set area_name = ?,area_ms = ?,area_zb = ?,area_size = ?,area_num = ?,area_avg_num = ? where area_id = ?";
		int c = jdbcTemplate.update(sql,area_name,area_ms,area_zb,area_size,area_num,area_avg_num,area_id);
//		LogUtil.log("营运动态监测-特定区域监测修改", "update tb_area set area_name = '"+area_name+"',area_ms = '"+area_ms+"',area_zb = '"+area_zb+"',area_size = '"+area_size+"' where area_id = '"+area_id+"'");
		return jacksonUtil.toJson(c);
	}
	public String delArea(HttpServletRequest request) {
		String area_id = request.getParameter("area_id");
		String sql = "delete from tb_area where area_id = ?";
		int c = jdbcTemplate.update(sql,area_id);
//		LogUtil.log("营运动态监测-特定区域监测删除", "delete from tb_area where area_id = '"+area_id+"'");
		return jacksonUtil.toJson(c);
	}
	public String getFance(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		String area_id = request.getParameter("areaid");
		String sql = "select t.*,g.abb_name from TB_AREA_IN_OUT t,TB_GLOBAL_COMPANY G WHERE T.COMPANY_ID=CONVERT(G.COMPANY_ID USING utf8) COLLATE utf8_unicode_ci and area_id = ?"
				+ " and positiontime >= ?"
				+ " and positiontime <= ? order by positiontime desc";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,area_id,date+" 00:00:00",date+" 23:59:59"));
		System.out.println(sql+"  "+area_id+" " +date+" 00:00:00"+" "+date+" 23:59:59");
		return jacksonUtil.toJson(list);
	}
	public String getDynamic(HttpServletRequest request){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String etime = sdf.format(new Date());
		String stime = request.getParameter("time");
		String vehi_no = request.getParameter("vehi_no");
		List<Map<String, Object>> list = new ArrayList<>();
		String vhic = "", compname = "";
		try {
			vhic = vehi_no.split("\\(")[0];
			compname = vehi_no.split("\\(")[1].substring(0,vehi_no.split("\\(")[1].length()-1);
		} catch (Exception e) {
			return jacksonUtil.toJson(list);
		}
		String table = stime.substring(2, 4) + stime.substring(5, 7);
		String sql = "select t.*,c.abb_name from TB_POSITIONVEHICLE_"+table+" t,TB_GLOBAL_COMPANY c"
				+ " where c.company_id=t.companyid and"
				+ " positiontime >= ? and"
				+ " positiontime <= ? and"
				+ " t.vehicleno = ? and abb_name = ? and longitude >=120 and longitude<1etAnalyze21 order by positiontime";
		System.out.println(sql+" "+stime+" "+etime);
		list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,stime,etime,vhic,compname));
		return jacksonUtil.toJson(list);
	}
	public String getNewVehicle(HttpServletRequest request){
		String vehi_no = request.getParameter("vehi_no");
		String sql = "select t.*,c.abb_name from TB_VEHICLE_GPS_STATUS t,"
				+ "TB_GLOBAL_COMPANY c where t.companyid=c.company_id and"
				+ " vehicleno = ? order by positiontime desc";
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,vehi_no));
		return jacksonUtil.toJson(list);
	}
	public String compSelect(){
//		String sql = "select distinct(companyid),g.abb_name from TB_ORDER_PAY_STATIS_1911 o,tb_global_company g where o.companyid=g.company_id";
		String sql = "select distinct company_id companyid,g.abb_name from tb_global_company g";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		List<Map<String, Object>> jg = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("COMPANYID", "0");
		map.put("ABB_NAME", "所有平台");
		jg.add(map);
		for (int i = 0; i < list.size(); i++) {
			jg.add(list.get(i));
		}
		return jacksonUtil.toJson(jg);
	}
	public String qyrjyytj(HttpServletRequest request){
		String time = String.valueOf(request.getParameter("time"));
		String type = String.valueOf(request.getParameter("type"));
		String table = time.substring(2, 4) + time.substring(5, 7);
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select abb_name,a.*,b.c hycl,c.c hyjsy,'"+time+"' time from TB_GLOBAL_COMPANY g,(select companyid"
				+ ",sum(factprice) zys,sum(drivemile) zxslc,sum(order_num) zdd,round(sum(drivetime)/60,2) zzksc from TB_ORDER_PAY_STATIS_"+table
				+ " where date_format(dbtime,'%Y-%m') =? group by companyid) a,(select companyid,count(1) c"
				+ " from (select vehicleno,companyid from TB_ORDER_PAY_STATIS_"+table+" where date_format(dbtime,'%Y-%m') =?"
				+ " group by vehicleno,companyid)group by companyid) b,(select companyid,count(1) c from (select licenseid"
				+ ",companyid from TB_ORDER_PAY_STATIS_"+table+" where date_format(dbtime,'%Y-%m') =? group by licenseid"
				+ ",companyid)group by companyid) c where g.company_id = a.companyid and g.company_id = b.companyid and"
				+ " g.company_id = c.companyid ";
		queryList.add(time);
		queryList.add(time);
		queryList.add(time);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
		List<Map<String, Object>> list1 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select count(1) zdd, sum(factprice) zys, sum(drivemile) zxslc,"
				+ " round(sum(drivetime) / 60, 2) zzksc from TB_ORDER_PAY_STATIS_"+table+" where date_format(dbtime,'%Y-%m') = ?", time));
		String hycl = jdbcTemplate.queryForObject("select count(distinct(vehicleno)) hycl from TB_ORDER_PAY_STATIS_"+table+""
				+ " where to_char(dbtime, 'yyyy-mm') = '"+time+"'", String.class);
		String hyjsy = jdbcTemplate.queryForObject("select count(distinct(licenseid)) hyjsy from TB_ORDER_PAY_STATIS_"+table+""
				+ " where to_char(dbtime, 'yyyy-mm') = '"+time+"'", String.class);
		if(list1.size() ==0){
			return jacksonUtil.toJson(list);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat df = new DecimalFormat("0.00");
		map.put("ABB_NAME", "行业平均");
		map.put("COMPANYID", "0");
		map.put("ZDD", df.format(Double.parseDouble(String.valueOf(list1.get(0).get("ZDD")))/list.size()));
		map.put("ZYS", df.format(Double.parseDouble(String.valueOf(list1.get(0).get("ZYS")))/list.size()));
		map.put("ZXSLC", df.format(Double.parseDouble(String.valueOf(list1.get(0).get("ZXSLC")))/list.size()));
		map.put("ZZKSC", df.format(Double.parseDouble(String.valueOf(list1.get(0).get("ZZKSC")))/list.size()));
		map.put("HYCL", df.format(Double.parseDouble(hycl)/list.size()));
		map.put("HYJSY", df.format(Double.parseDouble(hyjsy)/list.size()));
		map.put("TIME", time);
		List<Map<String, Object>> jg = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			jg.add(list.get(i));
		}
		jg.add(map);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(type.equals("1") || type.equals("3")) {
			String month = sdf.format(new Date());
			int day = 0;
			if(month.equals(time)){
				Calendar ca = Calendar.getInstance();
		        day=ca.get(Calendar.DATE);//获取日
			}else {
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(sdf.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			for(int i=0; i<jg.size(); i++) {
				jg.get(i).put("ZDD", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZDD")).equals("null")?"0":String.valueOf(jg.get(i).get("ZDD")))/day));
				jg.get(i).put("ZYS", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZYS")).equals("null")?"0":String.valueOf(jg.get(i).get("ZYS")))/day));
				jg.get(i).put("ZXSLC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZXSLC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZXSLC")))/day));
				jg.get(i).put("ZZKSC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZZKSC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZZKSC")))/day));
			}
			if(type.equals("3")) {
				for(int i=0; i<jg.size(); i++) {
					jg.get(i).put("ZDD", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZDD")).equals("null")?"0":String.valueOf(jg.get(i).get("ZDD")))/
							Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
					jg.get(i).put("ZYS", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZYS")).equals("null")?"0":String.valueOf(jg.get(i).get("ZYS")))/
							Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
					jg.get(i).put("ZXSLC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZXSLC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZXSLC")))/
							Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
					jg.get(i).put("ZZKSC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZZKSC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZZKSC")))/
							Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
				}
			}
		}else if(type.equals("4")) {
			for(int i=0; i<jg.size(); i++) {
				jg.get(i).put("ZDD", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZDD")).equals("null")?"0":String.valueOf(jg.get(i).get("ZDD")))/
						Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
				jg.get(i).put("ZYS", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZYS")).equals("null")?"0":String.valueOf(jg.get(i).get("ZYS")))/
						Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
				jg.get(i).put("ZXSLC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZXSLC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZXSLC")))/
						Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
				jg.get(i).put("ZZKSC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZZKSC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZZKSC")))/
						Double.parseDouble(String.valueOf(jg.get(i).get("HYCL")).equals("null")?"0":String.valueOf(jg.get(i).get("HYCL")))));
			}
		}
		return jacksonUtil.toJson(jg);
	}
	public String qyrjdcfx(HttpServletRequest request){
		String name = String.valueOf(request.getParameter("name"));
		String time = String.valueOf(request.getParameter("time"));
		String type = String.valueOf(request.getParameter("type"));
		int page = Integer.parseInt(String.valueOf(request.getParameter("page")));
		int pageSize = Integer.parseInt(String.valueOf(request.getParameter("pageSize")));
		String table = time.substring(2, 4) + time.substring(5, 7);
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select s.*,g.abb_name from (select tt.*,(select count(1) from (select count(1) c from"
				+ " TB_ORDER_PAY_STATIS_"+table+" where 1=1";
		if(!name.equals("0")){
			sql += " and companyid = ?";
			queryList.add(name);
		}
		sql += "group by vehicleno,companyid)) c from ( select"
				+ " t.* from ( select vehicleno,companyid,sum(drivemile) zzklc, round(sum(drivetime) / 60, 2) zzksj"
				+ ",sum(factprice) zys, sum(order_num) zdd from TB_ORDER_PAY_STATIS_"+table+" where 1=1 ";
		if(!name.equals("0")&&!name.equals("null")&&name.length()>0){
			sql += " and companyid = ?";
			queryList.add(name);
		}
		sql += "group by vehicleno,companyid"
				+ " order by companyid,vehicleno) t ) tt limit"+((page-1)*pageSize)+","+pageSize+") s,tb_global_company g"
				+ " where s.companyid=g.company_id";
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
		String sum = "select sum(drivemile) zxslc,round(sum(drivetime) / 60, 2) zzksc,sum(factprice) zys"
				+ ",sum(order_num) zdd from TB_ORDER_PAY_STATIS_"+table+" t where 1=1";
		List <Object> queryList1=new  ArrayList<Object>();
		if(!name.equals("0")&&!name.equals("null")&&name.length()>0){
			sum += " and companyid = ?";
			queryList1.add(name);
		}
		List<Map<String, Object>> sumCount = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sum,queryList1.toArray()));
		int countVhic = jdbcTemplate.queryForObject("select count(distinct(vehicleno)) hycl from TB_ORDER_PAY_STATIS_"+table+"", Integer.class);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> jg = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			jg.add(list.get(i));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		map.put("ABB_NAME", "行业平均");
		map.put("VEHICLENO", "");
		map.put("ZDD", df.format(Double.parseDouble(String.valueOf(sumCount.get(0).get("ZDD")))/countVhic));
		map.put("ZYS", df.format(Double.parseDouble(String.valueOf(sumCount.get(0).get("ZYS")))/countVhic));
		map.put("ZZKLC", df.format(Double.parseDouble(String.valueOf(sumCount.get(0).get("ZXSLC")))/countVhic));
		map.put("ZZKSJ", df.format(Double.parseDouble(String.valueOf(sumCount.get(0).get("ZZKSC")))/countVhic));
		jg.add(map);
		if(type.equals("0")) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			int day = 0;
			String month = sdf1.format(new Date());
			if(month.equals(time)){
				Calendar ca = Calendar.getInstance();
		        day=ca.get(Calendar.DATE);//获取日
			}else {
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(sdf1.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			for (int i = 0; i < jg.size(); i++) {
				jg.get(i).put("ZDD", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZDD")).equals("null")?"0":String.valueOf(jg.get(i).get("ZDD")))/day));
				jg.get(i).put("ZYS", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZYS")).equals("null")?"0":String.valueOf(jg.get(i).get("ZYS")))/day));
				jg.get(i).put("ZXSLC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZXSLC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZXSLC")))/day));
				jg.get(i).put("ZZKSC", df.format(Double.parseDouble(String.valueOf(jg.get(i).get("ZZKSC")).equals("null")?"0":String.valueOf(jg.get(i).get("ZZKSC")))/day));
			}

		}
		return jacksonUtil.toJson(jg);
	}
	public String zdmbcx(HttpServletRequest request){
		String name = String.valueOf(request.getParameter("name"));
		String vhic = String.valueOf(request.getParameter("vhic"));
		String comp = String.valueOf(request.getParameter("comp"));
		String stime = String.valueOf(request.getParameter("stime"));
		String etime = String.valueOf(request.getParameter("etime"));
		String table = stime.substring(2, 4) + stime.substring(5, 7);
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select vehicleno,companyid,drivername,licenseid,sum(drivemile) zzklc, round(sum(drivetime) / 60, 2) zzksj"
				+ ",sum(factprice) zys, sum(order_num) zdd,abb_name"
				+ " from TB_ORDER_PAY_STATIS_"+table+" o,tb_global_company g where o.companyid = g.company_id "
				+ " and dbtime >= ? and dbtime <= ?";
		queryList.add(stime + " 00:00:00");
		queryList.add(etime + " 23:59:59");
		if(!comp.equals("0")&&!comp.equals("null")&&comp.length()>0){
			sql += " and companyid = ?";
			queryList.add(comp);
		}
		if(!vhic.equals("0")&&!vhic.equals("null")&&vhic.length()>0){
			sql += " and vehicleno like ?";
			queryList.add("%"+vhic+"%");
		}
		if(!name.equals("0")&&!name.equals("null")&&name.length()>0){
			sql += " and (drivername like ? or licenseid like ?) ";
			queryList.add("%"+name+"%");
			queryList.add("%"+name+"%");
		}
		sql += " group by vehicleno,drivername,licenseid,companyid,abb_name";
		System.out.println(queryList);
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
		return jacksonUtil.toJson(list);
	}
	public String businessStart(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String orderid = String.valueOf(request.getParameter("orderid"));
		String table = time.substring(2, 4) + time.substring(5, 7);
		String sql = "select t.*,g.abb_name from tb_operatedepart_"+table+" t,tb_global_company g where"
				+ " t.companyid = g.company_id and orderid = ?";
		System.out.println(sql+"  "+orderid);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql , orderid));
		return jacksonUtil.toJson(list);
	}
	public String businessArrival(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String orderid = String.valueOf(request.getParameter("orderid"));
		String table = time.substring(2, 4) + time.substring(5, 7);
		String sql = "select t.*,g.abb_name from tb_operatearrive_"+table+" t,tb_global_company g where"
				+ " t.companyid = g.company_id and orderid = ?";
		System.out.println(sql+"  "+orderid);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql , orderid));
		return jacksonUtil.toJson(list);
	}
	public String getOverRange(HttpServletRequest request) {
		String stime = String.valueOf(request.getParameter("stime"));
		String etime = String.valueOf(request.getParameter("etime"));
		String vhic = String.valueOf(request.getParameter("vhic"));
		String company = String.valueOf(request.getParameter("company"));
		System.out.println(":"+vhic);
		List <Object> query =new ArrayList<Object>();
		String sql = "select o.*,t.abb_name from tb_operate_out_hz o,TB_GLOBAL_COMPANY t where o.companyid=t.company_id"
				+ " and deptime>= ? and"
				+ " deptime<= ?";
		query.add(stime);
		query.add(etime);
		if(!vhic.equals("null")&&vhic.length()>0) {
			sql += " and o.vehicleno like ?";
			query.add("%"+vhic+"%");
		}
		if(!company.equals("null")&&!company.equals("0")&&company.length()>0) {
			sql += " and companyid = ?";
			query.add(company);
		}
		System.out.println(sql);
		System.out.println(query);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, query.toArray()));
		return jacksonUtil.toJson(list);
	}
	public String getareajwd() {
		long time =  System.currentTimeMillis() - 1000*60*30;
		Date date=new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(date);
		String sql = "select * from TB_AREA_LONGLATI order by cast(id as unsigned int)";
		List<Map<String, Object>> newList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(newList);
	}
	public String getNoHG(HttpServletRequest request) {
//		LogUtil.log("营运动态监测-车辆动态监控", "营运动态监测-车辆动态监控");
		String vhic = String.valueOf(request.getParameter("vhic"));
		String name = String.valueOf(request.getParameter("name"));
		int page = Integer.parseInt(String.valueOf(request.getParameter("page")));
		int pageSize = 15;
		long time =  System.currentTimeMillis() - 1000*60*30;
		Date date=new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(date);
		String sql = "select * from (select t.*,g.abb_name,(select count(1) from TB_VEHICLE_GPS_STATUS t,"
				+ " TB_GLOBAL_COMPANY g where t.companyid = g.company_id ";
				if(!vhic.equals("null")){
					sql += "and vehicleno like '%"+vhic+"%'";
				}
				if(!name.equals("全部")){
					sql += " and abb_name = '"+name+"'";
				}
				sql += " and positiontime > '"+d+"' and vehicleno like '宁A%' and legal='0') c"
				+ "  from TB_VEHICLE_GPS_STATUS t, TB_GLOBAL_COMPANY g where t.companyid = g.company_id"
				+ " and positiontime > '"+d+"' and vehicleno like '宁A%' and legal='0'";
				if(!vhic.equals("null")){
					sql += "and vehicleno like '%"+vhic+"%'";
				}
				if(!name.equals("全部")){
					sql += " and abb_name = '"+name+"'";
				}
				sql += " ) tt limit "+((page-1)*pageSize)+","+pageSize;
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(String.valueOf(list.get(i).get("COMPANYID")).equals("yidao"))
				map.put("icon", "icon-ydzc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("caocao"))
				map.put("icon", "icon-cczc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("wanshun"))
				map.put("icon", "icon-wszc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shouyue"))
				map.put("icon", "icon-sqzc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("shenzhou"))
				map.put("icon", "icon-szzc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("didi"))
				map.put("icon", "icon-ddzc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("yangguangchedao"))
				map.put("icon", "icon-ygzc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("aa"))
				map.put("icon", "icon-aazc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("all"))
				map.put("icon", "icon-clsh");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("TJHT"))
				map.put("icon", "icon-xcyc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("hhyc"))
				map.put("icon", "icon-hhdc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tongcheng"))
				map.put("icon", "icon-mzdc");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("dccx"))
				map.put("icon", "icon-dccx");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiehua"))
				map.put("icon", "icon-xhcx");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("weixing"))
				map.put("icon", "icon-ycx");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("tmcx"))
				map.put("icon", "icon-tmcx");
			else if(String.valueOf(list.get(i).get("COMPANYID")).equals("xiangdao"))
				map.put("icon", "icon-xdcx");
			map.put("direction", list.get(i).get("DIRECTION"));
			map.put("bizStatus", list.get(i).get("BIZSTATUS"));
			map.put("speed", list.get(i).get("SPEED"));
			map.put("mdt_no", list.get(i).get("COMPANYID"));
			map.put("dateTime", list.get(i).get("POSITIONTIME"));
			map.put("longi", list.get(i).get("LONGITUDE"));
			map.put("vehino", list.get(i).get("VEHICLENO"));
			map.put("vehName", list.get(i).get("ABB_NAME"));
			map.put("lati", list.get(i).get("LATITUDE"));
			map.put("TYPE", String.valueOf(list.get(i).get("POSITIONTIME")).equals("null")?true:jisuan(String.valueOf(list.get(i).get("POSITIONTIME"))));
			map.put("C", list.get(i).get("C"));
			map.put("legal", list.get(i).get("LEGAL"));
			map.put("legalupdatetime", list.get(i).get("LEGALUPDATETIME"));
			list1.add(map);
			System.out.println(map.get("icon"));
		}
		return jacksonUtil.toJson(list1);
	}
	public String dailyAverage() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String riqi = sdf.format(new Date(new Date().getTime() - 1000*60*60*24l));
		String sql = "select y.*,c.abb_name from tb_order_yy y,tb_global_company c where y.companyid=c.company_id"
				+ " and y.dbtime = '"+riqi+"' order by order_ave desc";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}
	public String permitNum() {
//		int comp = jdbcTemplate4.queryForObject("select count(1) from platforms_base_info where `STATUS` = '1' and REMOVED = '0' and VALID_DATE_END > NOW()", Integer.class);
//		int driver = jdbcTemplate4.queryForObject("select count(1) from person_taxi_info t where t.`STATUS` = '0' and t.REMOVED = '0' and t.VALID_DATE_END > NOW()", Integer.class);
//		int vehi = jdbcTemplate4.queryForObject("select count(1) from platcar_base_info t where t.`STATUS` = '10' and t.REMOVED = '0' and t.end_date > NOW()", Integer.class);
		String sql = "select * from tb_tj";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		Map<String, Object> map = new HashMap<>();
		if(list.size()>0){
			map.put("comp", list.get(0).get("COMPANY_NUM"));
			map.put("driver", list.get(0).get("DRIVER_NUM"));
			map.put("vehi", list.get(0).get("VEHICLE_NUM"));
		}else{
			map.put("comp", 0);
			map.put("driver", 0);
			map.put("vehi", 0);
		}
		return jacksonUtil.toJson(map);
	}
	public String getGrid(HttpServletRequest request) {
		String type = String.valueOf(request.getParameter("type"));
		int time = Integer.parseInt(String.valueOf(request.getParameter("time")))*2;
		String table = "",filed = "";
		if(type.equals("0")) {
			table = "tb_hz_grid_operatedepart";
			filed = "operatedepart_cnt";
		}else {
			table = "tb_hz_grid_operatearrive";
			filed = "operatearrive_cnt";
		}
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
//		String sql = "select rn,a.* from (select ROW_NUMBER() OVER(PARTITION BY grid_id ORDER BY db_time DESC) rn,"
//				+ " g.* from (select g.grid_id,g.grid_lev,g.coord,g.center,"+filed+" operatedepart_cnt,o.db_time from tb_hz_grid g,"
//				+ table + " o where g.grid_id=o.grid_id) g) a where rn = 1";
		String sql = "select g.grid_id,g.grid_lev,g.coord,g.center,"+filed+" operatedepart_cnt,o.db_time from tb_hz_grid g,"
				+ table + " o where g.grid_id=o.grid_id and db_time >= '"+date+" "+time+":00:00'"
						+ " and db_time <='"+date+" "+time+":20:00'";
		System.out.println(time+"grid:"+sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}
	public String getDayData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		String time = sdf1.format(new Date());
		String now = sdf.format(new Date());
		String zt = sdf.format(new Date(new Date().getTime() - 1000*60*60*24l));
		String sz = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*7l));
		String sy = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*30l));
		String t15 = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*16));
		String sql = "select sum(CREATE_NUM) CREATE_NUM,sum(DEP_NUM) DEP_NUM,sum(ACCEPT_DUR) ACCEPT_DUR,max(ACTIVE_NUM) ACTIVE_NUM,sum(MATCH_NUM) MATCH_NUM"
				+ " from tb_order_pt_state where companyid = 'all' and ("
				+ "(db_time >= '"+now+" 00:00:00' and db_time <= '"+now+" "+time+"')"
				+ " or (db_time >= '"+zt+" 00:00:00' and db_time <= '"+zt+" "+time+"')"
				+ " or (db_time >= '"+sz+" 00:00:00' and db_time <='"+sz+" "+time+"')"
				+ " or (db_time >= '"+sy+" 00:00:00' and db_time <='"+sy+" "+time+"')"
				+ ") group by date_format(db_time,'%Y-%m-%d') order by date_format(db_time,'%Y-%m-%d') desc";
		System.out.println("getDayData:"+sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		Map<String, Object> map = new HashMap<>();
		map.put("top", list);
		String bottom = "select sum(CREATE_NUM) CREATE_NUM,sum(DEP_NUM) DEP_NUM,sum(ACCEPT_DUR) ACCEPT_DUR,max(ACTIVE_NUM) ACTIVE_NUM,sum(MATCH_NUM) MATCH_NUM,"
				+ " c.abb_name,SUM(DEP_NUM) DEP_NUM,SUM(ACCEPT_DUR)ACCEPT_DUR from tb_order_pt_state s left join tb_global_company c on"
				+ " s.companyid = c.company_id where db_time >= '"+now+" 00:00:00'"
				+ " and db_time <='"+now+" 23:59:59' group by abb_name";
		List<Map<String, Object>> bo = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(bottom));
		map.put("bottom", bo);
		System.out.println("select * from TB_ORDER_HY_STATE where db_time >= to_date('"+t15+"','yyyy-MM-dd') order by db_time");
		map.put("middle", JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select * from TB_ORDER_HY_STATE where db_time >= '"+t15+"' order by db_time")));
		return jacksonUtil.toJson(map);
	}
	public String iDayAver() {
		java.text.DecimalFormat df =new java.text.DecimalFormat("0.00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String yesterday = sdf.format(new Date(new Date().getTime() - 1000*60*60*24L));
    	String aWeekAgo = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*7L));
    	String sql = "select abb_name,sum(create_num) create_num,sum(dep_num) dep_num,sum(accept_dur) accept_dur,"
    			+ "sum(active_num) active_num from tb_order_pt_state s,tb_global_company c where s.companyid = c.company_id"
    			+ " and db_time >= '"+aWeekAgo+"' and"
    			+ " db_time < '"+yesterday+"' group by abb_name";
    	System.out.println("sql:"+sql);
    	List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
    	for (int i = 0; i < list.size(); i++) {
			list.get(i).put("CREATE_NUM", df.format(Double.parseDouble(String.valueOf(list.get(i).get("create_num")).equals("null")?"0":String.valueOf(list.get(i).get("create_num")))/7));
			list.get(i).put("DEP_NUM", df.format(Double.parseDouble(String.valueOf(list.get(i).get("dep_num")).equals("null")?"0":String.valueOf(list.get(i).get("dep_num")))/7));
			list.get(i).put("ACTIVE_NUM", df.format(Double.parseDouble(String.valueOf(list.get(i).get("active_num")).equals("null")?"0":String.valueOf(list.get(i).get("active_num")))/7/24));
			list.get(i).put("ACCEPT_DUR", df.format(Double.parseDouble(String.valueOf(list.get(i).get("accept_dur")).equals("null")?"0":String.valueOf(list.get(i).get("accept_dur")))/7<0?0.0:Double.parseDouble(String.valueOf(list.get(i).get("accept_dur")).equals("null")?"0":String.valueOf(list.get(i).get("accept_dur")))/7));
			list.get(i).put("COMPLETIONRATE", df.format((Double.parseDouble(String.valueOf(list.get(i).get("dep_num")).equals("null")?"0":String.valueOf(list.get(i).get("dep_num")))/7)/
											(Double.parseDouble(String.valueOf(list.get(i).get("create_num")).equals("null")?"0":String.valueOf(list.get(i).get("create_num")))/7)*100));
		}
    	return jacksonUtil.toJson(list);
	}
	public String getAreaSituation() {
//		String sql = "select rn,area_name,db_time,create_num,dep_num,accept_dur,active_num from"
//				+ " (select ROW_NUMBER() OVER(PARTITION BY area_name ORDER BY db_time DESC) rn,"
//				+ "area_name,db_time,create_num,dep_num,accept_dur,active_num from tb_order_area_state)"
//				+ " where rn = 1 order by active_num desc";
		String sql = "select area_name,db_time,create_num,dep_num,accept_dur,active_num,match_num from"
				+ " (select SUBSTRING_INDEX(GROUP_CONCAT(area_name ORDER BY db_time DESC),',',1) area_name,"
				+ " db_time,create_num,dep_num,accept_dur,active_num,match_num from tb_order_area_state GROUP BY area_name) tt"
				+ " group by area_name order by active_num desc";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		String day = "select area_name,sum(create_num) CREATE_NUM,sum(dep_num) DEP_NUM,sum(match_num) match_num from tb_order_area_state where"
				+ " db_time>='"+time+" 00:00:00'"
				+ " and db_time<='"+time+" 23:59:59' group by area_name order by create_num desc";
		List<Map<String, Object>> daylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(day));
		 HanyuPinyinUtil hanyuPinyinUtil = new HanyuPinyinUtil();
		for(int i=0; i<daylist.size(); i++) {
			daylist.get(i).put("AREA_NAME_PY", hanyuPinyinUtil.toHanyuPinyin(String.valueOf(daylist.get(i).get("AREA_NAME"))));
			for(int j=0; j<list.size(); j++) {
				if(String.valueOf(daylist.get(i).get("AREA_NAME")).
						equals(String.valueOf(list.get(j).get("AREA_NAME")))) {
					daylist.get(i).put("ACTIVE_NUM", list.get(j).get("ACTIVE_NUM"));
				}
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("current", list);
		map.put("day", daylist);
		return jacksonUtil.toJson(map);
	}
	public String getService() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(new Date().getTime() - 1000*60*15L));
		String sql = "select sum(case when BizStatus=1 or BizStatus=2 then 1 else 0  end) zks"
				+ ",sum(case when BizStatus=3 or BizStatus=4 then 1 else 0 end) kss"
				+ ",count(1) num from TB_VEHICLE_GPS_STATUS where vehicleno like '宁A%'"
				+ "and PositionTime>= '"+time+"'";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}
	public String cklxfx(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String type = String.valueOf(request.getParameter("type"));//0:热门起点   1：热门终点
		String sql = "";
		if(type.equals("0")) {
			sql = "select * from TB_WYC_ODGRAPH_STATIS t ,tb_area_min m where db_time = ? and t.orientid = m.area_id order by wyc_number desc";
		}else{
			sql = "select * from TB_WYC_ODGRAPH_STATIS2 t ,tb_area_min m where db_time = ? and t.destid = m.area_id order by wyc_number desc";
		}
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,time));
		return jacksonUtil.toJson(list);
	}
	public String cklxfxDetailed(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String id = String.valueOf(request.getParameter("id"));
		String type = String.valueOf(request.getParameter("type"));//0:热门起点   1：热门终点
		String sql = "";
		if(type.equals("0")) {
			sql = "select * from TB_WYC_ODGRAPH t,tb_area_min m where orientid = ? and db_time = ? and t.destid = m.area_id and t.destid != ?";
		}else{
			sql = "select * from TB_WYC_ODGRAPH t,tb_area_min m where orientid = ? and db_time = ? and t.destid = m.area_id and t.destid != ?";
		}
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,id,time,id));
		return jacksonUtil.toJson(list);
	}
	public String passengerFlowTo(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String type = String.valueOf(request.getParameter("type"));//0:热门起点   1：热门终点
		String area = String.valueOf(request.getParameter("area")).indexOf("全")>=0?"":String.valueOf(request.getParameter("area"));//所属区域
		String sql = "";
		if(type.equals("0")) {
			sql = "select * from TB_QY_ODGRAPH_STATIS t ,tb_area_min m where db_time = ?"
					+ " and district like ? and t.orientid = m.area_id order by wyc_number desc";
		}else{
			sql = "select * from TB_QY_ODGRAPH_STATIS2 t ,tb_area_min m where db_time = ?"
					+ " and district like ? and t.destid = m.area_id order by wyc_number desc";
		}
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,time,"%"+area+"%"));
		List<Map<String, Object>> jg = new ArrayList<>();
		if(list.size()>20){
			for(int i=0; i<20; i++) {
				jg.add(list.get(i));
			}
		}else{
			jg = list;
		}
		return jacksonUtil.toJson(jg);
	}
	public String passengerFlowToDetailed(HttpServletRequest request) {
		String time = String.valueOf(request.getParameter("time"));
		String id = String.valueOf(request.getParameter("id"));
		String type = String.valueOf(request.getParameter("type"));//0:热门起点   1：热门终点
		String sql = "";
		if(type.equals("0")) {
			sql = "select * from TB_QY_ODGRAPH t,tb_area_min m where orientid = ? and db_time = ?"
					+ " and t.destid = m.area_id and t.destid != ?";
		}else{
			sql = "select * from TB_QY_ODGRAPH2 t,tb_area_min m where orientid = ? and db_time = ?"
					+ " and t.destid = m.area_id and t.destid != ?";
		}
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,id,time,id));
		return jacksonUtil.toJson(list);
	}
	public String gettest() {
		String sql = "select * from tb_area_min limit 0,99";
		return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql)));
	}
	public String getZFCX(HttpServletRequest request) {
		String company = String.valueOf(request.getParameter("company"));
		String stime = String.valueOf(request.getParameter("stime"));
		String etime = String.valueOf(request.getParameter("etime"));
		String sql = "select * from TB_WYC_ZHZF_ZFDJ where 1=1 ";//"%"+vhic.toUpperCase()+"%"
		List<Object> list = new ArrayList<Object>();
		if(!company.equals("null")&&company.length()>0) {
			sql += " and company like ?";
			list.add("%"+company+"%");
		}
		if(!stime.equals("null")&&stime.length()>0) {
			list.add(stime);
			sql += " and zf_time >= ?";
		}
		if(!etime.equals("null")&&etime.length()>0) {
			list.add(etime);
			sql += " and zf_time <= ?";
		}
		System.out.println(sql+"  "+list);
		List<Map<String, Object>> data = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,list.toArray()));
		for(int i=0; i<data.size(); i++){
			data.get(i).put("pucture", JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select zf_zp from TB_WYC_ZHZF_ZFZP where zf_id = '"+data.get(i).get("ID")+"'")));
		}
		return jacksonUtil.toJson(data);
	}
	public String getZFGPS() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long sqsj = System.currentTimeMillis() - 1000*60*30;
		String time = sdf.format(new Date(sqsj));
		String sql = "select * from TB_WYC_ZHZF_REALTIME_GPS where gps_time >= ?";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,time));
		return jacksonUtil.toJson(list);
	}
	public String addZFDD(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		String name = String.valueOf(request.getParameter("name"));
		String note = String.valueOf(request.getParameter("note"));
		String id = String.valueOf(request.getSession().getAttribute("id"));
		String sql = "insert into TB_WYC_ZHZF_ZFDD (id,ZFRY,DDNR,DDRY) values (?,?,?,?)";
		int count = jdbcTemplate.update(sql,UUID.randomUUID().toString(),name,note,id);
		if(count >0) {
			map.put("code", "0");
			map.put("info", "下发成功");
		}else {
			map.put("code", "1");
			map.put("info", "下发失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String getEarlyWarningNum() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf1.format(new Date());
		String yesterday = sdf.format(new Date(new Date().getTime() - 1000*60*60*24L));
		String table = yesterday.substring(2, 4)+yesterday.substring(5, 7);
		String one = "select count(1) c from TB_VIOLATION_ANALYSIS_"+table+" t,TB_GLOBAL_COMPANY g where g.COMPANY_ID = t.PLATFORM_COMPANY and "
				+ "stime >= '"+yesterday+" 00:00:00'"
				+ " and stime <= '"+yesterday+" 23:59:59'";
		String oneNum = jdbcTemplate.queryForObject(one, String.class);
		String two = "select count(1) c from tb_vehiclebaseinfo";
//		String two = "select count(1) c from tb_vehiclebaseinfo where END_DATE < '"+now+"' and STATUS = 10 and removed = 0";
		String twoNum = jdbcTemplate.queryForObject(two, String.class);
		String three = "select count(1) from tb_operate_out_hz where deptime >= '"+yesterday+" 00:00:00'"
				+ " and deptime <= '"+yesterday+" 23:59:59'";
		String threeNum = jdbcTemplate.queryForObject(three, String.class);
		String four = "select sum(NUM) C from TB_AREA_LONGLATI";
		String fourNum = jdbcTemplate.queryForObject(four, String.class);
		Map<String, Object> map = new HashMap<>();
		map.put("one", oneNum);
		map.put("two", twoNum);
		map.put("three", threeNum);
		map.put("four", fourNum);
		return jacksonUtil.toJson(map);
	}

	//行业日均
	public String getIndustryDayAverage() {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(new Date());
		String yesterday = sdf.format(new Date(new Date().getTime() - 1000*60*60*24));
		String week = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*8));
		String month = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*31L));
		List<Map<String, Object>> nowList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select date_format(db_time,'%H') time,dep_num,active_num,match_num from tb_order_pt_state"
				+ " where db_time >= '"+now+"' and companyid = 'all' order by db_time"));
		List<Map<String, Object>> yesterdayList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select date_format(db_time,'%H') time,dep_num,active_num,match_num from tb_order_pt_state"
				+ " where db_time < '"+now+"' and db_time >= '"+yesterday+"' and companyid = 'all' order by db_time"));
		List<Map<String, Object>> weekList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select date_format(db_time,'%H') time,round(sum(dep_num)/7,2) dep_num,round(sum(active_num)/7,2)"
				+ " active_num,round(sum(match_num)/7,2) match_num from tb_order_pt_state where db_time < '"+now+"' and"
				+ " db_time >= '"+week+"' and companyid = 'all' group by date_format(db_time,'%H') order by date_format(db_time,'%H')"));
		List<Map<String, Object>> monthList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select date_format(db_time,'%H') time,round(sum(dep_num)/30,2) dep_num,round(sum(active_num)/30,2)"
				+ " active_num,round(sum(match_num)/30,2) match_num from tb_order_pt_state where db_time < '"+now+"' and"
				+ " db_time >= '"+month+"' and companyid = 'all' group by date_format(db_time,'%H') order by date_format(db_time,'%H')"));
		map.put("today", nowList);
		map.put("yesterday", yesterdayList);
		map.put("sevenDays", weekList);
		map.put("thirtyDays", monthList);
		return jacksonUtil.toJson(map);
	}

	//单车日均
	public String getBicycleDayAverage() {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String month = sdf.format(new Date(new Date().getTime() - 1000*60*60*24*31L));
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from tb_order_yy where dbtime >= '"+month+"' and companyid='all' order by dbtime");
		return jacksonUtil.toJson(list);
	}

    public String getMapCluster(HttpServletRequest request) {
        String areatype = getRequest(request, "areatype");
        String companyid = getRequest(request, "companyid");
        String sql = "select * from TB_CLUSTER_COMPANY where areatype = ? and companyid = ?";
        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, areatype, companyid));
        return jacksonUtil.toJson(list);
    }

	public String getVehicleByCoordinate(HttpServletRequest request) {
		float maxlat = request.getParameter("maxlat")==null?0: Float.parseFloat(request.getParameter("maxlat"));
		float maxlng = request.getParameter("maxlng")==null?0: Float.parseFloat(request.getParameter("maxlng"));
		float minlat = request.getParameter("minlat")==null?0: Float.parseFloat(request.getParameter("minlat"));
		float minlng = request.getParameter("minlng")==null?0: Float.parseFloat(request.getParameter("minlng"));
		Long num_time = request.getParameter("num_time")==null?60*24*365*10: Long.parseLong(request.getParameter("num_time"));
		String companyid = getRequest(request, "companyid");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(new Date().getTime() - 1000*60*num_time));
		String sql = "select * from tb_taxi_gps_realtime t, TB_GLOBAL_COMPANY g where t.companyid = g.company_id" +
				" and longitude >= ? and longitude <= ? and  latitude >= ? and latitude <= ? and ct >= ?";
		if(!companyid.isEmpty()&&!"null".equals(companyid)&&!"all".equals(companyid)){
			sql +="and t.companyid = '"+companyid+"'";
		}
		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,minlng,maxlng,minlat,maxlat,time));
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bizStatus", list.get(i).get("BIZSTATUS"));
			map.put("speed", list.get(i).get("SPEED"));
			map.put("mdt_no", list.get(i).get("COMPANYID"));
			map.put("dateTime", list.get(i).get("CT"));
			map.put("longi", list.get(i).get("LONGITUDE"));
			map.put("vehino", list.get(i).get("VEHICLENO"));
			map.put("vehName", list.get(i).get("ABB_NAME"));
			map.put("lati", list.get(i).get("LATITUDE"));
//			map.put("TYPE", String.valueOf(list.get(i).get("POSITIONTIME")).equals("null")?true:jisuan(String.valueOf(list.get(i).get("POSITIONTIME"))));
			map.put("TYPE", false);
			map.put("C", list.get(i).get("C"));
			map.put("legal", list.get(i).get("LEGAL"));
			map.put("legalupdatetime", String.valueOf(list.get(i).get("LEGALUPDATETIME")));
			list1.add(map);
		}
		return jacksonUtil.toJson(list1);
	}

	public String getVehicleLocation(HttpServletRequest request) {
		String vhic = String.valueOf(request.getParameter("vhic"));
		String companyid = String.valueOf(request.getParameter("companyid"));
		int page = Integer.parseInt(String.valueOf(request.getParameter("page")));
		int pageSize = Integer.parseInt(String.valueOf(request.getParameter("pageSize")));
		Long num_time = request.getParameter("num_time")==null?60*24*365*10: Long.parseLong(request.getParameter("num_time"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(new Date().getTime() - 1000*60*num_time));

		String tj ="";
		if(!vhic.equals("null")&&!vhic.isEmpty()){
			tj += " and t.vehicleno like '%"+vhic+"%'";
		}
		if(!companyid.equals("null")&&!companyid.equals("all")&&!companyid.isEmpty()){
			tj += " and t.companyid = '"+companyid+"'";
		}
		tj += " and t.CT >= '"+time+"'";

		String sql = "select * from (select t.*,g.abb_name,(select count(1) from tb_taxi_gps_realtime t,"
				+ " TB_GLOBAL_COMPANY g where t.companyid = g.company_id ";
		sql += tj;
		sql += " " +
				") c from tb_taxi_gps_realtime t, TB_GLOBAL_COMPANY g" +
				" where t.companyid = g.company_id" +
				"";
		sql += tj;
		sql += " ) tt limit "+(page-1)*pageSize+","+pageSize;

		System.out.println(sql);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bizStatus", list.get(i).get("BIZSTATUS"));
			map.put("speed", list.get(i).get("SPEED"));
			map.put("mdt_no", list.get(i).get("COMPANYID"));
			map.put("dateTime", list.get(i).get("CT"));
			map.put("longi", list.get(i).get("LONGITUDE"));
			map.put("vehino", list.get(i).get("VEHICLENO"));
			map.put("vehName", list.get(i).get("ABB_NAME"));
			map.put("lati", list.get(i).get("LATITUDE"));
//			map.put("TYPE", String.valueOf(list.get(i).get("POSITIONTIME")).equals("null")?true:jisuan(String.valueOf(list.get(i).get("POSITIONTIME"))));
			map.put("TYPE", false);
			map.put("C", list.get(i).get("C"));
			map.put("legal", list.get(i).get("LEGAL"));
			map.put("legalupdatetime", String.valueOf(list.get(i).get("LEGALUPDATETIME")));
			list1.add(map);
		}
		return jacksonUtil.toJson(list1);
	}

	public String getEchartData(HttpServletRequest request) {
		String companyid = getRequest(request, "companyid");
		String sql = "select * from TB_ONLINE where companyid = ?";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, companyid));
		return jacksonUtil.toJson(list);
	}
}