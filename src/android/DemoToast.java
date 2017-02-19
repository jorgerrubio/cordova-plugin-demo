package cordova.plugin.demotoast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.widget.Toast;

public class DemoToast extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("show")) {
            show(args.getString(0), callbackContext);
            return true;
        }
        return false;
    }

    private void show(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            Toast.makeText(webView.getContext(), message, Toast.LENGTH_LONG).show();
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
