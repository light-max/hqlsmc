package com.hql.scm.config;

import com.hql.scm.config.interceptor.AdminInterceptor;
import com.hql.scm.config.interceptor.GlobalInterceptor;
import com.hql.scm.config.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/notloggedin");
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/api/login")
                .excludePathPatterns("/user/notloggedin")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/api/register")
                .excludePathPatterns("/user/home");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("/admin/home");
        registry.addViewController("/user/login").setViewName("/user/login");
        registry.addViewController("/user/register").setViewName("/user/register");
        registry.addViewController("/user/da/submit/view").setViewName("/user/da/send");
        registry.addViewController("/user/setpwd").setViewName("/user/setpwd");
    }
}
