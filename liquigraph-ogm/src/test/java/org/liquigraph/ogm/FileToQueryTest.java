package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.*;
import org.neo4j.ogm.cypher.query.CypherQuery;

import javax.xml.bind.JAXB;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FileToQueryTest {

    @Test
    public void insertOperation() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        Map<String, Object> parameters = new HashMap<>();
        Map<String,String> properties = new HashMap<>();
        properties.put("title","Matrix");
        properties.put("language","English");
        properties.put("id","42");
        parameters.put("props",properties);

        CypherQuery expectedQuery = new CypherQuery("CREATE (n:Movie) SET n = $props",parameters);


        EntityChangeset entityChangeset = JAXB.unmarshal(getClass().getResourceAsStream("/insert_entity.xml"), EntityChangeset.class);
        Insert insert = (Insert) entityChangeset.getActionList().get(0);
        Operation insertOperation = new Operation(insert);
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();
        CypherQuery actual = operationTranslator.translate(insertOperation);

        assertEquals(expectedQuery.getStatement(),actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void updateOperation() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        OgmCypherTranslator operationTranslator = new OgmCypherTranslator();

        Map<String, Object> parameters = new HashMap<>();
        Map<String,String> properties = new HashMap<>();
        properties.put("title","Matrix 2");
        properties.put("language","English");
        parameters.put("props",properties);

        CypherQuery expectedQuery = new CypherQuery("MATCH (n:Movie { \"language\":\"English\" }  SET n = $props",parameters);

        EntityChangeset entityChangeset = JAXB.unmarshal(getClass().getResourceAsStream("/update_entity.xml"), EntityChangeset.class);
        Update update = (Update) entityChangeset.getActionList().get(0);
        Operation operation = new Operation(update);
        OgmCypherTranslator ogmCypherTranslator = new OgmCypherTranslator();
        CypherQuery actual = ogmCypherTranslator.translate(operation);

        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }

    @Test
    public void deleteOperation() throws NotAnOgmEntityException, ClassNotFoundException, GraphIdException {
        EntityChangeset entityChangeset = JAXB.unmarshal(getClass().getResourceAsStream("/delete_entity.xml"), EntityChangeset.class);
        Delete delete = (Delete) entityChangeset.getActionList().get(0);
        Operation operation = new Operation(delete);
        OgmCypherTranslator ogmCypherTranslator = new OgmCypherTranslator();
        CypherQuery actual = ogmCypherTranslator.translate(operation);

        CypherQuery expectedQuery = new CypherQuery("MATCH (n:Movie { \"language\":\"English\", \"title\":\"Matrix\" } DELETE n",new HashMap<>());

        assertEquals(expectedQuery.getStatement(), actual.getStatement());
        assertEquals(expectedQuery.getParameters(),actual.getParameters());

    }
}
