package chen.async.write;

public class LineOfStrDataItem implements IDataItem {

	private String[] data;
	private IDataItem.Status status = IDataItem.Status.WAITING;
	
	public LineOfStrDataItem(final String[] data){
		this.data = data;
	}
	
	@Override
	public Object getData() {
		return data;
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
