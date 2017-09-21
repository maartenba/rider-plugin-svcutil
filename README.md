# Windows Communication Foundation (WCF) tools

Rider plugin that allows generating Windows Communication Foundation (WCF) service clients by invoking <code>svcutil.exe</code>.

Requires [`svcutil.exe`](https://docs.microsoft.com/en-us/dotnet/framework/wcf/servicemodel-metadata-utility-tool-svcutil-exe) on the developer machine.

[&raquo; See end-to-end demo](https://raw.githubusercontent.com/maartenba/rider-plugin-svcutil/master/docs/plugin-demo.gif)

## Features

* Specify path to `svcutil.exe` from Rider settings
* Invoke `svcutil.exe` using a WSDL URL (try [this one](http://www.dneonline.com/calculator.asmx?WSDL)), namespace (defaults to project namespace) and a filename.
* Writes `svcutil.exe` errors to the event log.

## Links

* [Plugin page](https://plugins.jetbrains.com/plugin/10051-windows-communication-foundation-wcf-tools)
* [GitHub](https://github.com/maartenba/rider-plugin-svcutil)
* [Issues](https://github.com/maartenba/rider-plugin-svcutil/issues)

## Screenshots

![Generate client](https://raw.githubusercontent.com/maartenba/rider-plugin-svcutil/master/docs/generate-client.png)
![Settings](https://raw.githubusercontent.com/maartenba/rider-plugin-svcutil/master/docs/settings.png)
![Display errors](https://raw.githubusercontent.com/maartenba/rider-plugin-svcutil/master/docs/error-display.png)
