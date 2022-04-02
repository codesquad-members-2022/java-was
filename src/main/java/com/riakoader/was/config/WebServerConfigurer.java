package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMapper;

public interface WebServerConfigurer {

    /**
     * 사용할 'Handler' 를 'HandlerRegistry' 에 등록합니다. 현재는 UserHandler 만 등록하고 있습니다.
     * HandlerRegistry 에 등록된 순서에 따라 Handler 의 index 값이 결정됩니다.
     *
     * @param handlerRegistry
     * @throws Exception
     *
     */
    default void addHandler(HandlerRegistry handlerRegistry) throws Exception {
    }

    /**
     * HandlerMapper 에 'URL' 과 HandlerRegistry 에 저장된 Handler 의 'index' 값을 매핑합니다.
     * ex. UserHandler 는 UserHandler 는 '/users' 과 매핑되며 HandlerRegistry 에 0 번째 index 에 저장되어있으므로 - handlerMapper.mappingHandler("/user", 0);
     *
     * @param handlerMapper
     *
     */
    default void configureHandlerMapper(HandlerMapper handlerMapper) {
    }

    /**
     * Handler 와 연동될 'HandlerMethodMapper' 를 HandlerMethodMapperRegistry 등록합니다.
     * Handler 와 마찬가지로 등록된 순서에 따라 index 값이 결정되므로 이 값을 사용하여 Handler 와 HandlerMethodMapper 를 바인딩할 수 있습니다.
     *
     * HandlerMethodMapper 는 특정 Handler 가 담당할 메서드들의 집합입니다.
     * HandlerMethod 는 함수형 인터페이스로 구현되어있으며, 각자 매핑된 'HttpMethod' 와 'URL' 이 존재합니다.
     * UserHandler 에 바인딩된 HandlerMethodMapper 에는 GET /create, POST /create, POST /login, GET /logout 으로 매핑된 HandlerMethod 가 존재합니다.
     *
     * @param handlerMethodMapperRegistry
     *
     */
    default void addHandlerMethodMapper(HandlerMethodMapperRegistry handlerMethodMapperRegistry) {
    }

    /**
     * HandlerRegistry 에 등록된 Handler 를 모두 순회하며 index 값으로 HandlerMethodMapperRegistry 에서 각자의 HandlerMethodMapper 를 찾아 바인딩합니다. (setter 주입 방식 느낌)
     * ResourceHandler 를 제외한 모든 Handler 는 Handler 인터페이스를 구현하고 있으며 HandlerMethod 로 동작을 결정합니다.
     *
     * @param handlerRegistry
     * @param handlerMethodMapperRegistry
     *
     */
    default void bindMethodsToHandler(HandlerRegistry handlerRegistry, HandlerMethodMapperRegistry handlerMethodMapperRegistry) {
    }
}
