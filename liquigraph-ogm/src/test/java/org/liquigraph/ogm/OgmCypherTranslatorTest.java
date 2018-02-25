package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.Property;
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

        List<Property> entityProperties = new ArrayList<>();
        entityProperties.add(new Property("title","Matrix"));
        entityProperties.add(new Property("language","english"));

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.entity.Movie", entityProperties,null,"CREATE"));
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
        List<Property> entityProperties = new ArrayList<>();
        entityProperties.add(new Property("id","1"));

        entityProperties.add(new Property("title","Matrix"));
        entityProperties.add(new Property("language","english"));

        List<Property> whereProperties = new ArrayList<>();
        whereProperties.add(new Property("language","English"));

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.entity.Movie", entityProperties, whereProperties,"UPDATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testDeleteOperation() throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        List<Property> whereProperties = new ArrayList<>();
        whereProperties.add(new Property("language","English"));
        whereProperties.add(new Property("title","Matrix"));

        CypherQuery expectedQuery = new CypherQuery("MATCH (n:Movie { \"language\":\"English\", \"title\":\"Matrix\" } DELETE n",new HashMap<>());
        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.entity.Movie", null, whereProperties, "DELETE"));

        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testInsertWithCustomLabel() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("props",new HashMap<>());
        CypherQuery expectedQuery = new CypherQuery("CREATE (n:Custom) SET n = $props", parameters);

        List<Property> entityProperties = new ArrayList<>();

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.entity.MovieCustomLabel", entityProperties, null,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void testInsertWithParentLabel() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("props",new HashMap<>());
        CypherQuery expectedQuery = new CypherQuery("CREATE (n:MovieWithParent:Media) SET n = $props", parameters);

        List<Property> entityProperties = new ArrayList<>();

        CypherQuery actual = operationTranslator.translate(new Operation("org.liquigraph.ogm.entity.MovieWithParent", entityProperties, null,"CREATE"));
        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }
}
