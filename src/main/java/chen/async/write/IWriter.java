package chen.async.write;

public interface IWriter {

	
	public boolean write(IDataItem data);

	public void close();
}
