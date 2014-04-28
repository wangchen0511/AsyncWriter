package chen.async.write;

public class LineOfStrDataItem implements IDataItem {

	private String[] data;
	
	public LineOfStrDataItem(final String[] data){
		this.data = data;
	}
	
	@Override
	public Object getData() {
		return data;
	}

}
