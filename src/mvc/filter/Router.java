package mvc.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class Router implements Filter {

   Logger logger = Logger.getLogger(this.getClass());


   @Override
   public void destroy() {
   }

   @Override
   public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest)arg0;
      HttpServletResponse response = (HttpServletResponse)arg1;
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      String user = String.valueOf(request.getSession().getAttribute("username"));
      String contextPath = request.getRequestURI().toString();
      String origin = request.getHeader("Origin");
      response.setHeader("Access-Control-Allow-Origin", origin == null?"*":origin);
      response.setHeader("Access-Control-Allow-Credentials", "true");
      String method = request.getMethod();
      if(method.toUpperCase().equals("OPTIONS")) {
         response.setHeader("Access-Control-Allow-Origin", "*");
         response.setHeader("Access-Control-Allow-Credentials", "true");
      }

      System.out.println(contextPath);
//      if(!"".equals(user)){// 用户没有登录
//    	  if("/ycjgpt/".equals(contextPath)){
////    		  response.sendRedirect("http://192.168.11.238:8080/ycjgpt/index.html#/login");
////    		  response.sendRedirect("http://172.18.30.1:9090/ycjgpt/index.html#/login");
//    		  return;
//    	  }
//      }

      arg2.doFilter(arg0, arg1);


//      if(!"".equals(user)){// 用户没有登录
//    	  if(contextPath.indexOf("index.html") > 0 || contextPath.indexOf("getPatchca") > 0 || contextPath.indexOf("makeSurePatchca") > 0 || contextPath.indexOf("login") > 0 || contextPath.indexOf("makerSuerLogin") > 0){// 当用户请求的是登录页面时不进行拦截
//    		  arg2.doFilter(arg0, arg1);
//    	  }
//    	  else if(contextPath.indexOf("js") > 0 || contextPath.indexOf("css") > 0 || contextPath.indexOf("json") > 0 || contextPath.indexOf("img") > 0 || contextPath.indexOf("fonts") > 0 || contextPath.indexOf("favicon.ico") > 0){
//    		  arg2.doFilter(arg0, arg1);
//    	  }
//    	  else{// 当用户请求的地址不为登录页面时,进行拦截
//    		  // 当请求地址是/ycjgpt/index.html#/login时,后台拦截到的地址为/ycjgpt/index.html,这样无法区分用户访问的是/ycjgpt/index.html还是/ycjgpt/index.html#/login
//    		  response.sendRedirect("http://172.18.30.1:9090/ycjgpt/index.html#/login");
////    		  response.sendRedirect("http://localhost:8080/ycjgpt/index.html#/login");
//    		  return ;
//    	  }
//      }else{
//    	  arg2.doFilter(arg0, arg1);
//      }
   }

   @Override
   public void init(FilterConfig arg0) throws ServletException {
   }
}