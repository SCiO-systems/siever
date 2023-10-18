package org.siever.models;

import java.util.ArrayList;
import java.util.Objects;

public class Chapter {
    private String head;
    private int index;
    private ArrayList<Paragraph> paragraphs;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String toString() {
        return "Chapter{" +
                "head='" + head + '\'' +
                ", index='" + index + '\'' +
                ", paragraphs='" + paragraphs.toString() + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter that = (Chapter) o;
        return Objects.equals(getIndex(), that.getIndex()) && Objects.equals(getHead(), that.getHead()) && Objects.equals(getParagraphs(), that.getParagraphs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, index, paragraphs);
    }
}
