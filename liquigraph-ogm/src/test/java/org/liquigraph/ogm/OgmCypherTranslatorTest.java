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
    public void testInsertOperation() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
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

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.Movie", entityProperties,null,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());
    }

    @Test
    public void testUpdateOperation() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        Map<String, Object> parameters = new HashMap<>();
        Map<String,String> properties = new HashMap<>();
        properties.put("id","1");
        properties.put("title","Matrix");
        properties.put("language","english");
        parameters.put("props",properties);

        CypherQuery expectedQuery = new CypherQuery("MATCH (n:Movie { \"language\":\"English\" }  SET n = $props",parameters);
        List<OgmProperty> entityProperties = new ArrayList<>();
        entityProperties.add(new OgmProperty("id","1"));

        entityProperties.add(new OgmProperty("title","Matrix"));
        entityProperties.add(new OgmProperty("language","english"));

        List<OgmProperty> whereProperties = new ArrayList<>();
        whereProperties.add(new OgmProperty("language","English"));

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.Movie", entityProperties, whereProperties,"UPDATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testDeleteOperation() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        List<OgmProperty> whereProperties = new ArrayList<>();
        whereProperties.add(new OgmProperty("language","English"));
        whereProperties.add(new OgmProperty("title","Matrix"));

        CypherQuery expectedQuery = new CypherQuery("MATCH (n:Movie { \"language\":\"English\", \"title\":\"Matrix\" } DELETE n",new HashMap<>());
        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.Movie", null, whereProperties, "DELETE"));

        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testInsertWithCustomLabel() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("props",new HashMap<>());
        CypherQuery expectedQuery = new CypherQuery("CREATE (n:Custom) SET n = $props", parameters);

        List<OgmProperty> entityProperties = new ArrayList<>();

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.MovieCustomLabel", entityProperties, null,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testInsertWithParentLabel() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("props",new HashMap<>());
        CypherQuery expectedQuery = new CypherQuery("CREATE (n:MovieWithParent:Media) SET n = $props", parameters);

        List<OgmProperty> entityProperties = new ArrayList<>();

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.MovieWithParent", entityProperties, null,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }
}
