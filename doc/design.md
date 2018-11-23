## OP

## 命令约定

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