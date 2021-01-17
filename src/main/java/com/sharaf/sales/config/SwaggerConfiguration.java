package com.sharaf.sales.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
//@Import(SpringDataRestConfiguration.class)
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Public-API").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Sales Manager API").description("Look and feel for a basic sale interfaces...")
				.termsOfServiceUrl("https://www.linkedin.com/in/mohammad-sharaf-b8b77412b/")
				.contact(new Contact("Mohammad Sharaf", "https://www.linkedin.com/in/mohammad-sharaf-b8b77412b/", "SalesManager@gmail.com"))
				.license("SalesManager License").licenseUrl("SalesManager@gmail.com").version("1.0").build();
	}

}