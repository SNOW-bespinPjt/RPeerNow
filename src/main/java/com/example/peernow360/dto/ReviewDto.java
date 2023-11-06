package com.example.peernow360.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {

    private int no;
    private String user_id;
    private String peer_id;
    private int project_no;
    private String comment1;
    private String comment2;
    private int score1;
    private int score2;
    private int score3;
    private int score4;
    private int score5;
    private String reg_date;
    private int total;
    private int avg;
}
