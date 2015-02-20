package org.openhab.binding.nest.internal.api.listeners;

import org.openhab.binding.nest.internal.api.model.Structure;

public interface StructureListener {
    /**
     * Called when updated data is retrieved for a user's structure.
     * @param structure the new data for the structure (guaranteed
     *                  to be non-null)
     */
    void onStructureUpdated(Structure structure);
}
