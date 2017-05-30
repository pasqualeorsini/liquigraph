package org.liquigraph.ogm;

import org.liquigraph.ogm.schema.Action;
import org.liquigraph.ogm.schema.Insert;
import org.liquigraph.ogm.schema.Property;
import org.liquigraph.ogm.schema.Update;
import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.compiler.CompileContext;
import org.neo4j.ogm.compiler.Compiler;
import org.neo4j.ogm.context.EntityGraphMapper;
import org.neo4j.ogm.context.MappingContext;
import org.neo4j.ogm.request.Statement;
import org.neo4j.ogm.session.request.RowStatementFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, NoSuchFieldException {

        Movie movie = new Movie("Matrix", "English");
        MetaData metaData = new MetaData("org.liquigraph.ogm");

        EntityGraphMapper entityGraphMapper = new EntityGraphMapper(metaData, new MappingContext(metaData));



        CompileContext compileContext = entityGraphMapper.map(movie);
        Compiler compiler = compileContext.getCompiler();

        compiler.useStatementFactory(new RowStatementFactory());

        for (Statement statement : compiler.getAllStatements()) {
            System.out.println(statement.toString());
        }

        Insert insert = new Insert();
        insert.setEntity("org.liquigraph.ogm.Movie");
        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setValue("42");
        property.setName("id");
        properties.add(property);
        Property property2 = new Property();
        property2.setName("title");
        property2.setValue("King Kong");
        properties.add(property2);
        insert.setProperties(properties);
        Object objectFromAction = getObjectFromAction(insert);
        CompileContext map = entityGraphMapper.map(objectFromAction);
        Compiler compiler1 = map.getCompiler();
        compiler1.useStatementFactory(new RowStatementFactory());
        List<Statement> allStatements = compiler.getAllStatements();
        System.out.println("K");
/*
        ClassInfo classInfo = new ClassInfo(Main.class.getResourceAsStream("Movie.class"));
        System.out.println("sos");
        entityGraphMapper.map(o);
*/
    }

    public static Object getObjectFromAction(Action action) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> aClass = Class.forName(action.getEntity());
        Object entity = aClass.newInstance();

        if(action instanceof Insert){
            // Insert must not have an id field
            Insert insert = (Insert) action;
            if(hasIdProperty(insert)){
                for (Property property : insert.getProperties()) {
                    setValue(aClass, entity, property.getName(), property.getValue());
                }
            }

        } else if(action instanceof Update){
            // Update can't have an id field in properties
            Update update = (Update) action;

        }

        // For each property found we set the value on the entity
        return entity;
    }

    private  static void setValue(Class clazz, Object entity, String name, String value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = clazz.getDeclaredField(name);
        declaredField.setAccessible(true);
        if(declaredField.getType().equals(Long.class)) {
            declaredField.set(entity, Long.parseLong(value));
        } else{
            declaredField.set(entity, value);
        }
        declaredField.setAccessible(false);

    }

    private static boolean hasIdProperty(Insert insert) {
        for (Property property : insert.getProperties()) {
            if("id".equals(property.getName())){
                return true;
            }
        }
        return false;
    }
}
