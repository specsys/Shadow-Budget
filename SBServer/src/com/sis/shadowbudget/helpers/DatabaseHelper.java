/**
 * 
 */
package com.sis.shadowbudget.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.sis.shadowbudget.security.SecurityHelper;

/**
 * @author Alex
 *
 */
public class DatabaseHelper {
	public static Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/sbudgetdb");
		return ds.getConnection();
		
	}
	
	public static UUID addUser( String userName, String password, String firstName, String lastName, String email) throws Exception{
		Connection conn = DatabaseHelper.getConnection();
		UUID uuid = UUID.randomUUID(); 
		PreparedStatement st = conn.prepareStatement("insert into users (userName, pwdHash, firstName, lastName, email, uuid) values (?,?,?,?,?,?)");
		st.setString(1, userName);
		st.setString(2, SecurityHelper.getHash(userName, password));
		st.setString(3, firstName);
		st.setString(4, lastName);
		st.setString(5, email);
		st.setString(6, uuid.toString());
		try {
			st.executeUpdate();
		} finally{
			st.close();
			conn.close();
		}
		return uuid;
	}
	
	public static Map<String,Object> getUser(String userName, String pwdHash )throws Exception{
		Map<String,Object> userMap = null;
		Connection conn = DatabaseHelper.getConnection();
		PreparedStatement st = conn.prepareStatement("select id, userName, firstName, lastName, email, uuid, su from users where userName = ? and pwdHash = ?");
		st.setString(1, userName);
		st.setString(2, pwdHash);
		try {
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				userMap = new HashMap<String,Object>();
				userMap.put("id", rs.getInt("id"));
				userMap.put("user", rs.getString("userName"));
				userMap.put("firstName", rs.getString("firstName"));
				userMap.put("lastName", rs.getString("lastName"));
				userMap.put("email", rs.getString("email"));
				userMap.put("uid", rs.getString("uuid"));
				userMap.put("su", rs.getString("su"));
			}
			rs.close();
		} finally {
			st.close();
			conn.close();
		}
		return userMap;
	}
	
	public static Set<String> getUserRoles( int userId ) throws Exception {
		Set<String> roleLst = new HashSet<String>();
		Connection conn = DatabaseHelper.getConnection();
		PreparedStatement st = conn.prepareStatement("select distinct role from roles where userId = ?");
		st.setInt(1, userId);
		try {
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				roleLst.add(rs.getString("role"));
			}
			rs.close();
		} finally {
			st.close();
			conn.close();
		}
		return roleLst;
	} 
}
