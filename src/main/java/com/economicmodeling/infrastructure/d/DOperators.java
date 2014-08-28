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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum DOperators implements TokenType {
    COMMA(","), DOT("."), DOTDOT(".."), DOTDOTDOT("..."), SLASH("/"), DIVEQUALS("/="), NOT("!"), NOTLESS("!<"),
    NOTLESSEQUAL("!<="), NOTLESSGREATER("!<>"), NOTGREATEREQUALEQUAL("!<>="), NOTEQUALS("!="),
    NOTGREATER("!>"), NOTGREATEREQUAL("!>="), DOLLAR("$"), MOD("%"), MODASSIGN("%="), AMP("&"), LOGICAND("&&"),
    BITANDASSIGN("&="), LPAREN("("), RPAREN(")"), STAR("*"), MULASSIGN("*="), PLUS("+"), PLUSPLUS("++"),
    PLUSASSIGN("+="), MINUS("-"), MINUSMINUS("--"), MINUSASSIGN("-="), COLON(":"), SEMICOLON(";"), LESS("<"),
    SHIFTLEFT("<<"), SHIFTLEFTASSIGN("<<="), LESSEQUALS("<="), DIAMOND("<>"), DIAMONDEQUALS("<>="), ASSIGN("="),
    EQUALS("=="), LAMBDA("=>"), GREATER(">"), GREATEREQUAL(">="), SHIFTRIGHT(">>"), SHIFTRIGHTASSIGN(">>="),
    UNSIGNEDSHIFTRIGHT(">>>"), UNSIGNEDSHIFTRIGHTASSIGN(">>>="), QUESTION("?"), AT("@"), LBRACKET("["), RBRACKET("]"),
    XOR("^"), XORASSIGN("^="), POW("^^"), POWASSIGN("^^="), LBRACE("{"), BITOR("|"), BITORASSIGN("|="), LOGICOR("||"),
    RBRACE("}"), TILDE("~"), CATASSIGN("~=");

    private final String value;

    private DOperators(final String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean hasToBeSkippedFromAst(AstNode astNode) {
        return false;
    }
}