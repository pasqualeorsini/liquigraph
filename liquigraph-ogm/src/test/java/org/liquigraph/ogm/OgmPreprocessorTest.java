package org.liquigraph.ogm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.liquigraph.core.model.Changeset;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class OgmPreprocessorTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void translates_ogm_migration_to_Cypher() throws IOException {
        File migration = file("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<changelog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "           xsi:noNamespaceSchemaLocation=\"http://www.liquigraph.org/schema/1.0/liquigraph.xsd\"\n" +
                "           xmlns:ogm=\"http://todo.com\">\n" +
                "    \n" +
                "    <changeset id=\"hello-world\" author=\"you\">\n" +
                "        <ogm:insert entity=\"com.foo.Movie\">\n" +
                "            <ogm:property name=\"title\">Matrix</ogm:property>\n" +
                "        </ogm:insert>\n" +
                "    </changeset>\n" +
                "</changelog>");

        List<Changeset> statements = new LiquigraphOgmPreprocessor().preprocess(migration);

        assertThat(statements)
                .containsExactly(changeset("hello-world", "you", "CREATE (:Movie {title:'Matrix'}) "));
    }

    private File file(String s) throws IOException {
        File file = temp.newFile();
        Files.write(file.toPath(), s.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    private Changeset changeset(String id, String author, String query) {
        Changeset changeset = new Changeset();
        changeset.setId(id);
        changeset.setAuthor(author);
        changeset.setQueries(Collections.singletonList(query));
        return changeset;
    }
}
