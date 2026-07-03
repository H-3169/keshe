package guat.lxy.bigdata.keshe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("guat.lxy.bigdata.keshe.mapper")
@EnableCaching
public class KesheApplication {
    public static void main(String[] args) {
        SpringApplication.run(KesheApplication.class, args);
    }
}