package jpastudy.restapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class RestApiApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(RestApiApplication.class, args);
  }

}
