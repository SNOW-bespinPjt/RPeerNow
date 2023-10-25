package com.example.peernow360.response;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {


    //단일 데이터 처리
    public<T> SingleResponse<T> getSingleResponse(T data) {

        SingleResponse<T> singleResponse = new SingleResponse<T>();
        singleResponse.setData(data);
        setSuccessResponse(singleResponse);

        return singleResponse;
    }

    //복수 데이터 처리
    public<T> ListResponse<T> getListResponse(List<T> dataList) {

        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setDatalist(dataList);
        setSuccessResponse(listResponse);

        return listResponse;
    }

    public CommonResponse getSuccessResult() {

        CommonResponse commonResponse = new CommonResponse();
        setSuccessResponse(commonResponse);
        return commonResponse;
    }

    public CommonResponse getFailResult() {

        CommonResponse commonResponse = new CommonResponse();
        setFailResponse(commonResponse);
        return commonResponse;
    }

    private void setSuccessResponse(CommonResponse response) {

        response.success = true;
        response.code = 200;
        response.message = "SUCCESS";
    }

    private void setFailResponse(CommonResponse response) {

        response.success = false;
        response.code = 400;
        response.message = "FAIL";
    }
}
