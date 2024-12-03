<template>
  <div class="promotion">
    <br />
    <BTable :items="promotions" :fields="fields2">
      <template #cell(actions)="row">
        <div class="d-flex align-items-center justify-content-between">
          <BButton variant="danger" @click="deletePromotion(row.item.promotionId)">
            Delete Promotion
          </BButton>
          <b-Dropdown text="Actions" variant="primary" class="ml-2">
            <b-dropdown-item @click="editPromotion(row.item)">
              Edit
            </b-dropdown-item>
          </b-Dropdown>
          <b-Dropdown text="Add/Remove games from promotion" variant="primary" class="ml-2">
            <template v-for="(game) in this.games">
                        <b-dropdown-item>
                        <div class="d-flex justify-content-between align-items-center">
                            <span>{{ game.name }}</span>
                            <b-button
                            size="sm"
                            @click="addGameToPromotion(row.item.id, game.id)"
                            variant="primary"
                            class="ml-2"
                            >
                            +
                            </b-button>

                            <b-button
                            size="sm"
                            @click="removeGameFromPromotion(row.item.id, game.id)"
                            variant="danger"
                            class="ml-2"
                            >
                            -
                            </b-button>
                        </div>
                        </b-dropdown-item>
          </template>
</b-Dropdown>

        </div>
      </template>
    </BTable>

    <!-- Modal for editing promotion -->
    <b-modal
      id="editPromotionModal"
      v-model="showEditModal"
      title="Edit Promotion"
      hide-footer
    >
      <form @submit.prevent="updatePromotion">
        <b-form-group label="Discount" label-for="discount">
          <b-form-input
            id="discount"
            type="number"
            v-model="promotionToEdit.discount"
            required
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Start Date" label-for="startDate">
          <b-form-input
            id="startDate"
            type="date"
            v-model="promotionToEdit.start_date"
            required
          ></b-form-input>
        </b-form-group>
        <b-form-group label="End Date" label-for="endDate">
          <b-form-input
            id="endDate"
            type="date"
            v-model="promotionToEdit.end_date"
            required
          ></b-form-input>
        </b-form-group>
        <b-button variant="primary" type="submit">Save Changes</b-button>
      </form>
    </b-modal>
  </div>
</template>

<script>
import axios from 'axios';
import dayjs from 'dayjs';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
  baseURL: backendURL,
});

export default {
  name: "Promotion",
  data() {
    return {
      games: [],
      gamesMatrix: {},
      promotions: [],
      fields2: [
        { key: 'promotionId', label: 'ID', sortable: true },
        { key: 'discount', label: 'Discount', sortable: true },
        { key: 'start_date', label: 'Start Date', sortable: true },
        { key: 'end_date', label: 'End Date', sortable: true },
        { key: 'actions', label: '' },
      ],
      showEditModal: false, // Controls the modal visibility
      promotionToEdit: {
        promotionId: null,
        discount: null,
        start_date: '',
        end_date: '',
      },
    };
  },
  async mounted() {
    this.games = await this.fetchGames();
  },
  methods:{
    async fetchPromotions() {
    let response = '';
    try {
        response = await axiosClient.get("/promotions");
        if (response.status === 200) {
            
          
            this.promotions = response.data.map(promotion => {
                return {
                    ...promotion,
                    promotionId : `${promotion.id}`,
                    start_date: dayjs(new Date(promotion.startDate)).format('YYYY-MM-DD'), 
                    end_date:  dayjs(new Date(promotion.endDate)).format('YYYY-MM-DD'), 
                    discount: `${promotion.discount}%`,
                    key: 'actions', label: '' 
                };
            });// Check the transformed promotions array
        }
    } catch (error) {
         alert(error.response?.data?.errorMessages || error.message || "Something went wrong"); // Show error if any
    }

    
},editPromotion(promotion) {
      // Set promotion data to edit
      this.promotionToEdit = { ...promotion };
      // Show the modal
      this.showEditModal = true;
    },
  
    async deletePromotion(promotionId) {

  const id = promotionId;
  try {
    const response = await axiosClient.delete(`/promotions/${id}`);
    if (response.status === 200) {
      await this.fetchPromotions();
    }
  } catch (error) {
     alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
  }
       
    },async fetchGames() {
      let games = [];
      let response = "";

      try {
    const response = await axiosClient.get(`/games`);
    if (response.status === 200) {
      games = response.data
    }
  } catch (error) {
     alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
  }   
      return games.filter((game) => game.isActive);

    },async addGameToPromotion(promotionId, gameId) {
      const uri = `/games/${gameId}/promotions/${promotionId}`
      let response = "";
      try {
      const response = await axiosClient.put(uri);
      if (response.status === 200) {
      }
      } catch (error) {
       alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
      }   
    },async removeGameFromPromotion(promotionId, gameId) {
      const uri = `/games/${gameId}/promotions/${promotionId}`
      let response = "";
      try {
      const response = await axiosClient.delete(uri);
      if (response.status === 200) {
      }
      } catch (error) {
       alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
      }   
    },
    isGameInPromotion(game, promotionId) {
      const promotionsOnGame = game.promotions || [];
      const promotionIds = promotionsOnGame.map(promotion => promotion.id);
      this.$forceUpdate();
      return promotionIds.includes(parseInt(promotionId));
    }

  }

};



</script>

<style>
.added {
  font-size: 1.2em;
  color: #ffffffd8;
  background-color: rgb(136, 136, 255);
}

</style>