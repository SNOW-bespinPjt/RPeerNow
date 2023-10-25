package com.example.peernow360.response;

import lombok.Data;
@Data
public class SingleResponse<T> extends CommonResponse {

    private T data;
}
