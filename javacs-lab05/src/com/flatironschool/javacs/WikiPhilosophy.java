package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	static ArrayList<String> visited = new ArrayList();
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started
		String target = "https://en.wikipedia.org/wiki/Philosophy";
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);

		crawler(url, target);
        // the following throws an exception so the test fails
        // until you update the code
//        String msg = "Complete this lab by adding your code and removing this statement.";
//        throw new UnsupportedOperationException(msg);
	}
	
	private static String crawler(String url, String target) throws IOException {
		String status = "";
		if (url.equals(target)) {
			status = "found";
		} else {
			while(!status.equals("not found")) {
				String firstLink = getFirstLink(url, target);
//				System.out.println(firstLink);
				if (firstLink.equals("error")) {
					status = "not found";
					System.out.println(status);
					System.out.println(Arrays.toString(visited.toArray()));
					return status;
				} else if (firstLink.equals(target)){
					status = "found";
					System.out.println(status);
					System.out.println(Arrays.toString(visited.toArray()));
					return status;
				} else {
					url = firstLink;
				}
			}
		}
		return status;
	}
	
	private static String getFirstLink(String url, String target) throws IOException {
		
		Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);

		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter) {
			if (node instanceof Element) {
				String tagName = ((Element) node).tagName();
				String anchorText = ((Element) node).text();
				char firstLetter = anchorText.charAt(0);
				if (tagName.equals("a") && (firstLetter == Character.toLowerCase(firstLetter))) {
					String fullURL = "https://en.wikipedia.org"+ node.attr("href");
					if (visited.contains(fullURL)) {
						return "error";
					}
					visited.add(fullURL);
					return fullURL;
				}
			}
        }
		return "error";
	}
}
