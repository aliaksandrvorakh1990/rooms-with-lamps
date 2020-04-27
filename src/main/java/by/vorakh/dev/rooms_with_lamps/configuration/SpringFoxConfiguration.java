package by.vorakh.dev.rooms_with_lamps.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfiguration {

    private static final String DESCRIPTION = "This service is how you store information about "
            + "Rooms With Lamps and use for applications.<br/>Use this API calls to manage "
            + "information in this application database.";

    @Value("${doc.github_url}")
    private String github_url;
    @Value("${doc.author}")
    private String author;
    @Value("${doc.email}")
    private String email;
    @Value("${doc.title}")
    private String title;
    @Value("${doc.version}")
    private String version;
    @Value("${doc.license}")
    private String license;
    @Value("${doc.licenseUrl}")
    private String licenseUrl;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("by.vorakh.dev.rooms_with_lamps"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(DESCRIPTION)
                .version(version)
                .contact(new Contact(author, github_url, email))
                .license(license)
                .licenseUrl(licenseUrl)
                .build();
    }

}
