package model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Document {
	private String uuid = UUID.randomUUID().toString();
	private String url;
	private String title;
	private String date;
	private List<String> tags = new LinkedList<String>();
	private String text;
	private List<String> hubs = new LinkedList<String>();
	private String author;
	private int views; 
	private int score;
	private List<Comment> comments = new LinkedList<Comment>();
	
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getHubs() {
		return hubs;
	}
	public void setHubs(List<String> hubs) {
		this.hubs = hubs;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuid() {
		return uuid;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("uuid : ").append(uuid);
		sb.append("\nurl : ").append(url);
		sb.append("\ntitle : ").append(title);
		sb.append("\ntext : ").append(text);
		sb.append("\ndate : ").append(date);
		sb.append("\ntags : ").append(Arrays.toString(tags.toArray()));
		sb.append("\nhubs : ").append(Arrays.toString(hubs.toArray()));
		sb.append("\nauthor : ").append(author);
		sb.append("\nviews : ").append(views);
		sb.append("\nscore : ").append(score);
		sb.append("\n\tcomments : \n");
		for(Comment comment : comments)
			sb.append(comment).append("\n");
		return sb.toString();
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public int getViews() {
		return views;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<Comment> getComments() {
		return comments;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Document))
			return false;
		Document d = (Document)obj;
		return uuid.equals(d.uuid) && text.equals(d.text) && author.equals(d.author) && date.equals(d.date) && (score == d.score) &&
				url.equals(d.url) && title.equals(d.title) && (views == d.views) && (tags.equals(d.tags)) && hubs.equals(d.hubs) && 
				comments.equals(d.comments);
	}
	
}
