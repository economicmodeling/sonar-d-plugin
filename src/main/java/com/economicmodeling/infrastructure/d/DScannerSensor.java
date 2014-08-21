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

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper;

    public DScannerSensor(final FileSystem fileSystem, final ResourcePerspectives resourcePerspectives) {
        this.fileSystem = fileSystem;
        this.resourcePerspectives = resourcePerspectives;
        mapper = new ObjectMapper();
    }

    @Override
    public String toString() {
        return "DScannerSensor";
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        final InputFile reportFile = fileSystem.inputFile(fileSystem.predicates()
                .hasRelativePath("./dscanner-report.json"));
        if (reportFile != null) {
            LOG.info("Analyzing dscanner-report.json");
            try {
                final DScannerReport report = mapper.readValue(reportFile.file(), DScannerReport.class);
                LOG.info("Found " + String.valueOf(report.issues.size()) + " issues.");
                for (final DScannerIssue scannerIssue : report.issues)
                {
                    final Resource resource = File.fromIOFile(
                            fileSystem.inputFile(fileSystem.predicates().hasRelativePath(scannerIssue.fileName)).file(),
                            project);
                    final Issuable issuable = resourcePerspectives.as(Issuable.class, resource);
                    final Issue issue = issuable.newIssueBuilder()
                            .line((int) scannerIssue.line)
                            .message(scannerIssue.message)
                            .ruleKey(RuleKey.of("dscanner", scannerIssue.key))
                            .build();
                    issuable.addIssue(issue);
                }
            } catch (IOException e) {
                LOG.error("Could not open dscanner-report.json", e);
            }
        }
    }

    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }
}
