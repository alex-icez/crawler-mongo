package crawler.handler;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import model.Comment;
import model.Document;
import mongo.MongoUtil;

import es.ElasticSearchUtil;
import es.dao.DocumentDAO;

public class DocumentHandler {	
	
//	static DocumentDAO documentDAO = new DocumentDAO(ElasticSearchUtil.getClient());

	static mongo.dao.DocumentDAO documentDAO = null;
	static int count = 0;
	
	static {
		try {
			documentDAO = new mongo.dao.DocumentDAO(MongoUtil.getDB());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void addDocument(Document document) {
		if (document.getText() == null || document.getText().isEmpty()
				|| document.getTitle() == null || document.getTitle().isEmpty() 
				|| document.getAuthor() == null || document.getAuthor().isEmpty())
			return;
		try {
			documentDAO.saveDocument(document);
		} catch(Exception e) { 
			System.out.println("Error add!!!");
		//	e.printStackTrace();
		}
		count++;
		System.out.println("count : " + count);
	}
	
	public boolean checkUrl(String url) {
		try {
			return documentDAO.checkDupl(url);
		} catch(Exception e) {
			System.out.println("Error check!!!");
		//	e.printStackTrace();
		}
		return false;
	}
	
    public Document createDocument(Map<String, List<String>> metaData) {
    	Document doc = new Document();
    	if (metaData.containsKey("url")) {
    		List<String> url = metaData.get("url");
    		doc.setUrl(url.isEmpty() ? "" : url.get(0));
    	}
    	if (metaData.containsKey("title")) {
    		List<String> title = metaData.get("title");
    		doc.setTitle(title.isEmpty() ? "" : title.get(0));
    	}
    	if (metaData.containsKey("text")) {
    		List<String> text = metaData.get("text");
    		doc.setText(text.isEmpty() ? "" : text.get(0));
    	}
    	if (metaData.containsKey("date")) {
    		List<String> date = metaData.get("date");
    		doc.setDate(date.isEmpty() ? "" : date.get(0));
    	}
    	if (metaData.containsKey("tags")) {
    		List<String> tags = metaData.get("tags");
    		doc.getTags().addAll(tags);
    	}
    	if (metaData.containsKey("hubs")) {
    		List<String> hubs = metaData.get("hubs");
    		doc.getHubs().addAll(hubs);
    	}
    	if (metaData.containsKey("author")) {
    		List<String> author = metaData.get("author");
    		doc.setAuthor(author.isEmpty() ? "" : author.get(0));
    	}
    	if (metaData.containsKey("views")) {
    		List<String> views = metaData.get("views");
    		int val = Integer.parseInt(views.isEmpty() ? "0" : views.get(0));
    		doc.setViews(val);
    	}
    	if (metaData.containsKey("score")) {
    		List<String> score = metaData.get("score");
    		int val = score.isEmpty() ? 0 : parseScore(score.get(0));
    		doc.setScore(val);
    	}
    	List<String> textsComment = metaData.get("texts_comment");
    	List<String> authorsComment = metaData.get("authors_comment");
    	List<String> datesComment = metaData.get("dates_comment");
    	List<String> scoresComment = metaData.get("scores_comment");
    	if (textsComment == null || textsComment.isEmpty())
			return doc;
    	for(int i = 0; i < textsComment.size(); i++) {
    		Comment comment = new Comment();
    		comment.setText(textsComment.get(i));
    		if (authorsComment != null && i < authorsComment.size())
    			comment.setAuthor(authorsComment.get(i));
    		if (datesComment != null && i < datesComment.size())
    			comment.setDate(datesComment.get(i));
    		if (scoresComment != null && i < scoresComment.size())
    			comment.setScore(parseScore(scoresComment.get(i)));
    		doc.getComments().add(comment);
    	}
    	return doc;
    }
    
    private static int parseScore(String str) {
    	int score = 0;
    	try {
    		score = Integer.parseInt(str.replace("+", "").replace("–", "-"));
    	} catch (Exception e) {

    	}
    	return score;
    }
}