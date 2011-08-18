/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.mpd.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openhab.core.types.Type;

public 	class MultiClickDetector<T extends Type> {
	
	private MultiClickListener<T> listener;
	private long multiClickResolution;
	
	private Map<T, List<Long>> cache;
	
	public MultiClickDetector(MultiClickListener<T> listener, long multiClickResolution) {
		this.listener = listener;
		this.multiClickResolution = multiClickResolution;
		cache = new HashMap<T, List<Long>>();
	}		
	
	public void add(T type, long clickTime) {
		if (cache.get(type) == null) {
			cache.put(type, new ArrayList<Long>());
		}
		
		cache.get(type).add(clickTime);
		detectClicks();
	}
			
	protected void detectClicks() {		
		int clickCount = 0;
		
		for (T type : cache.keySet()) {
			
			List<Long> list = cache.get(type);
			if (list != null && !list.isEmpty()) {
				long currentTime = System.currentTimeMillis();
				Iterator<Long> cacheIterator = list.iterator();
				while (cacheIterator.hasNext()) {
					if (currentTime - cacheIterator.next() <= multiClickResolution) {
						clickCount++;
						if (clickCount == 2) {
							listener.onDoubleClick(type);
						}
					}
					else {
						cacheIterator.remove();
					}
				}
			}
			
			listener.onClick(type);
		}
	}
	
	
	public interface MultiClickListener<T extends Type> {
		
		void onClick(T type);
		void onDoubleClick(T type);
		
	}
	
}
