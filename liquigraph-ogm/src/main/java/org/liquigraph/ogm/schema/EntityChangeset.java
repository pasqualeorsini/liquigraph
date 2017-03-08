package org.liquigraph.ogm.schema;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlSeeAlso(Action.class)
@XmlRootElement(name = "entityChangeset")
public class EntityChangeset {
    private List<Action> actionList;

    @XmlElements({
            @XmlElement(name = "insert", type = Insert.class),
            @XmlElement(name = "update", type = Update.class)
    })
    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }
}
