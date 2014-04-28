package chen.async.write;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private AtomicBoolean isDone = new AtomicBoolean(true);
	private ReadWriteLock lock = new ReentrantReadWriteLock();

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
				lock.readLock().lock();
				try {
					IDataItem data = buffer.take();
					isDone.set(false);
					writer.write(data);
					isDone.set(true);
					;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.debug("Blocking Queue is interrupted!", e);
					return;
				} finally {
					lock.readLock().unlock();
				}
			}
		}
	}

	public void addData(IDataItem data) {
		buffer.add(data);
	}

	public void close() {
		writer.close();
		thread.interrupt();
	}

	public boolean isDone() {
		lock.writeLock().lock();
		try {
			if (buffer.size() == 0 && isDone.get()) {
				return true;
			}
			return false;
		} finally {
			lock.writeLock().unlock();
		}
	}

}
