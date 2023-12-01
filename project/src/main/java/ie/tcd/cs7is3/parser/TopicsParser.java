package ie.tcd.cs7is3.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import ie.tcd.cs7is3.model.TopicsObject;

public class TopicsParser {

    private static List<TopicsObject> topicsList;

    public static List<TopicsObject> getTopicsDocs() throws IOException {

        topicsList = new ArrayList<>();

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(new File("../dataset/queries/topics"), "UTF-8", "");
        
        jsoupDoc.outputSettings(new Document.OutputSettings().prettyPrint(false)); 

        Elements topics = jsoupDoc.select("top");

        for (Element topic : topics) {
            // System.out.println(topic.toString());

            String num = topic.getElementsByTag("num").text();
            String title = topic.getElementsByTag("title").text();
            String descStr = topic.getElementsByTag("desc").text();
            String narrativeStr = topic.getElementsByTag("narr").text();

            Pattern numberPattern = Pattern.compile("(\\d+)");
            Matcher numberMatcher = numberPattern.matcher(num);
            String number = "";
            if(numberMatcher.find()) {
                number = numberMatcher.group().trim();
            }

            descStr = descStr.replace("\n"," ");
            Pattern descPattern = Pattern.compile("Description: (.*)Narrative");
            Matcher descMatcher = descPattern.matcher(descStr);
            String desc = "";
            if(descMatcher.find()) {
                desc = descMatcher.group(1).trim();
            }

            String narrative = narrativeStr.replace("\n"," ").replace("Narrative: ","").trim();
            
            // System.out.println("1 "+ number);
            // System.out.println("2 "+ title);
            // System.out.println("3 "+ desc);
            // System.out.println("4 "+ narrative);


            TopicsObject topicsObject = new TopicsObject(number, title, desc, narrative);
            
            topicsList.add(topicsObject);
        }

        System.out.println("Total loaded " + topicsList.size() + " topics");
        return topicsList;
    }
    
}
