package chen.async.write;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.testng.annotations.Test;


public class TestAsyncWriter {

	@Test
	public void testAsyncWriter() throws IOException, InterruptedException{
		IWriter writer = new AFileWriter("/Users/adam701/asyncTest.txt", true);
		IDataItem line = new LineOfStrDataItem(new String[]{"hello", "world", "test!", "chen"});
		AsyncWriter asyncWriter = new AsyncWriter(writer);
		for(int i = 0; i < 10; i++){
			asyncWriter.addData(line);
		}
		Thread.sleep(1000);
		asyncWriter.close();
	}
	
	@Test
	public void performanceTestAsyncWriter() throws IOException, InterruptedException{
		long start = System.nanoTime();
		IWriter writer = new AFileWriter("/Users/adam701/asyncTest1.txt", false);
		IDataItem line = new LineOfStrDataItem(new String[]{"hello", "world", "test!", "chen"});
		AsyncWriter asyncWriter = new AsyncWriter(writer);
		for(int i = 0; i < 10000; i++){
			asyncWriter.addData(line);
		}
		//while(!asyncWriter.isDone()){
		//}
		long stop = System.nanoTime();
		System.out.println("Async Time is " + (stop -start));
		Thread.sleep(1000);
		asyncWriter.close();
	}
	
	/**It shows that the async writer has worse performance than the normal Printer even we do not use bufferedWriter inside. 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	@Test
	public void performanceTestNormalWriter() throws IOException, InterruptedException{
		long start = System.nanoTime();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/adam701/asyncTest2.txt"), false)));
		//PrintWriter writer = new PrintWriter(new FileWriter(new File("/Users/adam701/asyncTest2.txt"), false));

		IDataItem line = new LineOfStrDataItem(new String[]{"hello", "world", "test!", "chen"});
		String[] strs = new String[]{"hello", "world", "test!", "chen"};
		for(int i = 0; i < 10000; i++){
			for(String str : strs){
				writer.print(str);
				writer.print(" ");
			}
			writer.println();
		}
		long stop = System.nanoTime();
		System.out.println("Normal Time is " + (stop -start));
		Thread.sleep(1000);
		writer.close();
	}
	
}
