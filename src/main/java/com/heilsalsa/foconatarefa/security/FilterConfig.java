package com.heilsalsa.foconatarefa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilterRegistration(JwtAuthFilter filter) {
        FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(filter);               // injeta o seu filtro
        reg.addUrlPatterns("/*");            // filtra tudo, mas no filtro você já libera login/GET etc.
        reg.setOrder(1);
        return reg;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }
}
