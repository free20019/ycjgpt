package mvc.controllers;

import mvc.service.DynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: xianlehuang
 * @Description:动态数据
 * @date: 2020/7/27 - 11:24
 */
@Controller
@RequestMapping(value = "/dynamicData")
public class DynamicDataAction {

    @Autowired
    DynamicDataService dynamicDataService;

    //订单发起表
    @RequestMapping("/findOrderCreate")
    @ResponseBody
    public String findOrderCreate(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOrderCreate(request, postData);
        return msg;
    }
    //订单成功表
    @RequestMapping("/findOrderMatch")
    @ResponseBody
    public String findOrderMatch(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOrderMatch(request, postData);
        return msg;
    }
    //订单撤销表
    @RequestMapping("/findOrderCancel")
    @ResponseBody
    public String findOrderCancel(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOrderCancel(request, postData);
        return msg;
    }
    //车辆
    @RequestMapping("/findVehicle")
    @ResponseBody
    public String findVehicle(HttpServletRequest request){
        String msg = dynamicDataService.findVehicle(request);
        return msg;
    }

    //车辆经营上线表
    @RequestMapping("/findOperateLogin")
    @ResponseBody
    public String findOperateLogin(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOperateLogin(request, postData);
        return msg;
    }
    //车辆经营下线表
    @RequestMapping("/findOperateLogout")
    @ResponseBody
    public String findOperateLogout(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOperateLogout(request, postData);
        return msg;
    }
    //经营出发表
    @RequestMapping("/findOperateDepart")
    @ResponseBody
    public String findOperateDepart(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOperateDepart(request, postData);
        return msg;
    }
    //经营到达表
    @RequestMapping("/findOperateArrive")
    @ResponseBody
    public String findOperateArrive(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOperateArrive(request, postData);
        return msg;
    }
    //经营支付表
    @RequestMapping("/findOperatePay")
    @ResponseBody
    public String findOperatePay(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findOperatePay(request, postData);
        return msg;
    }
    //乘客评价信息表
    @RequestMapping("/findRatedPassenger")
    @ResponseBody
    public String findRatedPassenger(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findRatedPassenger(request, postData);
        return msg;
    }
    //乘客投诉信息表
    @RequestMapping("/findRatedPassengerComplaint")
    @ResponseBody
    public String findRatedPassengerComplaint(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findRatedPassengerComplaint(request, postData);
        return msg;
    }
    //驾驶员处罚信息表
    @RequestMapping("/findProvDriverPunish")
    @ResponseBody
    public String findProvDriverPunish(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findProvDriverPunish(request, postData);
        return msg;
    }
    //驾驶员信誉表
    @RequestMapping("/findRatedDriver")
    @ResponseBody
    public String findRatedDriver(HttpServletRequest request, String postData){
        String msg = dynamicDataService.findRatedDriver(request, postData);
        return msg;
    }
}
