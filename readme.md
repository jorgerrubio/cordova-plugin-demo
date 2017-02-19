# Crear un plugin cordova y su uso en ionic 2

### crear un plugin cordova

```sh
$ npm install -g plugman
$ plugman create --name DemoToast --plugin_id cordova.plugin.DemoToast  --plugin_version 0.0.1
```

```
DemoToast/
    |-src/
    |-www/
        |-DemoToast.js
```

## en este ejemplo vamos hacer uso del **Toast**

#### en el archivo `www/DemoToast.js`

```javascript
var exec = require('cordova/exec');

exports.show = function(arg0, success, error) {
    exec(success, error, "DemoToast", "show", [arg0]);
};
```
#### añadimos la plataforma android
```sh
$ cd DemoToast
$ plugman platform add --platform_name android
```

#### el comando `plugman platform add --platform_name [android|ios]` crea una carpeta 'src/android' o 'src/ios' y añade la configuracion al archivo 'plugin.xml'
```
DemoToast/
    |-src/
        |-android/
            |-DemoToast.java
    |-www/
        |-DemoToast.js
```
#### en el archivo `src/android/DemoToast.java`
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
#### añadimos la plataforma ios
```sh
$ cd DemoToast
$ plugman platform add --platform_name ios
```

```
DemoToast/
    |-src/
        |-android/
            |-DemoToast.java
        |-ios/
            |-DemoToast.m
    |-www/
        |-DemoToast.js
```

### En la carpeta `src/ios` creamos el archivo `DemoToast.h` y pegamos
```c
#import <Cordova/CDV.h>

@interface DemoToast : CDVPlugin

- (void)show:(CDVInvokedUrlCommand*)command;

@end
```
### En el archivo `src/ios/DemoToast.m`
```cpp
#import "DemoToast.h"
#import <Cordova/CDV.h>

@implementation DemoToast

- (void)show:(CDVInvokedUrlCommand*)command
{
  CDVPluginResult* pluginResult = nil;
  NSString* msg = [command.arguments objectAtIndex:0];

  if (msg == nil || [msg length] == 0) {
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
  } else {
    UIAlertView *toast = [
      [UIAlertView alloc] initWithTitle:@""
        message:msg
        delegate:nil
        cancelButtonTitle:nil
        otherButtonTitles:nil, nil];

    [toast show];
        
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 3 * NSEC_PER_SEC), 
    dispatch_get_main_queue(), ^{
      [toast dismissWithClickedButtonIndex:0 animated:YES];
    });
        
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK 
    messageAsString:msg];
  }

  [self.commandDelegate sendPluginResult:pluginResult 
  callbackId:command.callbackId];
}

@end
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
$ ionic platform add ios
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
$ ionic emulate ios
```

