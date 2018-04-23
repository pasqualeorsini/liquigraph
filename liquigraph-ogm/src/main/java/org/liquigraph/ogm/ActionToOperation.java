package org.liquigraph.ogm;

import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.Action;
import org.liquigraph.ogm.schema.Insert;
import org.liquigraph.ogm.schema.Property;
import org.liquigraph.ogm.schema.Update;

import java.util.ArrayList;
import java.util.List;

public class ActionToOperation {

    public static Operation convert(Action action) throws NotAnOgmEntityException, ClassNotFoundException {
        List<Property> properties = null;
        String operationType = null;
        if(action instanceof Insert){
            properties = ((Insert) action).getProperties();
            operationType="CREATE";
        } else if(action instanceof Update){
            properties = ((Update) action).getPropertyList();
            operationType="UPDATE";
        }

        List<OgmProperty> propertiesOgm = new ArrayList<>();
        for (Property property : properties) {
            propertiesOgm.add(new OgmProperty(property.getName(),property.getValue()));
        }
        return new Operation(action.getEntity(),properties,operationType);
    }
}
