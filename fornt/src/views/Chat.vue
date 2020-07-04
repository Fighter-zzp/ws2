<template>
    <el-container style="height: 100%; border: 1px solid #eee">
        <el-aside width="11%" style="background-color: rgb(238, 241, 246);height: 100%;">
            <el-card class="box-card" style="height: 99.5%">
                <div slot="header">
                    <span>在线群友({{people_num}})</span>
                </div>
                <div v-for="(v,i) in people" :key="i" class="text item">
                    {{v}}-{{i}}
                    <el-divider/>
                </div>
            </el-card>
        </el-aside>

        <el-container style="height: 100%">
            <el-header style="text-align: right; font-size: 16px">
                <span>昵称：{{nickName}}</span>
                <span style="margin-left: 1%">频道：{{aisle}}</span>
            </el-header>

            <el-main>
                <ul class="infinite-list" v-infinite-scroll="load" style="overflow:auto">
                    <li class="infinite-list-item" v-for="(value,key,index) in messageList" :key="index">
                        <el-tag v-if="value.name===nickName" type="success" style="float:right">我：{{value.msg}}</el-tag>
                        <br/>
                        <el-tag v-if="value.name!==nickName" style="float:left">{{value.name}}：{{value.msg}}</el-tag>
                        <br/>
                    </li>
                </ul>
            </el-main>
            <el-footer>
                <el-row>
                    <el-col :md="22">
                        <el-input
                                type="textarea"
                                placeholder="请输入内容"
                                v-model="messageValue"
                                maxlength="30"
                                show-word-limit
                                resize="none"
                        ></el-input>
                    </el-col>
                    <el-col :md="2">
                        <el-button type="primary" style="margin-top: 11%" @click="sendMessage">
                            发送
                        </el-button>
                    </el-col>
                </el-row>
            </el-footer>
        </el-container>
    </el-container>
</template>

<script>
    export default {
        name: "Chat",
        data() {
            return {
                count: 0,
                webSocket: null, // WebSocket对象
                nickName: "zzp",
                aisle: "11",
                people_num: "22",
                people: [],
                messageList: [], // 消息列表
                messageValue: "" // 消息内容
            }
        },
        mounted() {
            this.nickName = this.$route.params.username
            const h = this.$createElement;
            console.log("建立连接")
            if (!this.nickName || this.nickName === "") {
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
                    this.webSocket = new WebSocket("ws://localhost:8282/room/" + this.nickName)
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
                    let user = eval("(" + event.data + ")")
                    console.log("用户消息：" + event.data)
                    if (user.type === 0) {
                        // 提示连接成功
                        that.showInfo(user.people_num, user.aisle, user.people);
                    }
                    if (user.type === 1) {
                        //接受消息
                        console.log("接受消息");
                        that.messageList.push(user);
                    }
                    if (user.type === 2) {
                        //显示消息
                        console.log(user.name + "退出直播间")
                        that.showInfo(user.people_num, "", user.people);
                    }
                };

                this.webSocket.onclose = function (event) {
                    console.log("关闭：" + event)
                    that.webSocket.close()
                }
            }
        },
        methods: {
            load() {
                this.count = 10
            },
            siLiao(msg) {
                let n = msg.indexOf("@")
                if (n !== -1) {
                    return msg.substring(n + 1)
                } else {
                    return ""
                }
            },
            // 发送消息
            sendMessage: function () {
                let aisle = this.siLiao(this.messageValue)
                let socketMsg = {msg: this.messageValue, toUser: aisle};
                if (aisle === "") {
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
            showInfo: function (people_num, aisle, people) {
                this.people_num = people_num
                this.people = people
                if (aisle && aisle !==""){
                    this.aisle = aisle
                }
            }
        },
    }
</script>

<style>
    .el-header {
        background-color: #4076ff;
        color: whitesmoke;
        line-height: 60px;
    }

    html, body {
        /*设置内部填充为0，几个布局元素之间没有间距*/
        padding: 0px;
        /*外部间距也是如此设置*/
        margin: 0px;
        /*统一设置高度为100%*/
        height: 100%;
    }

    .el-card__header {
        color: whitesmoke;
        background: #409EFF;
    }

    ul li {
        display: block;
    }
</style>
