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

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Brian Schott
 */
public class DScannerRules implements RulesDefinition {

    RulesDefinitionXmlLoader xmlLoader;
    private static final Logger LOG = Loggers.get(DScannerSensor.class);

    public DScannerRules(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository("dscanner", "d")
                .setName("D-Scanner");

        xmlLoader.load(repository, getClass().getResourceAsStream("dscanner-rules.xml"),
                StandardCharsets.UTF_8);
        for (Repository repo : context.repositories()) {
            for (Rule r : repo.rules()) {
                LOG.info(r.key());
            }
        }
        repository.done();
    }
}
