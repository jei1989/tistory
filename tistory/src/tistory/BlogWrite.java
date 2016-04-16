package tistory;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.activation.MimetypesFileTypeMap;
import javax.lang.model.util.Elements;

import org.apache.xmlrpc.client.*;
import org.apache.xmlrpc.*;

import com.google.api.client.*;
import com.google.api.client.json.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class BlogWrite {

	/**********************
	static final String API_URL = "https://hjya.tistory.com/api";
	static final String API_ID = "2046802";
    static final String USER_ID = "hjya1989@daum.net";
    static final String USER_PASSWD = "hyun1255632";
    static final String API_PASSWORD = "7Y2Y2CE7";
    /**********************
	static final String API_URL = "https://api.blog.naver.com/xmlrpc";
	static final String API_ID = "hjya1989";
    static final String USER_ID = "hjya1989";
    static final String USER_PASSWD = "hyun1255632";
    static final String API_PASSWORD = "51c8f6960c202713490e039d5dd67203";
    /**********************/
    
	static String API_URL;
	static String API_ID;
    static String USER_ID;
    static String USER_PASSWD;
    static String API_PASSWORD;
	
    
    public XmlRpcClient client  = null;
    public XmlRpcClientConfigImpl config = null; 
    
    public String wtitle = "";
    public String wcontent = "";
    
    public String bloglistfile = "";
    
    public String[] ginfo;
    
    public static void main(String args[])
    {
    	if( args != null )
    	{
    		if( args.length > 0 ){
		    	BlogWrite write = new BlogWrite(args);
    			//new BlogWrite();
		    	//write.getBlogList();
		    	//write.getPost(postid);
    		}else{
    			System.out.println("Usage -t bloglist.txt");
    		}
    	}else{
			System.out.println("Usage -t bloglist.txt");
		}
    	
    }
    

    
    public BlogWrite()
    {
    	try{
    		/********************************
		SimpleDateFormat zformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		Date mdate =  zformat.parse("Fri, 08 Apr 2016 22:56:04 +0900");//
		Date today_date = new Date();
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTime(mdate);
		Calendar todaycal = Calendar.getInstance(Locale.ENGLISH);
		
		
		System.out.println( cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH) );
		System.out.println( cal.get(Calendar.HOUR_OF_DAY) >=23 );
		/********************************/
    		new Date();
    		System.out.println( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).format(new Date()) );
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
	    //	mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);    	
    	
    	//updateTest();
    }
    
    public BlogWrite(String[] args)
    {
    	try{
    		client = new XmlRpcClient();
    		config = new XmlRpcClientConfigImpl();
    		//config.setServerURL(new URL(ginfo[1]));
    		if( args[0].toString().trim().equals("-t") )
    		{
    			bloglistfile = args[1].toString().trim();
    		}else
    		{
    			bloglistfile = "tistorybloglist.txt";
    			System.out.println("Usage -t bloglist.txt");
    			//return;
    		}
    		
    		Path path = Paths.get(bloglistfile);
	    	if( new File(path.toAbsolutePath().toString()).exists() )
	    	{
	    		String line = "";
	    		BufferedReader br = new BufferedReader(new FileReader(path.toAbsolutePath().toString()));
	    		while(( line = br.readLine() ) != null )
	    		{
	    			String[] info = line.split("\\|");
	    			ginfo = info;
	    			Trace.trace(info[0] + " :: " + info[1] + " :: [START] ================================ ");
	    			getBlogList(info);
	    			Trace.trace(info[0] + " :: " + info[1] + " :: [END] ================================ ");
	    			
	    			
	    		}
	    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    public void setwtitle(String wtitle){
    	
    	this.wtitle = wtitle;
    }
    
    public void setwcontent(String wcontent)
    {
    	this.wcontent = wcontent;
    }
    
    public void getBlogList(String[] info)
    {
    	try{
    		
    		
    		config.setServerURL(new URL(info[1]));
	        client.setConfig(config);
	
	    	Map<String, Object> contents = new HashMap<String, Object>();
	        String categories[] = new String[] {info[6]};    	
	
	        List<Object> params = new ArrayList<Object>();
	        // 여기에는 API정보에 설정된 ID가 들어간다
	        params.add(info[2]);  
	        // 사용자 계정 즉 이메일이 들어간다.
	        params.add(info[3]);
	        // API 암호 또는 유저암호 둘다 가능하다.
	        params.add(info[4]);         
	        
	        /******************************
	        Object[] catobj = (Object[])client.execute("metaWeblog.getCategories", params);
	        //for( String obj catobj)
	        for( int k = 0; k < catobj.length ; k++)
	        {
	        	//System.out.println(" rsString :: " + catobj[k].toString());
	        	HashMap catdetail = (HashMap)catobj[k];
	        	//System.out.println(((String)catdetail.get("title")));
	        	
	        	//
	        	
	        }
	        /******************************/
	        boolean isupdate = false;
	        String postid = "";
	        Object[] postobj = (Object[])client.execute("metaWeblog.getRecentPosts", new Object[]{ info[2],info[3],info[4], 1000 } );
	        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
			String todaydate = mformat.format(new Date());
			String title = "";
			System.out.println(" postobj =================================== ");
	        for( int k = 0; k < postobj.length ; k++)
	        //for( int k = 0; k < 1 ; k++)
	        {
	        	//if( isupdate == false ){

		        	//System.out.println( postobj[k].toString() );
		        	
		        	try{
			        	HashMap catdetail = (HashMap)postobj[k];
			        	title = todaydate + "_" + info[0].trim();
			        	
			        	Trace.trace(title);
			        	
			        	String tmptitle = catdetail.get("title").toString().trim();
						if(tmptitle.equals(title))
						{
							isupdate = true;
							postid = (String)catdetail.get("postid");
							////////////////////////////////////////UPdate
							Trace.trace(" " +  tmptitle.equals( title  )   );
							Trace.trace("isupdate == " + isupdate + "  postid == " + postid + " title == " + title + "   tmptitle == " + tmptitle);
							/***********/
							try{
								updatePost(info,postid,catdetail.get("title").toString());
							}catch(Exception ex){
				        		Trace.trace(ex.toString());
				        	}
							/***********/
							break;
						}
			        	//getPost((String)catdetail.get("postid"));
		        	}catch(Exception ex){
		        		ex.printStackTrace();
		        	}
	        	//}
				/*******************/
	        }
	        
        	/*******************/
	        
	        if( (!isupdate) )
	        {
	        	try{
	        		newPost(info,title);
	        	}catch(Exception ex){
	        		Trace.trace(ex.toString());
	        	}
	        }
	        /*******************/
	        System.out.println(" postobj END =================================== ");
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    }
    

    public void updateTest()
    {
       	try{
    		Trace.trace("updateTest ================= ");

    		client = new XmlRpcClient();
    		config = new XmlRpcClientConfigImpl();
    		config.setServerURL(new URL(ginfo[1]));    		
    		
    		String wcontent = "";
    		String postid = "2046802-342";
    		//String[] splitetmp = postid.split("-");
    		//"+ splitetmp[1].trim().toString()
    		//config.setServerURL(new URL("https://hjya.tistory.com/"));
	        //client.setConfig(config); 
    		
	    	Map<String, Object> contents = new HashMap<String, Object>();
	        String categories[] = new String[] {"옵션데이터"};    	
	        List<Object> params = new ArrayList<Object>();
	        params.add(postid);  
	        // 여기에는 API정보에 설정된 ID가 들어간다
	        //params.add(API_ID);  
	        // 사용자 계정 즉 이메일이 들어간다.
	        params.add(this.USER_ID);
	        // API 암호 또는 유저암호 둘다 가능하다.
	        params.add(this.API_PASSWORD);
	        //params.add(this.USER_PASSWD); 
	
	        wcontent = "<br> ====== test 777 <br>";

	        contents.put("title", "test444");
	        contents.put("description", wcontent);
	        //SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
			//contents.put("dateCreated", todaydate);
	        //contents.put("categories", categories);
	        
	        // 블로그 컨텐츠 
	        params.add(contents);
	        // 공개여부 true이면 공개, false면 비공개
	        params.add(new Boolean(false));
	
	        
	        client.setConfig(config);
	        
	        //Trace.trace(params.toString());
	        try{
	        	client.execute("metaWeblog.editPost", params);
	        }catch(Exception ext){
	        	Trace.trace(ext.toString());
	        }
	        //String rsString = (String) client.execute("metaWeblog.editPost", params);
	        //Trace.trace(rsString);
    	}catch(Exception e){
    		Trace.trace(e.toString());
    	}
    	
    }
    
    public void updatePost(String[] info, String postid, String title)
    {
    	try{
    		Trace.trace("updatePost ================= ");
    		
    		String wcontent = " ";
    		config.setServerURL(new URL(info[1]));
	        //client.setConfig(config); 
    		
	    	Map<String, Object> contents = new HashMap<String, Object>();
	        String categories[] = new String[] {info[6]};    	
	        
	        List<Object> params = new ArrayList<Object>();
	        params.add(postid);  
	        // 여기에는 API정보에 설정된 ID가 들어간다
	        //params.add(info[2]);  
	        // 사용자 계정 즉 이메일이 들어간다.
	        params.add(info[3]);
	        // API 암호 또는 유저암호 둘다 가능하다.
	        params.add(info[4]);        	
	
	        contents.put("postid", postid);
	        contents.put("categories", categories);
	        contents.put("title", title);
	        contents.put("dateCreated", new Date());
	        
	        Path pathimsi = Paths.get(title+".txt");
	        FileInputStream input = new FileInputStream(new File(pathimsi.toAbsolutePath().toString()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,info[7].trim().toString()));
			
			String imsi = "";
			int num = 1;
			
			SimpleDateFormat mformat = null;//
			Calendar cal = null;
			Date mdate =  null;//
			
			Date today_date = new Date();
			Calendar todaycal = Calendar.getInstance(Locale.ENGLISH);			
			while( ( imsi = reader.readLine() ) != null )
			{
				String[] detailcontent = imsi.split("\\|");
  		    	
  		    	try{
  		    		mformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);//"yyyy-MM-dd HH:mm:ss",Locale.KOREA);
	  		    	mdate =  mformat.parse( detailcontent[3].trim().toString() );//
	  		    	mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
	  		    	//Trace.trace("mformat ================= " + mformat.format(mdate) + "  detailcontent[3] == " + detailcontent[3]);

	    			cal = Calendar.getInstance(Locale.ENGLISH);
	    			cal.setTime(mdate);
	    			
	    			if( (  cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)   )
	    					|| ( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 ))
	    			{
	    				
	  		    	wcontent = wcontent
							+
					"<table>" +
					"<tr>" +
						"<td rowspan=3 width=4 bgcolor=#87CEEB></td>" +
						"<td rowspan=3 width=2></td>" +
						"<td bgcolor=#00FFFF width=2></td>" +
						"<td width=3>" +
						"<td><font size=4 color=#4169E1><a href=" + detailcontent[1]  + ">" + 
							"<b>"+ String.valueOf(num) + ". " + detailcontent[0] +"</b></font></a></td>" +
					"</tr>" +
					"<tr>"+
						"<td></td>" +
						"<td></td>" +
						"<td>" +
						"<a href=" + detailcontent[1]  + ">";
					
					 if( detailcontent.length >= 6 )
		  		      {
						 wcontent = wcontent 
		  		    			  + "<br><img src=" +  detailcontent[5] + " align=center>";
		  		      }
					 	wcontent = wcontent + "</a></td>"
					 + "</tr>"
					 + "<tr>";
					 
					 	wcontent = wcontent + 
								"<td></td>" +
								"<td></td>" +
					 	"<td>" +
					 		"<br><a href=" + detailcontent[1]  + "><font size=3>"+ detailcontent[2] +"</font></a>" +	
					 		"&nbsp&nbsp<font size=3>[" + detailcontent[4]+" "+ mformat.format(mdate) +"]</font>" +
						"</td>" +		
					"</tr>" +
					"<tr>" +
						"<td colspan=5 height=10></td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan=5><hr size=1  color=#DCDCDC></td>" +
					"</tr>" +
					"</table>" +
					"<br>";
	  		    	/****************************
					wcontent = wcontent
		  		    		+ "-----------------------------------------"
		    	    		    + "<b><font color=black><a href="+ detailcontent[1] +"> "
		  		      		+ "<h2>"+ detailcontent[0]+" [ "+ mformat.format(mdate) + "]</h2></b>";
		  		      /***********
		  		      if( detailcontent.length >= 6 )
		  		      {
		  		    	wcontent = wcontent 
		  		    			  + "<img src=" +  detailcontent[5] + ">";
		  		      }
		  		      /***********
		  		    wcontent = wcontent 
		  		      		+ "<br>" + detailcontent[2]+"</font> ["+ detailcontent[4] + "]</a>"
		  		      		+ "<br>-----------------------------------------<br>"
		  		      		+ "<br>";
		  		      /***********/
		  		  /****************************/
	    			}//if( (  cal.get(Calendar.DAY_OF_MONTH) == todaycal.get(Calendar.DAY_OF_MONTH)   )
						//|| ( cal.get(Calendar.HOUR_OF_DAY) >=23 && cal.get(Calendar.MINUTE) >= 50 ))
  		    	}catch(Exception eex){
  		    		Trace.trace(eex.toString());
  		    	}
  		    	num = num + 1;
			}
			
			reader.close();
			input.close();
			
			mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
			contents.put("dateCreated", mformat.format(new Date()));
			Trace.trace("[DATE]=================== :: " + mformat.format(new Date()).toString());
	        contents.put("description", wcontent);        
	        
	        // 블로그 컨텐츠 
	        params.add(contents);
	        // 공개여부 true이면 공개, false면 비공개
	        params.add(new Boolean(true));
	        //params.add(Boolean.TRUE);
	
	        
	        client.setConfig(config);
	        
	        //Trace.trace(params.toString());
	        try{
	        	
	        	client.execute("metaWeblog.editPost", params);
	        }catch(Exception ext)
	        {
	        	Trace.trace(ext.toString());
	        }
    	}catch(Exception ex){
    		Trace.trace(ex.toString());
    	}
    }
    
    public void newPost(String[] info,String title)
    {
    	try{
    		Trace.trace("newPost ================= ");
    		
    		String wcontent = " ";
	    	Map<String, Object> contents = new HashMap<String, Object>();
	    	
	        String categories[] = new String[] {info[6]};    	
	
	        List<Object> params = new ArrayList<Object>();
	        // 여기에는 API정보에 설정된 ID가 들어간다
	        params.add(info[2]);  
	        // 사용자 계정 즉 이메일이 들어간다.
	        params.add(info[3]);
	        // API 암호 또는 유저암호 둘다 가능하다.
	        params.add(info[4]);           
	        
	        contents.put("categories", categories);
	        contents.put("title", title);
	       
	        
	        Path pathimsi = Paths.get(title+".txt");
	        Trace.trace(pathimsi.toAbsolutePath()+"");
	        FileInputStream input = new FileInputStream(new File(pathimsi.toAbsolutePath().toString()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,info[7].trim().toString()));
			
			String imsi = "";
			wcontent = "";
			int num = 1;
			SimpleDateFormat mformat = null;			
	    	Date mdate = null;
	    	while( ( imsi = reader.readLine() ) != null )
			{
				
				String[] detailcontent = imsi.split("\\|");
				
				Trace.trace(detailcontent[3]);
				
  		    	//Date mdate = new Date();
  		    	try{
  		    		
  		    		mformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);//"yyyy-MM-dd HH:mm:ss",Locale.KOREA);
  		    		
	  		    	mdate =  mformat.parse( detailcontent[3].trim().toString() );//
	  		    	mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
	  		    	//mformat.format(mdate);
	  		    	//Date mdate =  new Date(detailcontent[3].trim().toString()); 	

	  		    	/***********/
	  		    	wcontent = wcontent
							+
					"<table>" +
					"<tr>" +
						"<td rowspan=3 width=4 bgcolor=#87CEEB></td>" +
						"<td rowspan=3 width=2></td>" +
						"<td bgcolor=#00FFFF width=2></td>" +
						"<td width=3>" +
						"<td><font size=4 color=#4169E1><a href=" + detailcontent[1]  + ">" + 
							"<b>"+ String.valueOf(num) + ". " + detailcontent[0] +"</b></font></a></td>" +
					"</tr>" +
					"<tr>"+
						"<td></td>" +
						"<td></td>" +
						"<td>" +
						"<a href=" + detailcontent[1]  + ">";
					
					 if( detailcontent.length >= 6 )
		  		      {
						 wcontent = wcontent 
		  		    			  + "<br><img src=" +  detailcontent[5] + " align=center>";
		  		      }
					 	wcontent = wcontent + "</a></td>"
					 + "</tr>"
					 + "<tr>";
					 
					 	wcontent = wcontent + 
								"<td></td>" +
								"<td></td>" +
					 	"<td>" +
					 		"<br><a href=" + detailcontent[1]  + "><font size=3>"+ detailcontent[2] +"</font></a>" +	
					 		"&nbsp&nbsp<font size=3>[" + detailcontent[4]+" "+ mformat.format(mdate) +"]</font>" +
						"</td>" +		
					"</tr>" +
					"<tr>" +
						"<td colspan=5 height=10></td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan=5><hr size=1  color=#DCDCDC></td>" +
					"</tr>" +
					"</table>" +
					"<br>";
					/*************************************
					/*************************************
		  		    		+ "-----------------------------------------"
		    	    		    + "<b><font color=black><a href="+ detailcontent[1] +"> "
		  		      		+ "<h2>"+ detailcontent[0]+" [ "+ mformat.format(mdate) + "]</h2></b>";
		  		      /***********
		  		      if( detailcontent.length >= 6 )
		  		      {
		  		    	wcontent = wcontent 
		  		    			  + "<img src=" +  detailcontent[5] + ">";
		  		      }
		  		      /***********
		  		    wcontent = wcontent 
		  		      		+ "<br>" + +"</font> ["+  + "]</a>"
		  		      		+ "<br>-----------------------------------------<br>"
		  		      		+ "<br>";
		  		      /***********/
		  		  /*************************************/
		  		  /*************************************/
					
					
					//wcontent = wcontent + imsi;
				}catch(Exception eex){
		    		Trace.trace(eex.toString());
		    	}
  		    	num = num + 1;
			}
			
			reader.close();
			input.close();
	                
			//contents.put("dateCreated", mformat.format(new Date()));
	        contents.put("description", wcontent);
	
	        // 블로그 컨텐츠 
	        params.add(contents);
	        // 공개여부 true이면 공개, false면 비공개
	        //params.add(new Boolean(true)); 
	        params.add(Boolean.TRUE);

	        client.setConfig(config);
	        //if( wcontent != "" ){
	        	String rsString = (String) client.execute("metaWeblog.newPost", params);
	        //}
	          	
    	}catch(Exception ex){
    		Trace.trace(ex.toString());
    	}
    }
    
    public void getPost(String postid)
    {
    	try{
	    	Object[] postobj = (Object[])client.execute("metaWeblog.getPost", new Object[]{ postid,ginfo[3],ginfo[4], 30 } );
	        
	        for( int k = 0; k < postobj.length ; k++)
	        {
	        	System.out.println(" getPost :: " + postobj[k].toString() );
	        	try{
		        	HashMap catdetail = (HashMap)postobj[k];
		        	//System.out.println(((String)catdetail.get("title")));
	        	}catch(Exception ex){
	        		ex.printStackTrace();
	        	}
	
	        	//
	        	
	        }
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    public void setBlogWrite()
    {
        try {
        	

            Map<String, Object> contents = new HashMap<String, Object>();
            String categories[] = new String[] {"연예인/송중기"};
            contents.put("categories", categories);
            contents.put("title", wtitle);
            contents.put("description", wcontent);
            //contents.put("mt_keywords", "태그1, 태그2, 태그3");
            List<Object> params = new ArrayList<Object>();
            // 여기에는 API정보에 설정된 ID가 들어간다
            params.add(API_ID);  
            // 사용자 계정 즉 이메일이 들어간다.
            params.add(USER_ID);
            // API 암호 또는 유저암호 둘다 가능하다.
            params.add(API_PASSWORD); 
            // 블로그 컨텐츠 
            params.add(contents);
            // 공개여부 true이면 공개, false면 비공개
            params.add(new Boolean(true)); 

            
            client.setConfig(config);
            String rsString = (String) client.execute("metaWeblog.newPost", params);
            System.out.println(rsString);

            //getNewMediaObject("D:\\eclipse\\workspace\\tistory\\ws-commons-util-1.0.1.jar");
            
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }    
    }
    /************************************
    public void gSearch()
    {
    	
		//String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
    	
		String query = "minecraft";//programcreek";
		String address = "http://news.google.com/news?q=";
		String charset = "UTF-8";    	
    

		try{
			
			URL url = new URL("http://kr.hani.feedsportal.com/c/34762/f/640648/index.rss");
			BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream(),charset));
			String srccode = "";
			String line;
			while((line = buff.readLine()) != null){
				System.out.println(line);
				if( line.contains("<title>"))
				{
					int firstpos = line.indexOf("<title>");
					String temp = line.substring(firstpos);
					temp = temp.replace("<title>", "");
					int lastpos = temp.indexOf("</title>");
					temp = temp.substring(0, lastpos);
					srccode = srccode + temp + "\n";
					
				}
				srccode += line;
			}
			buff.close();
			wcontent = srccode;
			System.out.println(srccode);
			//return srcdoe;
			
			/***************
			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			
			GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
	 
			int total = results.getResponseData().getResults().size();
			System.out.println("total: "+total);
			
			wtitle = query;
			
			
			// Show title and URL of each results
			for(int i=0; i<=total-1; i++){
				String imsiURL =  URLDecoder.decode(results.getResponseData().getResults().get(i).getUrl());
				String content = results.getResponseData().getResults().get(i).getTitle();
				System.out.println("<a href=" +imsiURL + ">"+ content + "</a>");
				//System.out.println("URL: " +  + "\n");
				wcontent = wcontent + "<a href=" +imsiURL + ">"+ content + "</a><br><br>";
				
			}

		}catch(Exception ex){
			
			ex.printStackTrace();
		}
    }
/************************************/
    
    /***************
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new BlogWrite();
	}
	/***************/
	
	/**
	  * 파일을 업로드한다.
	  * @return String returnUrl
	  */

	/***************    
    
	 public String getNewMediaObject(String filepath){
	   String returnUrl = "";
	  
	   File file = new File(filepath);

	   try {
	     Map<String, Object> fileMap = new HashMap<String, Object>();  
	     fileMap.put("name", file.getName());
	     fileMap.put("type", new MimetypesFileTypeMap().getContentType(file));
	     fileMap.put("bits", getFileData(file));
	  
	     Map<String, Object> retMap = (HashMap)this.client.execute("metaWeblog.newMediaObject", new Object[] {API_ID, USER_ID, API_PASSWORD, fileMap});
	   
	     if(retMap != null) returnUrl = (String) retMap.get("url");
	     } catch (Exception ex) {
	       ex.printStackTrace();
	       System.out.println("ERROR: " + ex.getMessage());
	     }
	     System.out.println("url==>"+returnUrl);
	     return returnUrl;
	  }
	 
	  private static byte[] getFileData( File file ) throws IOException {
	    int total = 0;
	    int length = (int) file.length();
	    byte[] ret = new byte[length];
	    FileInputStream reader = new FileInputStream( file );
	    while ( total < length ) {
	       int read = reader.read( ret );
	       if ( read < 0 ) throw new IOException( "fail read file: " + file );
	       total += read;
	    }
	    return ret;
	  }
	
	
}

class GoogleResults{
	 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
	/***************/    
}
