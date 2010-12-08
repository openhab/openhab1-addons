var WebApp=(function(){var A_=setTimeout;var B_=setInterval;var _ll,_mm,_nn,_nner;var _pp,_qq,_rr,_ss,_tt;var _uu,_vv;var _ww,_xx,_yy;var _zz,_00;var _11=-1;var _22=-1;var _33=[];var _44=[];var _55=[];var _66=[];var _77=history.length;var _88=0;var _99=0;var _AAA="";var _BBB="";var _CCC=0;var _DDD=0;var _EEE=1;var _FFF=null;var _GGG=1;var _HHH="";var _III=0;var _JJJ=B_(_f,250);var _KKK=null;var _LLL=window;var _MMM="data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";var _wkt;var _NNN=!!document.getElementsByClassName&&UA("WebKit");var _OOO=!!navigator.standalone;var _PPP=_N(_LLL.ontouchstart)&&!UA("Android");var _QQQ=_PPP?_2:_3;var _RRR={}
_RRR.load=[];_RRR.beginslide=[];_RRR.endslide=[];_RRR.beginasync=[];_RRR.willasync=[];_RRR.endasync=[];_RRR.orientationchange=[];_RRR.tabchange=[];var _SSS=false;var d=document;var $h={get HEAD(){return 0},get BACK(){return 1},get HOME(){return 2},get LEFT(){return 3},get RIGHT(){return 4},get TITLE(){return 5}}
var $d={get L2R(){return+1},get R2L(){return-1}}
d.wa={get auto_GG(){return _SSS},set auto_GG(v){_SSS=(v=="true"||v=="yes"||v===true)},get header(){return $h},get direction(){return $d}}
d.webapp=d.wa;var $pc={get Version(){return 'v0.6.0 WIP'},Proxy:function(url){_HHH=url},Progressive:function(enable){_III=enable},Opener:function(func){_zz=func?func:function(u){location=u}},Refresh:function(id){if(id!==false){var o=$(id);if(!o){_ee()}else if(o.type=="radio"){_XX([o])}else if(o.type=="checkbox"){_CC(o.previousSibling,1)}}
_9();_0();_q()},HideBar:function(){if(_GGG&&_p()){_GGG=0;_A(1);A_(_A,0)}return false},Header:function(show,what,keep){_BB();if(keep!=-1){_D(!show,keep)}
_h(_mm,0);_mm=$(what);_h(_mm,show);_nner[$h.HEAD].style.zIndex=!show?2:"";return false},Tab:function(id,active){var o=$(id);_u(o,$$("li",o)[active])},AddEventListener:function(evt,handler){if(_N(_RRR[evt])){with(_RRR[evt]){if(indexOf(handler)==-1){push(handler)}}
}},RemoveEventListener:function(evt,handler){if(_N(_RRR[evt])){with(_RRR[evt]){splice(lastIndexOf(handler),1)}}
},Back:function(){if(_99){return(_99=0)}
_00=null;if(history.length-_77==_22){history.back()}else{_zz(_33[_22-1][1])}return false},Home:function(){if(history.length-_77==_22){history.go(-_22)}else{_zz("#")}return(_99=0)},Form:function(frm){var s,a,b,c,o,k,f,t;a=$(frm);b=$(_33[_22][0]);s=(a.style.display!="block");f=_T(a);with(_nner[$h.HEAD]){t=offsetTop+offsetHeight}if(s){a.style.top=t+"px"}if(f){k=f.onsubmit;if(!s){f.onsubmit=f.onsubmit(0,1)}else{f.onsubmit=function(e,b){if(b){return k}if(e){_K(e)}if(!(k&&k(e)===false)){$pc.Submit(this,null,e)}}
}}
_h(a,s);_k(s,t+a.offsetHeight);o=$$("legend",a)[0];_9(s&&o?o.innerHTML:null);_FFF=(s)?a:null;if(s){c=a;a=b;b=c}
_F(a);_E(b,s);if(s){$pc.Header()}else{_D(!s)}return false},Submit:function(frm){var f=_T(frm);if(f){var a=arguments[1];var _=function(i,f){var q="";for(var n=0;n<i.length;n++){i[n].blur();if(i[n].name&&!i[n].disabled&&(f?f(i[n]):1)){q+="&"+i[n].name+"="+encodeURIComponent(i[n].value)}}return q}
var q=_($$("input",f),function(i){with(i){return((_O(type,["text","password","hidden","search"])||(_O(type,["radio","checkbox"])&&checked)))}}
);q+=_($$("select",f));q+=_($$("textarea",f));q+="&"+(a&&a.id?a.id:"__submit")+"=1";q=q.substr(1);var u=($A(f,"action")||self.location.href);if($A(f,"method").toLowerCase()!="post"){u=_MM(u,q);q=null}
_NN(u,null,q);if(_FFF){$pc.Form(_FFF)}}return false},Postable:function(keys,values){var q="";for(var i=1;i<values.length&&i<=keys.length;i++){q+="&"+keys[i-1]+"="+encodeURIComponent(values[i])}return q.replace(/&=/g,"&").substr(1)},Request:function(url,prms,cb,async,loader){if(_22===cb){return}
var r,a=[url,prms];if(!_s("beginasync",a)){if(loader){A_(_a,100,loader,"__sel")}}else{url=a[0];prms=a[1];cb=cb==-1?_OO():cb;var o=new XMLHttpRequest();var c=function(){_UU(o,cb,loader)}
var m=prms?"POST":"GET";async=!!async;if(loader){$pc.Loader(loader,1)}
_66.push([o,a]);url=_LL(url,"__async","true");if(_22>=0){url=_LL(url,"__source",_33[_22][0])}
url=_JJ(url);o.open(m,url,async);if(prms){o.setRequestHeader("Content-Type","application/x-www-form-urlencoded")}
_s("willasync",a,o);o.onreadystatechange=(async)?c:null;o.send(prms);if(!async){c()}}
},Loader:function(obj,show){var o,h,f;if(o=$(obj)){h=_X(o,"__lod");_C(o);if(show){if(h){$pc.Loader(obj,0)}
_Z(o,"__lod");_44.push([o,_H(o)])}else if(h){_a(o,"__lod");f=_44.filter(function(f){return f[0]==o}
)[0];_d(_44,f);if(f=f[1]){f[0]._=0;clearInterval(f[1]);f[0].style.backgroundImage=""}}
}return h},Player:function(src){if(!_p()){_LLL.open(src)}else{if(_NNN){location="#"+Math.random()}
var w=$("__wa_media");var o=_R("iframe");o.id="__wa_media";o.src=src;_pp.appendChild(o);_S(w)}return false},toString:function(){return "[WebApp.Net Framework]"}}
function _A(h){h=h?h:0;_pp.style.minHeight=(_DDD+h)+"px";_LLL.scrollTo(0,h)}
function _B(s,w,dir,step,mn){s+=Math.max((w-s)/step,mn||4);return[s,(w+w*dir)/2-Math.min(s,w)*dir]}
function _C(o){if(_X(o,"iMore")){var a=$$("a",o)[0];if(a&&a.title){var s=$$("span",a)[0]||a;o=s.innerHTML;s.innerHTML=a.title;a.title=o}}
}
function _D(s,k){if(_nn){var h=_nner;k=(s)?[]:k||[];for(var i=1;i<h.length;i++){if(!_O(i,k)){_h(h[i],s)}}
with($h){if(!_O(BACK,k)){_h(h[BACK],s&&!h[LEFT]&&_22)}if(!_O(HOME,k)){_h(h[HOME],s&&!h[RIGHT]&&!_99&&_22>1)}}
}}
function _E(lay,ignore){if(_nn){var a=$$("a",lay);var p=$h.RIGHT;for(var i=0;i<a.length&&p>=$h.LEFT;i++){if(_nner[p]&&!ignore){i--;p--;continue}if(_W(a[i].rel,"action")||_W(a[i].rel,"back")){_Z(a[i],p==$h.RIGHT?"iRightButton":"iLeftButton");_h(a[i],1);_nner[p--]=a[i];_nn.appendChild(a[i--])}}
}}
function _F(lay){if(_nn){with($h){for(var i=LEFT;i<=RIGHT;i++){var a=_nner[i];if(a&&(_W(a.rel,"action")||_W(a.rel,"back"))){_h(a,0);_a(a,i==RIGHT?"iRightButton":"iLeftButton");lay.insertBefore(a,lay.firstChild)}}
_nner[RIGHT]=$("waRightButton");_nner[LEFT]=$("waLeftButton")}}
}
function _G(o){var u;if(u=getComputedStyle(o,null).backgroundImage){o._=1;return/(.+?(\d+)x(\d+)x)(\d+)(.*)/.exec(u)}}
function _H(o){var d,c,i;if(!(d=_G(o))){c=$$("*",o);for(i=0;i<c.length;i++){o=c[i];if(d=_G(o)){break}}
}return(d)?[o,B_(_I,d[2],[o,d[4],d[3],(d[1]+"*"+d[5]),new Image()])]:d}
function _I(a){if(!a[5]){a[1]=parseInt(a[1])% parseInt(a[2])+1;var b=a[3].replace("*",a[1]);a[4].onload=function(){if(a[0]._)a[0].style.backgroundImage=b;a[5]=0}
a[5]=a[4].src=b.substr(4,b.length-5)}}
function _J(s){return s.replace(/<.+?>/g,"").replace(/^\s+|\s+$/g,"").replace(/\s{2,}/," ")}
function _K(e){e.preventDefault();e.stopPropagation()}
function _L(o){return _W(o.rev,"async")||_W(o.rev,"async:np")}
function _M(o){return _W(o.rev,"media")}
function _N(o){return(typeof o!="undefined")}
function _O(o,a){return a.indexOf(o)!=-1}
function $(i){return typeof i=="string"?document.getElementById(i):i}
function $$(t,o){return(o||document).getElementsByTagName(t)}
function $A(o,a){return o.getAttribute(a)||""}
function XY(e){var x=0;var y=0;while(e){x+=e.offsetLeft;y+=e.offsetTop;e=e.offsetParent}return{x:x,y:y}}
function _P(){with(_LLL)return{x:pageXOffset,y:pageYOffset,w:innerWidth,h:innerHeight}}
function _Q(c){var s,h=$$("head")[0];s=_R("script");s.type="text/javascript";s.textContent=c;h.appendChild(s)}
function _R(t,c){var o=document.createElement(t);if(c){o.innerHTML=c}return o}
function _S(p,c){if(p){if(!c){c=p;p=c.parentNode}
p.removeChild(c)}}
function _T(o){o=$(o);if(o&&_V(o)!="form"){o=_b(o,"form")}return o}
function _U(o){return _V(o)=="a"?o:_b(o,"a")}
function _V(o){return o.localName.toLowerCase()}
function _W(o,t){return o&&_O(t,o.toLowerCase().split(" "))}
function _X(o,c){return o&&_O(c,_Y(o))}
function _Y(o){return o.className.split(" ")}
function _Z(o,c){var h=_X(o,c);if(!h){o.className+=" "+c}return h}
function _a(o){var c=_Y(o);var a=arguments;for(var i=1;i<a.length;i++){_d(c,a[i])}
o.className=c.join(" ")}
function _b(o,t){while((o=o.parentNode)&&(o.nodeType!=1||_V(o)!=t)){}return o}
function _c(o,c){while((o=o.parentNode)&&(o.nodeType!=1||!_X(o,c))){}return o}
function _d(a,e){var p=a.indexOf(e);if(p!=-1){a.splice(p,1)}}
function _e(o){o=o.childNodes;for(var i=0;i<o.length;i++){if(o[i].nodeType==3){return o[i].nodeValue.replace(/^\s+|\s+$/g,"")}}return null}
function _f(){if(!_pp){_pp=$("WebApp")}if(!_qq){_qq=$("iGroup")}
var i=$("iLoader");if(i&&!_X(i,"__lod")){$pc.Loader(i,1)}}
function _g(){_nner=[$("iHeader"),$("waBackButton"),$("waHomeButton"),$("waLeftButton"),$("waRightButton"),$("waHeadTitle")];_ss=document.body;_rr=(_ss.dir=="rtl")?-1:+1;_wkt=_N(_ss.style.webkitTransform)}
function _h(o,s){if(o=$(o)){o.style.display=s?"block":"none"}}
function _i(o,s){if(o=$(o)){o.style[_rr==1?"left":"right"]=s?0:"";o.style.display=s?"block":""}}
function _j(o){if(o=o||_II()){var z=$$("div",o);z=z[z.length-1];if(z&&(_X(z,"iList")||_X(z,"iFull"))){z.style.minHeight=parseInt(_pp.style.minHeight)-XY(z).y+"px"}}
}
function _k(s,p){var o=$("__wa_shadow");o.style.top=p+"px";_pp.style.position=s?"relative":"";_h(o,s)}
function _l(o,l){if(o){_33.splice(++_22,_33.length);_33.push([o,!l?location.hash:("#_"+_ll.substr(2)),_EEE])}}
function _m(o){var s=$$("script",o);while(s.length){_S(s[0])}return o}
function _n(){var s,i,c;while(_44.length){$pc.Loader(_44[0][0],0)}
s=$$("li");for(i=0;i<s.length;i++){_a(s[i],"__sel","__tap")}}
function _o(s,np){var ed=s.indexOf("#_");if(ed==-1){return null}
var rs="";var bs=_KK(s);if(!np){for(var i=0;i<bs[1].length;i++){rs+="/"+bs[1][i].split("=").pop()}}return bs[2]+rs}
function _p(){return(UA("iPhone")||UA("iPod")||UA("Aspen"))}
function UA(s){return _O(s,navigator.userAgent)}
function _q(){if(_88){return}
var m,h,o,w=(_P().w>=_vv)?_vv:_uu;if(w!=_CCC){_CCC=w;_pp.className=(w==_uu)?"portrait":"landscape";_s("orientationchange")}if(o=_II()){h=XY(o).y+o.offsetHeight}
m=_CCC==_uu?416:268;w=_P().h;h=h<w?w:h;h=h<m?m:h;_DDD=h;_pp.style.minHeight=h+"px";_j()}
function _r(){if(_88||_99==location.href){return}
_99=0;var act=_II();if(act){act=act.id}else if(location.hash.length>0){return}else{act=_33[0][0]}
var cur=_33[_22][0];if(act!=cur){var i,pos=-1;for(i in _33){if(_33[i][0]==act){pos=parseInt(i);break}}if(pos!=-1&&pos<_22){_z(cur,act,$d.L2R)}else{_y(act)}}
}
function _s(evt,ctx,obj){var l=_RRR[evt].length;if(l==0){return true}
var e={type:evt,target:obj||null,context:ctx||_EE(_33[_22][1]),windowWidth:_CCC,windowHeight:_DDD}
var k=true;for(var i=0;i<l;i++){k=k&&(_RRR[evt][i](e)==false?false:true)}return k}
function _t(){clearInterval(_JJJ);_f();_g();_ee();_WW();_VV();_bb("__wa_shadow");var i=$("iLoader");$pc.Loader(i,0);_S(i);_S($("iPL"));$pc.Opener(_zz);_uu=screen.width;_vv=screen.height;if(_uu>_vv){var l=_vv;_vv=_uu;_uu=l}
_ll=_FF()[0].id;_l(_ll,1);var a=(_II()||"").id;if(a!=_ll){_l(a)}if(!a){a=_ll;_zz("#")}
_ff(_qq);_i(a,1);_E($(a));with($h){var h=_nner;_h(h[BACK],(!h[LEFT]&&_22));_h(h[HOME],(!h[RIGHT]&&_22>1&&a!=_ll));if(h[BACK]){_BBB=h[BACK].innerHTML}if(h[TITLE]){_AAA=h[TITLE].innerHTML;_9()}}
B_(_r,250);_s("load");_pp.addEventListener("touchstart",new Function(),false);(_PPP?_qq:document).addEventListener(_PPP?"touchmove":"scroll",_ii,false);_q();_hh();_kk("DOMSubtreeModified");_kk("resize");$pc.HideBar()}
function _u(ul,li,h,ev){if(!(_X(li,"__dis")||_W($$("a",li)[0].rel,"action"))){var c,s,al=$$("li",ul);for(var i=0;i<al.length;i++){c=(al[i]==li);if(c){s=i}
_h(ul.id+i,(!h&&c));_a(al[i],"__act")}
_Z(li,"__act");if(ev){_s("tabchange",[s],ul)}}
}
function _v(e){if(_88){return _K(e)}
var o=e.target;var n=_V(o);if(n=="label"){var f=$($A(o,"for"));if(_X(f,"iToggle")){A_(_CC,1,f.previousSibling,1)}return}
var li=_b(o,"li");if(li&&_X(li,"iRadio")){_Z(li,"__sel");_aa(li);_99=location.href;_y("wa__00");return _K(e)}
var a=_U(o);if(a&&li&&_X(li,"__dis")){return _K(e)}if(a&&a.onclick){var old=a.onclick;a.onclick=null;var val=old.call(a,e);A_(function(){a.onclick=old},0);if(val===false){if(li){_Z(li,_X(a,"iSide")?"__tap":"__sel");_w(li)}return _K(e)}}
var ul=_b(o,"ul");var pr=!ul?null:ul.parentNode;var ax=a&&_L(a);if(a&&ul&&_X(pr,"iTab")){var h,t;t=_W(a.rel,"action");h=$(ul.id+"-loader");_h(h,0);if(!t&&ax){_h(h,1);$pc.Loader(h,1);_NN(a,function(o){_h(h,0);$pc.Loader(h,0);_h(_SS(o)[0],1);_u(ul,li,0,1)}
)}else{h=t}
_u(ul,li,!!h,!ax);if(!t){return _K(e)}}if(a&&_O(a.id,["waBackButton","waHomeButton"])){if(a.id=="waBackButton"){$pc.Back()}else{$pc.Home()}return _K(e)}if(ul&&_X(ul,"iCheck")){if(_ZZ(a,ul)!==false){var al=$$("li",ul);for(var i=0;i<al.length;i++){_a(al[i],"__act","__sel")}
_Z(li,"__act __sel");A_(_a,1000,li,"__sel")}return _K(e)}if(ul&&!_X(li,"iMore")&&((_X(ul,"iMenu")||_X(pr,"iMenu"))||(_X(ul,"iList")||_X(pr,"iList")))){if(a&&!_X(a,"iButton")){var c=_Z(li,_X(a,"iSide")?"__tap":"__sel");if(ax){if(!c){_NN(a)}return _K(e)}}
}
var dv=_c(o,"iMore");if(dv){if(!_X(dv,"__lod")){$pc.Loader(dv,1);if(ax){_NN(a)}}return _K(e)}if(a&&_FFF){if(_W(a.rel,"back")){$pc.Form(_FFF,a);return _K(e)}if(_W(a.rel,"action")){var f=_T(_FFF);if(f){f.onsubmit(e);return}}
}if(a&&_M(a)){_w(li);$pc.Player(a.href,a);return _K(e)}if(ax){_NN(a);_K(e)}else if(a&&!a.target){if(_x(a.href,"http:","https:","file:")){_QQ(a.href);_K(e)}
_w(li)}}
function _w(li){if(li){A_(_a,1000,li,"__sel","__tap")}}
function _x(s1){var r,i,a=arguments;for(i=1;i<a.length;i++){if(s1.toLowerCase().indexOf(a[i])==0){return 1}}
}
function _y(to){var h=_33[_22][0];if(h!=to){_z(h,to)}}
function _z(src,dst,dir){if(_88){return}
_88=1;_DD();if(dst==_33[0][0]){_77=history.length}
dir=dir||$d.R2L;src=$(src);dst=$(dst);var h;if(_wkt&&_nn){h=_m(_nn.cloneNode(true))}
_11=_22;if(dir==$d.R2L){_l(dst.id)}else{while(_22&&_33[--_22][0]!=dst.id){}}
_AA();_F(src);_E(dst);_BB();if(h){_nner[$h.HEAD].appendChild(h)}
_0((dir!=$d.R2L)?"":(_99?"":_J(src.title))||_BBB);_9(_99?dst.title:null);_7(src,dst,dir)}
function _0(txt){if(_nner[$h.BACK]){if(!txt&&_22){txt=_J($(_33[_22-1][0]).title)||_BBB}if(txt){_nner[$h.BACK].innerHTML=txt}}
}
function _1(m){var s=_EE(_33[_11][1]);var d=_EE(_33[_22][1]);var r=(m<0&&!!_99)?["wa__00"]:d;return[s,d,m,r]}
function _2(t){return "translate3d("+t+",0,0)"}
function _3(t){return "translateX("+t+")"}
function _4(o,t,i){if(o){if(t){t=_QQQ(t)}
o.style.webkitTransitionProperty=(i)?"none":"";o.style.webkitTransform=t}}
function _5(o){return o?getComputedStyle(o,null).webkitTransitionDuration:"0s"}
function _6(){var r,t,i,j,a=arguments;r=0;for(i=0;i<a.length;i++){t=_5(a[i]).split(',');for(j=0;j<t.length;j++){r=Math.max(r,parseFloat(t[j])*1000)}}return r}
function _7(src,dst,dir){_s("beginslide",_1(dir));_ee(dst);_i(src,1);_i(dst,1);if(!_wkt){_8(src,dst,dir);return}
var b=_qq;var w=_pp;var g=dir*_rr;b.style.height=(_DDD-b.offsetTop)+"px";_Z(w,"__ani");_4(src,"0",1);_4(dst,(g*-100)+"%",1);var h,hcs,hos,tim=_6(src,dst,_nn,_nner[$h.TITLE]);if(_nn){h=_nner[$h.HEAD].lastChild;hcs=h.style;hos=_nn.style;hcs.opacity=1;hos.opacity=0;_4(h,"0",1);_4(_nn,(g*-20)+"%",1);_4(_nner[$h.TITLE],(g==$d.R2L?60:-20)+"%",1)}
A_(function(){_j(dst);_4(dst,"0");_4(src,(g*100)+"%");if(h){hcs.opacity=0;hos.opacity=1;_4(h,(g*30)+"%");_4(_nn,"0");_4(_nner[$h.TITLE],"0")}
A_(function(){if(h){_S(_nner[$h.HEAD],h)}
_a(w,"__ani");b.style.height="";_8(src,dst,dir)},tim)},0)}
function _8(src,dst,dir){_n();_i(src,0);_s("endslide",_1(dir));_88=0;_11=-1;_q();A_(_DD,0,dir==$d.L2R?_33[_22+1][2]:null);A_(_hh,0)}
function _9(title){var o;if(o=_nner[$h.TITLE]){o.innerHTML=title||_HH(_II())||_AAA}}
function _AA(){if(_FFF){$pc.Form(_FFF)}
_h(_mm,0)}
function _BB(){_D(1)}
function _CC(o,dontChange){var c=o,i=$(c.title);var txt=i.title.split("|");if(!dontChange){i.click()}
(i.disabled?_Z:_a)(c,"__dis");o=c.firstChild.nextSibling;with(c.lastChild){innerHTML=txt[i.checked?0:1];if(i.checked){o.style.left="";o.style.right="-1px";_Z(c,"__sel");style.left=0;style.right=""}else{o.style.left="-1px";o.style.right="";_a(c,"__sel");style.left="";style.right=0}}
}
function _DD(to){var h=to?to:Math.min(50,_P().y);var s=to?Math.max(1,to-50):1;var d=to?-1:+1;while(s<=h){var z=_B(s,h,d,6,2);s=z[0];_LLL.scrollTo(0,z[1])}if(!to){$pc.HideBar()}}
function _EE(loc){if(loc){var p=loc.indexOf("#_");if(p!=-1){loc=loc.substring(p+2).split("/");var id="wa"+loc[0];for(var i in loc){loc[i]=decodeURIComponent(loc[i])}
loc[0]=id;if(_SSS&&!$(id)){_GG(id)}return $(id)?loc:[]}}return[]}
function _FF(){var lay=[];var src=_qq.childNodes;for(var i in src){if(src[i].nodeType==1&&_X(src[i],"iLayer")){lay.push(src[i])}}return lay}
function _GG(i){var n=_R("div");n.id=i;n.className="iLayer";_qq.appendChild(n);return n}
function _HH(o){return(!_22&&_AAA)?_AAA:o.title}
function _II(){var h=location.hash;return $(!h?_ll:_EE(h)[0])}
function _JJ(url){var d=url.match(/[a-z]+:\/\/(.+:.*@)?([a-z0-9-\.]+)((:\d+)?\/.*)?/i);return(!_HHH||!d||d[2]==location.hostname)?url:_LL(_HHH,"__url",url)}
function _KK(u){var s,q,d;s=u.replace(/&amp;/g,"&");d=s.indexOf("#");d=s.substr(d!=-1?d:s.length);s=s.substr(0,s.length-d.length);q=s.indexOf("?");q=s.substr(q!=-1?q:s.length);s=s.substr(0,s.length-q.length);q=!q?[]:q.substr(1).split("&");return[s,q,d]}
function _LL(u,k,v){u=_KK(u);var q=u[1].filter(function(o){return o&&o.indexOf(k+"=")!=0}
);q.push(k+"="+encodeURIComponent(v));return u[0]+"?"+q.join("&")+u[2]}
function _MM(u,q){u=_KK(u);u[1].push(q);return u[0]+"?"+u[1].join("&")+u[2]}
function _NN(item,cb,q){var h,o,u,i;i=(typeof item=="object");u=(i?item.href:item);o=_b(item,"li");if(!cb){cb=_OO(u,_W(item.rev,"async:np"))}
$pc.Request(u,q,cb,true,o,(i?item:null))}
function _OO(i,np){return function(o){var u=i?_o(i,np):null;var g=_SS(o);if(g&&(g[1]||u)){_QQ(g[1]||u)}else{_n()}return null}}
function _PP(o){var nds=o.childNodes;var txt="";for(var y=0;y<nds.length;y++){txt+=nds[y].nodeValue}return txt}
function _QQ(l){_EEE=_P().y;_DD();_zz(l)}
function Go(g){return "#_"+g.substr(2)}
function _RR(i){if(i.substr(0,2)=="wa"){var p=_22;if(p&&i==_33[0][0]){_33[1][2]=0}
while(p&&_33[--p][0]!=i){}if(p){_33[p+1][2]=0}}
}
function _SS(o){if(o.responseXML){o=o.responseXML.documentElement;var s,t,k,a=_II();var g=$$("go",o);g=(g.length!=1)?null:$A(g[0],"to");var f,p=$$("part",o);if(p.length==0){p=[o]}
for(var z=0;z<p.length;z++){var dst=$$("destination",p[z])[0];if(!dst){break}
var mod=$A(dst,"mode");var txt=_PP($$("data",p[z])[0]);var i=$A(dst,"zone");if(($A(dst,"create")=="true"||_SSS)&&i.substr(0,2)=="wa"&&!$(i)){_GG(i)}
f=f||i;g=g||$A(dst,"go");i=$(i||dst.firstChild.nodeValue);if(!k&&a&&a.id==i.id){_AA();_F(i);k=i}
_RR(i.id);_TT(i,txt,mod)}
t=$$("title",o);for(var n=0;n<t.length;n++){var s=$($A(t[n],"set"));s.title=_PP(t[n]);if(a==s){_9()}}if(k){_E(k);_BB()}
var e=$$("script",o)[0];if(e){_Q(_PP(e))}
_ee(a);_0();if(g==a){g=null}if(!g){_hh()}return[f,g?Go(g):null]}
throw "Invalid asynchronous response received."}
function _TT(o,c,m){c=_R("div",c);c=c.cloneNode(true);_ff(c);if(m=="replace"||m=="append"){if(m!="append"){o.innerHTML=""}
while(c.hasChildNodes()){o.appendChild(c.firstChild)}}else{var p=o.parentNode;var w=(m=="before")?o:o.nextSibling;if(m=="self"){_S(p,o)}
while(c.hasChildNodes()){p.insertBefore(c.firstChild,w)}}
}
function _UU(o,cb,lr){if(o.readyState!=4){return}
var er,ld,ob;if(ob=_66.filter(function(a){return o==a[0]}
)[0]){_s("endasync",ob.pop(),ob[0]);_d(_66,ob)}
er=(o.status!=200&&o.status!=0);if(!er){if(!o.responseXML)o.responseJSON=_jj(o.responseText);try{if(cb){ld=cb(o,lr,_OO())}}
catch(ex){er=ex;console.error(er)}}if(lr){$pc.Loader(lr,0);if(er){_a(lr,"__sel","__tap")}}
}
function _VV(){var hd=_nner[$h.HEAD];if(hd){var dv=_R("div");dv.style.opacity=1;while(hd.hasChildNodes()){dv.appendChild(hd.firstChild)}
hd.appendChild(dv);_nn=dv;_h(dv,1);_h(_nner[$h.TITLE],1)}}
function _WW(){var o=$$("ul");for(var i=0;i<o.length;i++){var p=o[i].parentNode;if(p&&_X(p,"iTab")){_h(o[i].id+"-loader",0);_u(o[i],$$("li",o[i])[0])}}
}
function _XX(r,p){for(var j=0;j<r.length;j++){with(r[j]){if(type=="radio"&&(checked||getAttribute("checked"))){checked=true;p=$$("span",p||_b(r[j],"li"))[0];p.innerHTML=_e(parentNode);break}}
}}
function _YY(p){var o=$$("li",p);for(var i=0;i<o.length;i++){if(_X(o[i],"iRadio")&&!_X(o[i],"__done")){var lnk=_R("a");var sel=_R("span");var inp=$$("input",o[i]);lnk.appendChild(sel);while(o[i].hasChildNodes()){lnk.appendChild(o[i].firstChild)}
o[i].appendChild(lnk);lnk.href="#";_Z(o[i],"__done");_XX(inp,o[i])}}
var s="wa__00";if(!$(s)){_GG(s)}}
function _ZZ(a,u){var p=_00;var x=$$("input",p);var y=$$("a",u);for(var i=0;i<y.length;i++){if(y[i]==a){if(x[i].disabled){return false}
var c=x[i].onclick;if(c&&c()===false){return false}
x[i].checked=true;_XX([x[i]]);if($A(p,"value")=="autoback"){A_($pc.Back,0)}
break}}
}
function _aa(p){var o=$$("input",p);var dv=_R("div");var ul=_R("ul");ul.className="iCheck";_00=p;for(var i=0;i<o.length;i++){if(o[i].type=="radio"){var li=_R("li");var a=_R("a",o[i].nextSibling.nodeValue);a.href="#";li.appendChild(a);ul.appendChild(li);if(o[i].checked){_Z(li,"__act")}if(o[i].disabled){_Z(li,"__dis")}}
}
dv.className="iMenu";dv.appendChild(ul);o=$("wa__00");if(o.firstChild){_S(o,o.firstChild)}
o.title=_e(p.firstChild);o.appendChild(dv)}
function _bb(i){var o=_R("div");o.id=i;_pp.appendChild(o);return o}
function _cc(p){var o=$$("input",p);for(var i=0;i<o.length;i++){if(o[i].type=="checkbox"&&_X(o[i],"iToggle")&&!_X(o[i],"__done")){o[i].id=o[i].id||"__"+Math.random();o[i].title=o[i].title||"ON|OFF";var txt=o[i].title.split("|");var b1=_R("b","&nbsp;");var b2=_R("b");var i1=_R("i",txt[1]);b1.className="iToggle";b1.title=o[i].id;b1.appendChild(b2);b1.appendChild(i1);o[i].parentNode.insertBefore(b1,o[i]);b1.onclick=function(){_CC(this)}
_CC(b1,1);_Z(o[i],"__done")}}
}
function _dd(o){var x11,x12,y11,y12;var x21,x22,y21,y22;var p=XY(o);x11=p.x;y11=p.y;x12=x11+o.offsetWidth-1;y12=y11+o.offsetHeight-1;p=_P();x21=p.x;y21=p.y;x22=x21+p.w-1;y22=y21+p.h-1;return!(x11>x22||x12<x21||y11>y22||y12<y21)}
function _ee(l){l=$(l)||_II();_cc(l);_YY(l)}
function _ff(c){if(_III){var p,tmp=$$("img",c);for(var i=0;i<tmp.length;i++){if((p=_b(tmp[i],"a"))&&(_W(p.rel,"action")||_W(p.rel,"back"))){continue}
tmp[i].setAttribute("load",tmp[i].src);tmp[i].src=_MMM}}
}
function _gg(){if(_yy-_P().y==0){_ww=clearInterval(_ww);_hh()}}
function _hh(){if(_III){var img=$$("img",_II());for(var i=0;i<img.length;i++){var o=$A(img[i],"load");if(o&&_dd(img[i])){img[i].src=o;img[i].removeAttribute("load")}}
}}
function _ii(){_GGG=1;if(_III&&!_88){if(!_xx){_xx=true;A_(function(){_yy=_P().y;_xx=false},500)}if(!_ww){_ww=B_(_gg,1000)}}
}
function _jj(s){if (/^[\s\t\n\r{}\[\]:,eE\-+falr-un.0-9]*$/.test(s.replace(/\\./g, '').replace(/"[^"\\\n\r]*"/g, ''))){try{return eval('('+s+')')}
catch(e){}}return null}
function _kk(s){addEventListener(s,_q,false)}
addEventListener("load",_t,true);addEventListener("click",_v,true);return $pc}
)();var WA=WebApp;