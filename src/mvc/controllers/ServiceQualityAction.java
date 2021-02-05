package mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import mvc.service.ServiceQualityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/getServiceQualityAction")
public class ServiceQualityAction {

	@Autowired
	private ServiceQualityService serviceQualityService;
	
	// 12328投诉查询
	@RequestMapping("/getComplaintEnquiry")
	@ResponseBody
	public String ComplaintEnquiry(HttpServletRequest request){
		String stime = request.getParameter("sTime");
		String etime = request.getParameter("eTime");
		String msg = serviceQualityService.getComplaintEnquiry(request,stime,etime);
		return msg;
	}
	
	// 投诉订单查询
	@RequestMapping("/getComplaintOrder")
	@ResponseBody
	public String ComplaintOrder(HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		String driverName = request.getParameter("driverName");
		String vehicleNo = request.getParameter("vehicleNo");
		String sTime = request.getParameter("sTime");
		String eTime = request.getParameter("eTime");
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String msg = serviceQualityService.getComplaintOrder(request,orderId,driverName,vehicleNo,sTime,eTime,page,pageSize);
		return msg;
		
	}
	
}
