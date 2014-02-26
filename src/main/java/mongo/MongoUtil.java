package mongo;

import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoUtil {
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("mongo");

	private static final String DB_URL = "connection.url";
	private static final String DB_DATABASE = "database";

	private static String Url = null;
	private static String Host = null;
	private static String Database = null;
	private static Integer Port = null;
	
	private static MongoClient mongoClient = null;
	private static DB db = null;

	public static synchronized DB getDB() throws UnknownHostException {
		if (db == null) {
			db = getClient().getDB(getDatabase());
		}
		return db;
	}
	
	public static synchronized MongoClient getClient() throws UnknownHostException {
		if (mongoClient == null) {
			mongoClient = new MongoClient(getHost(), getPort());
		}
		return mongoClient;
	}
	
	public static synchronized String getDatabase()
	{
		if (Database == null) {
			Database = RESOURCE_BUNDLE.getString(DB_DATABASE);
		}
		return Database;
	}
	
	private static synchronized String getUrl()
	{
		if (Url == null) {
			Url = RESOURCE_BUNDLE.getString(DB_URL);
		}
		return Url;
	}
	
	public static synchronized String getHost()
	{
		if (Host == null) {
			try {
				Host = getUrl().split("[:]")[0];
			} catch (Exception e) {
				e.printStackTrace();
				Host = "localhost";
			}
		}
		return Host;
	}
	
	public static synchronized Integer getPort()
	{
		if (Port == null) {
			try {
				Port = Integer.parseInt(getUrl().split("[:]")[1]);
			}catch (Exception e) {
				e.printStackTrace();
				Port = 27017;
			}
		}
		return Port;
	}
}
