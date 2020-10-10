package org.dreamteam.mafia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@ComponentScan("org.dreamteam.mafia")
@EnableWebSocketMessageBroker

public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {


    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat");
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-messaging") // Указываем URL для точки соединения
                // Подключаем обработчик handshake для установления WS соедининия
                // Аргументом ему передается стратегия создания соединения для целового сервера
                // В Spring-e - это Tomcat (в javadoc-е стратегии указана минимальная версия Tomcat-a, кстати)
                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()))
                // Откуда мы будем принимать соедения. * - отовсюду.
                .setAllowedOrigins("*")
                // Разрешаем использование SockJS, если бразуер слишком древний для WS
                // Побочка: со стороны браузера к URL нужно добавлять /websocket, т. к.
                // базовый URL отжирает под себя SockJS
                // Например /chat-messaging/websocket вместо /chat-messaging
                .withSockJS();
    }
}