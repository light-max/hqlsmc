package com.hql.scm.exception;

import com.hql.scm.constant.AssertException;
import com.hql.scm.constant.GlobalConstant;
import com.hql.scm.fieldcheck.FieldCheckException;
import com.hql.scm.model.data.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 是否打印断言异常
     */
//    @Value("${mobileoffice.global-exception.assert-exception}")
    private Boolean assertException = true;

    /**
     * 处理断言异常
     */
    @ExceptionHandler(AssertException.class)
    @ResponseBody
    public Result<Object> handlerAssertException(AssertException e) {
        if (assertException) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(FieldCheckException.class)
    @ResponseBody
    public Result<Object> handlerFieldException(FieldCheckException e) {
        if (assertException) {
            System.out.println(e.getMessage());
        }
        return Result.error(e.getMessage());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handlerException(Exception e) {
        e.printStackTrace();
        return Result.error(GlobalConstant.error.getMessage() + ": " + e.getMessage());
    }
}