/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.openhab.binding.velux.bridge.comm.BCgetScenes.BCproductState;
import org.openhab.binding.velux.bridge.comm.BCgetScenes.BCscene;

/**
 * <B>Velux</B> scene representation.
 * <P>
 * Combined set of information with references towards multiple Velux product states.
 *
 * @see VeluxProductState
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxScene {

    // Type definitions

    public static class SceneName {
        private String name;

        @Override
        public String toString() {
            return name;
        }

        public SceneName(String name) {
            this.name = name;
        }

        public boolean equals(SceneName anotherName) {
            return this.name.equals(anotherName.toString());
        }
    }

    public static class SceneBridgeIndex {
        private int id;

        @Override
        public String toString() {
            return String.valueOf(id);
        }

        public int toInt() {
            return id;
        }

        SceneBridgeIndex(int id) {
            this.id = id;
        }
    }

    // Class internal

    private SceneName name;
    private SceneBridgeIndex bridgeSceneIndex;
    private boolean silent;
    private VeluxProductState[] productStates;

    // Constructor

    public VeluxScene(BCscene sceneDescr) {
        this.name = new SceneName(sceneDescr.getName());
        this.bridgeSceneIndex = new SceneBridgeIndex(sceneDescr.getId());
        this.silent = sceneDescr.getSilent();

        BCproductState[] productStates = sceneDescr.getProductStates();
        this.productStates = new VeluxProductState[productStates.length];

        for (int i = 0; i < productStates.length; i++) {
            this.productStates[i] = new VeluxProductState(productStates[i]);
        }
    }

    public VeluxScene(String name, int sceneBridgeIndex, boolean silentOperation, VeluxProductState[] actions) {
        this.name = new SceneName(name);
        this.bridgeSceneIndex = new SceneBridgeIndex(sceneBridgeIndex);
        this.silent = silentOperation;
        this.productStates = actions;
    }

    // Class access methods

    public SceneName getName() {
        return this.name;
    }

    public SceneBridgeIndex getBridgeSceneIndex() {
        return this.bridgeSceneIndex;
    }

    @Override
    public String toString() {
        return String.format("Scene \"%s\" (index %d) with %ssilent mode and %d actions", this.name,
                this.bridgeSceneIndex.toInt(), this.silent ? "" : "non-", this.productStates.length);
    }
}

/**
 * end-of-VeluxScene.java
 */
