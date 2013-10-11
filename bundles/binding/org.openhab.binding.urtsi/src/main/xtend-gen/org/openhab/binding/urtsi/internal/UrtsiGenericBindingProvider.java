package org.openhab.binding.urtsi.internal;

import com.google.common.base.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.openhab.binding.urtsi.UrtsiBindingProvider;
import org.openhab.binding.urtsi.internal.UrtsiItemConfiguration;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The binding provider is responsible for defining some metadata of the binding and for providing a parser for the binding configuration.
 * @author Oliver Libutzki
 * @since 1.3.0
 */
@SuppressWarnings("all")
public class UrtsiGenericBindingProvider extends AbstractGenericBindingProvider implements UrtsiBindingProvider {
  private final static Logger logger = new Function0<Logger>() {
    public Logger apply() {
      Logger _logger = LoggerFactory.getLogger(UrtsiGenericBindingProvider.class);
      return _logger;
    }
  }.apply();
  
  private final static Pattern CONFIG_BINDING_PATTERN = new Function0<Pattern>() {
    public Pattern apply() {
      Pattern _compile = Pattern.compile("(.*?):([0-9]*)");
      return _compile;
    }
  }.apply();
  
  public String getBindingType() {
    return "urtsi";
  }
  
  /**
   * The methods checks if the item which uses the urtsi binding is a rollershutter item.
   */
  public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
    boolean _matched = false;
    if (!_matched) {
      if (item instanceof RollershutterItem) {
        final RollershutterItem _rollershutterItem = (RollershutterItem)item;
        _matched=true;
      }
    }
    if (!_matched) {
      String _name = item.getName();
      String _plus = ("item \'" + _name);
      String _plus_1 = (_plus + "\' is of type \'");
      Class<? extends Object> _class = item.getClass();
      String _simpleName = _class.getSimpleName();
      String _plus_2 = (_plus_1 + _simpleName);
      String _plus_3 = (_plus_2 + "\', only RollershutterItems are allowed - please check your *.items configuration");
      BindingConfigParseException _bindingConfigParseException = new BindingConfigParseException(_plus_3);
      throw _bindingConfigParseException;
    }
  }
  
  /**
   * The Urtsi binding doesn't use auto-Update as we first check if the command is executed successfully.
   * @see UrtsiBinding#internalReceiveCommand(String, org.openhab.core.types.Command)
   */
  public Boolean autoUpdate(final String itemName) {
    Boolean _xifexpression = null;
    boolean _providesBindingFor = this.providesBindingFor(itemName);
    if (_providesBindingFor) {
      _xifexpression = false;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  /**
   * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
   */
  public void processBindingConfiguration(final String context, final Item item, final String bindingConfig) throws BindingConfigParseException {
    super.processBindingConfiguration(context, item, bindingConfig);
    boolean _notEquals = (!Objects.equal(bindingConfig, null));
    if (_notEquals) {
      this.parseAndAddBindingConfig(item, bindingConfig);
    } else {
      String _bindingType = this.getBindingType();
      String _plus = (_bindingType + " bindingConfig is NULL (item=");
      String _plus_1 = (_plus + item);
      String _plus_2 = (_plus_1 + ") -> processing bindingConfig aborted!");
      UrtsiGenericBindingProvider.logger.warn(_plus_2);
    }
  }
  
  /**
   * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
   */
  protected void parseAndAddBindingConfig(final Item item, final String bindingConfig) throws BindingConfigParseException {
    final Matcher matcher = UrtsiGenericBindingProvider.CONFIG_BINDING_PATTERN.matcher(bindingConfig);
    boolean _matches = matcher.matches();
    boolean _not = (!_matches);
    if (_not) {
      String _plus = ("bindingConfig \'" + bindingConfig);
      String _plus_1 = (_plus + "\' doesn\'t contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp \'");
      String _pattern = UrtsiGenericBindingProvider.CONFIG_BINDING_PATTERN.pattern();
      String _plus_2 = (_plus_1 + _pattern);
      String _plus_3 = (_plus_2 + "\'");
      BindingConfigParseException _bindingConfigParseException = new BindingConfigParseException(_plus_3);
      throw _bindingConfigParseException;
    }
    matcher.reset();
    boolean _find = matcher.find();
    if (_find) {
      String _group = matcher.group(1);
      String _group_1 = matcher.group(2);
      Integer _valueOf = Integer.valueOf(_group_1);
      UrtsiItemConfiguration _urtsiItemConfiguration = new UrtsiItemConfiguration(_group, (_valueOf).intValue());
      final UrtsiItemConfiguration urtsiConfig = _urtsiItemConfiguration;
      this.addBindingConfig(item, urtsiConfig);
    } else {
      String _plus_4 = ("bindingConfig \'" + bindingConfig);
      String _plus_5 = (_plus_4 + "\' doesn\'t contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp \'");
      String _pattern_1 = UrtsiGenericBindingProvider.CONFIG_BINDING_PATTERN.pattern();
      String _plus_6 = (_plus_5 + _pattern_1);
      String _plus_7 = (_plus_6 + "\'");
      BindingConfigParseException _bindingConfigParseException_1 = new BindingConfigParseException(_plus_7);
      throw _bindingConfigParseException_1;
    }
  }
  
  /**
   * Returns the device id which is associated to the given item.
   */
  public String getDeviceId(final String itemName) {
    UrtsiItemConfiguration _itemConfiguration = this.getItemConfiguration(itemName);
    String _deviceId = _itemConfiguration==null?(String)null:_itemConfiguration.getDeviceId();
    return _deviceId;
  }
  
  /**
   * Returns the channel which is associated to the given item.
   */
  public int getChannel(final String itemName) {
    UrtsiItemConfiguration _itemConfiguration = this.getItemConfiguration(itemName);
    int _channel = _itemConfiguration==null?0:_itemConfiguration.getChannel();
    return _channel;
  }
  
  /**
   * Returns the item configuration for the given item.
   */
  private UrtsiItemConfiguration getItemConfiguration(final String itemName) {
    BindingConfig _get = this.bindingConfigs.get(itemName);
    return ((UrtsiItemConfiguration) _get);
  }
}
