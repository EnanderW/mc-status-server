package me.code.state;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.PacketHandler;
import me.code.util.BufferUtil;

public class HandshakeState implements PacketState {

    private PacketHandler handler;

    public HandshakeState(PacketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, ByteBuf buf, int length, int packetId) {

        if (packetId == 0)
            handleIntentionPacket(ctx, length, buf);
    }

    private void handleIntentionPacket(ChannelHandlerContext ctx, int length, ByteBuf buf) {
        int protocolVersion = BufferUtil.readVarInt(buf);
        String serverAddress = BufferUtil.readVarString(buf);
        int port = buf.readUnsignedShort();
        int nextState = BufferUtil.readVarInt(buf);

        System.out.println(protocolVersion + " " + serverAddress + " " + port + " " + nextState);

        if (nextState == 1)
            handler.setState(new StatusState(handler));
    }

}
