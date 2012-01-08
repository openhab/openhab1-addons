/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.openhab.designer.ui.UIActivator;

public class OpenFileAction extends Action {

    /**
     * The id of this action.
     */
    public static final String ID = UIActivator.PLUGIN_ID + ".OpenFileAction";//$NON-NLS-1$

    private Viewer viewer;
    
    public OpenFileAction(TreeViewer viewer) {
    	this.viewer = viewer;
    }

    public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection)selection).getFirstElement();
		if(obj instanceof IFile) {
			openFile((IFile) obj);
		}
    }

    /**
     * Opens an editor on the given file resource.
     *
     * @param file the file resource
     */
    void openFile(IFile file) {
    	IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	
    	FileEditorInput editorInput = null;
    	
//		IEditorReference[] editors = activePage.getEditorReferences();
//		for (IEditorReference editorReference : editors) {
//			IEditorInput input;
//			try {
//				input = editorReference.getEditorInput();
//			if (input instanceof FileEditorInput) {
//				FileEditorInput fileEditorInput = (FileEditorInput) input;
//				if(fileEditorInput.getStorage().getFullPath().equals(file.getFullPath())) {
//					editorInput = fileEditorInput;
//				}
//			}
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
    	if(editorInput==null) {
    		editorInput = new FileEditorInput(file);
    	}
    	
   	 	IEditorDescriptor editor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
     	String editorId = editor==null ? "org.eclipse.ui.DefaultTextEditor" : editor.getId();
    	try {
			activePage.openEditor(editorInput, editorId);
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

}
