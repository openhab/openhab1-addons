/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.designer.ui.internal.actions;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.openhab.designer.ui.UIActivator;

public class SelectConfigFolderAction extends Action {
	
	Viewer viewer;
	
	public SelectConfigFolderAction(Viewer viewer) {
		this.viewer = viewer;
		setText("Select configuration folder");
		setToolTipText("select a configuration folder");
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
				ConfigurationFolderProvider.setRootConfigurationFolder(new File(selection));
				viewer.setInput(ConfigurationFolderProvider.getRootConfigurationFolder());
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR, UIActivator.PLUGIN_ID,  "An error occurred while opening the configuration folder", e);
				ErrorDialog.openError(shell, "Cannot open configuration folder!", null, status);
			}
		}
	}
}
