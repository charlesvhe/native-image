参考文档 https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.native-build-tools

安装SDKMAN https://sdkman.io
sdk install java 22.3.r17-nik
sdk use java 22.3.r17-nik

provider模块目录运行
注意 provider pom build必须有spring-boot-maven-plugin
````
<plugin>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
````
mvn clean
mvn -Pnative native:compile 构建原生镜像

整体spring-data-r2dbc只适合单表简单CRUD，多表需要手写SQL。使用QueryDslR2dbcTemplate集成querydsl，querydsl仅仅作为生成SQL/绑定参数/类型映射工具，底层SQL执行由r2dbc负责。
