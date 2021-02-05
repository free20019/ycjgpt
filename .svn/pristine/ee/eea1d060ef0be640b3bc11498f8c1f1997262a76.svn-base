package mvc.service;

import helper.JacksonUtil;
import helper.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OperationAndWarningService {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate = null;
	@Autowired
	protected JdbcTemplate jdbcTemplate4 = null;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = format.format(new Date());
	private void log(HttpServletRequest request,String model,String s){
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,?)";
//		jdbcTemplate.update(sql,username,model,s,time);
	}

	// 违规营运分析
	public String getViolationOperationAnalysis(HttpServletRequest request, String platformName,String sTime,String eTime,String vehicleNumber,String driverNumber,String violationType,String page,String pageSize) {
//		log(request,"营运动态监测-日常监测预警-违规营运分析", "营运动态监测-日常监测预警-违规营运分析");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if(sTime == null || "".equals(sTime)){
//			sTime = format.format(new Date());
//		}
//		if(eTime == null || "".equals(eTime)){
//			eTime = format.format(new Date());
//		}
//		if(platformName.equals("全部选择")){
//			platformName = "";
//		}
//		Integer str = Integer.valueOf(sTime.replaceAll("-", "").substring(2, 6));
//		Integer etr = Integer.valueOf(eTime.replaceAll("-", "").substring(2, 6));
//		List<Integer> tableList = new ArrayList<Integer>();
//
//		for(int i=str;i<=etr;i++){
//			tableList.add(i);
//		}
//		List <Object> queryList=new  ArrayList<Object>();
//		List <Object> queryList1=new  ArrayList<Object>();
//		String pageSql = "select * from (select A.* from (";
//		String countSql = "select count(*) from (";
//
//		String finalSql = "";
//
//		for(int i=0;i<tableList.size();i++){
//			String sql = "select * from (select tv.*,tg.ABB_NAME from TB_GLOBAL_COMPANY tg,(select tt.*,STIME start_time,ETIME end_time" +
//					" from TB_VIOLATION_ANALYSIS_"+tableList.get(i)+" tt where 1 = 1";
//
//			if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
//				sql += " and STIME >= ?";
//				queryList.add(sTime + " 00:00:00");
//				queryList1.add(sTime + " 00:00:00");
//			}
//			if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
//				sql += " and STIME <= ?";
//				queryList.add(eTime + " 23:59:59");
//				queryList1.add(eTime + "23:59:59");
//			}
//			if(vehicleNumber!=null&&vehicleNumber.length()>0&&!vehicleNumber.isEmpty()&&!vehicleNumber.equals("")){
//				sql += " and VEHICLE_NUMBER = ?";
//				queryList.add(vehicleNumber);
//				queryList1.add(vehicleNumber);
//			}
//			if(driverNumber!=null&&driverNumber.length()>0&&!driverNumber.isEmpty()&&!driverNumber.equals("")){
//				sql += " and DRIVER_NUMBER = ?";
//				queryList.add(driverNumber);
//				queryList1.add(driverNumber);
//			}
//			if(violationType!=null&&violationType.length()>0&&!violationType.isEmpty()&&!violationType.equals("")){
//				sql += " and VIOLATION_TYPE = ?";
//				queryList.add(violationType);
//				queryList1.add(violationType);
//			}
//			sql += ")tv where tg.COMPANY_ID = tv.PLATFORM_COMPANY";
//			finalSql += sql += ")abc where 1 = 1";
//			if(platformName!=null&&platformName.length()>0&&!platformName.isEmpty()&&!platformName.equals("")){
//				finalSql += " and abc.ABB_NAME = ?";
//				queryList.add(platformName);
//				queryList1.add(platformName);
//			}
//
//			finalSql += " UNION ALL ";
//		}
//
//		finalSql = finalSql.substring(0, finalSql.lastIndexOf("UNION ALL"));
//
//		countSql += finalSql + ") end";
//		System.out.println("countsql:"+countSql);
//		Integer count = jdbcTemplate.queryForObject(countSql, queryList1.toArray(), Integer.class);
//
//		queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
//		queryList.add(Integer.valueOf(pageSize));
//
//		pageSql += finalSql + ") A ) AA limit ?,?";
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(pageSql, queryList.toArray()));
//		map.put("datas", list);// 表格数据
//		map.put("count", count);// 分页总数
//		return jacksonUtil.toJson(map);
		log(request,"营运动态监测-日常监测预警-违规营运分析", "营运动态监测-日常监测预警-违规营运分析");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(sTime == null || "".equals(sTime)){
			sTime = format.format(new Date());
		}
		if(eTime == null || "".equals(eTime)){
			eTime = format.format(new Date());
		}
		if(platformName.equals("全部选择")){
			platformName = "";
		}
		Integer str = Integer.valueOf(sTime.replaceAll("-", "").substring(2, 6));
		Integer etr = Integer.valueOf(eTime.replaceAll("-", "").substring(2, 6));
		List<Integer> tableList = new ArrayList<Integer>();

		for(int i=str;i<=etr;i++){
			tableList.add(i);
		}
		List <Object> queryList=new  ArrayList<Object>();
		List <Object> queryList1=new  ArrayList<Object>();
		String pageSql = "select A.* from (";
		String countSql = "select count(*) from (";

		String finalSql = "";

		for(int i=0;i<tableList.size();i++){
			String sql = "select tt.*,STIME start_time,ETIME end_time,tg.ABB_NAME from TB_GLOBAL_COMPANY tg, TB_VIOLATION_ANALYSIS_"+tableList.get(i)+" tt" +
					" where tg.COMPANY_ID = tt.PLATFORM_COMPANY";

			if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
				sql += " and tt.STIME >= ?";
				queryList.add(sTime + " 00:00:00");
				queryList1.add(sTime + " 00:00:00");
			}
			if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
				sql += " and tt.STIME <= ?";
				queryList.add(eTime + " 23:59:59");
				queryList1.add(eTime + " 23:59:59");
			}
			if(vehicleNumber!=null&&vehicleNumber.length()>0&&!vehicleNumber.isEmpty()&&!vehicleNumber.equals("")){
				sql += " and tt.VEHICLE_NUMBER = ?";
				queryList.add(vehicleNumber);
				queryList1.add(vehicleNumber);
			}
			if(driverNumber!=null&&driverNumber.length()>0&&!driverNumber.isEmpty()&&!driverNumber.equals("")){
				sql += " and tt.DRIVER_NUMBER = ?";
				queryList.add(driverNumber);
				queryList1.add(driverNumber);
			}
			if(violationType!=null&&violationType.length()>0&&!violationType.isEmpty()&&!violationType.equals("")){
				sql += " and tt.VIOLATION_TYPE = ?";
				queryList.add(violationType);
				queryList1.add(violationType);
			}
			finalSql += sql;
			if(platformName!=null&&platformName.length()>0&&!platformName.isEmpty()&&!platformName.equals("")){
				finalSql += " and tg.ABB_NAME = ?";
				queryList.add(platformName);
				queryList1.add(platformName);
			}

			finalSql += " UNION ALL ";
		}

		finalSql = finalSql.substring(0, finalSql.lastIndexOf("UNION ALL"));

		countSql += finalSql + ") end";
		countSql = countSql.replaceAll("tt.*,STIME start_time,ETIME end_time,tg.ABB_NAME","1");
		System.out.println("countsql:"+countSql);
		Integer count = jdbcTemplate.queryForObject(countSql, queryList1.toArray(), Integer.class);

		queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
		queryList.add(Integer.valueOf(pageSize));

		pageSql += finalSql + " order by tt.stime desc limit ?,?) A";

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(pageSql, queryList.toArray()));
		map.put("datas", list);// 表格数据
		map.put("count", count);// 分页总数
		return jacksonUtil.toJson(map);
	}
	
	// 违规营运分析导出
	public List<Map<String, Object>> getViolationOperationAnalysisExcel(HttpServletRequest request, String platformName,String sTime,String eTime,String vehicleNumber,String driverNumber,String violationType,String page,String pageSize) {
		log(request,"营运动态监测-日常监测预警-违规营运分析导出", "营运动态监测-日常监测预警-违规营运分析导出");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(sTime == null || "".equals(sTime)){
			sTime = format.format(new Date());
		}
		if(eTime == null || "".equals(eTime)){
			eTime = format.format(new Date());
		}
		if(platformName.equals("全部选择")){
			platformName = "";
		}
		Integer str = Integer.valueOf(sTime.replaceAll("-", "").substring(2, 6));
		Integer etr = Integer.valueOf(eTime.replaceAll("-", "").substring(2, 6));
		List<Integer> tableList = new ArrayList<Integer>();
		
		for(int i=str;i<=etr;i++){
			tableList.add(i);
		}
		List <Object> queryList=new  ArrayList<Object>();
		String pageSql = "select * from (select A.* from (";
		
		String finalSql = "";
		
		for(int i=0;i<tableList.size();i++){
			String sql = "select * from (select tv.*,tg.ABB_NAME from TB_GLOBAL_COMPANY tg,(select tt.*,STIME start_time,ETIME end_time from TB_VIOLATION_ANALYSIS_"+tableList.get(i)+" tt where 1 = 1";
			
			if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
				sql += " and STIME >= ?";
				queryList.add(sTime + " 00:00:00");
			}
			if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
				sql += " and STIME <= ?";
				queryList.add(eTime + " 23:59:59");
			}
			if(vehicleNumber!=null&&vehicleNumber.length()>0&&!vehicleNumber.isEmpty()&&!vehicleNumber.equals("")){
				sql += " and VEHICLE_NUMBER = ?";
				queryList.add(vehicleNumber);
			}
			if(driverNumber!=null&&driverNumber.length()>0&&!driverNumber.isEmpty()&&!driverNumber.equals("")){
				sql += " and DRIVER_NUMBER = ?";
				queryList.add(driverNumber);
			}
			if(violationType!=null&&violationType.length()>0&&!violationType.isEmpty()&&!violationType.equals("")){
				sql += " and VIOLATION_TYPE = ?";
				queryList.add(violationType);
			}
			sql += ")tv where tg.COMPANY_ID = tv.PLATFORM_COMPANY";
			finalSql += sql += ")abc where 1 = 1";
			if(platformName!=null&&platformName.length()>0&&!platformName.isEmpty()&&!platformName.equals("")){
				finalSql += " and abc.ABB_NAME = ?";
				queryList.add(platformName);
			}
			
			finalSql += " UNION ALL ";
		}
		
		finalSql = finalSql.substring(0, finalSql.lastIndexOf("UNION ALL"));

		queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
		queryList.add(Integer.valueOf(pageSize));

		pageSql += finalSql + ") A ) AA limit ?,?";
		
		List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(pageSql, queryList.toArray()));
		return list;
	}
	
	public String getViolationOperationAnalysisVehicleNumber(HttpServletRequest request,String postData) {
		Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle_number = String.valueOf(parampMap.get("vehicle_number")) + "%";
		String time = String.valueOf(parampMap.get("time"));
		String str = "";
		if(time == null || "".equals(time)){
			SimpleDateFormat format = new SimpleDateFormat();
			String format2 = format.format(new Date());
			str = format2.replaceAll("-", "").substring(2, 6);
		}else{
			str = time.replaceAll("-", "").substring(2, 6);
		}
		String sql = "select vehicle_number from TB_VIOLATION_ANALYSIS_"+str+" where vehicle_number like ? GROUP BY vehicle_number";
		return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,vehicle_number)));
	}

	// 车辆超期预警
//	public String getVehicleOverdueWarning(HttpServletRequest request, String vehicleNumber,String sTime,String eTime,String page,String pageSize,String Situation) {
//		log(request,"营运动态监测-日常监测预警-车辆超期预警", "营运动态监测-日常监测预警-车辆超期预警");
//		String pageSql = "select * from (select A.*,ROWNUM RN from (";
//		String ss = "select abc.*,tb_owner.XM,tb_owner.TYPE,tb_owner.JYZMC from (";
//		String sql = "select * from (select a.OWNER_ID,a.PLATE_NUMBER,a.VIN,TO_CHAR(a.PRODUCTION_DATE,'yyyy-mm-dd') PRODUCTION_DATE,CASE WHEN ADD_MONTHS(a.PRODUCTION_DATE, 8*12) <= now() THEN '已超期' WHEN ADD_MONTHS(ADD_MONTHS(a.PRODUCTION_DATE, 8*12), -1) <= now() THEN '即将超期' ELSE '正常' END RESULT from tb_vehicle_info a where a.STATUS = 0";
//		String countSql = "select count(1) from (";
//		
//		List <Object> queryList=new  ArrayList<Object>();
//		List <Object> queryList1=new  ArrayList<Object>();
//		
//		if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
//			sql += " and TO_CHAR(a.PRODUCTION_DATE,'yyyy-mm') >= ?";
//			queryList.add(sTime);
//			queryList1.add(sTime);
//		}
//		if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
//			sql += " and TO_CHAR(a.PRODUCTION_DATE,'yyyy-mm') <= ?";
//			queryList.add(eTime);
//			queryList1.add(eTime);
//		}
//		if(vehicleNumber!=null&&vehicleNumber.length()>0&&!vehicleNumber.isEmpty()&&!vehicleNumber.equals("")){
//			sql += " and a.PLATE_NUMBER like ?";
//			queryList.add("%"+vehicleNumber+"%");
//			queryList1.add("%"+vehicleNumber+"%");
//		}
//		ss += sql + ") tt where 1 = 1";
//		if(Situation!=null&&Situation.length()>0&&!Situation.isEmpty()&&!Situation.equals("")){
//			ss += " and tt.RESULT = ?";
//			queryList.add(Situation);
//			queryList1.add(Situation);
//		}
//		
//		ss += ") abc LEFT JOIN tb_owner ON abc.OWNER_ID = tb_owner.id";
//		countSql += ss + ")";
//		pageSql += ss + ") A where ROWNUM <= ?)where RN >= ?";
//		
//		queryList.add((Integer.valueOf(page)*Integer.valueOf(pageSize)));
//		queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
//		
//		// echarts的数据
//		String echartsSql = "select tt.COUNT,tt.TIME from (select TO_CHAR(PRODUCTION_DATE,'yyyy') TIME,count(PRODUCTION_DATE) COUNT from tb_vehicle_info where TO_CHAR(PRODUCTION_DATE,'yyyy') <= TO_CHAR(now(),'yyyy') GROUP BY TO_CHAR(PRODUCTION_DATE,'yyyy') ORDER BY time DESC)tt where ROWNUM <= 9";
//		String overdueCountSql = "select count(1) from tb_vehicle_info where ADD_MONTHS(PRODUCTION_DATE, 8*12) <= now()";// 已超期数量
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql,queryList.toArray());
//		
//		for(int i=0;i<list.size();i++){
//			String type = String.valueOf(list.get(i).get("TYPE"));
//			String JYZMC = String.valueOf(list.get(i).get("JYZMC"));
//			if("1".equals(type)){// 当type为1时展示企业名称
//				list.get(i).put("XM", JYZMC);
//			}
//		}
//		
//		Integer count = jdbcTemplate.queryForObject(countSql, queryList1.toArray(), Integer.class);
//		Integer overdueCount = jdbcTemplate.queryForObject(overdueCountSql, Integer.class);
//		List<Map<String, Object>> echarts = jdbcTemplate.queryForList(echartsSql);
//
//		Map<String, Object> overdueCountMap = new HashMap<String, Object>();
//		overdueCountMap.put("TIME", "已超期");
//		overdueCountMap.put("COUNT", overdueCount);
//		echarts.add(overdueCountMap);		
//		map.put("datas", list);// 表格数据
//		map.put("count", count);// 分页总数
//		map.put("echarts", echarts);// echarts数据
//		return jacksonUtil.toJson(map);
//	}
	/**
	 * 车辆超期预警
	 */
	public String getVehicleOverdueWarningList(HttpServletRequest request,boolean isExport){
		List<Map<String, Object>> list = getVehicleOverdueWarning(request, false);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", "0");
		if(list.size() > 0 ){
			map.put("count", list.get(0).get("count"));
		}
		map.put("datas",list);
		return jacksonUtil.toJson(map);
	}
	
	/**
	 * 车辆超期预警
	 * @param request
	 * @param vehicleNumber
	 * @param page
	 * @param pageSize
	 * @param Situation   0：全部  1：已超期  2： 即将超期
	 * isExport 是否为导出调用  true：是
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String, Object>>  getVehicleOverdueWarning(HttpServletRequest request,boolean isExport) {
		String vehicleNumber = request.getParameter("vehicleNumber");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
		String Situation = request.getParameter("Situation");
		
		List<Object> queryList = new ArrayList<>();
		String tj = "";
		/* 算出一个月前*/
		Calendar calendar = Calendar.getInstance();
		String nowDate = format.format(new Date());
		calendar.add(Calendar.MONTH, +1);
		String time= format.format(calendar.getTime());
		queryList.add(nowDate); //case when 用到的 当前时间
		queryList.add(nowDate);//case when 用到的 当前时间
		queryList.add(time);//case when 用到的 一个月后的时间
		//条件判断
		if(Situation.equals("0")){ //全部
			tj += " and END_DATE < ? ";
			queryList.add(time);
		}else if(Situation.equals("2")){ // 2： 即将超期  30天以内的
			tj += " and END_DATE between ? and  ? ";
			queryList.add(nowDate);
			queryList.add(time);
		}else if(Situation.equals("1")){  //已超期
			tj += " and END_DATE < ? ";
			queryList.add(nowDate);
		}
		if(!"".equals(vehicleNumber) && !"null".equals(vehicleNumber) ){
			tj += " and replace(AUTO_NUM,'.','') like ? ";
			queryList.add(vehicleNumber+"%");
		}
		String sql = "";
		if(isExport){ //导出 语句
			sql = "SELECT replace(AUTO_NUM,'.','') as AUTO_NUM,OWNER,OWNER_TEL,DATE_FORMAT(START_DATE,'%Y-%m-%d') as START_DATE,DATE_FORMAT(END_DATE,'%Y-%m-%d') as END_DATE,DATE_FORMAT(LICENSE_REGISTER_DATE,'%Y-%m-%d') as LICENSE_REGISTER_DATE,STATUS,REMOVED,"
					+ "case when END_DATE < ? then '已超期' when END_DATE between ?  and ? then '即将超期' else '数据异常' end as TYPE "
					+ " FROM platcar_base_info WHERE STATUS = 10 and removed = 0 "+ tj ;
		}else{ //查询列表语句
			//因为分页的特性 需要传两遍参数
			List<Object> queryList1 = new ArrayList<>();
			for(int i=0;i<queryList.size();i++){
				queryList1.add(queryList.get(i));
			}
			queryList.remove(0); //需要删除 case when 用到的 当前时间
			queryList.remove(0);//需要删除 case when 用到的 当前时间
			queryList.remove(0);//需要删除 case when 用到的一个月后 时间
			System.out.println(queryList);
			for(int i=0;i<queryList.size();i++){
				queryList1.add(queryList.get(i));
			}
			queryList1.add(((page - 1) * pageSize));
			queryList1.add(pageSize);
			queryList = queryList1;
			//查询列表的sql
			 sql = "select replace(AUTO_NUM,'.','') as AUTO_NUM,OWNER,OWNER_TEL,DATE_FORMAT(START_DATE,'%Y-%m-%d') as START_DATE,DATE_FORMAT(END_DATE,'%Y-%m-%d') as END_DATE,DATE_FORMAT(LICENSE_REGISTER_DATE,'%Y-%m-%d') as LICENSE_REGISTER_DATE,STATUS,REMOVED, "
			 			+ "case when END_DATE < ? then '已超期' when END_DATE between ?  and ? then '即将超期' else '数据异常' end as TYPE "
						+ " ,(select count(*) from platcar_base_info where status = 10 and removed = 0 "+tj+") as count "
						+ " from platcar_base_info where status = 10 and removed = 0 "+tj +" limit ?,?";
		}
		List<Map<String, Object>> result = jdbcTemplate4.queryForList(sql,queryList.toArray());
		return result;
	}
	
	public String getVehicleOverdueWarningVehicleNumber(String PLATE_NUMBER) {
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select replace(AUTO_NUM,'.','') as PLATE_NUMBER from platcar_base_info where replace(AUTO_NUM,'.','') like ?  GROUP BY AUTO_NUM";
		queryList.add(PLATE_NUMBER+"%");
		return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, queryList.toArray())));
	}

	// 获取平台名称
	public String getPlatformName() {
		String sql = "select ABB_NAME from TB_GLOBAL_COMPANY";
		List<Map<String, Object>> platformNameList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
		return jacksonUtil.toJson(platformNameList);
	}

	

}
