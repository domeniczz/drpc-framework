package com.domenic.netty.handler.consumer;

import com.domenic.exceptions.SerializationException;
import com.domenic.transport.message.MessageFormatConstant;
import com.domenic.transport.message.RequestMessage;
import com.domenic.transport.message.RequestPayload;
import com.domenic.transport.message.Types;

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
        out.writerIndex(out.writerIndex() + MessageFormatConstant.FULL_LENGTH_BYTES);

        out.writeByte(msg.getRequestType());
        out.writeByte(msg.getSerializeType());
        out.writeByte(msg.getCompressType());
        out.writeLong(msg.getRequestId());

        int bodyLength = 0;

        // if request type is normal request, write message body
        if (msg.getRequestType() == Types.RequestType.REQUEST.getId()) {
            // get message body in byte array
            byte[] body = bodyToBytes(msg.getRequestPayload());
            out.writeBytes(body);
            bodyLength = body.length;
        }

        int currentWriterIndex = out.writerIndex();
        // place writer index to beginning of "full length"
        out.writerIndex(MessageFormatConstant.MAGIC_VALUE.length +
                MessageFormatConstant.VERSION_BYTES +
                MessageFormatConstant.HEADER_LENGTH_BYTES);
        // write full length value
        out.writeInt(MessageFormatConstant.HEADER_LENGTH_VALUE + bodyLength);

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
