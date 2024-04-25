<template>
  <div class="playground">
    <GameMap />
  </div>
  <div class="button-refresh">
    <button type="button" class="btn btn-primary btn-lg" id="refresh-button">重载</button>
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

    refreshButton.addEventListener("click", function() {
        location.reload();
    });
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