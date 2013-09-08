package org.openhab.binding.enocean.internal.bus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.values.ButtonState;
import org.enocean.java.eep.RockerSwitch;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.IncreaseDecreaseType;

@Ignore
public class RockerSwitchInDimmerProfileTest extends BasicBindingTest {

    @Before
    public void setUpDefaultDevice() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), null);
        provider.setParameterAddress(parameterAddress);
        provider.setItem(new DimmerItem("dummie"));
        provider.setEep(RockerSwitch.EEP_ID_1);
        binding.addBindingProvider(provider);
    }

    @Test
    public void increaseLightOnShortButtonPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        waitFor(10);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        waitFor(10);
        assertNull("Update State", publisher.popLastCommand());
    }

    @Test
    public void decreaseLightOnShortButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        waitFor(10);
        assertEquals("Update State", IncreaseDecreaseType.DECREASE, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        waitFor(10);
        assertNull("Update State", publisher.popLastCommand());
    }

    @Test
    public void lightenUpDuringLongButtonPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        waitFor(10);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        waitFor(300);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        waitFor(10);
        assertNull("Update State", publisher.popLastCommand());
    }

    @Test
    public void lightenUpDuringVeryLongButtonPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        waitFor(10);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        waitFor(300);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        waitFor(300);
        assertEquals("Update State", IncreaseDecreaseType.INCREASE, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        waitFor(10);
        assertNull("Update State", publisher.popLastCommand());
    }

    @Test
    public void dimmLightDuringLongButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        waitFor(10);
        assertEquals("Update State", IncreaseDecreaseType.DECREASE, publisher.popLastCommand());
        waitFor(300);
        assertEquals("Update State", IncreaseDecreaseType.DECREASE, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        waitFor(10);
        assertNull("Update State", publisher.popLastCommand());
    }

    private void waitFor(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
