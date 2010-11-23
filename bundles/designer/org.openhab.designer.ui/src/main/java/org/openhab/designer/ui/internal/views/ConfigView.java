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

package org.openhab.designer.ui.internal.views;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.openhab.designer.ui.UIActivator;
import org.openhab.designer.ui.internal.actions.OpenFileAction;
import org.openhab.designer.ui.internal.actions.SelectConfigFolderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigView extends ViewPart {

	private static final Logger logger = LoggerFactory.getLogger(ConfigView.class);
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.openhab.designer.ui.ConfigView";
	
	private TreeViewer viewer;
	private Action doubleClickAction;

	private SelectConfigFolderAction selectConfigFolderAction;

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.addDropSupport(DND.DROP_COPY, new Transfer[] { FileTransfer.getInstance() }, new DropListener(viewer));
		viewer.setInput(getViewSite());
		makeActions();
		contributeToActionBars();
		hookDoubleClickAction();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		manager.add(selectConfigFolderAction);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void makeActions() {
		doubleClickAction = new OpenFileAction(viewer);
		selectConfigFolderAction = new SelectConfigFolderAction(viewer);
	}

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 */
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private IFolder configRootFolder;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if(newInput instanceof IFolder) {
				configRootFolder = (IFolder) newInput;
			}
		}
		
		public void dispose() {
			configRootFolder = null; 
		}
		
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				try {
					configRootFolder = ConfigurationFolderProvider.getRootConfigurationFolder();
					if(configRootFolder!=null) {
						return getChildren(configRootFolder);
					} else {
						return new String[] { "<select a configuration folder>" };
					}
				} catch (CoreException e) {
					logger.error("Cannot initialize configuration project in workspace", e);
					return new Object[0];
				}
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if(child.equals(configRootFolder)) return null;
			
			if(child instanceof IResource) {
				IResource res = (IResource) child;
				return res.getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if(parent instanceof IFolder) {
				IFolder folder = (IFolder) parent;
				try {
					IResource[] children = folder.members();
					List<IResource> sortedChildren = new ArrayList<IResource>(children.length);
					for(IResource child : children) {
						if(child instanceof IFolder) {
							sortedChildren.add(child);
						}
					}
					for(IResource child : children) {
						if(child instanceof IFile) {
							sortedChildren.add(child);
						}
					}
					return sortedChildren.toArray(new IResource[sortedChildren.size()]);
				} catch (CoreException e) {
					logger.warn("Error getting children for folder '{}'", folder.getName(), e);
				}
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			return getChildren(parent).length > 0;
		}
	}

	class ViewLabelProvider extends LabelProvider {

		private Map<String, Image> imageCache = new HashMap<String, Image>();
		
		@Override
		public void dispose() {
			for(Image image : imageCache.values()) image.dispose();
		}
		
		public String getText(Object obj) {
			if(obj instanceof IFolder) {
				IResource res = (IResource) obj;
				return StringUtils.capitalize(res.getName());
			} else if(obj instanceof IFile) {
					IResource res = (IResource) obj;
					return res.getName();
			} else {
				return obj.toString();
			}
		}
		
		public Image getImage(Object obj) {
			if (obj instanceof IFolder) {
				IFolder folder = (IFolder) obj;
				String name = folder.getName().toLowerCase();
				Image image = imageCache.get(name);
				if(image==null) {
					ImageDescriptor imageDesc = UIActivator.getImageDescriptor("icons/" + name + ".png");
					if(imageDesc!=null) {
						image = imageDesc.createImage();
						imageCache.put(name, image);
						return image;
					}
				} else {
					return image;
				}
				// use the folder image as a default
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			} else if(obj instanceof IFile) {
				IFile file = (IFile) obj;
				String fileExt = file.getFileExtension();
				Image image = imageCache.get(fileExt);
				if(image==null) {
					ImageDescriptor imageDesc = PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor("." + fileExt);
					image = imageDesc.createImage();
					imageCache.put(fileExt, image);
				}
				return image;	
			} else {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
			}
		}
	}
	
	class NameSorter extends ViewerSorter {
	}
	
	class DropListener extends ViewerDropAdapter {

		protected DropListener(Viewer viewer) {
			super(viewer);
		}
		
		@Override
		public boolean performDrop(Object data) {
			if (data instanceof File) {
				File file = (File) data;
				if(file.isDirectory()) {
					try {
						ConfigurationFolderProvider.setRootConfigurationFolder(file);
						return true;
					} catch (CoreException e) {
						IStatus status = new Status(IStatus.ERROR, UIActivator.PLUGIN_ID, "Cannot set configuration folder", e);
						StatusManager.getManager().handle(status);
					}
				}
			}
			return false;
		}

		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			return true;
		}		
	}

}
