package com.ecommerce.productservice.exception.util;

import com.ecommerce.common.exception.ApiResponse;
import com.ecommerce.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionUtil.class);
    private ExceptionUtil(){}
    public static void handleException(FeignException.FeignClientException e){
        if (e.status() >= 400 && e.status() < 500) {
            String responseBody = e.contentUTF8();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);
                LOG.info("Error response received from user service: {}", apiResponse.getMessage());
                throw new BusinessException(apiResponse);
            } catch (JsonProcessingException ex) {
                LOG.error("Error parsing JSON response from user service", ex);
                throw new BusinessException("Error parsing JSON response from user service");
            }
        } else {
            LOG.error("Internal server error while calling user service", e);
            throw new BusinessException("Internal server error while calling user service");
        }
    }
}
