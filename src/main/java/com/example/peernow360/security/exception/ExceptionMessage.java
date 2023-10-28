package com.example.peernow360.security.exception;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ExceptionMessage {

    public static void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException {
        Map<String, Object> errMsg = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8");
        if (result) {
            response.setStatus(HttpServletResponse.SC_OK);
            errMsg.put("success", true);
            errMsg.put("code", 1);
            errMsg.put("message", message);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errMsg.put("success", false);
            errMsg.put("code", -1);
            errMsg.put("message", message);
        }
        response.getWriter().print(errMsg);
    }

}
