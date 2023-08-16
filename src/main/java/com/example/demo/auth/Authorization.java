package com.example.demo.auth;

import com.example.demo.Util.Util;
import com.example.demo.responseHandler.ResponseHandler;
import com.example.demo.vehicle.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class Authorization {

    public static ResponseEntity<Object> checkAuthorization(String jwtToken) {
        try{
            Util.setCurrentUser(jwtToken);
        }catch(Exception e) {
            //e.printStackTrace();
            return ResponseHandler.generateResponse("You are not authorized.", HttpStatus.UNAUTHORIZED, null);
        }
        return ResponseHandler.generateResponse("Authorized.", HttpStatus.OK, null);
    }

    public static ResponseEntity<Object> checkCompany(Long vehicleCompanyId) {
        Long userCompanyId = Util.getCurrentUser().getCompanyId();
        if(Objects.equals(userCompanyId, vehicleCompanyId)) {
            return ResponseHandler.generateResponse("Same Company.", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("You are not authorized for this vehicle.", HttpStatus.UNAUTHORIZED, null);
        }
    }
}
