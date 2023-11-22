package com.example.peernow360.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PeerDto {

    private int no;
    private String user_id;
    private int avg;
    private String best_id;
    private String best_name;
    private Object best_image;

}
