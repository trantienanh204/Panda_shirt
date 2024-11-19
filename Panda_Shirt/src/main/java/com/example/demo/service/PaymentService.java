package com.example.demo.service;

import com.example.demo.entity.PaymentStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class PaymentService {

    private final RestTemplate restTemplate;

    @Value("${tpbank.api.url}")
    private String tpbankApiUrl;

    @Value("${tpbank.api.partnerCode}")
    private String tpbankPartnerCode;

    @Value("${tpbank.api.accessKey}")
    private String tpbankAccessKey;

    @Value("${tpbank.api.secretKey}")
    private String tpbankSecretKey;

    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentStatusResponse checkPaymentStatus(String orderId) {
        String url = UriComponentsBuilder.fromHttpUrl(tpbankApiUrl)
                .path("/query")
                .queryParam("partnerCode", tpbankPartnerCode)
                .queryParam("accessKey", tpbankAccessKey)
                .queryParam("orderId", orderId)
                .queryParam("requestId", orderId) // requestId có thể là orderId hoặc giá trị duy nhất
                .queryParam("secretKey", tpbankSecretKey)
                .toUriString();

        return restTemplate.getForObject(url, PaymentStatusResponse.class);
    }
}
