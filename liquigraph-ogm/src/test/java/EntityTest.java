import org.junit.Test;
import org.liquigraph.ogm.schema.*;

import javax.swing.text.html.parser.Entity;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EntityTest {

    @Test
    public void unmarshallInsert() {
        EntityChangeset entityChangeset = JAXB.unmarshal(getClass().getResourceAsStream("insert_entity.xml"), EntityChangeset.class);
        Insert insert = (Insert) entityChangeset.getActionList().get(0);
        assertEquals("org.liquigraph.ogm.entity.Movie", insert.getEntity());
        assertEquals(3, insert.getProperties().size());
    }

    @Test
    public void unmarshallUpdate() throws JAXBException {
        EntityChangeset changeset = JAXB.unmarshal(getClass().getResourceAsStream("update_entity.xml"), EntityChangeset.class);
        Update update = (Update) changeset.getActionList().get(0);
        assertEquals("42", update.getId());
        assertEquals(2, update.getPropertyList().size());
    }

    @Test
    public void marshallInsert() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(EntityChangeset.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Insert insert1 = new Insert();
        ArrayList<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setName("title");
        property.setValue("Batman");
        Property property1 = new Property();
        property1.setName("language");
        property1.setValue("French");

        properties.add(property);
        properties.add(property1);
        insert1.setProperties(properties);

        Update update = new Update();
        update.setId("12");
        update.setPropertyList(properties);

        EntityChangeset entityChangeset = new EntityChangeset();
        List<Action> actionList = new ArrayList<>();
        actionList.add(insert1);
        actionList.add(update);
        entityChangeset.setActionList(actionList);


        marshaller.marshal(entityChangeset, System.out);

    }
}