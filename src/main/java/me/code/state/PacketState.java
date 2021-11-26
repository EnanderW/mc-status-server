package me.code.state;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface PacketState {


    void handle(ChannelHandlerContext ctx, ByteBuf buf, int length, int packetId);
}
