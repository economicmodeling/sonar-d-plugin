/**
 * Copyright: © 2014 Economic Modeling Specialists, Intl.
 * <p>
 * This file is part of sonar-d-plugin.
 * <p>
 * sonar-d-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * sonar-d-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with sonar-d-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.economicmodeling.infrastructure.d;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Brian Schott
 */
public class DScannerSensor implements Sensor {
    private static final Logger LOG = Loggers.get(DScannerSensor.class);

    public DScannerSensor() {
    }

    @Override
    public void execute(SensorContext sensorContext) {
        final Set<String> reportedMissingFiles = new HashSet<>();
        FileSystem fileSystem = sensorContext.fileSystem();
        ObjectMapper mapper = new ObjectMapper();
        final String sources = sensorContext.settings().getString("sonar.sources");
        final InputFile reportFile = fileSystem.inputFile(fileSystem.predicates()
                .hasRelativePath(sources + "/dscanner-report.json"));
        if (reportFile != null) {
            LOG.info("Analyzing dscanner-report.json");
            try {
                final DScannerReport report = mapper.readValue(reportFile.file(), DScannerReport.class);
                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(CoreMetrics.CLASSES)
                        .withValue(report.classCount.intValue())
                        .save();

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(CoreMetrics.FUNCTIONS)
                        .withValue(report.functionCount.intValue());

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(CoreMetrics.LINES)
                        .withValue(report.lineOfCodeCount.intValue());

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(DMetrics.STRUCTS)
                        .withValue(report.structCount.intValue());

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(DMetrics.TEMPLATES)
                        .withValue(report.templateCount.intValue());

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(DMetrics.INTERFACES)
                        .withValue(report.interfaceCount.intValue());

                sensorContext.<Integer>newMeasure()
                        .on(reportFile)
                        .forMetric(CoreMetrics.STATEMENTS)
                        .withValue(report.statementCount.intValue());

                LOG.info("Found " + String.valueOf(report.issues.size()) + " issues.");
                for (final DScannerIssue scannerIssue : report.issues) {
                    final InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(scannerIssue.fileName));
                    if (inputFile == null) {
                        if (!reportedMissingFiles.contains(scannerIssue.fileName)) {
                            reportedMissingFiles.add(scannerIssue.fileName);
                            LOG.info("Could not find file " + scannerIssue.fileName);
                        }
                        continue;
                    }
                    NewIssue n = sensorContext.newIssue();

                    NewIssueLocation l = n
                            .newLocation()
                            .message(scannerIssue.message)
                            .on(inputFile)
                            .at(inputFile.selectLine((int) scannerIssue.line));
                    n.at(l)
                            .forRule(RuleKey.of("dscanner", scannerIssue.key))
                            .save();
                }
            } catch (IOException e) {
                LOG.error("Could not open file", e);
            }
        } else {
            LOG.error("Could not open dscanner-report.json");
        }
    }

    @Override
    public void describe(SensorDescriptor descriptor)
    {
        descriptor.onlyOnLanguage("d");
        descriptor.name("D-Scanner sensor");
        descriptor.createIssuesForRuleRepository("dscanner");
    }
}
