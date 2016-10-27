package com.ai.scaner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 扫描程序的配置封装;
 * 不支持动态修改参数
 * @author wutb
 *
 */
public class ScanConfig implements Serializable {
	private static final long serialVersionUID = 2140695274817677757L;
	//扫描数据，每次取记录的条数
	private int fetchSize = 100;
	//扫描周期，单位秒
	private long fetchPeriod = 20;
	//处理线程池大小
	private int poolSize = 10;
	//处理线程监听队列，阻塞时间
	private int blockTimeout=10;
	//
	private IScanService scanService;
	private IDealService dealService;
	//数据缓存队列列表
	private List<LinkedBlockingQueue> queues = new ArrayList<LinkedBlockingQueue>();

	public ScanConfig(int fetchSize, long fetchPeriod, int poolSize, int blockTimeout,
			IScanService scanService, IDealService dealService) {
		this.fetchSize=fetchSize;
		this.fetchPeriod=fetchPeriod;
		this.poolSize=poolSize;
		this.blockTimeout=blockTimeout;
		//
		this.scanService=scanService;
		this.dealService=dealService;
		
		//完成队列列表的初始化
		for(int i=0;i<poolSize;i++){
			LinkedBlockingQueue queue=new LinkedBlockingQueue();
			queues.add(queue);
		}
	}

	public List<LinkedBlockingQueue> getQueues() {
		return queues;
	}

	public IScanService getScanService() {
		return scanService;
	}

	public IDealService getDealService() {
		return dealService;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public long getFetchPeriod() {
		return fetchPeriod;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public int getBlockTimeout() {
		return blockTimeout;
	}

}
