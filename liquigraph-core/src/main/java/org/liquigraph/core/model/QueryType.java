package org.liquigraph.core.model;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import java.util.Collection;

public class QueryType {


    @XmlMixed
    @XmlElementRefs({
        @XmlElementRef(name = "template", type=Template.class),
        @XmlElementRef(name = "parameters", type = QueryParameters.class)
    })
    Collection<Object> content;
}
