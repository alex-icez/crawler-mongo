package crawler.task;

import java.util.HashMap;
import java.util.Map;

public class Task {
	
	public static class PageBlock {	
		protected Map<String, String> takers = new HashMap<String, String>();
		
		public Map<String, String> getTakers() {
			return takers;
		}
		
		public String getTaker(String field) {
			return takers.get(field);
		}
		
		public void setTaker(String field, String taker) {
			takers.put(field, taker);
		}
	}
	
	public static class TransferBlock extends PageBlock {
		
		protected String urlXPath;
		protected String nextPageXPath;

		protected PageBlock nextBlock;
		
		public String getUrlXPath() {
			return urlXPath;
		}
		
		public void setUrlXPath(String urlXPath) {
			this.urlXPath = urlXPath;
		}

		public String getNextPageXPath() {
			return nextPageXPath;
		}

		public void setNextPageXPath(String nextPageXPath) {
			this.nextPageXPath = nextPageXPath;
		}

		public PageBlock getNextBlock() {
			return nextBlock;
		}

		public void setNextBlock(PageBlock nextBlock) {
			this.nextBlock = nextBlock;
		}		
	}
	
	protected String host;
	protected PageBlock root;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public PageBlock getRoot() {
		return root;
	}

	public void setRoot(PageBlock root) {
		this.root = root;
	}

	public static Task buildTask() {
		Task task = new Task();
		task.host = "http://habrahabr.ru";
		TransferBlock tb1 = new TransferBlock();
		tb1.urlXPath = ".//*[@class='main_menu']/A[4]"; 
		tb1.nextPageXPath = null;
		task.root = tb1;
	
		TransferBlock tb2 = new TransferBlock();
		tb2.urlXPath = ".//*[@id='hubs']//*[@class='title']/*[@href]";
		tb2.nextPageXPath = ".//*[@id='next_page']";
		tb1.nextBlock = tb2;
		
		TransferBlock tb3 = new TransferBlock();
		tb3.urlXPath = ".//*[@class='post_title']";
		tb3.nextPageXPath = ".//*[@id='next_page']";
		tb2.nextBlock = tb3;
		
		PageBlock pb = new PageBlock();
		pb.setTaker("date", ".//*[@class='published']");
		pb.setTaker("hubs", ".//*[@class='hubs']/A");
		pb.setTaker("tags", ".//*[@rel='tag']");
		pb.setTaker("title", ".//*[@class='post_title']");
		pb.setTaker("text", ".//*[@class='content html_format']");
		pb.setTaker("author", ".//*[@class='author']/A[1]");
		pb.setTaker("views", ".//*[@class='pageviews']");
		pb.setTaker("score", ".//*[@class='infopanel ']//*[@class='score']");
		pb.setTaker("texts_comment", ".//*[@class='comment_body']/DIV[2]");
		pb.setTaker("authors_comment", ".//*[@id='comments']//*[@class='username']");
		pb.setTaker("dates_comment", ".//*[@id='comments']//TIME");
		pb.setTaker("scores_comment", ".//*[@id='comments']//SPAN[@class='score']");
		tb3.nextBlock = pb;
		return task;
	} 
}
