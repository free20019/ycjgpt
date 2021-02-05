package mvc.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import helper.DownloadAct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.ManageServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/manage")
public class ManageAction {
	private ManageServer manageService;

	public ManageServer getManageServer() {
		return manageService;
	}

	private DownloadAct downloadAct = new DownloadAct();
	@Autowired
	public void setManageServer(ManageServer manageService) {
		this.manageService = manageService;
	}
	
	/**
	 * 许可信息查询  → 查询人 
	 * 用到的表 ： person_base_info   person_taxi_info
	 * @param request
	 */
	@RequestMapping("/findPerson")
	@ResponseBody
	public String findPerson(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPerson(request,postData);
		return msg;
	}

	/**
	 * 许可信息查询  → 查询企业
	 * 用到的表 ： platforms_base_info
	 * @param request
	 */
	@RequestMapping("/findPlatforms")
	@ResponseBody
	public String findPlatforms(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPlatforms(request,postData);
		return msg;
	}
	
	/**
	 * 许可信息查询  → 查询车辆
	 * 用到的表 ： person_taxi_info
	 * @param request
	 */
	@RequestMapping("/findPlatcar")
	@ResponseBody
	public String findPlatcar(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPlatcar(request,postData);
		return msg;
	}
	
	/**
	 * 查询平台基本信息
	 * @param request
	 */
	@RequestMapping("/findCompany")
	@ResponseBody
	public String findCompany(HttpServletRequest request) {
		String msg = manageService.findCompany(request);
		return msg;
	}
	/**
	 * 查询平台基本信息
	 * @param request
	 */
	@RequestMapping("/findOnlyCompany")
	@ResponseBody
	public String findOnlyCompany(HttpServletRequest request) {
		String msg = manageService.findOnlyCompany(request);
		return msg;
	}
	/**
	 * 综合信息查询 → 查询平台基本信息
	 * @param request
	 */
	@RequestMapping("/findPtxx")
	@ResponseBody
	public String findPtxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtxx(request,postData);
		return msg;
	}
	/**
	 * 查询平台经营许可信息
	 * @param request
	 */
	@RequestMapping("/findPtjyxkxx")
	@ResponseBody
	public String findPtjyxkxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtjyxkxx(request,postData);
		return msg;
	}
	
	/**
	 * 查询平台营运规模
	 * @param request
	 */
	@RequestMapping("/findPtyygmxx")
	@ResponseBody
	public String findPtyygmxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtyygmxx(request,postData);
		return msg;
	}
	/**
	 * 查询平台支付信息
	 * @param request
	 */
	@RequestMapping("/findPtzfxx")
	@ResponseBody
	public String findPtzfxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtzfxx(request,postData);
		return msg;
	}
	/**
	 * 查询平台服务机构
	 * @param request
	 */
	@RequestMapping("/findPtfwjg")
	@ResponseBody
	public String findPtfwjg(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtfwjg(request,postData);
		return msg;
	}
	/**
	 * 查询平台运价信息
	 * @param request
	 */
	@RequestMapping("/findPtyjxx")
	@ResponseBody
	public String findPtyjxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtyjxx(request,postData);
		return msg;
	}
	/**
	 * 综合车辆查询 → 查询车辆信息
	 * @param request
	 */
	@RequestMapping("/findClxx")
	@ResponseBody
	public String findClxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findClxx(request,postData);
		return msg;
	}
	
	/**
	 * 综合车辆查询 → 查询车辆信息 → 车辆里程信息
	 * @param request
	 */
	@RequestMapping("/findCarClxx")
	@ResponseBody
	public String findCarClxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findCarClxx(request,postData);
		return msg;
	}
	
	/**
	 * 查询车辆保险信息
	 * @param request
	 */
	@RequestMapping("/findClbxxx")
	@ResponseBody
	public String findClbxxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findClbxxx(request,postData);
		return msg;
	}
	/**
	 * 模糊查询车辆保险信息  车牌号码
	 * @param request
	 */
	@RequestMapping("/findBxVehicleNo")
	@ResponseBody
	public String findBxVehicleNo(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findVehicleNo(request, postData, "tb_vehicleinsurance");
		return msg;
	}
	/**
	 * 查询车辆里程信息
	 * @param request
	 */
	@RequestMapping("/findCllcxx")
	@ResponseBody
	public String findCllcxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findCllcxx(request,postData);
		return msg;
	}
	/**
	 * 模糊查询车辆里程信息  车牌号码
	 * @param request
	 */
	@RequestMapping("/findlcVehicleNo")
	@ResponseBody
	public String findlcVehicleNo(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findVehicleNo(request, postData, "tb_vehicletotalmile");
		return msg;
	}
	
	/**
	 * 综合驾驶员查询 → 查询驾驶员信息
	 * @param request
	 */
	@RequestMapping("/findJsyxx")
	@ResponseBody
	public String findJsyxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findJsyxx(request,postData);
		return msg;
	}
	
	/**
	 * 综合驾驶员查询 → 查询驾驶员信息 → 驾驶员移动终端信息
	 * @param request
	 */
	@RequestMapping("/getDriverApp")
	@ResponseBody
	public String getDriverApp(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.getDriverApp(request,postData);
		return msg;
	}
	
	/**
	 * 综合驾驶员查询 → 查询驾驶员信息 → 驾驶员统计信息
	 * @param request
	 */
	@RequestMapping("/getDriverStat")
	@ResponseBody
	public String getDriverStat(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.getDriverStat(request,postData);
		return msg;
	}
	
	/**
	 * 查询驾驶员培训信息
	 * @param request
	 */
	@RequestMapping("/findJsypxxx")
	@ResponseBody
	public String findJsypxxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findJsypxxx(request,postData);
		return msg;
	}
	/**
	 * 查询驾驶员移动终端信息
	 * @param request
	 */
	@RequestMapping("/findJsyydzdxx")
	@ResponseBody
	public String findJsyydzdxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findJsyydzdxx(request,postData);
		return msg;
	}
	/**
	 * 查询驾驶员统计信息
	 * @param request
	 */
	@RequestMapping("/findJsytjxx")
	@ResponseBody
	public String findJsytjxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findJsytjxx(request,postData);
		return msg;
	}
	/**
	 * 查询订单信息
	 * @param request
	 */
	@RequestMapping("/findDdxx")
	@ResponseBody
	public String findDdxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findDdxx(request,postData);
		return msg;
	}
	/**
	 *  基础信息查询 → 查询支付明细信息
	 * @param request
	 */
	@RequestMapping("/findPay")
	@ResponseBody
	public String findPay(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPay(request,postData);
		return msg;
	}
	
	/**
	 *  基础信息查询 → 查询投诉信息
	 * @param request
	 */
	@RequestMapping("/findComplaint")
	@ResponseBody
	public String findComplaint(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findComplaint(request,postData);
		return msg;
	}
	
	/**
	 * 查询上线率分析
	 * @param request
	 */
	@RequestMapping("/findSxl")
	@ResponseBody
	public String findSxl(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findSxl(request,postData);
		return msg;
	}
	/**
	 * 查询周转率分析
	 * @param request
	 */
	@RequestMapping("/findZzlv")
	@ResponseBody
	public String findZzlv(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findZzlv(request,postData);
		return msg;
	}
	/**
	 * 查询周转量分析
	 * @param request
	 */
	@RequestMapping("/findZzliang")
	@ResponseBody
	public String findZzliang(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findZzliang(request,postData);
		return msg;
	}
	/**
	 * 查询平台基本信息
	 * @param request
	 */
	@RequestMapping("/findPtjcxx")
	@ResponseBody
	public String findPtjcxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findPtjcxx(request,postData);
		return msg;
	}
	/**
	 * 查询车辆基础信息
	 * @param request
	 */
	@RequestMapping("/findCljcxx")
	@ResponseBody
	public String findCljcxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findCljcxx(request,postData);
		return msg;
	}
	/**
	 * 模糊查询车辆保险信息  车牌号码
	 * @param request
	 */
	@RequestMapping("/findJcVehicleNo")
	@ResponseBody
	public String findJcVehicleNo(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findVehicleNo(request, postData, "tb_vehiclebaseinfo");
		return msg;
	}
	/**
	 * 查询驾驶员基础信息
	 * @param request
	 */
	@RequestMapping("/findJsyjcxx")
	@ResponseBody
	public String findJsyjcxx(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findJsyjcxx(request,postData);
		return msg;
	}
	
	/**
	 * 查询周订单
	 * @param request
	 */
	@RequestMapping("/findOrderWeek")
	@ResponseBody
	public String findOrderWeek(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findOrderWeek(request,postData);
		return msg;
	}
	
	/**
	 * 查询周订单Excel导出
	 * @param request
	 */
	@RequestMapping("/findOrderWeekExcel")
	@ResponseBody
	public String findOrderWeekExcel(HttpServletRequest request,HttpServletResponse response,String time) throws IOException{
		List<Map<String, Object>> list = manageService.findOrderWeekExcel(time,request);
		String gzb = "企业日报导出";
		String a[] = new String[30];
		String b[] = new String[30];
		a[0] = "企业名称";
		a[1] = "全天";
		
		b[0] = "companyName";
		b[1] = "sumNumber";
		
		for(int i=2;i<27;i++){
			a[i] = i - 2 + "时";
			b[i] = "y" + (i - 2);
		}
		DownloadAct.download(request,response,a,b,gzb,list);
		return "";
	}
	
	/**
	 * 查询周订单 echarts
	 * @param request
	 */
	@RequestMapping("/findOrderWeekEchart")
	@ResponseBody
	public String findOrderWeekEchart(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findOrderWeekEchart(request,postData);
		return msg;
	}

	/**
	 * 异常经营分析
	 * @param request
	 */
	@RequestMapping("/findErrorOperate")
	@ResponseBody
	public String findErrorOperate(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findErrorOperate(request,postData);
		return msg;
	}

	//异常经营分析导出
	@RequestMapping("/exportErrorOperate")
	@ResponseBody
	public String exportErrorOperate(HttpServletRequest request,
									 HttpServletResponse response,@RequestParam("postData") String postData) throws Exception{
		String a[] = {"公司名称","动态车辆数","车辆已办证数","动态驾驶员数","驾驶员已办证数","今日订单数","双合规订单数","双合规率","双合规人员数","车合规订单数",
				"车合规车辆数","人合规订单数","人合规人员数","双违规订单数","双违规人车数","时间"};//导出列明
		String b[] = {"COMPANYNAME","CAR_NUM","YBZS","DRIVER_NUM","DRIVER_LICENSE_NUM","DAY_ORDER","COMPLIANCE_ORDER","COMPLIANCE_RATE",
				"COMPLIANCE_PEOPLE","CAR_COMPLIANCE_ORDER","CAR_COMPLIANCE_NUM","PEOPLE_COMPLIANCE_ORDER","PEOPLE_COMPLIANCE_NUM","VIOLATION_ORDER","VIOLATION_PEOPLE","TIME"};//导出map中的key
		String gzb = "异常经营分析";//导出sheet名和导出的文件名khexportList
		String msg = manageService.findErrorOperate(request,postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List1(msg);//导出的数据
		downloadAct.download(request,response,a,b,gzb,list);
		return null;
	}

	/**
	 * 未营运车辆统计
	 * @param request
	 */
	@RequestMapping("/findCarNotOperate")
	@ResponseBody
	public String findCarNotOperate(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findCarNotOperateForList(request,postData,false);
		return msg;
	}
	
	/**
	 * 未营运车辆统计导出
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/exportCarNotOperate")
	@ResponseBody
	public String exportCarNotOperate(HttpServletRequest request,HttpServletResponse response,@RequestParam("postData") String postData) throws IOException {
		String a[] = {"车牌号码","车辆所有人","联系电话","行驶证注册日期","有效开始日期","有效截止日期","最近营运时间","营运状态"};//导出列明
		String b[] = {"AUTO_NUM","OWNER","OWNER_TEL","START_DATE","END_DATE","LICENSE_REGISTER_DATE","LAST_TIME","last_type"};//导出map中的key
		String gzb = "未营运车辆统计";//导出sheet名和导出的文件名khexportList
		List<Map<String, Object>>list = manageService.findCarNotOperate(request, postData, true);
		DownloadAct.downloadBig(request,response,a,b,gzb,list);
		return null;
	}
	
	/**
	 * 未营运车辆统计导出
	 * @param request
	 */
	@RequestMapping("/findNotOperateVehicleNo")
	@ResponseBody
	public String findNotOperateVehicleNo(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = manageService.findNotOperateVehicleNo(request,postData);
		return msg;
	}
}