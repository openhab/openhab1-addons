/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.openhab.io.habmin.HABminApplication;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.persistence.persistence.AllConfig;
import org.openhab.model.persistence.persistence.GroupConfig;
import org.openhab.model.persistence.persistence.ItemConfig;
import org.openhab.model.persistence.persistence.PersistenceConfiguration;
import org.openhab.model.persistence.persistence.PersistenceModel;
import org.openhab.model.persistence.persistence.Strategy;
import org.openhab.model.persistence.persistence.impl.CronStrategyImpl;
import org.openhab.model.persistence.persistence.impl.GroupConfigImpl;
import org.openhab.model.persistence.persistence.impl.ItemConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
public class PersistenceModelHelper {
	private static final Logger logger = LoggerFactory.getLogger(PersistenceModelHelper.class);

	private static ModelRepository repo;
	private static PersistenceModel models;
	private static String serviceName;
	private static String modelFile;

	public PersistenceModelHelper(String service) {
		repo = HABminApplication.getModelRepository();
		if (repo == null)
			return;

		modelFile = new String(service + ".persist");
		models = (PersistenceModel) repo.getModel(modelFile);
		if (models == null) {
			logger.error("Can't open model file '{}'", service);
			return;
		}

		serviceName = new String(service);
	}

	boolean setItemPersistence(String itemName, ItemPersistenceBean bean) {
		// Make sure the model is open
		if (models == null)
			return false;

		String orgName = "configurations/persistence/" + modelFile;
		String newName = "configurations/persistence/" + modelFile + ".new";
		String bakName = "configurations/persistence/" + modelFile + ".bak";

		try {
			// boolean itemSaved = deleteItem;

			FileWriter fw = null;
			fw = new FileWriter(newName, false);
			BufferedWriter out = new BufferedWriter(fw);

			out.write("// Persistence strategies have a name and a definition and are referred to in the \"Items\" section\r\n");

			// List<PersistenceConfiguration> configList = models.getConfigs();
			// for (PersistenceConfiguration config : configList) {
			out.write("Strategies {\r\n");

			// Write the strategies section
			List<Strategy> strategyList = models.getStrategies();
			for (int cnt = 0; cnt < strategyList.size(); cnt++) {
				Strategy modelStrategy = strategyList.get(cnt);
				if (modelStrategy instanceof CronStrategyImpl) {
					CronStrategyImpl cronStrategy = (CronStrategyImpl) modelStrategy;

					out.write("\t" + cronStrategy.getName() + "\t: \"" + cronStrategy.getCronExpression() + "\"\r\n");
				}
			}

			out.write("\r\n\t// If no strategy is specified for an item entry below, the default list will be used.");

			out.write("\r\n\tdefault = ");

			List<Strategy> defaultList = models.getDefaults();
			for (int cnt = 0; cnt < defaultList.size(); cnt++) {
				Strategy modelStrategy = defaultList.get(cnt);
				out.write(modelStrategy.getName() + " ");
			}

			out.write("\r\n}\r\n\r\n");
			out.write("/*\r\n");
			out.write(" * Each line in this section defines for which item(s) which strategy(ies) should be applied.\r\n");
			out.write(" * You can list single items, use \"*\" for all items or \"groupitem*\" for all members of a group\r\n");
			out.write(" * item (excl. the group item itself).\r\n");
			out.write(" */\r\n");

			out.write("Items {\r\n");

			List<PersistenceConfiguration> configList = models.getConfigs();
			for (PersistenceConfiguration config : configList) {

				for (int cnt = 0; cnt < config.getItems().size(); cnt++) {
					EObject modelItem = config.getItems().get(cnt);
					if (modelItem instanceof GroupConfig) {
						GroupConfig group = (GroupConfig) modelItem;
						out.write("\t" + group.getGroup() + "*");
					}
					if (modelItem instanceof ItemConfig) {
						ItemConfig item = (ItemConfig) modelItem;

						// Check for a match on the item we want to save
						if (item.getItem().equals(itemName))
							continue;

						out.write("\t" + item.getItem());
					}
					if (modelItem instanceof AllConfig) {
						out.write("\t*");
					}

					out.write(" : strategy = ");
					strategyList = config.getStrategies();
					for (int scnt = 0; scnt < strategyList.size(); scnt++) {
						if (scnt != 0)
							out.write(", ");
						Strategy modelStrategy = strategyList.get(scnt);
						out.write(modelStrategy.getName());
					}
					out.write("\r\n");
				}
			}

			// Now save the item
			if (bean.itemstrategies != null && bean.itemstrategies.size() > 0) {
				List<String> strategies = new ArrayList<String>();

				// Make sure the new item has strategies
				for (String s : bean.itemstrategies) {
					if (s != null && s.length() > 0) {
						strategies.add(s);
					}
				}

				if (strategies.size() > 0) {
					out.write("\t" + itemName + " : strategy = ");
					boolean first = true;
					for (String strategy : strategies) {
						if (first == false)
							out.write(", ");
						out.write(strategy);
						first = false;
					}
				}
			}

			out.write("\r\n}\r\n");
			out.close();
			fw.close();

			// Rename the files.
			File bakFile = new File(bakName);
			File orgFile = new File(orgName);
			File newFile = new File(newName);

			// Delete any existing .bak file
			if (bakFile.exists())
				bakFile.delete();

			// Rename the existing item file to backup
			if(orgFile.renameTo(bakFile) == true) {
				// Rename the new file to the item file
				newFile.renameTo(orgFile);	
			}
			else {
				logger.error("Error renaming file {} to {}", orgFile.getName(), bakFile.getName());
			}

			// Update the model repository
			InputStream inFile;
			try {
				inFile = new FileInputStream(orgName);
				repo.addOrRefreshModel(modelFile, inFile);
			} catch (FileNotFoundException e) {
				logger.error("Error refreshing item file " + modelFile + ":", e);
			}
		} catch (IOException e) {
			// logger.error("Error writing item file " + modelName + ":", e);
		}

		return true;
	}

	public ItemPersistenceBean getItemPersistence(String itemName, List<String> itemGroups) {
		// Make sure the model is open
		if (models == null)
			return null;

		ItemPersistenceBean bean = new ItemPersistenceBean();
		bean.service = serviceName;
		bean.itemstrategies = new ArrayList<String>();
		bean.groupstrategies = new ArrayList<String>();

		List<PersistenceConfiguration> configList = models.getConfigs();
		for (PersistenceConfiguration config : configList) {
			for (int cnt = 0; cnt < config.getItems().size(); cnt++) {
				EObject modelItem = config.getItems().get(cnt);
				if (modelItem instanceof GroupConfigImpl) {
					for (String group : itemGroups) {
						if (((GroupConfigImpl) modelItem).getGroup().equalsIgnoreCase(group)) {
							for (int str = 0; str < config.getStrategies().size(); str++) {
								Strategy strategyItem = config.getStrategies().get(str);
								bean.groupstrategies.add(strategyItem.getName());
							}
						}
					}
				}
				if (modelItem instanceof ItemConfigImpl) {
					if (((ItemConfigImpl) modelItem).getItem().equals(itemName)) {
						for (int str = 0; str < config.getStrategies().size(); str++) {
							Strategy strategyItem = config.getStrategies().get(str);
							bean.itemstrategies.add(strategyItem.getName());
						}
					}
				}
			}
		}

		// If there are no strategies for this item, then return null
		if (bean.itemstrategies.isEmpty() && bean.groupstrategies.isEmpty())
			return null;

		return bean;
	}

	List<PersistenceStrategyBean> getPersistenceStrategies() {
		// Make sure the model is open
		if (models == null)
			return null;

		ArrayList<PersistenceStrategyBean> strategies;

		strategies = new ArrayList<PersistenceStrategyBean>();
		List<Strategy> configList = models.getStrategies();
		for (int cnt = 0; cnt < configList.size(); cnt++) {
			EObject modelItem = configList.get(cnt);
			if (modelItem instanceof CronStrategyImpl) {
				CronStrategyImpl cronStrategy = (CronStrategyImpl) modelItem;
				PersistenceStrategyBean bean = new PersistenceStrategyBean();
				bean.name = cronStrategy.getName();
				bean.cron = cronStrategy.getCronExpression();

				strategies.add(bean);
			}
		}

		// Add the two system strategies that won't appear in the list
		PersistenceStrategyBean bean = new PersistenceStrategyBean();
		bean.name = "everyChange";
		strategies.add(bean);
		bean = new PersistenceStrategyBean();
		bean.name = "everyUpdate";
		strategies.add(bean);

		bean = new PersistenceStrategyBean();
		bean.name = "restoreOnStartup";
		strategies.add(bean);

		if (strategies.isEmpty())
			return null;

		return strategies;
	}
}
