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

import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Schott
 */
public class DSourceCodeColorizer extends CodeColorizerFormat {
    private static final String CLOSE_TAG = "</span>";

    public DSourceCodeColorizer() {
        super("d");
    }

    @Override
    public List<Tokenizer> getTokenizers() {
        List<Tokenizer> tokenizers = new ArrayList<Tokenizer>();
        tokenizers.add(new CDocTokenizer("<span class=\"cd\">", CLOSE_TAG));
        tokenizers.add(new CppDocTokenizer("<span class=\"cppd\">", CLOSE_TAG));
        tokenizers.add(new KeywordsTokenizer("<span class=\"k\">", CLOSE_TAG, DKeywords.keywordValues()));
        tokenizers.add(new LiteralTokenizer("<span class=\"s\">", CLOSE_TAG));
        return tokenizers;
    }
}
