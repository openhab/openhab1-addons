package org.openhab.binding.mystromecopower.internal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.BindingConfigParseException;

public class MyStromEcoPowerGenericBindingProviderTest {
    private MyStromEcoPowerGenericBindingProvider target;

    @Before
    public void init() {
        this.target = new MyStromEcoPowerGenericBindingProvider();
    }

    @Test
    public void whenValidateItemTypeIsSwitchItem_ItShouldValidateItTest() throws BindingConfigParseException {
        this.target.validateItemType(new SwitchItem("test"), null);
    }

    @Test
    public void whenValidateItemTypeIsNumberItem_ItShouldValidateItTest() throws BindingConfigParseException {
        this.target.validateItemType(new NumberItem("test"), null);
    }

    @Test
    public void whenValidateItemTypeIsStringItem_ItShouldValidateItTest() throws BindingConfigParseException {
        this.target.validateItemType(new StringItem("test"), null);
    }

    @Test
    public void whenValidateItemTypeIsLocationItem_ItShouldThrowABindingConfigParseExceptionTest() {
        boolean exceptionOccured = false;

        try {
            this.target.validateItemType(new LocationItem("test"), null);
        } catch (BindingConfigParseException e) {
            exceptionOccured = true;
        }

        assertTrue(exceptionOccured);
    }

    @Test
    public void whenProcessBindingConfigurationAndItemIsSwitch_ConfigurationIsSwitchShouldBeTrueTest()
            throws BindingConfigParseException {
        SwitchItem item = new SwitchItem("test");

        this.target.processBindingConfiguration("test", item, "");

        assertTrue(this.target.getIsSwitch(item.getName()));
        assertFalse(this.target.getIsNumberItem(item.getName()));
        assertFalse(this.target.getIsStringItem(item.getName()));
    }

    @Test
    public void whenProcessBindingConfigurationAndItemIsNumber_ConfigurationIsNumberShouldBeTrueTest()
            throws BindingConfigParseException {
        NumberItem item = new NumberItem("test");

        this.target.processBindingConfiguration("test", item, "");

        assertTrue(this.target.getIsNumberItem(item.getName()));
        assertFalse(this.target.getIsSwitch(item.getName()));
        assertFalse(this.target.getIsStringItem(item.getName()));
    }

    @Test
    public void whenProcessBindingConfigurationAndItemIsString_ConfigurationIsStringShouldBeTrueTest()
            throws BindingConfigParseException {
        StringItem item = new StringItem("test");

        this.target.processBindingConfiguration("test", item, "");

        assertTrue(this.target.getIsStringItem(item.getName()));
        assertFalse(this.target.getIsSwitch(item.getName()));
        assertFalse(this.target.getIsNumberItem(item.getName()));
    }

    @Test
    public void whenGetMystromFriendlyName_ItShouldReturnTheFriendlyNameOfTheItem() throws BindingConfigParseException {
        String itemName = "friendlyName";
        StringItem item = new StringItem("test");

        this.target.processBindingConfiguration("test", item, itemName);
        String result = this.target.getMystromFriendlyName(item.getName());

        assertEquals(itemName, result);
    }
}
