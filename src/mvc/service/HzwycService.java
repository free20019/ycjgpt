package mvc.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.JacksonUtil;
import helper.LogUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class HzwycService {
	protected JdbcTemplate jdbcTemplate = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	// 加载计数
	public String findHomePage() {
		LogUtil.log("查询今日在线 今日订单", "查询今日在线 今日订单");
		String sql = " select count(*) as count  from tb_global_company where BUSINESS_SCOPE_NAME like '网络预约出租%' and name != '测试'";
		List<Map<String,Object>> shizialvlist1 = jdbcTemplate.queryForList(sql);
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		resultMap.add(map);
		return jacksonUtil.toJson(resultMap);
	}
	
	// 车辆上线数 及 实载率
	public String findTj() {
		LogUtil.log("车辆上线数 及 实载率", "车辆上线数 及 实载率");
		String sql = " select * from  tb_tj ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("datas",list);
		return jacksonUtil.toJson(map);
	}
	
	//今日上线
	public String findjrsx() {
		LogUtil.log("今日上线", "今日上线");
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yearformat = new SimpleDateFormat("yyMM");
		SimpleDateFormat hourformat = new SimpleDateFormat("HH");
		String time = timeformat.format(new Date());
		String year = yearformat.format(new Date());
		String hour = hourformat.format(new Date());
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		for(int i = 0 ;i<Integer.valueOf(hour);i++){
			Map map = new HashMap<String,Object>();
			String sql ="";
			List<Map<String,Object>>  list = null;
			if(i<10){
				String s = time + "0" + i + "%";
				sql = "select  count(DistributeTime) as time from tb_ordermatch_"+year+"  where DistributeTime like ? ";;
				list = jdbcTemplate.queryForList(sql,s);
			}else{
				String s = time + " " + i + "%";
				sql = "select  count(DistributeTime) as time from tb_ordermatch_"+year+"  where DistributeTime like ? ";;
				list = jdbcTemplate.queryForList(sql,s);
			}
			map.put("time", list.get(0).get("time"));
			map.put("hour", i);
			resultMap.add(map);
		}
		return jacksonUtil.toJson(resultMap);
	}
	//今日订单
	public String findjrdd() {
		LogUtil.log("今日订单", "今日订单");
			String sql = "select DATE_FORMAT(Dbtime,'%H') as time,sum(carno) as carno,sum(orderNo) as orderno from tb_home_page GROUP BY DATE_FORMAT(Dbtime,'%Y-%m-%d %H')";;
			List<Map<String,Object>>  list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	//驾驶员
	public String findjsy() {
		LogUtil.log("查询驾驶员许可量", "查询驾驶员许可量");
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar =Calendar.getInstance();
		for(int i = -6 ;i<0;i++){
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, i);
			calendar.getTime();
			String time = format.format(calendar.getTime());
			Map map = new HashMap<String,Object>();
			String jsysql = "SELECT count(*) as count from tb_jsysqxx WHERE SQRQ LIKE ?";
			String driversql = "SELECT count(*) as count from tb_driving_info WHERE certificate_name = 2 AND certificate_initial_date LIKE ?";
			List<Map<String,Object>>  jsylist = jdbcTemplate.queryForList(jsysql,time+"%");
			List<Map<String,Object>>  driverlist = jdbcTemplate.queryForList(driversql,time+"%");
			map.put("year",time);
			map.put("jsy", jsylist.get(0).get("count"));
			map.put("driver", driverlist.get(0).get("count"));
			resultMap.add(map);
		}
		return jacksonUtil.toJson(resultMap);
	}
	//车辆
		public String findcl() {
			LogUtil.log("查询车辆许可量", "查询车辆许可量");
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar calendar =Calendar.getInstance();
			for(int i = -6 ;i<0;i++){
				calendar.setTime(date);
				calendar.add(Calendar.MONTH, i);
				calendar.getTime();
				String time = format.format(calendar.getTime());
				Map<String,Object> map = new HashMap<String,Object>();
				String clsql = "SELECT count(*) as count from tb_clsqxx WHERE SQRQ LIKE ?";
				String vehiclesql = "SELECT count(*) as count from tb_vehicle_info WHERE LICENSE_ISSUE_DATE LIKE ?";
				List<Map<String,Object>>  cllist = jdbcTemplate.queryForList(clsql,time+"%");
				List<Map<String,Object>>  vehiclelist = jdbcTemplate.queryForList(vehiclesql,time+"%");
				map.put("year",time);
				map.put("cl", cllist.get(0).get("count"));
				map.put("vehicle", vehiclelist.get(0).get("count"));
				resultMap.add(map);
			}
			return jacksonUtil.toJson(resultMap);
		}
		//车辆
		public String finddd() {
			LogUtil.log("查询月度订单", "查询月度订单");
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM");
			Calendar calendar =Calendar.getInstance();
			for(int i = -6 ;i<0;i++){
				calendar.setTime(date);
				calendar.add(Calendar.MONTH, i);
				calendar.getTime();
				String time = format.format(calendar.getTime());
				String ddsql = "SELECT t2.abb_name,t1.month,t1.amount/10000 as amount,t1.cishu/10000 as cishu  from tb_ordermoney t1,tb_global_company t2 WHERE t1.companyid = t2.company_id  and month = ? order by t2.abb_name desc";
				List<Map<String,Object>>  ddlist = jdbcTemplate.queryForList(ddsql,time);
				Map map2 = new HashMap<String,Object>();
				DecimalFormat decilmal = new DecimalFormat("#.####");
				for(int j = 0 ;j<ddlist.size();j++){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("company", ddlist.get(j).get("abb_name"));
					map.put("money", decilmal.format(ddlist.get(j).get("amount")));
					map.put("order", decilmal.format(ddlist.get(j).get("cishu")));
					map2.put(j, map);
				}
				map2.put("month",dayformat.format(calendar.getTime()));
				resultMap.add(map2);
			}
			return jacksonUtil.toJson(resultMap);
		}
}
