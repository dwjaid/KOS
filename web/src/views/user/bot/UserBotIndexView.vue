<template>
  <div id="liveToastPlaceholder"></div>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top: 20px;">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" width="100%;">
            <hr>
            <div class="card" style="text-align: center;">
              <div class="card-header">
                <span class="username" >{{$store.state.user.username}}</span>
              </div>
            </div>
            <hr>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top: 20px;">
          <div class="card-header">
            <span style="font-size: 150%;">我的Bot</span>
            <button type="button" class="btn btn-warning float-end" style="font-size: large;" data-bs-toggle="modal" data-bs-target="#add-bot-btn">创建Bot</button>
          </div>
          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createTime }}</td>
                  <td>
                    <button type="button" class="btn btn-primary" style="margin-right: 30px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                    <button type="button" class="btn btn-danger"  @click="remove_bot(bot)">删除</button>
          
                    <div class="modal fade" :id="'update-bot-modal-' + bot.id"  data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5">修改Bot</h1>
                            <button type="button" class="btn-close" ></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="add-bot-title" class="form-label">名称</label>
                              <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入名称">
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-description" class="form-label">简介</label>
                              <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入简介"></textarea>
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-code" class="form-label">代码</label>
                              
                              <VAceEditor
                                v-model:value="bot.content"
                                @init="editorInit"
                                lang="c_cpp"
                                theme="textmate"
                                style="height: 300px"
                                :options="{
                                  enableBasicAutocompletion: true,
                                  enableSnippets: true,
                                  enableLiveAutocompletion: true,
                                  fontSize: 22,
                                  tabSize: 2, 
                                  showPrintMargin: false, 
                                  highlightActiveLine: true,
                                }" />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error_message">{{ bot.error_message }}</div>
                            <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" @click="refresh_bots">取消</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="add-bot-btn" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="add-bot-buttonLabel">创建Bot</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label for="add-bot-title" class="form-label">名称</label>
            <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入名称">
          </div>
          <div class="mb-3">
            <label for="add-bot-description" class="form-label">简介</label>
            <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入简介"></textarea>
          </div>
          <div class="mb-3">
            <label for="add-bot-code" class="form-label">代码</label>
            <VAceEditor
              v-model:value="botadd.content"
              @init="editorInit"
              lang="c_cpp"
              theme="textmate"
              style="height: 300px"
              :options="{
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true,
                fontSize: 22,
                tabSize: 2, 
                showPrintMargin: false, 
                highlightActiveLine: true,
              }" />
          </div>
        </div>
        <div class="modal-footer">
          <div class="error_message">{{ botadd.error_message }}</div>
          <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { ref, reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default{

  components: {
    VAceEditor
  },

  setup() {

    ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" +
            require("ace-builds").version +
            "/src-noconflict/")

    const store = useStore();
    let bots = ref([]);
    const botadd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });
    const refresh_bots = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
        }
      })
    }
    refresh_bots();


    const add_bot = () => {
      botadd.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/add/",
        type: "post",
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            Modal.getInstance("#add-bot-btn").hide();
            refresh_bots();
            alert('Bot' + botadd.title + '创建成功', 'success');
            botadd.title = "";
            botadd.description = "";
            botadd.content = "";
          } else {
            botadd.error_message = resp.error_message;
          }
        }
      })
    }

    const update_bot = (bot) => {
      botadd.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/update/",
        type: "post",
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            Modal.getInstance('#update-bot-modal-' + bot.id).hide();
            alert('Bot' + botadd.title + '修改成功', 'success');
            refresh_bots()
          } else {
            botadd.error_message = resp.error_message;
          }
        }
      })
    }
    
    const remove_bot = (bot) => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/remove/",
        type: "post",
        data: {
          bot_id: bot.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            refresh_bots();
          }
        }
      })
    }

    return {
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
    }
  }
  
}
</script>

<style scoped>
.username {
  color:rgba(186, 194, 33);
  font-size: 150%;
  align-items: center;
}
.rank-style-菜鸟 {
  color:rgba(186, 194, 33);
  font-size: 150%;
  align-items: center;
}
div.error_message {
  color: red;
}
</style>