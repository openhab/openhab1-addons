/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velux.things;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * The {@link VeluxKLFAPI} class defines common KLF200 API constants, which are
 * used across the whole binding.
 * <P>
 * It provides the Enumeration of available API message identifiers as well as
 * constants which describe the KLF200 API restrictions.
 * <P>
 * Classes/Enumeration available:
 * <UL>
 * <LI>Enumeration {@link Command} provides command name, coding and description.</LI>
 * <LI>Class {@link CommandName} to handle symbolic API names.</LI>
 * <LI>Class {@link CommandNumber} to handle API code.</LI>
 * </UL>
 * Constants available:
 * <UL>
 * <LI>{@link #KLF_SYSTEMTABLE_MAX} provides limits of the System table.</LI>
 * </UL>
 * <P>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class VeluxKLFAPI {

    // Constants

    /**
     * System table index parameter - an be a number from 0 to 203.
     *
     * See <a href=
     * "https://velcdn.azureedge.net/~/media/com/api/klf200/technical%20specification%20for%20klf%20200%20api.pdf#page=25">KLF200
     * System table</a>
     */
    public static final int KLF_SYSTEMTABLE_MAX = 203;

    // Type definitions

    /**
     * Handle symbolic names of the {@link VeluxKLFAPI}.
     * <P>
     * Methods available:
     * <UL>
     * <LI>Constructor {@link CommandName} by String.</LI>
     * <LI>Method {@link toString} to return a String.</LI>
     * </UL>
     */
    public static class CommandName {
        private String name;

        CommandName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    /**
     * Handle API codings of the {@link VeluxKLFAPI}.
     * <P>
     * Methods available:
     * <UL>
     * <LI>CommandNumber {@link CommandName} by short.</LI>
     * <LI>Method {@link toShort} to return a short.</LI>
     * <LI>Method {@link toString} to return a well-formatted String.</LI>
     * </UL>
     */
    public static class CommandNumber {
        private short commandNumber;

        public CommandNumber(short commandNumber) {
            this.commandNumber = commandNumber;
        }

        public short toShort() {
            return commandNumber;
        }

        @Override
        public String toString() {
            return "0x" + Integer.toHexString(new Short(commandNumber).intValue());
        }
    }

    /**
     * Enumeration of complete API as definition of a
     * List of Gateway commands.
     * <P>
     * See <a href=
     * "https://velcdn.azureedge.net/~/media/com/api/klf200/technical%20specification%20for%20klf%20200%20api.pdf#page=115">Appendix
     * 3: List of Gateway commands</a>
     * <P>
     * Methods available:
     * <UL>
     * <LI>Constructor {@link Command} by String.</LI>
     * <LI>Method {@link getCommand} to return a {@link CommandNumber}.</LI>
     * <LI>Method {@link getDescription} to return a description as String.</LI>
     * <LI>Method {@link get} to return a {@link Command} based on the given <B>int</B>.</LI>
     * </UL>
     */
    public enum Command {
        // Special item: Shutdown of the connection
        GW_OPENHAB_CLOSE((short) -2, "openHAB connection shutdown command."),
        // Special item: unrecognized command
        UNDEFTYPE((short) -1, "Unknown command."),
        GW_ERROR_NTF((short) 0x0000, "Provides information on what triggered the error."),
        GW_REBOOT_REQ((short) 0x0001, "Request gateway to reboot."),
        GW_REBOOT_CFM((short) 0x0002, "Acknowledge to GW_REBOOT_REQ command."),
        GW_SET_FACTORY_DEFAULT_REQ((short) 0x0003,
                "Request gateway to clear system table, scene table and set Ethernet settings to factory default. Gateway will reboot."),
        GW_SET_FACTORY_DEFAULT_CFM((short) 0x0004, "Acknowledge to GW_SET_FACTORY_DEFAULT_REQ command."),
        GW_GET_VERSION_REQ((short) 0x0008, "Request version information."),
        GW_GET_VERSION_CFM((short) 0x0009, "Acknowledge to GW_GET_VERSION_REQ command."),
        GW_GET_PROTOCOL_VERSION_REQ((short) 0x000A, "Request KLF 200 API protocol version."),
        GW_GET_PROTOCOL_VERSION_CFM((short) 0x000B, "Acknowledge to GW_GET_PROTOCOL_VERSION_REQ command."),
        GW_GET_STATE_REQ((short) 0x000C, "Request the state of the gateway"),
        GW_GET_STATE_CFM((short) 0x000D, "Acknowledge to GW_GET_STATE_REQ command."),

        GW_LEAVE_LEARN_STATE_REQ((short) 0x000E, "Request gateway to leave learn state."),
        GW_LEAVE_LEARN_STATE_CFM((short) 0x000F, "Acknowledge to GW_LEAVE_LEARN_STATE_REQ command."),
        GW_GET_NETWORK_SETUP_REQ((short) 0x00E0, "Request network parameters."),
        GW_GET_NETWORK_SETUP_CFM((short) 0x00E1, "Acknowledge to GW_GET_NETWORK_SETUP_REQ."),
        GW_SET_NETWORK_SETUP_REQ((short) 0x00E2, "Set network parameters."),
        GW_SET_NETWORK_SETUP_CFM((short) 0x00E3, "Acknowledge to GW_SET_NETWORK_SETUP_REQ."),

        GW_CS_GET_SYSTEMTABLE_DATA_REQ((short) 0x0100, "Request a list of nodes in the gateways system table."),
        GW_CS_GET_SYSTEMTABLE_DATA_CFM((short) 0x0101, "Acknowledge to GW_CS_GET_SYSTEMTABLE_DATA_REQ"),
        GW_CS_GET_SYSTEMTABLE_DATA_NTF((short) 0x0102,
                "Acknowledge to GW_CS_GET_SYSTEM_TABLE_DATA_REQList of nodes in the gateways systemtable."),
        GW_CS_DISCOVER_NODES_REQ((short) 0x0103, "Start CS  DiscoverNodes macro in KLF200."),
        GW_CS_DISCOVER_NODES_CFM((short) 0x0104, "Acknowledge to GW_CS_DISCOVER_NODES_REQ command."),
        GW_CS_DISCOVER_NODES_NTF((short) 0x0105, "Acknowledge to GW_CS_DISCOVER_NODES_REQ command."),
        GW_CS_REMOVE_NODES_REQ((short) 0x0106, "Remove one or more nodes  in the systemtable."),
        GW_CS_REMOVE_NODES_CFM((short) 0x0107, "Acknowledge to GW_CS_REMOVE_NODES_REQ."),
        GW_CS_VIRGIN_STATE_REQ((short) 0x0108, "Clear systemtable and delete system key."),
        GW_CS_VIRGIN_STATE_CFM((short) 0x0109, "Acknowledge to GW_CS_VIRGIN_STATE_REQ."),
        GW_CS_CONTROLLER_COPY_REQ((short) 0x010A,
                "Setup KLF200 to get or give a system to or from another io-homecontrol®  remote control. By a system means all nodes in the systemtable and the system key."),
        GW_CS_CONTROLLER_COPY_CFM((short) 0x010B, "Acknowledge to GW_CS_CONTROLLER_COPY_REQ."),
        GW_CS_CONTROLLER_COPY_NTF((short) 0x010C, "Acknowledge to GW_CS_CONTROLLER_COPY_REQ."),
        GW_CS_CONTROLLER_COPY_CANCEL_NTF((short) 0x010D, "Cancellation of system copy to other controllers."),
        GW_CS_RECEIVE_KEY_REQ((short) 0x010E, "Receive system key from another controller."),
        GW_CS_RECEIVE_KEY_CFM((short) 0x010F, "Acknowledge to GW_CS_RECEIVE_KEY_REQ."),
        GW_CS_RECEIVE_KEY_NTF((short) 0x0110, "Acknowledge to GW_CS_RECEIVE_KEY_REQ with status."),
        GW_CS_PGC_JOB_NTF((short) 0x0111,
                "Information on Product Generic Configuration job initiated by  press on PGC button."),
        GW_CS_SYSTEM_TABLE_UPDATE_NTF((short) 0x0112,
                "Broadcasted to all clients and gives information about added and removed actuator nodes in system table."),
        GW_CS_GENERATE_NEW_KEY_REQ((short) 0x0113, "Generate new system key and update actuators in systemtable."),
        GW_CS_GENERATE_NEW_KEY_CFM((short) 0x0114, "Acknowledge to GW_CS_GENERATE_NEW_KEY_REQ."),
        GW_CS_GENERATE_NEW_KEY_NTF((short) 0x0115, "Acknowledge to GW_CS_GENERATE_NEW_KEY_REQ with status."),
        GW_CS_REPAIR_KEY_REQ((short) 0x0116, "Update key in actuators holding an old  key."),
        GW_CS_REPAIR_KEY_CFM((short) 0x0117, "Acknowledge to GW_CS_REPAIR_KEY_REQ."),
        GW_CS_REPAIR_KEY_NTF((short) 0x0118, "Acknowledge to GW_CS_REPAIR_KEY_REQ with status."),
        GW_CS_ACTIVATE_CONFIGURATION_MODE_REQ((short) 0x0119,
                "Request one or more actuator to open for configuration."),
        GW_CS_ACTIVATE_CONFIGURATION_MODE_CFM((short) 0x011A, "Acknowledge to GW_CS_ACTIVATE_CONFIGURATION_MODE_REQ."),

        GW_GET_NODE_INFORMATION_REQ((short) 0x0200, "Request extended information of one specific actuator node."),
        GW_GET_NODE_INFORMATION_CFM((short) 0x0201, "Acknowledge to GW_GET_NODE_INFORMATION_REQ."),
        GW_GET_NODE_INFORMATION_NTF((short) 0x0210, "Acknowledge to GW_GET_NODE_INFORMATION_REQ."),
        GW_GET_ALL_NODES_INFORMATION_REQ((short) 0x0202, "Request extended information of all nodes."),
        GW_GET_ALL_NODES_INFORMATION_CFM((short) 0x0203, "Acknowledge to GW_GET_ALL_NODES_INFORMATION_REQ"),
        GW_GET_ALL_NODES_INFORMATION_NTF((short) 0x0204,
                "Acknowledge to GW_GET_ALL_NODES_INFORMATION_REQ. Holds node information"),
        GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF((short) 0x0205,
                "Acknowledge to GW_GET_ALL_NODES_INFORMATION_REQ. No more nodes."),
        GW_SET_NODE_VARIATION_REQ((short) 0x0206, "Set node variation."),
        GW_SET_NODE_VARIATION_CFM((short) 0x0207, "Acknowledge to GW_SET_NODE_VARIATION_REQ."),
        GW_SET_NODE_NAME_REQ((short) 0x0208, "Set node name."),
        GW_SET_NODE_NAME_CFM((short) 0x0209, "Acknowledge to GW_SET_NODE_NAME_REQ."),
        GW_SET_NODE_VELOCITY_REQ((short) 0x020A, "Set node velocity."),
        GW_SET_NODE_VELOCITY_CFM((short) 0x020B, "Acknowledge to GW_SET_NODE_VELOCITY_REQ."),
        GW_NODE_INFORMATION_CHANGED_NTF((short) 0x020C, "Information has been updated."),
        GW_NODE_STATE_POSITION_CHANGED_NTF((short) 0x0211, "Information has been updated."),
        GW_SET_NODE_ORDER_AND_PLACEMENT_REQ((short) 0x020D, "Set search order and room placement."),
        GW_SET_NODE_ORDER_AND_PLACEMENT_CFM((short) 0x020E, "Acknowledge to GW_SET_NODE_ORDER_AND_PLACEMENT_REQ."),

        GW_GET_GROUP_INFORMATION_REQ((short) 0x0220, "Request information about  all defined groups."),
        GW_GET_GROUP_INFORMATION_CFM((short) 0x0221, "Acknowledge to GW_GET_GROUP_INFORMATION_REQ."),
        GW_GET_GROUP_INFORMATION_NTF((short) 0x0230, "Acknowledge to GW_GET_NODE_INFORMATION_REQ."),
        GW_SET_GROUP_INFORMATION_REQ((short) 0x0222, "Change an existing group."),
        GW_SET_GROUP_INFORMATION_CFM((short) 0x0223, "Acknowledge to GW_SET_GROUP_INFORMATION_REQ."),
        GW_GROUP_INFORMATION_CHANGED_NTF((short) 0x0224,
                "Broadcast to all, about group information of a group has been changed."),
        GW_DELETE_GROUP_REQ((short) 0x0225, "Delete a group."),
        GW_DELETE_GROUP_CFM((short) 0x0226, "Acknowledge to GW_DELETE_GROUP_INFORMATION_REQ."),
        GW_NEW_GROUP_REQ((short) 0x0227, "Request new group to be created."),
        GW_NEW_GROUP_CFM((short) 0x0228, ""),
        GW_GET_ALL_GROUPS_INFORMATION_REQ((short) 0x0229, "Request information about  all defined groups."),
        GW_GET_ALL_GROUPS_INFORMATION_CFM((short) 0x022A, "Acknowledge to GW_GET_ALL_GROUPS_INFORMATION_REQ."),
        GW_GET_ALL_GROUPS_INFORMATION_NTF((short) 0x022B, "Acknowledge to GW_GET_ALL_GROUPS_INFORMATION_REQ."),
        GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF((short) 0x022C, "Acknowledge to GW_GET_ALL_GROUPS_INFORMATION_REQ."),
        GW_GROUP_DELETED_NTF((short) 0x022D,
                "GW_GROUP_DELETED_NTF is broadcasted to all, when a group has been removed."),
        GW_HOUSE_STATUS_MONITOR_ENABLE_REQ((short) 0x0240, "Enable house status monitor."),
        GW_HOUSE_STATUS_MONITOR_ENABLE_CFM((short) 0x0241, "Acknowledge to GW_HOUSE_STATUS_MONITOR_ENABLE_REQ."),
        GW_HOUSE_STATUS_MONITOR_DISABLE_REQ((short) 0x0242, "Disable house status monitor."),
        GW_HOUSE_STATUS_MONITOR_DISABLE_CFM((short) 0x0243, "Acknowledge to GW_HOUSE_STATUS_MONITOR_DISABLE_REQ."),

        GW_COMMAND_SEND_REQ((short) 0x0300, "Send activating command direct to one or more io-homecontrol®  nodes."),
        GW_COMMAND_SEND_CFM((short) 0x0301, "Acknowledge to GW_COMMAND_SEND_REQ."),
        GW_COMMAND_RUN_STATUS_NTF((short) 0x0302, "Gives run status for io-homecontrol®  node."),
        GW_COMMAND_REMAINING_TIME_NTF((short) 0x0303,
                "Gives remaining time before io-homecontrol®  node enter target position."),
        GW_SESSION_FINISHED_NTF((short) 0x0304,
                "Command send, Status request, Wink, Mode or Stop session is finished."),
        GW_STATUS_REQUEST_REQ((short) 0x0305, "Get status request from one or more io-homecontrol®  nodes."),
        GW_STATUS_REQUEST_CFM((short) 0x0306, "Acknowledge to GW_STATUS_REQUEST_REQ."),
        GW_STATUS_REQUEST_NTF((short) 0x0307,
                "Acknowledge to GW_STATUS_REQUEST_REQ. Status request from one or more io-homecontrol®  nodes."),
        GW_WINK_SEND_REQ((short) 0x0308, "Request from one or more io-homecontrol®  nodes to Wink."),
        GW_WINK_SEND_CFM((short) 0x0309, "Acknowledge to GW_WINK_SEND_REQ"),
        GW_WINK_SEND_NTF((short) 0x030A, "Status info for performed wink request."),

        GW_SET_LIMITATION_REQ((short) 0x0310, "Set a parameter limitation in an actuator."),
        GW_SET_LIMITATION_CFM((short) 0x0311, "Acknowledge to GW_SET_LIMITATION_REQ."),
        GW_GET_LIMITATION_STATUS_REQ((short) 0x0312, "Get parameter limitation in an actuator."),
        GW_GET_LIMITATION_STATUS_CFM((short) 0x0313, "Acknowledge to GW_GET_LIMITATION_STATUS_REQ."),
        GW_LIMITATION_STATUS_NTF((short) 0x0314, "Hold  information about limitation."),
        GW_MODE_SEND_REQ((short) 0x0320, "Send Activate Mode to one or more io-homecontrol®  nodes."),
        GW_MODE_SEND_CFM((short) 0x0321, "Acknowledge to GW_MODE_SEND_REQ"),
        GW_MODE_SEND_NTF((short) 0x0322, "Notify with Mode activation info."),

        GW_INITIALIZE_SCENE_REQ((short) 0x0400, "Prepare gateway to record a scene."),
        GW_INITIALIZE_SCENE_CFM((short) 0x0401, "Acknowledge to GW_INITIALIZE_SCENE_REQ."),
        GW_INITIALIZE_SCENE_NTF((short) 0x0402, "Acknowledge to GW_INITIALIZE_SCENE_REQ."),
        GW_INITIALIZE_SCENE_CANCEL_REQ((short) 0x0403, "Cancel record scene process."),
        GW_INITIALIZE_SCENE_CANCEL_CFM((short) 0x0404, "Acknowledge to GW_INITIALIZE_SCENE_CANCEL_REQ command."),
        GW_RECORD_SCENE_REQ((short) 0x0405, "Store actuator positions changes since GW_INITIALIZE_SCENE, as a scene."),
        GW_RECORD_SCENE_CFM((short) 0x0406, "Acknowledge to GW_RECORD_SCENE_REQ."),
        GW_RECORD_SCENE_NTF((short) 0x0407, "Acknowledge to GW_RECORD_SCENE_REQ."),
        GW_DELETE_SCENE_REQ((short) 0x0408, "Delete a recorded scene."),
        GW_DELETE_SCENE_CFM((short) 0x0409, "Acknowledge to GW_DELETE_SCENE_REQ."),
        GW_RENAME_SCENE_REQ((short) 0x040A, "Request a scene to be renamed."),
        GW_RENAME_SCENE_CFM((short) 0x040B, "Acknowledge to GW_RENAME_SCENE_REQ."),
        GW_GET_SCENE_LIST_REQ((short) 0x040C, "Request a list of scenes."),
        GW_GET_SCENE_LIST_CFM((short) 0x040D, "Acknowledge to GW_GET_SCENE_LIST."),
        GW_GET_SCENE_LIST_NTF((short) 0x040E, "Acknowledge to GW_GET_SCENE_LIST."),
        GW_GET_SCENE_INFOAMATION_REQ((short) 0x040F, "Request extended information for one given scene."),
        GW_GET_SCENE_INFOAMATION_CFM((short) 0x0410, "Acknowledge to GW_GET_SCENE_INFOAMATION_REQ."),
        GW_GET_SCENE_INFOAMATION_NTF((short) 0x0411, "Acknowledge to GW_GET_SCENE_INFOAMATION_REQ."),
        GW_ACTIVATE_SCENE_REQ((short) 0x0412, "Request gateway to enter a scene."),
        GW_ACTIVATE_SCENE_CFM((short) 0x0413, "Acknowledge to GW_ACTIVATE_SCENE_REQ."),
        GW_STOP_SCENE_REQ((short) 0x0415, "Request  all nodes  in a given scene to stop at their current position."),
        GW_STOP_SCENE_CFM((short) 0x0416, "Acknowledge to GW_STOP_SCENE_REQ."),
        GW_SCENE_INFORMATION_CHANGED_NTF((short) 0x0419, "A scene has either been changed or removed."),

        GW_ACTIVATE_PRODUCTGROUP_REQ((short) 0x0447, "Activate a product  group in a given direction."),
        GW_ACTIVATE_PRODUCTGROUP_CFM((short) 0x0448, "Acknowledge to GW_ACTIVATE_PRODUCTGROUP_REQ."),
        GW_ACTIVATE_PRODUCTGROUP_NTF((short) 0x0449, "Acknowledge to GW_ACTIVATE_PRODUCTGROUP_REQ."),

        GW_GET_CONTACT_INPUT_LINK_LIST_REQ((short) 0x0460,
                "Get list of assignments to all Contact Input to scene  or product  group."),
        GW_GET_CONTACT_INPUT_LINK_LIST_CFM((short) 0x0461, "Acknowledge to GW_GET_CONTACT_INPUT_LINK_LIST_REQ."),
        GW_SET_CONTACT_INPUT_LINK_REQ((short) 0x0462, "Set a link from a Contact Input to a scene  or product  group."),
        GW_SET_CONTACT_INPUT_LINK_CFM((short) 0x0463, "Acknowledge to GW_SET_CONTACT_INPUT_LINK_REQ."),
        GW_REMOVE_CONTACT_INPUT_LINK_REQ((short) 0x0464, "Remove a link from a Contact Input to a scene."),
        GW_REMOVE_CONTACT_INPUT_LINK_CFM((short) 0x0465, "Acknowledge to GW_REMOVE_CONTACT_INPUT_LINK_REQ."),

        GW_GET_ACTIVATION_LOG_HEADER_REQ((short) 0x0500, "Request header from activation log."),
        GW_GET_ACTIVATION_LOG_HEADER_CFM((short) 0x0501, "Confirm header from activation log."),
        GW_CLEAR_ACTIVATION_LOG_REQ((short) 0x0502, "Request clear all data  in activation log."),
        GW_CLEAR_ACTIVATION_LOG_CFM((short) 0x0503, "Confirm clear all data  in activation log."),
        GW_GET_ACTIVATION_LOG_LINE_REQ((short) 0x0504, "Request line from activation log."),
        GW_GET_ACTIVATION_LOG_LINE_CFM((short) 0x0505, "Confirm line from activation log."),
        GW_ACTIVATION_LOG_UPDATED_NTF((short) 0x0506, "Confirm line from activation log."),
        GW_GET_MULTIPLE_ACTIVATION_LOG_LINES_REQ((short) 0x0507, "Request lines from activation log."),
        GW_GET_MULTIPLE_ACTIVATION_LOG_LINES_NTF((short) 0x0508, "Error log  data from activation log."),
        GW_GET_MULTIPLE_ACTIVATION_LOG_LINES_CFM((short) 0x0509, "Confirm lines from activation log."),

        GW_SET_UTC_REQ((short) 0x2000, "Request to set UTC time."),
        GW_SET_UTC_CFM((short) 0x2001, "Acknowledge to GW_SET_UTC_REQ."),
        GW_RTC_SET_TIME_ZONE_REQ((short) 0x2002, "Set time zone and daylight savings rules."),
        GW_RTC_SET_TIME_ZONE_CFM((short) 0x2003, "Acknowledge to GW_RTC_SET_TIME_ZONE_REQ."),
        GW_GET_LOCAL_TIME_REQ((short) 0x2004,
                "Request  the local time based on current time zone and daylight savings rules."),
        GW_GET_LOCAL_TIME_CFM((short) 0x2005, "Acknowledge to GW_RTC_SET_TIME_ZONE_REQ."),
        GW_PASSWORD_ENTER_REQ((short) 0x3000, "Enter password to authenticate request"),
        GW_PASSWORD_ENTER_CFM((short) 0x3001, "Acknowledge to GW_PASSWORD_ENTER_REQ"),
        GW_PASSWORD_CHANGE_REQ((short) 0x3002, "Request password change."),
        GW_PASSWORD_CHANGE_CFM((short) 0x3003, "Acknowledge to GW_PASSWORD_CHANGE_REQ."),
        GW_PASSWORD_CHANGE_NTF((short) 0x3004,
                "Acknowledge to GW_PASSWORD_CHANGE_REQ. Broadcasted to all connected clients."),

        ;

        // Class internal

        private CommandNumber command;
        private String description;

        // Reverse-lookup map for getting a ProductTypeId from an TypeId
        private static final Map<Integer, Command> LOOKUPTYPEID2ENUM = new HashMap<Integer, Command>();

        static {
            for (Command typeId : Command.values()) {
                LOOKUPTYPEID2ENUM.put(new Integer(typeId.getShort()), typeId);
            }
        }

        // Constructor

        private Command(short typeId, String description) {
            this.command = new CommandNumber(typeId);
            this.description = description;
        }

        Command(String categoryString) {
            try {
                this.command = Command.valueOf(categoryString).getCommand();
            } catch (IllegalArgumentException e) {
                try {
                    this.command = Command.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase()).getCommand();
                } catch (IllegalArgumentException e2) {
                    this.command = new CommandNumber((short) -1);
                }
            }
        }

        // Class access methods

        public CommandNumber getCommand() {
            return command;
        }

        public short getShort() {
            return command.toShort();
        }

        public String getDescription() {
            return description;
        }

        public static Command get(int thisTypeId) {
            Command typeId = LOOKUPTYPEID2ENUM.get(thisTypeId);
            return (typeId == null) ? Command.UNDEFTYPE : typeId;
        }
    }

}
