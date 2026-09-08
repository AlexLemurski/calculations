package ru.project.calculations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
		return http
			.csrf(csrf -> csrf
				.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
				.csrfTokenRepository(csrfTokenRepository)
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/css/**", "/js/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/calculations", true)
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.addLogoutHandler((request, response, authentication) -> {
					if (request.getSession(false) != null) {
						sessionRegistry().removeSessionInformation(request.getSession(false).getId());
					}
				})
				.addLogoutHandler(new HeaderWriterLogoutHandler(
					new ClearSiteDataHeaderWriter(
						ClearSiteDataHeaderWriter.Directive.COOKIES,
						ClearSiteDataHeaderWriter.Directive.STORAGE,
						ClearSiteDataHeaderWriter.Directive.CACHE
					)))
				.permitAll()
			)
			.sessionManagement(session -> session
				.sessionFixation().migrateSession()
				.maximumSessions(1)
//				.maxSessionsPreventsLogin(true)
				.sessionRegistry(sessionRegistry())
			)
			.build();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

}