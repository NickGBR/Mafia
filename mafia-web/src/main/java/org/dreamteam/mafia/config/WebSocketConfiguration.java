package org.dreamteam.mafia.config;

import org.dreamteam.mafia.security.SignedJsonWebToken;
import org.dreamteam.mafia.security.TokenAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Класс для настройки параметров WebSocket
 */
@Configuration
@ComponentScan("org.dreamteam.mafia")
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final TokenAuthenticationProvider provider;
    private final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Autowired
    public WebSocketConfiguration(final TokenAuthenticationProvider provider) {
        super();
        this.provider = provider;
    }

    /**
     * Регистрирует точки подключения клиентов Stomp
     *
     * @param registry - реестр точек подключения клиентов Stomp
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-messaging").withSockJS();
    }

    /**
     * Настраивает брокер сообщений
     *
     * @param config - настройки брокера сообщений
     */
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat");
        //Установка префикса для URL
        config.setApplicationDestinationPrefixes("/app");
        //Установка префикса для отправки сообщений определенным юзерам
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Устанавливаем перехватчик сообщений, чтобы аутентифицировать
     * пользователя при подключение через STOMP заголовок, согласно:
     * https://github.com/spring-projects/spring-framework/blob/master/src/docs/asciidoc/web/websocket.adoc#token-authentication
     *
     * @param registration - конфигурации канала, включающая в частности перехватчики сообщений
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if ((accessor != null) && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("x-auth-token");
                    logger.debug("WebSocket Connection attempt. Token: " + token);
                    Authentication user = provider.authenticate(new SignedJsonWebToken(token));
                    accessor.setUser(user);
                }
                return message;
            }
        });
    }
}