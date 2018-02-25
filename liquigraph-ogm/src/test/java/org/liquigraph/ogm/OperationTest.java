package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.Property;

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
        ArrayList<Property> properties = new ArrayList<>();
        properties.add(new Property("id","10"));
        properties.add(new Property("aProperty","toto"));
        Operation operation = new Operation("org.liquigraph.ogm.entity.EntityWithGraphId", properties,null,null);
        operation.resolveEntity();

    }

}
