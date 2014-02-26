package crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import crawler.handler.DocumentHandler;
import crawler.task.Task;
import crawler.task.Task.PageBlock;
import crawler.task.Task.TransferBlock;
import crawler.util.DOMUtil;

public class Robot implements Runnable {

	private static class TaskRobot {
		PageBlock block; 
		String url; 
		public TaskRobot(PageBlock block, String url) {
			this.block = block;
			this.url = url;
		}	
	}
	
	private static Queue<TaskRobot> queue = new LinkedList<TaskRobot>();
	
	private static synchronized Document getDom(String url) throws SAXException, IOException {
		return DOMUtil.parseDOM(url);
	}
	
	private static TaskRobot getTaskRobot() {
		synchronized(queue) {
			if (queue.isEmpty())
				return null;
			return queue.remove();
		}
	}
	
	private static void addTaskRobot(TaskRobot taskRobot) {
		synchronized(queue) {
			queue.add(taskRobot);
			System.out.println("queue size : " + queue.size() + " " + taskRobot.url);
		}
	}
	
	private static List<Robot> threads = new LinkedList<Robot>();
	private AtomicBoolean stopped = new AtomicBoolean(false);
	private AtomicBoolean working = new AtomicBoolean(false);
	Thread thread;
	Task task;
	DocumentHandler handler;
	
	public static void runTask(Task task, DocumentHandler handler, int nThread) {
		addTaskRobot(new TaskRobot(task.getRoot(), task.getHost()));
		for(int i = 0; i < nThread; i++)
			threads.add(new Robot(task, handler));
		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean run = false;
			for(Robot robot : threads) {
				synchronized(robot.working) {
					run |= robot.working.get();
				}
			}
			if (!run)
				break;
		}
		for(Robot robot : threads)
			robot.stopped();
	}
	
	private Robot(Task task, DocumentHandler handler) {
		this.task = task;
		this.handler = handler;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		System.out.println("Thread start!!! " + thread.getName());
		stopped.set(false);
		while(!stopped.get()) {
			TaskRobot taskRobot;
			synchronized(working) {
				taskRobot = getTaskRobot();
				if (taskRobot != null)
					working.set(true);
			}
			if (!working.get()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			eval(taskRobot);
			working.set(false);
		}
	}

	public void stopped() {
		stopped.set(true);
	}

	private void eval(TaskRobot taskRobor) {
		Document dom = null;
		String url = taskRobor.url;
		if (handler.checkUrl(url))
			return;
		if (taskRobor.block instanceof TransferBlock) {
			TransferBlock tBlock = (TransferBlock)taskRobor.block;
			while(true) {
				try {
					dom = getDom(url);
				} catch (SAXException e) {
					e.printStackTrace();
					return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				List<String> urls;
				try {
					urls = DOMUtil.getUrls(dom, tBlock.getUrlXPath());
					for(String newUrl : urls) 
						addTaskRobot(new TaskRobot(tBlock.getNextBlock(), newUrl));
				} catch (XPathExpressionException e1) {
					e1.printStackTrace();
					return;
				}
				if (tBlock.getNextPageXPath() == null || tBlock.getNextPageXPath().isEmpty())
					break;
				List<String> nextUrl;
				try {
					nextUrl = DOMUtil.getUrls(dom, tBlock.getNextPageXPath());
				} catch (XPathExpressionException e1) {
					e1.printStackTrace();
					return;
				}
				if (nextUrl.isEmpty())
					break;
				url = nextUrl.get(0);
				if (url.startsWith("/"))
					url = task.getHost().concat(url);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				dom = getDom(url);
			} catch (SAXException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			Map<String, List<String>> metaData = new HashMap<String, List<String>>();
			for(String field : taskRobor.block.getTakers().keySet()) {
				try {
					metaData.put(field, DOMUtil.getTexts(dom, taskRobor.block.getTaker(field)));
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
			List<String> listUrl = new LinkedList<String>();
			listUrl.add(url);
			metaData.put("url", listUrl);
			model.Document document = handler.createDocument(metaData);
			handler.addDocument(document);
		}
		
	}
	
	public static void main(String args[]) {
		runTask(Task.buildTask(), new DocumentHandler(), 20);
	//	System.out.println(new DocumentHandler().checkUrl("http://habrahabr.ru/post/172023/"));
		/*	Task task = Task.buildTask();
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
		
		runPage(task, pb ,"http://habrahabr.ru/post/213025/", new DocumentHandler());*/
		
	} 
}
