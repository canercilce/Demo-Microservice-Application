package com.example.demo.Tree;

import com.example.demo.Util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>(rootData);
        root.children = new ArrayList<Node<T>>();
    }

    public Tree(Node<T> node) {
        root = node;
    }

    public Node<T> getRoot(){
        return root;
    }

    public void addChild(T parentId, Node<T> child) {
        child.setParentId(parentId);
        Node<T> parent = getNode(parentId);
        parent.children.add(child);
    }

    public void addChild(Node<T> parent, Node<T> child) {
        child.setParentId((T) Util.objectToJSONObject(parent.getData()).get("id").toString());
        parent.children.add(child);
    }

    public void removeChild(Node<T> child) {
        T parentId = child.getParentId();
        Node<T> parent = getNode(parentId);
        if(parent!=null) {
            parent.children.remove(child);
        }
        child.setParentId(null);
    }

    public Tree copy() {
        Tree treeCopy = new Tree(this.getRoot());
        return treeCopy;
    }

    public Node<T> getNode(T id) {
        if(id==null){
            return null;
        }
        ArrayList<Node<T>> nodeArr = getSubtree(this.getRoot(), new ArrayList<>());
        for(Node<T> n: nodeArr) {
            if(Objects.equals(Util.objectToJSONObject(n.getData()).get("id").toString(), id.toString())) {
                return n;
            }
        }
        return null;
    }

    public Node<T> search(Node<T> node, T data) {
        if (node == null) {
            return null;
        }
        if (node.data.toString().equals(data.toString())) {
            return node;
        }
        for (Node<T> child : node.children) {
            if(search(child, data)!=null){
                return search(child, data);
            }
        }
        return null;
    }

    public ArrayList<Node<T>> getSubtree(Node<T> node, ArrayList<Node<T>> arr) {
        if (node == null) {
            return null;
        }
        arr.add(node);
        for (Node<T> child : node.children) {
            getSubtree(child, arr);
        }
        return arr;
    }

    public static class Node<T> {
        private List<Node<T>> children = new ArrayList<Node<T>>();
        private T parentId = null;
        private T data = null;

        public Node(T data) {
            this.data = data;
        }

        public Node(T data, T parentId) {
            this.data = data;
            this.parentId = parentId;
        }

        public List<Node<T>> getChildren() {
            return children;
        }

        public T getParentId() {
            return this.parentId;
        }

        public void setParentId(T parentId) {
            this.parentId = parentId;
        }

        public T getData() {
            return this.data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public boolean isRoot() {
            return (this.parentId == null);
        }

        public boolean isLeaf() {
            return this.children.size() == 0;
        }

        public void removeParent() {
            this.parentId = null;
        }
    }
}