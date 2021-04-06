package de.devcyntrix.maven.resolver;

public final class MavenUtils {

    public static boolean isSnapshot(String version) {
        return version.toLowerCase().endsWith("-snapshot");
    }

}
