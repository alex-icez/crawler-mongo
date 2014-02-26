package crawler.util;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DOMUtil {

	public static NodeList evaluateNodes(Node node, String expr) throws XPathExpressionException {
		if (expr.isEmpty()) 
			return null;
		XPath xpath = (XPath) XPathFactory.newInstance().newXPath();
		XPathExpression xpathExpr = xpath.compile(expr);
		NodeList nodes = (NodeList) xpathExpr.evaluate(node, XPathConstants.NODESET);
		return nodes;
	}
	
	public static Document parseDOM(byte[] content) throws SAXException {
		return parseDOM(new InputSource(new ByteArrayInputStream(content)));
	}
	
	public static Document parseDOM(InputSource source) throws SAXException {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parser.getDocument();
	}
	
	public static Document parseDOM(URL url) throws SAXException, IOException {
		return parseDOM(getInputSource(url));
	}
	
	public static Document parseDOM(String url) throws SAXException, IOException {
		return parseDOM(getInputSource(new URL(url)));
	}
	
	public static List<String> getTexts(Document dom, String xpath) throws XPathExpressionException {
		List<String> res = new LinkedList<String>();
		NodeList nodes = evaluateNodes(dom, xpath);
		for (int i = 0; i < nodes.getLength(); ++i) 
			res.add(nodes.item(i).getTextContent());
		return res;
	}
	
	public static List<String> getUrls(Document dom, String xpath) throws XPathExpressionException {
		List<String> res = new LinkedList<String>();
		NodeList nodes = evaluateNodes(dom, xpath);
		for (int i = 0; i < nodes.getLength(); ++i) 
			res.add(nodes.item(i).getAttributes().getNamedItem("href").getTextContent());
		return res;
	}
	
	public static InputSource getInputSource(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.connect();
		String contentType = con.getContentType();
		String charset = "utf-8";
		int n = contentType.indexOf("charset=");
		if (n > -1)
			charset = contentType.substring(8 + n);
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		return new InputSource(reader);
	}
		
	public static void main(String args[]) throws MalformedURLException, SAXException, IOException, XPathExpressionException {
		Document dom1 = DOMUtil.parseDOM(getInputSource(new URL("http://habrahabr.ru/post/213537/")));
		List<String> l1 = getTexts(dom1, "/HTML/BODY/DIV/DIV/DIV[2]/DIV[1]/DIV[2]/A".toUpperCase());
		for(String name : l1)
			System.out.println(name);
		
		/*	String pref = "http://habrahabr.ru";
		String url = "/hubs";
		
		while(true) {
			Document dom = DOMUtil.parseDOM(getInputSource(new URL(pref.concat(url))));
			List<String> list = getUrls(dom, "/html/body/div/div/div[2]/div/div[3]/div/div/div[1]/a".toUpperCase());
			for(String str : list) {
				Document dom1 = DOMUtil.parseDOM(getInputSource(new URL(str)));
				List<String> l1 = getTexts(dom1, "/html/body/div/div/div[3]/div[3]/div[1]/div/h1/a".toUpperCase());
				for(String name : l1)
					System.out.println(name);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list = getUrls(dom, ".//*[@id='next_page']");
			if (list.isEmpty())
				break;
			url = list.get(0);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} */
	}
}
