package com.example.project.userpanel.userNotifications;

public class StudentNotifcationClass {
    String name,email,gender,semester,url,item,id,answerStatus,catogryOfPost;

    public StudentNotifcationClass(String name, String email, String gender, String semester, String url, String item, String id, String answerStatus, String catogryOfPost) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.semester = semester;
        this.url = url;
        this.item = item;
        this.id = id;
        this.answerStatus = answerStatus;
        this.catogryOfPost = catogryOfPost;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
    }

    public String getCatogryOfPost() {
        return catogryOfPost;
    }

    public void setCatogryOfPost(String catogryOfPost) {
        this.catogryOfPost = catogryOfPost;
    }
}
