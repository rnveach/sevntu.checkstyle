package com.github.sevntu.checkstyle.checks.coding;

import static com.github.sevntu.checkstyle.checks.coding.UnnecessaryFinalParameterCheck.MSG_KEY;

import org.junit.Test;

import com.github.sevntu.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

public class UnnecessaryFinalParameterCheckTest extends BaseCheckTestSupport {
    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(UnnecessaryFinalParameterCheck.class);

        final String[] expected = {
            "4:5: " + getCheckMessage(MSG_KEY, "t"),
            "8:5: " + getCheckMessage(MSG_KEY, "s"),
            "9:5: " + getCheckMessage(MSG_KEY, "s"),
            "23:9: " + getCheckMessage(MSG_KEY, "s"),
        };
        verify(checkConfig, getPath("InputUnnecessaryFinalParameterCheck.java"), expected);
    }
}
