package ie.tcd.cs7is3;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.ProcessBuilder;
import java.lang.Process;
import java.lang.InterruptedException;

public class Eval{

    public Eval(){

    }

    public void evalResult(){

        String selectedFile = getFileName();
        executeEval(selectedFile);

    } 
    
    private void executeEval(String selectedFile){

        String command = "../trec_eval-9.0.7/trec_eval";
        String arg1 = "../qrels/qrels.assignment2.part1";
        String arg2 = "../result/" + selectedFile;

        ProcessBuilder processBuilder = new ProcessBuilder(command, arg1, arg2);
        processBuilder.redirectErrorStream(true); // 将错误输出和标准输出合并
    
        try {
            Process process = processBuilder.start();

            // 读取并显示命令的输出
            Scanner outputScanner = new Scanner(process.getInputStream());
            while (outputScanner.hasNextLine()) {
                System.out.println(outputScanner.nextLine());
            }
            

            process.waitFor(); // 等待命令执行完成
            //outputScanner.next();
            outputScanner.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
}
    
    private String getFileName(){

        Path resultPath = Paths.get("../result");
        List<String> fileNames = new ArrayList<>();
        
        System.out.println("Please enter the file name you want to evaluate: ");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resultPath)) {
            for (Path file : stream) {
                fileNames.add(file.getFileName().toString());
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
                e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        String selectedFile;
        boolean fileExists;

        do {
            selectedFile = scanner.nextLine();
            fileExists = fileNames.contains(selectedFile);

            if (!fileExists) {
                System.out.println("file not exit, please enter again");
            }
        } while (!fileExists);

        System.out.println("Evaluate with " + selectedFile);
        //scanner.close();
        return selectedFile;
    }
}
