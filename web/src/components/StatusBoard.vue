<template>
  <div class="status-board">
    <div class="status-board-state">
      游戏中
    </div>
    <div class="status-board-timer">
      剩余时间: {{ countDown }} s
    </div>
    <div class="status-board-player-A" v-if="$store.state.pk.a_id == $store.state.user.id">
      你在本局游戏中操控的蛇为蓝蛇
    </div>
    <div class="status-board-player-B" v-else>
      你在本局游戏中操控的蛇为红蛇
    </div>

  </div>

</template>

<script>
import { Timer } from '@/assets/scripts/Timer';

export default {
  setup() {
    let first_round = true;
    let timer = new Timer(3);
    let countDown = Number(timer.time).toFixed(2);
  },
}
</script>

<style scoped>
div.status-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 2vw;
}
div.status-board-state {
  text-align: center;
  color: rgba(30, 227, 79, 0.833);
  font-size: 50px;
  font-weight: 800;
  padding-top: 5vh;
} 
div.status-board-timer {
  text-align: center;
  color: rgba(215, 229, 17, 0.833);
  font-size: 35px;
  font-weight: 800;
}
div.status-board-player-A {
  text-align: center;
  color: rgba(17, 95, 229, 0.833);
  font-size: 35px;
  font-weight: 800;
}
div.status-board-player-B {
  text-align: center;
  color: rgba(208, 57, 3, 0.833);
  font-size: 35px;
  font-weight: 800;
}
</style>




