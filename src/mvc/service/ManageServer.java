package mvc.service;

import helper.JacksonUtil;
import helper.LogUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ManageServer {
	@Autowired
	protected JdbcTemplate jdbcTemplate = null;
	@Autowired
	protected JdbcTemplate jdbcTemplate4 = null;
	protected String dbname = "ptdata";

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	public boolean isNull(String str) {
		if (str == null || str == "null" || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private void getCount(Map<String, Object> map,
			List<Map<String, Object>> list) {
		if (list.size() > 0) {
			map.put("count", list.get(0).get("count")==null?list.get(0).get("COUNT"):list.get(0).get("count"));
		} else {
			map.put("count", "0");
		}
	}
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = format.format(new Date());
	private void log(HttpServletRequest request,String model,String s){
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,?)";
//		int list = jdbcTemplate.update(sql,username,model,s,time);
	}
	
	/**
	 * 许可信息查询  → 查询人 
	 * 用到的表 ： person_base_info   person_taxi_info
	 * @param request
	 */
	public String findPerson(HttpServletRequest request,String postData) {
		log(request,"许可信息查询  → 查询人 ", "许可信息查询  → 查询人 ");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String name = String.valueOf(parampMap.get("name"));
		String phone = String.valueOf(parampMap.get("phone"));
		String sfz = String.valueOf(parampMap.get("sfz"));
		String begin_time = String.valueOf(parampMap.get("begin_time"));
		String end_time = String.valueOf(parampMap.get("end_time"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		String tj = "";  //person_taxi_info 表的条件
		String tj1 = ""; //person_base_info 表的条件
		List <Object> queryList=new  ArrayList<Object>();
		if(!isNull(begin_time)){
			tj += " and PRINT_DATE > ? ";
			queryList.add(begin_time+" 00:00:00");
		}
		if(!isNull(end_time)){
			tj += " and PRINT_DATE < ? ";
			queryList.add(end_time+" 23:59:59");
		}
		if(!isNull(name)){
			tj1 += " and name like ? ";
			queryList.add(name+"%");
		}
		if(!isNull(phone)){
			tj1 += " and phone like ? ";
			queryList.add(phone+"%");
		}
		if(!isNull(sfz)){
			tj1 += " and ID_NUMBER like ? ";
			queryList.add(sfz+"%");
		}
		String sql = "select t2.DISTRICT,t2.name,t2.ID_NUMBER,t2.DRIVER_NUM,t.CERTI_SUB_CODE,t.STATUS,t2.sex,t2.BIRTHDAY,t2.NATION ,t2.FILE_NUMBER,t2.CITY,t2.ID_ADDR,t2.LIVING_ADDR,t2.PHONE,"
				+ " t.ORGAN_CODE,str_to_date(t.VALID_DATE_BEGIN,'%Y-%m-%d') as VALID_DATE_BEGIN,str_to_date(t.VALID_DATE_END,'%Y-%m-%d') as VALID_DATE_END,str_to_date(t.CERTI_EARLY_DATE,'%Y-%m-%d') as CERTI_EARLY_DATE,str_to_date(t.PRINT_DATE ,'%Y-%m-%d') as PRINT_DATE "
				+ "from (select * from person_taxi_info where STATUS = 0  and REMOVED = 0 "+tj+"  ) t , (select * from person_base_info where REMOVED = 0 "+tj1+" ) t2 where  t.PERSON_ID = t2.id limit "+(pageIndex - 1) * pageSize+","+(pageIndex * pageSize) ;
		String countsql = "select count(PERSON_ID) as count  from person_taxi_info where STATUS = 0  and REMOVED = 0 "+tj;
//		String countsql = "select count(t.PERSON_ID) as count "
//				+ "from (select PERSON_ID from person_taxi_info where STATUS = 0  and REMOVED = 0 "+tj+"  ) t , (select * from person_base_info where REMOVED = 0 "+tj1+" ) t2 where  t.PERSON_ID = t2.id ";
	

		System.out.println(sql);
		System.out.println(countsql);
		List<Map<String, Object>> list = jdbcTemplate4.queryForList(sql,queryList.toArray());
		List<Map<String, Object>> countList = jdbcTemplate4.queryForList(countsql,queryList.toArray());
		Map<String,Object> resultMap = new HashMap<>();
		getCount(resultMap, countList);
		resultMap.put("datas",list);
		return jacksonUtil.toJson(resultMap);
	}
	
	/**
	 * 许可信息查询  → 查询企业
	 * 用到的表 ： platforms_base_info
	 * @param request
	 */
	public String findPlatforms(HttpServletRequest request,String postData) {
		log(request,"许可信息查询  → 查询企业 ", "许可信息查询  → 查询企业");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String name = String.valueOf(parampMap.get("name"));
		String license_num = String.valueOf(parampMap.get("license_num"));
		String status = String.valueOf(parampMap.get("status"));
		String tj = "";  
		List <Object> queryList=new  ArrayList<Object>();
		if(!isNull(license_num)){
			tj += " and LICENSE_NUM like ? ";
			queryList.add(license_num+"%");
		}
		if(!isNull(status) && !status.equals("0")){
			tj += " and STATUS = ? ";
			queryList.add(status);
		}
		if(!isNull(name)){
			tj += " and name like ? ";
			queryList.add(name+"%");
		}
		String sql = "select * from platforms_base_info where REMOVED = 0  "+tj;
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate4.queryForList(sql,queryList.toArray());
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("datas", list);
		return jacksonUtil.toJson(resultMap);
	}
	
	/**
	 * 许可信息查询  → 查询车辆
	 * 用到的表 ： platforms_base_info
	 * @param request
	 */
	public String findPlatcar(HttpServletRequest request,String postData) {
		log(request,"许可信息查询  → 查询车辆 ", "许可信息查询  → 查询车辆");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String auto_num = String.valueOf(parampMap.get("auto_num"));  //车牌号码
		String color = String.valueOf(parampMap.get("color"));   //车牌颜色
		String start_begin = String.valueOf(parampMap.get("start_begin")); //有效期起的起始时间
		String start_end = String.valueOf(parampMap.get("start_end")); //有效期起的结束时间
		String end_begin = String.valueOf(parampMap.get("end_begin")); //有效期止的起始时间
		String end_end = String.valueOf(parampMap.get("end_end")); //有效期止的结束时间
		String license_memo = String.valueOf(parampMap.get("license_memo")); //道路运输证号
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		String tj = "";  
		List <Object> queryList=new  ArrayList<Object>();
		if(!isNull(auto_num)){
			tj += " and auto_num like ? ";
			queryList.add(auto_num.toUpperCase()+"%");
		}
		if(!isNull(color) && !color.equals("0")){
			tj += " and LICENSE_PLATE_COLOR = ? ";
			queryList.add(color);
		}
		if(!isNull(license_memo)){
			tj += " and LICENSE_NUMBER like ? ";
			queryList.add(license_memo+"%");
		}
		if(!isNull(start_begin)){
			tj += " and START_DATE > ? ";
			queryList.add(start_begin+" 00:00:00");
		}
		if(!isNull(start_end)){
			tj += " and START_DATE < ? ";
			queryList.add(start_end+" 23:59:59");
		}
		
		if(!isNull(end_begin)){
			tj += " and END_DATE > ? ";
			queryList.add(end_begin+" 00:00:00");
		}
		if(!isNull(end_end)){
			tj += " and END_DATE < ? ";
			queryList.add(end_end+" 23:59:59");
		}
		String sql = "select * from platcar_base_info where REMOVED = 0  "+tj +" limit "+(pageIndex - 1) * pageSize+","+(pageIndex * pageSize);
		String countSql = "select count(AUTO_NUM) as count from platcar_base_info where REMOVED = 0  "+tj ;
		System.out.println(countSql);
		List<Map<String, Object>> list = jdbcTemplate4.queryForList(sql,queryList.toArray());
		List<Map<String, Object>> countList = jdbcTemplate4.queryForList(countSql,queryList.toArray());
		Map<String, Object> resultMap = new HashMap<>();
		getCount(resultMap, countList);
		resultMap.put("datas", list);
		return jacksonUtil.toJson(resultMap);
	}
	
	/**
	 * 综合信息查询 → 查询平台基本信息
	 */
	public String findOnlyCompany(HttpServletRequest request) {
		log(request,"综合信息查询 → 查询平台基本信息", "综合信息查询 → 查询平台基本信息");
		String sql = "select companyId as id,CompanyName  as text from tb_company";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(list);
	}

	/**
	 * 综合信息查询 → 查询平台基本信息
	 */
	public String findCompany(HttpServletRequest request) {
		log(request,"综合信息查询 → 查询平台基本信息", "综合信息查询 → 查询平台基本信息");
		String sql = "select company_Id as id,abb_name  as text from TB_GLOBAL_COMPANY";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> allmap = new HashMap<String, Object>();
		allmap.put("id", "0");
		allmap.put("text", "-------全部-------");
		allmap.put("selected", "true");
		resultMap.add(allmap);
		if(list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", list.get(i).get("ID"));
				map.put("text", list.get(i).get("TEXT"));
				resultMap.add(map);
			}
		}
		return jacksonUtil.toJson(resultMap);
	}

	/**
	 * 综合信息查询 → 查询平台基本信息
	 */
	public String findPtxx(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询平台基本信息", "综合信息查询 → 查询平台基本信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname)) {
			map.put("msg", "公司id值为空");
			return jacksonUtil.toJson(map);
		}
		// 平台基本信息
		String ptxxSql = "select CompanyName,Identifier,Address,EconomicType,RegCapital,LegalName,LegalID,LegalPhone,ContactAddress,BusinessScope from  tb_company where CompanyId = ?";
		List<Map<String, Object>> ptxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptxxSql,ptname));
		// 平台经营许可信息
		String jyxkSql = "select t.*,t2.CompanyName from  tb_companypermit t,tb_company t2 where t.CompanyId = t2.CompanyId and  t.CompanyId = ?";
		List<Map<String, Object>> jyxklist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(jyxkSql,ptname));
		// 平台营运规模信息
		String yygmSql = "select  t.*,t2.CompanyName from  tb_companystat t,tb_company t2 where t.CompanyId = t2.CompanyId and  t.CompanyId = ?";
		List<Map<String, Object>> yygmlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(yygmSql,ptname));
		// 平台支付信息
		String ptzfSql = "select  t.*,t2.CompanyName from  tb_companypay t,tb_company t2 where t.CompanyId = t2.CompanyId and  t.CompanyId = ?";
		List<Map<String, Object>> ptzflist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptzfSql,ptname));
		// 平台服务机构信息
		String ptfwjgSql = "select  t.*,t2.CompanyName from  tb_companyservice t,tb_company t2 where t.CompanyId = t2.CompanyId and  t.CompanyId = ?";
		List<Map<String, Object>> ptfwjglist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptfwjgSql,ptname));
		map.put("ptxx", ptxxlist);
		map.put("jyxk", jyxklist);
		map.put("yygm", yygmlist);
		map.put("ptzf", ptzflist);
		map.put("ptfwjg", ptfwjglist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询平台经营许可信息
	 */
	public String findPtjyxkxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询平台经营许可信息", "基础信息查询 → 查询平台经营许可信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String state = String.valueOf(parampMap.get("state"));
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		List <Object> queryList=new  ArrayList<Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(state)) {
			tj += " and t.state = ? ";
			queryList.add(state);
		}
		// 平台经营许可信息
		String jyxkSql = "select t.*,t2.CompanyName from  tb_companypermit t,tb_company t2 where t.CompanyId = t2.companyid " + tj;
		List<Map<String, Object>> jyxklist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(jyxkSql,queryList.toArray()));
		map.put("jyxk", jyxklist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询平台营运规模信息
	 */
	public String findPtyygmxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询平台营运规模信息", "基础信息查询 → 查询平台营运规模信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		List <Object> queryList=new  ArrayList<Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		// 平台营运规模信息
		String yygmSql = "select  t.*,t2.CompanyName from  tb_companystat t,tb_company t2 where t.CompanyId = t2.companyid " + tj;
		List<Map<String, Object>> yygmlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(yygmSql,queryList.toArray()));
		map.put("yygm", yygmlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询平台支付信息
	 */
	public String findPtzfxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询平台支付信息", "基础信息查询 → 查询平台支付信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String state = String.valueOf(parampMap.get("state"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		
		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(state)) {
			tj += " and t.state = ? ";
			queryList.add(state);
		}
		
		String tj2 = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(state)) {
			tj2 += " and t.state = ? ";
			queryList.add(state);
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		// 平台支付信息
		String ptzfSql = "select (select count(*) from tb_companypay t,tb_company t2 where t.CompanyId = t2.companyid " + tj + " ) as count , tt.* from " +
				" (select  t.*,t2.CompanyName from tb_companypay t,tb_company t2 where t.CompanyId = t2.companyid " + tj2 + ")tt" +
				" limit ?, ?";
		List<Map<String, Object>> ptzflist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptzfSql,queryList.toArray()));
		map.put("datas", ptzflist);
		getCount(map, ptzflist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 查询平台服务机构信息
	 */
	public String findPtfwjg(HttpServletRequest request, String postData) {
		log(request,"查询平台服务机构信息","查询平台服务机构信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String jgmc = String.valueOf(parampMap.get("jgmc"));
		String state = String.valueOf(parampMap.get("state"));
		String tj = "";
		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(state)) {
			tj += " and t.State = ? ";
			queryList.add(state);
		}
		if (!isNull(jgmc)) {
			tj += " and t.ServiceName like ?";
			queryList.add(jgmc+"%");
		}
		// 平台服务机构信息1
		String ptfwjgSql = "select  t.*,t2.CompanyName from  tb_companyservice t,tb_company t2 where t.CompanyId = t2.companyid "
				+ tj;
		List<Map<String, Object>> ptfwjglist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptfwjgSql,queryList.toArray()));
		map.put("fwjg", ptfwjglist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询平台运价信息
	 */
	public String findPtyjxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询平台运价信息", "基础信息查询 → 查询平台运价信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String yjlx = String.valueOf(parampMap.get("yjlx"));
		String state = String.valueOf(parampMap.get("state"));
		String tj = "";
		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(state)) {
			tj += " and t.state = ? ";
			queryList.add(state);
		}
		if (!isNull(yjlx)) {
			tj += " and t.FareType = ? ";
			queryList.add(yjlx);
		}
		// 平台运价信息
		String ptyjxxSql = "select  t.*,t2.CompanyName from  tb_companyfare t,tb_company t2 where t.CompanyId = t2.companyid "
				+ tj;
		List<Map<String, Object>> ptyjxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptyjxxSql,queryList.toArray()));
		map.put("yjxx", ptyjxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合信息查询 → 查询车辆信息
	 */
	public String findClxx(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询车辆信息", "综合信息查询 → 查询车辆信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String vehino = String.valueOf(parampMap.get("vehino"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname) || isNull(vehino)) {
			map.put("msg", "平台名称和车牌号码不能为空");
			return jacksonUtil.toJson(map);
		}
		// 车辆基本信息
		String clxxSql = "select t2.CompanyName,t.* from  (select * from tb_vehiclebaseinfo where  CompanyId = ? and VehicleNo = ? ) t,tb_company t2 where t.CompanyId = t2.CompanyId  ";
		List<Map<String, Object>> clxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(clxxSql,ptname,vehino));
		// 车辆保险信息
		String clbxSql = "select  t.*,t2.CompanyName from  ( select * from tb_vehicleinsurance_"+format.format(new Date())+" where CompanyId = ? and VehicleNo = ? )  t,tb_company t2 where t.CompanyId = t2.CompanyId ";
		List<Map<String, Object>> clbxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(clbxSql,ptname,vehino));
		map.put("clxx", clxxlist);
		map.put("clbx", clbxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合信息查询 → 查询车辆信息 → 车辆里程信息
	 */
	public String findCarClxx(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询车辆信息", "综合信息查询 → 查询车辆信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String vehino = String.valueOf(parampMap.get("vehino"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname) || isNull(vehino)) {
			map.put("msg", "平台名称和车牌号码不能为空");
			return jacksonUtil.toJson(map);
		}
		// 车辆里程信息
		String cllcSql = "select   t.*,t2.CompanyName from  (select * from tb_vehicletotalmile_"+format.format(new Date())+" where CompanyId = ? and VehicleNo = ? )  t,tb_company t2 where t.CompanyId = t2.CompanyId ";
		List<Map<String, Object>> cllclist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(cllcSql,ptname,vehino));
		map.put("cllc", cllclist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	
	/**
	 * 基础信息查询 → 车牌号码模糊查询 3位起查
	 */
	public String findVehicleNo(HttpServletRequest request, String postData,String tableName) {
		log(request,"基础信息查询 → 车牌号码模糊查询 3位起查", "基础信息查询 → 车牌号码模糊查询 3位起查");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehino = String.valueOf(parampMap.get("vehino"));
		String year = String.valueOf(parampMap.get("year"));
		if(!isNull(year)){
			tableName+= "_"+year;
		}
		// 车辆保险信息
		String sql = "select VehicleNo from  "+ tableName + "  where VehicleNo like ? " ;
		List <Object> queryList=new  ArrayList<Object>();
		queryList.add(vehino+"%");
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> cllclist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
		map.put("vehicleNo", cllclist);
		return jacksonUtil.toJson(map);
	}
	/**
	 * 基础信息查询 → 查询车辆保险信息
	 */
	public String findClbxxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询车辆保险信息", "基础信息查询 → 查询车辆保险信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String vehino = String.valueOf(parampMap.get("vehino"));
		String year = String.valueOf(parampMap.get("year"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		
		List <Object> queryList=new  ArrayList<Object>();
		String tj = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(vehino)) {
			tj += " and t.VehicleNo like ?";
			queryList.add(vehino+"%");
		}
		String tj2 = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(vehino)) {
			tj2 += " and t.VehicleNo like ?";
			queryList.add(vehino+"%");
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		System.out.println("sql开始:"+new Date());
		// 车辆保险信息
		String clbxSql = "select (select count(*) from  tb_vehicleinsurance_"+year+"  t where 1 = 1 " + tj + ") as count ,tt.* from ("
				+ " select t1.*, t2.CompanyName from (SELECT t.* FROM tb_vehicleinsurance_"+year+" t"
				+ " where 1 = 1 " + tj2 + " ) t1,tb_company t2 WHERE t1.CompanyId = t2.companyid limit ?, ? )tt ";
		System.out.println(clbxSql);
		List<Map<String, Object>> clbxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(clbxSql,queryList.toArray()));
		System.out.println("sql结束:"+new Date());
		map.put("datas", clbxlist);
		getCount(map, clbxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询车辆里程信息
	 */
	public String findCllcxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询车辆里程信息", "基础信息查询 → 查询车辆里程信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String vehino = String.valueOf(parampMap.get("vehino"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		String year = String.valueOf(parampMap.get("year"));
		List <Object> queryList=new  ArrayList<Object>();
		String tj = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNull(vehino)) {
			tj += " and t.VehicleNo like ?";
			queryList.add(vehino+"%");
		}
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}

		String tj2 = "";
		if (!isNull(vehino)) {
			tj2 += " and t.VehicleNo like ?";
			queryList.add(vehino+"%");
		}
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}

		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		// 车辆里程信息
		String cllcSql = "select (select count(*) from  tb_vehicletotalmile_"+year+"  t where 1 = 1  " + tj + ") as count,tt.* from ("
				+ "SELECT t1.*, t2.CompanyName FROM ( SELECT t.* FROM tb_vehicletotalmile_"+year+" t WHERE 1 = 1 " + tj2 + ") t1, tb_company t2" +
				" WHERE t1.CompanyId = t2.companyid limit ?, ? )tt ";
		List<Map<String, Object>> cllclist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(cllcSql,queryList.toArray()));

		map.put("datas", cllclist);
		getCount(map, cllclist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合信息查询 → 查询驾驶员信息
	 */
	public String findJsyxx(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询驾驶员信息", "综合信息查询 → 查询驾驶员信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String jszh = String.valueOf(parampMap.get("jszh"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname) || isNull(jszh)) {
			map.put("msg", "平台名称和驾驶证号不能为空");
			return jacksonUtil.toJson(map);
		}

		// 驾驶员基本信息
		String jsyxxSql = "select t2.CompanyName,t.* from  (select * from tb_driverbaseinfo where CompanyId = ? and LicenseId = ? ) t,tb_company t2 where t.CompanyId = t2.CompanyId ";
		List<Map<String, Object>> jsyxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate .queryForList(jsyxxSql,ptname,jszh));
//		// 驾驶员培训信息
		String pxxxSql = "select  t.*,t2.CompanyName from  tb_drivereducate_"+ format.format(new Date()) +"  t,tb_company t2 where t.CompanyId = t2.CompanyId and  t.CompanyId = ? and t.LicenseId = ?";
		List<Map<String, Object>> pxxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(pxxxSql,ptname,jszh));
		map.put("jsyxx", jsyxxlist);
		map.put("pxxx", pxxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合信息查询 → 查询驾驶员信息 → 驾驶员移动终端信息
	 */
	public String getDriverApp(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询驾驶员信息", "综合信息查询 → 查询驾驶员信息 →获取驾驶员移动终端信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String jszh = String.valueOf(parampMap.get("jszh"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname) || isNull(jszh)) {
			map.put("msg", "平台名称和驾驶证号不能为空");
			return jacksonUtil.toJson(map);
		}
		// 驾驶员移动终端表tb_driverapp_"+ format.format(new Date()) +" 
		String ydzdSql = "select tt.*,c.CompanyName from (select * from tb_drivereducate_"+ format.format(new Date()) +" t where t.CompanyId = ? and t.LicenseId = ? )  tt ,tb_company c where tt.CompanyId = c.CompanyId ";
		List<Map<String, Object>> ydzdlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ydzdSql,ptname,jszh));
		map.put("ydzd", ydzdlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	/**
	 * 综合信息查询 → 查询驾驶员信息 → 驾驶员统计信息
	 */
	public String getDriverStat(HttpServletRequest request, String postData) {
		log(request,"综合信息查询 → 查询驾驶员信息 → 驾驶员统计信息", "综合信息查询 → 查询驾驶员信息 → → 驾驶员统计信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String jszh = String.valueOf(parampMap.get("jszh"));
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(ptname) || isNull(jszh)) {
			map.put("msg", "平台名称和驾驶证号不能为空");
			return jacksonUtil.toJson(map);
		}
		// 驾驶员统计信息
		String tjxxSql = "select   t.*,t2.CompanyName from (select * from tb_driverstat_"+ format.format(new Date()) +" where CompanyId = ? and LicenseId = ? )   t,tb_company t2 where t.CompanyId = t2.CompanyId ";
		List<Map<String, Object>> tjxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(tjxxSql,ptname,jszh));
		map.put("tjxx", tjxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	
	/**
	 * 查询驾驶员移动终端信息
	 */
	public String findJsyydzdxx(HttpServletRequest request, String postData) {
		log(request,"查询驾驶员移动终端信息", "查询驾驶员移动终端信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String phone = String.valueOf(parampMap.get("phone"));
		String state = String.valueOf(parampMap.get("state"));
		String year = String.valueOf(parampMap.get("year"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(phone)) {
			tj += " and t.DriverPhone like ? ";
			queryList.add(phone+"%");
		}
		if (!isNull(state)) {
			tj += " and t.State = ? ";
			queryList.add(state);
		}
		
		String tj2 = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(phone)) {
			tj2 += " and t.DriverPhone like ? ";
			queryList.add(phone+"%");
		}
		if (!isNull(state)) {
			tj2 += " and t.State = ? ";
			queryList.add(state);
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		// 驾驶员移动终端表
		String ydzdSql = "select (select count(*) from tb_driverapp_"+year+"  t  where 1 = 1 "+ tj+ ") as count, tt.* from ("+ "select   t.*,t2.CompanyName from  tb_driverapp_"+year+"  t,tb_company t2 "
				+ "where t.CompanyId = t2.companyid "+ tj2+ " )tt limit ?,?";
		List<Map<String, Object>> ydzdlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ydzdSql,queryList.toArray()));
		map.put("datas", ydzdlist);
		getCount(map, ydzdlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询驾驶员统计信息
	 */
	public String findJsytjxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询驾驶员统计信息", "基础信息查询 → 查询驾驶员统计信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String LicenseId = String.valueOf(parampMap.get("jszh"));
		String begintime = String.valueOf(parampMap.get("begintime"));
		String endtime = String.valueOf(parampMap.get("endtime"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		if (!isNull(begintime) && !isNull(endtime)) {
			tj += " and t.UpdateTime BETWEEN ?  AND ? ";
			queryList.add(begintime);
			queryList.add(endtime);
		}
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(LicenseId)) {
			tj += " and t.LicenseId like ? ";
			queryList.add(LicenseId+"%");
		}
		
		String tj2 = "";
		if (!isNull(begintime) && !isNull(endtime)) {
			tj2 += " and t.UpdateTime BETWEEN ? AND ? ";
			queryList.add(begintime);
			queryList.add(endtime);
		}
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(LicenseId)) {
			tj2 += " and t.LicenseId like ? ";
			queryList.add(LicenseId+"%");
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		String begin = begintime.substring(2, 7).replaceAll("-", "");
		// 驾驶员统计信息
		String tjxxSql = "select (select count(*) from  tb_driverstat_" + begin+ "  t where 1 = 1 " + tj + ") as count ,tt.* from ("
				+ "select   t.*,t2.CompanyName from  tb_driverstat_" + begin+ "  t,tb_company t2 "
				+ " where t.CompanyId = t2.companyid " + tj2+ " )tt limit ?,?";

		List<Map<String, Object>> tjxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(tjxxSql,queryList.toArray()));
		map.put("datas", tjxxlist);
		getCount(map, tjxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询驾驶员培训信息
	 */
	public String findJsypxxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询驾驶员培训信息", "基础信息查询 → 查询驾驶员培训信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String LicenseId = String.valueOf(parampMap.get("jszh"));
		String CourseName = String.valueOf(parampMap.get("pxkmc"));
		String year = String.valueOf(parampMap.get("year"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(LicenseId)) {
			tj += " and t.LicenseId like ? ";
			queryList.add(LicenseId+"%");
		}
		if (!isNull(CourseName)) {
			tj += " and t.CourseName like ? ";
			queryList.add(CourseName+"%");
		}
		String tj2 = "";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ?";
			queryList.add(ptname);
		}
		if (!isNull(LicenseId)) {
			tj2 += " and t.LicenseId like ? ";
			queryList.add(LicenseId+"%");
		}
		if (!isNull(CourseName)) {
			tj2 += " and t.CourseName like ? ";
			queryList.add(CourseName+"%");
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		// 驾驶员培训信息
		String pxxxSql = "select (select count(*) from tb_drivereducate_"+year+"  t where 1 = 1  " + tj + ") as count ,tt.* from ("
				+ "select  t.*,t2.CompanyName from  tb_drivereducate_"+year+"  t,tb_company t2 "
				+ "where t.CompanyId = t2.companyid " + tj2 + ")tt limit ?,?";
		List<Map<String, Object>> pxxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(pxxxSql,queryList.toArray()));
		System.out.println("驾驶员培训信息pxxxSql:" + pxxxSql);

		map.put("datas", pxxxlist);
		getCount(map, pxxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合订单查询 → 查询订单信息
	 */
	public String findDdxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询订单信息", "基础信息查询 → 查询订单信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String key = String.valueOf(parampMap.get("key"));
		String value = String.valueOf(parampMap.get("value"));
		String begintime = String.valueOf(parampMap.get("begintime"));
		String endtime = String.valueOf(parampMap.get("endtime"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		
		List <Object> queryList=new  ArrayList<Object>();
		List <Object> queryList1=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		if (!isNull(begintime) && !isNull(endtime)) {
			tj += " and t.DistributeTime BETWEEN ? AND  ? ";
			queryList.add(begintime);
			queryList.add(endtime);
			queryList1.add(begintime);
			queryList1.add(endtime);
		}
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
			queryList1.add(ptname);
		}
		if (!isNull(value) && key.equals("车牌号码")) {
			tj += " and t.VehicleNo = ? ";
			queryList.add(value);
			queryList1.add(value);
		} else if (!isNull(value) && key.equals("驾驶员电话")) {
			tj += " and t.DriverPhone = ? ";
			queryList.add(value);
			queryList1.add(value);
		} else if (!isNull(value) && key.equals("驾驶证号")) {
			tj += " and t.LicenseId = ? ";
			queryList.add(value);
			queryList1.add(value);
		} else if (!isNull(value) && key.equals("订单编号")) {
			tj += " and t.OrderId = ? ";
			queryList.add(value);
			queryList1.add(value);
		}

		String begin = begintime.substring(2, 7).replaceAll("-", "");
		String getCountSql = "select count(*) as count from  tb_ordermatch_"+ begin+ "  t where 1 = 1 "+ tj ; 
		String ddSql = "select * from (select t1.*,t2.abb_name CompanyName from" +
				" (select t.* from  tb_ordermatch_"+ begin + " t where 1 = 1 " + tj + " order by DistributeTime desc ) t1" +
				" ,TB_GLOBAL_COMPANY t2" +
				" where t1.CompanyId = t2.company_id)tt limit ?, ?";
		
		queryList1.add((pageIndex - 1) * pageSize);
		queryList1.add(pageSize);
		
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ddSql,queryList1.toArray()));
		List<Map<String, Object>> countList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(getCountSql,queryList.toArray()));
		map.put("datas", list);
		getCount(map, countList);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 *  综合订单信息 → 查询支付明细信息
	 */
	public String findPay(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询支付明细信息", "基础信息查询 → 查询支付明细信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String orderid = String.valueOf(parampMap.get("orderid"));
		String distributetime = String.valueOf(parampMap.get("distributetime"));
		Map<String, Object> map = new HashMap<String, Object>();
		String begin = distributetime.substring(2, 7).replaceAll("-", "");
		String paySql = "select t2.abb_name CompanyName,t.* from  tb_operatepay_"+ begin+ " t,TB_GLOBAL_COMPANY t2 "
				+ "where t.CompanyId = t2.company_id and t.OrderId = ? ";
		List<Map<String, Object>> paylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(paySql,orderid));
		map.put("datas", paylist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 综合订单信息 → 查询投诉信息
	 */
	public String findComplaint(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询投诉信息", "基础信息查询 → 查询投诉信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String orderid = String.valueOf(parampMap.get("orderid"));
		String distributetime = String.valueOf(parampMap.get("distributetime"));
		String begin = distributetime.substring(0, 4);
		Map<String, Object> map = new HashMap<String, Object>();
		String paySql = "select t2.abb_name CompanyName,t.* from  tb_ratedpassengercpm_"+begin+" t,TB_GLOBAL_COMPANY t2 "
				+ "where t.CompanyId = t2.company_id and t.OrderId = ? ";
		List<Map<String, Object>> paylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(paySql,orderid));
		map.put("datas", paylist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	/**
	 * 基础信息查询 → 查询上线率
	 */
	public String findSxl(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询上线率", "基础信息查询 → 查询上线率");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String today = String.valueOf(parampMap.get("time"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(today)) {
			map.put("msg", "时间不能为空");
			return jacksonUtil.toJson(map);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String yesterDay = "";
		String lastWeek = "";
		try {
			Date date = format.parse(today);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			yesterDay = format.format(date);
			calendar.add(Calendar.DATE, -7);
			date = calendar.getTime();
			lastWeek = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("msg", "时间格式出错");
			return jacksonUtil.toJson(map);
		}
		List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
		Map<String, Object> todayMap = analysis24(today, "今日", "online","tb_analysis", "time");
		Map<String, Object> yesterDayMap = analysis24(yesterDay, "昨日","online", "tb_analysis", "time");
		Map<String, Object> lastWeekMap = analysis24(lastWeek, "上周同比","online", "tb_analysis", "time");
		resultMaps.add(todayMap);
		resultMaps.add(yesterDayMap);
		resultMaps.add(lastWeekMap);
		map.put("datas", resultMaps);
		map.put("yesterDay", yesterDayMap);
		map.put("lastWeek", lastWeekMap);
		map.put("today", todayMap);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询周转率
	 */
	public String findZzlv(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询周转率", "基础信息查询 → 查询周转率");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String today = String.valueOf(parampMap.get("time"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(today)) {
			map.put("msg", "时间不能为空");
			return jacksonUtil.toJson(map);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String yesterDay = "";
		String lastWeek = "";
		try {
			Date date = format.parse(today);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			yesterDay = format.format(date);
			calendar.add(Calendar.DATE, -7);
			date = calendar.getTime();
			lastWeek = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("msg", "时间格式出错");
			return jacksonUtil.toJson(map);
		}
		List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
		Map<String, Object> todayMap = analysis24(today, "今日", "Turnover_rate","tb_analysis", "time");
		Map<String, Object> yesterDayMap = analysis24(yesterDay, "昨日","Turnover_rate", "tb_analysis", "time");
		Map<String, Object> lastWeekMap = analysis24(lastWeek, "上周同比","Turnover_rate", "tb_analysis", "time");
		resultMaps.add(todayMap);
		resultMaps.add(yesterDayMap);
		resultMaps.add(lastWeekMap);
		map.put("datas", resultMaps);
		map.put("yesterDay", yesterDayMap);
		map.put("lastWeek", lastWeekMap);
		map.put("today", todayMap);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询周转量
	 */
	public String findZzliang(HttpServletRequest request, String postData) {
		log(request,"基础信息查询 → 查询周转量", "基础信息查询 → 查询周转量");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() { });
		String today = String.valueOf(parampMap.get("time"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (isNull(today)) {
			map.put("msg", "时间不能为空");
			return jacksonUtil.toJson(map);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String yesterDay = "";
		String lastWeek = "";
		try {
			Date date = format.parse(today);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			yesterDay = format.format(date);
			calendar.add(Calendar.DATE, -7);
			date = calendar.getTime();
			lastWeek = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("msg", "时间格式出错");
			return jacksonUtil.toJson(map);
		}
		List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
		Map<String, Object> todayMap = analysis24(today, "今日", "Turnover_volume", "tb_analysis", "time");
		Map<String, Object> yesterDayMap = analysis24(yesterDay, "昨日", "Turnover_volume", "tb_analysis", "time");
		Map<String, Object> lastWeekMap = analysis24(lastWeek, "上周同比", "Turnover_volume", "tb_analysis", "time");
		resultMaps.add(todayMap);
		resultMaps.add(yesterDayMap);
		resultMaps.add(lastWeekMap);
		map.put("datas", resultMaps);
		map.put("yesterDay", yesterDayMap);
		map.put("lastWeek", lastWeekMap);
		map.put("today", todayMap);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 
	 * @param time
	 *            时间做筛选用
	 * @param day
	 *            时间 ： 今日 昨日 上周同比
	 * @param fieldname
	 *            字段名称 ： 数据库内要读取的字段名称
	 * @param tablename
	 *            表名
	 * @param condition
	 *            数据库内 ： 做筛选用的字段名
	 * @return
	 */
	public Map<String, Object> analysis24(String time, String day, String fieldname, String tablename, String condition) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String sql = "select " + fieldname + " from " + tablename + " where " + condition + " like '" + time + "%' ";
		resultMap.put("time", day);
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		if (list.size() > 0) {
			String[] value = String.valueOf(list.get(0).get(fieldname)).split(";");
			for (int i = 0; i < 24; i++) {
				if (i < value.length) {
					resultMap.put("y" + i, value[i]);
				} else {
					resultMap.put("y" + i, "");
				}
			}
		} else {
			for (int i = 0; i < 24; i++) {
				resultMap.put("y" + i, "");
			}
		}
		return resultMap;
	}

	/**
	 * 基础信息查询 → 查询平台基本信息
	 */
	public String findPtjcxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询-查询平台基本信息", "基础信息查询-查询平台基本信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String state = String.valueOf(parampMap.get("state"));
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = "";
		List <Object> queryList=new  ArrayList<Object>();
		queryList.add(state);
		if (!ptname.equals("0")) {
			tj += " and  CompanyId = ? ";
			queryList.add(ptname);
		}
		// 平台基本信息
		String ptjcxxSql = "select * from  tb_company where  state = ? " + tj;
		List<Map<String, Object>> ptjcxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(ptjcxxSql,queryList.toArray()));
		map.put("ptjcxx", ptjcxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询车辆基本信息
	 */
	public String findCljcxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询-查询车辆基本信息", "基础信息查询-查询车辆基本信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String vehino = String.valueOf(parampMap.get("vehino"));
		String state = String.valueOf(parampMap.get("state"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		List <Object> queryList=new  ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String tj = " and t.state = ? ";
		queryList.add(state);
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(vehino)) {
			tj += " and t.VehicleNo like ? ";
			queryList.add(vehino+"%");
		}
		String tj2 = " and t.state = ?  ";
		queryList.add(state);
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(vehino)) {
			tj2 += " and t.VehicleNo like ? ";
			queryList.add(vehino+"%");
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);
		// 车辆基本信息
		String cljcxxSql = "select (select count(*) from tb_vehiclebaseinfo t where 1 = 1  " + tj + ") as count,tt.* from ("
				+ "select t2.CompanyName,t.*  from  tb_vehiclebaseinfo t,tb_company t2 where t.CompanyId = t2.companyid " + tj2 + " )tt limit ?,?";
		List<Map<String, Object>> cljcxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(cljcxxSql,queryList.toArray()));

		getCount(map, cljcxxlist);
		map.put("datas", cljcxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}

	/**
	 * 基础信息查询 → 查询驾驶员基本信息
	 */
	public String findJsyjcxx(HttpServletRequest request, String postData) {
		log(request,"基础信息查询-查询驾驶员基本信息", "基础信息查询-查询驾驶员基本信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ptname = String.valueOf(parampMap.get("ptname"));
		String jszh = String.valueOf(parampMap.get("jszh"));
		String jsyname = String.valueOf(parampMap.get("jsyname"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

		List <Object> queryList=new  ArrayList<Object>();
		String tj = " ";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(jszh)) {
			tj += " and t.LicenseId like ? ";
			queryList.add(jszh+"%");
		}
		if (!isNull(jsyname)) {
			tj += " and t.DriverName like ? ";
			queryList.add(jsyname+"%");
		}
		
		String tj2 = " ";
		if (!isNull(ptname) && !ptname.equals("0")) {
			tj2 += " and t.CompanyId = ? ";
			queryList.add(ptname);
		}
		if (!isNull(jszh)) {
			tj2 += " and t.LicenseId like ? ";
			queryList.add(jszh+"%");
		}
		if (!isNull(jsyname)) {
			tj2 += " and t.DriverName like ? ";
			queryList.add(jsyname+"%");
		}
		queryList.add(((pageIndex - 1) * pageSize));
		queryList.add(pageSize);

		// 驾驶员基本信息
		String jsyjcxxSql = "select (select count(*) from tb_driverbaseinfo t where 1 = 1 " + tj + ") as count, tt.* from ("
				+ "select t2.CompanyName,t.* from  tb_driverbaseinfo t,tb_company t2 where t.CompanyId = t2.companyid " + tj2 + " )tt limit ?, ?";
		System.out.println("驾驶员基本信息jsyjcxxSql：" + jsyjcxxSql);
		
		List<Map<String, Object>> jsyjcxxlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(jsyjcxxSql,queryList.toArray()));
		
		map.put("datas", jsyjcxxlist);
		getCount(map, jsyjcxxlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	/**
	 * 订单周信息
	 */
	public String findOrderWeek(HttpServletRequest request, String postData) {
		log(request,"订单周信息", "订单周信息");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(parampMap.get("time"));

		Map<String, Object> map = new HashMap<String, Object>();
		// 车辆基本信息
		String sumSql = "select date_format(t.update_time,'%H') as datetime, sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
				+ "where t.companyid = t2.company_id and  date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid in(select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY) GROUP BY date_format(t.update_time,'%H')  ORDER BY date_format(t.update_time,'%H') asc";
		
		List<Map<String, Object>> sumlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sumSql,time));
		String sumCompanySql = "select  sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
				+ "where t.companyid = t2.company_id and  date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid in(select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY) ";
		List<Map<String, Object>> sumCompanylist = jdbcTemplate.queryForList( sumCompanySql,time);
		
		// 李精卫我帮你改了,不用谢我
		List<Map<String, Object>> companys1 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY"));
		
		String[] companys = new String[companys1.size()];
		
		for(int i=0;i<companys1.size();i++){
			companys[i] = String.valueOf(companys1.get(i).get("COMPANYID"));
		}
		List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
		for(int i = 0;i < companys.length ; i++){
			String Sql = "select date_format(t.update_time,'%H') as datetime, t.order_num,t2.abb_name as companyename from tb_order_week t,tb_global_company t2"
					+ " where t.companyid = t2.company_id and date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid = ? ORDER BY date_format(t.update_time,'%H') asc";
			List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( Sql,time,companys[i]));
			String onlyCompanySql = "select  t.companyid ,sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
					+ "where t.companyid = t2.company_id and  date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid = ? "
					+ " GROUP BY t.companyid ";
			List<Map<String, Object>> onlyCompanylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( onlyCompanySql,time,companys[i]));
			
			if(list.size() > 0){
				getField(String.valueOf(list.get(0).get("companyename")),resultlist,sumlist,list,sumCompanylist,onlyCompanylist);
			}
		}
		map.put("datas", resultlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	
	/**
	 * 订单周信息导出(李精卫我帮你写了，不用谢我，我是雷锋)
	 */
	public List<Map<String, Object>> findOrderWeekExcel(String time,HttpServletRequest request) {
		log(request,"订单周信息导出", "订单周信息导出");
		// 车辆基本信息
		String sumSql = "select date_format(t.update_time,'%H') as datetime, sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
				+ "where t.companyid = t2.company_id and   date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid in('aa','didi','caocao','shouyue','shenzhou','wanshun') GROUP BY date_format(t.update_time,'%H') ORDER BY date_format(t.update_time,'%H') asc";
		
		List<Map<String, Object>> sumlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sumSql,time));
		String sumCompanySql = "select  sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
				+ "where t.companyid = t2.company_id and   date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid in('aa','didi','caocao','shouyue','shenzhou','wanshun') ";
		List<Map<String, Object>> sumCompanylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sumCompanySql,time));
		
		// 李精卫我帮你改了,不用谢我
		List<Map<String, Object>> companys1 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList("select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY"));
		
		String[] companys = new String[100];
		
		for(int i=0;i<companys1.size();i++){
			companys[i] = String.valueOf(companys1.get(i).get("COMPANYID"));
		}
		
		List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
		for(int i = 0;i < companys.length ; i++){
			String Sql = "select date_format(t.update_time,'%H') as datetime, t.order_num,t2.abb_name as companyename from tb_order_week t,tb_global_company t2"
					+ " where t.companyid = t2.company_id and  date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid = ? ORDER BY date_format(t.update_time,'%H') asc";
			List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( Sql,time,companys[i]));
			String onlyCompanySql = "select  t.companyid ,sum(t.order_num) as sumorder  from tb_order_week t,tb_global_company t2 "
					+ "where t.companyid = t2.company_id and   date_format(t.update_time,'%Y-%m-%d') = ? and t.companyid = ? "
					+ " GROUP BY t.companyid ";
			List<Map<String, Object>> onlyCompanylist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( onlyCompanySql,time,companys[i]));
			if(list.size() > 0){
				getField(String.valueOf(list.get(0).get("companyename")),resultlist,sumlist,list,sumCompanylist,onlyCompanylist);
			}
		}
		return resultlist;
	}
	
	public void getField(String fieldName,List<Map<String, Object>> list,
			List<Map<String, Object>> sumList,List<Map<String, Object>> fieldList,List<Map<String, Object>> sumCompanylist,List<Map<String, Object>> onlyCompanylist){
		DecimalFormat df = new DecimalFormat("#.00");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		map.put("companyName",fieldName);
		fieldMap.put("companyName",fieldName);
		
		if(onlyCompanylist.size() > 0 ){
			Double ordernum = Double.valueOf(String.valueOf(onlyCompanylist.get(0).get("sumorder")));
			fieldMap.put("sumNumber",ordernum);
			if(sumCompanylist.size() > 0){
				Double sumorder = Double.valueOf(String.valueOf(sumCompanylist.get(0).get("sumorder")));
				fieldMap.put("sumNumber",ordernum);
				map.put("sumNumber", Double.valueOf(df.format((ordernum/ sumorder)* 100) )+"%");
			}
		}else{
			fieldMap.put("sumNumber","0");
			map.put("sumNumber","0%");
		}
		if(fieldList.size() > 0 ){
			for (int i = 0; i < 24; i++) {
				String hour = i+"";
				if(i < 10){
					hour = "0"+i;
				}
				for (int k = 0; k < sumList.size(); k++) {
					if(String.valueOf(sumList.get(k).get("datetime")).equals(hour)){
						for (int j = 0; j < fieldList.size(); j++) {
							if(String.valueOf(fieldList.get(j).get("datetime")).equals(String.valueOf(sumList.get(k).get("datetime")))){
								Double ordernum = Double.valueOf(String.valueOf(fieldList.get(j).get("order_num")));
								Double sumorder = Double.valueOf(String.valueOf(sumList.get(k).get("sumorder")));
								fieldMap.put("y"+i,ordernum);
								map.put("y"+i, Double.valueOf(df.format((ordernum/ sumorder)* 100) )+"%");
							}
						}
					}
				}
			}
		}
		list.add(fieldMap);
		list.add(map);
		
	}
	/**
	 * 周订单信息查询
	 */
	public String findOrderWeekEchart(HttpServletRequest request, String postData) {
		log(request,"周订单信息查询", "周订单信息查询");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(parampMap.get("time"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map = new HashMap<String, Object>();
		String lastWeek = "";
		try {
			Date date = format.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -7);
			date = calendar.getTime();
			lastWeek = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("msg", "时间格式出错");
			return jacksonUtil.toJson(map);
		}
		
		String sumSql = "select  date_format(t.update_time,'%Y-%m-%d') AS datetime, sum(t.order_num) as sumorder  from tb_order_week t "
				+ "where   date_format(t.update_time,'%Y-%m-%d') <= ? and  date_format(t.update_time,'%Y-%m-%d') >= ? GROUP BY  date_format(t.update_time,'%Y-%m-%d') ORDER BY  date_format(t.update_time,'%Y-%m-%d') asc";
		List<Map<String, Object>> sumlist = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sumSql,time,lastWeek));
		if(sumlist.size() > 0){
			for(int i = 0; i< sumlist.size(); i++){
				Map<String, Object> listMap= new HashMap<String, Object>();
				sumlist.get(i).put("sumorder", sumlist.get(i).get("sumorder"));
				sumlist.get(i).put("date", sumlist.get(i).get("datetime"));
			}
		}
		map.put("datas", sumlist);
		map.put("msg", "0");
		return jacksonUtil.toJson(map);
	}
	
	/**
	 * 异常经营分析
	 */
	public String findErrorOperate(HttpServletRequest request, String postData) {
		log(request,"异常经营分析", "异常经营分析");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(parampMap.get("time"));
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select t.* ,t.update_time as TIME, concat(round((t.compliance_order / t.day_order) * 100 ),'%') as compliance_rate,t2.abb_name CompanyName" +
				" from tb_error_operate t,TB_GLOBAL_COMPANY t2" +
				" where t.companyid = t2.company_id and update_time like CONCAT(date_format(?,'%Y-%m-%d'),'%')";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sql,time));
		
		String sql1 = "select '全部' CompanyName,concat(round((sum(compliance_order) / sum(day_order)) * 100 ),'%') compliance_rate,sum(CAR_NUM) CAR_NUM,sum(YBZS) YBZS" +
				",sum(DRIVER_NUM) DRIVER_NUM,sum(DRIVER_LICENSE_NUM) DRIVER_LICENSE_NUM,sum(DAY_ORDER) DAY_ORDER,sum(COMPLIANCE_ORDER) COMPLIANCE_ORDER" +
				",sum(COMPLIANCE_PEOPLE) COMPLIANCE_PEOPLE,sum(CAR_COMPLIANCE_ORDER) CAR_COMPLIANCE_ORDER,sum(CAR_COMPLIANCE_NUM) CAR_COMPLIANCE_NUM" +
				",sum(PEOPLE_COMPLIANCE_ORDER) PEOPLE_COMPLIANCE_ORDER,sum(PEOPLE_COMPLIANCE_NUM) PEOPLE_COMPLIANCE_NUM,sum(VIOLATION_ORDER) VIOLATION_ORDER" +
				",sum(VIOLATION_PEOPLE) VIOLATION_PEOPLE from (select t.*,t.update_time as TIME" +
				", concat(round((t.compliance_order / t.day_order) * 100 ),'%')  as  compliance_rate,t2.abb_name CompanyName " +
				" from tb_error_operate t,TB_GLOBAL_COMPANY t2" +
				" where t.companyid = t2.company_id and update_time like CONCAT(date_format(?,'%Y-%m-%d'),'%') ) t";
		List<Map<String, Object>> list1 = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql1,time));
		
		map.put("datas", list);
		map.put("list", list1);
		return jacksonUtil.toJson(map);
	}

	/**
	 * 未营运车辆统计
	 * @throws ParseException 
	 */
	public List<Map<String, Object>>  findCarNotOperate(HttpServletRequest request, String postData ,boolean isExport)  {
		log(request,"未营运车辆统计", "未营运车辆统计");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehino = String.valueOf(parampMap.get("vehino"));
		String type = String.valueOf(parampMap.get("type"));
		int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
		int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String tj = ""; //拼接条件
		List <Object> queryList=new  ArrayList<Object>();
		if(!isNull(vehino)){
			tj += " and t.AUTO_NUM like ? ";
			queryList.add(vehino+"%");
		}
		
		//时间工具类 
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.add(Calendar.MONTH, -1);
		String time= format.format(calendar.getTime());
		//type为0 查询所有近期未营运车辆
		if(!isNull(type) && type.equals("0")){
			tj += " and (  t.last_time< ?  or t.last_time is null ) ";
			queryList.add(time);
		}
		if(!isNull(type) && !type.equals("0")){
			Calendar calendar2 = Calendar.getInstance();
			calendar.setTime(date);
			calendar2.setTime(date);
			String beforeTime = "";
			String endTime = "";
			switch (type){
				case "1" : //超过一个月未营运
					calendar.add(Calendar.MONTH, -1);
					endTime= format.format(calendar.getTime());
					calendar2.add(Calendar.MONTH, -3);
					beforeTime = format.format(calendar2.getTime());
					tj += " and t.last_time >= ?  and t.last_time < ?  ";
					queryList.add(beforeTime);
					queryList.add(endTime);
					break;
				case "2" : //超过3个月未营运
					calendar.add(Calendar.MONTH, -3);
					endTime= format.format(calendar.getTime());
					calendar2.add(Calendar.MONTH, -6);
					beforeTime = format.format(calendar2.getTime());
					tj += " and t.last_time >=? and t.last_time < ? ";
					queryList.add(beforeTime);
					queryList.add(endTime);
					break;
				case "3" : //超过6个月未营运
					calendar.add(Calendar.MONTH, -6);
					endTime= format.format(calendar.getTime());
					calendar2.add(Calendar.MONTH, -12);
					beforeTime = format.format(calendar2.getTime());
					tj += " and t.last_time >= ?  and t.last_time < ? ";
					queryList.add(beforeTime);
					queryList.add(endTime);
					break;
				case "4" : //超过12个月未营运
					calendar.add(Calendar.MONTH, -12);
					tj += " and t.last_time < ? ";
					queryList.add(beforeTime);
					break;
			}
			//未参与营运
			if(type.equals("5")){
				tj += " and t.last_time is null ";
			}
		}
		
		//queryList1 为正常查询参数集合 因为有分页所以要把 分页参数加进去
		//queryList 为导出的参数集合
		List <Object> queryList1=new  ArrayList<Object>();
		//因为分页的特性 参数需要传两次
		for(int i=0;i<queryList.size();i++){
			queryList1.add(queryList.get(i));
		}
		for(int i=0;i<queryList.size();i++){
			queryList1.add(queryList.get(i));
		}
		queryList1.add(((pageIndex - 1) * pageSize));
		queryList1.add(pageSize );

		List<Map<String, Object>> list = new ArrayList<>();
		//如果是导出的sql
		if(isExport){
			String sql = "select AUTO_NUM,OWNER,OWNER_TEL, date_format(start_date,'%Y-%m-%d') as START_DATE ,date_format(end_date,'%Y-%m-%d') as END_DATE ,date_format(license_register_date,'%Y-%m-%d') as LICENSE_REGISTER_DATE,STATUS,last_time as LAST_TIME "
					+ " from tb_operation t where t.status = '10' "+tj;
			list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sql,queryList.toArray()));
		}else{
			String sql = "select (select count(*) from tb_operation t where t.status = '10' "+tj
					+") as count, tt.* from( select t.* from tb_operation t where t.status = '10' "+tj
					+"  ) tt limit ?, ?";
			System.out.println(sql);
			list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sql,queryList1.toArray()));
		}
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				String date2 = String.valueOf(list.get(i).get("last_time"));
				//如果时间不为空，则要判断是多久未营运
				switch (type){
					case "0" : //全部
						if(!isNull(date2)){
							long last_time = 0;
							try {
								last_time = format.parse(date2).getTime();
							} catch (ParseException e) {
								e.printStackTrace();
								return list;
							}
							int days = (int) ((new Date().getTime() - last_time) / (1000*3600*24));
							if(days < 30){
								list.get(i).put("last_type", date2);
							}else if(days >= 30 && days < 91 ){
								list.get(i).put("last_type", "超过一个月未营运");
							}else if(days >= 91 && days < 182 ){
								list.get(i).put("last_type", "超过三个月未营运");
							}else if(days >= 182 && days < 365 ){
								list.get(i).put("last_type", "超过六个月未营运");
							}else if(days >= 365 ){
								list.get(i).put("last_type", "超过一年未营运");
							}
						}else{
							list.get(i).put("last_type", "未参加营运");
						}
						break;
					case "1" : //超过一个月未营运
						list.get(i).put("last_type", "超过一个月未营运");
						break;
					case "2" : //超过3个月未营运
						list.get(i).put("last_type", "超过三个月未营运");
						break;
					case "3" : //超过6个月未营运
						list.get(i).put("last_type", "超过六个月未营运");
						break;
					case "4" : //超过12个月未营运
						list.get(i).put("last_type", "超过一年未营运");
						break;
					case "5" : //未营运
						list.get(i).put("last_type", "未参加营运");
						break;
				}
			}
		}
		return list;
		
	}
	/**
	 * 未营运车辆分析
	 * @param request
	 * @param postData
	 * @param isExport  判断是否为导出
	 * @return
	 */
	public String findCarNotOperateForList(HttpServletRequest request, String postData ,boolean isExport){
		List<Map<String, Object>> list = findCarNotOperate(request, postData, false);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", "0");
		if(list.size() > 0 ){
			map.put("count", list.get(0).get("count"));
		}
		map.put("datas",list);
		return jacksonUtil.toJson(map);
	}
	/**
	 * 异常经营分析 车牌号码查询
	 */
	public String findNotOperateVehicleNo(HttpServletRequest request, String postData) {
		log(request,"异常经营分析 车牌号码查询", "异常经营分析 车牌号码查询");
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehino = String.valueOf(parampMap.get("vehino"));
		Map<String, Object> map = new HashMap<String, Object>();
		List <Object> queryList=new  ArrayList<Object>();
		queryList.add(vehino+"%");
		String sql = "select  PLATE_NUMBER from tb_vehicle_info where PLATE_NUMBER like ? ";
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList( sql,queryList.toArray()));
		map.put("datas", list);
		return jacksonUtil.toJson(map);
	}
	
}
