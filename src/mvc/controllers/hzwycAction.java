package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import mvc.service.HzwycService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hzwyc")
public class hzwycAction {
	private HzwycService hzwycService;
	private DownloadAct downloadAct = new DownloadAct();
	public HzwycService getHzwycService() {
		return hzwycService;
	}

	@Autowired
	public void setHzwycService(HzwycService hzwycService) {
		this.hzwycService = hzwycService;
	}
	
	/**
	 * 查询今日在线 今日订单
	 */
	@RequestMapping("/findHomePage")
	@ResponseBody
	public String findHomePage(HttpServletRequest request) {
		String msg = hzwycService.findHomePage();
		return msg;
	}
	/**
	 * 查询 当前在线 实载率等
	 */
	@RequestMapping("/findTj")
	@ResponseBody
	public String findTj(HttpServletRequest request) {
		String msg = hzwycService.findTj();
		return msg;
	}
	/**
	 * 今日上线
	 */
	@RequestMapping("/findjrsx")
	@ResponseBody
	public String findjrsx(HttpServletRequest request) {
		String msg = hzwycService.findjrsx();
		return msg;
	}
	/**
	 * 今日订单
	 */
	@RequestMapping("/findjrdd")
	@ResponseBody
	public String findjrdd(HttpServletRequest request) {
		String msg = hzwycService.findjrdd();
		return msg;
	}
	/**
	 * 查询驾驶员许可量
	 */
	@RequestMapping("/findjsy")
	@ResponseBody
	public String findjsy(HttpServletRequest request) {
		String msg = hzwycService.findjsy();
		return msg;
	}
	/**
	 * 查询车辆许可量
	 */
	@RequestMapping("/findcl")
	@ResponseBody
	public String findcl(HttpServletRequest request) {
		String msg = hzwycService.findcl();
		return msg;
	}
	/**
	 * 查询月度订单
	 */
	@RequestMapping("/finddd")
	@ResponseBody
	public String finddd(HttpServletRequest request) {
		String msg = hzwycService.finddd();
		return msg;
	}
}
