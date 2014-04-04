package org.esgi.http.thread;

import java.util.PriorityQueue;

public class FixedThreadPool {
	private PriorityQueue<Runnable> queue = new PriorityQueue<Runnable>();
	private PoolWorker[] workers;
	private Object lock = new Object();

	public FixedThreadPool(int len){
		workers = new PoolWorker[len];
		for (int i=0;i<len;i++){
			workers[i]= new PoolWorker(this);
			workers[i].start();
		}
	}

	public void addJob(Runnable job) {
		synchronized (lock) {
			queue.add(job);
			lock.notify();
		}
	} 
	Runnable getJob() throws InterruptedException {
		synchronized (lock) {
			if (0 == queue.size())
				lock.wait();
			return queue.poll();
		}
	}
}
