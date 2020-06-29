<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="12" :offset="6">
                <div class="main">
                    <el-row>
                        <el-input
                                placeholder="请输入自己的昵称"
                                prefix-icon="el-icon-user-solid"
                                v-model="name"
                                style="width:50%"
                        ></el-input>
                        <el-button type="primary" @click="connectWebSocket()">建立连接</el-button>
                        <el-button type="danger">断开连接</el-button>
                    </el-row>
                    <el-row>
                        <el-input
                                placeholder="请输入对方频道号"
                                prefix-icon="el-icon-phone"
                                v-model="aisle"
                                style="width:40%"
                        ></el-input>
                    </el-row>
                    <el-row>
                        <el-input
                                placeholder="请输入要发送的消息"
                                prefix-icon="el-icon-s-promotion"
                                v-model="messageValue"
                                style="width:50%"
                        ></el-input>
                        <el-button type="primary" @click="sendMessage()">发送</el-button>
                    </el-row>
                    <div class="message">
                        <div v-for="(value,key,index) in messageList" :key="index">
                            <el-tag v-if="value.name===name" type="success" style="float:right">我：{{value.msg}}</el-tag>
                            <br/>
                            <el-tag v-if="value.name!==name" style="float:left">{{value.name}}：{{value.msg}}</el-tag>
                            <br/>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                name: "", // 昵称
                webSocket: null, // WebSocket对象
                aisle: "", // 对方频道号
                myAisle:"",
                messageList: [], // 消息列表
                messageValue: "" // 消息内容
            };
        },
        methods: {
            connectWebSocket: function () {
                const h = this.$createElement;
                console.log("建立连接")
                if (!this.name || this.name === "") {
                    this.$alert("请输入自己的昵称", "提示", {
                        confirmButtonText: "确定",
                        callback: action => {
                            this.$message({
                                type: 'error',
                                message: `action:没有名字,${action}`
                            });
                        }
                    })
                } else {
                    if ("WebSocket" in window) {
                        this.webSocket = new WebSocket("ws://localhost:8282/room/" + this.name)
                    } else {
                        this.$notify({
                            title: 'error',
                            message: h('i', {style: 'color: red'}, '不支持建立socket连接')
                        });
                    }
                    //连接发生错误的回调方法
                    this.webSocket.onerror = function () {
                        this.$notify({
                            title: 'error',
                            message: h('i', {style: 'color: red'}, 'webSocket连接失败')
                        });
                    }
                    //接收到消息的回调方法
                    let that = this;
                    this.webSocket.onmessage = function (event) {
                        console.log(event.data)
                        let user = eval("(" + event.data + ")")
                        console.log(user)
                        if (user.type === 0) {
                            // 提示连接成功
                            that.showInfo(user.people, user.aisle);
                        }
                        if (user.type === 1) {
                            //显示消息
                            console.log("接受消息");
                            that.messageList.push(user);
                        }
                    };
                }
            },
            // 发送消息
            sendMessage: function () {
                let socketMsg = {msg: this.messageValue, toUser: this.aisle};
                if (this.aisle === "") {
                    //群聊.
                    socketMsg.type = 0;
                } else {
                    //单聊.
                    socketMsg.type = 1;
                }
                console.log(JSON.stringify(socketMsg))
                this.webSocket.send(JSON.stringify(socketMsg));
                this.messageValue = ''
            },
            // 详细连接信息
            showInfo: function (people, aisle) {
                this.$notify({
                    title: "当前在线人数：" + people,
                    message: "您的频道号：" + aisle,
                    duration: 0
                });
            }
        },
    }
</script>