package com.unpassant.unforum.advice;

import com.alibaba.druid.support.json.JSONUtils;
import com.unpassant.unforum.dto.ResultDTO;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response){

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回JSON
            if (e instanceof CustomException) {
                //使用自定义的异常处理方法获取异常信息
                resultDTO =  ResultDTO.errorOf((CustomException)e);
            } else {
                //默认异常处理---当自定义异常里没有处理方法时
                resultDTO = ResultDTO.errorOf(CustomErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtils.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;

        }else {
            //错误页面跳转
            if (e instanceof CustomException) {
                //使用自定义的异常处理方法获取异常信息
                model.addAttribute("message", e.getMessage());
            } else {
                //默认异常处理---当自定义异常里没有处理方法时
                model.addAttribute("message", CustomErrorCode.SYS_ERROR.getMessage());
            }

            return new ModelAndView("error");
        }
    }
}
