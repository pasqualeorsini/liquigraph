package org.liquigraph.ogm.schema;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso({Insert.class,Update.class})
@XmlTransient
public interface Action {
}
