/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.scoping;

import org.openhab.model.script.scoping.ScriptExtensionClassNameProvider;

import com.google.inject.Singleton;

/**
 * This class registers all statically available functions as well as the
 * extensions for specific jvm types, which should only be available in rules,
 * but not in scripts
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@Singleton
public class RuleExtensionClassNameProvider extends	ScriptExtensionClassNameProvider {

}
