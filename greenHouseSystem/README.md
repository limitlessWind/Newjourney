**基于串口的温室监测系统**

入口：serial->Client

主要功能：
1. 串口交互。[通过RXTX](http://rxtx.qbang.org/wiki/index.php/Main_Page)与串口交互。
2. 动态图像生成。将接受到的数据解析后，使用jfreechart以动态图像的方式展现。
3. 保存为本地XML文档。使用Jdom将解析后的数据保存为本地XML文档。

特色：支持自定义发送命令格式与解析方式，收发灵活，可用于通用串口数据的检测。

![image](https://github.com/limitlessWind/greenHouseSystem/blob/master/greenHouseSystem/img/editOrder.png)
![image](https://github.com/limitlessWind/greenHouseSystem/blob/master/greenHouseSystem/img/graph.png)
![image](https://github.com/limitlessWind/greenHouseSystem/blob/master/greenHouseSystem/img/xmlResult.png)


软件比较原始，欢迎大家指正。
邮箱：lazy-wind@foxmail.com
