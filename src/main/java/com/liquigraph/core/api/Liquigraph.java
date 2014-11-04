package com.liquigraph.core.api;

import com.liquigraph.core.configuration.Configuration;
import com.liquigraph.core.configuration.ConfigurationBuilder;
import com.liquigraph.core.graph.ChangelogReader;
import com.liquigraph.core.graph.ChangelogWriter;
import com.liquigraph.core.graph.GraphConnector;
import com.liquigraph.core.parser.ChangelogParser;
import com.liquigraph.core.validation.DeclaredChangesetValidator;
import com.liquigraph.core.validation.PersistedChangesetValidator;

/**
 * Liquigraph facade in charge of migration execution.
 */
public final class Liquigraph {

    private final MigrationRunner migrationRunner;

    public Liquigraph() {
        migrationRunner = new MigrationRunner(
            new GraphConnector(),
            new ChangelogParser(),
            new ChangelogReader(),
            new ChangelogWriter(),
            new ChangelogDiffMaker(),
            new DeclaredChangesetValidator(),
            new PersistedChangesetValidator()
        );
    }

    /**
     * Triggers migration execution, according to the specified {@link Configuration}
     * instance.
     *
     * @param configuration configuration of the changelog location and graph connection parameters
     * @see ConfigurationBuilder to create {@link Configuration instances}
     */
    public void runMigrations(Configuration configuration) {
        migrationRunner.runMigrations(configuration);
    }
}