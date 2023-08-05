package com.domenic.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author Domenic
 * @Classname DataEncapsulate
 * @Description TODO
 * @Created by Domenic
 */
public class DataEncapsulate {

    public static void main(String[] args) throws IOException {
        String encapsulated = encapsulate();
        System.out.println("Binary Representation: " + encapsulated);
    }

    public static String encapsulate() throws IOException {
        ByteBuf message = Unpooled.buffer();

        // fill in data
        message.writeBytes("domenic".getBytes(StandardCharsets.UTF_8));
        message.writeByte(1);
        message.writeShort(125);
        message.writeInt(256);
        message.writeByte(1);
        message.writeByte(0);
        message.writeByte(2);
        message.writeLong(251455L);
        // encapsulate data object with ObjectOutputStream
        DataObject dataObject = new DataObject("demo message");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(dataObject);
        byte[] bytes = outputStream.toByteArray();
        message.writeBytes(bytes);

        String res = printAsBinary(message);
        // System.out.println("String Representation: " + message.toString(StandardCharsets.UTF_8));

        return res;
    }

    /**
     * Print Netty ByteBuf as binary representation
     * @param byteBuf Netty ByteBuf
     * @return binary representation in String
     */
    private static String printAsBinary(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        String binaryString = ByteBufUtil.hexDump(bytes);

        StringBuilder formattedBinary = new StringBuilder();

        for (int i = 0; i < binaryString.length(); i += 2) {
            formattedBinary.append(binaryString, i, i + 2).append(" ");
        }

        return formattedBinary.toString();
    }

    static class DataObject implements Serializable {

        private String message;

        public DataObject(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "DataObject{" +
                    "message='" + message + '\'' +
                    '}';
        }

    }

}
