
/*
 https://github.com/savelee/senchatouch-chrome43
 */
Ext.define('MyApp.util.SizeMonitor', {
	override: 'Ext.util.SizeMonitor',

	uses: [
		'Ext.env.Browser',
		'Ext.util.sizemonitor.Default',
		'Ext.util.sizemonitor.Scroll',
		'Ext.util.sizemonitor.OverflowChange'
	],

	//Thanks! http://trevorbrindle.com/chrome-43-broke-sencha/

	constructor: function (config) {
		var namespace = Ext.util.sizemonitor;

		if (Ext.browser.is.Firefox) {
			return new namespace.OverflowChange(config);
		} else if (Ext.browser.is.WebKit) {
			if (!Ext.browser.is.Silk && Ext.browser.engineVersion.gtEq('535') && !Ext.browser.engineVersion.ltEq('537.36')) {
				return new namespace.OverflowChange(config);
			} else {
				return new namespace.Scroll(config);
			}
		} else if (Ext.browser.is.IE11) {
			return new namespace.Scroll(config);
		} else {
			return new namespace.Scroll(config);
		}
	}
}, function () {
	// <debug>
	console.info("Ext.util.SizeMonitor temp. fix is active");
	// </debug>
});

Ext.define('MyApp.util.PaintMonitor', {
	override: 'Ext.util.PaintMonitor',

	uses: [
		'Ext.env.Browser',
		'Ext.env.OS',
		'Ext.util.paintmonitor.CssAnimation',
		'Ext.util.paintmonitor.OverflowChange'
	],

	constructor: function(config) {
		return new Ext.util.paintmonitor.CssAnimation(config);
	}

}, function () {
	// <debug>
	console.info("Ext.util.PaintMonitor temp. fix is active");
	// </debug>
});