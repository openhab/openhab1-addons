package org.openhab.designer.ui.internal.views;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemRegistry;
import org.openhab.designer.ui.UIActivator;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ItemView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.openhab.designer.ui.ItemView";
	
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider, ItemChangeListener {
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
				registry.addItemChangeListener(this);
				invisibleRoot = new Object();
			}
		}

		public void allItemsChanged(ItemProvider provider) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh();
				}
			});
		}

		public void itemAdded(ItemProvider provider, Item item) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh();
				}
			});
		}

		public void itemRemoved(ItemProvider provider, Item item) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

	class ViewLabelProvider extends LabelProvider {

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