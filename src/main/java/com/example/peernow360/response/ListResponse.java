package com.example.peernow360.response;

import lombok.Data;

import java.util.List;
@Data
public class ListResponse<T> extends CommonResponse {

    private List<T> datalist;
}
