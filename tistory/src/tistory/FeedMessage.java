package tistory;

public class FeedMessage {

	String title;
	  String description;
	  String link;
	  String author;
	  String pubDate;
	  String guid;
	  String thumbnail;

	  public String getTitle() {
	    return title;
	  }

	  public void setTitle(String title) {
	    this.title = title;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public void setDescription(String description) {
	    this.description = description;
	  }

	  public String getLink() {
	    return link;
	  }

	  public void setLink(String link) {
	    this.link = link;
	  }

	  public String getAuthor() {
	    return author;
	  }

	  public void setAuthor(String author) {
	    this.author = author;
	  }
	  
	  public String getpubDate() {
	    return pubDate;
	  }

	  public void setpubDate(String pubDate) {
	    this.pubDate = pubDate;
	  }

	  public String getGuid() {
	    return guid;
	  }

	  public void setGuid(String guid) {
	    this.guid = guid;
	  }
	  
	  public String getThumbnail() {
		    return this.thumbnail;
	  }
	  
	  public void setThumbnail(String thumbnail) {
		    this.thumbnail = thumbnail;
	  }

	  @Override
	  public String toString() {
	    return "FeedMessage [title=" + title + ", description=" + description
	        + ", link=" + link + ", author=" + author + ", pubDate=" + pubDate + ", guid=" + guid + ", thumbnail=" + thumbnail
	        + "]";
	  }

}


