package org.jystudio.opandroid.database.service;


public class Question {
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    private long id            ;
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

    private Question() {
        //This should NOT be called.
    }

    public Question(String body) {
       id         = 0;
       title      = "最新面试题"  ;
       this.body  = body  ;
       answer     = "我不知道"  ;
       submitter  = "小明"  ;
       modifier   = "笑笑"  ;
       lastmodify = MyConstant.MY_D_DAY_DATETIME  ;
       language   = "common"  ;
       category   = "na"  ;
       company    = "不可说"  ;
       rate       = "1"  ;
       imgpath    = "na"  ;
       heat       = "1"  ;
       syncflag   = Integer.toString(MyConstant.SYNC_FLAG_LOCAL_ADD)  ;
       blame      = "0"  ;
       duplicate  = "0"  ;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\'" + id + "\'" +
                ", \"title\":\'" + title + "\'" +
                ", \"body\":\'" + body + "\'" +
                ", \"answer\":\'" + answer + "\'" +
                ", \"submitter\":\'" + submitter + "\'" +
                ", \"modifier\":\'" + modifier + "\'" +
                ", \"lastmodify\":\'" + lastmodify + "\'" +
                ", \"language\":\'" + language + "\'" +
                ", \"category\":\'" + category + "\'" +
                ", \"company\":\'" + company + "\'" +
                ", \"rate\":\'" + rate + "\'" +
                ", \"imgpath\":\'" + imgpath + "\'" +
                ", \"heat\":\'" + heat + "\'" +
                ", \"syncflag\":\'" + syncflag + "\'" +
                ", \"blame\":\'" + blame + "\'" +
                ", \"duplicate\":\'" + duplicate + "\'" +
                '}';
    }


}
