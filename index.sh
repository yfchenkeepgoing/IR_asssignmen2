#!/bin/bash

# 检查是否在tmux会话中
if [ -z "$TMUX" ]; then
    # 如果不在tmux会话中，启动一个新的tmux会话并在其中运行脚本
    tmux new-session -d -s my_session "$(readlink -f "$0")"; tmux attach-session -t my_session
    exit
fi

# 以下是原始脚本的其余部分

# 解压并编译trec_eval
tar -xzf trec_eval-9.0.7.tar.gz
cd trec_eval-9.0.7
make

# 切换到项目目录
cd ../project

# 使用管道发送输入到Java程序
{
    # 等待Java程序启动
    sleep 2

    # 循环发送命令
    for i in {1..5}
    do
       echo "1"
       echo "$i"
       sleep 400
    done
    echo "5"
} | java -Xmx4g -jar target/group_assignment-1.0-SNAPSHOT.jar
