package com.domenic.transport.message;

import io.netty.handler.codec.TooLongFrameException;

/**
 * @author Domenic
 * @Classname MessageFormatConstant
 * @Description Message Format Constants
 * @Created by Domenic
 */
public class MessageFormatConstant {

    private MessageFormatConstant() {
    }

    /**
     * Magic bytes
     */
    public static final byte[] MAGIC_VALUE = "drpc".getBytes();

    /**
     * Version value
     */
    public static final byte VERSION_VALUE = 1;

    /**
     * Bytes of "version"
     */
    public static final short VERSION_BYTES = 1;

    /**
     * Message header length value
     */
    public static final short HEADER_LENGTH_VALUE = (byte) (MAGIC_VALUE.length + 1 + 2 + 4 + 1 + 1 + 1 + 8);

    /**
     * Bytes of "message header length"
     */
    public static final short HEADER_LENGTH_BYTES = 2;

    /**
     * Bytes of "message full length"
     */
    public static final int FULL_LENGTH_BYTES = 4;

    /**
     * <p>The maximum length of the frame</p>
     * <p>If the length of the frame is geater than this value, {@link TooLongFrameException} will be thrown</p>
     */
    public static final int MAX_FRAME_LENGTH = 1024 * 1024;

}
