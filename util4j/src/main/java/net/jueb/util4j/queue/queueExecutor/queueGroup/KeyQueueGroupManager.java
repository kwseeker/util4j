package net.jueb.util4j.queue.queueExecutor.queueGroup;

import java.util.Iterator;

import net.jueb.util4j.queue.queueExecutor.queue.QueueExecutor;

/**
 * 队列组
 * @author juebanlin
 */
public interface KeyQueueGroupManager extends Iterable<QueueExecutor>{
	
	/**
	 * 设置队列别名
	 * @param solt
	 * @param alias
	 */
	public void setAlias(String key,String alias);
	
	/**
	 * 获取队列别名
	 * @param solt
	 */
	public String getAlias(String key);
	
	/**
	 * 获取任务执行器,此队列的名字等于队列别名
	 * @param queue
	 * @return
	 */
	public QueueExecutor getQueueExecutor(String key);

	/**
	 * 迭代执行器
	 */
	@Override
	Iterator<QueueExecutor> iterator();
	
	public long getToalCompletedTaskCount();
	
	public long getToalCompletedTaskCount(String key);
	
	public void setGroupEventListener(KeyGroupEventListener listener);
	
	public static interface KeyGroupEventListener{
		/**
		 * 某队列的处理任务
		 * @param task
		 */
		public void onQueueHandleTask(String key,Runnable handleTask);
	}
}
