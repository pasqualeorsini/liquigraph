package org.liquigraph.ogm;

import org.liquigraph.ogm.exception.GraphIdException;
import org.liquigraph.ogm.exception.NotAnOgmEntityException;
import org.neo4j.ogm.context.EntityGraphMapper;
import org.neo4j.ogm.context.MappingContext;
import org.neo4j.ogm.cypher.compiler.CompileContext;
import org.neo4j.ogm.cypher.compiler.Compiler;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.request.Statement;
import org.neo4j.ogm.session.request.RowStatementFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        MetaData metaData = new MetaData("org.liquigraph.ogm");

        EntityGraphMapper entityGraphMapper = new EntityGraphMapper(metaData, new MappingContext(metaData));


        CompileContext compileContext = entityGraphMapper.map(entity);
        Compiler compiler = compileContext.getCompiler();

        compiler.useStatementFactory(new RowStatementFactory());
        return compiler.getAllStatements().get(0);

    }

    private String normalizeCypherQUery(Statement statement) {
        // TODO: un-parameterize the statement
        // "easy": only 1 row to insert !
        // UNWIND {rows} CREATE -----> CREATE

        String patternUnwind = ".*CREATE \\(n:`(.*?)`\\).*";
        Matcher matcher = Pattern.compile(patternUnwind).matcher(statement.getStatement());

        if(matcher.matches()){
            ArrayList rows = (ArrayList) statement.getParameters().get("rows");
            Map<String, String> props = ((Map<String, Map>) rows.get(0)).get("props");

            String attributesQuery = "{";

            for (String key : props.keySet()) {
                String value = props.get(key);
                attributesQuery += key + ":'" + value+"'";
            }
            attributesQuery += "}";
            return "CREATE (:"+matcher.group(1)+" " + attributesQuery + ") ";
        }

        return null;

    }
}
