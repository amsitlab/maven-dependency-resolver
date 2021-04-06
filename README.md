# Maven dependency resolver

## Snippets

```java
HTTPRepository repository = new HTTPRepository("	https://repo1.maven.org/maven2/");
Artifact artifact = repository.resolveArtifact("group id", "artifact id");

artifact.getVersions(); // A list of versions
artifact.getMetadata(); // The metadata of the artifact
artifact.getLatestVersion(); // Returns the latest version if present in metadata 
artifact.getReleaseVersion(); // Returns the release version if present in metadata 

InputStream stream = artifact.download("version", "classifier (source, javadoc, etc...)" or null, "extension (jar, pom, etc...)"); // Opens a url connection to repository server

```
