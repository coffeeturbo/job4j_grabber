package grabber.model;

import java.util.Date;

public class Post {
    private int id;
    private String name;
    private String text;
    private String link;
    private Date createdAt;

    @Override
    public String toString() {
        return "Post{"
            + "id=" + id
            + ", name='"
            + name + '\'' + ", text='"
            + text + '\'' + ", link='"
            + link + '\'' + ", createdAt="
            + createdAt + '}';
    }

    public Post() {
    }

    public Post(String name, String text, String link, Date createdAt) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.createdAt = createdAt;
    }

    public Post(int id, String name, String text, String link, Date createdAt) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.link = link;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post post = (Post) o;

        return link.equals(post.link);
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
}
