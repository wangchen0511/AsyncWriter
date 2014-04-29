package chen.async.write;

public class DBWriteItem implements IDataItem {

	private final String[] strs;
	private IDataItem.Status status = IDataItem.Status.WAITING;
	
	public DBWriteItem(String id, String name){
		this.strs = new String[]{id, name};
	}
	
	@Override
	public Object getData() {
		return strs;
	}

	@Override
	public synchronized Status getStatus() {
		return this.status;
	}

	@Override
	public synchronized void setStatus(final Status status) {
		this.status = status;
	}

}
