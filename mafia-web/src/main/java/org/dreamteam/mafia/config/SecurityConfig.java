package org.dreamteam.mafia.config;

import org.dreamteam.mafia.security.TokenAuthenticationFilter;
import org.dreamteam.mafia.security.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Класс для настройки параметров Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Доступны анонимным пользователя URL среди API
    private static final RequestMatcher PUBLIC_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/user/register"),
            new AntPathRequestMatcher("/api/user/login")
    );

    // Защищаемые URL
    private static final RequestMatcher SECURED_URLS = new AndRequestMatcher(
            new NegatedRequestMatcher(PUBLIC_API_URLS),
            new AntPathRequestMatcher("/api/**"));
    // Доступные любым пользователям URL
    private static final RequestMatcher PUBLIC_URLS = new NegatedRequestMatcher(SECURED_URLS);
    private final TokenAuthenticationProvider provider;

    @Autowired
    public SecurityConfig(final TokenAuthenticationProvider provider) {
        super();
        this.provider = provider;
    }

    /**
     * Настраивает менеджер аутентификации
     *
     * @param auth - менеджер аутентификации
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    /**
     * Настраивает параметры безопасноти HTTP
     *
     * @param http - параметры безопасноти HTTP
     * @throws Exception - при ошибках настройки
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Поскольку токен передается с каждым запросом,
                // то нет нужды создавать сессию
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                // Добавляем наш фильтр в начало цепочки аутентификации
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                // Публичные API-запросы аутентифицируем, но разрешаем даже анонимным
                .requestMatchers(PUBLIC_API_URLS)
                .permitAll()
                // Защищенные разрешаем только успешно аутентифицирвоанным
                .requestMatchers(SECURED_URLS)
                .authenticated()
                .and()
                // Настраиваем bean, отвечающий за обработку отказа аутентификации по указанным URL
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), SECURED_URLS)
                .and()
                // Отключаем подедржку CSRF, встроенной формы логина, аутентификации по HTTP и logout-а
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    /**
     * Настраивае обработчик успешной аутентификации, отключает редирект
     *
     * @return - Обработчик успешной аутентификации
     */
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((
                                                   httpServletRequest,
                                                   httpServletResponse, s) -> {
        });
        return successHandler;
    }

    /**
     * Настраивает и возвращает фильтр аутентификации по токенам
     *
     * @return - настроенный фильтр для включения в цепочку аутентификации
     * @throws Exception - при ошибках в натройке фильтра
     */
    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(SECURED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    /**
     * Возвращает обработчик проваленной аутентификации в виде страниы 403
     *
     * @return - Обработчик проваленной аутентификации
     */
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(FORBIDDEN);
    }
}
