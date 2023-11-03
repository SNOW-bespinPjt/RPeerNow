package com.example.peernow360.response;

public class ResponseResult {

    public static String result(int result) {
        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }
        return message;
    }
}
