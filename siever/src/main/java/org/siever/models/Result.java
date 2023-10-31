package org.siever.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.siever.models.InputJob;
import org.siever.models.Figure;
import org.siever.models.Chapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class Result {

    private InputJob metadata;
    private int pageCount;
    private String title;
    private String abstrct;
    private ArrayList<String> keywords;
    private ArrayList<Chapter> chapters;
    private ArrayList<Figure> figures;
    private String sieverID;

    public Result() {
        this.metadata = new InputJob();
        this.keywords = new ArrayList<String>();
        this.chapters = new ArrayList<Chapter>();
        this.figures = new ArrayList<Figure>();
    }

//    public Result(String id, String source, String url) {
//        this.metadata = new InputJob(id, source, url);
//    }

    public Result(InputJob metadata, int pageCount, String title, String abstrct, ArrayList<String> keywords, ArrayList<Chapter> chapters, ArrayList<Figure> figures, String sieverID) {
        this.metadata = new InputJob(metadata.getId(), metadata.getSource(), metadata.getUrl());
        this.pageCount = pageCount;
        this.keywords = new ArrayList<String>(keywords);
        this.title = title;
        this.abstrct = abstrct;
        this.chapters = new ArrayList<Chapter>(chapters);
        this.figures = new ArrayList<Figure>(figures);
        this.sieverID = sieverID;
    }
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("abstract")
    public String getAbstrct() {
        return abstrct;
    }

    public void setAbstrct(String abstrct) {
        this.abstrct = abstrct;
    }

    public InputJob getMetadata(){
        return metadata;
    }

    public void setMetadata(InputJob metadata) {
        this.metadata = metadata;
    }
    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public ArrayList<Figure> getFigures() {
        return figures;
    }

    public void setFigures(ArrayList<Figure> figures) {
        this.figures = figures;
    }

    public String getSieverID() {
        return sieverID;
    }

    public void setSieverID(String sieverID) {
        this.sieverID = sieverID;
    }

    public String toString() {
        return "Result{" +
                "metadata='" + metadata.toString() + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", keywords='" + keywords + '\'' +
                ", title='" + title + '\'' +
                ", abstarct='" + abstrct + '\'' +
                ", chapters='" + chapters + '\'' +
                ", figures='" + figures + '\'' +
                ", sieverID='" + sieverID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return pageCount == result.pageCount && Objects.equals(metadata, result.metadata) && Objects.equals(title, result.title) && Objects.equals(abstrct, result.abstrct) && Objects.equals(keywords, result.keywords) && Objects.equals(chapters, result.chapters) && Objects.equals(figures, result.figures) && Objects.equals(sieverID, result.sieverID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, pageCount, title, abstrct, keywords, chapters, figures, sieverID);
    }
}
