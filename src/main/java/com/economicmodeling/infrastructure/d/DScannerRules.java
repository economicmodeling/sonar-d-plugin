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

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * @author Brian Schott
 */
public class DScannerRules implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository("dscanner", "d").setName("D Scanner");

        NewRule rule = repository.createRule("dscanner")
                .setName("Generic dscanner rule")
                .setHtmlDescription("Generic dscanner rule")
                .setStatus(RuleStatus.BETA);
        repository.done();
    }
}
