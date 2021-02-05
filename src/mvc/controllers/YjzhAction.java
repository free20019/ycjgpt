package mvc.controllers;

import mvc.service.YjzhServics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 应急指挥处置系统
 * @author xianlehuang
 * @date 2018/1/9 
 */
@RequestMapping("/Yjzh")
@RestController
public class YjzhAction {

    @Autowired
    private YjzhServics Yjzhservics;

	/**
	 * 下拉栏
	 * @param request
	 * @param table
	 * @param field
	 * @return
	 */
	@RequestMapping("/xll")
	@ResponseBody
	public String findxll(HttpServletRequest request,@RequestParam("table") String table,@RequestParam("field") String field) {
		String msg = Yjzhservics.findxll(table,field);
		return msg;
	}
	
	/**
	 * 值班
	 * @return
	 */
	@RequestMapping("/findzbb")
	@ResponseBody
	public String findzbb(HttpServletRequest request) {
		String msg = Yjzhservics.findzbb();
		return msg;
	}
	/**
	 * 应急事件接入
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fingyjsjjr")
	@ResponseBody
	public String fingyjsjjr(@RequestParam("sjzt") String sjzt) {
		String msg=Yjzhservics.fingyjsjjr(sjzt);
		return msg;
	}	
	/**
	 * 应急事件接入保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/jrsave")
	@ResponseBody
	public Integer jrsave(HttpServletRequest request) {
		String sjbh = request.getParameter("sjbh");
		String sjzt = request.getParameter("sjzt");
		String fsdz = request.getParameter("fsdz");
		String jwdxx = request.getParameter("jwdxx");
		String bjr = request.getParameter("bjr");
		String bjdh = request.getParameter("bjdh");
		String jjr = request.getParameter("jjr");
		String sjjl = request.getParameter("sjjl");
		String bjnr = request.getParameter("bjnr");
		String bjfs = request.getParameter("bjfs");
		String sjjb = request.getParameter("sjjb");
		Integer msg =Yjzhservics.jrsave(sjbh,sjzt,fsdz,jwdxx,bjr,bjdh,jjr,sjjl,bjnr,bjfs,sjjb);
		return msg;
	}
	/**
	 * 应急事件接入修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/jrUpdate")
	@ResponseBody
	public Integer jrUpdate(HttpServletRequest request) {
		String sjbh = request.getParameter("sjbh");
		String sjzt = request.getParameter("sjzt");
		String fsdz = request.getParameter("fsdz");
		String jwdxx = request.getParameter("jwdxx");
		String bjr = request.getParameter("bjr");
		String bjdh = request.getParameter("bjdh");
		String jjr = request.getParameter("jjr");
		String sjjl = request.getParameter("sjjl");
		String bjnr = request.getParameter("bjnr");
		String bjfs = request.getParameter("bjfs");
		String sjjb = request.getParameter("sjjb");
		String id = request.getParameter("id");
		Integer msg =Yjzhservics.jrUpdate(sjbh,sjzt,fsdz,jwdxx,bjr,bjdh,jjr,sjjl,bjnr,bjfs,sjjb,id);
		return msg;
	}
	/**
	 * 应急事件接入审核
	 * 
	 * @return
	 */
	@RequestMapping(value = "/jrRzsh")
	@ResponseBody
	public Integer jrRzsh(HttpServletRequest request,
    		@RequestParam("id") String id) {
		Integer msg =Yjzhservics.jrRzsh(id);
		return msg;
	}
	/**
	 * 应急事件接入删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/jrDelete")
	@ResponseBody
	public Integer jrDelete(HttpServletRequest request,
    		@RequestParam("id") String id) {
		Integer msg =Yjzhservics.jrDelete(id);
		return msg;
	}
	/**
	 * 资源库文件名查询
	 * @param request
	 * @param table
	 * @return
	 */
	@RequestMapping("/getAllNames")
	@ResponseBody
	public String getAllNames(HttpServletRequest request,@RequestParam("table") String table) {
		String msg = Yjzhservics.getAllNames(table);
		return msg;
	}
	/**
	 * 资源库文件内容查询
	 * @param request
	 * @param table
	 * @param id
	 * @return
	 */
	@RequestMapping("/getContent")
	@ResponseBody
	public String getContent(HttpServletRequest request,@RequestParam("table") String table,@RequestParam("id") String id) {
		String msg = Yjzhservics.getContent(table,id);
		return msg;
	}
	/**
	 * 资源库文件添加
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveContent")
	@ResponseBody
	public void saveContent(HttpServletRequest request) {
		Yjzhservics.saveContent(request);
	}
	/**
	 * 资源库文件修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/editContent")
	@ResponseBody
	public void editContent(HttpServletRequest request) {
		Yjzhservics.editContent(request);
	}
	
	/**
	 * 查车辆
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping("/findclsj")
	@ResponseBody
	public String findclsj(HttpServletRequest request,@RequestParam("info") String info) {
		String msg = Yjzhservics.findclsj(info);
		return msg;
	}
	/**
	 * 查事件
	 * @param request
	 * @return
	 */
	@RequestMapping("/findjtsj")
	@ResponseBody
	public String findjtsj(HttpServletRequest request) {
		String sjbh = request.getParameter("sjbh");
		String msg = Yjzhservics.findjtsj(sjbh);
		return msg;
	}
}

