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
        assertEquals(null, out.getSieverID());
    }

    @Test
    public void testSetAndGetSieverId() {
        OutputJob out = new OutputJob();
        String sample = Instancio.create(String.class);
        out.setSieverID(sample);
        assertEquals(sample, out.getSieverID());
    }

    @Test
    public void testEqualsForEquality() {
        String sample = Instancio.create(String.class);
        OutputJob out1 = new OutputJob();
        out1.setSieverID(sample);
        OutputJob out2 = new OutputJob();
        out2.setSieverID(sample);
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
        String txt = out.getSieverID();
        String expected = "OutputJob{sieverID='" + txt + "'}";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testHashCode() {
        OutputJob out = Instancio.create(OutputJob.class);
        String txt = out.getSieverID();
        int expectedHashCode = Objects.hash(txt);
        assertEquals(expectedHashCode, out.hashCode());
    }

}