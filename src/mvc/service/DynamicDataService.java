package mvc.service;

import helper.JacksonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: xianlehuang
 * @Description:动态数据
 * @date: 2020/7/27 - 11:27
 */
@Service
public class DynamicDataService {


    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected JdbcTemplate jdbcTemplate1;
    @Autowired
    protected JdbcTemplate jdbcTemplate2;
    @Autowired
    protected JdbcTemplate jdbcTemplate3;
    @Autowired
    protected JdbcTemplate jdbcTemplate4;
    @Autowired
    protected JdbcTemplate jdbcTemplate5;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = format.format(new Date());

    public boolean isNull(String str) {
        if (str == null || str.isEmpty() || "null".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    private void getCount(Map<String, Object> map, List<Map<String, Object>> list) {
        if (list.size() > 0) {
            map.put("count", list.get(0).get("COUNT"));
        } else {
            map.put("count", "0");
        }
    }

    private void log(HttpServletRequest request, String model, String s){
        String username = String.valueOf(request.getSession().getAttribute("username"));
        String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,?)";
        int list = jdbcTemplate.update(sql,username,model,s,time);
    }

    //订单发起表
    public String findOrderCreate(HttpServletRequest request, String postData) {
//        log(request,"动态数据查询 → 订单发起表", "动态数据查询 → 订单发起表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.OrderTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.OrderTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_ordercreate_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_ordercreate_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.OrderTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //订单成功表
    public String findOrderMatch(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 订单成功表", "动态数据查询 → 订单成功表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.DistributeTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.DistributeTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);


        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_ordermatch_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_ordermatch_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.DistributeTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //订单撤销表
    public String findOrderCancel(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 订单撤销表", "动态数据查询 → 订单撤销表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.CancelTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.CancelTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_ordercancel_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_ordercancel_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.CancelTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //车辆
    public String findVehicle(HttpServletRequest request) {
        String sql = "select distinct VEHICLENO VEHICLE_NO,abb_name from TB_VEHICLE_GPS_STATUS g,TB_GLOBAL_COMPANY c" +
                " where g.companyid=c.company_id order by VEHICLENO";
        return jacksonUtil.toJson(JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql)));
    }

    //车辆经营上线表
    public String findOperateLogin(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 车辆经营上线表", "动态数据查询 → 车辆经营上线表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String vehicle = String.valueOf(parampMap.get("vehicle"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.LoginTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.LoginTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj2 += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_operatelogin_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_operatelogin_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.LoginTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //车辆经营下线表
    public String findOperateLogout(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 车辆经营下线表", "动态数据查询 → 车辆经营下线表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String vehicle = String.valueOf(parampMap.get("vehicle"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.LogoutTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.LogoutTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj2 += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_operatelogout_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_operatelogout_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.LogoutTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //经营出发表
    public String findOperateDepart(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 经营出发表", "动态数据查询 → 经营出发表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String vehicle = String.valueOf(parampMap.get("vehicle"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.DepTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.DepTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj2 += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_operatedepart_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_operatedepart_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.DepTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //经营到达表
    public String findOperateArrive(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 经营到达表", "动态数据查询 → 经营到达表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String vehicle = String.valueOf(parampMap.get("vehicle"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.DestTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.DestTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj2 += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }

        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_operatearrive_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_operatearrive_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.DestTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //经营支付表
    public String findOperatePay(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 经营支付表", "动态数据查询 → 经营支付表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String vehicle = String.valueOf(parampMap.get("vehicle"));
        String driverName = String.valueOf(parampMap.get("driverName"));
        String licenseId = String.valueOf(parampMap.get("licenseId"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.PayTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }
        if (!isNull(driverName)) {
            tj += " and t.driverName like ?";
            queryList.add(driverName+"%");
        }
        if (!isNull(licenseId)) {
            tj += " and t.licenseId like ?";
            queryList.add(licenseId+"%");
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.PayTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(vehicle)) {
            tj2 += " and t.VehicleNo = ?";
            queryList.add(vehicle);
        }
        if (!isNull(driverName)) {
            tj2 += " and t.driverName like ?";
            queryList.add(driverName+"%");
        }
        if (!isNull(licenseId)) {
            tj2 += " and t.licenseId like ?";
            queryList.add(licenseId+"%");
        }

        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_operatepay_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_operatepay_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.PayTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //乘客评价信息表
    public String findRatedPassenger(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 乘客评价信息表", "动态数据查询 → 乘客评价信息表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.EvaluateTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.EvaluateTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_ratedpassenger_"+begin+"  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_ratedpassenger_"+begin+" t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.EvaluateTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //乘客投诉信息表
    public String findRatedPassengerComplaint(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 乘客投诉信息表", "动态数据查询 → 乘客投诉信息表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.ComplaintTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.ComplaintTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_ratedpassengercomplaint  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_ratedpassengercomplaint t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.ComplaintTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //驾驶员处罚信息表
    public String findProvDriverPunish(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 驾驶员处罚信息表", "动态数据查询 → 驾驶员处罚信息表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.PunishTime BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.PunishTime BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
        String sql = "select (select count(*) from  tb_provdriverpunish  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_provdriverpunish t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.PunishTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }

    //驾驶员信誉表
    public String findRatedDriver(HttpServletRequest request, String postData) {
        //log(request,"动态数据查询 → 驾驶员信誉表", "动态数据查询 → 驾驶员信誉表");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String licenseId = String.valueOf(parampMap.get("licenseId"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String tj = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj += " and t.TestDate BETWEEN ?  AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(licenseId)) {
            tj += " and t.licenseId = ?";
            queryList.add(licenseId);
        }

        String tj2 = "";
        if (!isNull(begintime) && !isNull(endtime)) {
            tj2 += " and t.TestDate BETWEEN ? AND ? ";
            queryList.add(begintime);
            queryList.add(endtime);
        }
        if (!isNull(ptname) && !ptname.equals("0")) {
            tj2 += " and t.CompanyId = ?";
            queryList.add(ptname);
        }
        if (!isNull(licenseId)) {
            tj2 += " and t.licenseId = ?";
            queryList.add(licenseId);
        }
        queryList.add(((pageIndex - 1) * pageSize));
        queryList.add(pageSize);

        String begin = "";
        if (!isNull(begintime) || !isNull(endtime)) {
            if(!isNull(begintime)){
                begin = begintime.substring(2, 7).replaceAll("-", "");
            }else if(!isNull(endtime)){
                begin = endtime.substring(2, 7).replaceAll("-", "");
            }
        }else{
            begin = format.format(new Date()).substring(2, 7).replaceAll("-", "");
        }
        //sql
//        String sql = "select (select count(*) from  tb_rateddriver_"+begin+"  t" +
//                " where 1 = 1 " + tj + ") as count ,tt.* from " +
//                " (select t.*,t2.CompanyName from   tb_rateddriver_"+begin+" t" +
//                " left join tb_company t2 on t.CompanyId = t2.companyid " +
//                " where 1=1 " + tj2+ " " +
//                " order by t.TestDate desc limit ?,?" +
//                ")tt";
        String sql = "select (select count(*) from  tb_rateddriver  t" +
                " where 1 = 1 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from   tb_rateddriver t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid " +
                " where 1=1 " + tj2+ " " +
                " order by t.TestDate desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }
}
