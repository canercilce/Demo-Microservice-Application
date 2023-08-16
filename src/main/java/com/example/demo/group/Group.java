package com.example.demo.group;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private Long parentId;

    @Column(length = 100000)
    private String children;

    public Group(Long id, String groupName, Long parentId, String children) {
        this.id = id;
        this.groupName = groupName;
        this.parentId = parentId;
        this.children = children;
    }

    public Group(String groupName, Long parentId, String children) {
        this.groupName = groupName;
        this.parentId = parentId;
        this.children = children;
    }

    public Group() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
