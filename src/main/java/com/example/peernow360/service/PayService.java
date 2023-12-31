package com.example.peernow360.service;

import com.example.peernow360.dto.PayApproveDto;
import com.example.peernow360.dto.PayCancelDto;
import com.example.peernow360.dto.PayReadyDto;
import com.example.peernow360.mappers.IPayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PayService {

    static final String admin_Key = "0213830ee52de5184410e8146a130a69"; // 공개 조심
    private PayReadyDto payReadyDto;
    private final IPayMapper iPayMapper;

    public PayReadyDto kakaoPayReady(String user_id) {
        log.info("kakaoPayReady()");

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", user_id);
        parameters.add("item_name", "프리미엄 회원");
        parameters.add("quantity", "1");
        parameters.add("total_amount", "1900");
        parameters.add("vat_amount", "100");
        parameters.add("tax_free_amount", "1800");
        parameters.add("approval_url", "http://localhost:8080/api/pay/success"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:8080/pay/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:8080/pay/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        payReadyDto = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                PayReadyDto.class);

        return payReadyDto;
    }

    /**
     * 결제 완료 승인
     */
    public PayApproveDto approveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("tid", payReadyDto.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        PayApproveDto payApproveDto = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                PayApproveDto.class);

//        iPayMapper.payInfo();

        return payApproveDto;
    }

    public PayCancelDto kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        PayCancelDto cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                PayCancelDto.class);

        return cancelResponse;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
