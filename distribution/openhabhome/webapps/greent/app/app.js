/*
 * ----------------- OpenHAB GreenT UI -------------------
 *
 *
 *     Version: 1.1.0
 *     Developed by: Mihail Panayotov
 *     E-mail: mihail@m-design.bg
 *
 *
 * -----------------------------------------------------------
 */





// ------------------------------------
// Security is not fully implemented yet

/*var https_security = getLocalStoreItem('openHAB_security', "OFF");
console.log(https_security);
if (https_security == "ON" && window.location.href.substr(0, 7) == "http://") {
    url = window.location.href.replace("http://", "https://");
    url = url.replace("8080", "8443");
    window.location = url;
} else if(https_security == "OFF" && window.location.href.substr(0, 8) == "https://"){
	url = window.location.href.replace("https://", "http://");
    url = url.replace("8443", "8080");
    window.location = url;
}*/
// ------------------------------------

var oph_greenT_version = '1.1.0';
var oph_greenT_build = '1003';
var oph_openHAB_version = 'not known';
var oph_update_available = false;
var oph_update_version;
var oph_update_build;
var oph_update_info;



var firstload=true;

var detectedTransport = null;
var socket = $.atmosphere;
var fallbackProtocol = null;

var current_widgets;

function subscribe(location) {
	 
                var request = { url : location,
				    maxRequest : 256,
					timeout: 59000,
					attachHeadersAsQueryString : true,
					executeCallbackBeforeReconnect : false,
					//transport: 'long-polling',
					transport: force_transport,
					fallbackTransport: fallbackProtocol,
                    headers:{'Accept': 'application/json'}};

                request.onError = function (response) {
					//console.log('------ ERROR -------');
					//console.log(response);
				}
				request.onOpen = function(response) {
					//console.log('-------- OPEN --------');
					//console.log(response);
					detectedTransport = response.transport;
				}           
                 
                request.onMessage = function (response) {

                    
                    if (response.status == 200) {
                        var data = response.responseBody;
                        if (data.length > 0) {
							try{
							updateWidgets(Ext.JSON.decode(data));
							} catch(e) {
							}
                        }
                    }
                };

               socket.subscribe(request);
            }

function unsubscribe(){
      socket.unsubscribe();
}

function getOpenhabVersion(){
	Ext.Ajax.request({
    url: '/static/version',
    success: function(response){
        oph_openHAB_version = response.responseText;
		getUpdateServer();
    }
});
}


function getUpdateServer(){
	Ext.Ajax.request({
    url: '/greent/app/updates_server.cfg',
    success: function(response){
		checkForUpdates(response.responseText);
    },
	failure: function(response){
		alert('Error connecting update server!');
	}
});
}




function checkForUpdates(updates_srver){
	Ext.data.JsonP.request({
            url: updates_srver+'/check.php?version='+oph_greenT_build,
            callbackKey: 'callback',
            success: function(result, request) {
               
        if(result.status == true){
			oph_update_available = result.needsUpdate;
            oph_update_version = result.lastBuildName;
            oph_update_build = result.lastBuildNumber;
			oph_update_info = result.info;
			if(OpenHAB.enableUpdates == 'true'){
			if(result.needsUpdate == true){
				var update_answer = confirm('GreenT UI version '+result.lastBuildName+' is available. Do you want to update now?')
	            if (update_answer){
					if (!settingsPanel) {
						showSettingsWindow();
						settingsPanel.setActiveItem(2);
					} else {
						settingsPanel.setActiveItem(2);
					}
	            }
			} 
			}
		} else {
			alert('Error reading update server!');
		}
                
            }
        });
}



function updateWidgets(result){
	//console.log(result);
	if (Ext.isArray(result.widget)) {
                        for (var i = 0 in result.widget) {
                            if (result.widget[i].type == "Frame") {
                                if (Ext.isArray(result.widget[i].widget)) {
                                    for (var k = 0 in result.widget[i].widget) {
										setWidgetData(result.widget[i].widget[k]);
                                    }
                                } else {
									setWidgetData(result.widget[i].widget);
                                }
                            } else {
								setWidgetData(result.widget[i]);
                            }
                        }
                    } else {
						setWidgetData(result.widget);
                    }
                    if (deviceType != 'Phone') {
                        if (result.parent && result.parent.id == currentLeftNavPage) {
                            setTitle(result.title);
                            updateLeftNavMenuItem(result.id, result.title, result.icon);
                        } else if (result.id == currentLeftNavPage) {
                            refreshTitle();
                        } else {
                            refreshTitle();
                        }

                    } else {
                        setTitle(result.title);
                    }
}





var UIobjects = {}; // Object containing the UI description
var UInavPanel = {
	expanded: true,
    text: 'Left nav',
    children: []
}; // Object containing the navigation tree

var ajax_requests = new Array(); //holds pending ajax requests
var broadCrumb = new Array(); // Broadcrumb array
var broadCrumbText = '';
var newCard;

var settingsPanel = null;
var updateModel = {};

var deviceTypeCls = '';
var deviceType;

var clickOnLeaf = false;

var firstTime = true;

var sitemap;
var currentLeftNavPage;
var leftNavPanel;
var sitemapUIcontainer;
var transition = {type: 'slide',direction: 'left'};
var sitemapsPanel;
var icon_class = "oph_icon";
var icon_style = "background-image:url";
var oph_usepicker;

var dirtySettings = false;

function getLocalStoreItem(item_name, default_value){
	var value = localStorage.getItem(item_name);
	if(!value || value == "null"){
		return default_value;
	} else {
		return value;
	}
}

function setTheme(){
var current_theme = getLocalStoreItem('openHAB_theme', 'GreenT');
if(OpenHAB.themes.indexOf(current_theme) == -1){
	current_theme = 'GreenT';
}

theme_css_filename = './themes/' + current_theme + '/css/style.css';
//theme_css_filename = './themes/' + 'simple' + '/css/style.css'

var theme_css = document.createElement("link");
theme_css.setAttribute("rel", "stylesheet");
theme_css.setAttribute("type", "text/css");
theme_css.setAttribute("href", theme_css_filename);
document.getElementsByTagName("head")[0].appendChild(theme_css);
}



var ui_language = getLocalStoreItem('openHAB_language', 'en');
var transitions = getLocalStoreItem('openHAB_transitions', '1');
var force_transport = getLocalStoreItem('openHAB_transport', 'auto');
var chart_servlet = getLocalStoreItem('openHAB_chart_servlet', 'chart');


var sitemapStoreLoadTries = 3;
var sitemapsStoreSelection = new Ext.data.Store({
    fields: ['name', 'value'],
	autoLoad: true
});
var sitemapsStore = new Ext.data.Store({
    fields: ['name', 'value'],
    autoLoad: true,
    listeners: {
        exception: function (proxy, response, op, opt) {
            if (sitemapStoreLoadTries > 0) {
                sitemapsStore.load();
                sitemapStoreLoadTries--;
            } else {
                sitemapStoreLoadTries = 3;
                alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
            }
        }, load: function (store, records, success) {
            if (success) {
				
				for(record in records) {
                    records[record].data.value = records[record].data.name;
                };
                sitemapStoreLoadTries = 3;
                sitemapsStoreSelection.insert(0, [{
                    name: '- ' + OpenHAB.i18n_strings[ui_language].choose_on_startup,
					value: 'choose'
                }])
                sitemapsStore.each(function (record) {
                    sitemapsStoreSelection.add(record.copy());
                });
            } else {
                if (sitemapStoreLoadTries > 0) {
                    sitemapsStore.load();
                    sitemapStoreLoadTries--;
                } else {
                    sitemapStoreLoadTries = 3;
                    alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
                }


            }
        }
    }, proxy: {
        type: 'ajax',
        url: '/rest/sitemaps',
        headers: {
            'Accept': 'application/json',
        }, noCache: true,
        limitParam: undefined,
        startParam: undefined,
        pageParam: undefined,



        reader: {
            type: 'json',
            rootProperty: 'sitemap'
        }
    }

});







var sitemapsWindow = {

    id: 'sitemapsWindow',
    floating: true,
    centered: true,
    //width:300,
	//height:300,
    layout: 'fit',
    scrollable: false,
    items: [{
        title: OpenHAB.i18n_strings[ui_language].interfaces,
        xtype: 'toolbar',
        ui: 'light',
        docked: 'top'
    }, {
        xtype: 'list',
        store: sitemapsStore,
        singleSelect: true,
        itemTpl: '{name}',
		scrollable: false,
		//width:300,
		//height:300,
        listeners: {
            itemtap: function (view, index, target, record) {
                sitemap = record.data.name;
                loadUIData(sitemap);
                sitemapsPanel.destroy();
            }
        }
    }]
};

function getCurrentPageId() {
    return broadCrumb[broadCrumb.length - 1][0];
}


var settingsWindow = {

    id: 'settingsWindow',
    floating: true,
    centered: true,
    //width:300,
	height:500,
    layout: 'card',
	items: [{
	scrollable: 'vertical',
    items: [{
        xtype: 'toolbar',
        ui: 'light',
        docked: 'top',
		items: [{
                    xtype: 'spacer'
                }, {
                    xtype: 'label',
                    cls: 'x-title',
                    centered: true,
					zIndex:0,
					html: OpenHAB.i18n_strings[ui_language].settings
                }, {
                    xtype: 'spacer'
                },{
                    iconCls: 'delete',
					iconMask: true,
					ui: 'normal',
                    handler: function(){
						if(dirtySettings){
							localStorage.setItem('openHAB_sitemap', settingsPanel.getItems().items[0].getItems().items[1].getValue());
            
            				localStorage.setItem('openHAB_device_type', settingsPanel.getItems().items[0].getItems().items[2].getValue());
            				localStorage.setItem('openHAB_language', settingsPanel.getItems().items[0].getItems().items[3].getValue());
            				localStorage.setItem('openHAB_theme', settingsPanel.getItems().items[0].getItems().items[4].getValue());
							localStorage.setItem('openHAB_transport', settingsPanel.getItems().items[0].getItems().items[5].getValue());
							localStorage.setItem('openHAB_transitions', settingsPanel.getItems().items[0].getItems().items[6].getValue());
							localStorage.setItem('openHAB_chart_servlet', settingsPanel.getItems().items[0].getItems().items[7].getValue());

            				alert(OpenHAB.i18n_strings[ui_language].need_to_restart_for_changes_to_take_effect);
            				window.location.reload();
						}
						settingsPanel.destroy();
                        settingsPanel = null;},

                }

                ]
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].default_sitemap,
        labelWidth: '40%',
        store: sitemapsStoreSelection,
        displayField: 'name',
        valueField: 'value',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].this_device_is,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    },
        options: [{
            text: OpenHAB.i18n_strings[ui_language].auto_detect,
            value: 'Auto'
        }, {
            text: OpenHAB.i18n_strings[ui_language].phone,
            value: 'Phone'
        }, {
            text: OpenHAB.i18n_strings[ui_language].tablet,
            value: 'Tablet'
        }, {
            text: OpenHAB.i18n_strings[ui_language].pc,
            value: 'Desktop'
        }],
        //value: localStorage.getItem('openHAB_device_type'),
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].interface_language,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].theme,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    }, 
	{
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].transport_protocol,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    },
	{
        xtype: 'togglefield',
        label: OpenHAB.i18n_strings[ui_language].transitions,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    },
	{
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].chart_servlet,
        labelWidth: '40%',
		style: 'border-bottom: 1px solid E6E6E6;',
		listeners:{
                        change: function(){dirtySettings = true}
                    }
    },
	{
    xtype: 'button',
        text: '<img style="height:100%;" src="./app/logo_tr.png" border="0" />',
        style: 'clear:both;margin:0.7em 5%;height:3.6em;',
        scope: this,
		cls: 'oph_about_btn',
        handler: function () {
			settingsPanel.setActiveItem(1);
		}
    },
	
	 
	
	]
	},{
		cls: 'oph_about_page',
		scrollable: 'vertical',

    items: [{
        xtype: 'toolbar',
        ui: 'light',
        docked: 'top',
		items: [{
                    ui: 'back',
                    text: OpenHAB.i18n_strings[ui_language].back,
                    handler: function(){settingsPanel.setActiveItem(0);},

                }, {
                    xtype: 'spacer'
                }, {
                    xtype: 'label',
                    cls: 'x-title',
                    centered: true,
					zIndex:0,
					html: 'GreenT UI'
                }, {
                    xtype: 'spacer'
                },{
                    iconCls: 'delete',
					iconMask: true,
					ui: 'normal',
                    handler: function(){
						settingsPanel.destroy();
                        settingsPanel = null;},

                }

                ]
    },{
		xtype: 'container',
		cls: 'oph_about_page_content',
		
		}]
		
		
		
		
	},{
		cls: 'oph_update_page',
		scrollable: 'vertical',
    items: [{
        xtype: 'toolbar',
        ui: 'light',
        docked: 'top',
		items: [{
                    ui: 'back',
                    text: OpenHAB.i18n_strings[ui_language].back,
                    handler: function(){settingsPanel.setActiveItem(1);},

                }, {
                    xtype: 'spacer'
                }, {
                    xtype: 'label',
                    cls: 'x-title',
                    centered: true,
					zIndex:0,
					html: 'GreenT UI Update'
                }, {
                    xtype: 'spacer'
                },{
                    iconCls: 'delete',
					iconMask: true,
					ui: 'normal',
                    handler: function(){
						settingsPanel.destroy();
                        settingsPanel = null;},

                }

                ]
    },{
		xtype: 'container',
		cls: 'oph_about_page_content',
		
		},
		{
    xtype: 'button',
        text: 'Update GreenT UI',
        scope: this,
		style: 'margin:0.8em 5%;height:3em;',
		ui: 'confirm',
        handler: function (btn) {
			settingsPanel.getItems().items[2].getItems().items[2].destroy();
			sendCommand('update_greent', 'ON');
			settingsPanel.getItems().items[2].getItems().items[1].setHtml('');
			Ext.defer(readUpdateLog, 3000, this);
		}
    }]
		
		
		
		
	}
	
	]
};


function readUpdateLog(){
	Ext.Ajax.request({
    url: '/greent/update.tmp',
    success: function(response){
		settingsPanel.getItems().items[2].getItems().items[1].setHtml(response.responseText);
		Ext.defer(readUpdateLog, 3000, this);
    },
	failure: function(response){
		Ext.defer(readUpdateLog, 3000, this);
	}
});
	
		
}
















function loadUIData(sitemap_name) {
    Ext.getCmp('content').setMasked({
		xtype: 'loadmask',
        message: OpenHAB.i18n_strings[ui_language].loading
    });
	
    Ext.Ajax.request({
        url: '/rest/sitemaps/' + sitemap_name,
        headers: {
            'Accept': 'application/json',
        }, success: function (result_obj) {
            try {
                result = Ext.JSON.decode(result_obj.responseText);
			} catch (error) {
				loadUIData(sitemap_name);
				return;
			}
            //try {
                buildUIArray(result.homepage, UInavPanel);
                clearEmptyFrames();

                Ext.getCmp('content').unmask();
                broadCrumb[0] = new Array(result.homepage.id, result.homepage.title);
                Ext.getCmp('title').setHtml(result.homepage.title);
                //console.log(UInavPanel);

                goToPage(result.homepage.id);



                if (Ext.getCmp('leftPanel')) {
                    leftPanelstore.setRoot(UInavPanel);
                    setCurrentLeftNavPage(result.homepage.id);
                }


            //} catch (error) {
                Ext.getCmp('content').unmask();
               // alert(OpenHAB.i18n_strings[ui_language].error_build_interface + "\r\n(" + error + ")");
            //}




        }, failure: function () {
            alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
        }
    });
};





Ext.define('LeftPanelListItem', {
    extend: 'Ext.data.Model',
	config: {
    fields: [{
        name: 'icon',
        type: 'string'
    }, {
        name: 'text',
        type: 'string'
    }, {
        name: 'page_id',
        type: 'string'
    }, {
        name: 'page_label',
        type: 'string'
    }, {
        name: 'name',
        type: 'string'
    },
    //{ name : 'leaf', type : 'boolean' },
    ]
	}
});


var leftPanelstore = new Ext.data.TreeStore({
    model: 'LeftPanelListItem',
    autoLoad: false,
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'children'
        }
    }
});




function pushWidget(widget, container) {
    if (widget) {
        container.push(widget);
    }
}

function buildUIArray(json, nav_parent) {
    try{
    if (json) {

        var container;


        UIobjects[json.id] = new Array();
        container = UIobjects[json.id];


        var page_data = json.widget;
        //console.log(nav_parent);
        if (Ext.isArray(page_data)) {




            var frame = false;

            for (var i in page_data) {


                if (page_data[i].type != "Frame") {

                    if (!frame) {


                        container.push({
                            xtype: 'fieldset',
                            title: '',
                            cls: ['oph_fieldset', deviceTypeCls + '_fieldset', 'oph_clearfix']
                        });
                        container = container[container.length - 1]['items'] = new Array();

                        frame = true;
                    }
                    pushWidget(addsWidget(page_data[i].widgetId, page_data[i], container, nav_parent), container);
                } else if (page_data[i].type == "Frame") {
                    frame = false;
                    container.push({
                        xtype: 'fieldset',
                        title: page_data[i].label,
                        cls: ['oph_fieldset', deviceTypeCls + '_fieldset', 'oph_clearfix']
                    });
                    container[container.length - 1]['items'] = new Array();
                    if (Ext.isArray(page_data[i].widget)) {
                        for (var k in page_data[i].widget) {
                            pushWidget(addsWidget(page_data[i].widget[k].widgetId, page_data[i].widget[k], container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                        }
                    } else {
                        pushWidget(addsWidget(page_data[i].widget.widgetId, page_data[i].widget, container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                        
                    }
                }


            }

        } else {

            if (page_data.type != "Frame") {


                container.push({
                    xtype: 'fieldset',
                    title: '',
                    cls: ['oph_fieldset', deviceTypeCls + '_fieldset', 'oph_clearfix']
                });
                container = container[container.length - 1]['items'] = new Array();
                pushWidget(addsWidget(page_data.widgetId, page_data, container, nav_parent), container);

            } else if (page_data.type == "Frame") {
                container.push({
                    xtype: 'fieldset',
                    title: page_data.label,
                    cls: ['oph_fieldset', deviceTypeCls + '_fieldset', 'oph_clearfix']
                });
                container[container.length - 1]['items'] = new Array();
                if (Ext.isArray(page_data.widget)) {
                    for (var k in page_data.widget) {
                        pushWidget(addsWidget(page_data.widget[k].widgetId, page_data.widget[k], container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                    }
                } else {
                    pushWidget(addsWidget(page_data.widget.widgetId, page_data.widget, container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                }
            }



        }

    } 
			
    } catch(e){
		alert(OpenHAB.i18n_strings[ui_language].error_build_interface);
	}
}


function clearEmptyFrames() {

    for (var i in UIobjects) {
        var newArray = [];
        for (var k in UIobjects[i]) {
            if (UIobjects[i][k].items.length > 0) {
                newArray.push(UIobjects[i][k]);
            }
        }
        UIobjects[i] = newArray;
    }
}



function addsWidget(id, data, container, nav_parent) {
    var widget;
    var navigation_parent;
    if (data.type == "Switch") {
        if (data.item && data.item.type == "RollershutterItem") {
            widget = createRollershutterWidget(data.item ? data.item.name : '', data.label, data.icon)
        } else if (data.item && data.item.type == "NumberItem") {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else if (data.item && data.item.type == "SwitchItem" && data.mapping) {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else if (data.item && data.item.type == "GroupItem" && data.mapping) {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else {
            widget = createToggleWidget(data.item ? data.item.name : '', data.label, data.icon);
        }

    } else if (data.type == "Slider") {
        widget = createSliderWidget(data.item ? data.item.name : '', data.label, data.icon);
    } else if (data.type == "Setpoint") {
		widget = createSetpointWidget(data.item ? data.item.name : '', data.minValue, data.maxValue, data.step, data.label, data.icon);
    } else if (data.type == "Video") {
		widget = createVideoWidget(data.item ? data.item.name : '', data.url, data.label, data.icon);
	} else if (data.type == "Webview") {
		widget = createWebviewWidget(data.item ? data.item.name : '', data.url, data.height, data.label, data.icon);
	} else if (data.type == "Chart") {
		widget = createChartWidget(data.item ? data.item.name : '', id, data.w, data.h, data.period, data.refresh, data.item ? data.item.type : '', data.label, data.icon);	
	} else if (data.type == "Text") {
        if (data.linkedPage) {
            if (deviceType == "Phone") {
                widget = createLinkWidget(data);

            } else {
                navigation_parent = addButtonToLeftNav(id, data, nav_parent);
            }
            buildUIArray(data.linkedPage, navigation_parent);
        } else {
            widget = createTextWidget(data.label, data.icon);
        }
    } else if (data.type == "Group") {
        if (deviceType == "Phone") {
            widget = createLinkWidget(data);

        } else {
            navigation_parent = addButtonToLeftNav(id, data, nav_parent);
        }
        
        buildUIArray(data.linkedPage, navigation_parent);

    } else if (data.type == "Selection") {
        widget = createSelectionWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
    } else if (data.type == "Image") {
        if (data.linkedPage) {
            if (deviceType == "Phone") {
                widget = createImageLinkWidget(data.url, data.linkedPage.id, data.linkedPage.title);
            } else {
                navigation_parent = addButtonToLeftNav(id, data, nav_parent);
                widget = createImageLinkWidget(data.url, data.linkedPage.id, data.linkedPage.title);
                //widget = createImageWidget(data.url);
            }
            buildUIArray(data.linkedPage, navigation_parent);
        } else {
            widget = createImageWidget(data.url);
        }

    } else if (data.type == "Frame") {

    } else {
        widget = createUnsupportedWidget();
    }


    if (widget) {
        widget.name = id;
    }
    return widget;

}


function addButtonToLeftNav(id, data, nav_parent) {
	if(data.linkedPage){
		page_id = data.linkedPage.id;
	} else {
		page_id = "none";
	}
	
    pushWidget({
        icon: data.icon,
        text: data.label.replace(/[\[\]']+/g, ''),
        page_id: page_id,
        page_label: data.label,
        leaf: true,
        name: id,
		id: id
    }, nav_parent.children);
    nav_parent.leaf = false;
    nav_parent.children[nav_parent.children.length - 1].children = new Array();
    return nav_parent.children[nav_parent.children.length - 1];
	
}


function showSettingsWindow() {
    if (settingsPanel) {
        settingsPanel.destroy();
        settingsPanel = null;
    } else {
        settingsPanel = new Ext.Panel(settingsWindow);
        settingsPanel.setCls(deviceTypeCls + '_modal');


        settingsPanel.getItems().items[0].getItems().items[1].setValue(getLocalStoreItem('openHAB_sitemap', 'choose'));
        settingsPanel.getItems().items[0].getItems().items[2].setValue(getLocalStoreItem('openHAB_device_type', 'auto'));


        var options_array = new Array();
        for (var i in OpenHAB.i18n_strings) {
            options_array.push({
                text: OpenHAB.i18n_strings[i].language_name,
                value: i
            })
        }
        settingsPanel.getItems().items[0].getItems().items[3].setOptions(options_array);
        settingsPanel.getItems().items[0].getItems().items[3].setValue(getLocalStoreItem('openHAB_language', 'en'));


        options_array = [];
        for (var i in OpenHAB.themes) {
            options_array.push({
                text: OpenHAB.themes[i],
                value: OpenHAB.themes[i]
            })
        }
        settingsPanel.getItems().items[0].getItems().items[4].setOptions(options_array);
        settingsPanel.getItems().items[0].getItems().items[4].setValue(getLocalStoreItem('openHAB_theme', 'GreenT'));	
		
		settingsPanel.getItems().items[0].getItems().items[5].setOptions([
                        {text: OpenHAB.i18n_strings[ui_language].auto_detect,  value: 'auto'},
                        {text: 'Websockets', value: 'websocket'},
                        {text: 'HTTP Streaming',  value: 'streaming'},
						{text: 'HTTP Long-polling',  value: 'long-polling'}
                    ]);
        settingsPanel.getItems().items[0].getItems().items[5].setValue(getLocalStoreItem('openHAB_transport', 'auto'));
		settingsPanel.getItems().items[0].getItems().items[6].setValue(transitions);

		settingsPanel.getItems().items[0].getItems().items[7].setOptions([
						{text: 'Chart engine', value: 'chart'},
                        {text: 'RRD chart engine',  value: 'rrdchart.png'}
                    ]);

        settingsPanel.getItems().items[0].getItems().items[7].setValue(getLocalStoreItem('openHAB_chart_servlet', 'chart'));
		
		logo_width = 50;
		if(deviceType == 'Phone'){
			logo_width = 85;
		}
		update_link = '';
		if(OpenHAB.enableUpdates == 'true' || OpenHAB.enableUpdates == 'password'){
		if(oph_update_available){
			update_link = ' <i>(<a href="#" style="color:red" id="update_button">UPDATE</a>)</i>';
		} else {
			update_link = ' <i>(up to date)</i>';
		}
		}
		settingsPanel.getItems().items[1].getItems().items[1].setHtml('<div style="text-align:center;font-size:1em;"><img style="width:'+logo_width+'%;margin:1em;" src="./app/logo.png" border="0"/><br><b>GreenT UI installed version:</b> '+oph_greenT_version+'<br><b>GreenT UI available version:</b> '+oph_update_version+update_link+'<br><b>openHAB version:</b> '+oph_openHAB_version+'<br><br><b>Transport Protocol:</b> '+detectedTransport+'<br><a style="color:#87a600;" target="_blank" href="http://m-design.bg/greent/">GreenT UI Website</a></div>');
		settingsPanel.getItems().items[2].getItems().items[1].setHtml('<div style="padding-top:0.5em;text-align:center;font-size:1em;"><b>Installed version:</b> '+oph_greenT_version+' <i>(build '+oph_greenT_build+')</i><br><b>Version to update: </b>'+oph_update_version+' <i>(build '+oph_update_build+')</i><br><br><b>Update info:</b><br>'+oph_update_info+'</div><br><br>');
		
		
				
        Ext.Viewport.add(settingsPanel);
		try{
		Ext.get("update_button").clearListeners();
		Ext.get("update_button").on("tap", function(){
			if(OpenHAB.enableUpdates == 'password'){
				var password = prompt("Enter a password:","");
				if (password==OpenHAB.updatesPassword){
					settingsPanel.setActiveItem(2);					
				} else {
					alert('Wrong password!');
				}
			} else if(OpenHAB.enableUpdates == 'true'){
				settingsPanel.setActiveItem(2);
			}
			});
		} catch(e){
		}
			dirtySettings = false;
    }
}






function NavBarItemTap(list, item, index, e, data) {
	if(data.raw.page_id == 'none'){
		return;
	}
	
	
    leftNavPanel.getBackButton().show();
    //console.log(list.getStore().getAt(index).data.page_id);

    if (data.raw.leaf) { //if it is a leaf

        if (clickOnLeaf) {
            broadCrumb.pop();
        }
        clickOnLeaf = true;
    } else { //if it is not a leaf
        if (clickOnLeaf) {
            broadCrumb.pop();
        }
        clickOnLeaf = false;
        setCurrentLeftNavPage(data.raw.page_id);
    }
    broadCrumb.push([data.raw.page_id, data.raw.page_label]);
    if (UIobjects[data.raw.page_id].length > 0) {

        goToPage(data.raw.page_id);

    }

}





function tapHandler(btn, evt) {
	if(btn.config.page_id == 'none'){
		return;
	}
	
	btn.addCls('selected');
	
    if (deviceType == 'Phone') {
        broadCrumb.push([btn.config.page_id, btn.config.page_label]);
		transition = {
            type: 'slide',
            direction: 'left'
        };
        
        goToPage(btn.config.page_id);
    } else {
        leftNav = Ext.getCmp('leftPanel').getActiveItem();
        index = leftNav.getStore().getAt(leftNav.getStore().find('page_id', btn.config.page_id)).data.index;
        leftNav.select(index, false, false);
        NavBarItemTap(leftNav, '', index, '', leftNav.getStore().getAt(leftNav.getStore().find('page_id', btn.config.page_id)));
    }
}


function backPage() {
	
    if (broadCrumb.length > 1) {
        broadCrumb.pop();
		transition = {
            type: 'slide',
            direction: 'right'
        };
        goToPage(broadCrumb[broadCrumb.length - 1][0]);
    }
}





function goToPage(page) {
    
    newCard = new Oph.form.Panel({
        scrollable: 'vertical',
        autoDestroy: true,
        cls: ['oph_widgets_container', deviceTypeCls + '_widgets_container']
    });
    newCard.add(UIobjects[page]);




    if(transitions == '0'){
         Ext.getCmp("content").setActiveItem(newCard);
	} else if(transitions == '1'){
	     Ext.getCmp("content").animateActiveItem(newCard,transition);
	}

}


function updateWidgetsAjax(page, update_type) {
    if (page == getCurrentPageId()) {

        var update_type_header = '';
        var update_type_timeout = 5000; 
        if (!update_type || update_type == 'normal') {
            update_type_header = {
                'Accept': 'application/json'
            };
        } else if (update_type == 'long-poll') {
			unsubscribe();
            subscribe('/rest/sitemaps/' + sitemap + '/' + page);
			return;
        }


		for(var i=ajax_requests.length-1; i>=0; i--) {
			 if (ajax_requests.hasOwnProperty(i)) {
		        Ext.Ajax.abort(ajax_requests[i]);
		        ajax_requests.pop();
			 }
        }
		
		 req = Ext.Ajax.request({

            url: '/rest/sitemaps/' + sitemap + '/' + page,
            headers: update_type_header,
            disableCaching: true,
			//autoAbort: true,
            timeout: update_type_timeout,
            failure: function (result) {
				//console.log('--- fail ----');
				//console.log(result);
                if (update_type == "long-poll") {
                    updateWidgetsAjax(page, 'long-poll');
                } else {
                    updateWidgetsAjax(page, 'normal');
                }
            }, success: function (result_obj) {
                if (result_obj.statusText == "OK" && result_obj.responseText != "") {
					
					try {
                        var result = Ext.JSON.decode(result_obj.responseText);
					} catch(exception){
						updateWidgetsAjax(page, 'normal');
						return;
					}
                    updateWidgets(result);
                    
					updateWidgetsAjax(page, 'long-poll');
                } else {
					//updateWidgetsAjax(page, 'normal');
                    
					
					//alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
                }
                
            }
        });
        ajax_requests.push(req);
    }
}

function setWidgetData(widget_data){
	if(current_widgets[widget_data.widgetId]){
		current_widgets[widget_data.widgetId].setValueData(widget_data);
	} else {
		if (deviceType != "Phone") {
		updateLeftNavMenu(widget_data);
		}
	}
}


function updateLeftNavMenuItem(page_id, title, icon) {
    nav_store = Ext.getCmp('leftPanel').getActiveItem().getStore();
    index = nav_store.find('page_id', page_id);
	if(index != -1){
    nav_store.getAt(index).set("text", title.replace(/[\[\]']+/g, ''));
	nav_store.getAt(index).set("icon", icon);
	}
}

function updateLeftNavMenu(updatedata) {
    nav_store = Ext.getCmp('leftPanel').getActiveItem().getStore();
    index = nav_store.find('name', updatedata.widgetId);
    if(index != -1){
        nav_store.getAt(index).set("text", updatedata.label.replace(/[\[\]']+/g, ''));
		nav_store.getAt(index).set("icon", updatedata.icon);
	}


}




// -------- here we set some variables depending on device type and orientation ---------- 

function setProfile() {
	if(force_transport == 'auto'){
	force_transport = 'websocket';
	fallbackProtocol = 'streaming';
	if (Ext.os.is.Android2){
		fallbackProtocol = 'long-polling';
	}
	} else {
		fallbackProtocol = force_transport;
	}
	oph_usepicker = 'auto';
    deviceType = localStorage.getItem('openHAB_device_type');
    if (deviceType && deviceType != 'Auto') {} else {
        deviceType = Ext.os.deviceType;
    }

    if (deviceType == "Phone") {
        deviceTypeCls = 'oph_phone';
		oph_usepicker = true;
    } else if (deviceType == "Tablet") {
        deviceTypeCls = 'oph_tablet';
		oph_usepicker = false;
    } else {
        deviceTypeCls = 'oph_desktop';
		oph_usepicker = false;
    }

}



Ext.define('Oph.field.ButtonsSelect', {
    extend: 'Ext.field.Field',
    xtype: 'oph_buttons_selectfield',



    config: {
        cls: 'x-buttons-field',
        styleHtmlContent: true,
        styleHtmlCls: 'oph_none',
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    applyComponent: function (config) {
        component = this;
        container = Ext.factory({
            styleHtmlContent: true,
            styleHtmlCls: 'oph_right'
        }, Ext.Container);
        if (Ext.isArray(this.config.options)) {
            for (var i in this.config.options) {
                item = container.add(Ext.create('Ext.Button', {
                    cls: "x-button-normal oph_selection_btn",
                    item: component.config.oph_item,
                    command: component.config.options[i].command,
                    text: this.config.options[i].label
                }));
                item.on({
                    tap: function () {
                        sendCommand(this.config.item, this.config.command)
                    }
                });
            }
        } else {
            item = container.add(Ext.create('Ext.Button', {
                cls: "x-button-normal oph_selection_btn",
                item: component.config.oph_item,
                command: component.config.options.command,
                text: this.config.options.label
            }));
            item.on({
                tap: function () {
                    sendCommand(this.config.item, this.config.command)
                }
            });
        }

        return container;
    },

    initialize: function () {
        this.callParent();
    },


    setValueData: function (newValue) {
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        if (Ext.isArray(newValue.mapping)) {
            for (var i in newValue.mapping) {
                this._component._items.items[i].removeCls('x-button-action');
                if (newValue.item.state == newValue.mapping[i].command) {
                    this._component._items.items[i].addCls('x-button-action');
                }
            }
        } else {
            this._component._items.items[0].addCls('x-button-action');
        }
    }
});




Ext.define('Oph.field.Slider', {
    extend: 'Ext.field.Slider',
    xtype: 'oph_sliderfield',

    config: {
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
        this.on({

            painted: function (component) {
                component._component.element.on({
                    tap: function () {
                        sendCommand(component.config.oph_item, component.getValue())
                    }
                });
                component._component.getThumb(0).element.on({
                    dragend: function () {
                        sendCommand(component.config.oph_item, component.getValue())
                    }
                });
            }
        });

    },

    setValueData: function (newValue) {
        if (newValue.item) {
            this.setValue(newValue.item.state);
        }
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        return this;
    }
});


Ext.define('Oph.field.Setpoint', {
    extend: 'Ext.field.Spinner',
    xtype: 'oph_setpointfield',

    config: {
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
        this.on({
            spindown: function(component, value){
				sendCommand(component.config.oph_item, value);
			},
            spinup: function(component, value){
				sendCommand(component.config.oph_item, value);
			},
        });

    },

    setValueData: function (newValue) {
        if (newValue.item) {
			if(newValue.item.state == 'Uninitialized'){
				this.setValue(Number(this.config.minValue));
			} else {
				this.setValue(Number(newValue.item.state));
			}
            
        }
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        return this;
    }
});


Ext.define('Oph.field.Select', {
    extend: 'Ext.field.Select',
    xtype: 'oph_selectfield',


    config: {
        displayField: 'label',
        valueField: 'command',
        labelWidth: '50%'
    },


    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
        this.on({
            painted: function (component) {
                component.on({
                    selected: function () {
                        sendCommand(component.config.oph_item, component.getValue())
                    }
                });
            }
        });
    }, setValueData: function (newValue) {
        if (newValue.item) {
            this.setValue(newValue.item.state);
        }
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        return this;
    }, onListSelect: function (item, record) {
        var me = this;
        if (record) {
            me.setValue(record);
            this.fireEvent('selected', this);
        }
    }, onPickerChange: function (picker, value) {
        var me = this,
            currentValue = me.getValue(),
            newValue = value[me.getName()],
            store = me.getStore(),
            index = store.find(me.getValueField(), newValue);
        record = store.getAt(index);

        me.setValue(record);
        this.fireEvent('selected', this);
    },
});



Ext.define('Oph.field.Text', {
    extend: 'Ext.field.Field',
    xtype: 'oph_textfield',


    config: {
        cls: 'x-textfield',
        styleHtmlContent: true,
        styleHtmlCls: 'oph_none',
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
    }, setValueData: function (newValue) {
		//this.setHtml(newValue.item.state);
        this.setHtml(newValue.label.match(/\[(.*?)\]/) ? newValue.label.match(/\[(.*?)\]/)[1] : newValue.label);
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/\[(.*?)\]/, '')+'</div>');
        return this;
    }
});


Ext.define('Oph.field.Toggle', {
    extend: 'Ext.field.Toggle',
    xtype: 'oph_togglefield',

    config: {
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
        this.on({
            painted: function (component) {
                component._component.element.on({
                    singletap: function () {
                        sendCommand(component.config.oph_item, component.formatOutputValue(component.getValue()));
                    }
                })
                component._component.getThumb(0).element.on({
                    dragend: function () {
                        sendCommand(component.config.oph_item, component.formatOutputValue(component.getValue()))
                    }
                });
            }
        });
    }, setValueData: function (newValue) {
        if (newValue.item) {
            this.setValue(this.formatInputValue(newValue.item.state));
        }
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        return this;
    }, formatInputValue: function (value) {
        if (value == "ON") {
            return 1;
        } else {
            return 0;
        }
    }, formatOutputValue: function (value) {
        if (value == 1) {
            return "ON";
        } else if (value == 0) {
            return "OFF";
        }
    }
});

function sendCommand(oph_item, value) {
    if (oph_item) {
        Ext.Ajax.request({
            comm_method: 'ajax',
            url: '/rest/items/' + oph_item + '/',
            method: 'POST',
            params: value,
            headers: {
                'Content-Type': 'text/plain'
            }, failure: function () {
                //alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
            }
        });
    }


}


Ext.define('Oph.field.Rollershutter', {
    extend: 'Ext.field.Field',
    xtype: 'oph_rollershutterfield',


    config: {
        cls: 'x-rollershutter-field',
        styleHtmlContent: true,
        styleHtmlCls: 'oph_none',
        labelWidth: '50%'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);

    },

    applyComponent: function (config) {

        component = this;
        container = Ext.factory({
            styleHtmlContent: true,
            styleHtmlCls: 'oph_right'
        }, Ext.Container);
        but1 = container.add(Ext.create('Ext.Button', {
            cls: "x-button-normal oph_rollershutter_btn",
            longPress: false,
            iconCls: 'arrow_up',
            iconMask: true
        }));
        but1.element.item = component.config.oph_item;
        but1.element.on({
            touchstart: function () {
                sendCommand(this.item, "UP")
            }, longpress: function () {
                but1.longPress = true
            }, tap: function () {
                if (but1.longPress) {
                    but1.longPress = false;
                    sendCommand(this.item, "STOP")
                }
            }
        });
        but2 = container.add(Ext.create('Ext.Button', {
            cls: "x-button-normal oph_rollershutter_btn",
            iconCls: 'delete',
            iconMask: true
        }));
        but2.element.item = component.config.oph_item;
		but2.element.on({
            tap: function () {
                sendCommand(this.item, "STOP")
            }
        });
        but3 = container.add(Ext.create('Ext.Button', {
            cls: "x-button-normal oph_rollershutter_btn",
            longPress: false,
            iconCls: 'arrow_down',
            iconMask: true
        }));
        but3.element.item = component.config.oph_item;
        but3.element.on({
            touchstart: function () {
                sendCommand(this.item, "DOWN")
            }, longpress: function () {
                but3.longPress = true
            }, tap: function () {
                if (but3.longPress) {
                    but3.longPress = false;
                    sendCommand(this.item, "STOP")
                }
            }
        });

        return container;
    },

    initialize: function () {
        this.callParent();
    },

    initialize: function () {
        this.callParent();
        
    }, setValueData: function (newValue) {
        this.setLabel('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');

        return this;
    }
});





Ext.define('Oph.field.Button', {
    extend: 'Ext.Button',
    xtype: 'oph_buttonfield',

    isField: true,
    config: {
        style: 'display:block',
        baseCls: 'x-field oph_group_btn oph_button',
        labelCls: '',
        isField: true,
        cls: 'oph_link'
    },

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
    }, setValueData: function (newValue) {
        this.setHtml('<div class="'+icon_class+'" style="'+icon_style+'(/images/' + newValue.icon + '.png);"></div><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div><span class="arrow"></span>');
        return this;
    }, getName: function () {
        return this.config.name;
    }
});





Ext.define('Oph.field.Webview', {
    extend: 'Ext.Container',
    xtype: 'oph_webviewfield',


    constructor: function (config) {
        config = config || {};

        this.callParent([config]);		
		var w_height = 58*Number(config.oph_height);
		if(deviceType == 'Phone'){
			w_height = 52*Number(config.oph_height);
		}
		this.setHeight(w_height+18);
		this.setStyle('position:relative;padding:0.4em;');
		//alert(Ext.getDom(this).clientWidth);
		this.setHtml('<div class="oph_webview_mask" style="z-index:999;position:absolute;top:0;left:0;width:100%;height:'+w_height+'px;"></div>		<div style="position:absolute;top:0;left:0;overflow:hidden;height:'+Number(w_height+20)+'px;width:100%;"><iframe scrolling="no" src="' + config.url + '" width="100%" height='+w_height+' style="border:1px solid gray;"></iframe></div>');
    },

    initialize: function () {
        this.callParent();
    }, setValueData: function (newValue) {}
});



Ext.define('Oph.field.Video', {
    extend: 'Ext.Video',
    xtype: 'oph_videofield',


    constructor: function (config) {
        config = config || {};

        this.callParent([config]);
        if (config.url.substr(0, 4) != "http" && config.url.substr(0, 1) != "/") {
            config.url = "/" + config.url;
        }
		
		//this.setHtml('<div style="width:100%;padding:0.4em;"><video style="width:100%;" controls><source src="' + config.url + '"></video></div>');
    },

    initialize: function () {
        this.callParent();
        this.ghost.setHtml('<div style="text-align:center;padding-top:1em;">'+this.config.oph_label+'</div>');
    }, setValueData: function (newValue) {}
});





Ext.define('Oph.field.Chart', {
    extend: 'Ext.Container',
    xtype: 'oph_chartfield',
    refreshInterval: null,

    constructor: function (config) {
        config = config || {};
		var chartType = 'items';
        if(config.oph_type == 'GroupItem'){
			chartType = 'groups';
		}
		//config.img_src = '/'+chart_servlet+'?'+chartType+'='+config.oph_item+'&period='+config.oph_period+'&w='+config.oph_w+'&h='+config.oph_h;
		
		config.img_src = '/'+chart_servlet+'?'+chartType+'='+config.oph_item+'&period='+config.oph_period;
		this.callParent([config]);
        this.setHtml('<div style="width:100%;padding:0.4em;"><img id="img'+config.oph_id+'" src="'+config.img_src+'&random=' + new Date().getTime() + '" style="width:100%;"></div>');
		
		
    },

    initialize: function () {
        this.callParent();
		
		
		this.on({
			painted: function (component) {
				if(component.config.oph_refresh > 0){
				      component.config.refreshInterval = window.setInterval(function(){component.updateImage(component);}, component.config.oph_refresh);
				}
			},
            erased: function (component) {
				if(component.config.oph_refresh > 0){
				      window.clearInterval(component.config.refreshInterval);
				}
			}
		});
		

    }, setValueData: function (newValue) {},
	updateImage: function (container)
       {
            Ext.fly('img'+container.config.oph_id).dom.src = container.config.img_src+'&random=' + new Date().getTime();
       }
	
	
}

	   
	   
	   );


Ext.define('Oph.field.Image', {
    extend: 'Ext.Container',
    xtype: 'oph_imagefield',


    constructor: function (config) {
        config = config || {};

        this.callParent([config]);
        if (config.url.substr(0, 4) != "http" && config.url.substr(0, 1) != "/") {
            config.url = "/" + config.url;
        }
        this.setHtml('<div style="width:100%;padding:0.4em;"><img src="' + config.url + '" style="width:100%;"></div>');
    },

    initialize: function () {
        this.callParent();

    }, setValueData: function (newValue) {}
});


Ext.define('Oph.field.ImageLink', {
    extend: 'Ext.Button',
    xtype: 'oph_imagelink',

    config: {
        style: 'display:block',
        baseCls: 'none',
        labelCls: '',
        cls: 'oph_link'
    },

    constructor: function (config) {
        config = config || {};

        this.callParent([config]);
        if (config.url.substr(0, 4) != "http" && config.url.substr(0, 1) != "/") {
            config.url = "/" + config.url;
        }
        this.setHtml('<div style="width:100%;padding:0.4em;"><img src="' + config.url + '" style="width:100%;"></div>');
    },

    initialize: function () {
        this.callParent();

    }, setValueData: function (newValue) {}
});




Ext.define('Oph.form.Panel', {
    extend: 'Ext.Panel',
    xtype: 'oph_formpanel',

    constructor: function (config) {
        config = config || {};
        this.callParent([config]);
    },

    initialize: function () {
        this.callParent();
    }, setValuesData: function (values) {
        var fields = this.getFields(),
            name, field, value;

        values = values || {};
        for (name in values) {
            if (values.hasOwnProperty(name)) {
                field = fields[name];
                value = values[name];
                if (field) {
                    field.setValueData(value);
                }
            }
        }

        return this;
    }, getFields: function (byName) {
        var fields = {},
            itemName;

        var getFieldsFrom = function (item) {
            if (item.isField) {
                itemName = item.getName();

                if ((byName && itemName == byName) || typeof byName == 'undefined') {
                    if (fields.hasOwnProperty(itemName)) {
                        if (!Ext.isArray(fields[itemName])) {
                            fields[itemName] = [fields[itemName]];
                        }

                        fields[itemName].push(item);
                    } else {
                        fields[itemName] = item;
                    }
                }

            }

            if (item.isContainer) {
                item.items.each(getFieldsFrom);
            }
        };

        this.items.each(getFieldsFrom);

        return (byName) ? (fields[byName] || []) : fields;
    },
});







function createSliderWidget(oph_item, oph_label, oph_icon) {
    return {
        xtype: 'oph_sliderfield',
        minValue: 0,
        maxValue: 100,
        oph_item: oph_item,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
    };
}

function createTextWidget(oph_label, oph_icon) {

    return {
        xtype: 'oph_textfield',
		html: oph_label.match(/\[(.*?)\]/) ? oph_label.match(/\[(.*?)\]/)[1] : oph_label,
        label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/\[(.*?)\]/, '')+'</div>'
    };
}

function createToggleWidget(oph_item, oph_label, oph_icon) {
    return {
        xtype: 'oph_togglefield',
        oph_item: oph_item,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
    };

}

function createRollershutterWidget(oph_item, oph_label, oph_icon) {
    return {
        xtype: 'oph_rollershutterfield',
        oph_item: oph_item,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
    };

}



function createLinkWidget(data) {
	page_label = data.label;
	if(data.linkedPage){
		page_id = data.linkedPage.id;
	} else {
		page_id = "none";
	}
    return {
        xtype: 'oph_buttonfield',
        page_id: page_id,
        page_label: page_label
    }

}


function createImageLinkWidget(url, page_id, page_label) {
    return {
        xtype: 'oph_imagelink',
        url: url,
        page_id: page_id,
        page_label: page_label,

    }

}


function createSelectionWidget(oph_item, options, oph_label, oph_icon) {
    return {
        xtype: 'oph_selectfield',
        options: options,
        oph_item: oph_item,
		usePicker: oph_usepicker,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
		
    }
};


function createSetpointWidget(oph_item, oph_minvalue, oph_maxvalue, oph_step, oph_label, oph_icon) {
    return {
        xtype: 'oph_setpointfield',
        oph_item: oph_item,
		minValue: Number(oph_minvalue),
        maxValue: Number(oph_maxvalue),
        increment: Number(oph_step),
		defaultValue: oph_minvalue,
        cycle: false,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
		
    }
};


function createVideoWidget(oph_item, url, oph_label, oph_icon) {
    return {
        xtype: 'oph_videofield',
        oph_item: oph_item,
		url:url,
		width:'100%',
		height:'16em',
		style: 'background-color:white;',
		posterUrl: './app/video.png',
		oph_label: oph_label
		//label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
		
    }
};


function createWebviewWidget(oph_item, url, oph_height, oph_label, oph_icon) {
    return {
        xtype: 'oph_webviewfield',
        oph_item: oph_item,
		url:url,
		width:'100%',
		height:'16em',
		oph_label: oph_label,
		oph_height: oph_height,
		//label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
		
    }
};




function createButtonsWidget(oph_item, options, oph_label, oph_icon) {
    return {
        xtype: 'oph_buttons_selectfield',
        options: options,
        oph_item: oph_item,
		label: '<div class="'+icon_class+'" style="'+icon_style+'(/images/' + oph_icon + '.png);"></div><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
    }
};

function createUnsupportedWidget() {
    return {
        xtype: 'field',
        label: '[UNSUPPORTED WIDGET]',
    };

}


function createImageWidget(url) {
    return {
        xtype: 'oph_imagefield',
        url: url,
        cls: 'x-image-field',
    };

}


function createChartWidget(oph_item, oph_id, oph_w, oph_h, oph_period, oph_refresh, oph_type, oph_label, oph_icon) {
    return {
        xtype: 'oph_chartfield',
        oph_item: oph_item,
		oph_w: oph_w,
		oph_h: oph_h,
		oph_period: oph_period,
		oph_refresh: oph_refresh,
		oph_id: oph_id,
		oph_type: oph_type,
		img_src: ''
    };

}




var oph_app = Ext.application({
    name: 'OpenHAB',
	icon: '/images/icon.png',
    tabletStartupScreen: '/images/splash-ipad-v.png',
    phoneStartupScreen: '/images/splash-iphone.png',
    glossOnIcon: false,



    launch: function() {	
	    setTheme();		
		getOpenhabVersion();
        setProfile();
		if(OpenHAB.usePictogramIcons == true){
			icon_class += " oph_pictogram";
			icon_style = "-webkit-mask-image:url";
		}
        //sitemapUIcontainer = Ext.create('Ext.Container', {fullscreen: true});
        var sitemapUIpanel = Ext.create('Ext.Panel', {
            layout: {
                type: 'card',
                /*animation: {
                    type: 'slide',
                    direction: 'left'
                } */
            }, cls: 'oph_background',
            fullscreen: true,
            id: 'content',
            autoDestroy: true,


            items: []
        });


        sitemapUIpanel.onAfter('activeitemchange', function (container, newcard, oldcard, opts) {
			if (oldcard) {
			        //oldcard.clearListeners();
                    oldcard.removeAll(true);
                    oldcard.destroy();
			}
			newcard.on({
                tap: tapHandler,
                delegate: 'button'


            });
		}, sitemapUIpanel);
        

        sitemapUIpanel.onBefore('activeitemchange', function (container, newcard, oldcard, opts) {
			current_widgets = newcard.getFields();
			
			newcard.on({show:function(){

            // ---------- Do the two-column layout ----------
            var left_column_height = 0;
            var right_column_height = 0;
            var items = newcard.getItems();
			if(items.length == 1){
			    newcard.addCls('oph_widgets_container_one');
			} else {
				newcard.addCls('oph_widgets_container_many');
			}
            for (var i = 0; i < items.length; i++) {
				
                if (left_column_height > right_column_height) {
                    right_column_height += items.items[i].element.dom.clientHeight;
                    items.items[i].addCls(deviceTypeCls + '_fieldset_right');
                } else {
                    left_column_height += items.items[i].element.dom.clientHeight;
                    items.items[i].addCls(deviceTypeCls + '_fieldset_left');
                }
            }
            //------------------------	
				
				}});		

            updateWidgetsAjax(broadCrumb[broadCrumb.length - 1][0], 'normal');
        }, sitemapUIpanel);
		
		
		
        








        if (deviceType != "Phone") {
			transition = {
                type: 'fade',
                //out:true,
                duration: 500
            };
           


            leftNavPanel = sitemapUIpanel.add(new Ext.NestedList({
                docked: 'left',
                width: '14em',
                //allowDeselect: false,
                scrollable: 'auto',
                //deselectOnContainerClick: false,
                id: 'leftPanel',
				styleHtmlContent: true,
				styleHtmlCls: 'leftPanelContent',
                //displayField: 'text',
                backText: OpenHAB.i18n_strings[ui_language].back,
                updateTitleText: false,
                store: leftPanelstore,
                listeners: {
                    itemtap: NavBarItemTap,
                }, getItemTextTpl: function (node) {

                    return '<tpl if="icon"><div class="'+icon_class+'" style="'+icon_style+'(/images/{icon}.png);"></div></tpl><div class="oph_label">{text}</div><span class="arrow"></span>';
                }

            }));
			
			
			
			
            leftNavPanel.doBack = function (me, node, lastActiveList, detailCardActive) {
                broadCrumb.pop();


                if (UIobjects[broadCrumb[broadCrumb.length - 1][0]].length > 0) {
                    if (clickOnLeaf) {
                        me.getActiveItem().deselectAll();
                    } else {
                        this.goToNode(node.parentNode);
                    }
                } else {
                    broadCrumb.pop();
                    this.goToNode(node.parentNode);
                }
                clickOnLeaf = false;

                goToPage(broadCrumb[broadCrumb.length - 1][0]);
                setCurrentLeftNavPage(broadCrumb[broadCrumb.length - 1][0]);
                if (broadCrumb.length <= 1) {
                    me.getBackButton().hide();
                }

            }

            
        }
        sitemapUIpanel.add({
                docked: 'top',
                xtype: 'toolbar',
                ui: 'light',
                items: [{
                    id: 'back_btn',
                    ui: 'back',
                    text: OpenHAB.i18n_strings[ui_language].back,
                    handler: backPage,
                    hidden: true,

                }, {
                    xtype: 'spacer'
                }, {
                    id: 'title',
                    xtype: 'label',
                    cls: 'oph_title',
                    centered: true,
					zIndex:0
                }, {
                    xtype: 'spacer'
                }, {
                    id: 'settings_btn',
                    iconCls: 'settings',
                    iconMask: true,
                    handler: showSettingsWindow
                }

                ]
            });


        sitemap = localStorage.getItem('openHAB_sitemap');

        if (sitemap == '' || sitemap == null || sitemap == 'null' || sitemap == 'choose') {
			sitemapsPanel = new Ext.Panel(sitemapsWindow);
			//sitemapsPanel.setWidth(Ext.Viewport.getWindowWidth()*0.9);
			//sitemapsPanel.setHeight(Ext.Viewport.getWindowHeight()*0.9);
            sitemapsPanel.setCls(deviceTypeCls + '_modal');
            Ext.Viewport.add(sitemapsPanel);

        } else {
            loadUIData(sitemap);
        }

    }
});

function setTitle(value) {
    broadCrumb[broadCrumb.length - 1][1] = value;
    refreshTitle();
}

function refreshTitle() {
    if (deviceType == "Phone") {
        if (broadCrumb.length == 1) {
            Ext.getCmp('back_btn').hide();
        } else {
            Ext.getCmp('back_btn').show();
        }

        broadCrumbText = broadCrumb[broadCrumb.length - 1][1].replace(/[\[\]']+/g, '');
    } else {
        broadCrumbText = '';
        for (var k in broadCrumb) {
            broadCrumbText += broadCrumb[k][1].replace(/[\[\]']+/g, '');
            if (k != broadCrumb.length - 1) {
                broadCrumbText += ' > '
            }
        }
    }
    Ext.getCmp('title').setHtml(broadCrumbText);
}

function setCurrentLeftNavPage(value) {
    currentLeftNavPage = value;
}

