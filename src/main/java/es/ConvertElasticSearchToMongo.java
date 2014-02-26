package es;

import java.net.UnknownHostException;

import model.Document;
import mongo.MongoUtil;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import es.dao.DocumentDAO;

public class ConvertElasticSearchToMongo {
	public static void convert() throws UnknownHostException {
		Client client = ElasticSearchUtil.getClient();
		mongo.dao.DocumentDAO dao = new mongo.dao.DocumentDAO(MongoUtil.getDB());
		
		SearchResponse scrollResp = client.prepareSearch(DocumentDAO.INDEX)
				.setSearchType(SearchType.SCAN).setTypes(DocumentDAO.TYPE)
				.setScroll(new TimeValue(60000)).setSize(50).execute()
				.actionGet();
		long n = 0;

		while (true) {
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
					.setScroll(new TimeValue(600000)).execute().actionGet();
			
			for (SearchHit hit : scrollResp.getHits()) {
				Document doc = DocumentDAO.buildDocument(hit.getSourceAsString());
				if (doc == null) {
					System.out.println("error encode!");
					continue;
				}
				dao.saveDocument(doc);
				n++;
				System.out.println(n + "\t" + doc.getUrl());
			}
			if (scrollResp.getHits().hits().length == 0)
				break;
		}

	}

}
