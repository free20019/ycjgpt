package mvc.controllers;

import mvc.service.StaticDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: xianlehuang
 * @Description:静态数据
 * @date: 2020/7/27 - 11:23
 */

@Controller
@RequestMapping(value = "/staticData")
public class StaticDataAction {

    @Autowired
    StaticDataService staticDataService;

    //乘客基本信息
    @RequestMapping("/findPassengerBaseInfo")
    @ResponseBody
    public String findPassengerBaseInfo(HttpServletRequest request, String postData){
        String msg = staticDataService.findPassengerBaseInfo(request, postData);
        return msg;
    }

}
