package de.devcyntrix.maven.resolver;

import de.devcyntrix.maven.resolver.api.Artifact;
import de.devcyntrix.maven.resolver.api.Repository;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Stream;

public class MavenArtifact implements Artifact {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private final Repository repository;
    private final String groupId, artifactId;
    private final Metadata metadata;

    public MavenArtifact(Repository repository, String groupId, String artifactId, Metadata metadata) {
        this.repository = repository;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.metadata = metadata;
    }

    @Override
    public @NotNull String getGroupId() {
        return this.groupId;
    }

    @Override
    public @NotNull String getArtifactId() {
        return this.artifactId;
    }

    @Override
    public Date lastUpdatedAt() {
        Versioning versioning = metadata.getVersioning();
        if(versioning == null)
            return null;
        String lastUpdated = versioning.getLastUpdated();
        if(lastUpdated == null || lastUpdated.isEmpty())
            return null;
        try {
            return dateFormat.parse(lastUpdated);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public @NotNull Stream<String> getVersions() {
        Versioning versioning = metadata.getVersioning();
        if(versioning == null)
            return Stream.empty();
        return versioning.getVersions().stream();
    }

    @Override
    public @Nullable String getLatestVersion() {
        Versioning versioning = metadata.getVersioning();
        if(versioning == null)
            return null;
        return versioning.getLatest();
    }

    @Override
    public @Nullable String getReleaseVersion() {
        Versioning versioning = metadata.getVersioning();
        if(versioning == null)
            return null;
        return versioning.getRelease();
    }

    @Override
    public @NotNull Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public InputStream download(@NotNull String version, @Nullable String classifier, @NotNull String extension) {
        return this.repository.resolveArtifact(groupId, artifactId, version, classifier, extension);
    }
}
