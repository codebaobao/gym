package com.portal.controller.event;

import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	private static ILogger logger = LogUtil.getLogger(
			LogModule.EventPush, EventPushByNettyJob.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		String ipAddress = ctx.channel().remoteAddress().toString();
		NettyJob.getConnChannelMap().put(ipAddress, ctx);
		logger.info(ipAddress + " connected...");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		String ipAddress = incoming.remoteAddress().toString();
		if (!incoming.isActive()) {
			NettyJob.getConnChannelMap().remove(ipAddress);
		}
		ctx.close();
		logger.error(ipAddress + " disconnect...");
	}
}
