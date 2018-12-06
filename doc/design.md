## OP Design



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
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(20) DEFAULT '最新面试题' COMMENT '标题',
  `body` text NOT NULL COMMENT '你的问题',
  `answer` text COMMENT '答案',
  `submitter` varchar(20) DEFAULT '小明' COMMENT '问题提交人',
  `modifier` varchar(20) DEFAULT '我不知道' COMMENT '交提答案的人',
  `lastmodify` datetime DEFAULT NULL,
  `language` varchar(10) DEFAULT 'common',
  `category` varchar(10) DEFAULT NULL COMMENT '分类，如算法，数据结构，数据库',
  `company` varchar(20) DEFAULT NULL COMMENT '你懂的',
  `rate` int(1) DEFAULT '1' COMMENT '评分',
  `imgpath` varchar(256) DEFAULT NULL COMMENT '图片的路径，而不是图片自身',
  `heat` int(1) DEFAULT '1' COMMENT '最近被问到的次数',
  `syncflag` int(1) DEFAULT '0' COMMENT '同步标记1',
  `blame` int(1) DEFAULT '0' COMMENT '举报',
  `duplicate` int(1) DEFAULT '0' COMMENT '是否是重复的问题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;



### usecase 022
usecase 022 `应用程序`可以根据`当前复习题清单`制定默认的复习计划
usecase 021 `用户`可以根据`当前复习题清单`制定复习计划
usecase 020 用户可以查看`当前复习题清单`

usecase 019 用户可以查看`当前问题`的详细信息
usecase 018 用户可以标记跳过`当前问题`
usecase 017 用户可以标记将当前问题作为复习题
usecase 004 用户可以提交问题，或修改答案。 
usecase 005 `普通用户`可以举报问题违反法律法规，或者重复，但是不能删除问题
usecase 006 `普通用户`可以修改问题答案，应用程序显示最后修改人名称，时间。 相信答案不会被恶意篡改，如wiki


usecase 016 用户可以设置自动更新`数据库`
usecase 015 用户可以手动更新数据库
usecase 014 服务器可以通知用户，数据库有更新
usecase 013 用户可以查询当前数据库有没有更新

usecase 001 DBUtil ， 访问MySQL 数据库。 用PrepareStement 提供增删改操作， 查操作。 【老罗视频 jdbc 编程】
usecase 002 服务器端， 网页 用 post 方法 提交表单到数据库
usecase 003 手机端，  访问 web server 的 提交表单到数据库

usecase 007 本地离线数据库和网络数据同步
usecase 008 数据库怎样让git 管理每个人手一份。

usecase 009 主页分查询和提交问题两个按钮，分别跳转。用户成功提交了问题后，应该转到查询结果的网页，显示最新问题的标题。

usecase 010 问题怎样查询有重复的？

usecase 011 数据库怎样让git 管理每个人手一份。



==========================================

## TODO
TODO 项目计划

TODO 功能分解

TODO 数据库设计 uml

TODO 一张大表，拆分成多张小表 

TODO 如果客户端只有一张表，新增记录，id 怎么更新？，当同步时，id 有冲突怎么解决？

TODO 数据记录操作一次 version 增加1, 避免用 datetime 判断服务器和客户端数据库。 或者单独一张表，表明数据库版本

TODO SQLite 数据类型 和 MySQL的交集。 是否支持 datetime， int 范围，text 还是 varchar 1024？

直接在where里边小于就行了，where time < "2013-08-10 15:46:35"，如果大于的直接 time>"datetime" 

==========================================

## usecase 用例
## usecase 更新数据库

### usecase 服务器端推送更新
+ 服务器端，当增，删，改了数据库。 通过udp，向当前在线的客户端推送数据。

+ 客户端如果打开了接收推送标志，启动后台 service， 必须先同步数据库，避免 `id` 冲突
通过 `id` 找到对应的记录，根据 `action` ，做出相应的动作。


### usecase 服务器数据库-> 同步到 本地数据库

客户端本地新增数据 id 初始值为 一个大数 如 9000000， 避免和服务器冲突

| id  | lastmodify |  syncflag（0-服务器新增、修改共用，1-reserve， 2-服务器删除， 3-本地新增，4-本地修改，5-本地删除 ）|
| --- | -----------| --- |


+ 客户端开机时，或点击同步数据库 按钮， 查询服务器，判断是否需要同步。
    服务器端，提供每张表的，当前记录条数，最后修改时间。
    如果和本地表的信息都相等，则无需同步。
    否则说明，服务器数据库需要同步到本地。

+ 客户端找出服务器需要同步到本地的记录。
    客户端通过 本地最后一条已同步记录的时间戳，获得所有服务器更新的记录。

+ 本地数据库增加/修改
    按照id 查找， 
      本地没有，则直接插入。

      本地有，判断syncflag 是否为 本地改动，
            如果是本地改动，则将本地记录 id 修改为 本地最大 id + 1 ， 同时插入服务器数据
            如果不是，修改之。

+ 本地数据库删除
    按照id 查找
    本地没有，返回。
    本地有，判断syncflag 是否为 本地改动
            如果是本地改动，则将本地记录 id 修改为 本地最大 id + 1 
            如果不是，删除。


### usecase 客户端 ---> 更新服务器数据库
+ 客户端， 不能删除记录

+ 客户端新增数据时， id 从一个大数开始， 避免和服务器冲突。 sync flag = 本地新增。

+ 客户端修改数据时， id，不变，sync flag = 本地修改


+ 客户端找出要自己修改过的数据
    通过sync flag 找到。


+ 服务器端增加数据
    服务器忽略客户端提交记录的 id 值，按照当前timestamp， 增加一条记录，
    返回给客户端 新的 id值 和 lastmodify。 客户端更新对应记录的id 和 sync flag 和 lastmodify

+ 服务器端修改数据
    根据 id 值查找，按照当前timestamp 修改，返回 id 和 lastmodify




---

### done

usecase 012 数据库加 title 字段





## plan
2018-12-06
Android op.db, CRUD, 和 UT
要求，建表项和MySQL 无缝对接。

Q SQLite 的数据类型，到底有没有 datetime？ 如果有 datetime 怎样加上 毫秒 避免，以后时间戳重复。
Q 单元测试怎样做？
Q body 和 answer 到底用 vchar 还是 text


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




