/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class provides a storage class for zwave association groups
 * within the node class. This is then serialised to XML.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XStreamAlias("associationGroup")
public class AssociationGroup {
	int Index;
	List<Integer> Nodes = new ArrayList<Integer>();
	
	public AssociationGroup(int index) {
		Index = index;
	}

	public int getIndex() {
		return Index;
	}
	
	public void setIndex(int newIndex) {
		Index = newIndex;
	}
	
	public void addNode(int Node) {
		Nodes.add(Node);
	}
	
	public List<Integer> getNodes() {
		return Nodes;
	}
	
}
