package org.openhab.designer.ui.internal.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.openhab.designer.ui.internal.actions.OpenFileAction;

public class ConfigView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.openhab.designer.ui.ConfigView";
	
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action doubleClickAction;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		contributeToActionBars();
		hookDoubleClickAction();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		drillDownAdapter.addNavigationActions(manager);
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
	}

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
										   ITreeContentProvider {
		private IFolder configRootFolder;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		
		public void dispose() {
			configRootFolder = null; 
		}
		
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (configRootFolder==null) initialize();
				return getChildren(configRootFolder);
			}
			return getChildren(parent);
		}
		
		private void initialize() {
			configRootFolder = ConfigurationFolderProvider.getRootConfigurationFolder();
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
					return folder.members();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		public void dispose() {
			for(Image image : imageCache.values()) image.dispose();
		}
		
		public String getText(Object obj) {
			if(obj instanceof IResource) {
				IResource res = (IResource) obj;
				return res.getName();
			} else {
				return obj.toString();
			}
		}
		
		public Image getImage(Object obj) {
			if (obj instanceof IFolder) {
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

}
