/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.systeminfo.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.ptql.ProcessFinder;
import org.openhab.binding.systeminfo.SysteminfoBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for system and process information gathering.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class SysteminfoBinding extends AbstractActiveBinding<SysteminfoBindingProvider>
		implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SysteminfoBinding.class);

	/** the interval to find new refresh candidates (defaults to 1000 milliseconds) */
	private int granularity = 1000;
	/** the unit to measure keyfacts (defaults to 'M') */
	private char units = 'M';

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	private static Sigar sigarImpl;
	private static SigarProxy sigar;


	public void activate() {
	}

	public void deactivate() {
		sigar = null;
		sigarImpl = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "Systeminfo Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (SysteminfoBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				int refreshInterval = provider.getRefreshInterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {

					logger.debug("item '{}' is about to be refreshed now", itemName);

					SysteminfoCommandType commmandType = provider.getCommandType(itemName);
					Class<? extends Item> itemType = provider.getItemType(itemName);
					String target = provider.getTarget(itemName);

					State state = getData(commmandType, itemType, target);

					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					} else {
						logger.error("No response received from command '{}'", commmandType);
					}

					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}
		}
	}

	private State getData(SysteminfoCommandType commandType, Class<? extends Item> itemType, String target) {
		State state = UnDefType.UNDEF;
		long pid;

		try {
			switch (commandType) {
			case LOAD_AVERAGE_1MIN:
				state = new DecimalType(sigar.getLoadAverage()[0]);
				break;
			case LOAD_AVERAGE_5MIN:
				state = new DecimalType(sigar.getLoadAverage()[1]);
				break;
			case LOAD_AVERAGE_15MIN:
				state = new DecimalType(sigar.getLoadAverage()[2]);
				break;

			case CPU_COMBINED:
				state = new DecimalType(sigar.getCpuPerc().getCombined() * 100);
				break;
			case CPU_USER:
				state = new DecimalType(sigar.getCpuPerc().getUser() * 100);
				break;
			case CPU_SYSTEM:
				state = new DecimalType(sigar.getCpuPerc().getSys() * 100);
				break;
			case CPU_NICE:
				state = new DecimalType(sigar.getCpuPerc().getNice() * 100);
				break;
			case CPU_WAIT:
				state = new DecimalType(sigar.getCpuPerc().getWait() * 100);
				break;

			case UPTIME:
				state = new DecimalType(sigar.getUptime().getUptime());
				break;
			case UPTIME_FORMATTED:
				state = new StringType(getElapsedTime((long) sigar.getUptime().getUptime()));
				break;

			case MEM_FREE_PERCENT:
				state = new DecimalType(sigar.getMem().getFreePercent());
				break;
			case MEM_USED_PERCENT:
				state = new DecimalType(sigar.getMem().getUsedPercent());
				break;
			case MEM_FREE:
				state = new DecimalType(formatBytes(sigar.getMem().getFree(), units));
				break;
			case MEM_USED:
				state = new DecimalType(formatBytes(sigar.getMem().getUsed(), units));
				break;
			case MEM_ACTUAL_FREE:
				state = new DecimalType(formatBytes(sigar.getMem().getActualFree(), units));
				break;
			case MEM_ACTUAL_USED:
				state = new DecimalType(formatBytes(sigar.getMem().getActualUsed(), units));
				break;
			case MEM_TOTAL:
				state = new DecimalType(formatBytes(sigar.getMem().getTotal(), units));
				break;

			case SWAP_FREE:
				state = new DecimalType(formatBytes(sigar.getSwap().getFree(), units));
				break;
			case SWAP_TOTAL:
				state = new DecimalType(formatBytes(sigar.getSwap().getTotal(), units));
				break;
			case SWAP_USED:
				state = new DecimalType(formatBytes(sigar.getSwap().getUsed(), units));
				break;
			case SWAP_PAGE_IN:
				state = new DecimalType(formatBytes(sigar.getSwap().getPageIn(), units));
				break;
			case SWAP_PAGE_OUT:
				state = new DecimalType(formatBytes(sigar.getSwap().getPageOut(), units));
				break;

			case NET_RX_BYTES:
				state = new DecimalType(formatBytes(
						sigar.getNetInterfaceStat(target).getRxBytes(), units));
				break;
			case NET_TX_BYTES:
				state = new DecimalType(formatBytes(
						sigar.getNetInterfaceStat(target).getTxBytes(), units));
				break;

			case DISK_READS:
				state = new DecimalType(sigar.getDiskUsage(target).getReads());
				break;
			case DISK_WRITES:
				state = new DecimalType(sigar.getDiskUsage(target).getWrites());
				break;
			case DISK_READ_BYTES:
				state = new DecimalType(formatBytes(sigar.getDiskUsage(target).getReadBytes(), units));
				break;
			case DISK_WRITE_BYTES:
				state = new DecimalType(formatBytes(sigar.getDiskUsage(target).getWriteBytes(), units));
				break;

			case DIR_USAGE:
				state = new DecimalType(formatBytes(sigar.getDirUsage(target).getDiskUsage(), units));
				break;
			case DIR_FILES:
				state = new DecimalType(sigar.getDirUsage(target).getFiles());
				break;

			case PROCESS_REAL_MEM:
				pid = getPid(target);
				state = new DecimalType(formatBytes(sigar.getProcMem(pid).getResident(), units));
				break;
			case PROCESS_VIRTUAL_MEM:
				pid = getPid(target);
				state = new DecimalType(formatBytes(sigar.getProcMem(pid)
						.getSize(), units));
				break;
			case PROCESS_CPU_PERCENT:
				pid = getPid(target);
				state = new DecimalType(
						sigar.getProcCpu(pid).getPercent() * 100);
				break;
			case PROCESS_CPU_SYSTEM:
				pid = getPid(target);
				state = new DecimalType(sigar.getProcCpu(pid).getSys());
				break;
			case PROCESS_CPU_USER:
				pid = getPid(target);
				state = new DecimalType(sigar.getProcCpu(pid).getUser());
				break;
			case PROCESS_CPU_TOTAL:
				pid = getPid(target);
				state = new DecimalType(sigar.getProcCpu(pid).getTotal());
				break;
			case PROCESS_UPTIME:
				pid = getPid(target);
				state = new DecimalType(getProcessUptime(pid));
				break;
			case PROCESS_UPTIME_FORMATTED:
				pid = getPid(target);
				state = new StringType(getElapsedTime(getProcessUptime(pid)));
				break;

			default: break;
			}

		} catch (SigarException e) {
			logger.error("Error occured while reading KPI's", e);
		}

		return state;
	}

	private long getProcessUptime(long pid) throws SigarException {
		long processStartTime = sigar.getProcTime(pid).getStartTime();
		long currentTime = System.currentTimeMillis();
		return (currentTime - processStartTime) / 1000;
	}

	private long getPid(String processName) throws SigarException {
		long pid;

		ProcessFinder processFinder = new ProcessFinder(sigarImpl);
		String query;

		if (processName.equals("$$")) {
			pid = sigar.getPid();
			logger.debug("Return own pid {}", pid);
			return pid;
		} else if (processName.startsWith("*")) {
			query = "State.Name.sw=" + processName.replace("*", "");
		} else if (processName.endsWith("*")) {
			query = "State.Name.ew=" + processName.replace("*", "");
		} else if (processName.startsWith("=")) {
			query = "State.Name.eq=" + processName.replace("=", "");
		} else if (processName.startsWith("#")) {
			query = processName.replace("#", "");
		} else {
			query = "State.Name.ct=" + processName;
		}

		logger.debug("Query pid by '{}'", query);
		pid = processFinder.findSingleProcess(query);

		logger.debug("Return pid {}", pid);
		return pid;
	}

	private static String getElapsedTime(long sec) {

		final int SECOND = 1;
		final int MINUTE = 60 * SECOND;
		final int HOUR = 60 * MINUTE;
		final int DAY = 24 * HOUR;

		StringBuffer text = new StringBuffer("");

		if (sec > DAY) {
			if (sec < (2 * DAY)) {
				text.append(sec / DAY).append(" day ");
			} else {
				text.append(sec / DAY).append(" days ");
			}
			sec %= DAY;
		}

		text.append(sec / HOUR).append(":");
		sec %= HOUR;
		text.append(String.format("%02d", sec / MINUTE));

		return text.toString();
	}

	private double formatBytes(double value, char units) {
		double retval = 0;

		switch (units) {
			case 'K':
				retval = value / 1024;
				break;
			case 'M':
				retval = value / (1024 * 1024);
				break;
			case 'G':
				retval = value / (1024 * 1024 * 1024);
				break;
			case 'B':
			default:
				retval = value;
				break;
		}

		return retval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}

			logger.debug("Granularity: {} ms", granularity);

			String tmp = (String) config.get("units");
			if (StringUtils.isNotBlank(tmp)) {
				if (tmp.length() != 1) {
					throw new ConfigurationException("units", "Illegal units length");
				}

				if (!tmp.matches("[BKMGT]")) {
					throw new ConfigurationException("units", "Illegal units");
				}

				units = tmp.charAt(0);
			}

			logger.debug("Using units: {}", units);
		}
		
		initializeSystemMonitor();
		setProperlyConfigured(true);
	}
	
	private void initializeSystemMonitor() {
		if (sigarImpl == null) {
			sigarImpl = new Sigar();
		}

		if (sigar == null) {
			sigar = SigarProxyCache.newInstance(sigarImpl, 1000);
		}

		logger.info("Using Sigar version {}", Sigar.VERSION_STRING);

		try {
			String[] interfaces = sigar.getNetInterfaceList();
			logger.debug("valid net interfaces: {}", Arrays.toString(interfaces));

			FileSystem[] filesystems = sigar.getFileSystemList();
			logger.debug("file systems: {}", Arrays.toString(filesystems));

			List<String> disks = new ArrayList<String>();
			for (int i = 0; i < filesystems.length; i++) {
			    FileSystem fs = filesystems[i];
			    if (fs.getType() == FileSystem.TYPE_LOCAL_DISK){
			    	disks.add(fs.getDevName());
			    }
			}
			
			logger.debug("valid disk names: {}", Arrays.toString(disks.toArray()));
		} catch (SigarException e) {
			logger.error("System monitor error: {}", e);
		}
	}
	
}
