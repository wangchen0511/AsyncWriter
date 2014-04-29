package chen.async.write;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chen.async.write.IDataItem.Status;

/**
 * Not every case is good to use the async writer. Usually for the file, the
 * bufferedWriter is good enough. Since writing the content into the
 * BlockingQueue also wastes time. Also the second thread also decrease the
 * performance.
 * 
 * If we want to use Future in the write, we should create a new Linked List in
 * which each node can delete itself from the list
 * 
 * 
 * Useful Cases:
 * 
 * A lot of threads are trying to write to the same channel. The the dumping
 * speed is much higher than the Writing Speed.
 * 
 * DB/Network or multi-thread writing to the same local file.
 * 
 * @author adam701
 * 
 */

public class AsyncWriter {
	private final LinkedBlockingQueue<IDataItem> buffer = new LinkedBlockingQueue<>();
	private IWriter writer;
	private static Logger log = LoggerFactory.getLogger(AsyncWriter.class);
	private Thread thread;
	private IDataItem lastData;

	public AsyncWriter(final IWriter writer) {
		this.writer = writer;
		this.thread = Executors.defaultThreadFactory().newThread(
				new WriteTask());
		this.thread.start();
	}

	private class WriteTask implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					// Thread.sleep(100);
					lastData = buffer.take();
					synchronized (lastData) {
						if (lastData.getStatus() == Status.CANCELLED) {
							continue;
						}
						lastData.setStatus(Status.RUNNING);
					}
					writer.write(lastData);
					lastData.setStatus(Status.DONE);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.debug("Blocking Queue is interrupted!", e);
					return;
				}
			}
		}
	}

	public Future addData(IDataItem data) {
		buffer.add(data);
		return new FutureAdd(data);
	}

	public void close() {
		writer.close();
		thread.interrupt();
	}

	public boolean isDone() {
		if (buffer.size() == 0) {
			if (lastData == null
					|| (lastData.getStatus() != Status.WAITING && lastData
							.getStatus() != Status.RUNNING)) {
				return true;
			}
		}
		return false;
	}

	private class FutureAdd implements Future {

		IDataItem data;
		boolean isCancel = false;

		public FutureAdd(final IDataItem data) {
			this.data = data;
		}

		/*
		 * @see java.util.concurrent.Future#cancel(boolean)
		 */
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			synchronized (data) {
				if (data.getStatus() == Status.WAITING) {
					isCancel = true;
					data.setStatus(Status.CANCELLED);
				} else {
					isCancel = false;
				}
			}
			return isCancel;
		}

		/**
		 * We do not support cancel a write.
		 * 
		 */

		@Override
		public boolean isCancelled() {
			return isCancel;
		}

		@Override
		public boolean isDone() {
			return data.getStatus() == Status.DONE;
		}

		@Override
		public Object get() throws InterruptedException, ExecutionException {
			return null;
		}

		@Override
		public Object get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException,
				TimeoutException {
			return null;
		}

	}

}
