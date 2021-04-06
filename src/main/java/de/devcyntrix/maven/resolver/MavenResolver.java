package de.devcyntrix.maven.resolver;

import de.devcyntrix.maven.resolver.api.Artifact;
import de.devcyntrix.maven.resolver.api.Repository;
import de.devcyntrix.maven.resolver.api.Resolver;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

public class MavenResolver implements Resolver {

    private final Repository[] repositories;

    public MavenResolver(Repository... repositories) {
        this.repositories = repositories;
    }

    @Override
    public Stream<Repository> getRepositories() {
        return Arrays.stream(repositories);
    }

    @Override
    public @Nullable Metadata resolveMetadata(@NotNull String groupId, @NotNull String artifactId, @Nullable String version) {
        return getRepositories().map(repository -> repository.resolveMetadata(groupId, artifactId, version)).findAny().orElse(null);
    }

    @Override
    public @Nullable Artifact resolveArtifact(@NotNull String groupId, @NotNull String artifactId) {
        return getRepositories().map(repository -> repository.resolveArtifact(groupId, artifactId)).findAny().orElse(null);
    }

    @Override
    public @Nullable InputStream resolveArtifact(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String classifier, @NotNull String extension) {
        return getRepositories().map(repository -> repository.resolveArtifact(groupId, artifactId, version, classifier, extension)).findAny().orElse(null);
    }
}
