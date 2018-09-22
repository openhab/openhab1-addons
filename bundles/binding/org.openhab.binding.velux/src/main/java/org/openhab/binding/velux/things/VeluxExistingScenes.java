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

import org.openhab.binding.velux.things.VeluxScene.SceneName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combined set of scene informations provided by the <B>Velux</B> bridge,
 * which can be used for later interactions.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link VeluxExistingScenes#isRegistered} for querying existence of a {@link VeluxScene},
 * <LI>{@link VeluxExistingScenes#register} for storing a {@link VeluxScene},
 * <LI>{@link VeluxExistingScenes#get} for retrieval of a {@link VeluxScene},
 * <LI>{@link VeluxExistingScenes#values} for retrieval of all {@link VeluxScene}s,
 * <LI>{@link VeluxExistingScenes#getNoMembers} for retrieval of the number of all {@link VeluxScene}s,
 * <LI>{@link VeluxExistingScenes#toString} for a descriptive string representation.
 * </UL>
 *
 * @see VeluxScene
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxExistingScenes {
    private final Logger logger = LoggerFactory.getLogger(VeluxExistingScenes.class);

    // Type definitions

    private ConcurrentHashMap<String, VeluxScene> existingScenesBySceneName;
    private int memberCount;

    public VeluxExistingScenes() {
        logger.trace("VeluxExistingScenes() initializing.");
        existingScenesBySceneName = new ConcurrentHashMap<String, VeluxScene>();
        memberCount = 0;
    }

    // Class access methods

    public boolean isRegistered(SceneName sceneName) {
        logger.trace("isRegistered({}) returns {}.", sceneName,
                existingScenesBySceneName.containsKey(sceneName.toString()) ? "true" : "false");
        return existingScenesBySceneName.containsKey(sceneName.toString());
    }

    public boolean isRegistered(VeluxScene scene) {
        return isRegistered(scene.getName());
    }

    public boolean register(VeluxScene newScene) {
        logger.trace("register({}) called.", newScene);
        if (isRegistered(newScene)) {
            return false;
        }
        logger.trace("register() registering new scene {}.", newScene);
        existingScenesBySceneName.put(newScene.getName().toString(), newScene);
        memberCount++;
        return true;
    }

    public VeluxScene get(SceneName sceneName) {
        logger.trace("get({}) called.", sceneName);
        if (!isRegistered(sceneName)) {
            return null;
        }
        return existingScenesBySceneName.get(sceneName.toString());
    }

    public VeluxScene[] values() {
        return existingScenesBySceneName.values().toArray(new VeluxScene[0]);
    }

    public int getNoMembers() {
        logger.trace("getNoMembers() returns {}.", memberCount);
        return memberCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(memberCount).append(" members: ");
        for (VeluxScene scene : this.values()) {
            sb.append(scene.toString()).append(",");
        }
        if (sb.lastIndexOf(",") > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }
}

/**
 * end-of-VeluxExistingScenes.java
 */
