package com.kkpush.push.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kkpush.push.domain.ScheduledPush;

public class PushFactory {
	
	private static Logger logger = LoggerFactory.getLogger(PushFactory.class);

	public static void buildScheduledPush(ScheduledPush scheduledPush) {
		try {
			ScheduledPushBuilder p = new ScheduledPushBuilder();
			p.build(scheduledPush);
		} catch (Exception e) {
			logger.error("build push exception: " + e.getMessage());
		}
	}
	
}
