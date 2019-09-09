package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and tdTagling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		root = buildRecursive();
	}
	private TagNode buildRecursive() {
		/** COMPLETE THIS METHOD **/
		int length;
		boolean line = sc.hasNextLine();
		String temp;
		
		if (line == true) {
			temp = sc.nextLine();
			length = temp.length() - 1;
		} else {
			return null;
		}
		boolean first = false;
		
		if (temp.charAt(0) == '<') {
			temp = temp.substring(1,length);
			if (temp.charAt(0) == '/') {
				return null;
			} else {
				first = true;
			}
		}
		
		TagNode temp1 = new TagNode(temp,null,null);
		
		if (first == true) {
			temp1.firstChild = buildRecursive();
		}
		temp1.sibling = buildRecursive();
		return temp1;
		}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		//traverse through binary tree, base case is when u run into tag then u go back through tree and replace all instances of tagnode.tag w/ newtag
		//try to use recursion, makes it much simpler
		TagNode ptr = root;
		root = replaceTagRecursive(ptr,oldTag,newTag);
	}
	private TagNode replaceTagRecursive(TagNode ptr, String oldT, String newT) {
		if (ptr == null) {
			return ptr;
		}
		if (ptr.tag.equals(oldT)) {
			ptr.tag = newT;
		}
		
		replaceTagRecursive(ptr.firstChild,oldT,newT);
		replaceTagRecursive(ptr.sibling,oldT,newT);
		return ptr;
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		//need to find first instance of table, then keep track of htat and traverse through until you find td tag then put bold row under td tag
		TagNode ptr;
		TagNode tdTag;
		ptr = findTable(root);
		
		if (ptr == null) {
			return;
		}
		ptr = ptr.firstChild;
		ptr = findRow(ptr, row);
		if (ptr == null) {
			return;
		}
		tdTag = ptr.firstChild;
		boldRowRecursive(tdTag);
		
	}
	private void boldRowRecursive(TagNode ptr) {
		if (ptr == null) {
			return;
		}
		ptr.firstChild = new TagNode("b",ptr.firstChild,null);
		boldRowRecursive(ptr.sibling);
	}
	private TagNode findTable(TagNode ptr) {
				if (ptr == null) {
					return null; 
				}
				TagNode holder = null;
				
				if(ptr.tag.equals("table")) { 
					holder = ptr; 
					return holder;
				} 
				if(holder == null) {
					holder = findTable(ptr.firstChild);
				}
				
				if(holder == null) { 
					holder = findTable(ptr.sibling);
				} 
				
				
				
				return holder;
	}
	private TagNode findRow(TagNode ptr, int count) {
		if (ptr == null) {
			return null;
		}
		if (count == 1) {
			return ptr;
		}
		return findRow(ptr.sibling,--count);
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		while (tagCheck(tag,root) == true) {
		removeTagRecursive(tag,root,null);
		}
	}
	private void removeTagRecursive(String tag, TagNode ptr,TagNode prev) {
		if (ptr == null) {
			return;
		}
		if (ptr.tag.equals(tag)) {
			if (ptr.tag.equals("ol") || ptr.tag.equals("ul")) {
				replaceList(ptr.firstChild);
			}
			if (prev.firstChild == ptr) {
				prev.firstChild = ptr.firstChild;
				addSibling(ptr.firstChild,ptr.sibling);
			} else if (prev.sibling == ptr) {
				addSibling(ptr.firstChild,ptr.sibling);
				prev.sibling = ptr.firstChild;
			}
			return;
		}
		removeTagRecursive(tag,ptr.firstChild,ptr);
		removeTagRecursive(tag,ptr.sibling,ptr);
	}
	private void replaceList(TagNode ptr) {
		if (ptr == null) {
			return;
		}
		if (ptr.tag.equals("li")) {
			String nTag = "p";
			ptr.tag = nTag;
		}
		replaceList(ptr.sibling);
	}
	private TagNode lastSibling(TagNode ptr) {
		while (ptr.sibling != null) {
			ptr = ptr.sibling;
		}
		return ptr;
	}
	private void addSibling(TagNode ptr, TagNode sib) {
		ptr = lastSibling(ptr);
		ptr.sibling = sib;
	}
	private boolean tagCheck(String tag, TagNode ptr) {
		if (ptr == null) {
			return false;
		}
		if (ptr.tag.equals(tag)) {
			return true;
		}
		return tagCheck(tag,ptr.firstChild) || tagCheck(tag,ptr.sibling);
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addTagRecursive(word,tag,root.firstChild);
	}
	private void addTagRecursive(String word, String tag, TagNode ptr) {
		String wordT = word.toLowerCase();
		if (ptr == null) {
			return;
		} 
		String tagT = ptr.tag.toLowerCase();
		if (tagT.contains(wordT)) {
			if (ptr.tag.equalsIgnoreCase(word)) {
				ptr.tag = tag;
				TagNode temp = new TagNode(word,ptr.firstChild,null);
				ptr.firstChild = temp;
			} else if (tagT.contains(wordT)) {
				TagNode sib = ptr.sibling;
				String pre = ptr.tag.substring(0,tagT.indexOf(wordT));
				String post = ptr.tag.substring(tagT.indexOf(wordT) + word.length());
				String punc = "";
				String norm = ptr.tag.substring(tagT.indexOf(wordT), tagT.indexOf(wordT) + word.length());
				
				if (post.length() > 0) {
					if (post.length() > 1 && puncCheck(post.charAt(0)) == true && puncCheck(post.charAt(1)) == false) {
						punc = post.charAt(0) + "";
						post = post.substring(1);
					}
				}
				if (post.length() == 0 || (post.length() >= 1 && (post.charAt(0) == ' ' || puncCheck(post.charAt(0)) == true))) {
					if (puncCheck(post) == true) {
						norm = norm + post;
						post = "";
					}	
					ptr.tag = pre;
					TagNode temp2 = new TagNode(norm + punc, null, null);
					ptr.sibling = new TagNode(tag, temp2, null);
					
					if (post.length() > 0) {
						if (sib != null) {
							TagNode temp3 = new TagNode(post,null,sib);
							ptr.sibling.sibling = temp3;
						} else {
							TagNode temp4 = new TagNode(post,null,null);
							ptr.sibling.sibling = temp4;
						}
					} else if (sib != null) {
						ptr.sibling.sibling = sib;
					}
				}
				addTagRecursive(word,tag,ptr.sibling.sibling);
			}
		} else {
			addTagRecursive(word,tag,ptr.firstChild);
			addTagRecursive(word,tag,ptr.sibling);
		}
		
	}
	private boolean puncCheck(char c) {
		if (c == '!' || c == '?' || c == ';' || c == ':' || c == ',' || c == '.') {
			return true;
		}
		return false;
	}
	private boolean puncCheck (String s) {
		if (s.equals("!") || s.equals("?") || s.equals(";") || s.equals(":") || s.equals(",") || s.equals(".")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
