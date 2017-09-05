/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.http;

import java.util.List;
import java.util.Properties;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and HTTP items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Carman
 *
 * @since 0.6.0
 */
public interface HttpBindingProvider extends BindingProvider {

    /**
     * Returns the httpMethod to use according to <code>itemName</code> and
     * <code>command</code>. Is used by HTTP-Out-Binding.
     *
     * @param itemName the item for which to find a httpMethod
     * @param command the openHAB command for which to find a configuration
     *
     * @return the matching httpMethod or <code>null</code> if no matching
     *         httpMethod could be found.
     */
    String getHttpMethod(String itemName, Command command);

    /**
     * Returns the url to use according to <code>itemName</code> and
     * <code>command</code>. Is used by HTTP-Out-Binding.
     *
     * @param itemName the item for which to find a url
     * @param command the openHAB command for which to find a configuration
     *
     * @return the matching url or <code>null</code> if no matching
     *         url could be found.
     */
    String getUrl(String itemName, Command command);

    /**
     * Returns the body to use according to <code>itemName</code> and
     * <code>command</code>. Is used by HTTP-Out-Binding.
     *
     * @param itemName the item for which to find a body
     * @param command the openHAB command for which to find a configuration
     *
     * @return the matching body or <code>null</code> if no matching
     *         body could be found.
     */
    String getBody(String itemName, Command command);

    /**
     * Returns HTTP headers to use according to <code>itemName</code> and
     * <code>command</code>. Is used by HTTP-Out-Binding.
     *
     * @param itemName the item for which to find a url
     * @param command the openHAB command for which to find a configuration
     *
     * @return the matching headers or <code>null</code> if no matching
     *         headers could be found.
     */
    Properties getHttpHeaders(String itemName, Command command);

    /**
     * Returns the url to use according to <code>itemName</code>.
     * Is used by HTTP-In-Binding.
     *
     * @param itemName the item for which to find a url
     *
     * @return the matching url or <code>null</code> if no matching
     *         url could be found.
     */
    String getUrl(String itemName);

    /**
     * Returns HTTP headers to use according to <code>itemName</code>. Is
     * used by HTTP-In-Binding
     *
     * @param itemName the item for which to find headers
     * @return the matching HTTP headers
     */
    Properties getHttpHeaders(String itemName);

    /**
     * Returns the refresh interval to use according to <code>itemName</code>.
     * Is used by HTTP-In-Binding.
     *
     * @param itemName the item for which to find a refresh interval
     *
     * @return the matching refresh interval or <code>null</code> if no matching
     *         refresh interval could be found.
     */
    int getRefreshInterval(String itemName);

    /**
     * Returns the transformation rule to use according to <code>itemName</code>.
     * Is used by HTTP-In-Binding.
     *
     * @param itemName the item for which to find a transformation rule
     *
     * @return the matching transformation rule or <code>null</code> if no matching
     *         transformation rule could be found.
     */
    String getTransformation(String itemName);

    /**
     * Returns the transformation rule to use according to <code>itemName</code> and
     * <code>command</code>. Is used by HTTP-Out-Binding.
     *
     * @param itemName the item for which to find a transformation rule
     * @param command the openHAB command for which to find a configuration
     *
     * @return the matching transformation rule or <code>null</code> if no matching
     *         transformation rule could be found.
     */
    String getTransformation(String itemName, Command command);

    /**
     * Returns a {@link State} that is one of the named item's accepted data
     * types, parsed from <code>value</code>. If <code>value</code> cannot be parsed
     * into an accepted data type, <code>null</code> is returned.
     *
     * @param itemName the item for which to produce a State
     * @param value the string from which to produce a State
     *
     * @return the State, or null if no State can be derived from the value
     */
    State getState(String itemName, String value);

    /**
     * Returns all items which are mapped to a HTTP-In-Binding
     *
     * @return item which are mapped to a HTTP-In-Binding
     */
    List<String> getInBindingItemNames();
}
