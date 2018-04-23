package org.liquigraph.ogm;

import org.liquigraph.core.model.Changeset;
import org.liquigraph.core.model.Checksums;
import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.liquigraph.ogm.schema.Action;
import org.liquigraph.ogm.schema.EntityChangeset;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LiquigraphOgmPreprocessor {

    public List<Changeset> preprocess(File migration) throws NotAnOgmEntityException, GraphIdException, ClassNotFoundException {
        OgmCypherTranslator ogmCypherTranslator = new OgmCypherTranslator();
        EntityChangeset changesetFile = JAXB.unmarshal(migration, EntityChangeset.class);
        List<Changeset> changesets = new ArrayList<>();
        List<String> queries = new ArrayList<>();
        //TODO : un changeset doit t'il être le container de plusieurs queries?
        // 1 EntityChangeset = 1 changeset?
        for (Action action : changesetFile.getActionList()) {
            Operation operation = ActionToOperation.convert(action);
            queries.add(ogmCypherTranslator.translate(operation).getStatement());
        }

        Changeset changeset = new Changeset();
        changeset.setId("hello-world");
        changeset.setRunOnChange(false);
        changeset.setRunAlways(false);
        changeset.setQueries(queries);
        changeset.setPrecondition(null);
        changeset.setPostcondition(null);
        changeset.setContexts(null);
        changeset.setChecksum(Checksums.checksum(queries));
        changeset.setAuthor("you");
        changesets.add(changeset);
        return changesets;
    }
}
