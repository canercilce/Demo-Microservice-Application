package com.example.demo.group;

import com.example.demo.Tree.Tree;
import com.example.demo.Util.Util;
import com.example.demo.user.User;
import com.example.demo.vehicle.Vehicle;
import com.example.demo.vehicle.VehicleService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(Group group) throws Exception {
        Long groupId = group.getId()!=null ? group.getId() : -1;
        Group existingGroup = groupRepository.findById(groupId).orElse(null);
        if (existingGroup != null) {
            throw new Exception("Group with ID " + groupId + " already exists.");
        }
        Group savedGroup = groupRepository.save(group);
        Long parentId = group.getParentId()!=null ? group.getParentId() : -1;
        Group parentGroup = groupRepository.findById(parentId).orElse(null);
        if(parentGroup != null) {
            JSONArray parentGroupChildren = new JSONArray(parentGroup.getChildren()==null ? "[]" : parentGroup.getChildren());
            parentGroupChildren.put(savedGroup.getId().toString());
            parentGroup.setChildren(parentGroupChildren.toString());
            groupRepository.save(parentGroup);
        }
        return savedGroup;
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public void addVehicleToGroup(Long vehicleId, Long groupId) throws Exception{
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        Group group = groupRepository.findById(groupId).orElse(null);
        if(vehicle != null && group != null) {
            JSONArray groupChildren = new JSONArray(group.getChildren()==null ? "[]" : group.getChildren());
            groupChildren.put("V"+ vehicleId);
            group.setChildren(groupChildren.toString());
            groupRepository.save(group);
        }else {
            throw new Exception("No group or vehicle with this id.");
        }
    }

    public Tree getWholeTree() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", 0);
        data.put("groupName", "All");
        Tree tree = new Tree(data);
        List<Group> allGroups = this.getAllGroups();
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();
        for(Group g: allGroups) {
            data = new HashMap<>();
            data.put("id", g.getId().toString());
            data.put("groupName", g.getGroupName());
            Tree.Node node = new Tree.Node(data);
            for(Object s : Util.stringToArrayList(g.getChildren())) {
                if(s.toString().startsWith("V")){
                    Vehicle vehicle = (Vehicle) Util.findObjectBy("id", s.toString().substring(1), new ArrayList<>(allVehicles));
                    //Vehicle vehicle = vehicleService.getVehicleById(Long.valueOf(s.toString().substring(1))); //old code
                    Tree.Node node1 = new Tree.Node(vehicle);
                    tree.addChild(node, node1);
                }
            }
            if(g.getParentId() == null) {
                tree.addChild(tree.getRoot(), node);
            }else{
                data = new HashMap<>();
                data.put("id", g.getParentId().toString());
                data.put("groupName", this.getGroupById(g.getParentId()).getGroupName());
                Tree.Node foundNode = tree.search(tree.getRoot(), data);
                if(foundNode != null) {
                    tree.addChild(foundNode, node);
                }
            }
        }
        return tree;
    }

    public Tree getAuthorizedGroupsAsTree() {
        Tree tree = getWholeTree();
        Tree treeCopy = tree.copy();
        User currentUser = Util.getCurrentUser();
        JSONArray authorizations = new JSONArray(currentUser.getAuthorizations()==null ? "[]" : currentUser.getAuthorizations());
        ArrayList<String> authorizedElementIds = new ArrayList<>();
        for(Object o : authorizations) {
            String authorization = o.toString();
            if(!authorization.startsWith("V")) {
                Group group = getGroupById(Long.parseLong(authorization.toString()));
                HashMap<String, Object> tempData = new HashMap<>();
                tempData.put("id", group.getId().toString());
                tempData.put("groupName", group.getGroupName().toString());
                Tree.Node node = tree.search(tree.getRoot(), tempData);
                ArrayList<Tree.Node> subTreeNodes = tree.getSubtree(node, new ArrayList<>());
                for (Tree.Node n : subTreeNodes) {
                    String id;
                    if(Util.objectToJSONObject(n.getData()).has("plate")) { // if it is vehicle
                        id = "V" + Util.objectToJSONObject(n.getData()).get("id").toString();
                    }else {
                        id = Util.objectToJSONObject(n.getData()).get("id").toString();
                    }
                    if(!authorizedElementIds.contains(id)) {
                        authorizedElementIds.add(id);
                    }
                }
            }
            ArrayList<String> parentList = getParentList(authorization.toString());
            for(String s : parentList) {
                if(!authorizedElementIds.contains(s)) {
                    authorizedElementIds.add(s);
                }
            }
        }
        ArrayList<Tree.Node> allNodes = treeCopy.getSubtree(treeCopy.getRoot(), new ArrayList<>());
        for(Tree.Node n: allNodes) {
            String id;
            if(Util.objectToJSONObject(n.getData()).has("plate")) { // if it is vehicle
                id = "V" + Util.objectToJSONObject(n.getData()).get("id").toString();
            }else {
                id = Util.objectToJSONObject(n.getData()).get("id").toString();
            }
            if(!authorizedElementIds.contains(id) && !Objects.equals(id, "0")) {
                tree.removeChild(n);
            }
        }
        return tree;
    }

    public ArrayList<Tree.Node> getAuthorizedVehicles() {
        ArrayList<Tree.Node> result = new ArrayList<>();

        Tree tree = getAuthorizedGroupsAsTree();
        ArrayList<Tree.Node> subtree = tree.getSubtree(tree.getRoot(), new ArrayList<>());
        for(Tree.Node node: subtree) {
            if(node.isLeaf()) {
                result.add(node);
            }
        }
        return result;
    }

    public ArrayList<String> getParentList(String elementId) {
        ArrayList<String> result = new ArrayList<>();
        Group group = null;
        if(elementId.startsWith("V")) {
            result.add(elementId);
            group = getGroupFromVehicleId(elementId);
        }else {
            group = this.getGroupById(Long.parseLong(elementId));
        }
        if(group != null) {
            result.add(group.getId().toString());
        }
        while(group.getParentId() != null) {
            group = groupRepository.findById(group.getParentId()).orElse(null);
            if(group != null) {
                result.add(group.getId().toString());
            }
        }
        return result;
    }

    public Group getGroupFromVehicleId(String vehicleId) {
        List<Group> allGroups = this.getAllGroups();
        Group group = null;
        for(Group g : allGroups) {
            ArrayList<Object> children = Util.stringToArrayList(g.getChildren()==null ? "[]" : g.getChildren());
            if(children.contains(vehicleId)) {
                group = g;
                break;
            }
        }
        return group;
    }
}
