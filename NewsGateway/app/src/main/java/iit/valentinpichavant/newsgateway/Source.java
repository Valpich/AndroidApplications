package iit.valentinpichavant.newsgateway;

import java.io.Serializable;

/**
 * Created by valentinpichavant on 4/15/17.
 */

public class Source implements Serializable {

    private String id;
    private String name;
    private String url;
    private String category;

    public Source(String id, String name, String url, String category) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

