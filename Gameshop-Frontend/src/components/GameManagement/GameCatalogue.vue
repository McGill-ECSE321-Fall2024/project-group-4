<script>
import { ref, onMounted } from "vue";
import GamePreview from "./GamePreview.vue";

export default {
  name: "GameCatalogue",
  components: { GamePreview },
  setup() {
    const games = ref([]);
    const loading = ref(true);
    const error = ref(null);

    const fetchGames = async () => {
      try {
        const response = await fetch("http://localhost:8080/games");
        if (!response.ok) {
          throw new Error(`Error fetching games: ${response.statusText}`);
        }

        games.value = await response.json();
        console.log(games.value)

      } catch (err) {
        error.value = err.message;
      } finally {
        loading.value = false;
      }
    };

    onMounted(fetchGames);

    return {
      games,
      loading,
      error,
    };
  },
};
</script>


<template>
  <div class="game-catalogue">
    <h1>Game Catalogue</h1>
    <div v-if="loading" class="loading">Loading games...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="game-grid">
      <GamePreview
          v-for="game in games"
          :key="game.id"
          :game="game"
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

.loading {
  font-size: 1.2em;
  color: #555;
}

.error {
  font-size: 1.2em;
  color: #d9534f;
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}
</style>
