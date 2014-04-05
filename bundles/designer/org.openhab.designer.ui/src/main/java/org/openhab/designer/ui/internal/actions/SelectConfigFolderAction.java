/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.ui.internal.actions;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.openhab.config.core.ConfigConstants;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.openhab.designer.ui.UIActivator;

public class SelectConfigFolderAction extends Action {
	
	Viewer viewer;
	
	public SelectConfigFolderAction(Viewer viewer) {
		this.viewer = viewer;
		setText("Select Configuration Folder");
		setToolTipText("Select a Configuration Folder");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
	}
	
	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
		dialog.setMessage("Select the configuration folder of the openHAB runtime");
		String selection = dialog.open();
		if(selection!=null) {
			try {
				File file = new File(selection);
				if(isValidConfigurationFolder(file)) {
					ConfigurationFolderProvider.saveFolderToPreferences(selection);
					ConfigurationFolderProvider.setRootConfigurationFolder(new File(selection));
					viewer.setInput(ConfigurationFolderProvider.getRootConfigurationFolder());
				} else {
					MessageDialog.openError(shell, "No valid configuration directory", "The chosen directory is not a valid openHAB configuration" +
							" directory. Please choose a different one.");
				}
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR, UIActivator.PLUGIN_ID,  "An error occurred while opening the configuration folder", e);
				ErrorDialog.openError(shell, "Cannot open configuration folder!", null, status);
			}
		}
	}

	private boolean isValidConfigurationFolder(File dir) {
		if(dir.isDirectory()) {
			for(File file : dir.listFiles()) {
				if(file.getName().equals(ConfigConstants.DEFAULT_CONFIG_FILENAME)) {
					return true;
				}
			}
		}
		return false;
	}
}
