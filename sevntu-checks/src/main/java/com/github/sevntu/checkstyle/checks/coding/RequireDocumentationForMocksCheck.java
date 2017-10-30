package com.github.sevntu.checkstyle.checks.coding;

import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.FullIdent;

import com.github.sevntu.checkstyle.Utils;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class RequireDocumentationForMocksCheck extends AbstractCheck {
    /**
     * Violation message key.
     */
    public static final String MSG_KEY = "require.documentation";

    private static final String FQ_MOCKITO = "org.mockito.Mockito";
    private static final String FQ_POWERMOCK = "org.powermock.api.mockito.PowerMockito";

    private boolean importMockito;
    private boolean importPowermock;

    private Set<String> importMockitoMethods;
    private Set<String> importPowermockMethods;

    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.IMPORT, TokenTypes.STATIC_IMPORT, TokenTypes.METHOD_CALL, };
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void beginTree(DetailAST rootAST) {
        importMockito = false;
        importPowermock = false;
        importMockitoMethods.clear();
        importPowermockMethods.clear();
    }

    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
        case TokenTypes.IMPORT:
            final String imprt = getImportText(ast);

            if (FQ_MOCKITO.equals(imprt)) {
                importMockito = true;
            }
            if (FQ_POWERMOCK.equals(imprt)) {
                importPowermock = true;
            }
            break;
        case TokenTypes.STATIC_IMPORT:
            final String staticImprt = getImportText(ast);
            final int lastDot = staticImprt.lastIndexOf('.') + 1;

            if (staticImprt.startsWith(FQ_MOCKITO + ".")) {
                importMockitoMethods.add(staticImprt.substring(lastDot));
            }
            if (staticImprt.startsWith(FQ_POWERMOCK + ".")) {
                importPowermockMethods.add(staticImprt.substring(lastDot));
            }
            break;
        case TokenTypes.METHOD_CALL:
            examineMethodCall(ast);
            break;
        default:
            if (importMockito || importPowermock) {
                Utils.reportInvalidToken(ast.getType());
            }
            break;
        }
    }

    private void examineMethodCall(DetailAST ast) {
        // TODO Auto-generated method stub
    }

    /**
     * Returns import text.
     *
     * @param ast
     *            ast node that represents import
     * @return String that represents importing class
     */
    private static String getImportText(DetailAST ast) {
        final FullIdent imp;
        if (ast.getType() == TokenTypes.IMPORT) {
            imp = FullIdent.createFullIdentBelow(ast);
        }
        else {
            imp = FullIdent.createFullIdent(ast.getFirstChild().getNextSibling());
        }
        return imp.getText();
    }
}
