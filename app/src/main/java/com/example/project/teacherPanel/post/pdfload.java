package com.example.project.teacherPanel.post;

public class pdfload {
    String title;
    String pdfFile;
    String desc;
    String semester;
    String id;
    String DeadlineDate;
    String PostDate;
    String email;
    String catogeryOfPost;

    public pdfload() {
    }

    public pdfload(String title, String pdfFile, String desc, String semester, String id, String deadlineDate, String postDate, String email, String catogeryOfPost) {
        this.title = title;
        this.pdfFile = pdfFile;
        this.desc = desc;
        this.semester = semester;
        this.id = id;
        DeadlineDate = deadlineDate;
        PostDate = postDate;
        this.email = email;
        this.catogeryOfPost = catogeryOfPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeadlineDate() {
        return DeadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        DeadlineDate = deadlineDate;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCatogeryOfPost() {
        return catogeryOfPost;
    }

    public void setCatogeryOfPost(String catogeryOfPost) {
        this.catogeryOfPost = catogeryOfPost;
    }
}
