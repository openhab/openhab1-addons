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
package org.openhab.core.scriptengine;

import java.util.List;

import org.eclipse.xtext.xbase.XExpression;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupFunction;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.scriptengine.internal.ScriptEngineActivator;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.core.ModelRepository;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
@SuppressWarnings("restriction")
public class ScriptGroupFunction implements GroupFunction {
	
	private String groupItemName;
	
	
	public ScriptGroupFunction(String groupItemName) {
		this.groupItemName = groupItemName;
	}
	
	private Item getGroupItem(String groupItemName) {
		ItemRegistry itemReg = ScriptEngineActivator.itemRegistryTracker.getService();
		if (itemReg != null) {
			try {
				return itemReg.getItem(groupItemName);
			} catch (ItemNotFoundException e) {
				e.printStackTrace();
			} catch (ItemNotUniqueException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public State calculate(List<Item> items) {
		if (items.size() > 0) {
			State state = items.get(0).getState(); 
			for (int itemIndex =1; itemIndex < items.size(); itemIndex++) {
				items.get(itemIndex).getState()
				
				if(!state.equals()) {
					return UnDefType.UNDEF;
				}
			}
			return state;
		} else {
			return UnDefType.UNDEF;
		}
	}

	/**
	 * Calls a script which must be located in the configurations/scripts folder.
	 * 
	 * @param scriptName the name of the script (without the .script extension)
	 * 
	 * @return the return value of the script
	 * @throws ScriptExecutionException if an error occured during the execution
	 */
	public static Object callScript(String scriptName) throws ScriptExecutionException {
		ModelRepository repo = ScriptEngineActivator.modelRepositoryTracker.getService();
		if(repo!=null) {
			XExpression expr = (XExpression) repo.getModel(scriptName + ".script");
			if(expr!=null) {
				ScriptEngine scriptEngine = ScriptEngineActivator.scriptEngineTracker.getService();
				if(scriptEngine!=null) {
					Script script = scriptEngine.newScriptFromXExpression(expr);
					return script.execute();
				} else {
					throw new ScriptExecutionException("Script engine is not available.");
				}
			} else {
				throw new ScriptExecutionException("Script '" + scriptName + " cannot be found.");
			}
		} else {
			throw new ScriptExecutionException("Model repository is not available.");
		}
	}
	
	public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
		State state = calculate(items);
		if(stateClass.isInstance(state)) {
			return state;
		} else {
			return null;
		}
	}

}
