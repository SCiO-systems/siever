package org.siever.models;
import org.instancio.Instancio;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import scala.xml.PrettyPrinter;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ParagraphTest {

    @Test
    public void testDefaultConstructor() {
        Paragraph paragraph = new Paragraph();
        assertEquals(paragraph.getClass(), Paragraph.class);
        assertEquals(0, paragraph.getIndex());
        assertEquals(0, paragraph.getSize());
        assertEquals(null, paragraph.getText());
    }

    @Test
    public void testParameterizedConstructorAndGet() {
        String sample_txt = Instancio.create(String.class);
        int sample_index = Instancio.create(Integer.class);
        int sample_size = Instancio.create(Integer.class);
        Paragraph paragraph = new Paragraph(sample_index, sample_size, sample_txt);
        assertEquals(sample_index, paragraph.getIndex());
        assertEquals(sample_size, paragraph.getSize());
        assertEquals(sample_txt, paragraph.getText());
    }

    @Test
    public void testSetAndGetText() {
        Paragraph paragraph = new Paragraph();
        String sample_txt = Instancio.create(String.class);
        int sample_index = Instancio.create(Integer.class);
        int sample_size = Instancio.create(Integer.class);
        paragraph.setText(sample_txt);
        assertEquals(sample_txt, paragraph.getText());
        paragraph.setIndex(sample_index);
        assertEquals(sample_index, paragraph.getIndex());
        paragraph.setSize(sample_size);
        assertEquals(sample_size, paragraph.getSize());
    }

    @Test
    public void testEqualsForEquality() {
        String sample_txt = Instancio.create(String.class);
        int sample_index = Instancio.create(Integer.class);
        int sample_size = Instancio.create(Integer.class);
        Paragraph paragraph1 = new Paragraph(sample_index, sample_size, sample_txt);
        Paragraph paragraph2 = new Paragraph(sample_index, sample_size, sample_txt);
        assertTrue(paragraph1.equals(paragraph2));
    }

    @Test
    public void testEqualsForInequality() {
        Paragraph par1 = Instancio.create(Paragraph.class);
        Paragraph par2 = Instancio.create(Paragraph.class);
        assertFalse(par1.equals(par2));
    }

    @Test
    public void testToString() {
        Paragraph par = Instancio.create(Paragraph.class);
        String txt = par.getText();
        int size = par.getSize();
        int index = par.getIndex();
        String expected = "Paragraph{" +
                "index='" + index + '\'' +
                ", size='" + size + '\'' +
                ", text='" + txt + '\'' +
                '}';
        assertEquals(expected, par.toString());
    }

    @Test
    public void testHashCode() {
        Paragraph par = Instancio.create(Paragraph.class);
        String txt = par.getText();
        int size = par.getSize();
        int index = par.getIndex();
        int expectedHashCode = Objects.hash(index, size, txt);
        assertEquals(expectedHashCode, par.hashCode());
    }

}