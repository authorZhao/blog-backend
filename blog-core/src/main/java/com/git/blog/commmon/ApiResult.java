package com.git.blog.commmon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kaka
 */
@ApiModel(description = "统一返回结果")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {

    public static String RESULT_SUCCESS = "SUCCESS";
    public static String RESULT_OPT_EXCEPTION = "OPT_EXCEPTION";
    public static String RESULT_VALIDATE_FAIL = "VALIDATE_FAIL";
    public static String RESULT_FAIL = "FAIL";
    public static String RESULT_NO_PERMISSION = "NO_PERMISSION";
    public static String RESULT_TOKEN_FAIL = "TOKEN_FAIL";
    public static String RESULT_FORBIDDEN = "FORBIDDEN";
    public static String RESULT_NOT_SUPPORTED = "HTTP_REQUEST_METHOD_NOT_SUPPORTED";

    public static String RESULT_CONTENT_AUDIT_NOT_PASS = "CONTENT_AUDIT_NOT_PASS";

    //解密 ghId校验失败
    public static String DECRYPTION_CHECK_FAILED = "DECRYPTION_CHECK_FAILED";
    //解密 ghId转义异常
    public static String DECRYPTION_FAILED = "DECRYPTION_FAILED";
    //解密 前端传来long类型的字符串，直接返回1209
    public static String DECRYPTION_1209 = "1209";



    @ApiModelProperty("返回状态 SUCCESS:成功")
    private String result;

    @ApiModelProperty(value = "返回错误信息")
    private String errMsg;

    @ApiModelProperty(value = "兼容返回错误信息")
    private String msg;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty("数据")
    private T data;

    /**
     *
     *
     * @return
     */
    public Integer getCode() {

        if (RESULT_SUCCESS.equals(this.result)) {
            return 200;
        }
        if (RESULT_VALIDATE_FAIL.equals(this.result) || RESULT_CONTENT_AUDIT_NOT_PASS.equals(this.result)) {
            return 400;
        }
        if (DECRYPTION_CHECK_FAILED.equals(this.result)) {
            return 501;
        }
        if (DECRYPTION_FAILED.equals(this.result) ) {
            return 502;
        }
        if (DECRYPTION_1209.equals(this.result) ) {
            return 1209;
        }

        if (RESULT_FORBIDDEN.equals(this.result)){
            return 403;
        }
        if (RESULT_TOKEN_FAIL.equals(this.result) || RESULT_NO_PERMISSION.equals(this.result)) {
            return 401;
        } else {
            return 500;
        }
    }

    public static <E> ApiResult<E> init() {
        return new ApiResult<>();
    }

//    public static <E> Result<E> error(String result, String errMsg) {
//        Result<E> r = new Result<>();
//        r.setErrMsg(errMsg);
//        r.setMsg(errMsg);
//        r.setResult(result);
//        r.setCode(r.getCode());
//        return r;
//    }

//    public static <E> Result<E> error() {
//        return error(Result.RESULT_OPT_EXCEPTION, "服务异常");
//    }

    public ApiResult<T> result(String resultType) {
        this.result = resultType;
        this.code = this.getCode();
        return this;
    }

    public ApiResult<T> errorMsg(String errorMsg) {
        this.errMsg = errorMsg;
        this.msg = errorMsg;
        return this;
    }

    public ApiResult<T> data(T data) {
        this.data = data;
        this.result = ApiResult.RESULT_SUCCESS;
        this.code = this.getCode();
        return this;
    }

    public ApiResult<T> ok() {
        result(ApiResult.RESULT_SUCCESS);
        return this;
    }

    public boolean isOk() {
        return ApiResult.RESULT_SUCCESS.equals(this.result);
    }


}
