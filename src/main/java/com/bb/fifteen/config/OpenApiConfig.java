package com.bb.fifteen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LCK 기록 데이터 API")
                        .version("0.1")
                        .description("LCK 팀 및 선수별 기록 API를 제공합니다")
                        .contact(new Contact()
                                .name("bb")
                                .email("k941026h@naver.com")
                                .url("https://bbyuck.tistory.com")));
    }
}
