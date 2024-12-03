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
      searchBy: ref("all"),
      searchQuery: '',
      userRole: ref(localStorage.getItem('userRole')),
      showAddForm: ref(false),
      newGame: {
        name: '',
        description: '',
        coverPicture: '',
        price: null,
        stock: null,
        isActive: null,
        categories: [],
        promotions: [],
      },
    };
    
  },
  methods:{
    showRequestGameForm(){
      this.showAddForm = !this.showAddForm;
    },
    cancelAdd() {
            this.showAddForm = false;
        },
  }
};
</script>

<template>
  <div class="game-catalogue">
    <div class="display-6 my-3" align="center">Game Catalogue</div>
    <div class="d-flex mb-3">
      <BFormSelect v-model="searchBy" class="me-2 w-auto">
          <BFormSelectOption value="all">All</BFormSelectOption>
          <BFormSelectOption value="name">Name</BFormSelectOption>
          <BFormSelectOption value="genre">Genre</BFormSelectOption>
          <BFormSelectOption value="id">ID</BFormSelectOption>
      </BFormSelect>
      <BFormInput
          v-model="searchQuery"
          placeholder="Search games"
          class="me-2"
      />
      <BButton type="submit" class="search-btn">Search</BButton>
      <BButton variant="success" class="ms-auto save-info-btn" @click="showRequestGameForm" v-if="userRole==='employee'">+</BButton>
    </div>
    <div v-if="showAddForm" class="mb-3">
       <!-- create game here-->
      <BFormInput v-model="newGame.name" placeholder="Name" class="mb-2" />
      <BFormInput v-model="newGame.description" placeholder="Description" class="mb-2" />
      <BFormInput v-model="newGame.coverPicture" placeholder="Cover Picture" class="mb-2" />
      <BFormInput v-model="newGame.price" placeholder="Price" class="mb-2" />
      <BFormInput v-model="newGame.stock" placeholder="stock" class="mb-2" />
      <BFormInput v-model="newGame.isActive" placeholder="Is Active" class="mb-2" />
      <BFormInput v-model="newGame.categories" placeholder="Genre" class="mb-2" />
      <BFormInput v-model="newGame.promotions" placeholder="Promotions" class="mb-2" />
      <BButton variant="secondary" @click="cancelAdd" class="delete-btn">Cancel</BButton>
      <BButton variant="primary" @click="saveAddEmployee" class="save-info-btn">Save</BButton>
    </div>
    <div v-if="loading" class="loading">Loading games...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <GameBrowser
        :games="games"
    />
  </div>
</template>


<style scoped src="../../assets/main.css">
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
