package com.seveniu.common.queue;

import com.seveniu.common.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hao
 */
public abstract class BaseMessagingService implements MessageQueue {

    private static final Logger logger = LoggerFactory.getLogger(BaseMessagingService.class);

    public static final String QUEUE_NAME_PREFIX = "dhlz:queue:";
    //==========================================================================
    // Instance Fields and Methods
    //==========================================================================

    public String getQueueKey(String queueName) {
        String queueKey = queueName;
        if (!queueName.startsWith(QUEUE_NAME_PREFIX)) {
            queueKey = QUEUE_NAME_PREFIX + queueName;
        }
        return queueKey;
    }

    public String getMessageString(Object message) {
        return Json.toJson(message);
    }

    public void queue(String queueName, Object message) {
        String stringMsg = getMessageString(message);

        String queueKey = getQueueKey(queueName);

        doQueue(queueKey, stringMsg);

        if (logger.isDebugEnabled()) {
            logger.debug("Queued: key=" + queueKey + ", message=" + stringMsg);
        }
    }

    public abstract void doQueue(String queueKey, String stringMsg);


    public <T> T dequeue(String queueName, Class<T> clazz) {
        T message;
        String queueKey = getQueueKey(queueName);
        String stringMsg = doDequeue(queueKey);

        if (String.class.equals(clazz)) {
            message = Json.toObject(stringMsg, clazz);
        } else {
            message = Json.toObject(stringMsg, clazz);
        }

        if (logger.isDebugEnabled() && message != null) {
            logger.debug("Dequeued: key=" + queueKey + ", message=" + message);
        }
        return message;
    }


    public abstract String doDequeue(String queueKey);
}
