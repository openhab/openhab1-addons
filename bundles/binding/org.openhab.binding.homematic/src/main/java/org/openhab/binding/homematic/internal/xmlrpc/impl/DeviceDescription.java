/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.homematic.internal.xmlrpc.AbstractXmlRpcObject;

/**
 * A DeviceDescription object describes any available device - physical and
 * logical (channel). Not every device specifies each attribute, so depending on
 * the device there can be null values.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class DeviceDescription extends AbstractXmlRpcObject {

    public enum Direction {
        NONE, SENDER, RECEIVER
    }

    public enum Flag {
        VISIBLE, INTERNAL, DONTDELETE
    }

    private String type;
    private String address;
    private String[] paramsets;
    private Integer version;
    private Set<Flag> flags;
    private String[] children;
    private String parent;
    private String parentType;
    private Integer index;
    private Boolean aesActive;
    private String firmware;
    private String availableFirmware;
    private String linkSourceRoles;
    private String linkTargetRoles;
    private Direction direction;
    private String group;
    private String team;
    private String teamTag;
    private String[] teamChannels;
    private String interfaceName;
    private Boolean roaming;

    public DeviceDescription(Map<String, Object> values) {
        super(values);

        if (values.containsKey("TYPE")) {
            type = values.get("TYPE").toString();
        }

        if (values.containsKey("ADDRESS")) {
            address = values.get("ADDRESS").toString();
        }

        children = null;
        Object childrenObj = values.get("CHILDREN");
        if (childrenObj != null) {
            Object[] childrenArray = (Object[]) childrenObj;
            children = Arrays.copyOf(childrenArray, childrenArray.length, String[].class);
        }

        parent = null;
        Object parentObj = values.get("PARENT");
        if (parentObj != null) {
            parent = parentObj.toString();
        }

        parentType = null;
        Object parentTypeObj = values.get("PARENT_TYPE");
        if (parentTypeObj != null) {
            parentType = parentTypeObj.toString();
        }

        index = null;
        Object indexObj = values.get("INDEX");
        if (indexObj != null) {
            index = Integer.parseInt(indexObj.toString());
        }

        aesActive = null;
        Object aesActiveObj = values.get("AES_ACTIVE");
        if (aesActiveObj != null) {
            aesActive = Boolean.parseBoolean(aesActiveObj.toString());
        }

        Object[] paramsetsArray = (Object[]) values.get("PARAMSETS");
        if (paramsetsArray != null) {
            paramsets = Arrays.copyOf(paramsetsArray, paramsetsArray.length, String[].class);
        }

        firmware = null;
        Object firmwareObj = values.get("FIRMWARE");
        if (firmwareObj != null) {
            firmware = firmwareObj.toString();
        }

        if (values.containsKey("VERSION")) {
            version = Integer.parseInt(values.get("VERSION").toString());
        }

        availableFirmware = null;
        Object availFwObj = values.get("AVAILABLE_FIRMWARE");
        if (availFwObj != null) {
            availableFirmware = availFwObj.toString();
        }

        if (values.containsKey("FLAGS")) {
            Integer flagsVal = Integer.parseInt(values.get("FLAGS").toString());
            flags = new HashSet<DeviceDescription.Flag>();
            if ((flagsVal & 1) == 1) {
                flags.add(DeviceDescription.Flag.VISIBLE);
            }
            if ((flagsVal & 2) == 2) {
                flags.add(DeviceDescription.Flag.INTERNAL);
            }
            if ((flagsVal & 4) == 4) {
                flags.add(DeviceDescription.Flag.DONTDELETE);
            }
        }

        linkSourceRoles = null;
        Object linkSRObj = values.get("LINK_SOURCE_ROLES");
        if (linkSRObj != null) {
            linkSourceRoles = linkSRObj.toString();
        }

        linkTargetRoles = null;
        Object linkTRObj = values.get("LINK_TARGET_ROLES");
        if (linkTRObj != null) {
            linkTargetRoles = linkTRObj.toString();
        }

        direction = DeviceDescription.Direction.NONE;
        Object dirObj = values.get("DIRECTION");
        if (dirObj != null) {
            Integer directionVal = Integer.parseInt(dirObj.toString());
            if (directionVal == 2) {
                direction = DeviceDescription.Direction.SENDER;
            }
            if (directionVal == 3) {
                direction = DeviceDescription.Direction.RECEIVER;
            }
        }

        group = null;
        Object grpObj = values.get("GROUP");
        if (grpObj != null) {
            group = grpObj.toString();
        }

        team = null;
        Object teamObj = values.get("TEAM");
        if (teamObj != null) {
            team = teamObj.toString();
        }

        teamTag = null;
        Object teamTagObj = values.get("TEAM_TAG");
        if (teamTagObj != null) {
            teamTag = teamTagObj.toString();
        }

        teamChannels = null;
        Object teamChObj = values.get("TEAM_CHANNELS");
        if (teamChObj != null) {
            Object[] teamChannelsArray = (Object[]) values.get("TEAM_CHANNELS");
            teamChannels = Arrays.copyOf(teamChannelsArray, teamChannelsArray.length, String[].class);
        }

        interfaceName = null;
        Object ifaceNameObj = values.get("INTERFACE");
        if (ifaceNameObj != null) {
            interfaceName = ifaceNameObj.toString();
        }

        roaming = null;
        Object roamObj = values.get("ROAMING");
        if (roamObj != null) {
            roaming = Boolean.parseBoolean(roamObj.toString());
        }
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String[] getParamsets() {
        return paramsets;
    }

    public Integer getVersion() {
        return version;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public String[] getChildren() {
        return children;
    }

    public String getParent() {
        return parent;
    }

    public String getParentType() {
        return parentType;
    }

    public Integer getIndex() {
        return index;
    }

    public Boolean getAesActive() {
        return aesActive;
    }

    public String getFirmware() {
        return firmware;
    }

    public String getAvailableFirmware() {
        return availableFirmware;
    }

    public String getLinkSourceRoles() {
        return linkSourceRoles;
    }

    public String getLinkTargetRoles() {
        return linkTargetRoles;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getGroup() {
        return group;
    }

    public String getTeam() {
        return team;
    }

    public String getTeamTag() {
        return teamTag;
    }

    public String[] getTeamChannels() {
        return teamChannels;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Boolean getRoaming() {
        return roaming;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeviceDescription [address=" + address + ", aesActive=" + aesActive + ", availableFirmware=" + availableFirmware
                + ", children=" + Arrays.toString(children) + ", direction=" + direction + ", firmware=" + firmware + ", flags=" + flags
                + ", group=" + group + ", index=" + index + ", interfaceName=" + interfaceName + ", linkSourceRoles=" + linkSourceRoles
                + ", linkTargetRoles=" + linkTargetRoles + ", paramsets=" + Arrays.toString(paramsets) + ", parent=" + parent
                + ", parentType=" + parentType + ", roaming=" + roaming + ", team=" + team + ", teamChannels="
                + Arrays.toString(teamChannels) + ", teamTag=" + teamTag + ", type=" + type + ", version=" + version + "]";
    }

}
