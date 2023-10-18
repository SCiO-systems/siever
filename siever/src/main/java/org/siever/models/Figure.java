package org.siever.models;

import java.util.Objects;

public class Figure {
    private String text;

    public Figure(){

    }

    public Figure(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return "Figure{text='" + text + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return Objects.equals(text, figure.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
