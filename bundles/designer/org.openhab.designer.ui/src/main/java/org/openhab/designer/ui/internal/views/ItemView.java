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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.designer.ui.UIActivator;


public class ItemView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.openhab.designer.ui.ItemView";
	
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider, ItemRegistryChangeListener {
		private Object invisibleRoot;

		private ItemRegistry registry;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		
		public void dispose() {
			registry = null;
		}
		
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			return null;
		}

		public Object[] getChildren(Object parent) {
			if(parent==invisibleRoot) {
				if(registry!=null) {
					return getRootGroups(registry.getItems());
				}
			} else if(parent instanceof GroupItem) {
				GroupItem group = (GroupItem) parent;
				return group.getMembers();
			}
			return new Object[0];
		}

		private Object[] getRootGroups(Collection<Item> items) {
			List<GroupItem> allGroups = new LinkedList<GroupItem>();
			List<GroupItem> rootGroups = new LinkedList<GroupItem>();

			// first, let's get all group items
			for(Item item : items) {
				if(item instanceof GroupItem) {
					allGroups.add((GroupItem) item);
				}
			}
			
			// now select only the groups, which are contained in another group
			for(GroupItem group1 : allGroups) {
				boolean found = false;
				for(GroupItem group2 : allGroups) {
					if(ArrayUtils.contains(group2.getMembers(), group1)) {
						found = true;
						break;
					}
				}
				if(!found) rootGroups.add(group1);
			}
			return rootGroups.toArray(new GroupItem[rootGroups.size()]);
		}

		public boolean hasChildren(Object parent) {
			return getChildren(parent).length > 0;
		}

		private void initialize() {
			registry = (ItemRegistry) UIActivator.itemRegistryTracker.getService();
			if(registry!=null) {
				registry.addItemRegistryChangeListener(this);
				invisibleRoot = new Object();
			}
		}

		public void allItemsChanged(Collection<String> oldItemNames) {
			Display display = PlatformUI.getWorkbench().getDisplay();
			if(!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						viewer.refresh();
					}
				});
			}
		}

		public void itemAdded(Item item) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh();
				}
			});
		}

		public void itemRemoved(Item item) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

	class ViewLabelProvider extends LabelProvider {

		private Map<String, Image> imageCache = new HashMap<String, Image>();
		
		@Override
		public void dispose() {
			for(Image image : imageCache.values()) image.dispose();
		}

		public String getText(Object obj) {
			if(obj instanceof Item) {
				Item item = (Item) obj;
				return item.getName();
			} else {
				return obj.toString();
			}
		}
		
		public Image getImage(Object obj) {
			String imageKey = null;
			if (obj instanceof GroupItem) {
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			} else {
		        String imgName = obj.getClass().getSimpleName().replace("Item", "").toLowerCase();
				Image image = imageCache.get(imgName);
				if(image==null) {
					ImageDescriptor imageDesc = UIActivator.getImageDescriptor("icons/" + imgName + ".png");
					if(imageDesc!=null) {
						image = imageDesc.createImage();
						imageCache.put(imgName, image);
						return image;
					}
				} else {
					return image;
				}
				// use the shared image as a default
				imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			}
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ItemView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		contributeToActionBars();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}