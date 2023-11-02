package com.example.peernow360.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MapResponse<K, V> extends CommonResponse {

    private Map<K, V> dataMap;

}
