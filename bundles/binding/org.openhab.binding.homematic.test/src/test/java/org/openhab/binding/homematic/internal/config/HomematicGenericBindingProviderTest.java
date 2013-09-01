package org.openhab.binding.homematic.internal.config;

import org.junit.Test;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.BindingConfigParseException;

public class HomematicGenericBindingProviderTest {

    HomematicGenericBindingProvider provider = new HomematicGenericBindingProvider();

    @Test
    public void parseAdminItem() throws BindingConfigParseException {
        provider.processBindingConfiguration("homematic", new StringItem("Test"), "ADMIN:DUMP_UNCONFIGURED_DEVICES");
    }

}
