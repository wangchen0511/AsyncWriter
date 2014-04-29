package chen.async.write;

/**Every DataItem contains a set of data which can be consumed by writer.
 * 
 * 
 * Make sure the set/get stauts is thread-safe.
 * 
 * @author adam701
 *
 * @param <T>
 */

public interface IDataItem {
	
	public Object getData();
	
	public Status getStatus();
	
	public void setStatus(final Status status);
	
	public static enum Status{
		DONE, RUNNING, WAITING, CANCELLED;
	}
}
