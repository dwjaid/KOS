<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
  <ResultBoard v-if="$store.state.pk.loser !== 'none'"/>
  <StatusBoard v-if="$store.state.pk.status === 'playing'" />
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import StatusBoard from '../../components/StatusBoard.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'

export default{
  components:{
    PlayGround,
    MatchGround,
    ResultBoard,
    StatusBoard,
  },
  setup() {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

    let socket = null;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://img.boxmoe.com/uploads/202405/d2562b119cd00e94e6ff861a6a0e9435.webp",
      });
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateSocket", socket);
      }
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 3000);
          store.commit("updateGame", data.game);
        } else if (data.event === "move") {
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);
        } else if (data.event === "result") {
          
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          if (data.loser === "all" || data.loser === "A") snake0.status = "die";
          if (data.loser === "all" || data.loser === "B") snake1.status = "die";
          store.commit("updateLoser", data.loser);
        }
      }
      socket.onclose = () => {
        store.commit("updateStatus", "matching");
      }

    });

    onUnmounted(() => {
      socket.close();
    });
  }
}
</script>

<style scoped>

</style>