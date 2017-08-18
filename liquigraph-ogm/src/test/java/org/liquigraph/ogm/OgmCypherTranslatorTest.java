package org.liquigraph.ogm;

import org.junit.Test;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class OgmCypherTranslatorTest {

    @Test
    public void translates_insert_action_to_Cypher() throws NotAnOgmEntityException, GraphIdException {
        ArrayList<OgmProperty> properties = new ArrayList<>();
        properties.add(new OgmProperty("title","Matrix"));
        InsertOperation insert = new InsertOperation("org.liquigraph.ogm.Movie", properties);

        String cypher = new OgmCypherTranslator().translate(insert);

        assertThat(cypher).isEqualTo("CREATE (:Movie {title:'Matrix'}) ");
    }

    private OgmProperty property(String name, Object value) {
        return new OgmProperty(name, value);
    }
}
