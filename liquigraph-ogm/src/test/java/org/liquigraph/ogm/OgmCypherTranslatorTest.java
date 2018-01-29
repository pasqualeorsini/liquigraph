package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.neo4j.ogm.cypher.query.CypherQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OgmCypherTranslatorTest {

    @Test
    public void testInsertOperation() throws NotAnOgmEntityException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        Map<String, Object> parameters = new HashMap<>();
        Map<String,String> properties = new HashMap<>();
        properties.put("title","Matrix");
        properties.put("language","english");
        parameters.put("props",properties);

        CypherQuery expectedQuery = new CypherQuery("CREATE (n:Movie) SET n = $props",parameters);

        List<OgmProperty> entityProperties = new ArrayList<>();
        entityProperties.add(new OgmProperty("title","Matrix"));
        entityProperties.add(new OgmProperty("language","english"));

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.Movie", entityProperties,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());
    }

    @Test
    public void testUpdateOperation() throws NotAnOgmEntityException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        Map<String, Object> parameters = new HashMap<>();
        Map<String,String> properties = new HashMap<>();
        properties.put("title","Matrix");
        properties.put("language","english");
        parameters.put("props",properties);

        CypherQuery expectedQuery = new CypherQuery("MATCH (n {\"id\":1) SET n = $props",parameters);
        List<OgmProperty> entityProperties = new ArrayList<>();
        entityProperties.add(new OgmProperty("title","Matrix"));
        entityProperties.add(new OgmProperty("language","english"));

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.Movie", entityProperties, "UPDATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testDeleteOperation() throws NotAnOgmEntityException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        CypherQuery expectedQuery = new CypherQuery("MATCH (n {\"id\":1) DELETE n",null);

        assertEquals(expectedQuery, operationTranslator.translate(new Operation(null,null,"DELETE")));
    }
}
