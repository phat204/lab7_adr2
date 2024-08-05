package fpoly.account.myapplication.models;

import java.util.HashMap;

public class ToDo {
    private String id;
    private String title;
    private String content;
    private String date;
    private String type;
    private int status;

    public ToDo() {
    }

    public ToDo(String id, String title, String content, String date, String type, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.type = type;
        this.status = status;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Object> convertHashmap() {
        HashMap<String, Object> work = new HashMap<>();
        work.put("id", this.id);
        work.put("title", this. title);
        work.put("content", this. content);
        work.put("date", this. date);
        work.put("type", this. type);
        work.put("status", this. status);
        return work;
    }
}
