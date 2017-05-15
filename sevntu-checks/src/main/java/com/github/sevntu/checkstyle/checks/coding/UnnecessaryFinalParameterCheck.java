////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2017 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.github.sevntu.checkstyle.checks.coding;

import java.util.HashSet;
import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class UnnecessaryFinalParameterCheck extends AbstractCheck {
    public static final String MSG_KEY = "unnecessary.final.parameter";

    @Override
    public int[] getDefaultTokens() {
        return getAcceptableTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {TokenTypes.METHOD_DEF, TokenTypes.CTOR_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

        final Set<String> finalParameters = retrieveFinalParameters(parameters);

        if (!finalParameters.isEmpty()) {
            final DetailAST list = ast.findFirstToken(TokenTypes.SLIST);

            if (list != null) {
                scan(list, false, finalParameters);
            }

            if (finalParameters.size() > 0) {
                for (String parameter : finalParameters) {
                    log(ast, MSG_KEY, parameter);
                }
            }
        }
    }

    private static Set<String> retrieveFinalParameters(DetailAST parameters) {
        final Set<String> finalParameters = new HashSet<String>();

        for (DetailAST parameter = parameters.getFirstChild(); parameter != null;
                parameter = parameter.getNextSibling()) {
            if (parameter.getType() != TokenTypes.PARAMETER_DEF
                    || parameter.findFirstToken(TokenTypes.MODIFIERS).findFirstToken(
                            TokenTypes.FINAL) == null) {
                continue;
            }

            finalParameters.add(parameter.findFirstToken(TokenTypes.IDENT).getText());
        }

        return finalParameters;
    }

    private static void scan(DetailAST ast, boolean inObjblock, Set<String> finalParameters) {
        if (inObjblock) {
            if (ast.getType() == TokenTypes.IDENT) {
                final int parentType = ast.getParent().getType();

                if (parentType != TokenTypes.PARAMETER_DEF
                        && parentType != TokenTypes.VARIABLE_DEF
                        && parentType != TokenTypes.METHOD_DEF
                        && ((parentType == TokenTypes.DOT && ast != ast.getParent()
                                .getFirstChild().getNextSibling())
                                || (parentType != TokenTypes.DOT))
                        && parentType != TokenTypes.LITERAL_NEW) {
                    final String name = ast.getText();

                    for (String s : finalParameters) {
                        if (s.equals(name)) {
                            finalParameters.remove(s);
                            return;
                        }
                    }
                }
            }
        }

        for (DetailAST i = ast.getFirstChild(); i != null; i = i.getNextSibling()) {
            scan(i, inObjblock || i.getType() == TokenTypes.OBJBLOCK, finalParameters);
        }
    }
}
