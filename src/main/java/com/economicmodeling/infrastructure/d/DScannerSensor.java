/**
 * Copyright: © 2014 Economic Modeling Specialists, Intl.
 *
 * This file is part of sonar-d-plugin.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sonar-d-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sonar-d-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.economicmodeling.infrastructure.d;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brian Schott
 */
public class DScannerSensor implements Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DScannerSensor.class);

    private final FileSystem fileSystem;
    private final ResourcePerspectives resourcePerspectives;

    public DScannerSensor(final FileSystem fileSystem, final ResourcePerspectives resourcePerspectives) {
        this.fileSystem = fileSystem;
        this.resourcePerspectives = resourcePerspectives;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("DScannerSensor.analyse");
        final InputFile reportFile = fileSystem.inputFile(fileSystem.predicates()
                .hasRelativePath("./dscanner-report.txt"));
        if (reportFile != null) {
            try {
                final List<String> lines = FileUtils.readLines(reportFile.file());
                for (String line :  lines) {
//                    LOG.info(line);
                    final Pattern pattern = Pattern.compile("^(.+)\\((\\d+):(\\d+)\\)\\[(\\w+)\\]: (.+)$");
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        final String fileName = matcher.group(1);
                        final String lineNumber = matcher.group(2);
                        final String columnNumber = matcher.group(3);
                        final String level = matcher.group(4);
                        final String message = matcher.group(5);
//                        LOG.info(String.format("fileName = %s\t lineNumber = %s\tcolumnNumber = %s\tlevel = %s\tmessage = %s",
//                                fileName, lineNumber, columnNumber, level, message));
                        final Resource resource = File.fromIOFile(
                                fileSystem.inputFile(fileSystem.predicates().hasRelativePath(fileName)).file(),
                                project);
                        final Issuable issuable = resourcePerspectives.as(Issuable.class, resource);
                        final Issue issue = issuable.newIssueBuilder()
                                .line(Integer.decode(lineNumber))
                                .message(message)
                                .severity(Severity.INFO)
                                .ruleKey(RuleKey.of("dscanner", "dscanner"))
                                .build();
                        issuable.addIssue(issue);
                        LOG.info("Issue added for " + line);
                    }
                }
            } catch (IOException e) {
                LOG.error("Could not open dscanner-report.txt", e);
            }
        }
    }

    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }
}
