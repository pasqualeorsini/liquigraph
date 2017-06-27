package org.liquigraph.ogm;

import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.neo4j.ogm.request.Statement;

public class OgmCypherTranslator {

    public String translate(InsertOperation insert) throws NotAnOgmEntityException, GraphIdException {
        Object entity = insert.resolveEntity();
        Statement statement = compileStatement(entity);
        return normalizeCypherQUery(statement);
    }

    private Statement compileStatement(Object entity) {
        // TODO: use OGM compiler to get all statements
        // make sure there is only one
        // return it
        return null;
    }

    private String normalizeCypherQUery(Statement statement) {
        // TODO: un-parameterize the statement
        // "easy": only 1 row to insert !
        // UNWIND {rows} CREATE -----> CREATE
        return null;
    }
}
