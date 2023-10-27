package org.siever.models;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import java.util.Objects;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ChapterTest {
    @Test
    public void testDefaultConstructor() {
        Chapter chapter = new Chapter();
        assertEquals(0, chapter.getIndex());
        assertEquals(null, chapter.getHead());
        assertEquals(new ArrayList<Paragraph>(), chapter.getParagraphs());
    }

    @Test
    public void testSettersAndGetters() {
        Chapter chapter = new Chapter();
        chapter.setIndex(1);
        chapter.setHead("Chapter 1");
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        Paragraph paragraph = new Paragraph();
        paragraphs.add(paragraph);
        chapter.setParagraphs(paragraphs);

        assertEquals(1, chapter.getIndex());
        assertEquals("Chapter 1", chapter.getHead());
        assertEquals(paragraphs, chapter.getParagraphs());
    }

    @Test
    public void testEqualsForEquality() {
        Chapter chapter1 = new Chapter();
        chapter1.setIndex(1);
        chapter1.setHead("Chapter 1");
        ArrayList<Paragraph> paragraphs1 = new ArrayList<>();
        Paragraph paragraph = Instancio.create(Paragraph.class);
        paragraphs1.add(paragraph);
        chapter1.setParagraphs(paragraphs1);

        Chapter chapter2 = new Chapter();
        chapter2.setIndex(1);
        chapter2.setHead("Chapter 1");
        ArrayList<Paragraph> paragraphs2 = new ArrayList<>();
        paragraphs2.add(paragraph);
        chapter2.setParagraphs(paragraphs2);

        assertTrue(chapter1.equals(chapter2));
    }

    @Test
    public void testEqualsForInequality() {
        Chapter chapter1 = new Chapter();
        chapter1.setIndex(1);
        chapter1.setHead("Chapter 1");
        ArrayList<Paragraph> paragraphs1 = new ArrayList<>();
        Paragraph paragraph1 = Instancio.create(Paragraph.class);
        paragraphs1.add(paragraph1);
        chapter1.setParagraphs(paragraphs1);

        Chapter chapter2 = new Chapter();
        chapter2.setIndex(2);
        chapter2.setHead(chapter1.getHead());
        chapter2.setParagraphs(chapter1.getParagraphs());

        assertFalse(chapter1.equals(chapter2));

        chapter2.setIndex(chapter1.getIndex());
        chapter2.setHead(chapter1.getHead()+"2");

        assertFalse(chapter1.equals(chapter2));

        chapter2.setHead(chapter1.getHead());
        Paragraph paragraph2 = Instancio.create(Paragraph.class);
        ArrayList<Paragraph> paragraphs2 = new ArrayList<Paragraph>();
        paragraphs2.add(paragraph2);
        chapter2.setParagraphs(paragraphs2);
        assertFalse(chapter1.equals(chapter2));

        paragraphs1.add(paragraph2);
        assertFalse(chapter1.equals(chapter2));
    }

    @Test
    public void testHashCode() {
        Chapter chapter = new Chapter();
        chapter.setIndex(1);
        chapter.setHead("Chapter 1");
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        Paragraph paragraph = Instancio.create(Paragraph.class);
        paragraphs.add(paragraph);
        chapter.setParagraphs(paragraphs);

        int expectedHashCode = Objects.hash("Chapter 1", 1, paragraphs);
        assertEquals(expectedHashCode, chapter.hashCode());
    }

    @Test
    public void testToString() {
        Chapter chapter = Instancio.create(Chapter.class);
        String expected = "Chapter{" +
                "head='" + chapter.getHead() + '\'' +
                ", index='" + chapter.getIndex() + '\'' +
                ", paragraphs='" + chapter.getParagraphs().toString() + '\'' +
                '}';

        assertEquals(chapter.toString(), expected);
    }
}