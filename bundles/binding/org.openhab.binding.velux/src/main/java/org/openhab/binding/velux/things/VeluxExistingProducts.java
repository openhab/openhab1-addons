/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combined set of product informations provided by the <B>Velux</B> bridge,
 * which can be used for later interactions.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link VeluxExistingProducts#isRegistered} for querying existence of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#register} for storing a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#get} for retrieval of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#values} for retrieval of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#getNoMembers} for retrieval of the number of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#toString} for a descriptive string representation.
 * </UL>
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxExistingProducts {
	private final Logger logger = LoggerFactory.getLogger(VeluxExistingProducts.class);

	// Type definitions

	private ConcurrentHashMap<String, VeluxProduct> existingProductsByUniqueIndex;
	private ConcurrentHashMap<Integer, String> bridgeIndexToSerialNumber;
	private int memberCount;

	public VeluxExistingProducts() {
		logger.trace("VeluxExistingProducts() initializing.");
		existingProductsByUniqueIndex = new ConcurrentHashMap<String, VeluxProduct>();
		bridgeIndexToSerialNumber = new ConcurrentHashMap<Integer, String>();
		memberCount = 0;
	}

	// Class access methods

	public boolean isRegistered(String productUniqueIndexOrSerialNumber) {
		logger.trace("isRegistered({}) returns {}.", productUniqueIndexOrSerialNumber,
				existingProductsByUniqueIndex.containsKey(productUniqueIndexOrSerialNumber) ? "true" : "false");
		return existingProductsByUniqueIndex.containsKey(productUniqueIndexOrSerialNumber);
	}

	public boolean isRegistered(VeluxProduct product) {
		logger.trace("isRegistered({}) called.", product);
		if (product.isV2()) {
			return isRegistered(product.getProductUniqueIndex());

		} else {
			return isRegistered(product.getSerialNumber());
		}
	}

	public boolean isRegistered(ProductBridgeIndex bridgeProductIndex) {
		logger.trace("isRegistered({}) called.", bridgeProductIndex.toString());
		if (!bridgeIndexToSerialNumber.containsKey(bridgeProductIndex.toInt())) {
			return false;
		}
		return isRegistered(bridgeIndexToSerialNumber.get(bridgeProductIndex.toInt()));
	}

	public boolean register(VeluxProduct newProduct) {
		logger.trace("register({}) called.", newProduct);
		if (isRegistered(newProduct)) {
			return false;
		}
		logger.trace("register() registering new product {}.", newProduct);
		
		if (newProduct.isV2()) {
			logger.trace("register() registering by SerialNumber {}",newProduct.getSerialNumber());
			existingProductsByUniqueIndex.put(newProduct.getSerialNumber(), newProduct);
		} else {
			logger.trace("register() registering by UniqueIndex {}", newProduct.getProductUniqueIndex());
			existingProductsByUniqueIndex.put(newProduct.getProductUniqueIndex(), newProduct);
		}
		logger.trace("register() registering by ProductBridgeIndex {}", newProduct.getBridgeProductIndex().toInt());
		bridgeIndexToSerialNumber.put(newProduct.getBridgeProductIndex().toInt(), newProduct.getSerialNumber());

		memberCount++;
		return true;
	}
	
	public boolean update(ProductBridgeIndex bridgeProductIndex, int productState, int productPosition, int productTarget) {
		logger.trace("update(bridgeProductIndex={},productState={},productPosition={},productTarget={}) called.",
				bridgeProductIndex.toInt(),productState,productPosition,productTarget);
		if (!isRegistered(bridgeProductIndex)) {
			return false;
		}
		logger.trace("update() updating product {}.", bridgeProductIndex.toInt());
		VeluxProduct thisProduct = this.get(bridgeProductIndex);
		thisProduct.setState(productState);
		thisProduct.setCurrentPosition(productPosition);
		thisProduct.setTarget(productTarget);
		if (thisProduct.isV2()) {
			logger.trace("update() updating by SerialNumber {}",thisProduct.getSerialNumber());
			existingProductsByUniqueIndex.replace(thisProduct.getSerialNumber(), thisProduct);
		} else {
			logger.trace("update() updating by UniqueIndex {}", thisProduct.getProductUniqueIndex());
			existingProductsByUniqueIndex.replace(thisProduct.getProductUniqueIndex(), thisProduct);
		}
		return true;
	}

	public VeluxProduct get(String productUniqueIndexOrSerialNumber) {
		logger.trace("get({}) called.", productUniqueIndexOrSerialNumber);
		if (!isRegistered(productUniqueIndexOrSerialNumber)) {
			return null;
		}
		return existingProductsByUniqueIndex.get(productUniqueIndexOrSerialNumber);
	}

	public VeluxProduct get(ProductBridgeIndex bridgeProductIndex) {
		logger.trace("get({}) called.", bridgeProductIndex);
		if (!isRegistered(bridgeProductIndex)) {
			return null;
		}
		return existingProductsByUniqueIndex.get(bridgeIndexToSerialNumber.get(bridgeProductIndex.toInt()));
	}

	public VeluxProduct[] values() {
		return existingProductsByUniqueIndex.values().toArray(new VeluxProduct[0]);
	}

	public int getNoMembers() {
		logger.trace("getNoMembers() returns {}.", memberCount);
		return memberCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(memberCount).append(" members: ");
		for (VeluxProduct product : this.values()) {
			sb.append(product.toString()).append(",");
		}
		if (sb.lastIndexOf(",") > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
}

/**
 * end-of-VeluxExistingProducts.java
 */
