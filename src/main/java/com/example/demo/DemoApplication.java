package com.example.demo;

import com.example.demo.JwtHelper.JwtHelper;
import com.example.demo.Util.Util;
import com.example.demo.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        String jwtToken = JwtHelper.encodeJwt("{\"userId\":1547, \"name\":\"Caner\", \"surname\":\"Cilce\", \"companyId\":5879, \"companyName\":\"Mülakat A.Ş.\", \"role\":\"CompanyAdmin\", \"authorizations\": \"['V7', '7']\"}");
        System.out.println(jwtToken);
        Util.setCurrentUser(jwtToken);
        User user = Util.getCurrentUser();
        System.out.println(user.getName());
    }
}
