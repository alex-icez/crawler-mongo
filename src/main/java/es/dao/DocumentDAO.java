package es.dao;

import model.Document;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.gson.Gson;


public class DocumentDAO {
	
	private static Gson GSON = new Gson();
	public static final String INDEX = "habr";
	public static final String TYPE = "document";
	
	private Client client;
	
	public DocumentDAO(Client client)
	{
		this.client = client;
	}
	
	public synchronized void saveDocument(Document document)
	{
		try {
			client
				.prepareIndex(INDEX, TYPE, document.getUuid())
				.setSource(DocumentDAO.buildJson(document))
				.execute()
				.actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Document getDocument(String uuid)
	{
		GetResponse getResponse = client.prepareGet(INDEX, TYPE, uuid).execute().actionGet();
		return DocumentDAO.buildDocument(getResponse.getSourceAsString());
	}
			
	public void removeDocument(String uuid)
	{
		client.prepareDelete(INDEX, TYPE, uuid).execute().actionGet();
	}
	
	public void removeDocument(Document document)
	{
		this.removeDocument(document.getUuid());
	}
	
	public boolean checkDupl(String url) {
        SearchResponse searchResponse = client
                .prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.fieldQuery("url", "\"" + url.replaceAll("\"", "") + "\""))
                .setSize(1)
                .execute()
                .actionGet();
        return searchResponse.getHits().getTotalHits() > 0;
    }
	
	static public String buildJson(Document document)
	{
		return GSON.toJson(document, Document.class);
	}
	
	static public Document buildDocument(String json)
	{
		try {
			return GSON.fromJson(json, Document.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
