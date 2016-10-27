package com.ai.scaner;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealRunner implements Runnable{
	private static final Logger LOG=LoggerFactory.getLogger(DealRunner.class);
	private int index=0;
	private ScanConfig config;
	private IDealService dealService;
	private volatile boolean startFlag=true;
	

	public DealRunner(int i, ScanConfig config) {
		this.index=i;
		this.config=config;
		this.dealService=config.getDealService();
	}

	@Override
	public void run() {
		while(startFlag){
			try {
				Object record=config.getQueues().get(index).poll(this.config.getBlockTimeout(), TimeUnit.SECONDS);
				if(record!=null){
					this.dealService.deal(record);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("从队列取数据异常");
			}
		}
		
	}
	
	public void shutdown(){
		this.startFlag=false;
	}

}
