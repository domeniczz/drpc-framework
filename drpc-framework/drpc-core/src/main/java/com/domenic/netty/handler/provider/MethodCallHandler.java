package com.domenic.netty.handler.provider;

import com.domenic.config.ServiceConfig;
import com.domenic.drpc.DrpcBootstrap;
import com.domenic.exceptions.ServiceCallFailedException;
import com.domenic.transport.message.RequestMessage;
import com.domenic.transport.message.RequestPayload;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MethodCallHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, RequestMessage msg) throws Exception {
        RequestPayload payload = msg.getRequestPayload();

        Object res = callTargetMethod(payload);

        // TODO: encapsulate response message

        context.channel().writeAndFlush(res);
    }

    /**
     * call target method based on info in payload
     * @param payload request payload
     * @return call result
     */
    private Object callTargetMethod(RequestPayload payload) {
        String interfaceName = payload.getInterfaceName();
        String methodName = payload.getMethodName();
        Class<?>[] parametersType = payload.getParametersType();
        Object[] parametersValue = payload.getParametersValue();

        // get matched service
        ServiceConfig<?> service = DrpcBootstrap.SERVICES.get(interfaceName);

        // get implementation of the service
        Object reference = service.getReference();
        Class<? extends Object> implementation = reference.getClass();

        try {
            // call target method
            Method method = implementation.getMethod(methodName, parametersType);
            return method.invoke(reference, parametersValue);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error("Exception when call target method \"{}\" of service \"{}\", error: {}", methodName, interfaceName, e.getMessage());
            throw new ServiceCallFailedException(e.getMessage());
        }
    }

}
