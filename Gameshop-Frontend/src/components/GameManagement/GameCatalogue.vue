<script>
import {ref, onMounted, watch, computed} from "vue";
import GameBrowser from "@/components/GameManagement/GameBrowser.vue";
import {useRoute} from "vue-router";

export default {
  name: "GameCatalogue",
  components: { GameBrowser },
  setup() {
    const route = useRoute(); // Access the route object
    const games = ref([]);
    const loading = ref(true);
    const error = ref(null);

    const fetchGames = async (searchQuery = "") => {
      try {
        loading.value = true; // Start loading
        error.value = null; // Reset error

        const url = searchQuery
            ? `http://localhost:8080/catalogue/games/${searchQuery}`
            : "http://localhost:8080/games";

        const response = await fetch(url);
        const data = await response.json();

        if (!response.ok) {
          throw new Error(`Error fetching games: ${response.statusText}`);
        }

        games.value = data;
      } catch (err) {
        error.value = err.message;
      } finally {
        loading.value = false;
      }
    };

    const searchQuery = computed(() => route.query.search || ""); // Reactive search query

    // Watch for changes in the search query
    watch(searchQuery, (newSearchQuery) => {
      fetchGames(newSearchQuery); // Fetch games when the search query changes
    });

    // Fetch games on initial mount
    onMounted(() => {
      fetchGames(searchQuery.value);
    });

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
    <div class="display-6 my-3" align="center">Game Catalogue</div>
    <div v-if="loading" class="loading">Loading games...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <GameBrowser
        :games="games"
    />
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

</style>
