# RocketMq的安装
官方网站: http://rocketmq.apache.org
GitHub: https://github.com/apache/rocketmq
安装前准备: 
  1.建议使用64位操作系统，建议使用Linux / Unix / Mac；
  2.64位JDK 1.8+;
  3.Maven 3.2.x;
  4.Git;
  5.适用于Broker服务器的4g +可用磁盘

## 1.在Linux上安装Maven
下载Maven
```
wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
```
在maven配置文件中添加阿里云镜像
修改maven/conf目录下的settings.xml
在mirrors节点下添加
```
<mirror> 
    <id>aliyun-maven</id> 
    <mirrorOf>*</mirrorOf> 
    <name>aliyun maven</name> 
    <url>http://maven.aliyun.com/nexus/content/groups/public</url> 
</mirror>
```
配置maven环境变量
修改/etc/profile
```
export M2_HOME=/usr/local/maven
export PATH=$PATH:$M2_HOME/bin
```

安装JDK，找到JDK的安装目录,配置java环境变量

```
export JAVA_HOME="/usr/java/jdk1.8.0_181-amd64"
export CLASS_PATH="$JAVA_HOME/lib"
export PATH=".$PATH:$JAVA_HOME/bin
```

环境变量修完执行`source /etc/profile`立即生效


## 2.从GitHub上下载源码并上传到服务器编译安装
```
unzip rocketmq-master.zip –d /opt/module
cd rocketmq-master/
```
进入rocketmq主目录编译项目
```
mvn -Prelease-all -DskipTests clean install –U
```

编译后在target目录下有发布版的包，rocketmq-4.6.1
```
cd /opt/module/rocketmq-master/distribution/target/
```


可以将里面的包挪到安装位置


进入bin目录下可执行各种操作

## 3.启动nameserver
默认的server的jvm参数很大，需要的内存大，修改为合适的配置
```
vi runserver.sh   
  JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=100m"
```

bin目录下执行./mqnamesrv
```
cd /opt/module/rocketmq-master/distribution/target/rocketmq-4.6.1/rocketmq-4.6.1/bin
./mqnamesrv
```

启动成功，显示
```
Java HotSpot(TM) 64-Bit Server VM warning: Using the DefNew young collector with the CMS collector is deprecated and will likely be removed in a future release
Java HotSpot(TM) 64-Bit Server VM warning: UseCMSCompactAtFullCollection is deprecated and will likely be removed in a future release.
The Name Server boot success. serializeType=JSON
```

## 4.启动Broker
默认的broker的jvm参数很大，需要的内存大，修改为合适的配置
```
vi runbroker.sh
   JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m"
```

bin目录下执行./mqbroker
```
./mqbroker -n localhost:9876
```

启动成功时显示
```
The broker[broker-a, 192.168.117.110:10911] boot success. serializeType=JSON and name server is localhost:9876
```

## 5.测试消息发送
使用tool.sh脚本执行测试程序
在bin目录下执行
```
./tools.sh org.apache.rocketmq.example.quickstart.Producer
```

## 6.测试接受消息
```
./tools.sh org.apache.rocketmq.example.quickstart.Consumer
```

## 7.控制台rocketmq-console编译安装下载
https://github.com/apache/rocketmq-externals
中文指南
https://github.com/apache/rocketmq-externals/blob/master/rocketmq-console/doc/1_0_0/UserGuide_CN.md

7.1.上传到服务器并解压缩
```
unzip rocketmq-externals-master.zip  -d /opt/module/
```

7.2.编译
进入rocketmq-console目录
执行编译
```
cd /opt/module/rocketmq-externals-master/rocketmq-console
mvn clean package -Dmaven.test.skip=true
```

7.3.启动
编译成功后在rocketmq-console/target目录下执行rocketmq-console-ng-1.0.1.jar
启动时，直接动态添加nameserver地址或编辑application.properties添加属性
```
java -jar rocketmq-console-ng-1.0.1.jar --rocketmq.config.namesrvAddr=127.0.0.1:9876
```

或者在进入源码修改application文件中的配置

```
vi application.properties
```


启动成功后浏览器访问服务器8080端口即可:`http://192.168.117.110:8080/`

## 8.关机rocketmq服务
进入rocket安装目录的bin目录下，分别停止broker，namesrv
 ```
cd /opt/module/rocketmq-master/distribution/target/rocketmq-4.6.1/rocketmq-4.6.1/bin
./ mqshutdown broker
./mqshutdown namesrv
 ```


## 9.错误场景
#### 9.1 启动broker失败 Cannot allocate memory
**原因：**jvm启动初始化内存分配大于物理内存

修改启动脚本中的jvm参数
```
runbroker.sh  ---对应broker的配置
runserver.sh ---nameserver的配置
```
默认数值给的都很大，改小即可
```
vi runbroker.sh
   JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m"
vi runserver.sh   
  JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=100m"
```


#### 9.2 启动broker成功但提示：Failed to obtain the host name

**原因：**无法解析当前的主机名
hosts里添加映射即可
```
vi /etc/hosts
 192.168.117.110 xuexi
```

#### 9.3 tool.sh测试，发送失败提示connect to null failed
 **原因：**不知道nameserver在哪儿
 在tools脚本中添加主机名及端口
 ```
 vi tools.sh
  export NAMESRV_ADDR=localhost:9876
 ```

#### 9.4 若启动consumer启动报错，检查consumer的goup名称是否正确，应匹配正则表达式，由字母或数字组成，不能包含空格等:
  ```
  DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("xxoocsm  ");
  ```
  错误: the specified group[xxoocsm ] contains illegal characters, allowing only ^[%|a-zA-Z0-9_-]+$

