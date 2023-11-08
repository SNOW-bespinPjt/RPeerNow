package com.example.peernow360.controller;

import com.example.peernow360.dto.PayApproveDto;
import com.example.peernow360.dto.PayCancelDto;
import com.example.peernow360.dto.PayReadyDto;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/pay")
@Tag(name = "pay", description = "결제")
public class PayController {

    private final PayService payService;
    private final ResponseService responseService;

    @GetMapping("/ready")
    @Operation(summary = "결제 요청", description = "결제 요청", tags = {"create"})
    public PayReadyDto readyToKakaoPay() {

        return payService.kakaoPayReady();
    }

    @GetMapping("/success")
    @Operation(summary = "결제 성공", description = "결제 성공", tags = {"create"})
    public SingleResponse<PayApproveDto> afterPayRequest(@RequestParam("pg_token") String pgToken) {

        return responseService.getSingleResponse(payService.approveResponse(pgToken));
    }

    @PostMapping("/refund")
    @Operation(summary = "환불", description = "환불", tags = {"create"})
    public SingleResponse<PayCancelDto> refund() {

        return responseService.getSingleResponse(payService.kakaoCancel());
    }
}
