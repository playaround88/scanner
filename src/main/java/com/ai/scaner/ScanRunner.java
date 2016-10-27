package com.ai.scaner;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanRunner implements Runnable{
	private static final Logger LOG=LoggerFactory.getLogger(ScanRunner.class);
	private ScanConfig config;
	private IScanService service;
	private Random random=new Random();

	public ScanRunner(ScanConfig scanConfig) {
		this.config=scanConfig;
		this.service=scanConfig.getScanService();
	}

	@Override
	public void run() {
		LOG.debug("扫描表记录开始");
		List records=service.scan(Integer.valueOf(this.config.getFetchSize()));
		LOG.debug("取到"+records.size()+"条记录");
		for(Object record : records){
			//更新状态
			int row=this.service.updateStatus(record);
			if(row == 1){
				//简单随机分发，如果队列已满，则再次随机分发
				boolean result=false;
				while(!result){
					int index=random.nextInt(this.config.getPoolSize());
					result=this.config.getQueues().get(index).offer(record);
				}
			}
		}
	}

}
