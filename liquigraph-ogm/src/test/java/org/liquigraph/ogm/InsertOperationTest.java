package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InsertOperationTest {

    @Test
    public void anUnknownClassCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.Unknown", new ArrayList<>());
        assertEquals(null, insertOperation.resolveEntity());

    }

    @Test(expected = NotAnOgmEntityException.class)
    public void anEntityWithoutAnnotationsCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.entity.EntityWithoutAnnotation", new ArrayList<>());

        insertOperation.resolveEntity();
    }

    @Test(expected = GraphIdException.class)
    public void aGraphIdColumnCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        ArrayList<OgmProperty> properties = new ArrayList<>();
        properties.add(new OgmProperty("id",10));
        properties.add(new OgmProperty("aProperty","toto"));
        InsertOperation insertOperation = new InsertOperation("org.liquigraph.ogm.entity.EntityWithGraphId", properties);
        insertOperation.resolveEntity();

    }

}
