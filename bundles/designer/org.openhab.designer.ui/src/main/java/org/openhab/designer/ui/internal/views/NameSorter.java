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
package org.openhab.designer.ui.internal.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class NameSorter extends ViewerSorter {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if(e1 instanceof IFolder && e2 instanceof IFile) {
			return -1;
		} else if(e1 instanceof IFile && e2 instanceof IFolder) {
			return 1;
		} else if(e1 instanceof IResource && e2 instanceof IResource) {
			IResource r1 = (IResource) e1;
			IResource r2 = (IResource) e2;
			return r1.getName().compareToIgnoreCase(r2.getName());
		} else {
			return super.compare(viewer, e1, e2);
		}
	}
}