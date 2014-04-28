package chen.async.write;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.testng.annotations.Test;

public class TestAsyncDBWriter {

	@Test
	public void testAsyncDB() throws SQLException, InterruptedException{
		Connection conn = new CreateConnection().createConnection();
		PreparedStatement stat=  conn.prepareStatement("truncate table employee");
		stat.executeUpdate();
		long start = System.nanoTime();
		IWriter writer = new MySqlWriter(conn);
		AsyncWriter asyncWriter = new AsyncWriter(writer);
		for(int i = 0; i < 1000; i++){
			String[] strs = new String[]{String.valueOf(i), "test"};
			asyncWriter.addData(new LineOfStrDataItem(strs));
		}
		
		long stop = System.nanoTime();
		System.out.println("Async Time is " + (stop -start));
		while(!asyncWriter.isDone()){
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		asyncWriter.close();
	}
	
	
	@Test
	public void testNormalDB() throws SQLException, InterruptedException{
		Connection conn = new CreateConnection().createConnection();
		PreparedStatement stat=  conn.prepareStatement("truncate table employee");
		stat.executeUpdate();
		stat.close();
		long start = System.nanoTime();
		stat = conn.prepareStatement("insert into employee values (?, ?);");
		for(int i = 0; i < 1000; i++){
			stat.clearParameters();
			stat.setString(1, String.valueOf(i));
			stat.setString(2, "test");
			stat.executeUpdate();
		}
		
		long stop = System.nanoTime();
		System.out.println("Normal Time is " + (stop -start));
		stat.close();
		conn.close();
	}
}
