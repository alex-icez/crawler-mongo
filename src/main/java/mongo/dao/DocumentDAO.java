package mongo.dao;

import model.Document;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;


public class DocumentDAO {
	private static Gson GSON = new Gson();
	public static String TABLE = "document";
	
	private DBCollection table = null; 

	public DocumentDAO(DBCollection table)
	{
		this.table = table; 
	}
	
	public DocumentDAO(DB db)
	{
		this.table = db.getCollection(TABLE); 
	}
	
	public synchronized void saveDocument(Document document)
	{
		table.insert(buildDBObject(document));
	}
	
	public Document getDocument(String uuid)
	{
		BasicDBObject query = new BasicDBObject();
		query.put("uuid", uuid);
		DBCursor cursor = table.find(query);
		if (cursor.hasNext())
			return buildDocument(cursor.next());
		return null;
	}
			
	public void removeDocument(String uuid)
	{
		BasicDBObject query = new BasicDBObject();
		query.put("uuid", uuid);
		table.remove(query);
	}
	
	public void removeDocument(Document document)
	{
		removeDocument(document.getUuid());
	}
	
	public void removeAll()
	{
		DBCursor cursor = table.find();
		while(cursor.hasNext()) 
			removeDocument(buildDocument(cursor.next()));
	}
	
	public boolean checkDupl(String url) {
		BasicDBObject query = new BasicDBObject();
		query.put("url", url);
		DBCursor cursor = table.find(query);
		return cursor.hasNext();
    }
	
	static public DBObject buildDBObject(Document document)
	{
		return (DBObject)JSON.parse(GSON.toJson(document));
	}
	
	static public Document buildDocument(DBObject dbObj)
	{
		return GSON.fromJson(dbObj.toString(), Document.class);
	}
}
