package com.example.project.userpanel.UserUpload;

public class Answer {

    String name,email,semester,gender,url,item,postId,status,answerstatus,catogeryOfPost;

    public Answer(String name, String email, String semester, String gender, String url, String item, String postId, String status, String answerstatus, String catogeryOfPost) {
        this.name = name;
        this.email = email;
        this.semester = semester;
        this.gender = gender;
        this.url = url;
        this.item = item;
        this.postId = postId;
        this.status = status;
        this.answerstatus = answerstatus;
        this.catogeryOfPost = catogeryOfPost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswerstatus() {
        return answerstatus;
    }

    public void setAnswerstatus(String answerstatus) {
        this.answerstatus = answerstatus;
    }

    public String getCatogeryOfPost() {
        return catogeryOfPost;
    }

    public void setCatogeryOfPost(String catogeryOfPost) {
        this.catogeryOfPost = catogeryOfPost;
    }
}
