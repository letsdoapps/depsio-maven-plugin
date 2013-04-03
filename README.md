Deps.io Maven Plugin
====================

This is the maven plugin for integrating apps with [Deps.io](http://deps.io).

Build integration only requires adding a few lines to your pom.xml. When packaging your application for deployment, your pom.xml will be POSTED to Deps.io to update project dependencies.

Installation
------------

Add these lines to your application's pom.xml:

    <project>
      ...
      <plugins>
        ...
        <plugin>
          <groupId>com.letsdoapps.depsio.maven</groupId>
          <artifactId>depsio-maven-plugin</artifactId>
          <configuration>
            <appkey>YOUR_APPLICATION_KEY</appkey>
          </configuration>
          <executions>
            <execution>
              <phase>package</phase>
              <goals>
                <goal>depsio</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        ...
      </plugins>
      ...
    </project>

The default behaviour of the plugin is to only update the project on **package** build phase. You can switch it to **compile** or **deploy** at will to adapt to your workflow.

### License

MIT License. Copyright 2013 LetsDoApps. [http://letsdoapps.com/](http://letsdoapps.com/)
