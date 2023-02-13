package com.example.iot.service;

import com.example.iot.exception.ParamaErrorException;

import com.example.iot.vo.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice()
@ConditionalOnExpression("${env.debug:true}")
@Configuration
public class GlobalExceptionHandler {
//
//    /**
//     * 忽略参数异常处理器
//     *
//     * @param e 忽略参数异常
//     * @return ResponseResult
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseResult parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
//        logger.error("", e);
//        return new ResponseResult(ResultEnum.PARAMETER_ERROR.getCode(), "请求参数 " + e.getParameterName() + " 不能为空");
//    }
//
    /**
     * 缺少请求体异常处理器
     *
     * @param e 缺少请求体异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
        log.info("e:{}", e);
        return ResponseEntity.error(e.getMessage());
    }
//
//    /**
//     * 参数效验异常处理器
//     *
//     * @param e 参数验证异常
//     * @return ResponseInfo
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseResult parameterExceptionHandler(MethodArgumentNotValidException e) {
//        logger.error("", e);
//        // 获取异常信息
//        BindingResult exceptions = e.getBindingResult();
//        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
//        if (exceptions.hasErrors()) {
//            List<ObjectError> errors = exceptions.getAllErrors();
//            if (!errors.isEmpty()) {
//                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
//                FieldError fieldError = (FieldError) errors.get(0);
//                return new ResponseResult(ResultEnum.PARAMETER_ERROR.getCode(), fieldError.getDefaultMessage());
//            }
//        }
//        return new ResponseResult(ResultEnum.PARAMETER_ERROR);
//    }

    /**
     * 自定义参数错误异常处理器
     *
     * @param e 自定义参数
     * @return ResponseInfo
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ParamaErrorException.class})
    public ResponseEntity paramExceptionHandler(ParamaErrorException e) {
        log.info("error:{}", e);
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息

        return ResponseEntity.error(e.getMessage());
    }

    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public ResponseEntity otherExceptionHandler(Exception e) {
        log.info("error:{}", e);
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息

        return ResponseEntity.error(e.getMessage());
    }
}
