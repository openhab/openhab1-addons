package org.openhab.config.misterhouse.internal.items;

import java.io.IOException;
import java.io.InputStream;

import org.openhab.config.misterhouse.internal.MhConfigActivator;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemProvider;

public class MhItemProvider implements ItemProvider {

	@Override
	public GenericItem[] getItems() {
		try {
			InputStream is = MhConfigActivator.CONFIGFILE_URL.openStream();
			return MhtFileParser.parse(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return new GenericItem[0];
	}

}
