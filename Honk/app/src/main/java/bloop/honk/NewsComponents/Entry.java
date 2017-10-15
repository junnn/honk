package bloop.honk.NewsComponents;

/**
 * Created by Don on 2017/10/15.
 */

public class Entry {
    String title;
    String link;
    String summary;
    String pubDate;
    String news_type;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDesc() {
        return summary;
    }

    public Entry(String title, String summary, String link, String pubDate, String news_type) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.pubDate = pubDate;
        this.news_type = news_type;
    }
}
