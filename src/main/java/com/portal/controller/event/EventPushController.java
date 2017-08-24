package com.portal.controller.event;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@Controller
public class EventPushController {
	
	private static final ILogger logger = LogUtil.getLogger(LogModule.EventPush, EventPushController.class);
	
	private final int Timeout = 1800 * 1000;
	
	private static AtomicLong id = new AtomicLong(0);
	
	@RequestMapping(value="/sse/event", produces="text/event-stream")
	public void eventPush(HttpServletRequest req, HttpServletResponse response){
		response.setContentType("text/event-stream");
		response.addHeader("Cache-Control", "no-cache");
		//response.addHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
//		String uid = req.getParameter("uid");
		
		final AsyncContext ac = req.startAsync();
		//long defaultTimeout = ac.getTimeout();
		logger.info("Default timeout is "+Timeout);
	    ac.setTimeout(Timeout);
	    final EventReceiver receiver = new EventReceiver(ac);
	    receiver.setId(id.getAndIncrement());
//	    receiver.setUserId(uid.substring(0, uid.indexOf("|")));
//	    receiver.setUserRole(Role.getRole(uid.substring(uid.indexOf("|")+1)));
	    receiver.setSessionId(req.getSession(true).getId());
	    EventPushService.getInstance().addEventReceiver(receiver);
	    logger.info("Add event receiver is "+receiver.toString() + ", " + EventPushService.getInstance().getEventReceiverList().size());
	    ac.addListener(new AsyncListener() {
			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				logger.info("onComplete, remove event receiver: " + receiver.toString());
				EventPushService.getInstance().removeEventReceiver(receiver);
				logger.info("Event receiver size is "+ EventPushService.getInstance().getEventReceiverList().size());
			}
			@Override
			public void onError(AsyncEvent event) throws IOException {
				logger.info("onError, remove event receiver: " + receiver.toString());
				EventPushService.getInstance().removeEventReceiver(receiver);
				logger.info("Event receiver size is "+ EventPushService.getInstance().getEventReceiverList().size());
			}
			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {
				logger.info("onStartAsync " + receiver.toString());
				EventPushService.getInstance().removeEventReceiver(receiver);
				logger.info("Event receiver size is "+ EventPushService.getInstance().getEventReceiverList().size());
			}
			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				logger.info("onTimeout, remove event receiver: " + receiver.toString());
				EventPushService.getInstance().removeEventReceiver(receiver);
				logger.info("Event receiver size is "+ EventPushService.getInstance().getEventReceiverList().size());
			}
	    });
	}
}
