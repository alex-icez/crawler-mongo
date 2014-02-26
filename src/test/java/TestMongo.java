import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;

import model.Document;
import mongo.MongoUtil;

import junit.framework.TestCase;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

import es.ConvertElasticSearchToMongo;
import es.ElasticSearchUtil;
import es.dao.DocumentDAO;

public class TestMongo extends TestCase {

	public void test() throws UnknownHostException {
		
		//mongo.dao.DocumentDAO dao = new mongo.dao.DocumentDAO(MongoUtil.getDB());
		//dao.removeAll();
		ConvertElasticSearchToMongo.convert();
		
		
	}
}
