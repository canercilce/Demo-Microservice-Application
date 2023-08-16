package com.example.demo.group;

import com.example.demo.Tree.Tree;
import com.example.demo.auth.Authorization;
import com.example.demo.responseHandler.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestHeader HttpHeaders headers, @RequestBody Group group) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Group createdGroup;
        try{
            createdGroup = groupService.createGroup(group);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("Group is created successfully.", HttpStatus.OK, createdGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGroupById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Group group = groupService.getGroupById(id);
        return ResponseHandler.generateResponse("Group is getted.", HttpStatus.OK, group);
    }

    @GetMapping
    public ResponseEntity<Object> getAllGroups(@RequestHeader HttpHeaders headers) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        List<Group> groups = groupService.getAllGroups();
        return ResponseHandler.generateResponse("Groups are getted.", HttpStatus.OK, groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGroup(@RequestHeader HttpHeaders headers, @PathVariable Long id, @RequestBody Group group) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        group.setId(id);
        Group updatedGroup = groupService.updateGroup(group);
        return ResponseHandler.generateResponse("Group is updated.", HttpStatus.OK, updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Group group = groupService.getGroupById(id);
        if(group == null) {
            return ResponseHandler.generateResponse("No group with this id.", HttpStatus.OK, null);
        }
        groupService.deleteGroup(id);
        return ResponseHandler.generateResponse("Group is deleted.", HttpStatus.OK, null);
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<Object> addVehicleToGroup(@RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> request) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Long vehicleId = Long.parseLong(request.get("vehicleId").toString());
        Long groupId = Long.parseLong(request.get("groupId").toString());
        try{
            groupService.addVehicleToGroup(vehicleId, groupId);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("Vehicle is added to group successfully.", HttpStatus.OK, null);
    }

    @GetMapping("/getAsTree")
    public ResponseEntity<Object> getAuthorizedGroupsAsTree(@RequestHeader HttpHeaders headers) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        Tree responseData;
        try{
            responseData = groupService.getAuthorizedGroupsAsTree();
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("Authorized groups are getted.", HttpStatus.OK, responseData);
    }

    @GetMapping("/getAsList")
    public ResponseEntity<Object> getAuthorizedVehicles(@RequestHeader HttpHeaders headers) {
        String jwtToken = (headers.get("X-User")!=null) ? headers.get("X-User").get(0).toString() : null;
        ResponseEntity<Object> res = Authorization.checkAuthorization(jwtToken);
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return res;
        }
        ArrayList<Tree.Node> responseData;
        try{
            responseData = groupService.getAuthorizedVehicles();
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return ResponseHandler.generateResponse("Authorized groups are getted.", HttpStatus.OK, responseData);
    }
}
