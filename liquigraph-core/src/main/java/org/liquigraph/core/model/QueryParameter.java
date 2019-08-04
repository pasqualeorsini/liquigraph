package org.liquigraph.core.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public class QueryParameter {
    @XmlAttribute(name="name")
    String name;

    @XmlElement(name="value")
    Collection<String> values;

}
