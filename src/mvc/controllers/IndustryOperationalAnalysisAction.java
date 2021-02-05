package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.IndustryOperationalAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 行业运行分析
@Controller
@RequestMapping("/getIndustryOperationalAnalysisAction")
public class IndustryOperationalAnalysisAction {
	@Autowired
	private IndustryOperationalAnalysisService industryOperationalAnalysisService;
	private DownloadAct downloadAct = new DownloadAct();
	
	// 营运出车统计
	@RequestMapping("/getOperatingOutboundStatistics")
	@ResponseBody
	public String OperatingOutboundStatistics(HttpServletRequest request, String sTime,String eTime){
		return industryOperationalAnalysisService.getOperatingOutboundStatistics(request, sTime,eTime);
	}
	
	// 导出营运出车统计Excel
	@RequestMapping("/getOperatingOutboundStatisticsExcel")
	@ResponseBody
	public String OperatingOutboundStatisticsExcel(HttpServletRequest request,HttpServletResponse response,String sTime,String eTime) throws IOException{
		String a[] = {"统计日期","订单量","出车量","杭州许可总量","出车率"};
		String b[] = {"FORMAT_DATE","ORDER_NUMBER","DRIVER_OUT_NUMBER","PERMIT_NUMBER","RATE"};
		String c[] = {"统计时间","日均订单总量","日均出车量","日均出车率"};
		String d[] = {"VALUE","SUM_ORDER_NUMBER","SUM_DRIVER_OUT_NUMBER","RATE"};
		String gzb = "营运出车统计";
		Map<String, Object> map = industryOperationalAnalysisService.getOperatingOutboundStatisticsExcel(request, sTime,eTime);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("datas");
		List<Map<String, Object>> list1 = (List<Map<String, Object>>) map.get("statistics");	
		DownloadAct.downloadtwo(request,response,list1,list,d,c,b,a,gzb);
        return null;
	}
	
	// 企业月报
	@RequestMapping("/getEnterpriseMonthlyReport")
	@ResponseBody
	public String EnterpriseMonthlyReport(HttpServletRequest request, String time){
		return industryOperationalAnalysisService.getEnterpriseMonthlyReport(request, time);
	}
	
	// 企业月报导出
	@RequestMapping("/getEnterpriseMonthlyReportExcel")
	@ResponseBody
	public String EnterpriseMonthlyReportExcel(HttpServletRequest request,HttpServletResponse response,String time) throws IOException{
		String gzb = "企业月报导出";
		Map<String, Object> map = industryOperationalAnalysisService.getEnterpriseMonthlyReportExcel(request, time);
		List<Map<String, Object>> timeList = (List<Map<String, Object>>) map.get("timeList");
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
		
		String a[] = new String[40];
		String b[] = new String[40];
		a[0] = "企业名称";
		a[1] = "全月";
		
		b[0] = "ABB_NAME";
		b[1] = "sum";
		
		for(int i=0;i<timeList.size();i++){
			String s = String.valueOf(timeList.get(i).get("time"));
			a[2+i] = s;
			b[2+i] = s;
		}
		downloadAct.download(request,response,a,b,gzb,list);
		return "";
	}

}
