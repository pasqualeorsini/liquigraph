package org.liquigraph.ogm;

import org.liquigraph.core.io.lock.LiquigraphLock;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.MappingException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class InsertOperation {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertOperation.class);
    private final String entityName;
    private final List<OgmProperty> properties;

    public InsertOperation(String entityName, List<OgmProperty> properties) {

        this.entityName = entityName;
        this.properties = properties;
    }

    public Object resolveEntity() throws NotAnOgmEntityException, GraphIdException {
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
            if (relationshipAnnotation.length > 0 || nodeAnnotation.length > 0) {
                Object instance = entityClass.newInstance();
                for (OgmProperty property : this.properties) {
                    setValue(entityClass, instance, property.getName(), property.getValue());
                }
            } else {
                throw new NotAnOgmEntityException("This entity is not annotated with @NodeEntity or @RelationEntity : " + this.entityName);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            return null;
        } catch (MappingException e) {
            LOGGER.error("This entity ("+this.entityName+") can't be mapped",e);
        }

        return entityClass;
    }

    private static void setValue(Class clazz, Object entity, String name, Object value) throws MappingException, GraphIdException {
        try {
            Field declaredField = clazz.getDeclaredField(name);
            declaredField.setAccessible(true);
            GraphId[] graphIdAnnotation = declaredField.getAnnotationsByType(GraphId.class);
            if (graphIdAnnotation.length == 0) {
                if (value.getClass().equals(String.class)) {
                    if (declaredField.getType().equals(Long.class)) {
                        declaredField.set(entity, Long.parseLong((String) value));
                    } else {
                        declaredField.set(entity, value);
                    }
                    declaredField.setAccessible(false);
                }
            } else {
                throw new GraphIdException("Can't map a graphid field");
            }

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new MappingException("This OGMProperty is not available on this entity", e);
        }

    }
}
