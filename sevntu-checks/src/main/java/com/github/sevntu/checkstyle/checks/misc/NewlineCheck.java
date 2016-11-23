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

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConversionException;

import com.puppycrawl.tools.checkstyle.api.AbstractFileSetCheck;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.checks.LineSeparatorOption;

/**
 * <p>
 * Checks that only specific line terminators are used throughout an entire
 * file.
 * </p>
 * <p>
 * Some source code repositories discourage the user of non-unix (
 * {@link LineSeparatorOption#LF}) line endings, like Git. They do not, however,
 * prevent someone from coming in and committing files with inappropriate line
 * endings. When this happens, some utilities and viewers may show the text
 * inappropriately on certain systems. Windows notepad, for example, will not
 * recognize unix line endings and display everything on one line. If one line
 * ending is swapped for another, Git difference view will show the entire file
 * as being changed, even though none of the line's actual content changed. This
 * check aims to make sure all files stay in the correct coding.
 * </p>
 * <p>
 * There are some limitations of this check. It is not validating what is stored
 * in any source code repository. With the repository Git, there is an option,
 * {@code autocrlf}, which will turn line endings in the local, workspace to
 * {@link LineSeparatorOption#CRLF} and will store the line endings in the
 * server's commits as {@link LineSeparatorOption#LF}. Also, this check will
 * only validate the files examined by Checkstyle. This could possibly not be
 * everything stored in your repository. It is recommended you add other tests
 * to verify what is stored in the repository and not rely fully on this check.
 * </p>
 * <p>
 * If you plan to use this check on multiple, different systems, it is
 * recommended to stick with the default line ending,
 * {@link LineSeparatorOption#SYSTEM}.
 * </p>
 * <p>
 * If you use {@code LF_CR_CRLF} for the line separation, it will basically
 * accept all files as if no validation was being done.
 * </p>
 * <p>
 * An example of how to configure the check is:
 * </p>
 *
 * <pre>
 * &lt;module name="Newline"/&gt;
 * </pre>
 * <p>
 * This will check against the platform-specific default line separator.
 * </p>
 * <p>
 * It is also possible to enforce the use of a specific line-separator across
 * platforms, with the 'lineSeparator' property:
 * </p>
 *
 * <pre>
 * &lt;module name="Newline"&gt;
 *   &lt;property name="lineSeparator" value="lf"/&gt;
 * &lt;/module&gt;
 * </pre>
 * <p>
 * Valid values for the 'lineSeparator' property are 'system' (system default),
 * 'crlf' (windows), 'cr' (mac), 'lf' (unix) and 'lf_cr_crlf' (lf, cr or crlf).
 * </p>
 *
 * @author Richard Veach
 */
public final class NewlineCheck extends AbstractFileSetCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "invalid.newline";

    /**
     * Regular expression pattern matching all line terminators.
     */
    private static final Pattern LINE_TERMINATOR = Pattern.compile("\\r\\n|\\r|\\n");

    /** The line separator to check against. */
    private LineSeparatorOption lineSeparator = LineSeparatorOption.SYSTEM;

    /**
     * Sets the line separator to one of 'crlf', 'lf','cr', 'lf_cr_crlf' or
     * 'system'.
     *
     * @param lineSeparatorParam The line separator to set
     * @throws IllegalArgumentException If the specified line separator is not
     *         one of 'crlf', 'lf', 'cr', 'lf_cr_crlf' or 'system'
     */
    public void setLineSeparator(String lineSeparatorParam) {
        try {
            lineSeparator = Enum.valueOf(LineSeparatorOption.class, lineSeparatorParam.trim()
                    .toUpperCase(Locale.ENGLISH));
        }
        catch (IllegalArgumentException iae) {
            throw new ConversionException("unable to parse " + lineSeparatorParam, iae);
        }
    }

    @Override
    protected void processFiltered(File file, List<String> lines) {
        final FileText text = (FileText) lines;
        final String fullText = (String) text.getFullText();

        final Matcher matcher = LINE_TERMINATOR.matcher(fullText);

        while (matcher.find()) {
            final int position = matcher.start();

            if (!doesLineMatch(fullText, position, matcher.end())) {
                log(0, MSG_KEY, position);
                break;
            }
        }
    }

    /**
     * Checks if the bytes directly before the {@code position} matches the user
     * specified line encoding.
     *
     * @param fullText The text to examine.
     * @param start The start position of the text to examine.
     * @param end The end position of the text to examine.
     * @return {@code true} if the line matches the requested line encoding.
     */
    private boolean doesLineMatch(String fullText, int start, int end) {
        final boolean result;

        if (lineSeparator.length() == (end - start)) {
            result = lineSeparator.matches(fullText.substring(start, end).getBytes());
        }
        else {
            result = lineSeparator == LineSeparatorOption.LF_CR_CRLF;
        }

        return result;
    }
}
