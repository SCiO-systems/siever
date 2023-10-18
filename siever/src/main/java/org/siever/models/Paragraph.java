package org.siever.models;

import java.util.Objects;

public class Paragraph {
    private int index;
    private int size;
    private String text;

    public Paragraph() {
    }

    public Paragraph(int index, int size, String text){
        this.index = index;
        this.size = size;
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString(){
        return "Paragraph{" +
                "index='" + index + '\'' +
                ", size='" + size + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paragraph that = (Paragraph) o;
        return Objects.equals(getIndex(), that.getIndex()) && Objects.equals(getSize(), that.getSize()) && Objects.equals(getText(), that.getText());
    }

    public int hashCode() {
        return Objects.hash(getIndex(), getSize(), getText());
    }
}
