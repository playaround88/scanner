package com.ai.scaner;

import java.io.Serializable;

/**
 * 扫描程序的配置封装; 不支持动态修改参数
 * 
 * @author wutb
 *
 */
public class ScanConfig implements Serializable {
	private static final long serialVersionUID = 2140695274817677757L;
	// 扫描任务标识符
	private String identifier;
	// 扫描数据，每次取记录的条数
	private int fetchSize = 100;
	// 扫描不到数据时，阻塞时间，单位秒
	private int sleepTime = 10;
	// 处理线程池大小
	private int poolSize = 3;
	// 队列长度
	private int queueSize = 100;
	//
	private IScanService scanService;
	private IDealService dealService;

	//构造函数
	public ScanConfig(String identifier, int fetchSize, int sleepTime, int poolSize, int queueSize,
			IScanService scanService, IDealService dealService) {
		super();
		this.identifier = identifier;
		this.fetchSize = fetchSize;
		this.sleepTime = sleepTime;
		this.poolSize = poolSize;
		this.queueSize = queueSize;
		this.scanService = scanService;
		this.dealService = dealService;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setScanService(IScanService scanService) {
		this.scanService = scanService;
	}

	public void setDealService(IDealService dealService) {
		this.dealService = dealService;
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

	public int getPoolSize() {
		return poolSize;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	@Override
	public String toString() {
		return "ScanConfig [identifier=" + identifier + ", fetchSize=" + fetchSize + ", sleepTime=" + sleepTime
				+ ", poolSize=" + poolSize + ", queueSize=" + queueSize + ", scanService=" + scanService
				+ ", dealService=" + dealService + "]";
	}
	
}
