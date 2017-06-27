package org.liquigraph.ogm;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.lang.reflect.Field;
import java.util.List;

public class InsertOperation {
    private final String entityName;
    private final List<OgmProperty> properties;

    public InsertOperation(String entityName, List<OgmProperty> properties) {

        this.entityName = entityName;
        this.properties = properties;
    }

    public Object resolveEntity() {
        // use reflection to instantiate class and set properties
        // edge cases (TODO unit tests) :
        //  - class exists
        //  - class is an OGM entity (@NodeEntity / @RelationEntity)
        //  - properties exists and value type is compatible
        //  - do not assign @GraphId properties !!
        Class<?> entityClass = null;
        try {
            entityClass = Class.forName(this.entityName);
            NodeEntity[] nodeAnnotation = entityClass.getDeclaredAnnotationsByType(NodeEntity.class);
            RelationshipEntity[] relationshipAnnotation = entityClass.getDeclaredAnnotationsByType(RelationshipEntity.class);
            if(relationshipAnnotation.length>0 || nodeAnnotation.length > 0){
                Object instance = entityClass.newInstance();
                for (OgmProperty property : this.properties) {
                    setValue(entityClass, instance, property.getName(), property.getValue());
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            return null;
        }

        return null;
    }

    private  static void setValue(Class clazz, Object entity, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = clazz.getDeclaredField(name);
        declaredField.setAccessible(true);
        GraphId[] graphIdAnnotation = declaredField.getAnnotationsByType(GraphId.class);
        if(graphIdAnnotation.length==0) {
            if (declaredField.getType().equals(Long.class)) {
                declaredField.set(entity, Long.parseLong(value));
            } else {
                declaredField.set(entity, value);
            }
            declaredField.setAccessible(false);
        }

    }

}
