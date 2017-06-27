package org.liquigraph.ogm;

public class OgmProperty {
    private final String name;
    private final Object value;

    public OgmProperty(String name, Object value) {

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
