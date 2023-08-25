package com.domenic.transport.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Domenic
 * @Classname DrpcRequest
 * @Description Drpc Request
 * @Created by Domenic
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {

    /**
     * request id
     */
    private long requestId;

    /**
     * request type
     */
    private byte requestType;
    /**
     * message compression type
     */
    private byte compressType;
    /**
     * message serialization type
     */
    private byte serializeType;

    /**
     * request payload
     */
    private RequestPayload requestPayload;

}
