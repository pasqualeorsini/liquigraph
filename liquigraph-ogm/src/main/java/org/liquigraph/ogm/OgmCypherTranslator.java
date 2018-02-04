package org.liquigraph.ogm;

import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.neo4j.ogm.context.EntityGraphMapper;
import org.neo4j.ogm.context.MappingContext;
import org.neo4j.ogm.cypher.compiler.CompileContext;
import org.neo4j.ogm.cypher.compiler.Compiler;
import org.neo4j.ogm.cypher.query.CypherQuery;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.request.Statement;
import org.neo4j.ogm.session.request.RowStatementFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OgmCypherTranslator {

    public CypherQuery translate(Operation operation) throws NotAnOgmEntityException, GraphIdException {
        if("CREATE".equals(operation.getOperationType())) {
            String createQuery = "CREATE (n" + formatLabels(operation.getLabels()) + ") SET n = $props";
            Map props = new HashMap<>();
            Map properties = new HashMap<>();

            props.put("props", properties);

            for (OgmProperty property : operation.getProperties()) {
                properties.put(property.getName(), property.getValue());
            }
            return new CypherQuery(createQuery, props);
        } else if("UPDATE".equals(operation.getOperationType())) {
            Map props = new HashMap<>();
            Map properties = new HashMap<>();

            props.put("props", properties);

            for (OgmProperty property : operation.getProperties()) {
                properties.put(property.getName(), property.getValue());
            }

            String createQuery = "MATCH (n"+formatLabels(operation.getLabels())+" "+formatWhereCondition(operation.getWhereConditions())+"  SET n = $props";
            return new CypherQuery(createQuery, props);
        } else if("DELETE".equals(operation.getOperationType())){
            Map props = new HashMap<>();

            String deleteQuery = "MATCH (n"+formatLabels(operation.getLabels())+" "+formatWhereCondition(operation.getWhereConditions())+" DELETE n";
            return new CypherQuery(deleteQuery, props);
        } else {
            throw new UnsupportedOperationException();
        }

    }

    private String formatLabels(List<String> labels){
        StringBuilder formattedString = new StringBuilder();
        for (String label : labels) {
            formattedString.append(":"+label);
        }
        return formattedString.toString();
    }

    private String formatWhereCondition(List<OgmProperty> where){
        StringBuilder formattedString = new StringBuilder();
        formattedString.append("{ ");
        for (int i = 0;i<where.size();i++){
            OgmProperty ogmProperty = where.get(i);
            if(i!=0)
                formattedString.append(", \""+ogmProperty.getName()+"\":\""+ogmProperty.getValue()+"\"");
            else
                formattedString.append("\""+ogmProperty.getName()+"\":\""+ogmProperty.getValue()+"\"");

        }
        formattedString.append(" }");
        return formattedString.toString();
    }



}
