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
import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.MetricFinder;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;

import java.io.IOException;

/**
 * @author Brian Schott
 */
public class DScannerSensor implements Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DScannerSensor.class);

    private final FileSystem fileSystem;
    private final ResourcePerspectives resourcePerspectives;
    private final ObjectMapper mapper;
    private final Settings settings;

    public DScannerSensor(final FileSystem fileSystem, final ResourcePerspectives resourcePerspectives, final Settings settings) {
        this.fileSystem = fileSystem;
        this.resourcePerspectives = resourcePerspectives;
        this.settings = settings;
        mapper = new ObjectMapper();
    }

    @Override
    public String toString() {
        return "DScannerSensor";
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        final String sources = settings.getProperties().get("sonar.sources");
        final InputFile reportFile = fileSystem.inputFile(fileSystem.predicates()
                .hasRelativePath(sources + "/dscanner-report.json"));
        if (reportFile != null) {
            LOG.info("Analyzing dscanner-report.json");
            try {
                final DScannerReport report = mapper.readValue(reportFile.file(), DScannerReport.class);
                sensorContext.saveMeasure(CoreMetrics.CLASSES, report.classCount);
                sensorContext.saveMeasure(CoreMetrics.FUNCTIONS, report.functionCount);
                sensorContext.saveMeasure(CoreMetrics.LINES, report.lineOfCodeCount);
                sensorContext.saveMeasure(DMetrics.STRUCTS, report.structCount);
                sensorContext.saveMeasure(DMetrics.TEMPLATES, report.templateCount);
                sensorContext.saveMeasure(DMetrics.INTERFACES, report.interfaceCount);
                sensorContext.saveMeasure(CoreMetrics.STATEMENTS, report.statementCount);
                //sensorContext.saveMeasure(CoreMetrics.PUBLIC_UNDOCUMENTED_API, report.undocumentedPublicSymbols);
                LOG.info("Found " + String.valueOf(report.issues.size()) + " issues.");
                for (final DScannerIssue scannerIssue : report.issues)
                {
                    final InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(scannerIssue.fileName));
                    if (inputFile == null) {
                        LOG.info("Could not find file " + scannerIssue.fileName);
                        continue;
                    }
                    final Resource resource = File.fromIOFile(inputFile.file(), project);
                    final Issuable issuable = resourcePerspectives.as(Issuable.class, resource);
                    final Issue issue = issuable.newIssueBuilder()
                            .line((int) scannerIssue.line)
                            .message(scannerIssue.message)
                            .ruleKey(RuleKey.of("dscanner", scannerIssue.key))
                            .build();
                    issuable.addIssue(issue);
                }
            } catch (IOException e) {
                LOG.error("Could not open file", e);
            }
        } else {
            LOG.error("Could not open dscanner-report.json");
        }
    }

    public boolean shouldExecuteOnProject(Project project) {
        return fileSystem.files(fileSystem.predicates().hasLanguage("d")).iterator().hasNext();
    }
}
