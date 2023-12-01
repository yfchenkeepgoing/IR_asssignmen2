package ie.tcd.cs7is3.analyzer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAnalyzer extends StopwordAnalyzerBase {


    public CustomAnalyzer(){
//        super(new CharArraySet(Arrays.asList(
//                "a", "an", "and", "are", "as", "at", "be", "but", "by",
//                "for", "if", "in", "into", "is", "it",
//                "no", "not", "of", "on", "or", "such",
//                "that", "the", "their", "then", "there", "these",
//                "they", "this", "to", "was", "will", "with"
//        ), false));
//        super(new CharArraySet(Arrays.asList(
//                "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "you're",
//                "you've", "you'll", "you'd", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself",
//                "she", "she's", "her", "hers", "herself", "it", "it's", "its", "itself", "they", "them", "their",
//                "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "that'll", "these", "those",
//                "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does",
//                "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of",
//                "at", "by", "for", "with", "about", "between", "into", "through", "during", "before", "after", "above",
//                "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further",
//                "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few",
//                "more", "most", "other", "some", "such", "only", "own", "same", "so", "than", "too", "very", "s", "t",
//                "can", "will", "just", "don", "don't", "should", "should've", "now", "d", "ll", "m", "re", "ve"
//        ), false));
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new TrimFilter(tokenStream);
        tokenStream = new SynonymGraphFilter(tokenStream, buildSynonymMap(), true);
        tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(createStopWordList(),true));
        tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    public static SynonymMap buildSynonymMap(){
        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        try (BufferedReader br = new BufferedReader(new FileReader("../dataset/synonyms2.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] synonyms = line.split(",");
                for (int i = 1; i < synonyms.length; i++) {
                    builder.add(new CharsRef(synonyms[0]), new CharsRef(synonyms[i]), true);
                }
            }
            return builder.build();
        }catch (Exception e) {
            System.out.println("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create synonym map");
            return null;
        }

    }

    private List<String> createStopWordList()
    {
        ArrayList<String> stopWordList = new ArrayList<>();
        try {
            BufferedReader stopWords = new BufferedReader(new FileReader("../dataset/stopwords.txt"));
            String word = stopWords.readLine();
            while(word != null) {
                stopWordList.add(word);
                word = stopWords.readLine();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create stopword list");
        }
        return stopWordList;
    }
}
