package com.portal.controller.event;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;
import com.portal.utils.thread.AbstractJob;

public class NettyJob extends AbstractJob {
	private static ILogger logger = LogUtil.getLogger(
			LogModule.EventPush, NettyJob.class);

	private static ConcurrentHashMap<String, ChannelHandlerContext> connChannelMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

	private int port = 8888;

	@Override
	public void handle() {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap boorstrap = new ServerBootstrap();
			boorstrap.group(boss, worker).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)// 连接数
					.option(ChannelOption.TCP_NODELAY, true)// 不延迟，消息立即发送
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 长连接
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel)
								throws Exception {
							ChannelPipeline p = socketChannel.pipeline();
							p.addLast(new NettyServerHandler());
						}
					});
			ChannelFuture f = boorstrap.bind(port).sync();
			if (f.isSuccess()) {
				logger.debug("启动Netty服务成功，端口号：" + this.port);
			}
			// 关闭连接
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("启动Netty服务异常，异常信息：" + e.getMessage());
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	public static ConcurrentHashMap<String, ChannelHandlerContext> getConnChannelMap() {
		return connChannelMap;
	}

}
