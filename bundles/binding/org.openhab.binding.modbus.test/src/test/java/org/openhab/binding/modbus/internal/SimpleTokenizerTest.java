/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.modbus.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.model.item.binding.BindingConfigParseException;

@RunWith(Parameterized.class)
public class SimpleTokenizerTest {

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> parameters = Arrays.<Object[]> asList(//
                new Object[] { "", ':', new String[] { "" } }, //
                new Object[] { ":", ':', new String[] { "", "" } },
                new Object[] { ": ", ':', new String[] { "", " " } },
                new Object[] { "foo:bar", ':', new String[] { "foo", "bar" } },
                new Object[] { "foo,bar", ',', new String[] { "foo", "bar" } },
                new Object[] { "foo,", ',', new String[] { "foo", "" } },
                // there is no escape syntax when quotes are not used
                new Object[] { "foo,a\\,b", ',', new String[] { "foo", "a\\", "b" } },
                // delimiter inside a quoted token, but also escaped quote
                new Object[] { "foo,\"b\\\"a,r\"", ',', new String[] { "foo", "b\"a,r" } },
                // not ending with quote, we keep the starting quote but resolve the escaping...
                new Object[] { "foo,\"b\\\"a,r\"f", ',', new String[] { "foo", "\"b\"a,r\"f" } }

        );
        return parameters;

    }

    private String inputString;
    private char delimiter;
    private String[] expectedTokens;

    public SimpleTokenizerTest(String inputString, char delimiter, String[] expectedTokens) {
        this.inputString = inputString;
        this.delimiter = delimiter;
        this.expectedTokens = expectedTokens;
    }

    @Test
    public void testParsing() throws BindingConfigParseException {
        assertThat(new SimpleTokenizer(delimiter).parse(inputString), is(equalTo(Arrays.asList(expectedTokens))));
    }

}
