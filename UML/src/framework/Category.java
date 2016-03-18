package framework;

import java.util.*;

public class Category {
	Collection<Article> belong;
	private String name;
	
	public Category(String name) {
		this.name = name;
	}
	
	public Collection<Article> getArticles() {
		return belong;
	}
	public String toString() {
		return name;
	}
}