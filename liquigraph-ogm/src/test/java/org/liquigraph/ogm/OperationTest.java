package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperationTest {

    @Test
    public void anUnknownClassCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        Operation operation = new Operation("org.liquigraph.ogm.Unknown", new ArrayList<>(),null);
        assertEquals(null, operation.resolveEntity());

    }

    @Test(expected = NotAnOgmEntityException.class)
    public void anEntityWithoutAnnotationsCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        Operation operation = new Operation("org.liquigraph.ogm.entity.EntityWithoutAnnotation", new ArrayList<>(),null);

        operation.resolveEntity();
    }

    @Test(expected = GraphIdException.class)
    public void aGraphIdColumnCantBeMapped() throws NotAnOgmEntityException, GraphIdException {
        ArrayList<OgmProperty> properties = new ArrayList<>();
        properties.add(new OgmProperty("id",10));
        properties.add(new OgmProperty("aProperty","toto"));
        Operation operation = new Operation("org.liquigraph.ogm.entity.EntityWithGraphId", properties,null);
        operation.resolveEntity();

    }

}
