package org.siever.models;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ResultTest {

    @Test
    public void testDefaultConstructor() {
        Result res = new Result();
        assertEquals(res.getClass(), Result.class);
        assertEquals(null, res.getAbstrct());
        assertEquals(null, res.getSieverID());
        assertEquals(null, res.getTitle());
        assertEquals(new ArrayList<String>(), res.getKeywords());
        assertEquals(new ArrayList<Chapter>(), res.getChapters());
        assertEquals(new InputJob(), res.getMetadata());
        assertEquals(new ArrayList<Figure>(), res.getFigures());
    }

    @Test
    public void testParametrizedConstructor() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob metadata = new InputJob(id, source, url);
        int pageCount = Instancio.create(int.class);
        String title = Instancio.create(String.class);
        String abstrct = Instancio.create(String.class);
        ArrayList<String> keywords = new ArrayList<String>();
        String key = Instancio.create(String.class);
        keywords.add(key);
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        ArrayList<Figure> figures = new ArrayList<Figure>();
        Figure fig = Instancio.create(Figure.class);
        figures.add(fig);
        fig = Instancio.create(Figure.class);
        figures.add(fig);
        String sieverID = Instancio.create(String.class);
        Result res = new Result(metadata, pageCount, title, abstrct, keywords, chapters, figures, sieverID);
        assertEquals(res.getClass(), Result.class);
        assertEquals(metadata, res.getMetadata());
        assertEquals(pageCount, res.getPageCount());
        assertEquals(title, res.getTitle());
        assertEquals(abstrct, res.getAbstrct());
        assertEquals(keywords, res.getKeywords());
        assertEquals(chapters, res.getChapters());
        assertEquals(figures, res.getFigures());
        assertEquals(sieverID, res.getSieverID());
    }

    @Test
    public void testGettersAndSetters() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob metadata = new InputJob(id, source, url);
        int pageCount = Instancio.create(int.class);
        String title = Instancio.create(String.class);
        String abstrct = Instancio.create(String.class);
        ArrayList<String> keywords = new ArrayList<String>();
        String key = Instancio.create(String.class);
        keywords.add(key);
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        ArrayList<Figure> figures = new ArrayList<Figure>();
        Figure fig = Instancio.create(Figure.class);
        figures.add(fig);
        fig = Instancio.create(Figure.class);
        figures.add(fig);
        String sieverID = Instancio.create(String.class);
        Result res = new Result();
        res.setMetadata(metadata);
        res.setPageCount(pageCount);
        res.setTitle(title);
        res.setAbstrct(abstrct);
        res.setKeywords(keywords);
        res.setChapters(chapters);
        res.setFigures(figures);
        res.setSieverID(sieverID);
        assertEquals(metadata, res.getMetadata());
        assertEquals(pageCount, res.getPageCount());
        assertEquals(title, res.getTitle());
        assertEquals(abstrct, res.getAbstrct());
        assertEquals(keywords, res.getKeywords());
        assertEquals(chapters, res.getChapters());
        assertEquals(figures, res.getFigures());
        assertEquals(sieverID, res.getSieverID());
    }

    @Test
    public void testEqualsForEquality() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob metadata = new InputJob(id, source, url);
        int pageCount = Instancio.create(int.class);
        String title = Instancio.create(String.class);
        String abstrct = Instancio.create(String.class);
        ArrayList<String> keywords = new ArrayList<String>();
        String key = Instancio.create(String.class);
        keywords.add(key);
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        chap = Instancio.create(Chapter.class);
        chapters.add(chap);
        ArrayList<Figure> figures = new ArrayList<Figure>();
        Figure fig = Instancio.create(Figure.class);
        figures.add(fig);
        fig = Instancio.create(Figure.class);
        figures.add(fig);
        String sieverID = Instancio.create(String.class);

        Result res1 = new Result(metadata, pageCount, title, abstrct, keywords, chapters, figures, sieverID);
        Result res2 = new Result(metadata, pageCount, title, abstrct, keywords, chapters, figures, sieverID);

        assertTrue(res1.equals(res2));
    }

    @Test
    public void testEqualsForInequality() {
        Result res1 = Instancio.create(Result.class);
        Result res2 = Instancio.create(Result.class);

        assertFalse(res1.equals(res2));
    }

    @Test
    public void testToString() {
        Result result = Instancio.create(Result.class);
        InputJob metadata = result.getMetadata();
        int pageCount = result.getPageCount();
        String title = result.getTitle();
        String abstrct = result.getAbstrct();
        ArrayList<String> keywords = result.getKeywords();
        ArrayList<Chapter> chapters = result.getChapters();
        ArrayList<Figure> figures = result.getFigures();
        String sieverID = result.getSieverID();
        String expected = "Result{" +
                "metadata='" + metadata.toString() + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", keywords='" + keywords + '\'' +
                ", title='" + title + '\'' +
                ", abstarct='" + abstrct + '\'' +
                ", chapters='" + chapters + '\'' +
                ", figures='" + figures + '\'' +
                ", sieverID='" + sieverID + '\'' +
                '}';
        assertEquals(expected, result.toString());
    }

    @Test
    public void testHashCode() {
        Result result = Instancio.create(Result.class);
        InputJob metadata = result.getMetadata();
        int pageCount = result.getPageCount();
        String title = result.getTitle();
        String abstrct = result.getAbstrct();
        ArrayList<String> keywords = result.getKeywords();
        ArrayList<Chapter> chapters = result.getChapters();
        ArrayList<Figure> figures = result.getFigures();
        String sieverID = result.getSieverID();
        int expectedHashCode = Objects.hash(metadata, pageCount, title, abstrct, keywords, chapters, figures, sieverID);
        assertEquals(expectedHashCode, result.hashCode());
    }

}