package org.openhab.binding.mystromecopower.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.openhab.binding.mystromecopower.MyStromEcoPowerBindingProvider;
import org.openhab.binding.mystromecopower.internal.api.IMystromClient;
import org.openhab.binding.mystromecopower.internal.api.model.MystromDevice;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyStromEcoPowerBindingTest {
    private MyStromEcoPowerBinding target;
    private IMystromClient myStromClient;
    private EventPublisher eventPublisher;
    private MyStromEcoPowerBindingProvider provider;

    private void internalReceiveCommandAnd(boolean isSwitch, boolean isString, boolean isNumber, Command command,
            boolean isDeviceMaster) {
        final Dictionary<String, String> config = new Hashtable<String, String>();
        List<MystromDevice> devices = new ArrayList<MystromDevice>();

        config.put("userName", "test");
        config.put("password", "test");

        MystromDevice device = new MystromDevice();
        device.id = "1";
        device.name = "deviceFriendlyName";

        if (isDeviceMaster) {
            device.type = "mst";
        } else {
            device.type = "eth";
        }

        if (isSwitch) {
            device.state = command == OnOffType.ON ? "on" : "off";
        }

        device.power = "12";
        devices.add(device);

        MystromDevice deviceOnMyStromServer = new MystromDevice();
        deviceOnMyStromServer.id = "1";
        deviceOnMyStromServer.name = "deviceFriendlyName";

        if (isDeviceMaster) {
            deviceOnMyStromServer.type = "mst";
        } else {
            deviceOnMyStromServer.type = "eth";
        }

        if (isSwitch) {
            deviceOnMyStromServer.state = command == OnOffType.ON ? "off" : "on";
        }

        deviceOnMyStromServer.power = "12";

        String deviceOpenhabName = "deviceOpenhabName";

        this.updated(config, false, devices);

        Mockito.when(this.provider.getMystromFriendlyName(deviceOpenhabName)).thenReturn(device.name);

        if (isSwitch) {
            Mockito.when(this.provider.getIsSwitch(deviceOpenhabName)).thenReturn(true);
            Mockito.when(this.myStromClient.ChangeState(device.id, true)).thenReturn(command == OnOffType.ON);
        }

        Mockito.when(this.myStromClient.getDeviceInfo(device.id)).thenReturn(deviceOnMyStromServer);

        this.target.internalReceiveCommand(deviceOpenhabName, command);
    }

    private void updated(Dictionary<String, String> config, boolean configurationExceptionShouldOccured,
            List<MystromDevice> devices) {
        boolean configurationExceptionOccured = false;

        if (devices != null) {
            Mockito.when(this.myStromClient.login()).thenReturn(true);
            Mockito.when(this.myStromClient.getDevices()).thenReturn(devices);
        }

        try {
            this.target.updated(config);
        } catch (ConfigurationException exception) {
            configurationExceptionOccured = true;
        }

        Assert.assertEquals(configurationExceptionShouldOccured, configurationExceptionOccured);
    }

    private void execute(boolean isSwitch, boolean isString, boolean isNumber, MystromDevice myStromDevice,
            State busExpectedValue) {
        final Dictionary<String, String> config = new Hashtable<String, String>();
        final Collection<String> itemNames = new HashSet<String>();

        List<MystromDevice> devices = new ArrayList<MystromDevice>();

        config.put("userName", "test");
        config.put("password", "test");

        devices.add(myStromDevice);
        String deviceOpenhabName = "deviceOpenhabName";
        itemNames.add(deviceOpenhabName);

        Mockito.when(this.myStromClient.getDevicesState()).thenReturn(devices);
        Mockito.when(this.provider.getMystromFriendlyName(deviceOpenhabName)).thenReturn(myStromDevice.name);
        Mockito.when(this.provider.getItemNames()).thenReturn(itemNames);

        if (isSwitch) {
            Mockito.when(this.provider.getIsSwitch(deviceOpenhabName)).thenReturn(true);
        }

        if (isString) {
            Mockito.when(this.provider.getIsStringItem(deviceOpenhabName)).thenReturn(true);
        }

        if (isNumber) {
            Mockito.when(this.provider.getIsNumberItem(deviceOpenhabName)).thenReturn(true);
        }

        this.updated(config, false, devices);
        waitFor(10);

        this.target.execute();
        waitFor(10);

        Mockito.verify(this.eventPublisher, Mockito.atLeastOnce()).postUpdate(deviceOpenhabName, busExpectedValue);
    }

    @Before
    public void init() {
        this.myStromClient = Mockito.mock(IMystromClient.class);
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.provider = Mockito.mock(MyStromEcoPowerBindingProvider.class);

        this.target = new MyStromEcoPowerBinding(this.myStromClient);
        this.target.setEventPublisher(this.eventPublisher);
        this.target.addBindingProvider(this.provider);
    }

    @Test
    public void whenUserNameInConfigIsBlank_ItShouldThrowConfigurationExceptionTest() {
        final Dictionary<String, String> config = new Hashtable<String, String>();

        config.put("userName", "");
        this.updated(config, true, null);
        waitFor(10);
    }

    @Test
    public void whenPasswordInConfigIsBlank_ItShouldThrowConfigurationExceptionTest() {
        final Dictionary<String, String> config = new Hashtable<String, String>();

        config.put("userName", "test");
        config.put("password", "");
        this.updated(config, true, null);
        waitFor(10);
    }

    @Test
    public void whenDoDiscoveryAndInvalidLogin_ItShouldReturn() {
        final Dictionary<String, String> config = new Hashtable<String, String>();

        config.put("userName", "test");
        config.put("password", "test");

        Mockito.when(this.myStromClient.login()).thenReturn(false);

        this.updated(config, false, null);
        waitFor(10);

        Mockito.verify(this.myStromClient).login();
        Mockito.verify(this.myStromClient, Mockito.times(0)).getDevices();
    }

    @Test
    public void whenDoDiscoveryAndValidLogin_ItShouldGetDevices() {
        final Dictionary<String, String> config = new Hashtable<String, String>();
        List<MystromDevice> devices = new ArrayList<MystromDevice>();

        config.put("userName", "test");
        config.put("password", "test");

        this.updated(config, false, devices);
        waitFor(10);

        Mockito.verify(this.myStromClient).login();
        Mockito.verify(this.myStromClient).getDevices();
    }

    @Test
    public void whenExecuteAndNoDevices_ItShouldDoNothing() {
        final Dictionary<String, String> config = new Hashtable<String, String>();

        List<MystromDevice> devices = new ArrayList<MystromDevice>();

        config.put("userName", "test");
        config.put("password", "test");

        this.updated(config, false, devices);
        waitFor(10);

        this.target.execute();
        waitFor(10);

        Mockito.verify(this.myStromClient, Mockito.never()).getDevicesState();
    }

    @Test
    public void whenExecuteAndSwitchDeviceOn_ItShouldPublishOn() {
        MystromDevice device = new MystromDevice();
        device.id = "1";
        device.name = "deviceFriendlyName";
        device.type = "eth";
        device.state = "on";

        this.execute(true, false, false, device, OnOffType.ON);
    }

    @Test
    public void whenExecuteAndSwitchDeviceOff_ItShouldPublishOff() {
        MystromDevice device = new MystromDevice();
        device.id = "1";
        device.name = "deviceFriendlyName";
        device.type = "eth";
        device.state = "off";

        this.execute(true, false, false, device, OnOffType.OFF);
    }

    @Test
    public void whenExecuteAndStringDeviceOn_ItShouldPublishDeviceState() {
        MystromDevice device = new MystromDevice();
        device.id = "1";
        device.name = "deviceFriendlyName";
        device.type = "eth";
        device.state = "on";

        this.execute(false, true, false, device, new StringType(device.state));
    }

    @Test
    public void whenExecuteAndNumber_ItShouldPublishTheDevicePower() {
        MystromDevice device = new MystromDevice();
        device.id = "1";
        device.name = "deviceFriendlyName";
        device.type = "eth";
        device.state = "on";
        device.power = "12";

        this.execute(false, false, true, device, new DecimalType(device.power));
    }

    @Test
    public void whenInternalReceiveCommandAndSwitchDeviceOn_ItShouldSendOnToMyStromServerTest() {
        this.internalReceiveCommandAnd(true, false, false, OnOffType.ON, false);

        Mockito.verify(this.myStromClient).ChangeState("1", true);
    }

    @Test
    public void whenInternalReceiveCommandAndSwitchDeviceOff_ItShouldSendOffToMyStromServerTest() {
        this.internalReceiveCommandAnd(true, false, false, OnOffType.OFF, false);

        Mockito.verify(this.myStromClient).ChangeState("1", false);
    }

    @Test
    public void whenInternalReceiveCommandAndNumberDevice_ItShouldDoNothingTest() {
        this.internalReceiveCommandAnd(false, false, true, new DecimalType(2), false);

        Mockito.verify(this.myStromClient, Mockito.never()).ChangeState(Matchers.any(), Matchers.anyBoolean());
    }

    @Test
    public void whenInternalReceiveCommandAndMasterDeviceOff_ItShouldSendRestartMasterToMyStromServerTest() {
        this.internalReceiveCommandAnd(true, false, false, OnOffType.OFF, true);

        Mockito.verify(this.myStromClient, Mockito.never()).ChangeState(Matchers.any(), Matchers.anyBoolean());
        Mockito.verify(this.myStromClient).RestartMaster("1");
    }

    @Test
    public void whenInternalReceiveCommandAndMasterDeviceOn_ItShouldDoNothingTest() {
        this.internalReceiveCommandAnd(true, false, false, OnOffType.ON, true);

        Mockito.verify(this.myStromClient, Mockito.never()).ChangeState(Matchers.any(), Matchers.anyBoolean());
        Mockito.verify(this.myStromClient, Mockito.never()).RestartMaster(Matchers.anyString());
    }

    private void waitFor(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
