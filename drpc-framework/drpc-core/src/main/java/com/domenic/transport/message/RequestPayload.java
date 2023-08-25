package com.domenic.transport.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Domenic
 * @Classname RequestPayload
 * @Description info of requested interface
 * @Created by Domenic
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * requested interface name
     */
    private String interfaceName;

    /**
     * requested method name
     */
    private String methodName;

    /**
     * type of method's parameters
     */
    private Class<?>[] parametersType;
    /**
     * value of method's parameters
     */
    private Object[] parametersValue;

    /**
     * required type of return value
     */
    private Class<?> returnType;

}
