# Maven dependency resolver

[![](https://jitpack.io/v/DevCyntrix/maven-dependency-resolver.svg)](https://jitpack.io/#DevCyntrix/maven-dependency-resolver)


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

## Repository

To use this project in your maven or gradle project use jitpack 

https://jitpack.io/#DevCyntrix/maven-dependency-resolver

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.DevCyntrix</groupId>
        <artifactId>maven-dependency-resolver</artifactId>
        <version><!-- RELEASE-VERSION --></version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.DevCyntrix:maven-dependency-resolver:Tag'
}
```