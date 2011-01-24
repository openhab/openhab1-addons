package org.openhab.core.binding;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface BindingProvider {

	/**
	 * Adds a binding change listener, which gets notified whenever there 
	 * are changes in the binding configuration
	 * 
	 * @param listener the binding change listener to add
	 */
	public void addBindingChangeListener(BindingChangeListener<? extends BindingProvider> listener);

	/**
	 * Removes a binding change listener again.
	 * Does nothing, if this listener has not been added before.
	 * 
	 * @param listener the binding listener to remove
	 */
	public void removeBindingChangeListener(BindingChangeListener<? extends BindingProvider> listener);

}
