package com.example.springsecurityapplication.config;

import com.example.springsecurityapplication.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;

    // Отклчяем необходимость в зашифрованности паролей в БД
    // В реальном приложении этого нельзя использовать
    @Bean
    public PasswordEncoder getPasswordEncode() { return new BCryptPasswordEncoder(); }
//    public PasswordEncoder getPasswordEncode() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // конфигурируем работу Spring Security
        // .csrf().disable()  // csrf() - включаем защиту от межсайтовой подделки запросов --- комментарием отключаем
        // Далее нобходимо внедрить токен на форму аутентификации, на все остальные формы Spring Security внедрит сам
        http
                .authorizeHttpRequests()  // указываем, что все страницы должны быть защищены аутентификацией
                .requestMatchers("/admin", "/admin/orders").hasRole("ADMIN") // указываем на то что страница /admin доступна пользователю с ролью ADMIN
                // Указываем, что не аутентифицированные пользователи могут зайти на страницу аутентификации и на объект ошибки
                // С помощью permitAll указываем, что неаутентифицированные пользователи могут заходить на пречисленные страницы
                .requestMatchers("/", "/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/product", "/product/info0/{id}", "/product/search").permitAll()
                // Указываем, что для всех остальных страниц необходимо вызвать метод authenticated(), которые открывают форму аутентификации
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()      // дальше указываем, что дальше настаивается аутентификация и соединяем ее с настройкой доступа
                .formLogin().loginPage("/authentication")   // указваем какой url запрос будет отправляться при заходе на защищенные страницы
                // Указвыаем на какой url-адрес будут отправляться данные из формы
                // Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы.
                // Мы задали url, который используется по умолчанию для обработки с формы аутентификации посредством Spring Security.
                // Spring Security будет ждать объект с формы аутентификации и затем сверять логин и пароль с данными и БД
                .loginProcessingUrl("/process_login")
                // Указываем на какой url необходимо направить пользователя после успешной аутентификации.
                // Вторым аргументом указываеся true, чтобы перенаправление шло в любом случае после успешной аутентификации
                .defaultSuccessUrl("/person account", true)
                // Указываем куда необходимо перенаправить при проваленной аутентификации.
                // В запрос будет передан объект error, который будет проверяться на форме и при наличии данного объекта в запросе
                // выводиться сообщение - "Неправильный логин или пароль"
                .failureUrl("/authentication?error")
                .and()
                // указываем настройки для logout
                .logout().logoutUrl("/logout").logoutSuccessUrl("/product"); // logoutSuccessUrl("/authentication"); // /product
        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // authenticationManagerBuilder.authenticationProvider((authenticationProvider));
        authenticationManagerBuilder.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncode());
    }
}
