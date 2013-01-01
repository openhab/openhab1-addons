/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.openhab.designer.ui.UIActivator;

public class ViewLabelProvider extends LabelProvider {

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