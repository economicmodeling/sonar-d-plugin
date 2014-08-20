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
* Create a file called "sonar-project.properties" in your project's root
directory, following the directions (here)[http://docs.codehaus.org/display/SONAR/Analyzing+with+SonarQube+Runner]
* Execute dscanner to create a report file. ```dscanner -S . > dscanner-report.txt```
* Run ```sonar-runner``` in this directory to upload analysis results to the
SonarQube server.
