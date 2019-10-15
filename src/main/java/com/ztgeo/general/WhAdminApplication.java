package com.ztgeo.general;

import com.ace.cache.EnableAceCache;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.github.wxiaoqi.merge.EnableAceMerge;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.ztgeo.general.util.DBLog;
import com.ztgeo.general.util.chenbin.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.ztgeo.general.mapper")
@ComponentScan({"org.activiti.rest.diagram","com.ztgeo.general","com.ztgeo.general.config.activity"})
@EnableSwagger2Doc
@EnableAceCache
@EnableAceMerge  //merge项目用于访问某个实体类的值需要通过其他方法获取时通过注解获取对应表的详细内容  如user的depart详情  通过id查询depart表
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class WhAdminApplication {
    public static void main(String[] args) {
        DBLog.getInstance().start(); //阻塞型日志记载线程启动
        ApplicationContext app =  SpringApplication.run(WhAdminApplication.class, args);
        //启动时将ApplicationContext对象作为资源注入
        SpringUtil.setApplicationContext(app);
    }
}
