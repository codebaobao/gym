//package com.portal.config;
//
//import org.apache.catalina.Context;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
//import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TomcatConfigure{
//	@Bean
//	public EmbeddedServletContainerFactory servletContainer() {
//
//	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//
//	    TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
//	        @Override
//	        public void customize(Context context) {
//	            context.addWelcomeFile("/login.html");
//	        }
//	    };
//	    factory.addContextCustomizers(contextCustomizer);
//
//	    return factory;
//	}
//}
