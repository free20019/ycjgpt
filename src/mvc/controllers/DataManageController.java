package mvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.DownloadAct;
import helper.SpecialExcelUtil;
import mvc.service.DataManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jingwei·li
 * @date 2020/3/31
 */
@Controller
@RequestMapping("/data")
public class DataManageController {
	@Autowired
	DataManageService dataManageService;

	DownloadAct downloadAct = new DownloadAct();
	SpecialExcelUtil specialExcelUtil = new SpecialExcelUtil();
	
	/**
	 * 数据接入管理 → 查询数据列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDataList")
	@ResponseBody
	public String getDataList(HttpServletRequest request){
		String msg = dataManageService.getDataList(request);
		return msg;
	}
	
	/**
	 * 数据接入管理 → 获取图表显示数据
	 * @return
	 */
	@RequestMapping("/getChartInfo")
	@ResponseBody
	public String getChartInfo(){
		String msg = dataManageService.getChartInfo();
		return msg;
	}
	
	/**
	 * 数据接入管理 → 修改数据
	 * @return
	 */
	@RequestMapping("/editData")
	@ResponseBody
	public String editData(HttpServletRequest request){
		String msg = dataManageService.editData(request);
		return msg;
	}

	/**
	 * 数据接入管理 → 添加数据
	 * @return
	 */
	@RequestMapping("/addData")
	@ResponseBody
	public String addData(HttpServletRequest request){
		String msg = dataManageService.addData(request);
		return msg;
	}
	

	/**
	 * 数据接入管理 → 删除数据
	 * @return
	 */
	@RequestMapping("/delData")
	@ResponseBody
	public String delData(HttpServletRequest request){
		String msg = dataManageService.delData(request);
		return msg;
	}
	
	/**
	 * 运力分析报告 → 查询数据
	 * @return
	 */
	@RequestMapping("/getAnalyze")
	@ResponseBody
	public String getAnalyze(HttpServletRequest request){
		String msg = (String) dataManageService.getAnalyze(request,"0");
		return msg;
	}

	/**
	 * 运力分析报告 → 导出数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportAnalyze")
	public void exportAnalyze(HttpServletRequest request,HttpServletResponse response) throws IOException{
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) dataManageService.getAnalyze(request,"1");
		String[] keys = new String[]{"DBTIME1","CREATE_ORDER_NUM","CREATE_YESTERDAY","CREATE_LASTWEEK","DEP_ORDER_NUM","DEP_YESTERDAY","DEP_LASTWEEK","DEP_ORDER_VEH","VEH_YESTERDAY","VEH_LASTWEEK","END_ORDER","AREA_NAME","AREA_NUM","HOT_TIME","TIME_NUM"};
		specialExcelUtil.download(request, response, list, keys, "运力分析报告");
	}

	/**
	 * 特殊目标查询 → 查询数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/getSpecialTarget")
	@ResponseBody
	public String getSpecialTarget(HttpServletRequest request){
		String msg = dataManageService.getSpecialTarget(request);
		return msg;
	}
	
	/**
	 * 特殊目标查询 → 查询驾驶员信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDriverInfo")
	@ResponseBody
	public String getDriverInfo(HttpServletRequest request){
		String msg = dataManageService.getDriverInfo(request);
		return msg;
	}
	
	/**
	 * 特殊目标查询 → 查询车辆信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCarInfo")
	@ResponseBody
	public String getCarInfo(HttpServletRequest request){
		String msg = dataManageService.getCarInfo(request);
		return msg;
	}
	
	/**
	 * 特殊目标查询 → 查询订单明细
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPayInfo")
	@ResponseBody
	public Object getPayInfo(HttpServletRequest request){
		Object msg = dataManageService.getPayInfo(request,"0");
		return msg;
	}

	/**
	 * 特殊目标查询 → 查询订单明细
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportPayInfo")
	public void exportPayInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String vehicleno = String.valueOf(request.getParameter("vehicleno"));
		String field[] = {"ABB_NAME","DRIVERNAME","LICENSEID","VEHICLENO","DEPTIME","DEPAREA","DESTTIME","DESTAREA","FACTPRICE"};
		String fieldName[] = {"平台名称","姓名","身份证号","服务车号","上车时间","上车地点","下车时间","下车地点","营收金额"};
		String name = vehicleno+"的订单明细信息";
		List<Map<String, Object>> list = (List<Map<String, Object>>) dataManageService.getPayInfo(request,"1");
		DownloadAct.download(request, response, fieldName, field , name, list);
	}
	/**
	 * 数据接入检测查询
	 * @return
	 */
	@RequestMapping("/getCompanyDataFlow")
	@ResponseBody
	public String getCompanyDataFlow(HttpServletRequest request){
		String msg = dataManageService.getCompanyDataFlow(request);
		return msg;
	}
	
	/**
	 * 单车收益分析 → 获取单车数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBikeAnalysis")
	@ResponseBody
	public String getBikeAnalysis(HttpServletRequest request){
		String msg = dataManageService.getBikeAnalysis(request);
		return msg;
	}
	
	/**
	 * 单车收益分析 → 获取单车Echarts
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBikeEcharts")
	@ResponseBody
	public String getBikeEcharts(HttpServletRequest request){
		String msg = dataManageService.getBikeEcharts(request);
		return msg;
	}
	
	/**
	 * 企业运营登记 → 查询企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBikeCompany")
	@ResponseBody
	public String getBikeCompany(HttpServletRequest request){
		String msg = dataManageService.getBikeCompany(request);
		return msg;
	}
	/**
	 * 企业运营登记 → 修改企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateBikeCompany")
	@ResponseBody
	public String updateBikeCompany(HttpServletRequest request){
		String msg = dataManageService.updateBikeCompany(request);
		return msg;
	}
	/**
	 * 企业运营登记 → 添加企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/addBikeCompany")
	@ResponseBody
	public String addBikeCompany(HttpServletRequest request){
		String msg = dataManageService.addBikeCompany(request);
		return msg;
	}
	/**
	 * 企业运营登记 → 删除企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/delBikeCompany")
	@ResponseBody
	public String delBikeCompany(HttpServletRequest request){
		String msg = dataManageService.delBikeCompany(request);
		return msg;
	}

	/**
	 * 车辆里程管理 → 查询车辆里程
	 * @param request
	 * @return
	 */
	@RequestMapping("/getVehicleMileage")
	@ResponseBody
	public String getVehicleMileage(HttpServletRequest request){
		String msg = dataManageService.getVehicleMileage(request);
		return msg;
	}
	/**
	 * 车辆里程管理 → 修改车辆里程
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateVehicleMileage")
	@ResponseBody
	public String updateVehicleMileage(HttpServletRequest request){
		String msg = dataManageService.updateVehicleMileage(request);
		return msg;
	}
	/**
	 * 车辆里程管理 → 导入车辆里程
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadVehicleMileage")
	@ResponseBody
	public String uploadVehicleMileage(HttpServletRequest request, @RequestParam("file") MultipartFile file){
		String msg = dataManageService.uploadVehicleMileage(request, file);
		return msg;
	}
	/**
	 * 车辆里程管理 → 添加车辆里程
	 * @param request
	 * @return
	 */
	@RequestMapping("/addVehicleMileage")
	@ResponseBody
	public String addVehicleMileage(HttpServletRequest request){
		String msg = dataManageService.addVehicleMileage(request);
		return msg;
	}
	/**
	 * 车辆里程管理 → 删除车辆里程
	 * @param request
	 * @return
	 */
	@RequestMapping("/delVehicleMileage")
	@ResponseBody
	public String delVehicleMileage(HttpServletRequest request){
		String msg = dataManageService.delVehicleMileage(request);
		return msg;
	}

	/**
	 * 车辆里程预警 → 查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/getVehicleMileageWarning")
	@ResponseBody
	public String getVehicleMileageWarning(HttpServletRequest request){
		String msg = dataManageService.getVehicleMileageWarning(request);
		return msg;
	}

	/**
	 * 车辆里程预警 → 导出
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportVehicleMileageWarning")
	public String exportVehicleMileageWarning(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String a[] = {"平台公司名称","车牌号码","总行驶里程(千米)","状态"};//导出列明
		String b[] = {"ABB_NAME", "CPHM", "MILEAGES", "STATUS"};//导出map中的key
		String gzb = "车辆里程预警";//导出sheet名和导出的文件名khexportList
		String msg = dataManageService.getVehicleMileageWarning(request);
		List<Map<String, Object>> list = DownloadAct.parseJSON2List1(msg);//导出的数据
		DownloadAct.download(request, response, a, b, gzb, list);
		return null;
	}

	/**
	 * 营运日报 → 查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/getEnterpriseDaily")
	@ResponseBody
	public String getEnterpriseDaily(HttpServletRequest request){
		String msg = dataManageService.getEnterpriseDaily(request);
		return msg;
	}

	/**
	 * 营运日报 → 导出
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportEnterpriseDaily")
	public String exportEnterpriseDaily(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String a[] = {"企业名称","活跃车辆数","接单量","完单量","完单率","载客里程(千米)","营收金额(元)"};//导出列明
		String b[] = {"ABB_NAME", "ACTIVE_NUM", "MATCH_NUM", "DEP_NUM", "DEP_RATE", "DRIVEMILE", "FACTPRICE"};//导出map中的key
		String gzb = "营运日报";//导出sheet名和导出的文件名khexportList
		String msg = dataManageService.getEnterpriseDaily(request);
		List<Map<String, Object>> list = DownloadAct.parseJSON2List1(msg);//导出的数据
		DownloadAct.download(request, response, a, b, gzb, list);
		return null;
	}

	/**
	 * 营运月报 → 查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/getEnterpriseMonthlyReport")
	@ResponseBody
	public String getEnterpriseMonthlyReport(HttpServletRequest request){
		String msg = dataManageService.getEnterpriseMonthlyReport(request);
		return msg;
	}

	/**
	 * 营运月报 → 导出
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportEnterpriseMonthlyReport")
	public String exportEnterpriseMonthlyReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String a[] = {"企业名称","日均活跃车辆数","接单量","完单量","完单率","载客里程(千米)","营收金额(元)"};//导出列明
		String b[] = {"ABB_NAME", "ACTIVE_NUM", "MATCH_NUM", "DEP_NUM", "DEP_RATE", "DRIVEMILE", "FACTPRICE"};//导出map中的key
		String gzb = "营运月报";//导出sheet名和导出的文件名khexportList
		String msg = dataManageService.getEnterpriseMonthlyReport(request);
		List<Map<String, Object>> list = DownloadAct.parseJSON2List1(msg);//导出的数据
		DownloadAct.download(request, response, a, b, gzb, list);
		return null;
	}
}
