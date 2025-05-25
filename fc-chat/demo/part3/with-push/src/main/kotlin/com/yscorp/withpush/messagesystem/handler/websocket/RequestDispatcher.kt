package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.dto.websocket.inbound.BaseRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.reflect.ParameterizedType

@Component
class RequestDispatcher(listableBeanFactory: ListableBeanFactory) {
    private val handlerMap: MutableMap<Class<out BaseRequest?>, BaseRequestHandler<out BaseRequest?>> =
        HashMap<Class<out BaseRequest?>, BaseRequestHandler<out BaseRequest?>>()
    private val listableBeanFactory: ListableBeanFactory = listableBeanFactory

    fun <T : BaseRequest?> dispatchRequest(
        webSocketSession: WebSocketSession?, request: T
    ) {
        val handler = handlerMap[request.getClass()] as BaseRequestHandler<T?>?
        if (handler != null) {
            handler.handleRequest(webSocketSession, request)
            return
        }
        log.error("Handler not found for request type: {}", request.getClass().getSimpleName())
    }

    @PostConstruct
    private fun prepareRequestHandlerMapping() {
        val beanHandlers: Map<String, BaseRequestHandler<*>> =
            listableBeanFactory.getBeansOfType<BaseRequestHandler<*>>(
                BaseRequestHandler::class.java
            )
        for (handler in beanHandlers.values) {
            val requestClass: Class<out BaseRequest?>? = extractRequestClass(handler)
            if (requestClass != null) {
                handlerMap[requestClass] = handler
            }
        }
    }

    private fun extractRequestClass(handler: BaseRequestHandler<*>): Class<out BaseRequest?>? {
        for (type in handler.javaClass.genericInterfaces) {
            if (type is ParameterizedType
                && type.rawType == BaseRequestHandler::class.java
            ) {
                return type.actualTypeArguments[0] as Class<out BaseRequest?>
            }
        }
        return null
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RequestDispatcher::class.java)
    }
}
