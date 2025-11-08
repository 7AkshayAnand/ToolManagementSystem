package com.toolmanagementsystem.demo.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        List<String> allowedRoutes = List.of("/v3/api-docs", "/actuator");

        boolean isAllowed = allowedRoutes
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));




        if(body instanceof ApiResponse<?>|| isAllowed) {
            log.error("error hapen inside body wala code "+body.getClass().getName());
            return body;
        }
       log.error("error hapen outside wala code "+body.getClass().getName());
        // 1. Do not wrap raw bytes
        if (body instanceof byte[]) {
            return body;
        }

// 2. Do not wrap ResponseEntity<byte[]>
        if (body instanceof org.springframework.http.ResponseEntity<?> entity) {
            if (entity.getBody() instanceof byte[]) {
                return body;
            }
        }

// 3. Do not wrap resources (InputStreamResource / ByteArrayResource)
        if (body instanceof org.springframework.core.io.Resource) {
            return body;
        }

// 4. Do not wrap file-download headers
        if (response.getHeaders().containsKey("Content-Disposition")) {
            return body;
        }
        return new ApiResponse<>(body);
    }
}
