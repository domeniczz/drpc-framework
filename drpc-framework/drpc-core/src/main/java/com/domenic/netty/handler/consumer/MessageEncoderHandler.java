package com.domenic.netty.handler.consumer;

import com.domenic.exceptions.SerializationException;
import com.domenic.transport.message.MessageFormatConstant;
import com.domenic.transport.message.RequestMessage;
import com.domenic.transport.message.RequestPayload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Domenic
 * @Classname MessageEncoder
 * @Description Message Encoder
 * @Created by Domenic
 */
public class MessageEncoderHandler extends MessageToByteEncoder<RequestMessage> {

    /**
     * Encode the message based on defined structure
     */
    @Override
    protected void encode(ChannelHandlerContext context, RequestMessage msg, ByteBuf out) throws Exception {
        // magic value
        out.writeBytes(MessageFormatConstant.MAGIC_VALUE);
        // version
        out.writeByte(MessageFormatConstant.VERSION_VALUE);
        // header length
        out.writeShort(MessageFormatConstant.HEADER_LENGTH_VALUE);
        // full length (unknown yet, move writer index 4 bytes forward)
        out.writerIndex(out.writerIndex() + 4);

        out.writeByte(msg.getRequestType());
        out.writeByte(msg.getSerializeType());
        out.writeByte(msg.getCompressType());
        out.writeLong(msg.getRequestId());

        // message body
        byte[] body = bodyToBytes(msg.getRequestPayload());
        out.writeBytes(body);

        int currentWriterIndex = out.writerIndex();
        // place writer index to beginning of "header length"
        out.writerIndex(7);
        // write header length value
        out.writeInt(MessageFormatConstant.HEADER_LENGTH_VALUE + body.length);

        // return writer index to the end
        out.writerIndex(currentWriterIndex);
    }

    /**
     * Encode the message body to byte array
     */
    private byte[] bodyToBytes(RequestPayload payload) {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ObjectOutputStream oout = new ObjectOutputStream(bout)) {
            oout.writeObject(payload);
            return bout.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
    }

}
