package com.example.demo.user;

import com.example.demo.auth.Authorization;
import com.example.demo.responseHandler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        User createdUser;
        try{
            createdUser = userService.createUser(user);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("User is created successfully.", HttpStatus.OK, createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        User user = userService.getUserById(id);
        return ResponseHandler.generateResponse("User is getted.", HttpStatus.OK, user);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader HttpHeaders headers) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        List<User> users = userService.getAllUsers();
        return ResponseHandler.generateResponse("Users are getted.", HttpStatus.OK, users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestHeader HttpHeaders headers, @PathVariable Long id, @RequestBody User user) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseHandler.generateResponse("User is updated.", HttpStatus.OK, updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        userService.deleteUser(id);
        return ResponseHandler.generateResponse("User is deleted.", HttpStatus.OK, null);
    }
}