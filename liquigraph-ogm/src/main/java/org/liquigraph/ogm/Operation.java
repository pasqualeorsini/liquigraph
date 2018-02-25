package org.liquigraph.ogm;

import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.MappingException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.Delete;
import org.liquigraph.ogm.schema.Insert;
import org.liquigraph.ogm.schema.Property;
import org.liquigraph.ogm.schema.Update;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class);
    private final String entityName;
    private final List<OgmProperty> properties;
    private List<String> labels;
    private String operationType;
    private final List<OgmProperty> whereConditions;

    public Operation(String entityName, List<Property> properties, List<Property>where, String operationType) throws NotAnOgmEntityException, ClassNotFoundException {

        this.entityName = entityName;
        this.labels = new ArrayList<>();
        List<OgmProperty> ogmProperties = getOgmProperties(properties);
        this.properties = ogmProperties;
        this.operationType = operationType;
        List<OgmProperty> ogmPropertiesWhere = getOgmProperties(where);
        this.whereConditions = ogmPropertiesWhere;

        this.sanityCheck();
    }

    public Operation(String entityName, List<Property> properties, String operationType) throws NotAnOgmEntityException, ClassNotFoundException {
        this(entityName,properties,null,operationType);
    }

    public Operation(Insert insert) throws NotAnOgmEntityException, ClassNotFoundException {
        this(insert.getEntity(),insert.getProperties(),"CREATE");
    }

    public Operation(Update update) throws NotAnOgmEntityException, ClassNotFoundException {
        this(update.getEntity(),update.getPropertyList(),update.getWhere().getPropertyList(),"UPDATE");
    }

    public Operation(Delete delete) throws NotAnOgmEntityException, ClassNotFoundException {
        this(delete.getEntity(),null,delete.getWhere().getPropertyList(),"DELETE");
    }

    private List<OgmProperty> getOgmProperties(List<Property> properties) {
        if(properties!=null) {
            List<OgmProperty> ogmProperties = new ArrayList<>();
            for (Property property : properties) {
                OgmProperty ogmProperty = new OgmProperty(property.getName(), property.getValue());
                ogmProperties.add(ogmProperty);
            }
            return ogmProperties;
        } else {
            return null;
        }
    }

    private void sanityCheck() throws ClassNotFoundException, NotAnOgmEntityException {
        // Is an entity
        Class<?> entityClass = null;
        entityClass = Class.forName(this.entityName);
        NodeEntity[] nodeAnnotation = entityClass.getDeclaredAnnotationsByType(NodeEntity.class);
        if(nodeAnnotation.length==0){
            throw new NotAnOgmEntityException("This entity is not annotated with @NodeEntity or @RelationEntity : " + this.entityName);
        }

        this.labels = this.getLabelsFromEntity(entityClass,nodeAnnotation);

    }

    public Object resolveEntity() throws NotAnOgmEntityException, GraphIdException {
        Class<?> entityClass = null;
        //TODO : garder le controle de l'existence des champs
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

    private List<String> getLabelsFromEntity(Class<?> classes, NodeEntity[] nodeAnnotation){
        List<String> labelsFound = new ArrayList<>();
        if(nodeAnnotation.length!=0 && !nodeAnnotation[0].label().isEmpty()){
            labelsFound.add(nodeAnnotation[0].label());
        } else {
            labelsFound.add(this.entityName.split("\\.")[entityName.split("\\.").length - 1]);
            Class<?> currentClass = classes;
            while(currentClass.getSuperclass()!=null){
                if(!"Object".equals(currentClass.getSuperclass().getSimpleName())) {
                    labelsFound.add(currentClass.getSuperclass().getSimpleName());
                }
                currentClass = currentClass.getSuperclass();

            }

        }
        return labelsFound;
    }


    public List<String> getLabels() {
        return labels;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }


    public List<OgmProperty> getProperties() {
        return properties;
    }

    public List<OgmProperty> getWhereConditions() {
        return whereConditions;
    }
}
