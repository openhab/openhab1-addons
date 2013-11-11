/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.ui.internal.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.CommonViewer;
import org.openhab.designer.ui.internal.actions.SelectConfigFolderAction;

public class ConfigNavigatorActionGroup extends ActionGroup {

	private SelectConfigFolderAction selectConfigFolderAction;
	private CommonViewer commonViewer;

	public ConfigNavigatorActionGroup(CommonViewer aViewer) {
		super();
		commonViewer = aViewer;
		makeActions();
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		IToolBarManager manager = actionBars.getToolBarManager();
		manager.add(selectConfigFolderAction);
	}
	
	private void makeActions() {
		selectConfigFolderAction = new SelectConfigFolderAction(commonViewer);
	}

}
