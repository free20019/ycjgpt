package mvc.controllers;



import mvc.service.CommonServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/common")
public class CommonAction {
	private CommonServer commonService;

	public CommonServer getCommonServer() {
		return commonService;
	}

	@Autowired
	public void setCommonServer(CommonServer commonService) {
		this.commonService = commonService;
	}
}
