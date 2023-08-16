package com.example.demo.vehicle;

import com.example.demo.Util.Util;
import com.example.demo.auth.Authorization;
import com.example.demo.responseHandler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Object> createVehicle(@RequestHeader HttpHeaders headers, @RequestBody Vehicle vehicle) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        String userRole = Util.getCurrentUser().getRole();
        if (!Objects.equals(userRole, "CompanyAdmin")) { //It can added by only Company Admins.
            return ResponseHandler.generateResponse("You are not Company Admin.", HttpStatus.UNAUTHORIZED, null);
        }
        res = Authorization.checkCompany(vehicle.getCompanyId());
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Vehicle createdVehicle;
        try{
            createdVehicle = vehicleService.createVehicle(vehicle);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("Vehicle is created successfully.", HttpStatus.OK, createdVehicle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVehicleById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseHandler.generateResponse("Vehicle is getted.", HttpStatus.OK, vehicle);
    }

    @GetMapping
    public ResponseEntity<Object> getAllVehicles(@RequestHeader HttpHeaders headers) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseHandler.generateResponse("Vehicles are getted.", HttpStatus.OK, vehicles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVehicle(@RequestHeader HttpHeaders headers, @PathVariable Long id, @RequestBody Vehicle vehicle) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        String userRole = Util.getCurrentUser().getRole();
        if (!Objects.equals(userRole, "CompanyAdmin")) { //It can updated by only Company Admins.
            return ResponseHandler.generateResponse("You are not Company Admin.", HttpStatus.UNAUTHORIZED, null);
        }
        res = Authorization.checkCompany(vehicle.getCompanyId());
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        vehicle.setId(id);
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicle);
        return ResponseHandler.generateResponse("Vehicle is updated.", HttpStatus.OK, updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        String userRole = Util.getCurrentUser().getRole();
        if (!Objects.equals(userRole, "CompanyAdmin")) { //It can deleted by only Company Admins.
            return ResponseHandler.generateResponse("You are not Company Admin.", HttpStatus.UNAUTHORIZED, null);
        }
        Vehicle vehicle = vehicleService.getVehicleById(id);
        if(vehicle == null) {
            return ResponseHandler.generateResponse("No vehicle with this id.", HttpStatus.OK, null);
        }
        res = Authorization.checkCompany(vehicle.getCompanyId());
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        vehicleService.deleteVehicle(id);
        return ResponseHandler.generateResponse("Vehicle is deleted.", HttpStatus.OK, null);
    }
}
