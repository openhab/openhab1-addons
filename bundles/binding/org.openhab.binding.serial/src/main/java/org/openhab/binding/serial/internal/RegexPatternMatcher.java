/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.serial.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pattern matcher to match and cache regular expression patterns
 *
 * @author Marek Halmo
 * @SInCE 1.10.0
 * @see RegexPatternMatcher#getMatches getMatches()
 *
 */
public class RegexPatternMatcher {
    private static final Logger logger = LoggerFactory.getLogger(RegexPatternMatcher.class);
    private static Map<String, Pattern> patternCache = new HashMap<>();

    /*
     * Regular expression to match or a substitution in form of
     * "s/<regex>/result/g" (replace all) or
     * "s/<regex>/result/" (replace first)
     */
    private static final Pattern SUBST_PATTERN = Pattern.compile("^s/(.*?[^\\\\])/(.*?[^\\\\])/(.*)$");

    /**
     * Returns compiled pattern for specific regular expression that is stored in patternCache
     *
     * @param regExpression
     * @return
     */
    private static Pattern getPattern(String regExpression) {
        return cache(regExpression);
    }

    /**
     * Returns true if given regular expression is a substitution pattern
     * This fact is indicated by patternCache containing "null" for given pattern
     *
     * @param regExpression
     * @return
     */
    private static boolean isSubtitution(String regExpression) {
        return cache(regExpression) == null;
    }

    /**
     * Caches given regular expression.
     * If the expression is a substitution pattern, it marks it as a substitution with a null
     *
     * @return Pattern from cache
     * @see RegexPatternMatcher#isSubtitution isSubtitution
     */
    private static synchronized Pattern cache(String regExpression) {
        if (!patternCache.containsKey(regExpression)) {
            Matcher substMatcher = SUBST_PATTERN.matcher(regExpression);

            // Check if this pattern is a substitution
            if (substMatcher.matches()) {
                // If so mark it in cache with null
                patternCache.put(regExpression, null);
            } else {
                // Otherwise compile the pattern
                Pattern pattern = Pattern.compile(regExpression, Pattern.DOTALL);
                patternCache.put(regExpression, pattern);
            }
        }

        return patternCache.get(regExpression);
    }

    /**
     * Returns array of strings that match given regular expression.
     * If expression is not known the compiled pattern is cached in pattern cache
     *
     * @param regExpression regular expression to match or a substitution in form of "s/<regex>/result/g" (replace all)
     *            or "s/<regex>/result/" (replace first)
     * @param source text to search in
     * @return Array of matched strings or empty array if none found
     * @throws TransformationException if regExpression or source is null
     * @see org.eclipse.smarthome.transform.regex.internal.RegExTranformationService RegExTranformationService
     */
    public static String[] getMatches(String regExpression, String source) throws TransformationException {
        if (regExpression == null || source == null) {
            throw new TransformationException("The given parameters 'regExpression' and 'source' must not be null");
        }

        logger.debug("about to transform '{}' by the function '{}'", source, regExpression);

        // Check if RegEx is a substitution (s/<regex>/result/g) or (s/<regex>/result/)
        if (isSubtitution(regExpression)) {
            Matcher substMatcher = SUBST_PATTERN.matcher(regExpression);

            // If there is no match of substitution, source is returned
            String result = source;
            if (substMatcher.matches()) {
                logger.debug("Using substitution form of regex transformation");
                String regex = substMatcher.group(1);
                String substitution = substMatcher.group(2);
                String options = substMatcher.group(3);

                if (options.equals("g")) {
                    result = source.trim().replaceAll(regex, substitution);
                } else {
                    result = source.trim().replaceFirst(regex, substitution);
                }
            }

            return new String[] { result };
        } else {
            // Not a substitution, return matches
            Matcher matcher = getPattern(regExpression).matcher(source.trim());

            List<String> results = new ArrayList<>();

            while (matcher.find()) {
                // Only return first/primary group matches
                // This way you can use secondary groups to quantify occurrence count
                results.add(matcher.group(1));
            }

            return results.toArray(new String[results.size()]);
        }
    }

    /**
     * Removes pattern from cache
     *
     * @param regExpression
     */
    public static synchronized void removePattern(String regExpression) {
        patternCache.remove(regExpression);
    }
}
