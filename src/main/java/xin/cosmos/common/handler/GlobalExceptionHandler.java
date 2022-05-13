package xin.cosmos.common.handler;

import xin.cosmos.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return R.failed(false, e.getMessage());
    }
}
