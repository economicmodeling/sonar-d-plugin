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

/**
 * @author Brian Schott
 */
public enum DKeywords implements TokenType {
    ABSTRACT("abstract"), ALIAS("alias"), ALIGN("align"), ASM("asm"), ASSERT("assert"), AUTO("auto"), BODY("body"),
    BOOL("bool"), BREAK("break"), BYTE("byte"), CASE("case"), CAST("cast"), CATCH("catch"), CDOUBLE("cdouble"),
    CENT("cent"), CFLOAT("cfloat"),	CHAR("char"), CLASS("class"), CONST("const"), CONTINUE("continue"), CREAL("creal"),
    DCHAR("dchar"), DEBUG("debug"), DEFAULT("default"), DELEGATE("delegate"), DELETE("delete"), DEPRECATED("deprecated"),
    DO("do"), DOUBLE("double"), ELSE("else"), ENUM("enum"), EXPORT("export"), EXTERN("extern"), FALSE("false"),
    FINAL("final"), FINALLY("finally"), FLOAT("float"), FOR("for"), FOREACH("foreach"), FOREACH_REVERSE("foreach_reverse"),
    FUNCTION("function"), GOTO("goto"), IDOUBLE("idouble"), IF("if"), IFLOAT("ifloat"), IMMUTABLE("immutable"),
    IMPORT("import"), IN("in"), INOUT("inout"), INT("int"), INTERFACE("interface"), INVARIANT("invariant"),
    IREAL("ireal"), IS("is"), LAZY("lazy"), LONG("long"), MACRO("macro"), MIXIN("mixin"), MODULE("module"), NEW("new"),
    NOTHROW("nothrow"), NULL("null"), OUT("out"), OVERRIDE("override"), PACKAGE("package"), PRAGMA("pragma"),
    PRIVATE("private"), PROTECTED("protected"), PUBLIC("public"), PURE("pure"), REAL("real"), REF("ref"),
    RETURN("return"), SCOPE("scope"), SHARED("shared"), SHORT("short"), STATIC("static"), STRUCT("struct"),
    SUPER("super"), SWITCH("switch"), SYNCHRONIZED("synchronized"), TEMPLATE("template"),THIS("this"), THROW("throw"),
    TRUE("true"), TRY("try"), TYPEDEF("typedef"), TYPEID("typeid"), TYPEOF("typeof"), UBYTE("ubyte"), UCENT("ucent"),
    UINT("uint"), ULONG("ulong"), UNION("union"), UNITTEST("unittest"), USHORT("ushort"), VERSION("version"),
    VIRTUAL("virtual"), VOID("void"), VOLATILE("volatile"), WCHAR("wchar"), WHILE("while"), WITH("with"),
    DATE("__DATE__"), EOF("__EOF__"), FILE("__FILE__"), _FUNCTION("__FUNCTION__"), GSHARED("__gshared"),
    LINE("__LINE__"), _MODULE("__MODULE__"), PARAMETERS("__parameters"), PRETTY_FUNCTION("__PRETTY_FUNCTION__"),
    TIME("__TIME__"), TIMESTAMP("__TIMESTAMP__"), TRAITS("__traits"), VECTOR("__vector"), VENDOR("__VENDOR__"),
    _VERSION("__VERSION__");

    private final String value;

    private DKeywords(final String value) {
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

    public static String[] keywordValues() {
        final String[] values = new String[values().length];
        int i = 0;
        for (final DKeywords item : values()) {
            values[i++] = item.value;
        }
        return values;
    }
}