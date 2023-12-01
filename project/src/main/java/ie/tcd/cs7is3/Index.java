package ie.tcd.cs7is3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import ie.tcd.cs7is3.parser.FBISParser;
import ie.tcd.cs7is3.parser.FR94Parser;
import ie.tcd.cs7is3.parser.FTParser;
import ie.tcd.cs7is3.parser.LATimesParser;

public class Index
{
    String INDEX_DIRECTORY;

    // private String[] args;

    public Index(){
        
        
    }

    public void createIndex(Analyzer analyzer, String analyzerName) throws IOException{

        this.INDEX_DIRECTORY = "../index/"+analyzerName+"_index";
        // ArrayList of documents in the corpus
        // ArrayList<Document> documents = new ArrayList<Document>();
        Files.createDirectories(Paths.get(INDEX_DIRECTORY));
        // Open the directory that contains the search index
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Set up an index writer to add process and save documents to the index
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config);

        
        FBISParser fbisParser = new FBISParser();
        List<Document> fbisDocs = fbisParser.getFBISDocuments();
        iwriter.addDocuments(fbisDocs);
        iwriter.commit();

        FR94Parser fr94Parser = new FR94Parser();
        List<Document> fr94Docs = fr94Parser.getFr94Documents();
        iwriter.addDocuments(fr94Docs);
        iwriter.commit();

        FTParser ftParser = new FTParser();
        ArrayList<Document> ftDocs = ftParser.loadFTDocs();
        iwriter.addDocuments(ftDocs);
        iwriter.commit();

        LATimesParser laTimesParser = new LATimesParser();
        ArrayList<Document> laTimesDocs = laTimesParser.loadLaTimesDocs();
        iwriter.addDocuments(laTimesDocs);
        iwriter.commit();

        System.out.println("Total indexed documents: " + indexTotalDocs());
        // Write all the documents in the linked list to the search index
        // iwriter.addDocuments(documents);

        // Commit everything and close
        iwriter.close();
        directory.close();

    }

    public void printDoc(int docId) throws IOException{
        //Validation
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexReader reader = DirectoryReader.open(directory);
        
        docId = docId - 1;        
        try{
            Document document = reader.document(docId);
            List<IndexableField> fields = document.getFields();
            for (IndexableField field : fields) {
                String fieldName = field.name();
                String fieldValue = document.get(fieldName);  // 获取字段值
                System.out.println(fieldName + ": " + fieldValue);
            }
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        
        reader.close();
        directory.close();

    }
    
    public int indexTotalDocs() throws IOException{
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexReader reader = DirectoryReader.open(directory);
        
        return reader.numDocs();
    }    



}