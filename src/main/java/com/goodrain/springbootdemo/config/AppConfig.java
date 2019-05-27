package com.goodrain.springbootdemo.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AppConfig {
    /*错误页处理*/
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
                ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/405");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");

                container.addErrorPages(error401Page,error405Page, error404Page, error500Page);
            }
        };
    }

    //自定义WebMvcConfigurerAdapter
    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            //自定义视图映射
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                //registry.addViewController("/").setViewName("forward:/index.do");
                registry.addViewController("/index.html").setViewName("model1/login");
                //registry.addViewController("/main.html").setViewName("dashboard");
                //registry.addViewController("/register.html").setViewName("register");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(new LoginHandlerInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/admin/login.do","/admin/logout.do");
            }

            //配置自定义静态资源路径
//            @Override
//            public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                //虚拟url 真实工程环境  http://127.0.0.1:8080/ff/resources/img/1.jpg
//                registry.addResourceHandler("/ff/resources/**").addResourceLocations("classpath:/static/");
//                String filepath = configParam.getFilepath();
//                registry.addResourceHandler("/pic/**").addResourceLocations("file:"+filepath);
//            }

            /* 跨域请求 */
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedHeaders("*")
//                        .allowedMethods("*")
//                        .allowedOrigins("*");
//            }

        };
        return adapter;
    }
}
