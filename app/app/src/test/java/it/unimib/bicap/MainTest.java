package it.unimib.bicap;

import java.io.IOException;
import java.util.*;
import org.junit.Test;
import com.google.gson.*;
import java.net.URL;
import java.net.URLConnection;

import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndaginiHeadList;

import static org.junit.Assert.*;


public class MainTest {
    @Test
    public void IndaginiHeadListTest() {
        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();
        assertNotEquals(indaginiHeadList, null);
    }

    @Test
    public void Indagine829533Test() throws IOException {
        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();
        IndagineBody indagineBody = null;
        String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/Indagine829533.json";
        try {
            URLConnection connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String response = scanner.next();
            scanner.close();
            Gson g = new Gson();
            indagineBody = g.fromJson(response, IndagineBody.class);
            assertNotEquals(indagineBody, null);
        }catch(Exception e){
            e.printStackTrace();
        }
        indagineBody.setHead(indaginiHeadList.getIndagineHeadFromId(829533));
    }

    @Test
    public void Indagine830616Test() throws IOException {
        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();
        IndagineBody indagineBody = null;
        String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/Indagine830616.json";
        try {
            URLConnection connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String response = scanner.next();
            scanner.close();
            Gson g = new Gson();
            indagineBody = g.fromJson(response, IndagineBody.class);
            assertNotEquals(indagineBody, null);
        }catch(Exception e){
            e.printStackTrace();
        }
        indagineBody.setHead(indaginiHeadList.getIndagineHeadFromId(830616));
    }

    @Test
    public void Indagine830075Test() throws IOException {
        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();
        IndagineBody indagineBody = null;
        String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/Indagine830075.json";
        try {
            URLConnection connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String response = scanner.next();
            scanner.close();
            Gson g = new Gson();
            indagineBody = g.fromJson(response, IndagineBody.class);
            assertNotEquals(indagineBody, null);
        }catch(Exception e){
            e.printStackTrace();
        }
        indagineBody.setHead(indaginiHeadList.getIndagineHeadFromId(830075));
    }

    public IndaginiHeadList getIndaginiHeadList(){
        String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/listaIndagini.json";
        try{
            URLConnection connection =  new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String response = scanner.next();
            scanner.close();
            Gson g = new Gson();
            return g.fromJson(response, IndaginiHeadList.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
