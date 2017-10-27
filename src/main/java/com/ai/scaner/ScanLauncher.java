package com.ai.scaner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 内存扫描表启动程序
 * 
 */
public class ScanLauncher {
	private static final Logger LOG = LoggerFactory.getLogger(ScanLauncher.class);
	private ScanConfig config;
	private ScanRunner scanRunner;
	// deal线程句柄，用于关闭线程
	private List<DealRunner> tasks = new ArrayList<DealRunner>();
	private BlockingQueue<Object> queue;

	public ScanLauncher(ScanConfig scanConfig) {
		this.config = scanConfig;
	}
	/**
	 * 初始化方法
	 */
	public void init() {
		LOG.info("启动扫描任务："+config.getIdentifier());
		LOG.debug("扫描任务配置" + config.toString());
		
		LOG.debug("创建数据共享队列");
		this.queue=new ArrayBlockingQueue<Object>(config.getQueueSize());
		
		// 启动读取数据操作线程
		LOG.debug("启动数据库扫描线程..."+config.getIdentifier());
		ScanRunner scanRunner = new ScanRunner(config,queue);
		scanRunner.start();
		LOG.debug("启动数据库扫描线程...END");

		// ----------------无聊分割线

		LOG.debug("启动处理线程池..."+config.getIdentifier());
		LOG.debug("线程池大小：" + this.config.getPoolSize());
		// 启动一批处理线程
		for (int i = 0; i < this.config.getPoolSize(); i++) {
			DealRunner runner = new DealRunner(config,queue);
			runner.start();
			tasks.add(runner);
		}
		LOG.debug("启动处理线程池...END");
	}

	/**
	 * 销毁方法
	 */
	public void destroy() {
		LOG.debug("read push thread is shutdown...");
		// 先关闭数据库扫描数据线程
		scanRunner.shutdown();

		// 要处理完成所有的队列任务再关闭；遍历读取每个队列的长度；
		LOG.debug("阻塞等待queue中的数据处理完成...");
		try {
			while (!this.queue.isEmpty()) {
				Thread.currentThread().sleep(1000);
			}
		} catch (InterruptedException e) {
			LOG.error("阻塞等待queue中的数据处理完成出现异常，", e);
		}
		
		//关闭所有task
		LOG.debug("shutdown all deal thread...");
		for(int i=0;i<tasks.size();i++){
			tasks.get(i).shutdown();
		}
		LOG.debug("deal thread all shutdowned!");
	}
}
