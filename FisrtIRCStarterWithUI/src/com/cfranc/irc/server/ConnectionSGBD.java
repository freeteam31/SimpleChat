package com.cfranc.irc.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.JDBC;

public class ConnectionSGBD {

	private java.sql.Connection conn = null;
	private java.sql.Statement  smt  = null;
	
	public java.sql.Connection getConn() {
		return conn;
	}

	public java.sql.Statement getSmt() {
		return smt;
	}

	public ConnectionSGBD(String url, String username, String password) {

		try {
			if (url.indexOf("mysql") != -1) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (java.lang.ClassNotFoundException e) {
					System.err.print("ClassNotFoundException: ");
					System.err.println(e.getMessage());
				}

				conn = DriverManager.getConnection(url, username, password);
				System.out.println("OK connexion réussie...");
				smt = conn.createStatement();

			} else if (url.indexOf("sqlite") != -1) {
				System.out.println("Ouverture de la base url : " + url);
				if (JDBC.isValidURL(url)) {
				
					if (conn != null) {
						conn.close();
					}

					conn = DriverManager.getConnection(url);
					System.out.println("OK connexion réussie...");
					smt = conn.createStatement();					  					
				}
			}
			;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ConnectionSGBD() {
		this("jdbc:mysql://localhost:3306/test", "root", "root");
	}

	public boolean executeInsert(String query) {
		try {
			return smt.execute(query);
		} catch (SQLException e) {
			System.err.println(query);
			e.printStackTrace();
			return false;
		}
	}

	public int executeUpdate(String query) {
		try {
			return smt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getSQLState());
			System.err.println(e.getMessage());
			return -1;
		}
	}

	public ResultSet executeSelect(String query) {
		try {
			return smt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
