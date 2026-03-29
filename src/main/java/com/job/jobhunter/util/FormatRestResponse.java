package com.job.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.job.jobhunter.domain.RestResponse;
import com.job.jobhunter.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // 1. Trả về trực tiếp nếu là lỗi hoặc body là kiểu String/Resource
        if (status >= 400 || body instanceof String || body instanceof Resource) {
            return body;
        }

        // 2. Xử lý lỗi ép kiểu khi method khai báo trả về String/Resource nhưng giá trị thực tế lại bị null
        if (String.class.equals(returnType.getParameterType()) ||
                Resource.class.isAssignableFrom(returnType.getParameterType())) {
            return body;
        }

        // 3. Tránh trường hợp Response bị bọc 2 lần
        if (body instanceof RestResponse) {
            return body;
        }

        // 4. Các trường hợp Success khác: Khởi tạo response chuẩn
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        res.setMessage(apiMessage != null ? apiMessage.value() : "call api success");

        // Set data (cho phép body null đối với các endpoint trả về void hoặc empty)
        res.setData(body);

        return res;
    }
}