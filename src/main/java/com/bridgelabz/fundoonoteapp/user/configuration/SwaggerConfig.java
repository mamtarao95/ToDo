package com.bridgelabz.fundoonoteapp.user.configuration;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;

import static com.google.common.base.Predicates.or;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
	/*@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())                                   
          .build();                                           
    }
*/
	/*
	@Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").select()
                .paths(postPaths())
                .apis(RequestHandlerSelectors.basePackage("com.bridgelabz.fundoonoteapp"))
              //  .paths(PathSelectors.ant("/fundoo/.*"))
                .build();
    }*/
	/*
	                                  
	    @Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .select()                                  
	          .apis(RequestHandlerSelectors.any())              
	          .paths(PathSelectors.any())                          
	          .build();                                           
	    }
	

   private Predicate<String> postPaths() {
	   return or(regex("/.*"), regex("/.*"));
    }*/

  
   /* @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null, "token", ApiKeyVehicle.HEADER, "token", ",");
    }
    */
    
  /*  @Bean
    SecurityConfiguration security() {
    return new SecurityConfiguration(null, null, null, null, "token", ApiKeyVehicle.HEADER, "token",",");
    }*/
    
    
  /* private SecurityScheme securityScheme() {
	    GrantType grantType = new AuthorizationCodeGrantBuilder()
	        .tokenEndpoint(new TokenEndpoint( "/token", "oauthtoken"))
	        .tokenRequestEndpoint(
	          new TokenRequestEndpoint( "/authorize",null,null))
	        .build();
	 
	    SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
	        .grantTypes(Arrays.asList(grantType))
	       
	        .build();
	    return oauth;
	}*/ 
	@Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/.*"), regex("/.*"));
    }

    @SuppressWarnings("deprecation")
      private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("FundooNoteAppln").description("Notes Taking using Spring Boot ANd MongoDB ")
                .contact("dharaparanjape.1007@gmail.com").version("1.0").build();
    }
   
    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null, "token",
                ApiKeyVehicle.HEADER, "token", ",");
    }

}
	
