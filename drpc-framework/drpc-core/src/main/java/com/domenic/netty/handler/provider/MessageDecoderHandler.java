package com.domenic.netty.handler.provider;

import com.domenic.exceptions.DecodeException;
import com.domenic.exceptions.SerializationException;
import com.domenic.transport.message.MessageFormatConstant;
import com.domenic.transport.message.RequestMessage;
import com.domenic.transport.message.RequestPayload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Domenic
 * @Classname MessageDecoderHandler
 * @Description Message Decoder Handler
 * @Created by Domenic
 */
public class MessageDecoderHandler extends LengthFieldBasedFrameDecoder {

    public MessageDecoderHandler() {
        super(
                // define max frame length
                // if the length of the frame is geater than this value, it will be discard
                MessageFormatConstant.MAX_FRAME_LENGTH,
                // offset of the beginning of "full length" field
                MessageFormatConstant.MAGIC_VALUE.length + MessageFormatConstant.VERSION_BYTES + MessageFormatConstant.HEADER_LENGTH_BYTES,
                // length of "full length" field
                MessageFormatConstant.FULL_LENGTH_BYTES,
                /*
                 * A compensation value to add to or subtract from the value of the length field
                 * The `lengthAdjustment` parameter is used to compensate for the fact that the length field is signed.
                 * The lengthAdjustment allows you to adjust the length value as needed for your specific protocol.
                 *
                 * Example:
                 * If your length field includes the length of the header, and you want to
                 * exclude the header from the length, you would use a negative lengthAdjustment.
                 */
                -(MessageFormatConstant.MAGIC_VALUE.length + MessageFormatConstant.VERSION_BYTES
                        + MessageFormatConstant.HEADER_LENGTH_BYTES + MessageFormatConstant.FULL_LENGTH_BYTES),
                0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // decode the message into a byte array
        Object obj = super.decode(ctx, in);
        if (obj instanceof ByteBuf byteBuf) {
            return decodeFrame(byteBuf);
        }
        return null;
    }

    /**
     * Decode the message frame based on defined protocol structure
     * @param byteBuf message frame
     * @return decode result
     */
    private Object decodeFrame(ByteBuf byteBuf) {
        // decode magic value
        byte[] magic = new byte[MessageFormatConstant.MAGIC_VALUE.length];
        byteBuf.readBytes(magic);
        // check if magic value matches with the expected value
        for (int i = 0; i < magic.length; i++) {
            if (magic[i] != MessageFormatConstant.MAGIC_VALUE[i]) {
                throw new DecodeException("The request obtained is not legitimate (magic value mismatch)");
            }
        }

        // decode version
        byte version = byteBuf.readByte();
        if (version > MessageFormatConstant.VERSION_VALUE) {
            throw new DecodeException("The request obtained is not legitimate (version " + version + " is not supported)");
        }

        // decode head length
        short headLength = byteBuf.readShort();

        // decode full frame length
        int fullLength = byteBuf.readInt();

        // decode request type (2: heartbeat)
        byte requestType = byteBuf.readByte();

        // decode serialize type
        byte serializeType = byteBuf.readByte();

        // decode compress type
        byte compressType = byteBuf.readByte();

        // decode request id
        long requestId = byteBuf.readLong();

        RequestMessage message = new RequestMessage();
        message.setRequestType(requestType);
        message.setCompressType(compressType);
        message.setSerializeType(serializeType);

        if (requestType == 2) {
            return message;
        }

        int payloadLength = fullLength - headLength;
        byte[] payloadBytes = new byte[payloadLength];
        // read bytes into byte array `payloadBytes`
        byteBuf.readBytes(payloadBytes);

        RequestPayload payload = bytesToBody(payloadBytes);
        message.setRequestPayload(payload);

        return message;
    }

    /**
     * Decode the message body from byte array
     */
    private RequestPayload bytesToBody(byte[] bytes) {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (RequestPayload) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException(e.getMessage());
        }
    }

}
