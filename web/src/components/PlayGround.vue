<template>
  <div class="playground">
    <GameMap />
  </div>

</template>

<script>
import GameMap from './GameMap.vue'

export default {
  components: {
    GameMap,
  }
}

document.addEventListener("DOMContentLoaded", function() {
    var refreshButton = document.getElementById("refresh-button");

});
</script>

<style scoped>
div.playground {
  width: 60vw;
  height: 70vh;
  margin: 8vh auto;
}

div.button-refresh{
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>