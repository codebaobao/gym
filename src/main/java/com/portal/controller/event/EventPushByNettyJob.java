package com.portal.controller.event;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSON;
import com.portal.utils.event.EventInfo;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;
import com.portal.utils.thread.AbstractJob;

public class EventPushByNettyJob extends AbstractJob {
	private static ILogger logger = LogUtil.getLogger(
			LogModule.EventPush, EventPushByNettyJob.class);

	private boolean stopFlag = false;

	private static AtomicLong eventId;

	@Override
	public void handle() {

		while (!stopFlag) {
			try {
				BlockingQueue<EventInfo> eventQueue = EventPushService
						.getInstance().getEventServerQueue();
				EventInfo event = eventQueue.take();
				eventId = new AtomicLong(System.currentTimeMillis());
				String eventMessage = getEventMessage(event);
				Set<String> keys = NettyJob.getConnChannelMap().keySet();
				for (String key : keys) {
					NettyJob.getConnChannelMap().get(key).writeAndFlush(
							getSendByteBuf(eventMessage));
					logger.info("To push client " + key + " eventData by netty:"
							+ event.toString());
				}
			} catch (Exception e) {
				logger.warn("", e);
			}
		}
	}

	private String getEventMessage(EventInfo event) {
		String eventType = event.getAction().name();
		String eventAction = event.getSource().getType().name();

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"eventType\": ").append("\"").append(eventType)
				.append("\"").append(",");
		sb.append("\"eventAction\": ").append("\"").append(eventAction)
				.append("\"").append(",");
		sb.append("\"eventId\": ").append("\"")
				.append(eventId.getAndIncrement()).append("\"");
        if(event.getSource().getDetail() != null){
        	sb.append(", \"detail\":").append(JSON.toJSONString(event.getSource().getDetail()));
        }else{
        	sb.append(", \"detail\":{}");
        }
		sb.append("}");
		sb.append("\n");

		return sb.toString();
	}

	private ByteBuf getSendByteBuf(String message)
			throws UnsupportedEncodingException {

		byte[] req = message.getBytes("UTF-8");
		ByteBuf pingMessage = Unpooled.buffer();
		pingMessage.writeBytes(req);

		return pingMessage;
	}
}
