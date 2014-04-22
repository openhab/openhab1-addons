$.extend({
  getUrlVars: function(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
      hash = hashes[i].split('=');
      vars.push(hash[0]);
      vars[hash[0]] = hash[1];
    }
    return vars;
  },
  getUrlVar: function(name){
    return $.getUrlVars()[name];
  }
});

$(function() {
    var oAuthVerifier = $.getUrlVar('oauth_verifier');
    var userId = $.getUrlVar('userid');
    
    if(oAuthVerifier && userId) {
        onAuthenticationSuccessful(oAuthVerifier, userId);
    } else {
        onAuthenticationFailed();
    }
});

function onAuthenticationSuccessful(oAuthVerifier, userId) {
    $('#error-text').hide();

    $('#oauth-verifier').val(oAuthVerifier);
    $('#user-id').val(userId);

    $('#console-command').val('withings:finishAuthentication "'+oAuthVerifier+'" "'+userId+'"');
    $('#console-command').select();
}

function onAuthenticationFailed() {
    $('#success-text').hide();
    $('form').hide();
}