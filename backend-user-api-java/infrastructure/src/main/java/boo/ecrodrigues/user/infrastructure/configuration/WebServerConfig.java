package boo.ecrodrigues.user.infrastructure.configuration;

import java.util.Collections;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("boo.ecrodrigues.user")
public class WebServerConfig implements WebMvcConfigurer {

  @Override
  public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
    configurer.useRegisteredExtensionsOnly(true);
    configurer.replaceMediaTypes(Collections.emptyMap());
  }

  @Override
  @SuppressWarnings("deprecation")
  public void configurePathMatch(final PathMatchConfigurer configurer) {
    // The below will become the default values in 5.3
    // Safe to use in 5.2 as long as disabling pattern match
    configurer.setUseSuffixPatternMatch(Boolean.FALSE);
    configurer.setUseRegisteredSuffixPatternMatch(Boolean.TRUE);
    configurer.setUseTrailingSlashMatch(true);
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {

  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
