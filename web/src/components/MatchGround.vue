<template>
  <div class="matchground">
      <div class="row">
        <div class="col-4">
          <div class="user-photo">
            <img :src="$store.state.user.photo" alt="">
          </div>
          <div class="user-username">
            {{ $store.state.user.username }}
          </div>
          <div class="user-rank">
            {{ "段位: " +  $store.state.user.rank }}
          </div>
          <div class="user-rating">
            {{ "分数: " +  $store.state.user.rating }}
          </div>
        </div>
        <div class="col-4">
          <div class="user-select-bot">
            <select v-model="select_bot" class="form-select" aria-label="Default select example">
              <option value="-1" selected>手动操作</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                {{ bot.title }}
              </option>
            </select>
          </div>
        </div>
        <div class="col-4">
          <div class="user-photo">
            <img :src="$store.state.pk.opponent_photo" alt="">
          </div>
          <div class="user-username">
            {{ $store.state.pk.opponent_username }}
          </div>
          <div class="user-rank">
            {{ "段位: " +  $store.state.pk.opponent_rank }}
          </div>
          <div class="user-rating">
            {{ "分数: " +  $store.state.pk.opponent_rating }}
          </div>
        </div>
        <div class="col-12" style="text-align: center; padding-top: 3vh;">
          <button @click="click_match_btn" class="btn btn-outline-success btn-lg" type="button">{{ match_btn_info }}</button>
        </div>
      </div>
  </div>

</template>

<script>
import $ from 'jquery'
import { ref } from 'vue'
import { useStore } from 'vuex'


export default {
  setup() {
    const store = useStore();
    let match_btn_info = ref("开始匹配");
    let bots = ref([]);
    let select_bot = ref("-1");


    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消匹配"
        store.state.pk.socket.send(JSON.stringify({
          event: "start-matching",
          bot_id: select_bot.value,
        }))
      } else {
        match_btn_info.value = "开始匹配"
        store.state.pk.socket.send(JSON.stringify({
          event: "stop-matching",
        }))
      }
    }
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
    return {
      match_btn_info,
      click_match_btn,
      bots,
      select_bot,
    }
  }
}

</script>

<style scoped>
div.matchground {
  width: 60vw;
  height: 70vh;
  margin: 8vh auto;
  background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
  text-align: center;
  padding-top: 8vh;
}
div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}
div.user-select-bot {
  padding-top: 20vh;
}
div.user-select-bot > select {
  width: 60%;
  margin: auto;
}
div.user-username {
  text-align: center;
  font-size: 32px;
  font-weight: 800;
  color: rgba(186, 194, 33);
  padding-top: 2vh;
}
div.user-rank {
  text-align: center;
  font-size: 24px;
  font-weight: 800;
  color: rgb(32, 236, 66);
  padding-top: 2vh;
}
div.user-rating {
  text-align: center;
  font-size: 24px;
  font-weight: 800;
  color: rgb(227, 81, 48);
}

</style>