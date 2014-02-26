package model;

import java.util.Arrays;
import java.util.UUID;

public class Comment {
	private String uuid = UUID.randomUUID().toString();
	private String text;
	private String author;
	private String date;
	private int score;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\tuuid : ").append(uuid);
		sb.append("\n\ttext : ").append(text);
		sb.append("\n\tdate : ").append(date);
		sb.append("\n\tauthor : ").append(author);
		sb.append("\n\tscore : ").append(score);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Comment))
			return false;
		Comment c = (Comment)obj;
		return uuid.equals(c.uuid) && text.equals(c.text) && author.equals(c.author) && date.equals(c.date) && (score == c.score);
	}
	
}
