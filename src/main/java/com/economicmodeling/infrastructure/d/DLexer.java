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

import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.*;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.o2n;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.opt;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.or;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.g;
import java.nio.charset.Charset;

/**
 * @author Brian Schott
 */
public final class DLexer {

    private static final String WHITESPACE = "\\t\\000B\\u0020\\u00A0\\uFEFF\\p{Zs}";
    private static final String NEWLINES = "\\n\\r\\u2028\\u2029";
    private static final String DECIMAL_DIGITS = "[0-9_]++";
    private static final String DECIMAL_DIGITS_NO_UNDERSCORE = "[0-9]++";
    private static final String HEX_DIGITS = "[0-9a-fA-F_]++";
    private static final String HEX_PREFIX = "0[x|X]";
    private static final String BINARY_DIGITS = "[01_]++";
    private static final String STRING_SUFFIX = "[cwd]";
    private static final String DECIMAL_EXPONENT = "[eE]" + opt("[-+]?") + DECIMAL_DIGITS;
    private static final String HEX_EXPONENT = "[pP][-+]?" + DECIMAL_DIGITS;
    private static final String INTEGER_SUFFIX = "u|uL|U|UL|L|Lu|LU";
    private static final String FLOAT_SUFFIX = "[fF]";
    private static final String REAL_SUFFIX = "L";
    private static final String IMAGINARY_SUFFIX = "i";
    private static final String BINARY_INTEGER = "0[bB]" + BINARY_DIGITS + opt(INTEGER_SUFFIX);
    private static final String DECIMAL_INTEGER = DECIMAL_DIGITS + opt(INTEGER_SUFFIX);
    private static final String HEX_INTEGER = HEX_PREFIX + HEX_DIGITS + opt(INTEGER_SUFFIX);
    private static final String INTEGER_LITERAL = DECIMAL_INTEGER + or(HEX_INTEGER) + or(BINARY_INTEGER);
    private static final String DECIMAL_FLOAT = DECIMAL_DIGITS_NO_UNDERSCORE + opt(DECIMAL_DIGITS) + opt("\\.")
            + opt(DECIMAL_DIGITS) + opt(DECIMAL_EXPONENT) + opt(g(FLOAT_SUFFIX + or(REAL_SUFFIX))) + opt(IMAGINARY_SUFFIX);
    private static final String HEX_FLOAT = HEX_PREFIX + HEX_DIGITS + opt("\\.") + HEX_DIGITS + HEX_EXPONENT;
    private static final String FLOAT_LITERAL = DECIMAL_FLOAT + or(HEX_FLOAT);

    public static Lexer create() {
        return Lexer.builder()
                .withCharset(Charset.forName("UTF-8"))
                .withFailIfNoChannelToConsumeOneCharacter(false)
                .withChannel(new BlackHoleChannel("[" + NEWLINES + WHITESPACE + "]"))
                .withChannel(new PunctuatorChannel(DOperators.values()))
                .withChannel(new IdentifierAndKeywordChannel("\\w+", true, DKeywords.values()))
                .withChannel(regexp(DTokenType.INTLITERAL, INTEGER_LITERAL))
                .withChannel(regexp(DTokenType.FLOATLITERAL, FLOAT_LITERAL))
                .withChannel(commentRegexp("/\\*", ".?", "\\*/"))
                .withChannel(commentRegexp("/\\+", ".?", "\\+/"))
                .withChannel(commentRegexp("//", o2n("[^" + NEWLINES + "]")))
                .withChannel(new BomCharacterChannel())
                .withChannel(new UnknownCharacterChannel())
                .build();
    }
}
