package com.example.project.teacherPanel.ViewPost;

public class viewCLass {

   String title,disc,pdf,semester,id,postDate,lastdate,catogeryofPost;

    public viewCLass(String title, String disc, String pdf, String semester, String id, String postDate, String lastdate, String catogeryofPost) {
        this.title = title;
        this.disc = disc;
        this.pdf = pdf;
        this.semester = semester;
        this.id = id;
        this.postDate = postDate;
        this.lastdate = lastdate;
        this.catogeryofPost = catogeryofPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getCatogeryofPost() {
        return catogeryofPost;
    }

    public void setCatogeryofPost(String catogeryofPost) {
        this.catogeryofPost = catogeryofPost;
    }
}
