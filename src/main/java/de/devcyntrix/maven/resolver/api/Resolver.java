package de.devcyntrix.maven.resolver.api;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.stream.Stream;

public interface Resolver {

    Stream<Repository> getRepositories();

    default @Nullable Metadata resolveMetadata(@NotNull String groupId, @NotNull String artifactId) {
        return resolveMetadata(groupId, artifactId, null);
    }

    @Nullable Metadata resolveMetadata(@NotNull String groupId, @NotNull String artifactId, @Nullable String version);

    @Nullable Artifact resolveArtifact(@NotNull String groupId, @NotNull String artifactId);

    @Nullable InputStream resolveArtifact(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String classifier, @NotNull String extension);

}
