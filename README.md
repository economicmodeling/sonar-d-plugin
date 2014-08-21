sonar-d-plugin
==============

SonarQube plugin for the D programming language

# Installation
### Requirements
* A JDK
* [Maven 3](http://maven.apache.org/download.cgi)
* [D Scanner](https://github.com/Hackerpilot/Dscanner)
* [SonarQube](http://www.sonarqube.org/downloads/) (Only tested with 4.4)
* [SonarQube Runner](http://www.sonarqube.org/downloads/) (Only tested with 2.4)

### Instructions
* Run ```mvn clean install``` in the sonar-d-plugin directory. This will compile
and package the plugin.
* Copy the generated jar file from sonar-d-plugin/target/sonar-d-plugin-0.1-SNAPSHOT.jar
to sonarqube-4.4/extensions/plugins/.
* Start SonarQube.

# Use
<ul>
<li>Create a file called "sonar-project.properties" in your D project's root
directory, following the directions <a href="http://docs.codehaus.org/display/SONAR/Analyzing+with+SonarQube+Runner">here</a>. For example:
<pre>sonar.projectKey=dscanner
sonar.projectName=D Scanner
sonar.projectVersion=1.0
sonar.sourceEncoding=UTF-8
sonar.sources=.</pre>
</li>
<li>Execute dscanner to create a report file. <code>dscanner --report . > dscanner-report.json</code></li>
<li>Run <code>sonar-runner</code> in this directory to upload analysis results to the
SonarQube server.</li>
</ul>
