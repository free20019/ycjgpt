package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.OperationAndWarningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 日常营运监测预警
@Controller
@RequestMapping("/getOperationAndWarningAction")
public class OperationAndWarningAction {
	@Autowired
	private OperationAndWarningService operationAndWarningService;
	private DownloadAct downloadAct = new DownloadAct();
	// 违规营运分析
	@RequestMapping("/getViolationOperationAnalysis")
	@ResponseBody
	public String ViolationOperationAnalysis(HttpServletRequest request){
		String platformName = request.getParameter("platformName");
		String sTime = request.getParameter("sTime");
		String eTime = request.getParameter("eTime");
		String vehicleNumber = request.getParameter("vehicleNumber");
		String driverNumber = request.getParameter("driverNumber");
		String violationType = request.getParameter("violationType");
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		return operationAndWarningService.getViolationOperationAnalysis(request,platformName,sTime,eTime,vehicleNumber,driverNumber,violationType,page,pageSize);
	}
	
	// 获取违规营运分析车牌号码
	@RequestMapping("/getViolationOperationAnalysisVehicleNumber")
	@ResponseBody
	public String getViolationOperationAnalysisVehicleNumber(HttpServletRequest request,@RequestParam("postData") String postData){
		return operationAndWarningService.getViolationOperationAnalysisVehicleNumber(request,postData);
	}
	
	// 获取平台名称
	@RequestMapping("/getPlatformName")
	@ResponseBody
	public String getPlatformName(){
		return operationAndWarningService.getPlatformName();
	}
	
	// 导出违规营运分析Excel
	@RequestMapping("/getViolationOperationAnalysisExcel")
	@ResponseBody
	public String ViolationOperationAnalysisExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String page = "1";
		String pageSize = "9999999";
		String platformName = request.getParameter("platformName");
		String sTime = request.getParameter("sTime");
		String eTime = request.getParameter("eTime");
		String vehicleNumber = request.getParameter("vehicleNumber");
		String driverNumber = request.getParameter("driverNumber");
		String violationType = request.getParameter("violationType");
		String type = request.getParameter("type");
		String filedLabel = request.getParameter("filedLabel");
		String filedValue = request.getParameter("filedValue");
		String a[] = {"平台公司","订单编号","订单开始时间","订单结束时间","出发位置","到达位置","订单金额","违规类型","驾驶员姓名","驾驶员证号","车牌号码"};
		String b[] = {"ABB_NAME","ORDER_NUMBER","START_TIME","END_TIME","START_POSITION","END_POSITION","ORDER_MONEY","VIOLATION_TYPE","DRIVER_NAME","DRIVER_NUMBER","VEHICLE_NUMBER"};
		String gzb = "违规营运分析";
		List<Map<String, Object>> list = operationAndWarningService.getViolationOperationAnalysisExcel(request,platformName,sTime,eTime,vehicleNumber,driverNumber,violationType,page,pageSize);
		if(type.equals("2")) {
			String [] label = filedLabel.split(",");
			String [] value = filedValue.split(",");
			String aa[] = new String[label.length];
			String bb[] = new String[label.length];
			for(int i=0; i<label.length; i++) {
				aa[i] = label[i];
				bb[i] = value[i];
			}
			DownloadAct.downloadBig(request,response,aa,bb,gzb,list);
			return null;
		}
		DownloadAct.downloadBig(request,response,a,b,gzb,list);
        return null;
	}
	
	// 车辆超期预警
	@RequestMapping("/getVehicleOverdueWarning")
	@ResponseBody
	public String VehicleOverdueWarning(HttpServletRequest request) {
		return operationAndWarningService.getVehicleOverdueWarningList(request,false);
	}
	
	/**
	 * 未营运车辆统计导出
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/exportVehicleOverdueWarning")
	@ResponseBody
	public String exportVehicleOverdueWarning(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String a[] = {"车牌号码","车辆所有人","联系电话","行驶证注册日期","有效开始日期","有效截止日期","状态"};//导出列明
		String b[] = {"AUTO_NUM","OWNER","OWNER_TEL","START_DATE","END_DATE","LICENSE_REGISTER_DATE","TYPE"};//导出map中的key
		String gzb = "车辆超期预警";//导出sheet名和导出的文件名khexportList
		List<Map<String, Object>> list = operationAndWarningService.getVehicleOverdueWarning(request, true);
		DownloadAct.downloadBig(request,response,a,b,gzb,list);
		return null;
	}
	
	// 获取车辆超期预警车牌号码
	@RequestMapping("/getVehicleOverdueWarningVehicleNumber")
	@ResponseBody
	public String getVehicleOverdueWarningVehicleNumber(HttpServletRequest request){
		String PLATE_NUMBER = request.getParameter("PLATE_NUMBER");
		return operationAndWarningService.getVehicleOverdueWarningVehicleNumber(PLATE_NUMBER);
		
	}
	
	
	

}
