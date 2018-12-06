package org.jystudio.opandroid.database.service;


public class Question {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(String lastmodify) {
        this.lastmodify = lastmodify;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }



    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getBlame() {
        return blame;
    }

    public void setBlame(String blame) {
        this.blame = blame;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String id          ;
    private String title       ;
    private String body        ;
    private String answer      ;
    private String submitter   ;
    private String modifier    ;
    private String lastmodify  ;
    private String language    ;
    private String category    ;
    private String company     ;
    private String rate        ;
    private String imgpath     ;
    private String heat        ;
    private String syncflag    ;
    private String blame       ;
    private String duplicate   ;
}