package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static <T> ResponseEntity<T> createResponseEntity(T data, String responseMessage) {
        checkEmptyResponseData(data);
        printResponseMessage(data, responseMessage);
        return ResponseEntity.ok(data);
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

    private static <T> void printResponseMessage(T data, String responseMessage) {
        if ((data instanceof List && !CollectionUtils.isEmpty((List) data))) {
            log.info("{} entities have been loaded. {}", ((List) data).size(), responseMessage);
        } else {
            log.info("Operation completed. {}", responseMessage);
        }
    }
}
