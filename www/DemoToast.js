var exec = require('cordova/exec');

exports.show = function(arg0, success, error) {
    exec(success, error, "DemoToast", "show", [arg0]);
};