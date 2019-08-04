package org.liquigraph.core.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Template {
    @XmlValue
    private String content;

}
