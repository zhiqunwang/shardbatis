Shardbatis的名称由shard(ing)+mybatis组合得到。诣在为ibatis实现数据水平切分的功能。




数据的水平切分包括多数据库的切分和多表的数据切分。目前shardbatis已经实现了单数据库的数据多表水平切分。
Shardbatis2.0可以以插件的方式和mybatis3.x进行整合，对mybatis的代码无侵入，不改变用户对mybatis的使用习惯。
shardbatis的使用与原生的mybatis3没有区别，使用者只需要将shardbatis以Mybatis插件的方式引入进来，实现路由策略接口，
实现自己的路由策略即可，此外还需要一个shard_config.xm配置文件，定义哪些sql映射操作需要使用路由策略。


maven配置：
主pom中添加
<shardbatis.version>2.0.0B</shardbatis.version>
<jsqlparser.version>0.7.0</jsqlparser.version>


dao和web模块pom中添加:
<dependency>
<groupId>org.shardbatis</groupId>
<artifactId>shardbatis</artifactId>
<version>${shardbatis.version}</version>
</dependency>
<dependency>
<groupId>net.sf.jsqlparser</groupId>
<artifactId>jsqlparser</artifactId>
<version>${jsqlparser.version}</version>
</dependency>




3.添加sharding配置
新建一个xml文件,例如：shard_config.xml


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE shardingConfig [
        <!ELEMENT shardingConfig (ignoreList?,parseList?,strategy*)>
        <!ELEMENT ignoreList (value+)>
        <!ELEMENT parseList (value+)>
        <!ELEMENT value (#PCDATA)>
        <!ELEMENT strategy EMPTY>
        <!ATTLIST strategy
                tableName CDATA #REQUIRED
                strategyClass CDATA #REQUIRED
                >
        ]>
<shardingConfig>
        <!--
                parseList可选配置
                如果配置了parseList,只有在parseList范围内的sql才会被解析和修改，配置 dao中的方法
        -->
       <parseList>
                <value>com.dao.xxxDao.insert</value>
                <value>com.dao.xxxDao.updateByPrimaryKey</value>
       </parseList>
       !--
                配置分表策略
        -->
        <strategy tableName = "T_xxx表名" strategyClass = "com.dao.impl.ShardStrategyImpl"/>
</shardingConfig>


4.接口实现类,实现一个简单的接口即可,实现自己的sharding策略：
import com.google.code.shardbatis.strategy.ShardStrategy;
/**
         * 得到实际表名
         * @param baseTableName 逻辑表名,一般是没有前缀或者是后缀的表名
         * @param params mybatis执行某个statement时使用的参数
         * @param mapperId mybatis配置的statement id
         * @return
         */
public class ShardStrategyImpl implements ShardStrategy {
@Override
public String getTargetTableName(String tableName, Object arg1, String arg2) {
String sTableName = tableName + "_" + DateUtil.getCurrentDate_YYYY_MM();
return sTableName;
}
}


5.在mybatis配置文件中添加插件配置:
<plugins>
<plugin interceptor="com.google.code.shardbatis.plugin.ShardPlugin">
<property name="shardingConfig" value="shard_config.xml"/>
</plugin>
</plugins>


6.代码中使用shardbatis
因为shardbatis2.0使用插件方式对mybatis功能进行增强，因此使用配置了shardbatis的mybatis3和使用原生的mybatis3没有区别。


如果maven无法识别google的jar，采用以下配置把jar引入war包lib下：
dao和web模块pom中添加:
<dependency>
<groupId>org.shardbatis</groupId>
<artifactId>shardbatis</artifactId>
<version>${shardbatis.version}</version>
<scope>system</scope>
<systemPath>${project.basedir}/libs/shardbatis-2.0.0B.jar</systemPath>
</dependency>
<dependency>
<groupId>net.sf.jsqlparser</groupId>
<artifactId>jsqlparser</artifactId>
</dependency>