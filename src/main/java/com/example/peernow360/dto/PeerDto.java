package com.example.peernow360.dto;

import lombok.Data;

import java.util.List;

@Data
public class PeerDto {

    private int no;
    private String user_id;
    private int avg;
    private String best;
    private List<String> peer_id;
}
