<template>
  <div>
    <el-form ref="loginForm" :model="user" :rules="rules" label-width="80px" class="login-box">
      <h3 class="login-title">欢迎进入聊天室</h3>
      <el-form-item label="昵称" prop="username">
        <el-input type="text" placeholder="请输入账号" v-model="user.username"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" v-on:click="onSubmit()">登录</el-button>
      </el-form-item>
    </el-form>

    <el-dialog
      title="温馨提示"
      :visible.sync="dialogVisible"
      width="30%">
      <span>请输入账号和密码</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        user:{
          username:""
        },
        rules: {
          username: [
            {required: true, message: '昵称不可为空', trigger: 'blur'}
          ],
        },

        // 对话框显示和隐藏
        dialogVisible: false
      }
    },
    methods: {
      onSubmit() {
        this.$router.push({
          path: `/chat/${this.user.username}`,
        })
      }
    }
  }
</script>

<style>
  .login-box {
    border: 1px solid #DCDFE6;
    width: 350px;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px #909399;
  }

  .login-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #303133;
  }
</style>
