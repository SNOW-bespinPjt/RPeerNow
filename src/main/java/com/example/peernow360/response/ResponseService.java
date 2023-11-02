package com.example.peernow360.response;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class ResponseService {

    //단일 데이터 처리
    public<T> SingleResponse<T> getSingleResponse(T data) {

        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setData(data);
        if (data == null) {
            setFailResponse(singleResponse);;
        } else {
            setSuccessResponse(singleResponse);
        }

        return singleResponse;
    }

    //복수 데이터 처리
    public<T> ListResponse<T> getListResponse(List<T> dataList) {

        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setDatalist(dataList);
        setSuccessResponse(listResponse);

        return listResponse;
    }

    // 맵 데이터 처리
    public<K, V> MapResponse<K, V> getMapResponse(Map<K, V> dataMap) {

        MapResponse<K, V> mapResponse = new MapResponse<>();
        mapResponse.setDataMap(dataMap);
        setSuccessResponse(mapResponse);

        if (dataMap.isEmpty()) {
            setFailResponse(mapResponse);;
        } else {
            setSuccessResponse(mapResponse);
        }

        return mapResponse;

    }

    //성공 결과만 처리
    public CommonResponse getSuccessResult() {

        CommonResponse commonResponse = new CommonResponse();
        setSuccessResponse(commonResponse);
        return commonResponse;
    }

    //실패 결과만 처리
    public CommonResponse getFailResult() {

        CommonResponse commonResponse = new CommonResponse();
        setFailResponse(commonResponse);
        return commonResponse;
    }

    //api 성공시 성공 데이터
    private void setSuccessResponse(CommonResponse response) {

        response.setSuccess(true);
        response.setCode(CommonResult.SUCCESS.getCode());
        response.setMessage(CommonResult.SUCCESS.getMessage());
    }

//    //api 성공했으나 데이터가 없음
//    private void setDataResponse(CommonResponse response) {
//
//        response.setSuccess(true);
//        response.setCode(CommonResult.NULL.getCode());
//        response.setMessage(CommonResult.NULL.getMessage());
//    }

    //api 실패시 실패 데이터
    private void setFailResponse(CommonResponse response) {

        response.setSuccess(false);
        response.setCode(CommonResult.FAIL.getCode());
        response.setMessage(CommonResult.FAIL.getMessage());
    }


}
