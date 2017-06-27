package org.liquigraph.ogm;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InsertOperationTest {

    @Test
    public void anUnknownClassCantBeMapped() {
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.Unknown", new ArrayList<>());
        assertEquals(null, insertOperation.resolveEntity());

    }

    @Test
    public void anEntityWithoutAnnotationsCantBeMapped() {
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.EntityWithoutAnnotation", new ArrayList<>());

        assertEquals(null, insertOperation.resolveEntity());
    }

    @Test
    public void aGraphIdColumnCantBeMapped() {
        ArrayList<OgmProperty> properties = new ArrayList<>();
        properties.add(new OgmProperty("id",10));
        properties.add(new OgmProperty("aProperty","toto"));
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.EntityWithGraphId", properties);
        assertEquals(null, insertOperation.resolveEntity());

    }

}