package me.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.code.state.HandshakeState;
import me.code.state.PacketState;
import me.code.util.BufferUtil;

public class PacketHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private PacketState state;

    public PacketHandler() {
        setState(new HandshakeState(this));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        while (byteBuf.readableBytes() > 0) {
            int length = BufferUtil.readVarInt(byteBuf);
            System.out.println("1: " + byteBuf.readableBytes());
            int packetId = BufferUtil.readVarInt(byteBuf);
            System.out.println("2: " + byteBuf.readableBytes());

            this.state.handle(ctx, byteBuf, length, packetId);
            System.out.println("3: " + byteBuf.readableBytes());
        }
    }

    public void setState(PacketState state) {
        this.state = state;

        System.out.println("Switched to " + state.getClass().getSimpleName());
    }
}
