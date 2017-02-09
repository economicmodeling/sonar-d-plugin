/**
 * Copyright: © 2014 Economic Modeling Specialists, Intl.
 * <p>
 * This file is part of sonar-d-plugin.
 * <p>
 * Foobar is free software: you can redistribute it and/or modify
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

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * @author Brian Schott
 */
public class DMetrics implements Metrics {

    public static final Metric<Integer> STRUCTS = new Metric.Builder("dscanner.struct_count", "Structs", Metric.ValueType.INT)
            .setDomain(CoreMetrics.DOMAIN_SIZE)
            .setQualitative(true)
            .setDescription("Number of struct declarations in the project")
            .create();

    public static final Metric<Integer> TEMPLATES = new Metric.Builder("dscanner.template_count", "Templates", Metric.ValueType.INT)
            .setDomain(CoreMetrics.DOMAIN_SIZE)
            .setQualitative(true)
            .setDescription("Number of template declarations in the project")
            .create();

    public static final Metric<Integer> INTERFACES = new Metric.Builder("dscanner.interface_count", "Interfaces", Metric.ValueType.INT)
            .setDomain(CoreMetrics.DOMAIN_SIZE)
            .setQualitative(true)
            .setDescription("Number of interface declarations in the project")
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(STRUCTS, TEMPLATES, INTERFACES);
    }
}
