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
public class ServiceQualityService {
	
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

	public String getComplaintEnquiry(HttpServletRequest request,String stime,String etime) {
		log(request,"12328投诉查询", "12328投诉查询");
		String s = stime + " 00:00:00";
		String e = etime + " 23:59:59";
		String table = stime.substring(0, 4);
		String sql = "select tt.*,T_CAR_HAILING_PROVIDER_YWLY.CONTENT FINALFROMETYPE from (select * from T_CAR_HAILING_PROVIDER_"+table+" where YWTYPE = '0'";
		List <Object> queryList=new  ArrayList<Object>();
		if(stime!=null&&!"".equals(stime)){
			sql += " and HAPPEN_DATE >= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			queryList.add(s);
		}
		if(etime!=null&&!"".equals(etime)){
			sql += " and HAPPEN_DATE <= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			queryList.add(e);
		}
		sql += ")tt LEFT JOIN T_CAR_HAILING_PROVIDER_YWLY ON tt.FROMTYPE = T_CAR_HAILING_PROVIDER_YWLY.ID";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		return jacksonUtil.toJson(list);
	}

	public String getComplaintOrder(HttpServletRequest request,String orderId, String driverName,
			String vehicleNo, String sTime, String eTime, String page, String pageSize) {
		log(request,"投诉订单查询", "投诉订单查询");
		String str = sTime.replaceAll("-", "").substring(2,6);
		String s = sTime.substring(0,4);
		String sql = "select * from (select A.*,ROWNUM RN from (select t1.ORDERID,TO_CHAR(t1.COMPLAINTTIME,'yyyy-mm-dd hh24:mi:ss') COMPLAINTTIME,t1.DETAIL,t1.RESULT,t2.DRIVERNAME,t2.LICENSEID,t2.VEHICLENO,t2.DEPAREA,TO_CHAR(t2.DEPTIME,'yyyy-mm-dd hh24:mi:ss') DEPTIME,t2.DESTAREA,TO_CHAR(t2.DESTTIME,'yyyy-mm-dd hh24:mi:ss') DESTTIME,t2.FACTPRICE,t2.ORDERMATCHTIME from TB_RATEDPASSENGERCPM_"+s+" t1, TB_OPERATEPAY_"+str+" t2 where t1.ORDERID = t2.ORDERID";
		String countSql = "select count(1) count from (select t1.ORDERID,t1.COMPLAINTTIME,t1.DETAIL,t1.RESULT,t2.DRIVERNAME,t2.LICENSEID,t2.VEHICLENO,t2.DEPAREA,t2.DEPTIME,t2.DESTAREA,t2.DESTTIME,t2.FACTPRICE,t2.ORDERMATCHTIME from TB_RATEDPASSENGERCPM_"+s+" t1, TB_OPERATEPAY_"+str+" t2 where t1.ORDERID = t2.ORDERID";
		
		List <Object> queryList=new  ArrayList<Object>();
		List <Object> queryList1=new  ArrayList<Object>();
		
		if(orderId!=null && !"".equals(orderId)){
			sql += " and t1.ORDERID = ?";
			countSql += " and t1.ORDERID = ?";
			queryList.add(orderId);
			queryList1.add(orderId);
		}
		if(driverName!=null && !"".equals(driverName)){
			sql += " and t2.DRIVERNAME = ?";
			countSql += " and t2.DRIVERNAME = ?";
			queryList.add(driverName);
			queryList1.add(driverName);
		}
		if(vehicleNo!=null && !"".equals(vehicleNo)){
			sql += " and t2.VEHICLENO = ?";
			countSql += " and t2.VEHICLENO = ?";
			queryList.add(vehicleNo);
			queryList1.add(vehicleNo);
		}
		if(sTime!=null && !"".equals(sTime)){
			sql += " and t1.COMPLAINTTIME >= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			countSql += " and t1.COMPLAINTTIME >= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			queryList.add(sTime);
			queryList1.add(sTime);
		}
		if(eTime!=null && !"".equals(eTime)){
			sql += " and t1.COMPLAINTTIME <= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			countSql += " and t1.COMPLAINTTIME <= TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
			queryList.add(eTime);
			queryList1.add(eTime);
		}
		countSql += ")";
		sql += ") A where ROWNUM <= ?) where RN >= ?";
		
		queryList.add((Integer.valueOf(page)*Integer.valueOf(pageSize)));
		queryList.add(((Integer.valueOf(page)-1)*Integer.valueOf(pageSize)));
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		int count = jdbcTemplate.queryForObject(countSql, queryList1.toArray(), Integer.class);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("datas", list);
		map.put("count", count);
		return jacksonUtil.toJson(map);
	}

}
