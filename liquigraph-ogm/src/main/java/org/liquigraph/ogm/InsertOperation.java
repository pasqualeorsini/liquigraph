package org.liquigraph.ogm;

public class InsertOperation {
    public InsertOperation(String entityName, OgmProperty property) {
    }

    public Object resolveEntity() {
        // use reflection to instantiate class and set properties
        // edge cases (TODO unit tests) :
        //  - class exists
        //  - class is an OGM entity (@NodeEntity / @RelationEntity)
        //  - property exists and value type is compatible
        //  - do not assign @GraphId property !!
        return null;
    }
}
