package org.openhab.binding.connectsdk.internal.bridges;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.ToastControl;

public class ToastControlToast extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	private static final Logger logger = LoggerFactory.getLogger(ToastControlToast.class);

	@Override
	protected String getItemClass() {
		return "ToastControl";
	}

	@Override
	protected String getItemProperty() {
		return "toast";
	}

	private ToastControl getControl(final ConnectableDevice device) {
		return device.getCapability(ToastControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property)
				&& d.hasCapabilities(ToastControl.Show_Toast)) {
			final String value = command.toString();
			final ToastControl control = getControl(d);
			try {
				BufferedImage bi = ImageIO.read(getClass().getResource("/openhab-logo-square.png"));
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				OutputStream b64 = Base64.getEncoder().wrap(os);
				ImageIO.write(bi, "png", b64);
				control.showToast(value, os.toString("UTF-8"), "png", createDefaultResponseListener() );
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	@Override
	public void onDeviceReady(ConnectableDevice device, Collection<ConnectSDKBindingProvider> providers,
			EventPublisher eventPublisher) {
		super.onDeviceReady(device, providers, eventPublisher);
		if(!findMatchingItemNames(device, providers).isEmpty()) { // only send hello world, if at least one toast Control item is configured
			onReceiveCommand(device, getItemClass(), getItemProperty(), new StringType("Welcome to Openhab!"));
		}
	}
		
}