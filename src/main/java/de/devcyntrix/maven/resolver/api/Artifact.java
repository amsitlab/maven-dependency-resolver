package de.devcyntrix.maven.resolver.api;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Date;
import java.util.stream.Stream;

public interface Artifact {

    @NotNull String getGroupId();

    @NotNull String getArtifactId();

    @Nullable Date lastUpdatedAt();

    @NotNull Stream<String> getVersions();

    @Nullable String getLatestVersion();

    @Nullable String getReleaseVersion();

    @NotNull Metadata getMetadata();

    @Nullable InputStream download(@NotNull String version, @Nullable String classifier, @NotNull String extension);

}
