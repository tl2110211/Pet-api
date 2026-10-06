package com.example.petapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理器
 *
 * @RestControllerAdvice 的作用：
 * - 监听所有 Controller 抛出的异常
 * - 统一处理，返回标准格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     *
     * 当 @Valid 校验失败时，Spring 抛出 MethodArgumentNotValidException
     * 我们捕获它，转换成友好的错误格式
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        // 步骤 1：从异常中获取 Spring 的错误列表
        List<org.springframework.validation.FieldError> springErrors =
            ex.getBindingResult().getFieldErrors();

        // 步骤 2：创建一个空列表，用来存放我们自己的 FieldError
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();

        // 步骤 3：遍历 Spring 的错误列表
        for (org.springframework.validation.FieldError springError : springErrors) {
            // 步骤 4：从 Spring 的 FieldError 提取信息
            String fieldName = springError.getField();              // "name"
            String errorMessage = springError.getDefaultMessage();  // "不能为空"

            // 步骤 5：创建我们自己的 FieldError
            ErrorResponse.FieldError ourError =
                new ErrorResponse.FieldError(fieldName, errorMessage);

            // 步骤 6：加入列表
            fieldErrors.add(ourError);
        }

        // 步骤 7：构建统一错误响应
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),  // 400
                "Validation Failed",             // 错误类型
                "参数校验失败",                   // 错误消息
                fieldErrors                      // 字段错误列表
        );

        // 步骤 8：返回 400 响应
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
