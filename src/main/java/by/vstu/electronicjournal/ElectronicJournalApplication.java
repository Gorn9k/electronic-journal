package by.vstu.electronicjournal;

import by.vstu.electronicjournal.security.CustomTokenConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@RequiredArgsConstructor
public class ElectronicJournalApplication {

    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(ElectronicJournalApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @PostConstruct
    public void replaceDefaultConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverterBean = applicationContext.getBean(JwtAccessTokenConverter.class);
        jwtAccessTokenConverterBean.setAccessTokenConverter(new CustomTokenConverter());
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("128KB"));
        factory.setMaxRequestSize(DataSize.parse("128KB"));
        return factory.createMultipartConfig();
    }
}