package org.liquigraph.ogm;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class EntityWithGraphId {

    @GraphId
    private String id;

    private String aProperty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getaProperty() {
        return aProperty;
    }

    public void setaProperty(String aProperty) {
        this.aProperty = aProperty;
    }
}
