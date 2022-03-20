package com.git.blog.api.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kaka
 */
@Configuration
@EnableSwagger2
@RefreshScope
public class SwaggerConfig {

    @Value(value = "${swagger.enable:true}")
    private boolean enableSwagger;

    /**
     * 这个给前端生成代码用的
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        //在配置好的配置类中增加此段代码即可
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenBuilder = new ParameterBuilder();
        Parameter token = tokenBuilder.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enableSwagger)
                .groupName("default")
                .select()
                //这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                //************把消息头添加
                .globalOperationParameters(pars);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OPEN_BLOG Doc")
                .description("OPEN_BLOG+ Api文档")
                .version("1.0")
                .build();
    }

//    @Bean
//    @Primary
//    public ServiceModelToSwagger2Mapper serviceModelToSwagger2Mapper(){
//        PrimaryServiceModelToSwagger2MapperImpl primaryServiceModelToSwagger2Mapper = new PrimaryServiceModelToSwagger2MapperImpl();
//        primaryServiceModelToSwagger2Mapper.addReplaceRegular(new PrimaryServiceModelToSwagger2MapperImpl.ReplaceRegular("com.git.blog.api/mina","subscribe/com.git.blog.api/mina"));
//        return primaryServiceModelToSwagger2Mapper;
//    }
}
