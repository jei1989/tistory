package tistory;

import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.io.InputStream;
//import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

///import com.google.api.client.repackaged.org.apache.commons.codec.Encoder;

public class RssWriter {

	  static final String TITLE = "title";
	  static final String DESCRIPTION = "description";
	  static final String CHANNEL = "channel";
	  static final String LANGUAGE = "language";
	  static final String COPYRIGHT = "copyright";
	  static final String LINK = "link";
	  static final String AUTHOR = "author";
	  static final String ITEM = "item";
	  static final String PUB_DATE = "pubDate";
	  static final String GUID = "guid";
	  static final String MIMAGE = "url";
	  static final String THUMBNAIL = "thumbnail";
	  
	  public String wmode = "NAVER";
	  public String tempFile = "temp.txt";
	  public String rssfile = "";//"rsslist.txt";
	  public String keywordfile = "";//"keywordlist.txt";

	  public URL url;
	  
	  //public BlogWrite blogwrite;
	  public String charset = "UTF-8";
	  
	  //public RssWriter(String feedUrl) {
	  
	  public RssWriter(String[] args) throws MalformedURLException {
		  
	      //String title = "";
	      //String content = "";
	      //blogwrite = new BlogWrite();
	      //String naver_rss = "http://newssearch.naver.com/search.naver?where=rss&query=";
	      //String search_word = "송중기";
	      //naver_rss = "http://tab.search.daum.net/dsa/search?w=news&m=rss&SortType=1&q=";
	      //charset = "euc-kr";
	      
	    try {
	    	
	    	
	    	if( args != null )
	    	{
	    		Trace.trace("Usage example -k keywordfile_list.txt -r rssfile_list.txt");
	    		if( args.length > 0 )
	    		{
	    			
	    			for( int k=0; k < args.length ; k++)
	    			{
	    				if( args[k].equals("-k") )
	    				{
	    					Trace.trace(args[k+1]);
	    					keywordfile = args[k+1];
	    				}
	    				if( args[k].equals("-r") )
	    				{
	    					Trace.trace(args[k+1]);
	    					rssfile = args[k+1];
	    				}
	    			}
	    			
	    		}
	    		else
	    		{
	    			keywordfile = "keywordlist.txt";
	    			rssfile = "rsslist.txt";
	    			Trace.trace("Usage example -k keywordfile_list.txt -r rssfile_list.txt");	    			
	    		}
	    	}else
	    	{
    			keywordfile = "keywordlist.txt";
    			rssfile = "rsslist.txt";	    		
	    		Trace.trace("Usage example -k keywordfile_list.txt -r rssfile_list.txt");
	    	}
	    	
	      //apikey = f75f3cd81dd3df007cc393dedbb2f388
	      //this.url = new URL("http://search.daum.net/search?q=송중기&w=news&nil_profile=search&nil_src=entertain&DA=MEFX");
	      //this.url = new URL("http://rss.ohmynews.com/rss/todayphoto.xml");
	    	//https://news.google.co.kr/news/section?sa=N&tab=bn&q=
			//this.url = new URL("https://news.google.co.kr/news?cf=all&hl=ko&ned=kr&output=rss&q=" + URLEncoder.encode("송중기",charset));
	    	//this.url = new URL("http://search.daum.net/search?nil_suggest=btn&nil_ch=&rtupcoll=&w=news&m=&lpp=&q=" + URLEncoder.encode("송중기",charset));
			//송중기 
	    	//네이버
	    	//http://newssearch.naver.com/search.naver?where=rss&query=송중기
	    	Path pathrssfile = Paths.get(rssfile);
	    	BufferedReader br = null;
	    	String line = null;
	    	Trace.trace(pathrssfile.toAbsolutePath().toString());
	    	SimpleDateFormat zformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	    	
	    	if( new File(pathrssfile.toAbsolutePath().toString()).exists() )
	    	{
	    		br = new BufferedReader(new FileReader(pathrssfile.toAbsolutePath().toString()));
	    		while(( line = br.readLine() ) != null )
	    		{
	    			Trace.trace(line);
	    			String[] urlstr = line.split("\\|");
	    			//0 - DAUM or NAVER
	    			//1 - charset
	    			//2 - Search URL
	    			
	    			Path pathkeywordfile = Paths.get(keywordfile);
	    			BufferedReader brkey = null;
	    			String linekey = null;
	    			Trace.trace(pathkeywordfile.toAbsolutePath().toString());
	    			
	    			if( new File(pathkeywordfile.toAbsolutePath().toString()).exists() )
	    			{
	    				brkey = new BufferedReader(new FileReader(pathkeywordfile.toAbsolutePath().toString()));
	    				while( (linekey = brkey.readLine() ) != null )
	    				{
	    					
	    					//Trace.trace(linekey);
	    					this.charset = urlstr[1];
	    					this.url = new URL(urlstr[2] + URLEncoder.encode(linekey.substring(1, linekey.length()-1),charset));//urlstr[1]) );
	    					Feed feed = this.readFeed();
	    					
	    					
	    					/**************************************/
	    					/**************************************/
	    					/**************************************/
	    					
	    					SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
	    	    			String todaydate = mformat.format(new Date());
	    			    	//Trace.trace("todaydate :: " +  todaydate );
	    			    	
	    			    	String tempfile = todaydate + "_" + urlstr[0] + linekey +".txt";
	    			    	Path pathimsi = Paths.get(tempfile);
	    			    	Trace.trace(pathimsi.toAbsolutePath().toString());
	    			    	
	    			    	if( new File(pathimsi.toAbsolutePath().toString()).exists() )
	    			    	{
	    			    		Trace.trace("aleady file exits :: " + pathimsi.toAbsolutePath() );
	    			    		
	    			    		FileInputStream input = new FileInputStream(new File(pathimsi.toAbsolutePath().toString()));
	    			    		BufferedReader reader = new BufferedReader(new InputStreamReader(input,charset));
	    			    		Vector<String> vec = new Vector();
	    			    		String imsi = "";
	    			    		String vectemp = "";
	    			    		while( ( imsi = reader.readLine() ) != null )
	    			    		{
	    			    			vec.addElement(imsi);
	    			    		}
	    			    		
	    			    		String newstr = "";
	    			    		boolean iscontinue = true;
	    			    		
	    			    		
	    			    		for (FeedMessage message : feed.getMessages())
	    			    		{

	    			    			Date mdate =  zformat.parse(message.pubDate);//
	    			    			Date today_date = new Date();
	    			    			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    			    			cal.setTime(mdate);
	    			    			Calendar todaycal = Calendar.getInstance(Locale.ENGLISH);
    			    			
    			    			
	    			    			if(  (cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)) || (( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 ))   )
	    			    			{
	    			    			
		    			    			if( iscontinue )
	    			    				{
		    			    				String tempstr = "";
		    			    			
		    			    				String descript = message.description.replaceAll("\\|", "");
		    			    				descript = message.description.replaceAll("\\|", "");
			    			    			tempstr = message.title.replaceAll("\\|", "") + "|" + message.link + "|" +  descript + "|" + message.pubDate + "|" + message.author.replaceAll("\\|", "") + "|" + message.thumbnail+ "\n";
		    			    				
			    			    					    			    			
				    			    			for( int k=0 ; k < vec.size() && iscontinue ; k++ )
				    			    			{
					    			    				if( vec.elementAt(k).toString().trim().equals(tempstr.trim()) )
					    			    				{
					    			    					tempstr = "";
					    			    					iscontinue = false;
					    			    				}
					    			    				
				    			    			}
				    			    		newstr = newstr + tempstr;
		    			    			}
	    			    			}//if(  (cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)) || (( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 ))   )
		    			    		
	    			    		}
	    			    		
	    			    		reader.close();
	    			    		input.close();
	    			    		
	    			    		FileOutputStream out = new FileOutputStream(new File(pathimsi.toAbsolutePath().toString()),false);
	    			    		OutputStreamWriter stream = new OutputStreamWriter(out,charset);
	    			    		
    			    			if( newstr.trim().equals("") || newstr.trim().equals(null) || newstr.trim().equals(" "))
    			    			{

				    					for( int k=0 ; k < vec.size() ; k++ )
	    	    			    		{	    			    			
			    			    			try{			    			    			
					    						String[] strarray = (vec.elementAt(k)).toString().split("\\|");
					    						//Trace.trace( strarray[0] + " : " + strarray[1] + " : " + strarray[2] + " : " + strarray[3] + " : " + strarray[4]  );
					    						Date mdate =  zformat.parse(  strarray[3]);
					    						
				    			    			Date today_date = new Date();
				    			    			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
				    			    			cal.setTime(mdate);
				    			    			Calendar todaycal = Calendar.getInstance(Locale.ENGLISH);    			    				
		    			    				
			    			    				//if(  cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)   )
				    			    			if(  (cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)) || (( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 ))   )
			    			    				{
			
			       	    			    			stream.write(vec.elementAt(k).toString() + "\n");
			        			    				Trace.trace( " new string blank  "  );
			        			    				
				    			    			}
			    			    			}catch(Exception ex){
			    			    				ex.printStackTrace();
			    			    			}		    			    				
	    	    			    		}

    			    				
    			    			}//if( newstr.trim().equals("") || newstr.trim().equals(null) || newstr.trim().equals(" "))
    			    			/*************************/
    			    			else
    			    			{
    			    				/*************************
    			    				if (newstr.length() == 0 && vec.size() ==  0 )
    			    				{
    			    					List mess = feed.getMessages();
    			    					FeedMessage message = (FeedMessage)mess.get(0);
    			    					newstr = message.title.replaceAll("\\|", "") + "|" + message.link + "|" +  message.description.replaceAll("\\|", "") + "|" + message.pubDate + "|" + message.author.replaceAll("\\|", "") + "|" + message.thumbnail+ "\n";
    			    					stream.write(newstr);	
    			    				}
    			    				
    	    			    		     			    				
    			    				/*************************/
    			    				
    			    				Trace.trace( " new string NOT blank START :: "+  vec.size() + " ::: "  + newstr );
    			    				stream.write(newstr);
    			    				for( int k=0 ; k < vec.size() ; k++ )
    	    			    		{
    	    			    			stream.write(vec.elementAt(k).toString() + "\n");
    	    			    		}
    	    			    		Trace.trace( " new string NOT blank END  "  );
    			    			}
    			    			/*************************/
    			    			///Trace.trace(newstr + vectemp);
    			    			
    			    			stream.close();
    			    			out.close();
    			    			
	    			    		
	    			    		
	    			    	}//if( new File(pathimsi.toAbsolutePath().toString()).exists() )
	    			    	else
	    			    	{
	    			    		FileOutputStream out = new FileOutputStream(new File(pathimsi.toAbsolutePath().toString()),false);
	    			    		OutputStreamWriter stream = new OutputStreamWriter(out,charset);
	    			    		
	    			    		String tempstr = "";
	    			    		for (FeedMessage message : feed.getMessages()) {
	    			    			Trace.trace(" file not fountd ============================= ");
	    			    			try{
	    			    				//Trace.trace( "(message.pubDate).toString() == " + (message.pubDate).toString() );
		    			    			
		    			    			Date mdate =  zformat.parse(message.pubDate);//
		    			    			Date today_date = new Date();
		    			    			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		    			    			cal.setTime(mdate);
		    			    			Calendar todaycal = Calendar.getInstance(Locale.ENGLISH);
	    			    			
	    			    			
		    			    			if(  cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)   ){
		    			    				//Trace.trace( " [same time] tempfile == " + tempfile + " :: cal.get(Calendar.DAY_OF_MONTH) == "  + cal.get(Calendar.DAY_OF_MONTH)+ ":"  + cal.get(Calendar.MINUTE));
		    			    				tempstr = tempstr + message.title + "|" + message.link + "|" +  message.description + "|" + message.pubDate + "|" + message.author + "|" + message.thumbnail+ "\n";
		    			    			}
		    			    			/***********/
		    			    			else
		    			    			{
		    			    				//Trace.trace( " [different time] tempfile == " + tempfile + " :: Calendar.HOUR_OF_DAY 11111 == "  + cal.get(Calendar.HOUR_OF_DAY));
		    			    				if( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 )
		    			    				{
		    			    					//Trace.trace( " [different inner time] tempfile == " + tempfile + " :: Calendar.HOUR_OF_DAY 22222 == "  + cal.get(Calendar.HOUR_OF_DAY) + " :: " + cal.get(Calendar.MINUTE));
		    			    					tempstr = tempstr + message.title + "|" + message.link + "|" +  message.description + "|" + message.pubDate + "|" + message.author + "|" + message.thumbnail+ "\n";
		    			    				}
		    			    				
		    			    			}
		    			    			/***********/
		    			    			
	    			    			}catch(Exception ex){
	    			    				ex.printStackTrace();
	    			    			}
	    			    			
	    			    		}
	    			    		stream.write(tempstr);
    			    			//Trace.trace(tempstr);
	    			    	    /***********	
	    			  		      //System.out.println("thumbnail = " +  message.thumbnail + " ::: " + message);
	    			  		    	//DateFormat dateformat = new 
	    			  		    	SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
	    			  		    	Date mdate =  new Date(message.pubDate.toString()); 
	    			  		    			//mformat.parse(mdate);
	    			  		    	
	    			  		      content = content
	    			  		    		+ "-----------------------------------------"
	    			    	    		    + "<b><font color=black><a href="+ message.link +"> "
	    			  		      		+ "<h2>"+ message.title+" [ "+ mformat.format(mdate) + "]</h2></b>";
	    			  		      /***********	
	    			  		      if( !message.thumbnail.equals("") )
	    			  		      {
	    			  		    	  content = content 
	    			  		    			  + "<img src=" + message.thumbnail + ">";
	    			  		      }
	    			  		      /***********
	    			  		      content = content 
	    			  		      		+ "<br>" + message.description+"</font> ["+ message.author + "]</a>"
	    			  		      		+ "<br>-----------------------------------------<br>"
	    			  		      		+ "<br>";
	    			  		      /***********
	    			  		    }	
	    			    		    /***********/
	    			    		
	    			    		
	    			    		stream.close();
	    			    		out.close();
	    			    		
	    			    	}
	    					
	    			    	/**************************************/
	    			    	/**************************************/
	    			    	/**************************************/
	    					
	    					
	    				}
	    				
	    				
	    			}
	    			else
	    			{
	    				Trace.trace(keywordfile + " file not found...");
	    				return;
	    			}
	    			
	    			
	    			
	    		}
	    	}
	    	else
	    	{
	    		Trace.trace(rssfile + " file not found.." );
	    		return;
	    	}
	    	
	    	
	    	//Files rssfile = new Files(path);
	    	/********************
	    	if( !rssfile.exists() ){
	    		//System.out.println("rssfile not exist :: " + rssfile.getPath());
	    		return;
	    	}else{
	    		BufferedReader reader = new BufferedReader(rssfile);
	    	}
	    	File srchkeywordfile = new File(srchkeywordfilepath);
	    	if( !srchkeywordfile.exists() ){
	    		//System.out.println("srchkeywordfile not exist :: " + srchkeywordfile.getPath());
	    		return;
	    	}
	    	for( int rsscount = 0 ; rsscount < rsslist.length ; rsscount ++ )
	    	{
	    		for( int srchkeywordcount = 0 ; srchkeywordcount < srchkeywordlist.length ; srchkeywordcount ++ )
	    		{
	    			
	    			
	    		}
	    		
	    	}
	    	/********************
	    	
	    	this.url = new URL(naver_rss + URLEncoder.encode(search_word,charset) );
	    	//http://newssearch.naver.com/search.naver?where=rss&query=
	    	
			Feed feed = this.readFeed();
		    //System.out.println(feed);
		    
		    blogwrite.setwtitle("["+feed.title + " " + feed.pubDate+"]");//title
		    content = content + "<a href="+naver_rss +  URLEncoder.encode(search_word,charset)  + ">";
		    
		    if( !feed.mimage.equals("")){
		    	content = content
		    			+ "<img src="+ feed.mimage + " width=20> ";	
		    }
		    content = content
		    		 +feed.description+"</a> " + feed.copyright+"<br><br>";
		    /************
		    for (FeedMessage message : feed.getMessages()) {
		      //System.out.println("thumbnail = " +  message.thumbnail + " ::: " + message);
		    	//DateFormat dateformat = new 
		    	SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
		    	Date mdate =  new Date(message.pubDate.toString()); 
		    			//mformat.parse(mdate);
		    	
		      content = content
		    		+ "-----------------------------------------"
  	    		    + "<b><font color=black><a href="+ message.link +"> "
		      		+ "<h2>"+ message.title+" [ "+ mformat.format(mdate) + "]</h2></b>";
		      /***********	
		      if( !message.thumbnail.equals("") )
		      {
		    	  content = content 
		    			  + "<img src=" + message.thumbnail + ">";
		      }
		      /***********
		      content = content 
		      		+ "<br>" + message.description+"</font> ["+ message.author + "]</a>"
		      		+ "<br>-----------------------------------------<br>"
		      		+ "<br>";
		      /***********
		    }	
		    /************/
		    //blogwrite.setwcontent(content);
		    //blogwrite.getBlogList();
		    //blogwrite.setBlogWrite();		
		    
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();    
	    }

	    
	}

	  public Feed readFeed() {
	    Feed feed = null;
	    try {
	      boolean isFeedHeader = true;
	      // Set header values intial to the empty string
	      String description = "";
	      String title = "";
	      String link = "";
	      String language = "";
	      String copyright = "";
	      String author = "";
	      String pubdate = "";
	      String guid = "";
	      String mimage = "";
	      String thumbnail = "";

	      // First create a new XMLInputFactory
	      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	      // Setup a new eventReader
	      InputStream in = read();
	      XMLEventReader eventReader = inputFactory.createXMLEventReader(in,charset);
	      // read the XML document
	      while (eventReader.hasNext()) {
	        XMLEvent event = eventReader.nextEvent();
	        //System.out.println( " :: " + event);
	        if (event.isStartElement()) {
	          String localPart = event.asStartElement().getName()
	              .getLocalPart();
	          
	          switch (localPart) {
	          case ITEM:
	            if (isFeedHeader) {
	              isFeedHeader = false;
	              feed = new Feed(title, link, description, language,
	                  copyright, pubdate,mimage);
	            }
	            event = eventReader.nextEvent();
	            break;
	          case TITLE:
	            title = getCharacterData(event, eventReader);
	            break;
	          case DESCRIPTION:
	            description = getCharacterData(event, eventReader);
	            break;
	          case LINK:
	            link = getCharacterData(event, eventReader);
	            break;
	          case GUID:
	            guid = getCharacterData(event, eventReader);
	            break;
	          case LANGUAGE:
	            language = getCharacterData(event, eventReader);
	            break;
	          case AUTHOR:
	            author = getCharacterData(event, eventReader);
	            break;
	          case PUB_DATE:
	            pubdate = getCharacterData(event, eventReader);
	            break;
	          case COPYRIGHT:
	            copyright = getCharacterData(event, eventReader);
	            break;
	          case MIMAGE:
		            mimage = getCharacterData(event, eventReader);
		            break;
	          case THUMBNAIL:
		            String[] thumbnail_array = event.toString().split("url=");
		            thumbnail = thumbnail_array[1].substring(1, thumbnail_array[1].length()-2);
		            break;
	          }
	          
	        } else if (event.isEndElement()) {
	        	
	          if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
	            FeedMessage message = new FeedMessage();
	            message.setAuthor(author);
	            message.setDescription(description);
	            message.setGuid(guid);
	            message.setLink(link);
	            message.setTitle(title);
	            message.setpubDate(pubdate);
				message.setThumbnail(thumbnail);
	            feed.getMessages().add(message);
	            event = eventReader.nextEvent();
	            continue;
	          }
	        }
	      }
	    } catch (XMLStreamException e) {
	      throw new RuntimeException(e);
	    }
	    return feed;
	  }

	  private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
	      throws XMLStreamException {
	    String result = "";
	    event = eventReader.nextEvent();
	    
	    if (event instanceof Characters) {
	      result = event.asCharacters().getData();
	      //System.out.println( " :: " + result);
	    }
	    
	    //System.out.println("event +++++++ " + event + " ::  result ==== " + event.asStartElement().getNamespaceURI("url"));
	    
	    return result;
	  }

	  private InputStream read() {
	    try {
	    	return url.openStream();
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			new RssWriter(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
