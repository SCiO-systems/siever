package org.siever.models;
import org.instancio.Instancio;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {

    @Test
    public void testDefaultConstructor() {
        Figure figure = new Figure();
        assertEquals(null, figure.getText());
    }

    @Test
    public void testParameterizedConstructorAndGetText() {
        String sample = Instancio.create(String.class);
        Figure figure = new Figure(sample);
        assertEquals(sample, figure.getText());
    }

    @Test
    public void testSetAndGetText() {
        Figure figure = new Figure();
        String sample = Instancio.create(String.class);
        figure.setText(sample);
        assertEquals(sample, figure.getText());
    }

    @Test
    public void testEqualsForEquality() {
        String sample = Instancio.create(String.class);
        Figure figure1 = new Figure(sample);
        Figure figure2 = new Figure(sample);
        assertTrue(figure1.equals(figure2));
    }

    @Test
    public void testEqualsForInequality() {
        Figure figure1 = Instancio.create(Figure.class);
        Figure figure2 = Instancio.create(Figure.class);
        assertFalse(figure1.equals(figure2));
    }

    @Test
    public void testToString() {
        Figure fig = Instancio.create(Figure.class);
        String txt = fig.getText();
        String expected = "Figure{text='" + txt + "'}";
        assertEquals(expected, fig.toString());
    }

    @Test
    public void testHashCode() {
        Figure figure = Instancio.create(Figure.class);
        String txt = figure.getText();
        int expectedHashCode = Objects.hash(txt);
        assertEquals(expectedHashCode, figure.hashCode());
    }
}