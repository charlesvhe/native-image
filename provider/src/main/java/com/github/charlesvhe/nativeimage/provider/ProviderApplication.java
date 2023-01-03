package com.github.charlesvhe.nativeimage.provider;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "com.github.charlesvhe.nativeimage")
public class ProviderApplication {

    @Bean
    public SQLQueryFactory sqlQueryFactory() {
        // 仅用来生成sql 不执行
        DataSource dataSource = null;
        return new SQLQueryFactory(new Configuration(H2Templates.DEFAULT), dataSource);
    }

    public static void main(String[] args) {
        // 开启 H2database TCP服务端
        // jdbc:h2:mem:testdb
        // r2dbc:h2:mem:///testdb
        // jdbc:h2:tcp://localhost/mem:testdb
        try {
            Server server = Server.createTcpServer("-tcpAllowOthers", "-ifNotExists").start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(ProviderApplication.class, args);
    }
}