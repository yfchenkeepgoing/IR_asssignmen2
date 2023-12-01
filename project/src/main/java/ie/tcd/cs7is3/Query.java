package ie.tcd.cs7is3;

import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import ie.tcd.cs7is3.model.TopicsObject;
import ie.tcd.cs7is3.parser.TopicsParser;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.document.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.*;

public class Query
{
    private String INDEX_DIRECTORY;
    private static int MAX_RESULTS = 1000;
    private Analyzer analyzer = null;
    
    public Query(){
        
    }
    
    public void queryIndex(Analyzer analyzer, int similarityChoice, String analyzerName) throws IOException, ParseException, org.apache.lucene.queryparser.surround.parser.ParseException {
        this.INDEX_DIRECTORY = "../index/"+analyzerName+"_index";
        Files.createDirectories(Paths.get("../result/"));
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        String fileName = null;
        this.analyzer = analyzer;

        switch (similarityChoice) {
            case 1:
                isearcher.setSimilarity(new BM25Similarity(0.75f, 0.7f));
                System.out.println("Query with samilarity: BM25Similarity()");
                fileName = "../result/"+analyzerName+"_BM25.txt";
                break;
            case 2:        
                isearcher.setSimilarity(new BooleanSimilarity());
                System.out.println("Query with samilarity: BooleanSimilarity()");
                fileName = "../result/"+analyzerName+"_Boolean.txt";
                break;
            case 3:
                isearcher.setSimilarity(new ClassicSimilarity());
                System.out.println("Query with samilarity: ClassicSimilarity()");
                fileName = "../result/"+analyzerName+"_classic.txt";
                break;
            case 4:
                isearcher.setSimilarity(new LMJelinekMercerSimilarity(0.7f));
                System.out.println("Query with samilarity: LMJelinekMercerSimilarity(0.7f)");
                fileName = "../result/"+analyzerName+"_LMJ.txt";
                break;
            case 5:
                isearcher.setSimilarity(new LMDirichletSimilarity());
                System.out.println("Query with samilarity: LMDirichletSimilarity()");
                fileName = "../result/"+analyzerName+"_LMD.txt";
                break;
            default:
                isearcher.setSimilarity(new ClassicSimilarity());
                System.out.println("Query with default samilarity: ClassicSimilarity()");
                fileName = "../result/"+analyzerName+"_classic.txt";
        }

        TopicsParser topicsParser = new TopicsParser();
        List<TopicsObject> topicsList = topicsParser.getTopicsDocs();

        Map<String, Float> boosts = new HashMap<>();

        boosts.put("headline", 0.05f);
        boosts.put("text", 0.95f);

        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"headline", "text"}, this.analyzer, boosts);
//        int queryNumber = 1;
        
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (TopicsObject topic: topicsList) {
//            String queryString =parser.escape(queries.get(key).toString().trim());

            //System.out.println("Query " + queryNumber + " is '" + queryString + "'");

            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

            org.apache.lucene.search.Query titleQuery = parser.parse(MultiFieldQueryParser.escape(topic.getTopicTitle().trim()));
            org.apache.lucene.search.Query descQuery = parser.parse(MultiFieldQueryParser.escape(topic.getTopicDesc().trim()));

            // booleanQuery.add(new BoostQuery(titleQuery, 4f), BooleanClause.Occur.SHOULD);
            // booleanQuery.add(new BoostQuery(descQuery, 1.7f), BooleanClause.Occur.SHOULD);

            List<String> splitNarrative =  splitNarrIntoRelNotRel(topic.getTopicNarrative());
            System.out.println("narr: " + splitNarrative.get(0));
            System.out.println("innarr: " + splitNarrative.get(1));

            if(!splitNarrative.get(0).isEmpty()){
                org.apache.lucene.search.Query narrQuery = parser.parse(MultiFieldQueryParser.escape(splitNarrative.get(0).trim()));
                booleanQuery.add(new BoostQuery(narrQuery, 2f), BooleanClause.Occur.SHOULD);
            }else if(!splitNarrative.get(1).isEmpty()){
                org.apache.lucene.search.Query notRelatedNarrQuery = parser.parse(MultiFieldQueryParser.escape(splitNarrative.get(1).trim()));
                booleanQuery.add(new BoostQuery(notRelatedNarrQuery, 2f), BooleanClause.Occur.FILTER);
//                booleanQuery.add(new BoostQuery(notRelatedNarrQuery, 3f), BooleanClause.Occur.FILTER);

            }

//            booleanQuery.add(new BoostQuery(titleQuery, 4f), BooleanClause.Occur.SHOULD);
            booleanQuery.add(new BoostQuery(titleQuery, 7f), BooleanClause.Occur.SHOULD);
//            booleanQuery.add(new BoostQuery(descQuery, 1.7f), BooleanClause.Occur.SHOULD);
            booleanQuery.add(new BoostQuery(descQuery, 2.75f), BooleanClause.Occur.SHOULD);

            ScoreDoc[] hits = isearcher.search(booleanQuery.build(), MAX_RESULTS).scoreDocs;

            //System.out.println("Documents: " + hits.length);
            for (int i = 0; i < hits.length; i++)
            {
                Document hitDoc = isearcher.doc(hits[i].doc);
                String result = topic.getTopicNum() + " Q0 " + hitDoc.get("docno") + " " + (i+1) + " " + hits[i].score + " STANDARD";
                writer.write(result);
                writer.newLine();
            }

//            queryNumber = queryNumber + 1;

//            if(topic.getTopicNum().equals("425")){
//                break;
//            }
        }

        writer.close();
        ireader.close();
		directory.close();
        
        System.out.println("Result Output: " + fileName);

    }

    private static List<String> splitNarrIntoRelNotRel(String narrative) {
        StringBuilder narr = new StringBuilder();
        StringBuilder notRelatedNarr = new StringBuilder();
        List<String> splitNarrative = new ArrayList<>();

        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(narrative);
        int index = 0;
        while (bi.next() != BreakIterator.DONE) {
            String sentence = narrative.substring(index, bi.current());

            if (!sentence.contains("not relevant") && !sentence.contains("irrelevant")) {
                narr.append(sentence.replaceAll(
                        "a relevant document identifies|a relevant document could|a relevant document may|a relevant document must|a relevant document will|a document will|to be relevant|relevant documents|a document must|relevant|will contain|will discuss|will provide|must cite",
                        ""));
            } else {
                notRelatedNarr.append(sentence.replaceAll("are also not relevant|are not relevant|are irrelevant|is not relevant|not|NOT", ""));
            }
            index = bi.current();
        }
        splitNarrative.add(narr.toString());
        splitNarrative.add(notRelatedNarr.toString());
        return splitNarrative;
    }
}
