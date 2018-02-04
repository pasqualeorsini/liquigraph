package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperationTest {

    @Test(expected = ClassNotFoundException.class)
    public void anUnknownClassCantBeMapped() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        Operation operation = new Operation("org.liquigraph.ogm.Unknown", new ArrayList<>(),null,null);

    }

    @Test(expected = NotAnOgmEntityException.class)
    public void anEntityWithoutAnnotationsCantBeMapped() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        Operation operation = new Operation("org.liquigraph.ogm.entity.EntityWithoutAnnotation", new ArrayList<>(),null,null);

        operation.resolveEntity();
    }

    @Test(expected = GraphIdException.class)
    public void aGraphIdColumnCantBeMapped() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        ArrayList<OgmProperty> properties = new ArrayList<>();
        properties.add(new OgmProperty("id",10));
        properties.add(new OgmProperty("aProperty","toto"));
        Operation operation = new Operation("org.liquigraph.ogm.entity.EntityWithGraphId", properties,null,null);
        operation.resolveEntity();

    }

}
