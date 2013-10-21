package geowimp;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class StreamingData {
	Connection conn;
	PreparedStatement stmnt;
	ResultSet result;
	DatabaseConnector dbc; 
	ArrayList<GeoStreamEntry> gp;
	
	
	StreamingData() {
		conn = null;
		stmnt = null;
		result = null;
		dbc = new DatabaseConnector();
	}
		ArrayList<GeoStreamEntry> getGeoStreams(String artnum) {
			try {
				conn = dbc.getConnection();
				if(conn != null){
					System.out.println("Connection retrieved");
				}
			} catch (SQLException e) {
				System.out.println("Connection Error");
				e.printStackTrace();
			}
		
			if(conn != null){
				try {
					String sql = "SELECT PS_COUNT, PNUM, pp.lat LAT, pp.lon LON, pp.poststad AS PLACE " +
							     "FROM ( SELECT COUNT(*) as ps_count, mc.POSTNUMMER_BESOK as pnum " +
									"FROM WIMP.ODS_STREAMING ods " +
									"INNER JOIN WIMP.MD_CUSTOMER mc ON ods.sid_customer = mc.sid_customer " +
									"WHERE ods.sid_artist = ? " +
									"GROUP BY mc.POSTNUMMER_BESOK) pa " +
									"LEFT JOIN WIMP.POSTPLACE pp ON pa.pnum = pp.postnr ";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, artnum);
					stmnt.executeUpdate();
					
					
					
					
				} catch (SQLException e) {
					System.out.println("Could not create statement");
					e.printStackTrace();
				}
			}
			if( conn != null && stmnt != null){
				try {
					stmnt.execute();
					result = stmnt.getResultSet();
				} catch (SQLException e) {
					System.out.println("Could not perform query");
					e.printStackTrace();
				}				
			}
		
			if(result != null){
				gp = new ArrayList<GeoStreamEntry>();
				try {
					while(result.next()){
						int pscount = result.getInt("PS_COUNT");
						int pnum = result.getInt("PNUM");
						float lat = result.getFloat("LAT");
						float lon = result.getFloat("LON");
						String place = result.getString("PLACE");
						System.out.println("Got: "+ place+ " at pcode: "+pnum+" with lon and lat"+lon+" : "+lat+"  count: "+pscount);
						
						
						//Setting new values for max and min value if these are not defined
						if (pnum < GeoDraw.minnum) {
							System.out.println("MINNUM was :" + GeoDraw.minnum + "  now: "+ pnum);
							GeoDraw.minnum = pnum;
						}
						if (pnum > GeoDraw.maxnum) {
							System.out.println("MAXNUM was :" + GeoDraw.maxnum + "  now: "+ pnum);
							GeoDraw.maxnum = pnum;	
						}
						
						//Adding the entry to the array containing the location
						GeoStreamEntry e = new GeoStreamEntry(lat, lon, place, pscount);
						gp.add(e);
						
					}
				} catch (SQLException e) {
					System.out.println("Could not get data");
					e.printStackTrace();
				}
			}
			return gp;
		
		
		}
		
		String GetSIDFromName(String artname) {
			String answer = "-1"; // Return value in case no result is returned
			
			try {
				conn = dbc.getConnection();
				if(conn != null){
					System.out.println("Connection retrieved");
				}
			} catch (SQLException e) {
				System.out.println("Connection Error");
				e.printStackTrace();
			}
		
			if(conn != null){
				try {
					String sql = "SELECT sid_artist as SID_ARTIST FROM " +
							" (SELECT sid_artist FROM WIMP.MD_ARTIST WHERE lower(artistname) LIKE lower(?)) WHERE rownum = 1";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, artname);
					stmnt.executeUpdate();
					
					
				} catch (SQLException e) {
					System.out.println("Could not create statement");
					e.printStackTrace();
				}
			}
			if( conn != null && stmnt != null){
				try {
					stmnt.execute();
					result = stmnt.getResultSet();
				} catch (SQLException e) {
					System.out.println("Could not perform query");
					e.printStackTrace();
				}				
			}
			
			
		
			if(result != null){
				try {
					while(result.next()){
						answer = result.getString("SID_ARTIST");
						
					}
				} catch (SQLException e) {
					System.out.println("Could not get data");
					e.printStackTrace();
				}
			}
			return answer;
		
		
		}
		
		class DatabaseConnector {
			public Connection getConnection() throws SQLException {
				Connection conn  = null;
				conn = DriverManager.getConnection("dbconnstring","username","password");
				return conn;
			}
			
		}
		

}