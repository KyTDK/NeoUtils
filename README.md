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
NeoUtils REQUIRES adventure, please include it like so and make sure it is shaded in your plugin, relocate as well
```xml
<dependency>
    <artifactId>adventure-text-minimessage</artifactId>
    <version>4.11.0</version>
</dependency>
```
