package chen.async.write;

public class DBWriteItem implements IDataItem {

	private final String[] strs;
	
	public DBWriteItem(String id, String name){
		this.strs = new String[]{id, name};
	}
	
	@Override
	public Object getData() {
		return strs;
	}

}
