package ie.tcd.cs7is3.parser;

// import ie.tcd.cs7is3.model.FBISObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FBISParser {

	private static ArrayList<Document> FBISDocList;

    public List<Document> getFBISDocuments() throws IOException{

        FBISDocList = new ArrayList<>();
        System.out.println("Loading FBIS collection");

        // Directory dir = FSDirectory.open(Paths.get("../dataset/collections/fbis/"));

        File[] files = new File("../dataset/collections/fbis/").listFiles();

        String docNo;
        String ht;
        String header;
        String h3;
        String ti;
        String text;

        for(File file: files){
            // System.out.println(file.getAbsolutePath());
            org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");

            Elements documents = d.select("DOC");

            for(Element doc : documents){
                // System.out.println(doc.toString());

                docNo = doc.select("docno").text().trim();
                ht = doc.select("ht").text().trim();
                header= doc.select("header").text().trim();
                h3 = doc.select("h3").text().trim();
                ti = doc.select("ti").text().trim();
                text = doc.select("text").text().trim();

                // System.out.println(docNo);
                // System.out.println(ht);
                // System.out.println(header);
                // System.out.println(h3);
                // System.out.println(ti);
                // System.out.println(text);
                
                Document document = new Document();
                document.add(new StringField("docno", docNo, Field.Store.YES));
                document.add(new StringField("ht", ht, Field.Store.YES));
                document.add(new TextField("header", header, Field.Store.YES));
                document.add(new StringField("h3", h3, Field.Store.YES));
                document.add(new TextField("headline", ti, Field.Store.YES));
                document.add(new TextField("text", text, Field.Store.YES));

                FBISDocList.add(document);
            }
            // break;
        }

        System.out.println("Totally loaded: " + FBISDocList.size()+ " documents");
        return FBISDocList;
    }

    
}