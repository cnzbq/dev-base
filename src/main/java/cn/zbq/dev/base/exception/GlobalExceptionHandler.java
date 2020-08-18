package cn.zbq.dev.base.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理
 *
 * @author zbq
 * @date 2020/8/14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public R<String> apiExceptionHandler(ApiException e) {
        IErrorCode errorCode = e.getErrorCode();
        if (Objects.nonNull(errorCode)) {
            return R.failed(errorCode);
        }
        return R.failed(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> exception(Exception e) {
        return R.failed(e.getMessage());
    }
}
