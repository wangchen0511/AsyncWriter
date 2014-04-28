package chen.async.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AFileWriter implements IWriter {

	private final File file;
	private final PrintWriter writer;
	
	public AFileWriter(String filePath, boolean isAppend) throws IOException{
		this.file = new File(filePath);
		if(file.exists() && file.isDirectory()){
			throw new IllegalArgumentException("The path provided is a directory!");
		}
		if(!file.exists()){
			file.createNewFile();
		}
		this.writer = new PrintWriter(new BufferedWriter(new FileWriter(file, isAppend)));
	}
	
	@Override
	public boolean write(IDataItem data) {
		if(data instanceof LineOfStrDataItem){
			for(String str : (String[]) data.getData()){
				writer.print(str);
				writer.print(" ");
			}
			writer.println();
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void close() {
		writer.close();
	}

}
