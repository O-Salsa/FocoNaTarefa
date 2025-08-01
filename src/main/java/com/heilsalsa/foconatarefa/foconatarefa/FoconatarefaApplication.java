package com.heilsalsa.foconatarefa.foconatarefa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.heilsalsa.foconatarefa.security.JwtAuthFilter;

@SpringBootApplication(scanBasePackages = "com.heilsalsa.foconatarefa")
@EnableJpaRepositories(basePackages = "com.heilsalsa.foconatarefa.repository")
@EntityScan(basePackages = "com.heilsalsa.foconatarefa.model")
public class FoconatarefaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoconatarefaApplication.class, args);
    }
    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(){
    	FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
    	registrationBean.setFilter(new JwtAuthFilter());
    	registrationBean.addUrlPatterns("/api/*");
    	return registrationBean;
    	}
    
    
    
}
    
    
    
    

