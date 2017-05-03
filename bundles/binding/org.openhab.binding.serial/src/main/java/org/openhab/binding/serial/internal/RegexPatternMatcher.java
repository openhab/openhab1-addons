/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pattern matcher to match and cache patterns
 *
 * @author Marek Halmo
 * @see RegexPatternMatcher#getMatches getMatches()
 *
 */
public class RegexPatternMatcher {
    private static final Logger logger = LoggerFactory.getLogger(RegexPatternMatcher.class);
    private static HashMap<String, Pattern> patternCache = new HashMap<String, Pattern>();
    private static final Pattern SUBST_PATTERN = Pattern.compile("^s/(.*?[^\\\\])/(.*?[^\\\\])/(.*)$");

    /**
     * Returns compiled pattern for specific regeExpression that is stored in patternCache
     *
     * @param regExpression
     * @return
     */
    private static Pattern getPattern(String regExpression) {
        cache(regExpression);
        return patternCache.get(regExpression);
    }

    /**
     * Returns true if given regExpression is a substitution pattern
     * This fact is indicated by giving
     *
     * @param regExpression
     * @return
     */
    private static boolean isSubtitution(String regExpression) {
        cache(regExpression);
        return patternCache.get(regExpression) == null;
    }

    /**
     * Caches given regexpression, if the expression is substitution pattern it marks is as substitution
     *
     * @return
     */
    private static void cache(String regExpression) {
        if (!patternCache.containsKey(regExpression)) {
            // Check if this pattern is a substitution. If so ignore it
            Matcher substMatcher = SUBST_PATTERN.matcher(regExpression);
            if (substMatcher.matches()) {
                patternCache.put(regExpression, null);
            } else {
                Pattern pattern = Pattern.compile(regExpression, Pattern.DOTALL);
                patternCache.put(regExpression, pattern);
            }
        }
    }

    /**
     * Returns array of strings that match given regular expression.
     * If expression is not known the compiled pattern is cached in pattern cache
     *
     * @param regExpression regular expression to match or a substitution in form of "s/<regex>/result/g"
     * @param source text to search in
     * @return
     * @throws TransformationException if regExpression or source is null
     * @see org.eclipse.smarthome.transform.regex.internal.RegExTranformationService RegExTranformationService
     */
    public static String[] getMatches(String regExpression, String source) throws TransformationException {
        if (regExpression == null || source == null) {
            throw new TransformationException("the given parameters 'regex' and 'source' must not be null");
        }

        logger.debug("about to transform '{}' by the function '{}'", source, regExpression);

        // Check if RegEx is a substitution (s/<regex>/result/g)
        if (isSubtitution(regExpression)) {
            Matcher substMatcher = SUBST_PATTERN.matcher(regExpression);
            if (substMatcher.matches()) {
                logger.debug("Using substitution form of regex transformation");
                String regex = substMatcher.group(1);
                String substitution = substMatcher.group(2);
                String options = substMatcher.group(3);

                String result;
                if (options.equals("g")) {
                    result = source.trim().replaceAll(regex, substitution);
                } else {
                    result = source.trim().replaceFirst(regex, substitution);
                }

                return new String[] { result };
            }
        }

        Matcher matcher = getPattern(regExpression).matcher(source.trim());
        List<String> results = new ArrayList<String>();

        while (matcher.find()) {
            results.add(matcher.group(1));
        }

        return results.toArray(new String[results.size()]);
    }

    /**
     * Removes pattern from cache
     *
     * @param regExpression
     */
    public static void removePattern(String regExpression) {
        patternCache.remove(regExpression);
    }
}
