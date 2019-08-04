package org.liquigraph.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement(name = "parameters")
public class QueryParameters {
    @XmlElement(name="parameter")
    private Collection<QueryParameter>  parameters;


}
