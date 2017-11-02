package bloop.honk.Model;

/**
 * Created by Don on 2017/10/15.
 */

public class News {

    private String title;
    private String link;
    private String summary;
    private String pubDate;
    private String news_type;

    public News(String title, String summary, String link, String pubDate, String news_type) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.pubDate = pubDate;
        this.news_type = news_type;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getNews_type() {
        return news_type;
    }

    public String getLink() {
        return link;
    }


}
