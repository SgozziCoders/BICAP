
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;

public class test {

	public static void main(String[] args) throws FileNotFoundException {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement jsonTree;
		HttpURLConnection urlConn = null;
		URL url = null;

	    try {
	    	url = new URL("https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/indagine.json");
			urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setDoInput (true);
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
	        {
				jsonTree = parser.parse(new InputStreamReader((InputStream)urlConn.getContent()));
				
				IndagineBody body = gson.fromJson(jsonTree, IndagineBody.class);
				
				System.out.println(jsonTree);
	        }
			
	    	url = new URL("https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/listaIndagini.json");
			urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setDoInput (true);
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
	        {
				jsonTree = parser.parse(new InputStreamReader((InputStream)urlConn.getContent()));
				
				IndaginiHeadList heads = gson.fromJson(jsonTree, IndaginiHeadList.class);
				
				System.out.println(jsonTree);
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		
		
		
		
		
		
		
		
	}

}
