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
 * @Description:静态数据
 * @date: 2020/7/27 - 11:27
 */
@Service
public class StaticDataService {

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

    private void log(HttpServletRequest request,String model,String s){
        String username = String.valueOf(request.getSession().getAttribute("username"));
        String sql = "insert into JGPT_HANDLE_LOG (USERNAME,HANDLE,CONTENT,DBTIME) values (?,?,?,?)";
        int list = jdbcTemplate.update(sql,username,model,s,time);
    }

    public String findPassengerBaseInfo(HttpServletRequest request ,String postData) {
//        log(request,"静态数据查询 → 乘客基本信息", "静态数据查询 → 乘客基本信息");
        Map<String, Object> parampMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String ptname = String.valueOf(parampMap.get("ptname"));
        String phone = String.valueOf(parampMap.get("phone"));
        String begintime = String.valueOf(parampMap.get("begintime"));
        String endtime = String.valueOf(parampMap.get("endtime"));
        int pageIndex = Integer.valueOf(String.valueOf(parampMap.get("pageIndex")));
        int pageSize = Integer.valueOf(String.valueOf(parampMap.get("pageSize")));

        List<Object> queryList=new ArrayList<Object>();
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
        if (!isNull(phone)) {
            tj += " and t.PassengerPhone like ?";
            queryList.add(phone+"%");
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
        if (!isNull(phone)) {
            tj2 += " and t.PassengerPhone like ?";
            queryList.add(phone+"%");
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
        String sql = "select (select count(*) from  tb_passengerbaseinfo  t" +
                " where 1 = 1 and t.Flag!=3 and t.State=0 " + tj + ") as count ,tt.* from " +
                " (select t.*,t2.CompanyName from  tb_passengerbaseinfo t" +
                " left join tb_company t2 on t.CompanyId = t2.companyid" +
                " where 1=1 and t.Flag!=3 and t.State=0 " + tj2+ " " +
                " order by t.UpdateTime desc limit ?,?" +
                ")tt";

        List<Map<String, Object>> list = JacksonUtil.getUpperCaseList(jdbcTemplate5.queryForList(sql,queryList.toArray()));
        map.put("datas", list);
        getCount(map, list);
        map.put("msg", "0");
        return jacksonUtil.toJson(map);
    }
}
