# Crear un plugin cordova y su uso en ionic 2

### crear un plugin cordova

```sh
$ npm install -g plugman
$ plugman create --name DemoToast --plugin_id cordova.plugin.DemoToast  --plugin_version 0.0.1
```

el comando crea una carpeta 'DemoToast'

```sh
$ cd DemoToast
$ plugman platform add --platform_name [android|ios]
```

el comando crea una carpeta 'src/android' o 'src/ios' y añade la configuracion al archivo 'plugin.xml'

## en este ejemplo vamos hacer uso del **Toast**

en el archivo `www/DemoToast.js`

```javascript
var exec = require('cordova/exec');

exports.show = function(arg0, success, error) {
    exec(success, error, "DemoToast", "show", [arg0]);
};
```
en el archivo `src/android/DemoToast.java`
```java
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
```

## crear un proyecto [ionic](http://ionicframework.com/docs/v2/intro/installation/ "Documentacion ionic") en otra carpeta

### **Para crear proyectos ionic 2**, tendrá que instalar la última versión de ionic CLI y Córdoba. Antes de hacer esto, necesitará una versión reciente de Node.js. [Descargar el instalador](https://nodejs.org/ "Node.js") para Node.js 6 o superior y luego proceder a instalar el ionic CLI y Córdoba para el desarrollo de aplicaciones nativas:

```sh
$ cd ../
$ npm install -g ionic cordova
$ ionic start demotoast --v2
$ cd demotoast
```
### Instalar el plugin que hemos creado en ionic 2
#### se hace como cualquier otro plugin de cordova
```sh
$ ionic platform add android
$ ionic plugin add ..\DemoToast\ --save
```
### Ejemplo de uso del plugin creado en ionic 2
#### se puede usuar en cualquier sitio de la siguiente manera

en `src/pages/home/home.html` añadimos dentro del `<ion-content></ion-content>`
```html
<ion-list>
    <ion-item>
        <ion-label floating>Message</ion-label>
        <ion-input name="message" [(ngModel)]="message" type="text"></ion-input>
    </ion-item>
</ion-list>
<div padding>
    <button ion-button block (click)="showMessage()">Toast Message</button>
</div>
```

en `src/pages/home/home.ts` añadimos
```ts 
import { NavController } from 'ionic-angular';
declare var cordova: any;
```
dentro de la clase HomePage
```ts
private message: string = '';

showMessage() {
    let msg = (this.message == '') ? 'Escriba un mensaje' : this.message;

    cordova.plugins.DemoToast.show(msg, (res) => console.log(res), (err) => console.log(err));
}
```
### Ya solo queda probarlo
#### con un emulador o dispositivo conectado por usb
```sh
$ ionic run android
```

