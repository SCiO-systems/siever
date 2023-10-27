package org.siever.models;

import jnr.ffi.annotations.In;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InputJobTest {

    @Test
    public void testDefaultConstructor() {
        InputJob input = new InputJob();
        assertEquals(input.getClass(), InputJob.class);
        assertEquals(null, input.getId());
        assertEquals(null, input.getUrl());
        assertEquals(null, input.getSource());
    }

    @Test
    public void testParametrizedConstructor() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob input = new InputJob(id, source, url);
        assertEquals(input.getClass(), InputJob.class);
        assertEquals(id, input.getId());
        assertEquals(url, input.getUrl());
        assertEquals(source, input.getSource());
    }

    @Test
    public void testSetAndGet() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob input = new InputJob();
        input.setId(id);
        input.setSource(source);
        input.setUrl(url);
        assertEquals(url, input.getUrl());
        assertEquals(id, input.getId());
        assertEquals(source, input.getSource());
    }

    @Test
    public void testEqualsForEquality() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        InputJob input1 = new InputJob(id, source, url);
        InputJob input2 = new InputJob(id, source, url);
        assertTrue(input1.equals(input2));
    }

    @Test
    public void testEqualsForInequality() {
        InputJob input1 = Instancio.create(InputJob.class);
        InputJob input2 = Instancio.create(InputJob.class);
        assertFalse(input1.equals(input2));
    }

    @Test
    public void testToString() {
        InputJob input = Instancio.create(InputJob.class);
        String url = input.getUrl();
        String id = input.getId();
        String source = input.getSource();
        String expected = "InputJob{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
        assertEquals(expected, input.toString());
    }

    @Test
    public void testHashCode() {
        InputJob input = Instancio.create(InputJob.class);
        String url = input.getUrl();
        String id = input.getId();
        String source = input.getSource();
        int expectedHashCode = Objects.hash(id, source, url);
        assertEquals(expectedHashCode, input.hashCode());
    }

}