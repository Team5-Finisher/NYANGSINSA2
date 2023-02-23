package com.nss.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// Util 클래스
// : 객체 생성 xxx  --->> "객체와 무관하게" == static
// : 메소드(로직)을 제공하는 것이 목표 
public class JDBCUtil {
	static final String driverName = "com.mysql.cj.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost/wjhdb"; // 경로
	static final String user = "root";
	static final String passwd = "1234";

	public static Connection connect() {
		Connection conn = null;
		try {
			Class.forName(driverName); // 드라이버 로드
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("로그: conn 수행실패");
		} finally {
			System.out.println("로그 : conn 수행완료");
		}
		return conn;
	}

	public static void disconnect(Connection conn, PreparedStatement pstmt) {
		try {
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("   로그: disconnect() 수행완료");
		}
	}

}