#!/bin/bash

# remove the old version of result.txt 
rm result.txt

cd project/

# 定义分析器和相似度的数组
analyzers=("standard" "english" "simple" "whitespace" "custom")
similarities=("BM25" "Boolean" "classic" "LMJ" "LMD")

# 循环遍历每个分析器
for analyzer in {1..5}; do
    # 循环遍历每个相似度
    for similarity in {1..5}; do
        # 构造文件名
        file_name="${analyzers[$analyzer-1]}_${similarities[$similarity-1]}.txt"

        # 执行 Java 程序并提取结果
        {
            sleep 3

            echo "3" # query the document
            sleep 3
            echo "$analyzer" # choose the analyzer
            sleep 3
            echo "$similarity" # choose the similarity
            sleep 30
            echo "4" # evaluation
            sleep 3
            echo $file_name # choose the file to score
            sleep 3
            echo "5" # exit the program
        } | java -Xmx4g -jar target/group_assignment-1.0-SNAPSHOT.jar | awk "/Evaluate with $file_name/,/P_1000/" | tee -a ../result.txt

        # 终端输出去掉 .txt 后缀的文件名
        echo "Processed: ${file_name%.txt}"

        echo -e "\n--------------------------------------" >> ../result.txt
    done
done
