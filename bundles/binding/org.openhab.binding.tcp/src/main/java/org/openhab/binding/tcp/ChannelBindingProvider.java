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
package org.openhab.binding.tcp;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;

/**
 * 
 * the ChannelBindingProvider interface is just a "base" interface
 * that ensures us that each binding config somehow defines or captures
 * host:ip information. Obviously, more specialised sub-classes can 
 * define additional binding configuration/information as required
 * 
 * @author Karel Goderis
 *
 */
public interface ChannelBindingProvider extends BindingProvider {

	// return a list of internet socket addresses for the given Item
	public List<InetSocketAddress> getInetSocketAddresses(String itemName);

	// return the internet socket address for the given Item and Command
	public InetSocketAddress getInetSocketAddress(String itemName, Command command);

	// return the hostname for the given Item and Command
	public String getHost(String itemName, Command command);

	// return the portnumber for the given Item and Command
	public int getPort(String itemName, Command command);

	// return a collection of all the Items that have host:port in their configuration
	public Collection<String> getItemNames(String host, int port);


}
