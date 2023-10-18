package org.siever.models;

import java.util.Objects;

public class InputJob {

    private String id;
    private String source;
    private String url;

    public InputJob() {
    }

    public InputJob(String id, String source, String url) {
        this.id = id;
        this.source = source;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {

        return "InputJob{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputJob that = (InputJob) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getSource(), that.getSource()) && Objects.equals(getUrl(), that.getUrl());
    }

    public int hashCode() {
        return Objects.hash(getId(), getSource(), getUrl());
    }

}
