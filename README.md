# IR_Group_7_project

## Group Members:
- Alok Singh 
- YIFAN CHEN 
- XINYANG SHAO 
- PATRICK O CALLAGHAN
- CORNEL CICAI

It takes about 300 seconds to index.
The code will automatically check for the index folder of a specific analyzer. If there is an existing index folder created by the requested analyzer, re-indexing will not be necessary.

1. Reinstall trec_eval
    ```
    tar -xzf trec_eval-9.0.7.tar.gz
    cd trec_eval-9.0.7
    make
    ```
    Then test the installation:
    ```
    make quicktest
    ```
2. Package:
    ```
    cd project
    mvn package
    ```
3. [OPTIONAL] Test:
    ```
    cd project
    mvn test
    ```
4. Execute the program:
    ```
    cd project
    java -Xmx4g -jar target/group_assignment-1.0-SNAPSHOT.jar
    ```


