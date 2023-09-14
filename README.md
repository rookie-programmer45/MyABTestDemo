# MyABTestDemo
一个实现了AB测试功能的Demo，主要使用方法如下：
1、用@RoutingInjected给使用者注入要实现AB测试功能的接口的代理类；
2、用@RoutingVersion给AB测试接口的实现类标注版本；
3、用@VersionSwitch标注当前要测试的版本，未注解或未指定版本则会随机选取一个版本；

具体样例在DemoController
