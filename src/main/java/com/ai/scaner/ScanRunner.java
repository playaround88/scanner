package com.ai.scaner;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanRunner extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ScanRunner.class);
	private ScanConfig config;
	private IScanService service;
	private BlockingQueue<Object> queue;
	private volatile boolean loop=true;
	
	public ScanRunner(ScanConfig scanConfig,BlockingQueue<Object> queue) {
		this.config = scanConfig;
		this.queue=queue;
		this.service = scanConfig.getScanService();
	}

	@Override
	public void run() {
		LOG.debug("扫描表线程开始"+this.config.getIdentifier());
		while(loop){
			try{
				List records = service.scan(this.config.getFetchSize());
				//如果没有取到数据，则休眠一段时间
				if(records==null || records.size()==0){
					Thread.currentThread().sleep(config.getSleepTime());
				}
				
				for (Object record : records) {
					// 更新状态
					int row = this.service.updateStatus(record);
					if (row == 1) {
						//阻塞方法
						queue.put(record);
					}
				}
				
			}catch (Exception e) {
				LOG.error("扫描线程出现异常(ScanRunner Catch Exception)",e);
			}
		}
		
	}
	
	public void shutdown(){
		this.loop=false;
	}

}
