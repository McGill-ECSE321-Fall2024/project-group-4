<script>
import GamePreview from "./GamePreview.vue";

export default {
  name: "GameBrowser",
  props: {
    games: {
      type: Array,
      required: true
    }
  },
  components: { GamePreview }

};
</script>

<template>
  <div class="game-catalogue">
    <div class="game-grid">
      <GamePreview
          v-for="game in games"
          :key="game.id"
          :game="game"
          :enable-add-to-cart="true"
      />
    </div>
  </div>
</template>


<style scoped>
.game-catalogue {
  padding: 20px;
}

.game-catalogue h1 {
  font-size: 2em;
  margin-bottom: 20px;
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 100px;
}
</style>
