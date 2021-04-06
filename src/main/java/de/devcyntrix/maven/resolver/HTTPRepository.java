package de.devcyntrix.maven.resolver;

import de.devcyntrix.maven.resolver.api.Artifact;
import de.devcyntrix.maven.resolver.api.Repository;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.SnapshotVersion;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

public class HTTPRepository implements Repository {

    private static final MetadataXpp3Reader reader = new MetadataXpp3Reader();
    private final URL url;
    private final Consumer<URLConnection> configureConnection;

    public HTTPRepository(String url) throws MalformedURLException {
        this(url, urlConnection -> { });
    }

    public HTTPRepository(String url, Consumer<URLConnection> configureConnection) throws MalformedURLException {
        url = url.trim();
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        this.url = new URL(url);
        this.configureConnection = configureConnection;
    }

    @Override
    public @NotNull URLConnection newConnection(@NotNull String path) throws IOException {
        URLConnection urlConnection = new URL(this.url.toString() + "/" + path).openConnection();
        this.configureConnection.accept(urlConnection);
        return urlConnection;
    }

    @Override
    public @Nullable Metadata resolveMetadata(@NotNull String groupId, @NotNull String artifactId, @Nullable String version) {
        String url = formatUrl(groupId, artifactId, version, "maven-metadata.xml");
        try {
            URLConnection urlConnection = newConnection(url);
            return reader.read(urlConnection.getInputStream());
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Artifact resolveArtifact(@NotNull String groupId, @NotNull String artifactId) {
        Metadata metadata = resolveMetadata(groupId, artifactId);
        if (metadata == null)
            return null;
        return new MavenArtifact(this, groupId, artifactId, metadata);
    }

    @Override
    public @Nullable InputStream resolveArtifact(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String classifier, @NotNull String extension) {

        String artifactVersion = version;
        // SNAPSHOT
        if (MavenUtils.isSnapshot(version)) {
            Metadata metadata = resolveMetadata(groupId, artifactId, version);
            if (metadata == null || metadata.getVersioning() == null || metadata.getVersioning().getSnapshotVersions() == null) {
                return null;
            }

            @NotNull String finalExtension = extension;
            SnapshotVersion snapshotVersion = metadata.getVersioning().getSnapshotVersions().stream()
                    .filter(sv -> sv.getExtension().equalsIgnoreCase(finalExtension))
                    .filter(sv -> (sv.getClassifier() == null || sv.getClassifier().isEmpty()) || (classifier != null && sv.getClassifier().equalsIgnoreCase(classifier)))
                    .findAny().orElse(null);
            if (snapshotVersion == null) {
                return null;
            }
            artifactVersion = snapshotVersion.getVersion();
            extension = snapshotVersion.getExtension();
        }

        String path = artifactId + "-" + artifactVersion + (classifier != null ? "-" + classifier : "") + "." + extension;
        String uri = formatUrl(groupId, artifactId, version, path);

        try {
            URLConnection urlConnection = newConnection(uri);
            return urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatUrl(@NotNull String groupId, @NotNull String artifactId, @Nullable String version, @NotNull String path) {
        // Group Id/Artifact Id
        String format = "%s/%s";
        if (version != null)
            format += "/%s";
        format += "/" + path;

        String uri = null;
        if (version == null) {
            uri = String.format(format, groupId.replace('.', '/'), artifactId);
        } else {
            uri = String.format(format, groupId.replace('.', '/'), artifactId, version);
        }
        return uri;
    }
}
