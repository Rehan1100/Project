package com.example.project.userpanel;

public class ModelCLass {

   String title,disc,pdf,semester,pDate,Ldate,id,catogeryOfPost;

    public ModelCLass(String title, String disc, String pdf, String semester, String pDate, String ldate, String id, String catogeryOfPost) {
        this.title = title;
        this.disc = disc;
        this.pdf = pdf;
        this.semester = semester;
        this.pDate = pDate;
        Ldate = ldate;
        this.id = id;
        this.catogeryOfPost = catogeryOfPost;
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

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getLdate() {
        return Ldate;
    }

    public void setLdate(String ldate) {
        Ldate = ldate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatogeryOfPost() {
        return catogeryOfPost;
    }

    public void setCatogeryOfPost(String catogeryOfPost) {
        this.catogeryOfPost = catogeryOfPost;
    }
}
