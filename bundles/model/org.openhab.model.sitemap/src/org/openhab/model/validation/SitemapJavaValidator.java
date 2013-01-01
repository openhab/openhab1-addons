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
package org.openhab.model.validation;

import org.eclipse.xtext.validation.Check;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapPackage;
import org.openhab.model.sitemap.Widget;
 

public class SitemapJavaValidator extends AbstractSitemapJavaValidator {

	@Check
	public void checkFramesInFrame(Frame frame) {
		for(Widget w : frame.getChildren()) {
			if(w instanceof Frame) {
				error("Frames must not contain other frames", SitemapPackage.Literals.FRAME.getEStructuralFeature(SitemapPackage.FRAME__CHILDREN));
				break;
			}
		}
	}

	@Check
	public void checkFramesInWidgetList(Sitemap sitemap) {
		boolean containsFrames = false;
		boolean containsOtherWidgets = false;
		for(Widget w : sitemap.getChildren()) {
			if(w instanceof Frame) {
				containsFrames = true;
			} else {
				containsOtherWidgets = true;
			}
			if(containsFrames && containsOtherWidgets) {
				error("Sitemap should contain either only frames or none at all", SitemapPackage.Literals.FRAME.getEStructuralFeature(SitemapPackage.SITEMAP__CHILDREN));
				break;
			}
		}
	}

	@Check
	public void checkFramesInWidgetList(LinkableWidget widget) {
		if(widget instanceof Frame) {
			// we have a dedicated check for frames in place
			return;
		}
		boolean containsFrames = false;
		boolean containsOtherWidgets = false;
		for(Widget w : widget.getChildren()) {
			if(w instanceof Frame) {
				containsFrames = true;
			} else {
				containsOtherWidgets = true;
			}
			if(containsFrames && containsOtherWidgets) {
				error("Linkable widget should contain either only frames or none at all", SitemapPackage.Literals.FRAME.getEStructuralFeature(SitemapPackage.LINKABLE_WIDGET__CHILDREN));
				break;
			}
		}
	}

}
