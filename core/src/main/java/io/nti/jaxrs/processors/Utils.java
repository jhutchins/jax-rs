package io.nti.jaxrs.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jeff Hutchins
 */
public class Utils {

    private static final Pattern PATH_PARAMS = Pattern.compile("(\\{.+?})");

    private Utils() {}

    public static String regexify(String value) {
        Matcher matcher = PATH_PARAMS.matcher(value);
        while (matcher.find()) {
            final String match = matcher.group(1);
            StringBuilder rule = new StringBuilder("(?<");
            final int index = match.indexOf(":");
            if (index > -1) {
                rule.append(match.substring(1, index));
                rule.append(">");
                rule.append(match.substring(index + 1, match.length() - 1).replace("\\", "\\\\"));
                rule.append(")");
            } else {
                rule.append(match.substring(1, match.length() - 1));
                rule.append(">[^/]+)");
            }
            value = value.replace(match, rule.toString());
        }
        return value;
    }
}
