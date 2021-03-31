setting up runner in idea:
Test kind: Suite
Suite: C:/Users/abmet/Desktop/AppiumCucumberTestProject/src/test/resources/testng.xml
VM options: -ea -Dtestng.dtd.http=true


run with maven:
mvn test -Dapp_url=bs://662ea709aaa1b94e31b7e7c704a941aa9bd5ecfa -Dapp_args=com.tradeatf.global,GlobalTradeATF
mvn test -Dapp_url=bs://b7fdbf6071143c112f08b4b9711d57dacd2009b1 -Dapp_args=com.roinvesting,ROInvesting
mvn test -Dapp_url=bs://213dba86664108aa0fc41d8fd9de4531cc5991cc -Dapp_args=com.101investing,101investing
mvn test -Dapp_url=bs://bcc769b96c7b359bdda0be096f08dce26d42c31a -Dapp_args=com.etfinance,ETFinance
mvn test -Dapp_url=bs://14bc47124fa5751fa9758cca0266f5e925f84f28 -Dapp_args=au.com.hftrading,HFTrading.com
mvn test -Dapp_url=bs://47567cef121fc64976d1d4f5c513c29e50260551 -Dapp_args=com.gcb.T1,T1
mvn test -Dapp_url=bs://b47756c6343943649dd1f3cf62dc5cf27ea626d8 -Dapp_args=com.tradedwell,TradedWell

npm install -g appium@1.18.3
npm uninstall appium -g
appium -v

run appium:
appium

downgrade appium:
npm i -g npm@latest
npm install -g appium@1.18.3

npm i -g npm@6
npm i -g appium

source ~/.bash_profile - (copy all system vars and paths to .zprofile)

1) Right click on Appium desktop > Contents /Resources/app/node_modules/Appium/node_modules/appium-xcuitest-driver/WebDriverAgent
2) (for desktop appium) cd /usr/local/lib/node_modules/appium/node_modules/appium-webdriveragent  or (for command line appium)
cd /Resources/app/node_modules/Appium/node_modules/appium-xcuitest-driver/WebDriverAgent
Now, run below two commands:
2) mkdir -p Resources/WebDriverAgent.bundle
3) ./Scripts/bootstrap.sh -d
4) XCODE: Signing & Capabilities --> All(Debug + Release) --> set Team
5) IntegrationApp(build on device)
6) WebDriverAgentRunner(build on device)

Appium desktop: /Applications/Appium.app/Contents/Resources/app/node_modules/appium/node_modules/appium-webdriveragent --> then open WebDriverAgent.xcodeproj by xcode and build on device WebDriverAgentRunner
Appium: /usr/local/lib/node_modules/appium/node_modules/appium-webdriveragent

[WebDriverAgent] Got derived data root: '/Users/mac05/Library/Developer/Xcode/DerivedData/WebDriverAgent-ciegwgvxzxdrqthilmrmczmqvrgu'

URL for downloading the latest version of WebDriverAgent: https://github.com/facebookarchive/WebDriverAgent --> then open WebDriverAgent.xcodeproj by xcode and build on device WebDriverAgentRunner

resources:
https://developer.aliyun.com/mirror/npm/package/appium-mac2-driver
https://developer.aliyun.com/mirror/npm/package/appium-mac2-driver#element-attributes
https://developer.apple.com/documentation/xctest/xcuielementtype/xcuielementtypestatictext?language=objc
https://academy.realm.io/posts/nspredicate-cheatsheet/?spm=a2c6h.14275010.0.0.7ae43cc5kyoziw