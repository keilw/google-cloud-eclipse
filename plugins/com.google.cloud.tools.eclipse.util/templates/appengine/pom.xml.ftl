<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <packaging>war</packaging>
  <version>0.1.0-SNAPSHOT</version>

  <groupId>${groupId}</groupId>
  <artifactId>${artifactId}</artifactId>

  <properties>
    <appengine.version>1.9.48</appengine.version>
    <appengine.maven.plugin.version>1.0.0</appengine.maven.plugin.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
    <maven.compiler.showDeprecation>true</maven.compiler.showDeprecation>
    <archiveClasses>true</archiveClasses>
  </properties>

  <prerequisites>
    <maven>3.3.9</maven>
  </prerequisites>

  <dependencies>
    <!-- Compile/runtime dependencies -->
    <dependency>
      <groupId>com.google.appengine</groupId>
      <artifactId>appengine-api-1.0-sdk</artifactId>
      <version>${r"${appengine.version}"}</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
 
    <!-- Test Dependencies -->
    <dependency>
      <groupId>com.google.appengine</groupId>
      <artifactId>appengine-testing</artifactId>
      <version>${r"${appengine.version}"}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.google.appengine</groupId>
      <artifactId>appengine-api-stubs</artifactId>
      <version>${r"${appengine.version}"}</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>com.google.appengine</groupId>
      <artifactId>appengine-tools-sdk</artifactId>
      <version>${r"${appengine.version}"}</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>com.google.truth</groupId>
      <artifactId>truth</artifactId>
      <version>0.30</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-all</artifactId>
      <version>1.10.19</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <!-- for hot reload of the web application-->
    <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/classes</outputDirectory>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>versions-maven-plugin</artifactId>
        <version>2.3</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <goals>
              <goal>display-dependency-updates</goal>
              <goal>display-plugin-updates</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>2.6</version> <!-- required for Eclipse Mars -->
        <configuration>
          <archiveClasses>true</archiveClasses>
          <webResources>
            <!-- in order to interpolate version from pom into appengine-web.xml -->
            <resource>
              <directory>${basedir}/src/main/webapp/WEB-INF</directory>
              <filtering>true</filtering>
              <targetPath>WEB-INF</targetPath>
            </resource>
          </webResources>
        </configuration>
      </plugin>

      <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>appengine-maven-plugin</artifactId>
        <version>${r"${appengine.maven.plugin.version}"}</version>
      </plugin>
    </plugins>
  </build>
</project>
