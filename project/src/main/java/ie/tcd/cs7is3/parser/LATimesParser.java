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

public class LATimesParser{
    
	// 加载并处理LA Times文档的方法
	public ArrayList<Document> loadLaTimesDocs() throws IOException {
		String pathToLADocs = "../dataset/collections/latimes";
		// 打印开始加载的信息
		System.out.println("Loading LATimes collection");
		// 初始化文档列表
		ArrayList<Document> laDocs = new ArrayList<>();

		// 创建代表给定路径的文件夹对象
		File folder = new File(pathToLADocs);

		// 获取文件夹中的所有文件
		File[] allFiles = folder.listFiles();

		// 遍历每个DOC元素
		for (File file : allFiles) {
			// 使用Jsoup解析文件内容
			org.jsoup.nodes.Document allContent = Jsoup.parse(file, null, "");
			// 获取所有DOC元素
			Elements docs = allContent.select("DOC");
            // 遍历每个DOC元素
			for (Element doc : docs) {
				String docNo, headline, text;
				// 获取DOCNO元素的文本
				docNo = (doc.select("DOCNO").text());
				// 获取HEADLINE中的P元素的文本
				headline = (doc.select("HEADLINE").select("P").text());
				// 获取TEXT中的P元素的文本
				text = (doc.select("TEXT").select("P").text());
				// 创建Lucene文档并添加到列表中
				laDocs.add(createDocument(docNo, headline, text));
			}
		}
		System.out.println("Totally loaded: " + laDocs.size() + " documents");
		// 返回文档列表
		return laDocs;
	}

	// 将提取的数据转换为Lucene文档的方法
	private static org.apache.lucene.document.Document createDocument(String docNo, String headline, String text) {
		// 创建Lucene文档对象
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
		// 添加docno字段
		document.add(new StringField("docno", docNo, Field.Store.YES));
		// 添加headline字段
		document.add(new TextField("headline", headline, Field.Store.YES));
		// 添加text字段
		document.add(new TextField("text", text, Field.Store.YES));
		// 返回Lucene文档
		return document;
	}
}
