# NeoUtils
Utils for spigot development

For details on this API read the Wiki page here https://github.com/KyTDK/NeoUtils/wiki

HOW TO INSTALL

For maven
```xml
<repository>
    <id>neomechanical-snapshots</id>
    <url>https://hub.neomechanical.com/repository/maven-snapshots/</url>
</repository>
<!-- NeoUtils -->
<dependency>
    <groupId>com.neomechanical</groupId>
    <artifactId>NeoUtils</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```
RELOCATION
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.3.0</version>
    <configuration>
    <minimizeJar>true</minimizeJar> <!--If you do minimize the jar, you must apply the filter otherwise essential classes will excluded from the final jar-->
        <relocations>
            <relocation>
                <pattern>com.neomechanical.neoutils</pattern>
                <shadedPattern>com.neomechanical.neoconfig.neoutils</shadedPattern>
                </relocation>
        </relocations>
        <filters>
            <filter>
                <artifact>*:*</artifact>
                <excludeDefaults>false</excludeDefaults>
                <includes>
                    <include>com/neomechanical/neoutils/**</include>
                </includes>
            </filter>
        </filters>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
             <goals>
                 <goal>shade</goal>
              </goals>
        </execution>
    </executions>
</plugin>
```
