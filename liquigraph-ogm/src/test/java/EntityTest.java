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
        assertEquals(2, update.getPropertyList().size());
        assertEquals(1,update.getWhere().getPropertyList().size());
    }

    @Test
    public void unmarshallDelete() throws JAXBException {
        EntityChangeset changeset = JAXB.unmarshal(getClass().getResourceAsStream("delete_entity.xml"), EntityChangeset.class);
        Delete delete = (Delete) changeset.getActionList().get(0);
        assertEquals(1, delete.getWhere().getPropertyList().size());
    }



}
