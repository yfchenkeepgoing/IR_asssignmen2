package ie.tcd.cs7is3.parser;

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


public class FR94Parser {

	private static ArrayList<Document> FR94DocList;

	public List<Document> getFr94Documents() throws IOException{
		FR94DocList = new ArrayList<>();

		System.out.println("Loading FR94 collections");

		File[] forders = new File("../dataset/collections/fr94/").listFiles(File::isDirectory);

		String docno,text,title;

		for (File forder : forders) {
			File[] files = forder.listFiles();

			for (File file : files) {
				org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
				
				Elements documents = d.select("DOC");

				for (Element document : documents) {
					// System.out.println(document.toString());

					document.select("ADDRESS").remove();
                    document.select("SIGNER").remove();
                    document.select("SIGNJOB").remove();
                    document.select("BILLING").remove();
                    document.select("FRFILING").remove();
                    document.select("DATE").remove();
                    document.select("RINDOCK").remove();

					docno = document.select("DOCNO").text();
                    text = document.select("TEXT").text();
                    title = document.select("DOCTITLE").text();
					// System.out.println("docno:" + docno);
					// System.out.println("text:" + text);
					// System.out.println("title:" + title);

					Document doc = new Document();
					doc.add(new StringField("docno", docno, Field.Store.YES));
					doc.add(new TextField("text", text, Field.Store.YES));
					doc.add(new TextField("headline", title, Field.Store.YES));
					FR94DocList.add(doc);

					// break;
				}
				// break;
			}
			// break;

		}
		System.out.println("Totally loaded: " + FR94DocList.size() + " documents");

		return FR94DocList;
	}

}