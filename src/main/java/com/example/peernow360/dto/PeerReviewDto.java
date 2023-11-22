package com.example.peernow360.dto;

import lombok.Data;

@Data
public class PeerReviewDto {

    private int no;
    private String user_id;
    private String peer_id;
    private String peer_name;
    private Object peer_image;
    private String peer_team;
    private String peer_role;
    private int score;

    }
