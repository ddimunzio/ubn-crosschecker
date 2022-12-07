package org.lw5hr.adif.derby;

import java.sql.*;


public class DerbyManager {

	private static DerbyManager instance = null;

	public static DerbyManager getInstance() {
		if (instance == null) {
			instance = new DerbyManager();
			instance.initializeDb();
		}
		return instance;
	}

	private DerbyManager() {
		// TODO Auto-generated constructor stub
	}

	private static String createQrzInfo = "CREATE TABLE QRZ_INFO  (QRZ_USER VARCHAR(255) NOT NULL,PASSWORD VARCHAR(255) NOT NULL) ";
	private static String createStationData = "CREATE TABLE SATION_DATA  (CALL_ID VARCHAR(255) NOT NULL, LOCATOR VARCHAR(10), LATITUDE DOUBLE  ,LONGITUDE DOUBLE ,QRZ_USER VARCHAR(255),QRZ_PASS VARCHAR(255))";
	private static String createCtyDat = "CREATE TABLE CTY_DAT  (PREFIX VARCHAR(255) NOT NULL, NAME VARCHAR(255), LATITUDE DOUBLE  ,LONGITUDE DOUBLE ,QRZ_USER VARCHAR(255),QRZ_PASS VARCHAR(255))";

	// private static String createString3 =
	// "CREATE INDEX callIndex ON CALL_LOCATOR(CALL_ID)";

	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String dbName = new java.io.File("").getAbsolutePath() + "\\poseditordb";

	public void relaseConnection(Connection conn) throws SQLException {
		conn.close();
		conn = null;
	}

	public Connection getDbConnection() throws SQLException {
		Connection conn = null;
		try {

			String connectionURL = "jdbc:derby:" + dbName + ";create=true";

			Class.forName(driver);
			conn = DriverManager.getConnection(connectionURL);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public boolean initializeDb() {
		try {
			if (!isdabaseInitialized()) {
				getDbConnection().createStatement().execute(createQrzInfo);
				getDbConnection().createStatement().execute(createStationData);
				//initStationData();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return false;
	}

	public boolean isdabaseInitialized() throws SQLException {
		ResultSet result;
		boolean tableExists = false;
		DatabaseMetaData metadata = null;
		Connection conn = getDbConnection();
		metadata = conn.getMetaData();
		String[] names = { "TABLE" };
		ResultSet tableNames = metadata.getTables(null, "APP", null, names);
		while ((tableNames.next()) && (tableExists == false)) {

			if (tableNames.getString("TABLE_NAME").equalsIgnoreCase("QRZ_INFO")) {
				tableExists = true;
			}
		}

		relaseConnection(conn);
		return tableExists;
	}


	public String getCallLocator(String call) throws SQLException {
		String query = "SELECT * FROM CALL_LOCATOR WHERE CALL_ID=?";
		Connection con = getDbConnection();
		java.sql.PreparedStatement stm = con.prepareStatement(query);
		stm.setString(1, call);
		ResultSet rs = stm.executeQuery();
		String locator = null;
		if (rs.next()) {
			locator = rs.getString("LOCATOR");

		}
		return locator;
	}

	public void cleanDatabase() throws SQLException {
	}

}
