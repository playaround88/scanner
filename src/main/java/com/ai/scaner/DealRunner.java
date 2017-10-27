package com.ai.scaner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealRunner extends Thread{
	private static final Logger LOG=LoggerFactory.getLogger(DealRunner.class);
	private ScanConfig config;
	private BlockingQueue<Object> queue;
	private IDealService dealService;
	private volatile boolean loop=true;
	

	public DealRunner( ScanConfig config,BlockingQueue<Object> queue) {
		this.config=config;
		this.queue=queue;
		this.dealService=config.getDealService();
	}

	@Override
	public void run() {
		while(loop){
			try {
				Object record=this.queue.take();
				this.dealService.deal(record);
			} catch (Exception e) {
				LOG.error("从队列取数据异常:",e);
			}
		}
	}
	
	public void shutdown(){
		this.loop=false;
	}

}
