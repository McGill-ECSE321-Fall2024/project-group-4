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
        isActive: true,
        categories: [],
        promotionId: null,
      },
      promotions: [],

    };
    
  },
  methods:{
    showRequestGameForm(){
      this.showAddForm = !this.showAddForm;
    },
    cancelAddGame() {
      this.showAddForm = false;
    },
    clearInputs(){
      this.newGame.name = '';
      this.newGame.description = '';
      this.newGame.coverPicture = '';
      this.newGame.price = null;
      this.newGame.stock = null;
      this.newGame.isActive = null;
      this.newGame.categories = [];
      this.newGame.promotionId = [];
    }
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
      
    </div>
    <div v-if="showAddForm" class="mb-3">
       <!-- create game here-->
      <BFormInput v-model="newGame.name" placeholder="Name" class="mb-2" />
      <BFormInput v-model="newGame.description" placeholder="Description" class="mb-2" />
      <BFormInput v-model="newGame.coverPicture" placeholder="Cover Picture" class="mb-2" />
      <BFormInput v-model="newGame.price" placeholder="Price" class="mb-2" />
      <BFormInput v-model="newGame.stock" placeholder="stock" class="mb-2" />
      <BFormSelect v-model="newGame.isActive" class="mb-2">
        <BFormSelectOption :value="true">Active</BFormSelectOption>
        <BFormSelectOption :value="false">Inactive</BFormSelectOption>
      </BFormSelect>
      <BFormInput v-model="newGame.categories" placeholder="Genre" class="mb-2" />
      <BFormSelect v-model="newGame.promotionId" class="mb-2">
        <BFormSelectOption :value="null" disabled>Select Promotion</BFormSelectOption>
        <BFormSelectOption v-for="promotion in promotions" :key="promotion.id" :value="promotion.id">{{ promotion.description }}</BFormSelectOption>
      </BFormSelect>      
      <BButton variant="secondary" @click="cancelAddGame" class="delete-btn" style="margin-left:-2px;margin-right:10px;">Cancel</BButton>
      <BButton variant="primary" @click="sendRequestGame" class="save-info-btn">Send Game Request</BButton> <!--when game is requested, send to GameRequestsEmployee page-->
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
