package mvc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import helper.JacksonUtil;
import helper.LogUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IndustryOperationalAnalysisService {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate = null;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = format.format(new Date());
	private void log(HttpServletRequest request,String model,String s){
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'))";
		jdbcTemplate.update(sql,username,model,s,time);
	}

	public String getOperatingOutboundStatistics(HttpServletRequest request, String sTime,String eTime) {
		log(request,"行业运行统计-营运出车统计查询", "行业运行统计-营运出车统计查询");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(sTime == null || "".equals(sTime)){
			sTime = format.format(new Date());
		}
		if(eTime == null || "".equals(eTime)){
			eTime = format.format(new Date());
		}
		List <Object> queryList=new  ArrayList<Object>();
		String countSql = "select count(*) count from (";
		
		String sql = "select tt.*,ROUND(DRIVER_OUT_NUMBER/PERMIT_NUMBER*100, 2) || '%' rate,TO_CHAR(DATE_TIME,'yyyy-mm-dd') format_date from TB_OPERATION_DRIVER_OUT_STATIS tt where 1 = 1";
		if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
			sql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') >= TO_DATE(?,'yyyy-mm-dd')";
			queryList.add(sTime);
		}
		if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
			sql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') <= TO_DATE(?,'yyyy-mm-dd')";
			queryList.add(eTime);
		}
		
		sql += " ORDER BY DATE_TIME";
		
		countSql += sql + ")";
		int count = jdbcTemplate.queryForObject(countSql, queryList.toArray(), Integer.class);
		
		List <Object> queryList1=new  ArrayList<Object>();
		String statisticsSql = "select ROUND(SUM(order_number)/"+count+",0) sum_order_number,ROUND(SUM(driver_out_number)/"+count+",0) sum_driver_out_number,ROUND(SUM(driver_out_number)/SUM(permit_number)*100,2) || '%' rate from TB_OPERATION_DRIVER_OUT_STATIS where 1 = 1";
		if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
			statisticsSql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') >= TO_DATE(?,'yyyy-mm-dd')";
			queryList1.add(sTime);
		}
		if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
			statisticsSql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') <= TO_DATE(?,'yyyy-mm-dd')";
			queryList1.add(eTime);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		List<Map<String, Object>> statisticsList = jdbcTemplate.queryForList(statisticsSql,queryList1.toArray());
		
		List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
		
		String value = sTime +"到"+ eTime;

		Map<String, Object> map2 = new HashMap<String, Object>();
		for(int i=0;i<statisticsList.size();i++){
			map2 = statisticsList.get(i);
		}
		map2.put("VALUE", value);
		finalList.add(map2);
		
		map.put("datas", list);// 表格数据
		map.put("statistics", finalList);// 统计数据
		return jacksonUtil.toJson(map);
	}

	public Map<String, Object> getOperatingOutboundStatisticsExcel(HttpServletRequest request, String sTime, String eTime) {
		log(request,"行业运行统计-导出营运出车统计Excel", "行业运行统计-导出营运出车统计Excel");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(sTime == null || "".equals(sTime)){
			sTime = format.format(new Date());
		}
		if(eTime == null || "".equals(eTime)){
			eTime = format.format(new Date());
		}
		String countSql = "select count(*) count from TB_OPERATION_DRIVER_OUT_STATIS";
		Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
		
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select tt.*,ROUND(DRIVER_OUT_NUMBER/PERMIT_NUMBER*100, 2) || '%' rate,TO_CHAR(DATE_TIME,'yyyy-mm-dd') format_date from TB_OPERATION_DRIVER_OUT_STATIS tt where 1 = 1";
		String statisticsSql = "select ROUND(SUM(order_number)/"+count+",0) sum_order_number,ROUND(SUM(driver_out_number)/"+count+",0) sum_driver_out_number,ROUND(SUM(driver_out_number)/SUM(permit_number)*100,2) || '%' rate from TB_OPERATION_DRIVER_OUT_STATIS where 1 = 1";
		if(sTime!=null&&sTime.length()>0&&!sTime.isEmpty()&&!sTime.equals("")){
			sql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') >= TO_DATE(?,'yyyy-mm-dd')";
			statisticsSql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') >= TO_DATE(?,'yyyy-mm-dd')";
			queryList.add(sTime);
		}
		if(eTime!=null&&eTime.length()>0&&!eTime.isEmpty()&&!eTime.equals("")){
			sql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') <= TO_DATE(?,'yyyy-mm-dd')";
			statisticsSql += " and TO_DATE(TO_CHAR(DATE_TIME,'yyyy-mm-dd'),'yyyy-mm-dd') <= TO_DATE(?,'yyyy-mm-dd')";
			queryList.add(eTime);
		}
		
		sql += " ORDER BY DATE_TIME";
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		List<Map<String, Object>> statisticsList = jdbcTemplate.queryForList(statisticsSql,queryList.toArray());
		
		List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
		
		String value = sTime +"到"+ eTime;

		Map<String, Object> map2 = new HashMap<String, Object>();
		for(int i=0;i<statisticsList.size();i++){
			map2 = statisticsList.get(i);
		}
		map2.put("VALUE", value);
		finalList.add(map2);
		
		map.put("datas", list);// 表格数据
		map.put("statistics", finalList);// 统计数据
		return map;
	}

	public String getEnterpriseMonthlyReport(HttpServletRequest request, String time) {
		log(request,"订单数据统计-企业月报查询", "订单数据统计-企业月报查询");
		String echartsSql = "select t1.*,t2.ABB_NAME from (select COMPANYID,to_char(UPDATE_TIME,'yyyy-mm') time,sum(ORDER_NUM) sum from TB_ORDER_WEEK where 1 = 1 and to_char(UPDATE_TIME,'yyyy-mm') = ? GROUP BY to_char(UPDATE_TIME,'yyyy-mm'),COMPANYID ORDER BY to_char(UPDATE_TIME,'yyyy-mm')) t1,TB_GLOBAL_COMPANY t2 where t1.COMPANYID = t2.COMPANY_ID";
		List<Map<String, Object>> echartsList = jdbcTemplate.queryForList(echartsSql,time);
		
		Integer total = jdbcTemplate.queryForObject("select sum(ORDER_NUM) from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ?", Integer.class, time);
		
		List<Map<String, Object>> companys = jdbcTemplate.queryForList("select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<companys.size();i++){
			String sql = "select t1.*,t2.ABB_NAME from (select tt1.time,tt1.COMPANYID,tt1.sum1 sum,ROUND(tt1.sum1/tt2.sum3*100, 2) rate from (select COMPANYID,to_char(UPDATE_TIME,'yyyy-mm-dd') time,sum(ORDER_NUM) sum1,sum(YSJE) sum2 from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ? and COMPANYID = ? GROUP BY COMPANYID,to_char(UPDATE_TIME,'yyyy-mm-dd')) tt1,(select to_char(UPDATE_TIME,'yyyy-mm-dd') time,sum(ORDER_NUM) sum3 from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ? GROUP BY to_char(UPDATE_TIME,'yyyy-mm-dd')) tt2 where tt1.time = tt2.time) t1,TB_GLOBAL_COMPANY t2 where t1.COMPANYID = t2.COMPANY_ID ORDER BY t1.time";
			List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql,time,companys.get(i).get("COMPANYID"),time);
			if(list1 != null && list1.size() > 0){
				
				Map<String, Object> map1 = new HashMap<String, Object>();
				Map<String, Object> map2 = new HashMap<String, Object>();
				int sum = 0;
				for(int j=0;j<list1.size();j++){
					String valueOf = String.valueOf(list1.get(j).get("TIME"));
					String valueOf1 = String.valueOf(list1.get(j).get("SUM"));
					String valueOf2 = String.valueOf(list1.get(j).get("RATE")) + "%";
					String valueOf3 = String.valueOf(list1.get(j).get("ABB_NAME"));

					sum += Integer.valueOf(valueOf1);
					
					map1.put(valueOf, valueOf1);
					map2.put(valueOf, valueOf2);
					map1.put("ABB_NAME", valueOf3);
					map2.put("ABB_NAME", valueOf3);
				}
				map1.put("sum", sum);
				map2.put("sum", String.format("%.2f",Double.valueOf(sum)/Double.valueOf(total) * 100) + "%");
				list.add(map1);
				list.add(map2);
			}
		}
		map.put("list", list);
		map.put("echartsList", echartsList);// echarts数据
		return jacksonUtil.toJson(map);
	}
	
	public Map<String, Object> getEnterpriseMonthlyReportExcel(HttpServletRequest request, String time) {
		log(request,"订单数据统计-企业月报查询", "订单数据统计-企业月报查询");
		
		Integer total = jdbcTemplate.queryForObject("select sum(ORDER_NUM) from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ?", Integer.class, time);
		
		List<Map<String, Object>> companys = jdbcTemplate.queryForList("select COMPANY_ID COMPANYID from TB_GLOBAL_COMPANY");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<companys.size();i++){
			String sql = "select t1.*,t2.ABB_NAME from (select tt1.time,tt1.COMPANYID,tt1.sum1 sum,ROUND(tt1.sum1/tt2.sum3*100, 2) rate from (select COMPANYID,to_char(UPDATE_TIME,'yyyy-mm-dd') time,sum(ORDER_NUM) sum1,sum(YSJE) sum2 from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ? and COMPANYID = ? GROUP BY COMPANYID,to_char(UPDATE_TIME,'yyyy-mm-dd')) tt1,(select to_char(UPDATE_TIME,'yyyy-mm-dd') time,sum(ORDER_NUM) sum3 from TB_ORDER_WEEK where to_char(UPDATE_TIME,'yyyy-mm') = ? GROUP BY to_char(UPDATE_TIME,'yyyy-mm-dd')) tt2 where tt1.time = tt2.time) t1,TB_GLOBAL_COMPANY t2 where t1.COMPANYID = t2.COMPANY_ID ORDER BY t1.time";
			List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql,time,companys.get(i).get("COMPANYID"),time);
			if(list1 != null && list1.size() > 0){
				
				Map<String, Object> map1 = new HashMap<String, Object>();
				Map<String, Object> map2 = new HashMap<String, Object>();
				int sum = 0;
				for(int j=0;j<list1.size();j++){
					String valueOf = String.valueOf(list1.get(j).get("TIME"));
					String valueOf1 = String.valueOf(list1.get(j).get("SUM"));
					String valueOf2 = String.valueOf(list1.get(j).get("RATE")) + "%";
					String valueOf3 = String.valueOf(list1.get(j).get("ABB_NAME"));

					sum += Integer.valueOf(valueOf1);
					
					map1.put(valueOf, valueOf1);
					map2.put(valueOf, valueOf2);
					map1.put("ABB_NAME", valueOf3);
					map2.put("ABB_NAME", valueOf3);
				}
				map1.put("sum", sum);
				map2.put("sum", String.format("%.2f",Double.valueOf(sum)/Double.valueOf(total) * 100) + "%");
				list.add(map1);
				list.add(map2);
			}
		}
		
		List<Map<String, Object>> timeList = jdbcTemplate.queryForList("select tt.time from (select TO_CHAR(UPDATE_TIME,'yyyy-mm-dd') time from TB_ORDER_WEEK where TO_CHAR(UPDATE_TIME,'yyyy-mm') = ? GROUP BY TO_CHAR(UPDATE_TIME,'yyyy-mm-dd')) tt ORDER BY tt.time",time);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timeList", timeList);
		map.put("list", list);
		return map;
	}

}
