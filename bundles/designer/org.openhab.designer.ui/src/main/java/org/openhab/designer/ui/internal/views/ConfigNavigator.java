/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.ui.internal.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonNavigator;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.openhab.designer.ui.internal.actions.SelectConfigFolderAction;

public class ConfigNavigator extends CommonNavigator {

	private IResourceChangeListener changeListener;

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		Action action = new SelectConfigFolderAction(getCommonViewer());
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager dropDownMenu = actionBars.getMenuManager();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		dropDownMenu.add(action);
		toolBar.add(action);
	}

	@Override
	protected Object getInitialInput() {
		changeListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						getCommonViewer().refresh();
					}
				});
			}
		};
		ResourcesPlugin.getWorkspace()
				.addResourceChangeListener(changeListener);

		try {
			return ConfigurationFolderProvider.getRootConfigurationFolder()
					.getProject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				changeListener);
		changeListener = null;
	}
}
