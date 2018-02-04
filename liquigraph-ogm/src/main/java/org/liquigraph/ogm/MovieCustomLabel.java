package org.liquigraph.ogm;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;


@NodeEntity(label = "Custom")
public class MovieCustomLabel {

    @GraphId
    private Long id = null;

    private String title;

    private String language;


    public MovieCustomLabel() {
    }

    public MovieCustomLabel(String title, String language) {
        this.title = title;
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
