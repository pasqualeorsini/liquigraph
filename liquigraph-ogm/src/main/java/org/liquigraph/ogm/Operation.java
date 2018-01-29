package org.liquigraph.ogm;

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

public class Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class);
    private final String entityName;
    public final List<OgmProperty> properties;
    private String label;
    private String operationType;

    public Operation(String entityName, List<OgmProperty> properties, String operationType) {

        this.entityName = entityName;
        this.label =entityName.split("\\.")[entityName.split("\\.").length-1];
        this.properties = properties;
        this.operationType = operationType;
    }

    public Object resolveEntity() throws NotAnOgmEntityException, GraphIdException {
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

                return instance;
            } else {
                throw new NotAnOgmEntityException("This entity is not annotated with @NodeEntity or @RelationEntity : " + this.entityName);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            return null;
        } catch (MappingException e) {
            LOGGER.error("This entity ({}) can't be mapped : {}",entityClass.getName(),e);
        }

        return null;
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
                throw new GraphIdException("It's impossible to map a graphid field");
            }

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new MappingException("An OGMProperty is not available on this entity", e);
        }

    }

    public String getLabel() {
        return label;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
