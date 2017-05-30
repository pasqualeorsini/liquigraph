package org.liquigraph.ogm;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OgmCypherTranslatorTest {

    @Test
    public void translates_insert_action_to_Cypher() {
        InsertOperation insert = new InsertOperation("org.liquigraph.ogm.examples.Movie", property("title", "Matrix"));

        String cypher = new OgmCypherTranslator().translate(insert);

        assertThat(cypher).isEqualTo("CREATE (:Movie {title:'Matrix'}) ");
    }

    private OgmProperty property(String name, Object value) {
        return new OgmProperty(name, value);
    }
}
