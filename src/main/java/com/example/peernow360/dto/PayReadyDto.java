package com.example.peernow360.dto;

import lombok.Data;

@Data
public class PayReadyDto {

    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url; // 결제 페이지 url
    private String created_at;

}
