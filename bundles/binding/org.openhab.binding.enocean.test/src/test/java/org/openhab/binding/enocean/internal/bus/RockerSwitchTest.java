package org.openhab.binding.enocean.internal.bus;

import static org.junit.Assert.assertEquals;

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.values.ButtonState;
import org.enocean.java.eep.RockerSwitch;
import org.junit.Test;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class RockerSwitchTest extends BasicBindingTest {

    @Test
    public void testReceiveButtonPress() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), RockerSwitch.BUTTON_I);
        provider.setParameterAddress(parameterAddress);
        binding.addBindingProvider(provider);

        provider.setItem(new SwitchItem("dummie"));
        binding.valueChanged(parameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", OnOffType.ON, publisher.getUpdateState());
    }
}
