Ext.define("Oph.Communication",{config:{comm_method:"ajax"},request:function(_1){
_1=_1||{};
var me=this,_2=_1.scope||window,_3=this.config.comm_method,_4=_1.method,_5=_1.params,_6=_1.success,_7=_1.failure,_8=_1.url,_9=_1.headers,_a;
if(_3=="ajax"){
Ext.Ajax.request({url:_8,method:_4,params:_5,headers:_9,disableCaching:true,failure:function(_b){
Ext.callback(_1.failure,me.scope,[_b]);
},success:function(_c){
Ext.callback(_1.success,me.scope,[_c]);
}});
}else{
if(_3=="websocket"){
alert("socket");
}
}
},constructor:function(_d){
this.initConfig(_d);
if(this.config.comm_method=="websocket"){
if("WebSocket" in window){
var ws=new WebSocket("ws://localhost:8080/");
ws.onopen=function(){
ws.send("Message to send");
alert("Message is sent...");
};
}else{
alert("websockets not supported");
}
}
},});
var UIobjects={};
var UInavPanel={expanded:true,text:"Left nav",children:[]};
var ajax_requests=new Array();
var broadCrumb=new Array();
var broadCrumbText="";
var newCard;
var settingsPanel=null;
var updateModel={};
var deviceTypeCls="";
var deviceType;
var clickOnLeaf=false;
var firstTime=true;
var sitemap;
var currentLeftNavPage;
var leftNavPanel;
var sitemapUIcontainer;
var transition={type:"slide",direction:"left"};
function getLocalStoreItem(_e,_f){
var _10=localStorage.getItem(_e);
if(!_10||_10=="null"){
return _f;
}else{
return _10;
}
};
theme_css_filename="./themes/"+"simple"+"/css/style.css";
var theme_css=document.createElement("link");
theme_css.setAttribute("rel","stylesheet");
theme_css.setAttribute("type","text/css");
theme_css.setAttribute("href",theme_css_filename);
document.getElementsByTagName("head")[0].appendChild(theme_css);
var ui_language=getLocalStoreItem("openHAB_language","en");
var transitions=getLocalStoreItem("openHAB_transitions","1");
var oph_communication=Ext.create("Oph.Communication",{comm_method:"ajax"});
var sitemapStoreLoadTries=3;
var sitemapsStoreSelection=new Ext.data.Store({fields:["name"]});
var sitemapsStore=new Ext.data.Store({fields:["name"],autoLoad:true,listeners:{exception:function(_11,_12,op,opt){
if(sitemapStoreLoadTries>0){
sitemapsStore.load();
sitemapStoreLoadTries--;
}else{
sitemapStoreLoadTries=3;
alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
}
},load:function(_13,_14,_15){
if(_15){
sitemapStoreLoadTries=3;
sitemapsStoreSelection.insert(0,[{name:"- "+OpenHAB.i18n_strings[ui_language].choose_on_startup}]);
sitemapsStore.each(function(_16){
sitemapsStoreSelection.add(_16.copy());
});
}else{
if(sitemapStoreLoadTries>0){
sitemapsStore.load();
sitemapStoreLoadTries--;
}else{
sitemapStoreLoadTries=3;
alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
}
}
}},proxy:{type:"ajax",url:"/rest/sitemaps",headers:{"Accept":"application/json",},noCache:true,limitParam:undefined,startParam:undefined,pageParam:undefined,reader:{type:"json",rootProperty:"sitemap"}}});
var sitemapsWindow={id:"sitemapsWindow",floating:true,centered:true,layout:"fit",scrollable:"vertical",items:[{title:OpenHAB.i18n_strings[ui_language].interfaces,xtype:"toolbar",ui:"light",docked:"top"},{xtype:"list",store:sitemapsStore,singleSelect:true,itemTpl:"{name}",listeners:{itemtap:function(_17,_18){
sitemap=_17.getStore().getAt(_18).data.name;
loadUIData(_17.getStore().getAt(_18).data.name);
Ext.getCmp("sitemapsWindow").destroy();
}}}]};
function getCurrentPageId(){
return broadCrumb[broadCrumb.length-1][0];
};
var settingsWindow={id:"settingsWindow",floating:true,centered:true,scrollable:"vertical",items:[{title:OpenHAB.i18n_strings[ui_language].settings,xtype:"toolbar",ui:"light",docked:"top"},{xtype:"selectfield",label:OpenHAB.i18n_strings[ui_language].default_sitemap,labelWidth:"40%",store:sitemapsStoreSelection,displayField:"name",valueField:"name",},{xtype:"selectfield",label:OpenHAB.i18n_strings[ui_language].this_device_is,labelWidth:"40%",options:[{text:OpenHAB.i18n_strings[ui_language].auto_detect,value:"Auto"},{text:OpenHAB.i18n_strings[ui_language].phone,value:"Phone"},{text:OpenHAB.i18n_strings[ui_language].tablet,value:"Tablet"},{text:OpenHAB.i18n_strings[ui_language].pc,value:"Desktop"}],},{xtype:"selectfield",label:OpenHAB.i18n_strings[ui_language].interface_language,labelWidth:"40%",},{xtype:"selectfield",label:OpenHAB.i18n_strings[ui_language].theme,labelWidth:"40%",disabled:true},{xtype:"togglefield",label:OpenHAB.i18n_strings[ui_language].transitions,labelWidth:"40%",},{xtype:"button",ui:"confirm",text:OpenHAB.i18n_strings[ui_language].save,style:"width:30%;margin:10px 10%;float:left;",scope:this,handler:function(){
if(settingsPanel.items.items[1].getValue().charAt(0)=="-"){
localStorage.setItem("openHAB_sitemap","");
}else{
localStorage.setItem("openHAB_sitemap",settingsPanel.items.items[1].getValue());
}
localStorage.setItem("openHAB_device_type",settingsPanel.items.items[2].getValue());
localStorage.setItem("openHAB_language",settingsPanel.items.items[3].getValue());
localStorage.setItem("openHAB_theme",settingsPanel.items.items[4].getValue());
localStorage.setItem("openHAB_transitions",settingsPanel.items.items[5].getValue());
alert(OpenHAB.i18n_strings[ui_language].need_to_restart_for_changes_to_take_effect);
window.location.reload();
}},{xtype:"button",ui:"decline",text:OpenHAB.i18n_strings[ui_language].cancel,style:"width:30%;margin:10px 10%;float:right;",scope:this,handler:function(){
settingsPanel.destroy();
settingsPanel=null;
}}]};
function loadUIData(_19){
Ext.getCmp("content").setMasked({xtype:"loadmask",message:OpenHAB.i18n_strings[ui_language].loading});
oph_communication.request({url:"/rest/sitemaps/"+_19,comm_method:"ajax",headers:{"Accept":"application/json",},success:function(_1a){
try{
result=Ext.JSON.decode(_1a.responseText);
}
catch(error){
loadUIData(_19);
return;
}
buildUIArray(result.homepage,UInavPanel);
clearEmptyFrames();
Ext.getCmp("content").unmask();
broadCrumb[0]=new Array(result.homepage.id,result.homepage.title);
Ext.getCmp("title").setHtml(result.homepage.title);
goToPage(result.homepage.id);
if(Ext.getCmp("leftPanel")){
leftPanelstore.setRoot(UInavPanel);
setCurrentLeftNavPage(result.homepage.id);
}
Ext.getCmp("content").unmask();
},failure:function(){
alert(OpenHAB.i18n_strings[ui_language].error_server_connection);
}});
};
Ext.define("LeftPanelListItem",{extend:"Ext.data.Model",config:{fields:[{name:"icon",type:"string"},{name:"text",type:"string"},{name:"page_id",type:"string"},{name:"page_label",type:"string"},{name:"name",type:"string"},]}});
var leftPanelstore=new Ext.data.TreeStore({model:"LeftPanelListItem",autoLoad:false,proxy:{type:"memory",reader:{type:"json",rootProperty:"children"}}});
function pushWidget(_1b,_1c){
if(_1b){
_1c.push(_1b);
}
};
function buildUIArray(_1d,_1e){
if(_1d){
var _1f;
UIobjects[_1d.id]=new Array();
_1f=UIobjects[_1d.id];
var _20=0;
var _21=_1d.widget;
if(Ext.isArray(_21)){
var _22=false;
for(var i in _21){
if(_21[i].type!="Frame"){
if(!_22){
_1f.push({xtype:"fieldset",title:"",cls:["oph_fieldset",deviceTypeCls+"_fieldset","oph_clearfix"]});
_1f=_1f[_1f.length-1]["items"]=new Array();
_22=true;
}
pushWidget(addsWidget(_20,_21[i],_1f,_1e),_1f);
_20++;
}else{
if(_21[i].type=="Frame"){
_22=false;
_1f.push({xtype:"fieldset",title:_21[i].label,cls:["oph_fieldset",deviceTypeCls+"_fieldset","oph_clearfix"]});
_1f[_1f.length-1]["items"]=new Array();
if(Ext.isArray(_21[i].widget)){
for(var k in _21[i].widget){
pushWidget(addsWidget(_20,_21[i].widget[k],_1f[_1f.length-1]["items"],_1e),_1f[_1f.length-1]["items"]);
_20++;
}
}else{
pushWidget(addsWidget(_20,_21[i].widget,_1f[_1f.length-1]["items"],_1e),_1f[_1f.length-1]["items"]);
_20++;
}
}
}
}
}else{
if(_21.type!="Frame"){
_1f.push({xtype:"fieldset",title:"",cls:["oph_fieldset",deviceTypeCls+"_fieldset","oph_clearfix"]});
_1f=_1f[_1f.length-1]["items"]=new Array();
pushWidget(addsWidget(_20,_21,_1f,_1e),_1f);
_20++;
}else{
if(_21.type=="Frame"){
_1f.push({xtype:"fieldset",title:_21.label,cls:["oph_fieldset",deviceTypeCls+"_fieldset","oph_clearfix"]});
_1f[_1f.length-1]["items"]=new Array();
if(Ext.isArray(_21.widget)){
for(var k in _21.widget){
pushWidget(addsWidget(_20,_21.widget[k],_1f[_1f.length-1]["items"],_1e),_1f[_1f.length-1]["items"]);
_20++;
}
}else{
pushWidget(addsWidget(_20,_21.widget,_1f[_1f.length-1]["items"],_1e),_1f[_1f.length-1]["items"]);
_20++;
}
}
}
}
}else{
alert(OpenHAB.i18n_strings[ui_language].error_build_interface);
}
};
function clearEmptyFrames(){
for(var i in UIobjects){
var _23=[];
for(var k in UIobjects[i]){
if(UIobjects[i][k].items.length>0){
_23.push(UIobjects[i][k]);
}
}
UIobjects[i]=_23;
}
};
function addsWidget(id,_24,_25,_26){
var _27;
var _28;
if(_24.type=="Switch"){
if(_24.item&&_24.item.type=="RollershutterItem"){
_27=createRollershutterWidget(_24.item?_24.item.name:"",_24.mapping);
}else{
if(_24.item&&_24.item.type=="NumberItem"){
_27=createButtonsWidget(_24.item?_24.item.name:"",_24.mapping,_24.label,_24.icon);
}else{
if(_24.item&&_24.item.type=="SwitchItem"&&_24.mapping){
_27=createButtonsWidget(_24.item?_24.item.name:"",_24.mapping,_24.label,_24.icon);
}else{
if(_24.item&&_24.item.type=="GroupItem"&&_24.mapping){
_27=createButtonsWidget(_24.item?_24.item.name:"",_24.mapping,_24.label,_24.icon);
}else{
_27=createToggleWidget(_24.item?_24.item.name:"");
}
}
}
}
}else{
if(_24.type=="Slider"){
_27=createSliderWidget(_24.item?_24.item.name:"");
}else{
if(_24.type=="Text"){
if(_24.linkedPage){
if(deviceType=="Phone"){
_27=createLinkWidget(_24.linkedPage.id,_24.linkedPage.title);
}else{
_28=addButtonToLeftNav(id,_24,_26);
}
buildUIArray(_24.linkedPage,_28);
}else{
_27=createTextWidget();
}
}else{
if(_24.type=="Group"){
if(deviceType=="Phone"){
_27=createLinkWidget(_24.linkedPage.id,_24.linkedPage.title);
}else{
_28=addButtonToLeftNav(id,_24,_26);
}
buildUIArray(_24.linkedPage,_28);
}else{
if(_24.type=="Selection"){
_27=createSelectionWidget(_24.item?_24.item.name:"",_24.mapping);
}else{
if(_24.type=="Image"){
if(_24.linkedPage){
if(deviceType=="Phone"){
_27=createImageLinkWidget(_24.url,_24.linkedPage.id,_24.linkedPage.title);
}else{
_28=addButtonToLeftNav(id,_24,_26);
_27=createImageLinkWidget(_24.url,_24.linkedPage.id,_24.linkedPage.title);
}
buildUIArray(_24.linkedPage,_28);
}else{
_27=createImageWidget(_24.url);
}
}else{
if(_24.type=="Frame"){
}else{
_27=createUnsupportedWidget();
}
}
}
}
}
}
}
if(_27){
_27.name="obj"+id;
}
return _27;
};
function addButtonToLeftNav(id,_29,_2a){
pushWidget({icon:_29.icon,text:_29.label.replace(/[\[\]']+/g,""),page_id:_29.linkedPage.id,page_label:_29.linkedPage.title,leaf:true,name:"obj"+id},_2a.children);
_2a.leaf=false;
_2a.children[_2a.children.length-1].children=new Array();
return _2a.children[_2a.children.length-1];
};
function showSettingsWindow(){
if(settingsPanel){
settingsPanel.destroy();
settingsPanel=null;
}else{
settingsPanel=new Ext.Panel(settingsWindow);
settingsPanel.setCls(deviceTypeCls+"_modal");
settingsPanel.getItems().items[1].setValue(localStorage.getItem("openHAB_sitemap"));
settingsPanel.getItems().items[2].setValue(localStorage.getItem("openHAB_device_type"));
var _2b=new Array();
for(var i in OpenHAB.i18n_strings){
_2b.push({text:OpenHAB.i18n_strings[i].language_name,value:i});
}
settingsPanel.getItems().items[3].setOptions(_2b);
settingsPanel.getItems().items[3].setValue(localStorage.getItem("openHAB_language"));
_2b=[];
for(var i in OpenHAB.themes){
_2b.push({text:OpenHAB.themes[i],value:OpenHAB.themes[i]});
}
settingsPanel.getItems().items[4].setOptions(_2b);
settingsPanel.getItems().items[4].setValue(localStorage.getItem("openHAB_theme"));
settingsPanel.getItems().items[5].setValue(transitions);
Ext.Viewport.add(settingsPanel);
}
};
function NavBarItemTap(_2c,_2d,_2e,e,_2f){
leftNavPanel.getBackButton().show();
if(_2f.raw.leaf){
if(clickOnLeaf){
broadCrumb.pop();
}
clickOnLeaf=true;
}else{
if(clickOnLeaf){
broadCrumb.pop();
}
clickOnLeaf=false;
setCurrentLeftNavPage(_2f.raw.page_id);
}
broadCrumb.push([_2f.raw.page_id,_2f.raw.page_label]);
if(UIobjects[_2f.raw.page_id].length>0){
goToPage(_2f.raw.page_id);
}
};
function tapHandler(btn,evt){
if(deviceType=="Phone"){
broadCrumb.push([btn.config.page_id,btn.config.page_label]);
transition={type:"slide",direction:"left"};
goToPage(btn.config.page_id);
}else{
leftNav=Ext.getCmp("leftPanel").getActiveItem();
index=leftNav.getStore().getAt(leftNav.getStore().find("page_id",btn.config.page_id)).data.index;
leftNav.select(index,false,false);
NavBarItemTap(leftNav,"",index,"",leftNav.getStore().getAt(leftNav.getStore().find("page_id",btn.config.page_id)));
}
};
function backPage(){
if(broadCrumb.length>1){
broadCrumb.pop();
transition={type:"slide",direction:"right"};
goToPage(broadCrumb[broadCrumb.length-1][0]);
}
};
function goToPage(_30){
newCard=new Oph.form.Panel({scrollable:"vertical",autoDestroy:true,cls:["oph_widgets_container",deviceTypeCls+"_widgets_container"]});
newCard.add(UIobjects[_30]);
if(transitions=="0"){
Ext.getCmp("content").setActiveItem(newCard);
}else{
if(transitions=="1"){
Ext.getCmp("content").animateActiveItem(newCard,transition);
}
}
};
function updateWidgetsAjax(_31,_32){
if(_31==getCurrentPageId()){
var _33="";
var _34=5000;
if(!_32||_32=="normal"){
_33={"Accept":"application/json"};
}else{
if(_32=="long-poll"){
_33={"Accept":"application/json","X-Atmosphere-Transport":"long-polling"};
_34=30000;
}
}
for(var i=ajax_requests.length-1;i>=0;i--){
if(ajax_requests.hasOwnProperty(i)){
Ext.Ajax.abort(ajax_requests[i]);
ajax_requests.pop();
}
}
ajax_requests.push(Ext.Ajax.request({url:"/rest/sitemaps/"+sitemap+"/"+_31,headers:_33,disableCaching:true,timeout:_34,failure:function(_35){
if(_32=="long-poll"){
updateWidgetsAjax(_31,"long-poll");
}else{
updateWidgetsAjax(_31,"normal");
}
},success:function(_36){
if(_36.statusText=="OK"&&_36.responseText!=""){
try{
var _37=Ext.JSON.decode(_36.responseText);
}
catch(exception){
updateWidgetsAjax(_31,"normal");
return;
}
updateModel={};
var _38=0;
if(Ext.isArray(_37.widget)){
for(var i=0 in _37.widget){
if(_37.widget[i].type=="Frame"){
if(Ext.isArray(_37.widget[i].widget)){
for(var k=0 in _37.widget[i].widget){
updateModel["obj"+_38]=_37.widget[i].widget[k];
_38++;
}
}else{
updateModel["obj"+_38]=_37.widget[i].widget;
_38++;
}
}else{
updateModel["obj"+_38]=_37.widget[i];
_38++;
}
}
}else{
updateModel["obj"+_38]=_37.widget;
_38++;
}
newCard.setValuesData(updateModel);
if(deviceType!="Phone"){
if(_37.parent&&_37.parent.id==currentLeftNavPage){
setTitle(_37.title);
updateLeftNavMenuItem(_37.id,_37.title,_37.icon);
}else{
if(_37.id==currentLeftNavPage){
refreshTitle();
updateLeftNavMenu(updateModel);
}else{
refreshTitle();
}
}
}else{
setTitle(_37.title);
}
updateWidgetsAjax(_31,"long-poll");
}else{
}
}}));
}
};
function updateLeftNavMenuItem(_39,_3a,_3b){
nav_store=Ext.getCmp("leftPanel").getActiveItem().getStore();
index=nav_store.find("page_id",_39);
nav_store.getAt(index).set("text",_3a.replace(/[\[\]']+/g,""));
nav_store.getAt(index).set("icon",_3b);
};
function updateLeftNavMenu(_3c){
nav_store=Ext.getCmp("leftPanel").getActiveItem().getStore();
nav_items=nav_store.data.items;
for(i in nav_items){
nav_store.getAt(i).set("text",_3c[nav_store.getAt(i).data.name].label.replace(/[\[\]']+/g,""));
nav_store.getAt(i).set("icon",_3c[nav_store.getAt(i).data.name].icon);
}
};
function setProfile(){
deviceType=localStorage.getItem("openHAB_device_type");
if(deviceType&&deviceType!="Auto"){
}else{
deviceType=Ext.os.deviceType;
}
if(deviceType=="Phone"){
deviceTypeCls="oph_phone";
}else{
if(deviceType=="Tablet"){
deviceTypeCls="oph_tablet";
}else{
deviceTypeCls="oph_desktop";
}
}
};
Ext.define("Oph.field.ButtonsSelect",{extend:"Ext.field.Field",xtype:"oph_buttons_selectfield",config:{cls:"x-buttons-field",styleHtmlContent:true,styleHtmlCls:"oph_none",labelWidth:"50%"},constructor:function(_3d){
_3d=_3d||{};
this.callParent([_3d]);
},applyComponent:function(_3e){
component=this;
container=Ext.factory({styleHtmlContent:true,styleHtmlCls:"oph_right"},Ext.Container);
if(Ext.isArray(this.config.options)){
for(var i in this.config.options){
item=container.add(Ext.create("Ext.Button",{cls:"x-button-normal oph_selection_btn",item:component.config.oph_item,command:component.config.options[i].command,text:this.config.options[i].label}));
item.on({tap:function(){
sendCommand(this.config.item,this.config.command);
}});
}
}else{
item=container.add(Ext.create("Ext.Button",{cls:"x-button-normal oph_selection_btn",item:component.config.oph_item,command:component.config.options.command,text:this.config.options.label}));
item.on({tap:function(){
sendCommand(this.config.item,this.config.command);
}});
}
return container;
},initialize:function(){
this.callParent();
},setValueData:function(_3f){
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_3f.icon+".png\" /><div class=\"oph_label\">"+_3f.label.replace(/[\[\]']+/g,"")+"</div>");
if(Ext.isArray(_3f.mapping)){
for(var i in _3f.mapping){
this._component._items.items[i].removeCls("x-button-action");
if(_3f.item.state==_3f.mapping[i].command){
this._component._items.items[i].addCls("x-button-action");
}
}
}else{
this._component._items.items[0].addCls("x-button-action");
}
}});
Ext.define("Oph.field.Slider",{extend:"Ext.field.Slider",xtype:"oph_sliderfield",config:{labelWidth:"50%"},constructor:function(_40){
_40=_40||{};
this.callParent([_40]);
},initialize:function(){
this.callParent();
this.on({painted:function(_41){
_41._component.element.on({tap:function(){
sendCommand(_41.config.oph_item,_41.getValue());
}});
_41._component.getThumb(0).element.on({dragend:function(){
sendCommand(_41.config.oph_item,_41.getValue());
}});
}});
},setValueData:function(_42){
if(_42.item){
this.setValue(_42.item.state);
}
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_42.icon+".png\" /><div class=\"oph_label\">"+_42.label.replace(/[\[\]']+/g,"")+"</div>");
return this;
}});
Ext.define("Oph.field.Select",{extend:"Ext.field.Select",xtype:"oph_selectfield",config:{displayField:"label",valueField:"command",labelWidth:"50%"},constructor:function(_43){
_43=_43||{};
this.callParent([_43]);
},initialize:function(){
this.callParent();
this.on({painted:function(_44){
_44.on({selected:function(){
sendCommand(_44.config.oph_item,_44.getValue());
}});
}});
},setValueData:function(_45){
if(_45.item){
this.setValue(_45.item.state);
}
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_45.icon+".png\" /><div class=\"oph_label\">"+_45.label.replace(/[\[\]']+/g,"")+"</div>");
return this;
},onListSelect:function(_46,_47){
var me=this;
if(_47){
me.setValue(_47);
this.fireEvent("selected",this);
}
},onPickerChange:function(_48,_49){
var me=this,_4a=me.getValue(),_4b=_49[me.getName()],_4c=me.getStore(),_4d=_4c.find(me.getValueField(),_4b);
record=_4c.getAt(_4d);
me.setValue(record);
this.fireEvent("selected",this);
},});
Ext.define("Oph.field.Text",{extend:"Ext.field.Field",xtype:"oph_textfield",config:{cls:"x-textfield",styleHtmlContent:true,styleHtmlCls:"oph_none",labelWidth:"50%"},constructor:function(_4e){
_4e=_4e||{};
this.callParent([_4e]);
},initialize:function(){
this.callParent();
},setValueData:function(_4f){
this.setHtml(_4f.label.match(/\[(.*?)\]/)?_4f.label.match(/\[(.*?)\]/)[1]:_4f.label);
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_4f.icon+".png\" /><div class=\"oph_label\">"+_4f.label.replace(/\[(.*?)\]/,"")+"</div>");
return this;
}});
Ext.define("Oph.field.Toggle",{extend:"Ext.field.Toggle",xtype:"oph_togglefield",config:{labelWidth:"50%"},constructor:function(_50){
_50=_50||{};
this.callParent([_50]);
},initialize:function(){
this.callParent();
this.on({painted:function(_51){
_51._component.element.on({tap:function(){
sendCommand(_51.config.oph_item,_51.formatOutputValue(_51.getValue()));
}});
_51._component.getThumb(0).element.on({dragend:function(){
sendCommand(_51.config.oph_item,_51.formatOutputValue(_51.getValue()));
}});
}});
},setValueData:function(_52){
if(_52.item){
this.setValue(this.formatInputValue(_52.item.state));
}
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_52.icon+".png\" /><div class=\"oph_label\">"+_52.label.replace(/[\[\]']+/g,"")+"</div>");
return this;
},formatInputValue:function(_53){
if(_53=="ON"){
return 1;
}else{
return 0;
}
},formatOutputValue:function(_54){
if(_54==1){
return "ON";
}else{
if(_54==0){
return "OFF";
}
}
}});
function sendCommand(_55,_56){
if(_55){
Ext.Ajax.request({comm_method:"ajax",url:"/rest/items/"+_55+"/",method:"POST",params:_56,headers:{"Content-Type":"text/plain"},failure:function(){
}});
}
};
Ext.define("Oph.field.Rollershutter",{extend:"Ext.field.Field",xtype:"oph_rollershutterfield",config:{cls:"x-rollershutter-field",styleHtmlContent:true,styleHtmlCls:"oph_none",labelWidth:"50%"},constructor:function(_57){
_57=_57||{};
this.callParent([_57]);
},applyComponent:function(_58){
component=this;
container=Ext.factory({styleHtmlContent:true,styleHtmlCls:"oph_right"},Ext.Container);
but1=container.add(Ext.create("Ext.Button",{cls:"x-button-normal oph_rollershutter_btn",longPress:false,iconCls:"arrow_up",iconMask:true}));
but1.element.item=component.config.oph_item;
but1.element.on({touchstart:function(){
sendCommand(this.item,"UP");
},longpress:function(){
but1.longPress=true;
},tap:function(){
if(but1.longPress){
but1.longPress=false;
sendCommand(component.config.oph_item,"STOP");
}
}});
but2=container.add(Ext.create("Ext.Button",{cls:"x-button-normal oph_rollershutter_btn",iconCls:"delete",iconMask:true}));
but2.element.item=component.config.oph_item;
but2.on({tap:function(){
sendCommand(component.config.oph_item,"STOP");
}});
but3=container.add(Ext.create("Ext.Button",{cls:"x-button-normal oph_rollershutter_btn",longPress:false,iconCls:"arrow_down",iconMask:true}));
but3.element.item=component.config.oph_item;
but3.element.on({touchstart:function(){
sendCommand(this.item,"DOWN");
},longpress:function(){
but3.longPress=true;
},tap:function(){
if(but3.longPress){
but3.longPress=false;
sendCommand(component.config.oph_item,"STOP");
}
}});
return container;
},initialize:function(){
this.callParent();
},initialize:function(){
this.callParent();
},setValueData:function(_59){
this.setLabel("<img class=\"oph_icon\" src=\"/images/"+_59.icon+".png\" /><div class=\"oph_label\">"+_59.label.replace(/[\[\]']+/g,"")+"</div>");
return this;
}});
Ext.define("Oph.field.Button",{extend:"Ext.Button",xtype:"oph_buttonfield",isField:true,config:{style:"display:block",baseCls:"x-field oph_group_btn oph_button",labelCls:"",isField:true,cls:"oph_link"},constructor:function(_5a){
_5a=_5a||{};
this.callParent([_5a]);
},initialize:function(){
this.callParent();
},setValueData:function(_5b){
this.setHtml("<img class=\"oph_icon\" src=\"/images/"+_5b.icon+".png\" /><div class=\"oph_label\">"+_5b.label.replace(/[\[\]']+/g,"")+"</div>");
return this;
},getName:function(){
return this.config.name;
}});
Ext.define("Oph.field.Image",{extend:"Ext.Container",xtype:"oph_imagefield",constructor:function(_5c){
_5c=_5c||{};
this.callParent([_5c]);
if(_5c.url.substr(0,4)!="http"&&_5c.url.substr(0,1)!="/"){
_5c.url="/"+_5c.url;
}
this.setHtml("<div style=\"width:100%;padding:0.4em;\"><img src=\""+_5c.url+"\" style=\"width:100%;\"></div>");
},initialize:function(){
this.callParent();
},setValueData:function(_5d){
}});
Ext.define("Oph.field.ImageLink",{extend:"Ext.Button",xtype:"oph_imagelink",config:{style:"display:block",baseCls:"none",labelCls:"",cls:"oph_link"},constructor:function(_5e){
_5e=_5e||{};
this.callParent([_5e]);
if(_5e.url.substr(0,4)!="http"&&_5e.url.substr(0,1)!="/"){
_5e.url="/"+_5e.url;
}
this.setHtml("<div style=\"width:100%;padding:0.4em;\"><img src=\""+_5e.url+"\" style=\"width:100%;\"></div>");
},initialize:function(){
this.callParent();
},setValueData:function(_5f){
}});
Ext.define("Oph.form.Panel",{extend:"Ext.Panel",xtype:"oph_formpanel",constructor:function(_60){
_60=_60||{};
this.callParent([_60]);
},initialize:function(){
this.callParent();
},setValuesData:function(_61){
var _62=this.getFields(),_63,_64,_65;
_61=_61||{};
for(_63 in _61){
if(_61.hasOwnProperty(_63)){
_64=_62[_63];
_65=_61[_63];
if(_64){
_64.setValueData(_65);
}
}
}
return this;
},getFields:function(_66){
var _67={},_68;
var _69=function(_6a){
if(_6a.isField){
_68=_6a.getName();
if((_66&&_68==_66)||typeof _66=="undefined"){
if(_67.hasOwnProperty(_68)){
if(!Ext.isArray(_67[_68])){
_67[_68]=[_67[_68]];
}
_67[_68].push(_6a);
}else{
_67[_68]=_6a;
}
}
}
if(_6a.isContainer){
_6a.items.each(_69);
}
};
this.items.each(_69);
return (_66)?(_67[_66]||[]):_67;
},});
function createSliderWidget(_6b){
return {xtype:"oph_sliderfield",minValue:0,maxValue:100,oph_item:_6b};
};
function createTextWidget(){
return {xtype:"oph_textfield"};
};
function createToggleWidget(_6c){
return {xtype:"oph_togglefield",oph_item:_6c};
};
function createRollershutterWidget(_6d){
return {xtype:"oph_rollershutterfield",oph_item:_6d};
};
function createLinkWidget(_6e,_6f){
return {xtype:"oph_buttonfield",page_id:_6e,page_label:_6f};
};
function createImageLinkWidget(url,_70,_71){
return {xtype:"oph_imagelink",url:url,page_id:_70,page_label:_71};
};
function createSelectionWidget(_72,_73){
return {xtype:"oph_selectfield",options:_73,oph_item:_72};
};
function createButtonsWidget(_74,_75,_76,_77){
return {xtype:"oph_buttons_selectfield",options:_75,oph_item:_74,label:"<img class=\"oph_icon\" src=\"/images/"+_77+".png\" /><div class=\"oph_label\">"+_76.replace(/[\[\]']+/g,"")+"</div>"};
};
function createUnsupportedWidget(){
return {xtype:"field",label:"[UNSUPPORTED WIDGET]",};
};
function createImageWidget(url){
return {xtype:"oph_imagefield",url:url,cls:"x-image-field",};
};
Ext.application({name:"OpenHAB",icon:"/images/icon.png",tabletStartupScreen:"/images/splash-ipad-v.png",phoneStartupScreen:"/images/splash-iphone.png",glossOnIcon:false,launch:function(){
setProfile();
var _78=Ext.create("Ext.Panel",{layout:{type:"card",},cls:"oph_background",fullscreen:true,id:"content",autoDestroy:true,items:[]});
_78.onAfter("activeitemchange",function(_79,_7a,_7b,_7c){
if(_7b){
_7b.clearListeners();
_7b.removeAll(true);
_7b.destroy();
}
_7a.on({tap:tapHandler,delegate:"button"});
},_78);
_78.onBefore("activeitemchange",function(_7d,_7e,_7f,_80){
_7e.on({show:function(){
var _81=0;
var _82=0;
var _83=_7e.getItems();
for(var i=0;i<_83.length;i++){
if(_81>_82){
_82+=_83.items[i].element.dom.clientHeight;
_83.items[i].addCls(deviceTypeCls+"_fieldset_right");
}else{
_81+=_83.items[i].element.dom.clientHeight;
_83.items[i].addCls(deviceTypeCls+"_fieldset_left");
}
}
}});
updateWidgetsAjax(broadCrumb[broadCrumb.length-1][0],"normal");
},_78);
if(deviceType!="Phone"){
transition={type:"fade",duration:500};
leftNavPanel=_78.add(new Ext.NestedList({docked:"left",width:"14em",scrollable:"auto",id:"leftPanel",backText:OpenHAB.i18n_strings[ui_language].back,updateTitleText:false,store:leftPanelstore,listeners:{itemtap:NavBarItemTap,},getItemTextTpl:function(_84){
return "<tpl if=\"icon\"><img class=\"oph_icon\" src=\"/images/{icon}.png\" /></tpl><div class=\"oph_label\">{text}</div>";
}}));
leftNavPanel.doBack=function(me,_85,_86,_87){
broadCrumb.pop();
if(UIobjects[broadCrumb[broadCrumb.length-1][0]].length>0){
if(clickOnLeaf){
me.getActiveItem().deselectAll();
}else{
this.goToNode(_85.parentNode);
}
}else{
broadCrumb.pop();
this.goToNode(_85.parentNode);
}
clickOnLeaf=false;
goToPage(broadCrumb[broadCrumb.length-1][0]);
setCurrentLeftNavPage(broadCrumb[broadCrumb.length-1][0]);
if(broadCrumb.length<=1){
me.getBackButton().hide();
}
};
}
_78.add({docked:"top",xtype:"toolbar",ui:"light",items:[{id:"back_btn",ui:"back",text:OpenHAB.i18n_strings[ui_language].back,handler:backPage,hidden:true,},{xtype:"spacer"},{id:"title",xtype:"label",cls:"oph_title",centered:true,},{xtype:"spacer"},{id:"settings_btn",iconCls:"settings",iconMask:true,handler:showSettingsWindow}]});
sitemap=localStorage.getItem("openHAB_sitemap");
if(sitemap){
loadUIData(sitemap);
}else{
var _88=new Ext.Panel(sitemapsWindow);
_88.setCls(deviceTypeCls+"_modal");
Ext.Viewport.add(_88);
}
}});
function setTitle(_89){
broadCrumb[broadCrumb.length-1][1]=_89;
refreshTitle();
};
function refreshTitle(){
if(deviceType=="Phone"){
if(broadCrumb.length==1){
Ext.getCmp("back_btn").hide();
}else{
Ext.getCmp("back_btn").show();
}
broadCrumbText=broadCrumb[broadCrumb.length-1][1].replace(/[\[\]']+/g,"");
}else{
broadCrumbText="";
for(var k in broadCrumb){
broadCrumbText+=broadCrumb[k][1].replace(/[\[\]']+/g,"");
if(k!=broadCrumb.length-1){
broadCrumbText+=" > ";
}
}
}
Ext.getCmp("title").setHtml(broadCrumbText);
};
function setCurrentLeftNavPage(_8a){
currentLeftNavPage=_8a;
};

