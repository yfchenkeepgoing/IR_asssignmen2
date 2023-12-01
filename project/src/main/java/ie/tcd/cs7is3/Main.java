package ie.tcd.cs7is3;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import ie.tcd.cs7is3.analyzer.CustomAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;

public class Main{
    public static void main(String[] args) throws IOException{
        // if(args.length<=0){
        //     System.out.println("Expected corpus as input");
        //     System.exit(1);
        // }
        
        String userInput = "";
        int indexedFlag = 0;
        Index indexer = null;    
        Analyzer analyzer = null;
        
        String analyzerName = null;
        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println("-------------------------------------");
            System.out.println("Please enter the number of options");
            System.out.println("1. Index the corpus");
            System.out.println("2. Print the document");
            System.out.println("3. Query the document");
            System.out.println("4. Evaluation");
            System.out.println("5. exit");
            System.out.println("-------------------------------------");
            userInput = scanner.nextLine();
            
            // trim leading and trailing whitespace from the query
            userInput = userInput.trim();

            if(userInput.equals("1")){
                
                System.out.println("------------------------------------------------------");
                System.out.println("Please select the analyzer, input the number of it: ");
                System.out.println("1. StandardAnalyzer");
                System.out.println("2. EnglishAnalyzer");
                System.out.println("3. SimpleAnalyzer");
                System.out.println("4. WhitespaceAnalyzer");
                System.out.println("5. CustomAnalyzer");
                System.out.println("6. back");
                System.out.println("------------------------------------------------------");
                 
                int analyzerChoice = 0;
                while(true){
                    try{
                        analyzerChoice = scanner.nextInt();
                        scanner.nextLine();  // consume the newline left-over
                        break;
                    }catch(InputMismatchException e){
                        System.out.println("Wrong input");
                        scanner.next();
                    }
                }

                switch (analyzerChoice) {
                    case 1:
                        analyzer = new StandardAnalyzer();
                        System.out.println("Indexing with analyzer:  StandardAnalyzer()");
                        analyzerName = "standard";
                        break;
                    case 2:
                        analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
                        System.out.println("Indexing with analyzer: EnglishAnalyzer()");
                        analyzerName = "english";
                        break;
                    case 3:
                        analyzer = new SimpleAnalyzer();
                        System.out.println("Indexing with analyzer: SimpleAnalyzer()");
                        analyzerName = "simple";
                        break;
                    case 4:
                        analyzer = new WhitespaceAnalyzer();
                        System.out.println("Indexing with analyzer: WhitespaceAnalyzer()");
                        analyzerName = "whitespace";
                        break;
                    case 5:
                        analyzer = new CustomAnalyzer();
                        System.out.println("Indexing with default analyzer: CustomAnalyzer()");
                        analyzerName = "custom";
                    case 6:
                        break;
                    default:
                        analyzer = new CustomAnalyzer();
                        System.out.println("Indexing with default analyzer: CustomAnalyzer()");
                        analyzerName = "custom";
                }
                if(analyzerChoice == 6){
                    continue;
                } 
                indexer = new Index();
                indexer.createIndex(analyzer, analyzerName);
                indexedFlag = 1;
                System.out.println("Indexed docs number:" + indexer.indexTotalDocs());
            }else if(userInput.equals("2")){
                if(indexedFlag==1){
                    System.out.print("Please enter the document id: ");
                    int docId = 0;
                    while(true){
                        try{
                            docId = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        }catch (InputMismatchException e){
                            System.out.println("Wrong input");
                            scanner.next();
                        }
                    }   
                    indexer.printDoc(docId);
                }else{
                    System.out.println("Please index the documents first.");
                }
            }else if(userInput.equals("3")){
                System.out.println("------------------------------------------------------");
                System.out.println("Please select the analyzer, input the number of it: ");
                System.out.println("1. StandardAnalyzer");
                System.out.println("2. EnglishAnalyzer");
                System.out.println("3. SimpleAnalyzer");
                System.out.println("4. WhitespaceAnalyzer");
                System.out.println("5. CustomAnalyzer");
                System.out.println("6. back");
                System.out.println("------------------------------------------------------");

                int analyzerChoice = 0;
                while(true){
                    try{
                        analyzerChoice = scanner.nextInt();
                        scanner.nextLine();  // consume the newline left-over
                        break;
                    }catch(InputMismatchException e){
                        System.out.println("Wrong input");
                        scanner.next();
                    }
                }

                switch (analyzerChoice) {
                    case 1:
                        analyzer = new StandardAnalyzer();
                        analyzerName = "standard";
                        break;
                    case 2:
                        analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
                        analyzerName = "english";
                        break;
                    case 3:
                        analyzer = new SimpleAnalyzer();
                        analyzerName = "simple";
                        break;
                    case 4:
                        analyzer = new WhitespaceAnalyzer();
                        analyzerName = "whitespace";
                        break;

                    case 5:
                        analyzer = new CustomAnalyzer();
                        analyzerName = "custom";
                    case 6:
                        break;
                    default:
                        analyzer = new CustomAnalyzer();
                        analyzerName = "custom";
                }
                if(analyzerChoice == 6){
                    continue;
                }
                Path path = Paths.get("../index/"+analyzerName+"_index");

                boolean exists = Files.exists(path);

                if (!exists) {
                    System.out.println("Do not have index file by " + analyzerName + " analyzer");
                    System.out.println("Please index first");
                    continue;
                }

                System.out.println("------------------------------------------------------");
                System.out.println("Please select the type of Similarity, input the number of it: ");
                System.out.println("1. BM25");
                System.out.println("2. Bollean");
                System.out.println("3. Classic(TF IDF)");
                System.out.println("4. LMJ");
                System.out.println("5. LMD");
                System.out.println("6. back");
                System.out.println("------------------------------------------------------");

                int similarityChoice = 0;
                while(true){
                    try{
                        similarityChoice = scanner.nextInt();
                        scanner.nextLine();  // consume the newline left-over
                        break;
                    }catch(InputMismatchException e){
                        System.out.println("Wrong input");
                        scanner.next();
                    }
                }

                if(similarityChoice == 6){
                    continue;
                }

                Query query = new Query();
                try{
                    query.queryIndex(analyzer, similarityChoice, analyzerName);
                }catch (ParseException | org.apache.lucene.queryparser.surround.parser.ParseException e){
                    System.out.println(e);
                }


                
            }else if(userInput.equals("4")){
                Eval eval = new Eval();
                eval.evalResult();


            }
            
            // scanner.close();
        }while(!userInput.equals("5"));     
        //CreateIndex indexer = new CreateIndex(args);
        //indexer.createIndex();
        //indexer.validateIndex();
    }
}
