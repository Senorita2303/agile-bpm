package com.dstz.component.mq.engine.producer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.dstz.base.common.async.ContextCleanTaskDecorator;
import com.dstz.base.common.async.ContextDuplicationTaskDecorator;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.TaskDecoratorUtils;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.consumer.AbstractMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Asynchronous JmsProducer
 *
 */
public class AsyncJmsProducer extends AbstractMessageQueue implements JmsProducer, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(AsyncJmsProducer.class);

	private ThreadPoolExecutor threadPoolExecutor;

	@Override
	public void afterPropertiesSet() {
		int poolSize = Math.min(RuntimeUtil.getProcessorCount() + 1, 20);
		threadPoolExecutor = ExecutorBuilder.create()
				.setCorePoolSize(1)
				.setMaxPoolSize(poolSize)
				.setAllowCoreThreadTimeOut(true)
				.setThreadFactory(ThreadUtil.newNamedThreadFactory("jmsproducer-async-pool-", false))
				.setWorkQueue(new LinkedBlockingQueue<>(512))
				.setHandler(RejectPolicy.BLOCK.getValue())
				.build();
	}

	@Override
	public void destroy(){
		if (threadPoolExecutor != null) {
			threadPoolExecutor.shutdown();
		}
	}

	@Override
	public void sendToQueue(JmsDTO message) {
		sendToQueue(Collections.singletonList(message));
	}

	@Override
	public void sendToQueue(List<JmsDTO> messages) {
		if(CollUtil.isNotEmpty(messages)) {
			Queue<JmsDTO<Serializable>> dataList = new LinkedList<>(CastUtils.cast(messages));
			threadPoolExecutor.submit(TaskDecoratorUtils.decorate(new JmsMessageHandleTask(dataList), ContextCleanTaskDecorator.INSTANCE, ContextDuplicationTaskDecorator.INSTANCE));
		}
	}

	private class JmsMessageHandleTask implements Runnable {

		private final Queue<JmsDTO<Serializable>> dataList;

		public JmsMessageHandleTask(Queue<JmsDTO<Serializable>> dataList) {
			this.dataList = dataList;
		}

		@Override
		public void run() {
			JmsDTO<Serializable> jmsDTO;
			while ((jmsDTO = dataList.poll()) != null) {
				invokeJmsHandler(jmsDTO);
			}
		}
	}
}
