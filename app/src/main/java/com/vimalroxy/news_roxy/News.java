package com.vimalroxy.news_roxy;

public class News {

    private String webTitle;
    private String sectionName;
    private String webUrl;
    private String authorName;
    private String date;

    public News(String webTitle, String sectionName, String webUrl, String authorName, String date) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webUrl = webUrl;
        this.authorName = authorName;
        this.date = date;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "News{" +
                "webTitle='" + webTitle + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
