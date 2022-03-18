package com.git.blog.api.config;

import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.ApiResult;
import com.git.blog.exception.ApiUnauthorizedException;
import com.git.blog.exception.BizException;
import com.git.blog.exception.ErrorException;
import com.git.blog.exception.SqlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2021-01-20
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    public ApiResponse bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！,请求url：{}，原因是：",req.getRequestURL(),e);
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse errorExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！,请求url：{}，原因是：",req.getRequestURL(),e);
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = SqlException.class)
    public ApiResponse sqlExceptionHandler(HttpServletRequest req, SqlException e){
        log.warn("发生业务异常！,请求url：{}，原因是：",req.getRequestURL(),e);
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = ApiUnauthorizedException.class)
    public ApiResponse apiExceptionHandler(HttpServletRequest req, ApiUnauthorizedException e){
        log.warn("发生业务异常！,请求url：{}，原因是：",req.getRequestURL(),e);
        return ApiResponse.error(e.getMessage());
    }



    @ExceptionHandler(value = Exception.class)
    public ApiResponse exceptionHandler(HttpServletRequest req, Exception e){
        log.warn("发生业务异常！,请求url：{}，原因是：",req.getRequestURI(),e);
        String errorMsg = "系统异常，请联系管理员或者稍后重试";
        if(e instanceof BindException){
            errorMsg = ((BindException) e).getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        }else if(e instanceof MethodArgumentNotValidException){
            errorMsg = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        }else if(e instanceof DuplicateKeyException){
            errorMsg = "数据重复异常";
        }

        return ApiResponse.error(errorMsg);
    }

    @ExceptionHandler(value = Throwable.class)
    public ApiResponse throwableHandler(HttpServletRequest req, Throwable e){
        log.warn("发生业务异常！,请求url：{}，原因是：",req.getRequestURI(),e);
        return ApiResponse.error("系统异常，请联系管理员或者稍后重试");
    }




}
