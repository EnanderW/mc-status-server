package me.code.state;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import me.code.PacketHandler;
import me.code.util.BufferUtil;

public class StatusState implements PacketState {

    private PacketHandler handler;

    public StatusState(PacketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, ByteBuf buf, int length, int packetId) {
        if (packetId == 0)
            handleRequestPacket(ctx, buf, length);
        else if (packetId == 1)
            handlePingRequestPacket(ctx, buf, length);
    }

    private void handleRequestPacket(ChannelHandlerContext ctx, ByteBuf buf, int length) {
        System.out.println("Handle Request");
        // length packet_id content

        String json = "{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"1.17.1\",\n" +
                "        \"protocol\": 756\n" +
                "    },\n" +
                "    \"players\": {\n" +
                "        \"max\": 100,\n" +
                "        \"online\": 5,\n" +
                "        \"sample\": [\n" +
                "            {\n" +
                "                \"name\": \"thinkofdeath\",\n" +
                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"description\": {\n" +
                "        \"text\": \"My own status server!\"\n" +
                "    },\n" +
                "    \"favicon\": \"data:image/png;base64,<data>\"\n" +
                "}";
        int packetLength = json.length() + BufferUtil.getVarIntSize(json.length()) + BufferUtil.getVarIntSize(0);

        ByteBuf writeBuf = Unpooled.buffer();

        BufferUtil.writeVarInt(packetLength, writeBuf); // length
        BufferUtil.writeVarInt(0, writeBuf); // packet id
        BufferUtil.writeVarInt(json.length(), writeBuf); // content
        writeBuf.writeBytes(json.getBytes()); // content

        ctx.channel().writeAndFlush(writeBuf);
    }

    private void handlePingRequestPacket(ChannelHandlerContext ctx, ByteBuf buf, int length) {
        System.out.println("Handle Ping Request");
        long payload = buf.readLong();

        int packetLength = 8 + BufferUtil.getVarIntSize(1);

        ByteBuf writeBuf = Unpooled.buffer();

        BufferUtil.writeVarInt(packetLength, writeBuf); // length
        BufferUtil.writeVarInt(1, writeBuf); // packet id
        buf.writeLong(payload); // content

        ctx.channel().writeAndFlush(writeBuf);
    }

}
