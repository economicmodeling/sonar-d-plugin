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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

/**
 * @author Brian Schott
 */
public enum DTokenType implements TokenType {
    SPECIALTOKENSEQUENCE,
    COMMENT,
    IDENTIFIER,
    SCRIPTLINE,
    WHITESPACE,
    DOUBLELITERAL,
    FLOATLITERAL,
    IDOUBLELITERAL,
    IFLOATLITERAL,
    INTLITERAL,
    LONGLITERAL,
    REALLITERAL,
    IREALLITERAL,
    UINTLITERAL,
    ULONGLITERAL,
    CHARACTERLITERAL,
    DSTRINGLITERAL,
    STRINGLITERAL,
    WSTRINGLITERAL;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return name();
    }

    @Override
    public boolean hasToBeSkippedFromAst(AstNode astNode) {
        return false;
    }
}
