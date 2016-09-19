package net.jueb.util4j.lock.waitCondition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LiteBlockingWaitConditionStrategy implements WaitConditionStrategy
{
	protected final Logger log=LoggerFactory.getLogger(getClass());
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final AtomicBoolean signalNeeded = new AtomicBoolean(false);

    @Override
	public <T> T waitFor(WaitCondition<T> waitCondition) throws InterruptedException {
    	waitCondition.doComplete(); 
    	if (!waitCondition.isComplete())
         {
             lock.lock();
             try
             {
                 do
                 {
                	 waitCondition.doComplete();
                     if (waitCondition.isComplete())
                     {
                         break;
                     }
                     signalNeeded.getAndSet(true);
                     processorNotifyCondition.await();
                 }
                 while (!waitCondition.isComplete());
             }
             finally
             {
                 lock.unlock();
             }
         }
         return waitCondition.result();
	}
    
    @Override
	public <T> T waitFor(WaitCondition<T> waitCondition, long timeOut, TimeUnit unit) throws InterruptedException {
        long endTime=System.currentTimeMillis()+unit.toMillis(timeOut);
     	long waitTime = unit.toNanos(timeOut);
     	int errorCode=0;
         for (;;) 
         {
         	waitCondition.doComplete();
         	if(waitCondition.isComplete())
         	{
         		errorCode=1;
         		break;
         	}else
         	{
         		if (waitTime <= 0) 
                 {//时间到了可不等待,忽略结果
         			errorCode=2;
                     break;
                 }
             	lock.lock();
                 try {
                 	if(!waitCondition.isComplete())
                 	{
                 		waitCondition.doComplete();
                 	}
                 	if(waitCondition.isComplete())
                 	{
                 		errorCode=3;
                 		break;
                 	}
                 	//取出待执行的队列,阻塞waitTime
                 	signalNeeded.getAndSet(true);
                 	waitTime=processorNotifyCondition.awaitNanos(waitTime);
                 	waitCondition.doComplete();
                 	if(waitCondition.isComplete())
                 	{
                 		errorCode=4;
                 		break;
                 	}
                 } 
                 catch (Exception e) {
 					
 				}
                 finally {
                 	lock.unlock();
                 }
         	}
         }
         if(!waitCondition.isComplete())
         {
         	 if(System.currentTimeMillis()<endTime)
         	 {
         		 log.warn("非正常等待,errorCode="+errorCode);
         	 }
         }
         return waitCondition.result();
	}

    @Override
    public void signalAllWhenBlocking()
    {
        if (signalNeeded.getAndSet(false))
        {
            lock.lock();
            try
            {
                processorNotifyCondition.signalAll();
            }
            finally
            {
                lock.unlock();
            }
        }
    }
}