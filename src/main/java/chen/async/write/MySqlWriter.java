package chen.async.write;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlWriter implements IWriter {

	private final Connection conn;
	private final PreparedStatement stat;
	private final String sql = "insert into employee values (?, ?);";
	
	public MySqlWriter(final Connection conn) throws SQLException{
		if(conn == null){
			throw new IllegalArgumentException("Mysql Connection can not be Null");
		}
		this.conn = conn;
		stat = conn.prepareStatement(sql);
	}
	
	@Override
	public boolean write(IDataItem data){
		try {
			stat.clearParameters();
			String[] strs = (String[]) data.getData();
			stat.setString(1, strs[0]);
			stat.setString(2, strs[1]);
			stat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void close(){
		try {
			stat.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
