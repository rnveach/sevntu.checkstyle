////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2016 the original author or authors.
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

package com.github.sevntu.checkstyle.checks.misc;

import static com.github.sevntu.checkstyle.checks.misc.NewlineCheck.MSG_KEY;

import org.junit.Test;

import com.github.sevntu.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class NewlineCheckTest extends BaseCheckTestSupport {
    @Override
    protected DefaultConfiguration createCheckerConfig(Configuration config) {
        final DefaultConfiguration dc = new DefaultConfiguration("root");
        dc.addChild(config);
        return dc;
    }

    @Test
    public void defaultTest() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputStandard.java"), expected);
    }

    @Test
    public void defaultTestSystemProperty() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "SYSTEM");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputStandard.java"), expected);
    }

    @Test
    public void testCrPass() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "CR");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineCrAtEndOfFile.java"), expected);
    }

    @Test
    public void testCrFail() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "CR");

        final String[] expected = {
            "0: " + getCheckMessage(MSG_KEY, 80),
        };

        verify(createChecker(checkConfig), getPath("InputNewlineCrlfAtEndOfFile.java"), expected);
    }

    @Test
    public void testLfPass() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "LF");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineLfAtEndOfFile.java"), expected);
    }

    @Test
    public void testLfFail() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "LF");

        final String[] expected = {
            "0: " + getCheckMessage(MSG_KEY, 80),
        };

        verify(createChecker(checkConfig), getPath("InputNewlineCrlfAtEndOfFile.java"), expected);
    }

    @Test
    public void testCrlfPass() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "CRLF");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineCrlfAtEndOfFile.java"), expected);
    }

    @Test
    public void testCrlfFail() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "CRLF");

        final String[] expected = {
            "0: " + getCheckMessage(MSG_KEY, 80),
        };

        verify(createChecker(checkConfig), getPath("InputNewlineLfAtEndOfFile.java"), expected);
    }

    @Test
    public void testLfCrCrlfPass1() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "LF_CR_CRLF");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineCrAtEndOfFile.java"), expected);
    }

    @Test
    public void testLfCrCrlfPass2() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "LF_CR_CRLF");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineLfAtEndOfFile.java"), expected);
    }

    @Test
    public void testLfCrCrlfPass3() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "LF_CR_CRLF");

        final String[] expected = {};

        verify(createChecker(checkConfig), getPath("InputNewlineCrlfAtEndOfFile.java"), expected);
    }

    @Test
    public void testSetLineSeparatorFailure() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(NewlineCheck.class);

        checkConfig.addAttribute("lineSeparator", "ct");

        try {
            createChecker(checkConfig);
            fail("exception expected");
        }
        catch (CheckstyleException ex) {
            assertTrue(ex.getMessage().startsWith(
                    "cannot initialize module com.github.sevntu.checkstyle.checks.misc."
                            + "NewlineCheck - "
                            + "Cannot set property 'lineSeparator' to 'ct' in module"));
        }
    }
}
