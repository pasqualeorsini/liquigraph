package org.liquigraph.ogm.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Delete extends Action {
    private Where where;

    @XmlElement(name = "where")
    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

}
