package ie.tcd.cs7is3.parser;

import ie.tcd.cs7is3.model.FTObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import java.io.File;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTParser {
	private static BufferedReader br;

	// 静态变量，用于存储转换后的Lucene文档列表
	private static ArrayList<Document> ftDocList;

    // String ftDirectory = "../dataset/collections/ft/";
	// String ftDirectory = "../../../dataset/collections/ft/";

    public ArrayList<Document> loadFTDocs() throws IOException
    {
		String ftDirectory = "../dataset/collections/ft/";
        // 打印开始加载的信息
        System.out.println("Loading FT collections");
        // 初始化文档列表
        ftDocList = new ArrayList<>();
		File rootDir = new File(ftDirectory);
		File[] directories = rootDir.listFiles(File::isDirectory);
    	if (directories != null)
		{
        	for (File directory : directories)
			{
            	File[] files = directory.listFiles(); // 处理所有文件
            	if (files != null)
				{
                	for (File file : files)
					{
                    	if (file.isFile() && !file.getName().equals("readfrcg") && !file.getName().equals("readmeft")) 
						{   // 确保是文件且不是排除的文件
                        	br = new BufferedReader(new FileReader(file));
                        	process();
						}
                    }
                }
            }
        }
        // 打印加载完成的信息
        System.out.println("Totally loaded: " + ftDocList.size() + " documents");
        // 返回文档列表
        return ftDocList;
    }

	// 从文件中读取内容并处理的方法
	private static void process() throws IOException {
		// 读取文件内容
		String file = readFile();
		// 使用Jsoup解析文件内容
		org.jsoup.nodes.Document document = Jsoup.parse(file);
		// 获取所有doc标签的元素
		List<Element> list = document.getElementsByTag("doc");

        String docNo, profile, date, headline, pub, page, byline, dateline, text;

		// 遍历每个doc元素
		for (Element doc : list) {
			// 创建FTObject对象，用于存储解析后的数据
			FTObject ftObject = new FTObject();
			// 下面的if语句块用于解析并设置FTObject对象的属性
			// getElementsByTag是jsoup的函数
			// set函数在FTObject中定义
            docNo = doc.select("DOCNO").text().trim();
            profile = doc.select("PROFILE").text().trim();
            date = doc.select("DATE").text().trim();
            headline = doc.select("HEADLINE").text().trim();
            pub = doc.select("PUB").text().trim();
            page = doc.select("PAGE").text().trim();
            byline = doc.select("BYLINE").text().trim();
            dateline = doc.select("DATELINE").text().trim();
            text = doc.select("TEXT").text().trim();

			if (!doc.getElementsByTag("DOCNO").isEmpty())
				ftObject.setDocNo(docNo);
			if (!doc.getElementsByTag("PROFILE").isEmpty())
				ftObject.setProfile(profile);
			if (!doc.getElementsByTag("DATE").isEmpty())
				ftObject.setDate(date);
			if (!doc.getElementsByTag("HEADLINE").isEmpty())
				ftObject.setHeadline(headline);
			if (!doc.getElementsByTag("PUB").isEmpty())
				ftObject.setPub(pub);
			if (!doc.getElementsByTag("PAGE").isEmpty())
				ftObject.setPage(page);
			if (!doc.getElementsByTag("BYLINE").isEmpty())
				ftObject.setByLine(byline);
			if (!doc.getElementsByTag("DATELINE").isEmpty())
				ftObject.setDateLine(dateline);
			if (!doc.getElementsByTag("TEXT").isEmpty())
				ftObject.setText(text);
			// 将FTObject转换为Lucene文档并添加到列表中
			ftDocList.add(createFTDocument(ftObject));
		}
	}

	// 将FTObject转换为Lucene文档的方法
	private static Document createFTDocument(FTObject ftObject) {
		// 创建Lucene文档对象
		Document document = new Document();
		// 添加字段到文档
		document.add(new StringField("docno", ftObject.getDocNo() != null ? ftObject.getDocNo() : "", Field.Store.YES));
    	document.add(new StringField("profile", ftObject.getProfile() != null ? ftObject.getProfile() : "", Field.Store.YES));
   	 	document.add(new StringField("date", ftObject.getDate() != null ? ftObject.getDate() : "", Field.Store.YES));
    	document.add(new TextField("headline", ftObject.getHeadline() != null ? ftObject.getHeadline() : "", Field.Store.YES));
    	document.add(new TextField("pub", ftObject.getPub() != null ? ftObject.getPub() : "", Field.Store.YES));
    	document.add(new TextField("page", ftObject.getPage() != null ? ftObject.getPage() : "", Field.Store.YES));
    	document.add(new TextField("byline", ftObject.getByLine() != null ? ftObject.getByLine() : "", Field.Store.YES));
    	document.add(new TextField("dateline", ftObject.getDateLine() != null ? ftObject.getDateLine() : "", Field.Store.YES));
    	document.add(new TextField("text", ftObject.getText() != null ? ftObject.getText() : "", Field.Store.YES));
		
		// 返回文档
		return document;
	}

	// 从BufferedReader中读取文件内容的方法
	private static String readFile() throws IOException {
		try {
			// 用于构建文件内容的StringBuilder
			StringBuilder sb = new StringBuilder();
			// 读取一行
			String line = br.readLine();

			// 当读到文件末尾时停止
			while (line != null) {
				// 添加行到StringBuilder
				sb.append(line);
				// 添加换行符
				sb.append("\n");
				// 读取下一行
				line = br.readLine();
			}
			// 返回文件内容
			return sb.toString();
		} finally {
			// 关闭BufferedReader
			br.close();
		}
	}
}
