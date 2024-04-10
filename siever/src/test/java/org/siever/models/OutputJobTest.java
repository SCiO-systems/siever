package org.siever.models;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class OutputJobTest {

    @Test
    public void testDefaultConstructor() {
        OutputJob out = new OutputJob();
        assertEquals(OutputJob.class, out.getClass());
        assertEquals(null, out.getS3FileName());
    }

    @Test
    public void testSetAndGetSieverId() {
        OutputJob out = new OutputJob();
        String sample = Instancio.create(String.class);
        out.setS3FileName(sample);
        assertEquals(sample, out.getS3FileName());
    }

    @Test
    public void testEqualsForEquality() {
        String sample = Instancio.create(String.class);
        OutputJob out1 = new OutputJob();
        out1.setS3FileName(sample);
        OutputJob out2 = new OutputJob();
        out2.setS3FileName(sample);
        assertTrue(out1.equals(out2));
    }

    @Test
    public void testEqualsForInequality() {
        OutputJob out1 = Instancio.create(OutputJob.class);
        OutputJob out2 = Instancio.create(OutputJob.class);
        assertFalse(out1.equals(out2));
    }

    @Test
    public void testToString() {
        OutputJob out = Instancio.create(OutputJob.class);
        String txt = out.getS3FileName();
        String expected = "OutputJob{S3FileName='" + txt + "'}";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testHashCode() {
        OutputJob out = Instancio.create(OutputJob.class);
        String txt = out.getS3FileName();
        int expectedHashCode = Objects.hash(txt);
        assertEquals(expectedHashCode, out.hashCode());
    }

}