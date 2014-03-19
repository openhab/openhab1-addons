/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
