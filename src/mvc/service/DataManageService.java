package mvc.service;

import helper.ExcelUtil;
import helper.JacksonUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jingwei·li
 * @date 2020/3/31
 */
@Service
public class DataManageService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate4;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    private ExcelUtil excelUtil = new ExcelUtil();

    public String getRequest(HttpServletRequest request, String field) {
        return String.valueOf(request.getParameter(field));
    }

    public boolean IsNull(String str) {
        if (str.equals("") || str == null || str.equals("null")) return true;
        return false;
    }

    /**
     * 数据接入管理 → 查询数据列表
     *
     * @param request
     * @return
     */
    public String getDataList(HttpServletRequest request) {
        String company = getRequest(request, "company");
        String status = getRequest(request, "status");
        System.out.println(company);
        String tj = "";
        ArrayList<String> condition = new ArrayList<>();
        if (!IsNull(company)) {
            tj += " and company like ?";
            condition.add("%" + company + "%");
        }
        if (!IsNull(status) && !status.equals("0")) {
            tj += " and status = ? ";
            condition.add(status);
        }
        String sql = "select * from tb_data_access where 1 = 1 " + tj;
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, condition.toArray(new String[]{})));

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("datas", resultList);
        return jacksonUtil.toJson(resultMap);
    }

    /**
     * 数据接入管理 → 获取图标显示数据
     *
     * @return
     */
    public String getChartInfo() {
        String sql = "select count(*) as count,status from tb_data_access t group by status";
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("online", 0);
        resultMap.put("end", 0);
        if (resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
                if (map.get("status").equals("1")) {
                    resultMap.put("online", map.get("count"));
                }
                if (map.get("status").equals("2")) {
                    resultMap.put("end", map.get("count"));
                }
            }
        }
        return jacksonUtil.toJson(resultMap);
    }

    /**
     * 数据接入管理 → 修改数据
     *
     * @param request
     * @return
     */
    public String editData(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String company = getRequest(request, "company");
        String ip_addr = getRequest(request, "ip_addr");
        String abb_name = getRequest(request, "abb_name");
        String scale = getRequest(request, "scale");
        String stime = getRequest(request, "stime");
        String etime = getRequest(request, "etime");
        String status = getRequest(request, "status");
        String company_id = getRequest(request, "company_id");
        String access_scale = getRequest(request, "access_scale");
        ArrayList<String> condition = new ArrayList<>();
        String sql = "update tb_data_access set company = ? ,company_id = ?, ip_addr = ? , abb_name = ?,scale = ?, status = ? , access_scale = ?  ";
        condition.add(company);
        condition.add(company_id);
        condition.add(ip_addr);
        condition.add(abb_name);
        condition.add(scale);
        condition.add(status);
        condition.add(access_scale);
        if (!IsNull(stime)) {
            sql += " ,stime = ? ";
            condition.add(stime);
        }
        if (!IsNull(etime)) {
            sql += " ,etime =? ";
            condition.add(stime);
        }
        sql += " where id = ? ";
        condition.add(id);
        int result = jdbcTemplate.update(sql, condition.toArray(new String[]{}));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", result > 0 ? "0" : "1");
        return jacksonUtil.toJson(resultMap);
    }

    /**
     * 数据接入管理 → 添加数据
     *
     * @param request
     * @return
     */
    public String addData(HttpServletRequest request) {
        String company = getRequest(request, "company");
        String company_id = getRequest(request, "company_id");
        String ip_addr = getRequest(request, "ip_addr");
        String abb_name = getRequest(request, "abb_name");
        String scale = getRequest(request, "scale");
        String stime = getRequest(request, "stime");
        String etime = getRequest(request, "etime");
        String status = getRequest(request, "status");
        String access_scale = getRequest(request, "access_scale");
        ArrayList<String> condition = new ArrayList<>();
        String sqlbegin = "insert into tb_data_access(company,company_id,ip_addr,abb_name,scale,status,access_scale";
        String sqlend = " values (?,?,?,?,?,?,?";
        condition.add(company);
        condition.add(company_id);
        condition.add(ip_addr);
        condition.add(abb_name);
        condition.add(scale);
        condition.add(status);
        condition.add(access_scale);
        if (!IsNull(stime)) {
            sqlbegin += ",stime";
            sqlend += " ,to_date(?,'yyyy-MM-dd') ";
            condition.add(stime);
        }
        if (!IsNull(etime)) {
            sqlbegin += ",etime";
            sqlend += " ,to_date(?,'yyyy-MM-dd') ";
            condition.add(stime);
        }
        sqlbegin += " )";
        sqlend += ")";
        int result = jdbcTemplate.update(sqlbegin + sqlend, condition.toArray(new String[]{}));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", result > 0 ? "0" : "1");
        return jacksonUtil.toJson(resultMap);
    }

    /**
     * 数据接入管理 → 删除数据
     *
     * @return
     */
    public String delData(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String sql = "update tb_data_access set deleted = 1 where id= ? ";
        int result = jdbcTemplate.update(sql, id);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", result > 0 ? "0" : "1");


        return jacksonUtil.toJson(resultMap);

    }

    /**
     * 运力分析报告 → 查询数据
     *
     * @return
     */
    public Object getAnalyze(HttpServletRequest request, String type) {
        String stime = getRequest(request, "stime");
        String etime = getRequest(request, "etime");
        Integer page = type.equals("0") ? Integer.valueOf(getRequest(request, "page")) : 1;
        Integer pageSize = type.equals("0") ? Integer.valueOf(getRequest(request, "pageSize")) : 999999999;

        String tj = "";
        if (!IsNull(stime)) {
            tj += " and t.dbtime >='" + stime + "'";
        }
        if (!IsNull(etime)) {
            tj += " and t.dbtime <= '" + etime + "'";
        }
        String sql = "select tt.*,t2.* from  ("
                //tb_order_hottime 和 tb_order_hotpot 表 左连接 拼到一起
                + "select hot_time, time_num, t.dbtime,date_format(t.dbtime,'%Y-%m-%d') as DBTIME1,area_name, area_num from" +
//                " (select hot_time,time_num,dbtime, @rowno1:=@rowno1+1 as rn from tb_order_hottime r ,(select @rowno1:=0) t  order by dbtime desc ) t"
                " (SELECT t.*,COUNT(*)  AS rn FROM tb_order_hottime t LEFT JOIN tb_order_hottime r ON t.dbtime = r.dbtime AND t.HOT_TIME >= r.HOT_TIME GROUP BY t.dbtime,t.HOT_TIME order by dbtime desc ,rn ) t"
                + " left join" +
//                " (select area_name,area_num, @rowno2:=@rowno2+1 as rn from tb_order_hotpot  r ,(select @rowno2:=0) t order by dbtime desc) t2 on t.rn = t2.rn  "
                " (SELECT t.*,COUNT(*)  AS rn FROM tb_order_hotpot t LEFT JOIN tb_order_hotpot r ON t.dbtime = r.dbtime AND t.area_num >= r.area_num GROUP BY t.dbtime,t.area_name order by dbtime desc ,rn) t2 on t.rn = t2.rn  and t.dbtime = t2.dbtime"
                + " where 1 = 1 " + tj + " limit  " + (page - 1) * pageSize * 5 + "," + pageSize * 5 + " )tt "

                + "left join  (select create_order_num,dep_order_num,dep_order_veh ,dbtime ,concat(TRUNCATE(dep_order_num / create_order_num,2) * 100 ,'%') as end_order,"
                //每日派单的 昨日环比 保留两位小数
                + "concat(TRUNCATE((create_order_num / (select create_order_num from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 1 DAY) ))* 100 , 2 ), '%') as create_yesterday, "
                //每日派单的上周同比  保留两位小数
                + "concat(TRUNCATE((create_order_num / (select create_order_num from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 7 DAY) ))* 100 , 2 ), '%') as create_lastweek, "
                //订单完成量的 昨日环比 保留两位小数
                + "concat(TRUNCATE((dep_order_num / (select dep_order_num from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 1 DAY) ))* 100 , 2 ), '%') as dep_yesterday, "
                //订单完成量的上周同比  保留两位小数
                + "concat(TRUNCATE((dep_order_num / (select dep_order_num from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 7 DAY) ))* 100 , 2 ), '%') as dep_lastweek, "
                //活跃车辆的 昨日环比 保留两位小数
                + "concat(TRUNCATE((dep_order_veh / (select dep_order_veh from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 1 DAY) ))* 100 , 2 ), '%') as veh_yesterday, "
                //活跃车辆的上周同比  保留两位小数
                + "concat(TRUNCATE((dep_order_veh / (select dep_order_veh from tb_order_num where dbtime = DATE_SUB(t.dbtime, INTERVAL 7 DAY) ))* 100 , 2 ), '%') as veh_lastweek from  tb_order_num t ) t2 "
                + " on tt.dbtime = t2.dbtime  order by tt.dbtime asc, tt.hot_time ASC";
        System.out.println(sql);
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        String countSql = "select count(*) as count from tb_order_num t where 1 = 1 " + tj;
        List<Map<String, Object>> countList = type.equals("0") ? jdbcTemplate.queryForList(countSql) : new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("datas", resultList);
        resultMap.put("count", countList.size() > 0 ? countList.get(0).get("count") : "0");
        return type.equals("0") ? jacksonUtil.toJson(resultMap) : resultList;
    }

    /**
     * 特殊目标查询 → 查询数据
     *
     * @param request
     * @return
     */
    public String getSpecialTarget(HttpServletRequest request) {
        String stime = getRequest(request, "stime");
        String etime = getRequest(request, "etime");
        String company_id = getRequest(request, "company_id");
        String driver_name = getRequest(request, "driver_name");
        String vehicleno = getRequest(request, "vehicleno");
        String licenseid = getRequest(request, "licenseid");
        Integer page = Integer.valueOf(getRequest(request, "page"));
        Integer pageSize = Integer.valueOf(getRequest(request, "pageSize"));

        String tj = "";
        ArrayList<Object> condition = new ArrayList<>();
        if (!IsNull(stime)) {
            tj += " and t.month >= ? ";
            condition.add(stime);
        }
        if (!IsNull(etime)) {
            tj += " and t.month <= ? ";
            condition.add(etime);
        }
        if (!IsNull(company_id) && !company_id.equals("0")) {
            tj += " and t.companyid = ? ";
            condition.add(company_id);
        }
        if (!IsNull(driver_name)) {
            tj += " and t.drivername like ? ";
            condition.add("%" + driver_name + "%");
        }
        if (!IsNull(vehicleno)) {
            tj += " and t.vehicleno like ? ";
            condition.add("%" + vehicleno + "%");
        }
        if (!IsNull(licenseid)) {
            tj += " and t.licenseid like ? ";
            condition.add("%" + licenseid + "%");
        }
        String countSql = "select count(*) as count  from (select t.companyid from tb_order_operate t where 1 = 1 " + tj
                + " ) tt ,tb_global_company t2  where tt.companyid = t2.company_id ";
        List<Map<String, Object>> countList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(countSql, condition.toArray(new Object[]{})));
        condition.add((page - 1) * pageSize);
        condition.add(pageSize);
        String sql = "select tt.*,t2.abb_name from (select t.vehicleno,t.drivername,t.order_num,t.companyid,t.licenseid,t.factprice,CONCAT(replace(t.month,'-','年'),'月') as month "
                + " from tb_order_operate t where 1 = 1  " + tj
                + "  order by t.month asc ) tt ,tb_global_company t2  where tt.companyid = t2.company_id limit ?, ?";
        List<Map<String, Object>> dataList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, condition.toArray(new Object[]{})));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("datas", dataList);
        resultMap.put("count", countList.size() > 0 ? countList.get(0).get("count") : "0");
        return jacksonUtil.toJson(resultMap);
    }

    /**
     * 特殊目标查询 → 查询驾驶员信息
     *
     * @param request
     * @return
     */
    public String getDriverInfo(HttpServletRequest request) {
        String lincenseid = getRequest(request, "lincenseid");
        Map<String, Object> map = new HashMap<>();
        if (IsNull(lincenseid)) {
            map.put("msg", 1);
            map.put("info", "身份证不能为空");
            return jacksonUtil.toJson(map);
        }
//        String sql = "select t.* ,t2.* from tb_driving_info t, person_taxi_info t2 where t.id = t2.PERSON_ID and t.ID_NUMBER = ? ";
        String sql = "select t.*  from tb_driving_info t where t.idNumber = ? ";
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, lincenseid));

        map.put("datas", resultList);
        map.put("msg", 0);
        return jacksonUtil.toJson(map);
    }

    /**
     * 特殊目标查询 → 查询车辆信息
     *
     * @param request
     * @return
     */
    public String getCarInfo(HttpServletRequest request) {
        String cphm = getRequest(request, "cphm");
        Map<String, Object> map = new HashMap<>();
        if (IsNull(cphm)) {
            map.put("msg", 1);
            map.put("info", "车牌不能为空");
            return jacksonUtil.toJson(map);
        }
        String sql = "select * from tb_vehicle_info where plate_number = ? ";
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql, cphm));

        map.put("datas", resultList);
        map.put("msg", 0);
        return jacksonUtil.toJson(map);
    }

    /**
     * 特殊目标查询 → 查询订单明细
     *
     * @param request
     * @return
     */
    public Object getPayInfo(HttpServletRequest request, String type) {
        String companyid = getRequest(request, "companyid");
        String month = getRequest(request, "month");
        String vehicleno = getRequest(request, "vehicleno");
        String lincenseid = getRequest(request, "lincenseid");
        System.out.println(companyid + "," + month + "," + vehicleno + "," + lincenseid);
        Map<String, Object> map = new HashMap<>();
        if (IsNull(companyid) || IsNull(month) || IsNull(vehicleno) || IsNull(lincenseid)) {
            map.put("msg", 1);
            map.put("info", "关键字段不能为空");
            return jacksonUtil.toJson(map);
        }
        String year = (month.replaceAll("年", "").replaceAll("月", "")).substring(2);
        String time = month.replaceAll("年", "-").replaceAll("月", "");
        String sql = " select distinct t2.abb_name,companyid,DRIVERNAME,LICENSEID,VEHICLENO,deptime,deparea,desttime,destarea,FACTPRICE from tb_operatepay_" + year
                + " t ,tb_global_company t2  where t.companyid = t2.company_id and t.companyid = '" + companyid + "' and t.VEHICLENO = '" + vehicleno + "'"
                + " and t.LICENSEID = '" + lincenseid + "' and (date_format(t.deptime,'%Y-%m')='"+time+"' or date_format(t.desttime,'%Y-%m')='"+time+"' )  order by t.desttime desc  ";
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        for (int i = 0; i < resultList.size(); i++) {
            resultList.get(i).put("DEPTIME",resultList.get(i).get("DEPTIME")==null?"":resultList.get(i).get("DEPTIME").toString().substring(0,19));
            resultList.get(i).put("DESTTIME",resultList.get(i).get("DESTTIME")==null?"":resultList.get(i).get("DESTTIME").toString().substring(0,19));
        }
        map.put("datas", resultList);
//		map.put("count", countList.size() > 0 ? countList.get(0).get("count"):"0");
        return type.equals("0") ? jacksonUtil.toJson(map) : resultList;
    }

    /**
     * 数据接入检测 → 数据接入检测查询
     *
     * @return
     */
    public String getCompanyDataFlow(HttpServletRequest request) {
        String sql = "select ifnull(t.dataflow,0) as dataflow,ifnull(t.flag,0) as flag,t.dbtime,t2.abb_name,t.companyid from tb_global_company t2  left join tb_companyDataFlow t "
                + "on t.companyid = t2.company_id and date_format(t.datetime,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d'') where  t2.operating = '0' ";
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));

        Map<String, Object> map = new HashMap<>();
        map.put("datas", resultList);
        map.put("msg", 0);
        return jacksonUtil.toJson(map);
    }

    /**
     * 单车收益分析 → 获取单车数据
     *
     * @param request
     * @return
     */
    public String getBikeAnalysis(HttpServletRequest request) {
        String time = getRequest(request, "time");
        String sql = "select t.dbtime,t.companyid,ifnull(t.order_ave,0) order_ave,ifnull(t.price_ave,0) price_ave,ifnull(t.mile_ave,0) mile_ave,ifnull(t.dur_ave,0) dur_ave" +
                " ,t2.abb_name  from (select abb_name,company_id from tb_global_company/* where operating = 0*/) t2 " +
                " left join (select t.* from tb_order_yy t where t.dbtime = STR_TO_DATE('" + time + "','%Y-%m-%d') ) t on t.companyid = t2.company_id";
        int[] key = new int[]{0, 1, 7, 30};
        String[] value = new String[]{"行业平均", "昨日平均", "上周平均", "上月平均"};
        for (int i = 0; i < 4; i++) {
            sql += "   union all  select DATE_SUB('" + time + "', INTERVAL " + key[i] + " DAY) as dbtime,'' companyid, order_ave,price_ave,mile_ave ,dur_ave ,'" + value[i] + "' as abb_name from tb_order_yy "
                    + " where  dbtime =DATE_SUB('" + time + "', INTERVAL " + key[i] + " DAY) and companyid='all' ";

//			sql += "   union all  select DATE_SUB('"+ time +"', INTERVAL "+key[i]+" DAY) as dbtime,'' companyid, TRUNCATE(avg(order_ave),2) as order_ave," +
//					" TRUNCATE(avg(price_ave),2) as price_ave,TRUNCATE(avg(mile_ave),2) as mile_ave ,TRUNCATE(avg(dur_ave),2) as dur_ave ,'"+ value[i] +"' as abb_name" +
//					" from tb_order_yy "+
//					" where  dbtime = DATE_SUB('"+ time +"', INTERVAL "+key[i]+" DAY) group by dbtime  ";
        }
        List<Map<String, Object>> resultList = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        Map<String, Object> map = new HashMap<>();
        map.put("datas", resultList);
        return jacksonUtil.toJson(map);
    }

    /**
     * 单车收益分析 → 获取单车Echarts
     *
     * @param request
     * @return
     */
    public String getBikeEcharts(HttpServletRequest request) {
        String time = getRequest(request, "time");
        Map<String, Object> map = new HashMap<>();
        int[] key = new int[]{0, 1, 7, 30};
        String[] mapKey = new String[]{"today", "yesterDay", "lastWeek", "lastMonth"};
        for (int i = 0; i < 4; i++) {
            String sql = "select DATE_SUB('" + time + "', INTERVAL " + key[i] + " DAY) as dbtime, t2.abb_name, ifnull(t.order_ave, 0) order_ave, ifnull(t.price_ave, 0) price_ave, ifnull(t.mile_ave, 0) mile_ave, ifnull(t.dur_ave, 0) dur_ave "
                    + " from (select abb_name, company_id from tb_global_company /*where operating = 0*/ order by company_id asc) t2 left join  "
                    + "  (select t.*  from tb_order_yy t where t.dbtime = DATE_SUB('" + time + "', INTERVAL " + key[i] + " DAY) ) t  on t.companyid =  t2.company_id  ";
            map.put(mapKey[i], JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql)));
        }
        return jacksonUtil.toJson(map);
    }


    /**
     * 企业运营登记 → 查询企业列表
     *
     * @param request
     * @return
     */
    public String getBikeCompany(HttpServletRequest request) {
        String abb_name = getRequest(request, "abb_name");
        String name = getRequest(request, "name");
        String operating = getRequest(request, "operating");
        String sql = "select * from tb_global_company t where 1 = 1 ";
        if (!IsNull(name)) {
            sql += " and name like '%" + name + "%' ";
        }
        if (!IsNull(abb_name)) {
            sql += " and abb_name like '%" + abb_name + "%' ";
        }
        //2： 全部
        if (!IsNull(operating) && !"2".equals(operating)) {
            sql += " and OPERATING = '" + operating + "' ";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("datas", JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql)));
        return jacksonUtil.toJson(map);
    }

    /**
     * 企业运营登记 → 添加企业列表
     *
     * @param request
     * @return
     */
    public String addBikeCompany(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String abb_name = getRequest(request, "abb_name");
        String name = getRequest(request, "name");
        String operating = getRequest(request, "operating");
        String sql = " insert into tb_global_company (ID,COMPANY_ID,ABB_NAME,NAME,OPERATING) values(?,?,?,?,?)";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, UUID.randomUUID().toString(), id, abb_name, name, operating) > 0 ? "0" : "1");
        return jacksonUtil.toJson(map);
    }

    /**
     * 企业运营登记 → 修改企业列表
     *
     * @param request
     * @return
     */
    public String updateBikeCompany(HttpServletRequest request) {
        String jlid = getRequest(request, "jlid");
        String id = getRequest(request, "id");
        String abb_name = getRequest(request, "abb_name");
        String name = getRequest(request, "name");
        String operating = getRequest(request, "operating");
        String sql = " update tb_global_company set COMPANY_ID = ? ,ABB_NAME = ? ,NAME = ? ,OPERATING = ?  where ID = ? ";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, id, abb_name, name, operating, jlid) > 0 ? "0" : "1");
        return jacksonUtil.toJson(map);
    }

    /**
     * 企业运营登记 → 删除企业列表
     *
     * @param request
     * @return
     */
    public String delBikeCompany(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String sql = "delete from  tb_global_company t  where id = ? ";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, id) > 0 ? "0" : "1");
        return jacksonUtil.toJson(map);
    }

    /**
     * 车辆里程管理 → 查询车辆里程
     *
     * @param request
     * @return
     */
    public String getVehicleMileage(HttpServletRequest request) {
        String cphm = getRequest(request, "cphm");
        String company_name = getRequest(request, "company_name");
        String sql = "select t.* from tb_mileage t where 1 = 1 and is_yx = 0";
        if (!IsNull(cphm)) {
            sql += " and cphm like '%" + cphm + "%' ";
        }
        if (!IsNull(company_name)) {
            sql += " and company_name like '%" + company_name + "%' ";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("datas", JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql)));
        return jacksonUtil.toJson(map);
    }

    /**
     * 车辆里程管理 → 修改车辆里程
     *
     * @param request
     * @return
     */
    public synchronized String updateVehicleMileage(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String cphm = getRequest(request, "cphm").trim();
        String mileage = getRequest(request, "mileage");
        String company_name = getRequest(request, "company_name");
        String sql = " update tb_mileage set cphm = ? ,mileage = ? ,company_name = ? ,change_time = now() where not exists (select 1 from (select 1 from tb_mileage where cphm=? and is_yx=0 and ID != ?) a) and ID = ?";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, cphm, mileage, company_name, cphm, id, id) > 0 ? "1" : "0");
        return jacksonUtil.toJson(map);


    }


//    private List<String> successList = null;
    private HashSet<String> successSet=null;
    private HashSet<String> repeatSet=null;
    private HashSet<String> errorSet=null;

    /**
     * 车辆里程管理 → 导入车辆里程
     *
     * @param request
     * @return
     */
    public synchronized String uploadVehicleMileage(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        HashMap<String, Object> map = new HashMap<>();
        String msg="";
        if (file.isEmpty()) {
            msg="文件内容为空";
            map.put("msg",msg);
            return jacksonUtil.toJson(map);
        }
        String fileName = file.getName();
        // 原文件名即上传的文件名
        String origFileName = file.getOriginalFilename();
        String suffix = origFileName.substring(origFileName.lastIndexOf(".") + 1);
        if(!(suffix.equals("xlsx"))){
            msg="文件格式应该为xlsx";
            map.put("msg",msg);
            return jacksonUtil.toJson(map);
        }
        String path = System.getProperty("user.dir")+"/excelFile/"+ origFileName;
//        String path ="d://erxi//" + origFileName;
        File ff = new File(path);
        // 检测是否存在目录
        if (!ff.getParentFile().exists()) {
            ff.getParentFile().mkdirs();// 新建文件夹
        }
        if(ff.exists()){
            ff.delete();
        }
        try {
            file.transferTo(ff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<Object>> list = null;
        try {
            list = excelUtil.readExcel(ff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list == null) {
            msg="读取内容为空";
            map.put("msg",msg);
            return jacksonUtil.toJson(map);
        } else {
            int count = 0;
            try {
                count = insertVehicleMileageList(list);
            }catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                count = 0;
            }
            if(count>0){
                msg="导入成功";
            }else{
                msg="导入失败";
            }
            map.put("success",successSet);
            map.put("repeat",repeatSet);
            map.put("error",errorSet);
        }
        map.put("msg",msg);
        return jacksonUtil.toJson(map);
    }

    public Integer insertVehicleMileageList(List<List<Object>> list) {
        successSet = new LinkedHashSet<>();
        repeatSet = new LinkedHashSet<>();
        errorSet = new LinkedHashSet<>();
        //获取导入开始位置
        int start = 0;
        for (int i = 0; i < list.size(); i++) {
            if(String.valueOf(list.get(i).get(0)).trim().replace(" ","").equals("接入平台公司名称")){
                start= i+1;
                if(start > list.size()-1){
                    return 0;
                }
                break;
            }
        }
        int count = 0;
        int vehicle_index = 1;
        for (int i = start; i < list.size(); i++) {
            int result = -1;
            //导入列数是否足够判断
            if(list.get(i).size() < 3){
                if(list.get(i).size() >= vehicle_index+1&&String.valueOf(list.get(i).get(vehicle_index)).trim()!="null"&&String.valueOf(list.get(i).get(vehicle_index)).trim()!=""){
                    errorSet.add(String.valueOf(list.get(i).get(vehicle_index)).trim());
                }
                continue;
            }
            String sql = " insert into tb_mileage (cphm,mileage,company_name)" +
                    " select ?,?,? from dual where not exists (select 1 from tb_mileage where cphm=? and is_yx=0)";
            try {
                result= jdbcTemplate.update(sql, String.valueOf(list.get(i).get(vehicle_index)).trim() ,String.valueOf(list.get(i).get(2)) ,String.valueOf(list.get(i).get(0)),String.valueOf(list.get(i).get(vehicle_index)).trim());
            }catch (Exception e){
                errorSet.add(String.valueOf(list.get(i).get(vehicle_index)).trim());
                e.printStackTrace();
            }
            if(result==0){
                repeatSet.add(String.valueOf(list.get(i).get(vehicle_index)).trim());
            }else if(result==1){
                successSet.add(String.valueOf(list.get(i).get(vehicle_index)).trim());
                count ++;
            }
        }
        return count;
    }

    /**
     * 车辆里程管理 → 添加车辆里程
     *
     * @param request
     * @return
     */
    public synchronized String addVehicleMileage(HttpServletRequest request) {
        String cphm = getRequest(request, "cphm").trim();
        String mileage = getRequest(request, "mileage");
        String company_name = getRequest(request, "company_name");
        String sql = " insert into tb_mileage (cphm,mileage,company_name)" +
                " select ?,?,? from dual where not exists (select 1 from tb_mileage where cphm=? and is_yx=0)";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, cphm ,mileage ,company_name, cphm) > 0 ? "1" : "0");
        return jacksonUtil.toJson(map);
    }

    /**
     * 车辆里程管理 → 删除车辆里程
     *
     * @param request
     * @return
     */
    public String delVehicleMileage(HttpServletRequest request) {
        String id = getRequest(request, "id");
        String sql = " update tb_mileage set is_yx = 1, change_time = now()  where ID = ? ";
        Map<String, Object> map = new HashMap<>();
        map.put("msg", jdbcTemplate.update(sql, id) > 0 ? "1" : "0");
        return jacksonUtil.toJson(map);
    }

    /**
     * 车辆里程预警 → 查询
     * @param request
     * @return
     */
    public String getVehicleMileageWarning(HttpServletRequest request) {
        String cphm = getRequest(request, "cphm");
        String companyname = getRequest(request, "companyname");
        String mileages = getRequest(request, "mileages");
        String status = getRequest(request, "status");
        String sql = "select s.*,g.abb_name,max(s.dbtime) max_dbtime from  (SELECT * FROM tb_mileage_state ORDER BY dbtime DESC) s" +
                " left join  TB_GLOBAL_COMPANY g on s.companyname = g.company_id" +
                " where 1=1 ";
        if (!IsNull(cphm)) {
            sql += " and s.cphm like '%" + cphm + "%' ";
        }
        if (!IsNull(companyname)&&!"0".equals(companyname)) {
            sql += " and s.companyname = '" + companyname + "' ";
        }
         if (!IsNull(mileages)) {
            sql += " and s.mileages = '" + mileages + "' ";
        }
         if (!IsNull(status)) {
            sql += " and s.status = '" + status + "' ";
        }
         sql += " group by s.cphm order by s.MILEAGES desc, s.COMPANYNAME, s.cphm";
        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        String list_status;
        for (int i = 0; i < list.size(); i++) {
            list_status = list.get(i).get("STATUS")==null?"":list.get(i).get("STATUS").toString();
            if("0".equals(list_status)){
                list.get(i).put("STATUS","正常");
            }else if("1".equals(list_status)){
                list.get(i).put("STATUS","预警");
            }else if("2".equals(list_status)){
                list.get(i).put("STATUS","报警");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("datas", list);
        return jacksonUtil.toJson(map);
    }

    public String getEnterpriseDaily(HttpServletRequest request) {
        String time = getRequest(request, "time");
        String table = time.substring(2, 4) + time.substring(5, 7);
        String tj1 = "";
        String tj2 = "";
        String tj3 = "";
        if (!IsNull(time)) {
            tj1 += " and date_format(dbtime,'%Y-%m-%d') ='" + time + "'";
            tj2 += " and date_format(db_time,'%Y-%m-%d') = '" + time + "'";
            tj3 += " and date_sub(db_time,interval 1 day) = '" + (time+" 00:00:00") + "'";
        }
        String sql = "select a.*,b.*,bb.active_num,g.abb_name,concat(round((b.dep_num / b.match_num) * 100 ),'%'),round((b.dep_num / b.match_num) * 100, 2) dep_rate from" +
                " (select companyid,round(sum(drivemile)) drivemile,round(sum(factprice)) factprice from tb_order_pay_statis_"+table+" where 1=1";
        sql += tj1;
        sql +=  "  group by companyid) a" +
                " left join (select companyid,sum(match_num) match_num, sum(dep_num) dep_num from tb_order_pt_state where 1=1";
        sql += tj2;
        sql +=  "  group by companyid) b on b.companyid = a.companyid" +
                " left join tb_order_pt_state bb on a.companyid = bb.companyid";
        sql += tj3;
        sql +=  " left join tb_global_company g on a.companyid = g.company_id" +
                " where 1=1 and b.companyid is not null" +
                " order by a.companyid";
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        }catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("DEP_RATE",list.get(i).get("DEP_RATE")+"%");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("datas", list);
        return jacksonUtil.toJson(map);
    }

    public String getEnterpriseMonthlyReport(HttpServletRequest request) {
        String time = getRequest(request, "time");
        String table = time.substring(2, 4) + time.substring(5, 7);
        String tj1 = "";
        String tj2 = "";
        String tj3 = "";
        if (!IsNull(time)) {
            tj1 += " and date_format(dbtime,'%Y-%m') ='" + time + "'";
            tj2 += " and date_format(db_time,'%Y-%m') = '" + time + "'";
            tj3 += " and date_format(date_sub(db_time,interval 1 day),'%Y-%m') = '" + time + "' and date_format(date_sub(db_time,interval 1 day),'%H:%i:%s') = '00:00:00'";
        }
        String sql = "select a.*,b.*,bb.active_num,g.abb_name,concat(round((b.dep_num / b.match_num) * 100 ),'%'),round((b.dep_num / b.match_num) * 100, 2) as dep_rate from" +
                " (select companyid,round(sum(drivemile)) drivemile,round(sum(factprice)) factprice from tb_order_pay_statis_"+table+" where 1=1";
        sql += tj1;
        sql +=  "  group by companyid) a" +
                " left join (select companyid,sum(match_num) match_num, sum(dep_num) dep_num from tb_order_pt_state where 1=1";
        sql += tj2;
        sql +=  "  group by companyid) b on b.companyid = a.companyid" +
                " left join (select companyid, round(sum(active_num)/(select count(DISTINCT date_format(db_time,'%Y-%m-%d')) from tb_order_pt_state where 1=1";
        sql += tj2;
        sql +=  ")) active_num from tb_order_pt_state where 1=1 ";
        sql += tj3;
        sql +=  "  group by companyid) bb on a.companyid = bb.companyid" +
                " left join tb_global_company g on a.companyid = g.company_id" +
                " where 1=1 and b.companyid is not null" +
                " order by a.companyid";
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = JacksonUtil.getUpperCaseList(jdbcTemplate.queryForList(sql));
        }catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("DEP_RATE",list.get(i).get("DEP_RATE")+"%");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("datas", list);
        return jacksonUtil.toJson(map);
    }
}