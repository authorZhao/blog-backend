package com.git.blog.commmon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author authorZhao
 * @since 2022-03-01
 */
@Data
@Accessors(chain = true)
public class ApiResponse<T> {

    public static final String SUCCESS_MSG = "处理成功";
    public static final String FAILED_MSG = "处理失败";
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer FAILED_CODE = 500;


    private Integer code;

    private String msg;

    private T data;


    public static <T> ApiResponse<T> ok(T data){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(SUCCESS_CODE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> ok(){
        return ok(null);
    }

    public static <T> ApiResponse<T> error(String msg){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(FAILED_CODE);
        apiResponse.setMsg(StringUtils.isNotBlank(msg)?msg:FAILED_MSG);
        return apiResponse;
    }

    public static <T> ApiResponse<T> error(){
        return error(null);
    }




    public static boolean isSuccess(ApiResponse apiResponse){
        return apiResponse!=null && apiResponse.getCode()==0;
    }

    public static boolean isNotNull(ApiResponse apiResponse){
        return isSuccess(apiResponse) && apiResponse.getData()!=null;
    }

}
