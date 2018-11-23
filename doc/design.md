## OP

## Eclipse shortcut
Ctrl + D                delete line
Alt+Shift+Up            duplicate line
Alt + Up/Down           move line

Alt + /                 auto complete

Ctrl+Shift+F            格式化代码

自动补全
windows/preference/java/editor/code assistant 改为

.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ

## 命令约定

OP              One Piece
JSStudio        Jiu Yan(nine eyes)

---
### 通用
包名                      org.jystudio.XXX

### Client

layout id 命名                缩写+Camel   `ListView @id+/lvAppList`
activity  控件命名            和 layout id 一致 `ListView lvAppList=findViewById(R.id.lvAppList) `

资源文件   命名               activity名称+逻辑名称 如 `main_back.png`
                            common+逻辑名称      如 `common_up_arraow.png`

string    命名                activity 名称_逻辑名称 如  `<string name=main_downloading>downloading</string>`

变量       命名                bool 用 is， has 开头

函数       命名                动宾短语 findViewById

缩写       命名                作为类名开头 全大写， 如 IP ， `IPAddressName`
                              变量名开头全小写               `String ipAddress`
                              函数中 首字母大写              `findUserByIp`

### web
每个包分三层 service, action 和 dao 如注册 register
org.jystudio.register.service
org.jystudio.register.action
org.jystudio.register.dao

CREATE TABLE `question` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `submitter` varchar(20) DEFAULT '小明' COMMENT '问题提交人',
  `knower` varchar(20) DEFAULT '我不知道' COMMENT '交提答案的人',
  `datetime` datetime DEFAULT NULL,
  `language` varchar(10) DEFAULT 'java',
  `sort` varchar(10) DEFAULT NULL COMMENT '分类，如算法，数据结构，数据库',
  `company` varchar(20) DEFAULT NULL COMMENT '你懂的',
  `rate` int(1) DEFAULT '1' COMMENT '评分',
  `blame` char(1) DEFAULT '0' COMMENT '举报',
  `body` varchar(1000) NOT NULL COMMENT '你的问题',
  `answer` varchar(1000) DEFAULT NULL COMMENT '答案',
  `img` blob,
  `heat` int(1) unsigned zerofill DEFAULT '1' COMMENT '最近被问到的次数',
  `syncflag1` int(1) DEFAULT '0' COMMENT '同步标记1',
  `syncflag2` int(1) DEFAULT '0' COMMENT '同步标记2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;



### usecase
usecase 001 DBUtil ， 访问MySQL 数据库。 用PrepareStement 提供增删改操作， 查操作。 【老罗视频 jdbc 编程】
usecase 002 服务器端， 网页 用 post 方法 提交表单到数据库
usecase 003 手机端，  访问 web server 的 提交表单到数据库
usecase 004 可以提交问题，或提交答案。 
usecase 005 问题可以被举报，不能被删除
usecase 006 答案可以被修改，显示修改人名称。 相信答案不会被恶意篡改，如wiki

usecase 005 本地数据库和网络数据同步
 



## plan

2018-11-23
服务器端， eclipse 项目上 github
见数据库
usecase 001， 002， 003



```
submit.jsp
<!-- my implementation -->

<div align="center" style="margin-top: 50px;">
 
        <form action="theurl">
            Please enter your Username:  <input type="text" name="username" size="20px"> <br>
            Please enter your Password:  <input type="text" name="password" size="20px"> <br><br>
        <input type="submit" value="submit">
        </form>
 
</div>
```


### Q&A
### 无法识别 javax.servlet annotation
```
在eclipse里面 Project ->Properties，在左边找到Java Builder Path，单击，在右边找到Libraries，然后Add External Jars，找到“\Tomcat 7.0\lib\servlet-api.jar”。

以前创建的一个项目，打开的时候总是报错。

import javax.servlet.annotation.WebServlet;  

后来想起当时这个项目是发布在tomcat7.0下面的， 也就是说当时这个项目buildpath下"add library->Server runtime是tomcat7.0，而现在我给这个项目添加的server runtime是tomcat 8.0，所以会出现这样的错误。当我改了server runtime为tomcat 7.0以后就没有报错了。
```


