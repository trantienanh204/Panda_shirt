package com.example.demo.Controller.admin.BanHang;

import com.example.demo.entity.DiaChi;
import com.example.demo.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DiaChiController {
    private RestTemplate restTemplate = new RestTemplate();

//    @Autowired
//    DiaChiService diaChiService;
//    public DiaChiController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @GetMapping("/provinces")
//    public String getAllProvinces() {
//        return diaChiService.getHuyen(String.valueOf(1));
//    }
//
//
//    @GetMapping("/districts")
//    public ResponseEntity<List<DiaChi>> getDistricts(@RequestParam("provinceId") int provinceId) {
//        try {
//            String url = "https://vietnamprovinces.info/api/provinces" + provinceId + "/districts";
//
//            ResponseEntity<DiaChi[]> response = restTemplate.getForEntity(url, DiaChi[].class);
//
//            List<DiaChi> districts = Arrays.asList(response.getBody());
//
//            return ResponseEntity.ok(districts);
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//        }
//    }
//
//
//    @GetMapping("/getWards")
//    public String getWards(@RequestParam("districtId") int districtId) {
//        String url = "https://provinces.open-api.vn/api/d/" + districtId + "/wards";
//        return restTemplate.getForObject(url, String.class);
//    }
}
