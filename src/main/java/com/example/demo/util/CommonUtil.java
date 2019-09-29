package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static <T> ResponseEntity<T> createResponseEntity(T data, String responseMessage) {
        checkEmptyResponseData(data);
        log.info("Operation completed. {}", responseMessage);
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity createVoidResultResponse(String responseMessage) {
        log.info("Operation completed. {}", responseMessage);
        return ResponseEntity.ok(null);
    }

    private static <T> void checkEmptyResponseData(T data) {
        if (isEmpty(data)) {
            String msg = "Received empty response from service.";
            log.info(msg);
            throw new IllegalStateException(msg);
        }
    }

    private static <T> boolean isEmpty(T data) {
        return data == null || (data instanceof Collection && CollectionUtils.isEmpty((Collection) data));
    }
}
