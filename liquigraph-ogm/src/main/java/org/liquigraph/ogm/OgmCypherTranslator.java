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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OgmCypherTranslator {

    public CypherQuery translate(Operation operation) throws NotAnOgmEntityException, GraphIdException {
        if("CREATE".equals(operation.getOperationType())) {
            String createQuery = "CREATE (n:" + operation.getLabel() + ") SET n = $props";
            Map props = new HashMap<>();
            Map properties = new HashMap<>();


            props.put("props", properties);

            for (OgmProperty property : operation.properties) {
                properties.put(property.getName(), property.getValue());
            }
            CypherQuery cypherQuery = new CypherQuery(createQuery, props);
            return cypherQuery;
        } else if("UPDATE".equals(operation.getOperationType())) {
            String createQuery = "MATCH (n {id : \"placeholder\") SET n = $props";
            Map props = new HashMap<>();
            Map properties = new HashMap<>();

            props.put("props", properties);

            for (OgmProperty property : operation.properties) {
                properties.put(property.getName(), property.getValue());
            }
            CypherQuery cypherQuery = new CypherQuery(createQuery, props);
            return cypherQuery;

        } else {
            throw new UnsupportedOperationException();
        }

    }


}
