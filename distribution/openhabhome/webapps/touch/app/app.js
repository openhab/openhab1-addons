/*
 * ----------------- OpenHAB Advanced UI -------------------
 *
 *
 *     Version: 0.9.1
 *     Developed by: Mihail Panayotov
 *     E-mail: mishoboss@gmail.com
 *
 *
 * -----------------------------------------------------------
 * 
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 * 
 * 
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 * 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * 
 * If you modify this Program, or any covered work, by linking or 
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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




Ext.define('Oph.Communication', {
    config: {
        comm_method: 'ajax'
    },

    request: function (options) {
        options = options || {};
        var me = this,
            scope = options.scope || window,
            comm_method = this.config.comm_method,
            method = options.method,
            params = options.params,
            success_fn = options.success,
            failure_fn = options.failure,
            url = options.url,
            headers = options.headers,
            request;



        if (comm_method == 'ajax') {
            Ext.Ajax.request({
                url: url,
                method: method,
                params: params,
                headers: headers,
                disableCaching: true,
                failure: function (response) {
                    Ext.callback(options.failure, me.scope, [response]);
                }, success: function (response) {
                    Ext.callback(options.success, me.scope, [response]);
                }
            });


        } else if (comm_method == 'websocket') {
            alert('socket');


        }


    },


    constructor: function (config) {
        this.initConfig(config);
        if (this.config.comm_method == 'websocket') {
            if ("WebSocket" in window) {
                var ws = new WebSocket("ws://localhost:8080/");
                ws.onopen = function () {
                    ws.send("Message to send");
                    alert("Message is sent...");
                };



            } else {
                alert('websockets not supported');
            }
        }


    },



});


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



function getLocalStoreItem(item_name, default_value){
	var value = localStorage.getItem(item_name);
	if(!value || value == "null"){
		return default_value;
	} else {
		return value;
	}
}



//theme_css_filename = './themes/' + getLocalStoreItem('openHAB_theme', 'simple') + '/css/style.css'
theme_css_filename = './themes/' + 'simple' + '/css/style.css'

var theme_css = document.createElement("link");
theme_css.setAttribute("rel", "stylesheet");
theme_css.setAttribute("type", "text/css");
theme_css.setAttribute("href", theme_css_filename);
document.getElementsByTagName("head")[0].appendChild(theme_css);





var ui_language = getLocalStoreItem('openHAB_language', 'en');
var transitions = getLocalStoreItem('openHAB_transitions', '1');

var oph_communication = Ext.create('Oph.Communication', {
    comm_method: 'ajax' //websocket, ajax
});

var sitemapStoreLoadTries = 3;
var sitemapsStoreSelection = new Ext.data.Store({
    fields: ['name']
});
var sitemapsStore = new Ext.data.Store({
    fields: ['name'],
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
                sitemapStoreLoadTries = 3;
                sitemapsStoreSelection.insert(0, [{
                    name: '- ' + OpenHAB.i18n_strings[ui_language].choose_on_startup
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

    layout: 'fit',
    scrollable: 'vertical',
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
        listeners: {
            itemtap: function (view, index) {
                sitemap = view.getStore().getAt(index).data.name;
                loadUIData(view.getStore().getAt(index).data.name);
                Ext.getCmp('sitemapsWindow').destroy();
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


    scrollable: 'vertical',
    items: [{
        title: OpenHAB.i18n_strings[ui_language].settings,
        xtype: 'toolbar',
        ui: 'light',
        docked: 'top'
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].default_sitemap,
        labelWidth: '40%',
        store: sitemapsStoreSelection,
        displayField: 'name',
        valueField: 'name',
        //value: sitemap
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].this_device_is,
        labelWidth: '40%',
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
    }, {
        xtype: 'selectfield',
        label: OpenHAB.i18n_strings[ui_language].theme,
        labelWidth: '40%',
		disabled:true
    }, {
        xtype: 'togglefield',
        label: OpenHAB.i18n_strings[ui_language].transitions,
        labelWidth: '40%',
    }, {
        xtype: 'button',
        ui: 'confirm',
        text: OpenHAB.i18n_strings[ui_language].save,
        style: 'width:30%;margin:10px 10%;float:left;',
        scope: this,
        handler: function () {

            if (settingsPanel.items.items[1].getValue().charAt(0) == '-') {
                localStorage.setItem('openHAB_sitemap', '');
            } else {
                localStorage.setItem('openHAB_sitemap', settingsPanel.items.items[1].getValue());
            }

            localStorage.setItem('openHAB_device_type', settingsPanel.items.items[2].getValue());
            localStorage.setItem('openHAB_language', settingsPanel.items.items[3].getValue());
            localStorage.setItem('openHAB_theme', settingsPanel.items.items[4].getValue());
			localStorage.setItem('openHAB_transitions', settingsPanel.items.items[5].getValue());

            alert(OpenHAB.i18n_strings[ui_language].need_to_restart_for_changes_to_take_effect);
            window.location.reload();
        }
    }, {
        xtype: 'button',
        ui: 'decline',
        text: OpenHAB.i18n_strings[ui_language].cancel,
        style: 'width:30%;margin:10px 10%;float:right;',
        scope: this,
        handler: function () {
            settingsPanel.destroy();
            settingsPanel = null;
        }
    }]
};


















function loadUIData(sitemap_name) {
    Ext.getCmp('content').setMasked({
		xtype: 'loadmask',
        message: OpenHAB.i18n_strings[ui_language].loading
    });

    oph_communication.request({
        url: '/rest/sitemaps/' + sitemap_name,
        comm_method: 'ajax',
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

    if (json) {

        var container;


        UIobjects[json.id] = new Array();
        container = UIobjects[json.id];
        var widget_id = 0;


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
                    pushWidget(addsWidget(widget_id, page_data[i], container, nav_parent), container);
                    widget_id++;
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
                            pushWidget(addsWidget(widget_id, page_data[i].widget[k], container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                            widget_id++;
                        }
                    } else {
                        pushWidget(addsWidget(widget_id, page_data[i].widget, container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                        widget_id++;
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
                pushWidget(addsWidget(widget_id, page_data, container, nav_parent), container);
                widget_id++;
            } else if (page_data.type == "Frame") {
                container.push({
                    xtype: 'fieldset',
                    title: page_data.label,
                    cls: ['oph_fieldset', deviceTypeCls + '_fieldset', 'oph_clearfix']
                });
                container[container.length - 1]['items'] = new Array();
                if (Ext.isArray(page_data.widget)) {
                    for (var k in page_data.widget) {
                        pushWidget(addsWidget(widget_id, page_data.widget[k], container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                        widget_id++;
                    }
                } else {
                    pushWidget(addsWidget(widget_id, page_data.widget, container[container.length - 1]['items'], nav_parent), container[container.length - 1]['items']);
                    widget_id++;
                }
            }



        }

    } else {
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
            widget = createRollershutterWidget(data.item ? data.item.name : '', data.mapping)
        } else if (data.item && data.item.type == "NumberItem") {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else if (data.item && data.item.type == "SwitchItem" && data.mapping) {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else if (data.item && data.item.type == "GroupItem" && data.mapping) {
            widget = createButtonsWidget(data.item ? data.item.name : '', data.mapping, data.label, data.icon);
        } else {
            widget = createToggleWidget(data.item ? data.item.name : '');
        }

    } else if (data.type == "Slider") {
        widget = createSliderWidget(data.item ? data.item.name : '');

    } else if (data.type == "Text") {
        if (data.linkedPage) {
            if (deviceType == "Phone") {
                widget = createLinkWidget(data.linkedPage.id, data.linkedPage.title);

            } else {
                navigation_parent = addButtonToLeftNav(id, data, nav_parent);
            }
            buildUIArray(data.linkedPage, navigation_parent);
        } else {
            widget = createTextWidget();
        }
    } else if (data.type == "Group") {
        if (deviceType == "Phone") {
            widget = createLinkWidget(data.linkedPage.id, data.linkedPage.title);

        } else {
            navigation_parent = addButtonToLeftNav(id, data, nav_parent);
        }

        buildUIArray(data.linkedPage, navigation_parent);

    } else if (data.type == "Selection") {
        widget = createSelectionWidget(data.item ? data.item.name : '', data.mapping);
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
        widget.name = 'obj' + id;
    }
    return widget;

}


function addButtonToLeftNav(id, data, nav_parent) {
	
    pushWidget({
        icon: data.icon,
        text: data.label.replace(/[\[\]']+/g, ''),
        page_id: data.linkedPage.id,
        page_label: data.linkedPage.title,
        leaf: true,
        name: 'obj' + id
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


        settingsPanel.getItems().items[1].setValue(localStorage.getItem('openHAB_sitemap'));
        settingsPanel.getItems().items[2].setValue(localStorage.getItem('openHAB_device_type'));


        var options_array = new Array();
        for (var i in OpenHAB.i18n_strings) {
            options_array.push({
                text: OpenHAB.i18n_strings[i].language_name,
                value: i
            })
        }
        settingsPanel.getItems().items[3].setOptions(options_array);
        settingsPanel.getItems().items[3].setValue(localStorage.getItem('openHAB_language'));


        options_array = [];
        for (var i in OpenHAB.themes) {
            options_array.push({
                text: OpenHAB.themes[i],
                value: OpenHAB.themes[i]
            })
        }
        settingsPanel.getItems().items[4].setOptions(options_array);
        settingsPanel.getItems().items[4].setValue(localStorage.getItem('openHAB_theme'));
		settingsPanel.getItems().items[5].setValue(transitions);
				
        Ext.Viewport.add(settingsPanel);
    }
}






function NavBarItemTap(list, item, index, e, data) {
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
            update_type_header = {
                'Accept': 'application/json',
                'X-Atmosphere-Transport': 'long-polling'
            };
            //update_type_timeout = 180000; //3 minutes
			update_type_timeout = 30000; //3 minutes
        }

        //Ext.Ajax.setTimeout(1);

		
		for(var i=ajax_requests.length-1; i>=0; i--) {
			 if (ajax_requests.hasOwnProperty(i)) {
		   Ext.Ajax.abort(ajax_requests[i]);
		   ajax_requests.pop();
			 }
        }
        ajax_requests.push(Ext.Ajax.request({

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
					
                    //alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
                    updateWidgetsAjax(page, 'normal');
                }

            }, success: function (result_obj) {
				//console.log('--- success ----');
                //console.log(result_obj);
                if (result_obj.statusText == "OK" && result_obj.responseText != "") {
					
					try {
                        var result = Ext.JSON.decode(result_obj.responseText);
					} catch(exception){
						updateWidgetsAjax(page, 'normal');
						return;
					}
                    updateModel = {};
                    var curr_widget = 0;
                    if (Ext.isArray(result.widget)) {
                        for (var i = 0 in result.widget) {
                            if (result.widget[i].type == "Frame") {
                                if (Ext.isArray(result.widget[i].widget)) {
                                    for (var k = 0 in result.widget[i].widget) {
                                        updateModel['obj' + curr_widget] = result.widget[i].widget[k];
                                        curr_widget++;
                                    }
                                } else {
                                    updateModel['obj' + curr_widget] = result.widget[i].widget;
                                    curr_widget++;
                                }
                            } else {
                                updateModel['obj' + curr_widget] = result.widget[i];
                                curr_widget++;
                            }
                        }
                    } else {
                        updateModel['obj' + curr_widget] = result.widget;
                        curr_widget++;
                    }
                    newCard.setValuesData(updateModel);
                    if (deviceType != 'Phone') {
                        if (result.parent && result.parent.id == currentLeftNavPage) {
                            setTitle(result.title);
                            updateLeftNavMenuItem(result.id, result.title, result.icon);
                        } else if (result.id == currentLeftNavPage) {
                            refreshTitle();
                            updateLeftNavMenu(updateModel);
                        } else {
                            refreshTitle();
                        }

                    } else {
                        setTitle(result.title);
                    }
					updateWidgetsAjax(page, 'long-poll');
                } else {
					//updateWidgetsAjax(page, 'normal');
                    
					
					//alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
                }
                
            }
        }));
    }
}

function updateLeftNavMenuItem(page_id, title, icon) {
    nav_store = Ext.getCmp('leftPanel').getActiveItem().getStore();
    index = nav_store.find('page_id', page_id);
    nav_store.getAt(index).set("text", title.replace(/[\[\]']+/g, ''));
	nav_store.getAt(index).set("icon", icon);
}

function updateLeftNavMenu(updateModel) {

    nav_store = Ext.getCmp('leftPanel').getActiveItem().getStore();
    nav_items = nav_store.data.items;
    //console.log(nav_store.data);
    for (i in nav_items) {
        nav_store.getAt(i).set("text", updateModel[nav_store.getAt(i).data.name].label.replace(/[\[\]']+/g, ''));
		nav_store.getAt(i).set("icon", updateModel[nav_store.getAt(i).data.name].icon);

    }

}




// -------- here we set some variables depending on device type and orientation ---------- 

function setProfile() {
    deviceType = localStorage.getItem('openHAB_device_type');
    if (deviceType && deviceType != 'Auto') {} else {
        deviceType = Ext.os.deviceType;
    }

    if (deviceType == "Phone") {
        deviceTypeCls = 'oph_phone';
    } else if (deviceType == "Tablet") {
        deviceTypeCls = 'oph_tablet';
    } else {
        deviceTypeCls = 'oph_desktop';
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/\[(.*?)\]/, '')+'</div>');
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
                    tap: function () {
                        sendCommand(component.config.oph_item, component.formatOutputValue(component.getValue()))
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
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
                    sendCommand(component.config.oph_item, "STOP")
                }
            }
        });
        but2 = container.add(Ext.create('Ext.Button', {
            cls: "x-button-normal oph_rollershutter_btn",
            iconCls: 'delete',
            iconMask: true
        }));
        but2.element.item = component.config.oph_item;
        but2.on({
            tap: function () {
                sendCommand(component.config.oph_item, "STOP")
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
                    sendCommand(component.config.oph_item, "STOP")
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
        this.setLabel('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');

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
        this.setHtml('<img class="oph_icon" src="/images/' + newValue.icon + '.png" /><div class="oph_label">' + newValue.label.replace(/[\[\]']+/g, '')+'</div>');
        return this;
    }, getName: function () {
        return this.config.name;
    }
});





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







function createSliderWidget(oph_item) {
    return {
        xtype: 'oph_sliderfield',
        minValue: 0,
        maxValue: 100,
        oph_item: oph_item
    };
}

function createTextWidget() {

    return {
        xtype: 'oph_textfield'
    };
}

function createToggleWidget(oph_item) {
    return {
        xtype: 'oph_togglefield',
        oph_item: oph_item
    };

}

function createRollershutterWidget(oph_item) {
    return {
        xtype: 'oph_rollershutterfield',
        oph_item: oph_item
    };

}



function createLinkWidget(page_id, page_label) {

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
        page_label: page_label

    }

}


function createSelectionWidget(oph_item, options) {
    return {
        xtype: 'oph_selectfield',
        options: options,
        oph_item: oph_item
    }
};







function createButtonsWidget(oph_item, options, oph_label, oph_icon) {
    return {
        xtype: 'oph_buttons_selectfield',
        options: options,
        oph_item: oph_item,
		label: '<img class="oph_icon" src="/images/' + oph_icon + '.png" /><div class="oph_label">' + oph_label.replace(/[\[\]']+/g, '')+'</div>'
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




Ext.application({
    name: 'OpenHAB',
	icon: '/images/icon.png',
    tabletStartupScreen: '/images/splash-ipad-v.png',
    phoneStartupScreen: '/images/splash-iphone.png',
    glossOnIcon: false,

    launch: function() {
        setProfile();
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
			        oldcard.clearListeners();
                    oldcard.removeAll(true);
                    oldcard.destroy();
			}
			newcard.on({
                tap: tapHandler,
                delegate: 'button'


            });
		}, sitemapUIpanel);
        

        sitemapUIpanel.onBefore('activeitemchange', function (container, newcard, oldcard, opts) {
			newcard.on({show:function(){

            // ---------- Do the two-column layout ----------
            var left_column_height = 0;
            var right_column_height = 0;
            var items = newcard.getItems();
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
                //displayField: 'text',
                backText: OpenHAB.i18n_strings[ui_language].back,
                updateTitleText: false,
                store: leftPanelstore,
                listeners: {
                    itemtap: NavBarItemTap,
                }, getItemTextTpl: function (node) {

                    return '<tpl if="icon"><img class="oph_icon" src="/images/{icon}.png" /></tpl><div class="oph_label">{text}</div>';
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

        if (sitemap) {

            loadUIData(sitemap);

        } else {

            var sitemapsPanel = new Ext.Panel(sitemapsWindow);
            sitemapsPanel.setCls(deviceTypeCls + '_modal');
            Ext.Viewport.add(sitemapsPanel);

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