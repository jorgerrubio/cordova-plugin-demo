/********* DemoToast.h Cordova Plugin Implementation *******/
#import <Cordova/CDV.h>

@interface DemoToast : CDVPlugin

- (void)show:(CDVInvokedUrlCommand*)command;

@end