<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
      Draw
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.loser === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
      Lose
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.loser === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
      Lose
    </div>
    <div class="result-board-text" v-else>
      Win
    </div>
    <div class="result-board-btn">
      <button @click="restart" class="btn btn-outline-warning btn-lg" type="button">
        再来一局
      </button>
    </div>

  </div>

</template>

<script>
import { useStore } from 'vuex';

export default {
  setup () {
    const store = useStore();
    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateLoser", "none");
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://img.boxmoe.com/uploads/202405/d2562b119cd00e94e6ff861a6a0e9435.webp",
        rank: "暂无",
        rating: "暂无",
      });
    }
    return {
      restart
    };
  }
}
</script>

<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  right: 2vw;
}
div.result-board-text {
  text-align: center;
  color: rgba(215, 229, 17, 0.833);
  font-size: 50px;
  font-weight: 800;
  font-style: italic;
  padding-top: 5vh;
}
div.result-board-btn {
  text-align: center;
  padding-top: 5vh;
}
</style>




