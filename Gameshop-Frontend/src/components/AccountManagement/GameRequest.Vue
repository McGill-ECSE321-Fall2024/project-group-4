<template>
    <div class="gameRequest">
      <br>
      <div class="d-flex mb-3">
      <BFormInput
                v-model="searchQuery"
                placeholder="Search Game Requests by ID"
                class="me-2"
            />
        <BButton type="submit" class="search-btn">Search</BButton>
      <BButton variant="success" class="ms-auto save-info-btn" @click="showAddGameRequestForm">+</BButton><br>
      </div>
      <div v-if="showAddForm" class="mb-3">
        <br>
        <BFormInput v-model="newGameRequest.externalReview" placeholder="External Review" class="mb-2" />
        <BFormSelect v-model="newGameRequest.gameId" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select Game</BFormSelectOption>
            <BFormSelectOption v-for="game in games" :key="game.id" :value="game.id">{{ game.name }}</BFormSelectOption>
        </BFormSelect>
        <BFormSelect v-model="newGameRequest.requestStatus" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select Status</BFormSelectOption>
            <BFormSelectOption value="APPROVED">APPROVED</BFormSelectOption>
            <BFormSelectOption value="DENIED">DENIED</BFormSelectOption>
            <BFormSelectOption value="PENDING">PENDING</BFormSelectOption>
        </BFormSelect>
        <BFormSelect v-model="newGameRequest.requestorId" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select Requestor</BFormSelectOption>
            <BFormSelectOption v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.username }}</BFormSelectOption>
        </BFormSelect>
        <BButton variant="secondary" @click="cancelAddGameRequest" class="delete-btn">Cancel</BButton>
        <BButton variant="primary" @click="saveAddGameRequest" class="save-info-btn">Save</BButton>
      </div>
      <br>
      <BTable :items="gameRequests" :fields="fields">
        <template #cell(actions)="data">
            <div v-if="data.item.requestStatus === 'PENDING'">
                <BButton size="sm" variant="danger" @click="rejectGameRequest(data.item.id)" class="delete-btn" style="margin-left: -10px;margin-right: 10px;">Reject</BButton>
                <BButton size="sm" variant="success" @click="approveGameRequest(data.item.id)" class="save-info-btn">Approve</BButton>
            </div>
        </template>
      </BTable>
    </div>
  </template>

<style scoped src="../../assets/main.css">
</style>

<script>
export default {
  name: "GameRequest",
    // props: {
    //     gameRequests: {
    //         type: Array,
    //         required: true
    //     }
    // },
  data(){
    return{
        fields: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'externalReview', label: 'External Review', sortable: true },
        { key: 'requestStatus', label: 'Request Status', sortable: true },
        { key: 'requestor', label: 'Requestor', sortable: true },
        { key: 'game', label: 'Game', sortable: true },
        { key: 'actions', label: 'Actions' },
      ],
      gameRequests: [{
        id: 1,
        externalReview: 'Great game',
        requestStatus: 'PENDING',
        requestor: 'John Doe',
        game: 'Game 1',
      }, {
        id: 2,
        externalReview: 'Good game',
        requestStatus: 'APPROVED',
        requestor: 'Jane Smith',
        game: 'Game 2',
      }],
      showAddForm: false,
      newGameRequest: {
        externalReview: '',
        gameId: null,
        requestorId: null,
        requestStatus: null,
      },
      games: [],
      employees: [],
    }
  },
  methods: {
    showAddGameRequestForm() {
      this.showAddForm = !this.showAddForm;
    },
    cancelAddGameRequest() {
      this.showAddForm = false;
      this.newGameRequest = {
        externalReview: '',
        gameId: '',
        requestorId: '',
      };
    },
    async saveAddGameRequest() {
      const gameRequestDTO = {
        externalReview: this.newGameRequest.externalReview,
        game: { id: this.newGameRequest.gameId },
        requestor: { id: this.newGameRequest.requestorId },
      };
      try {
        const response = await axios.post('/gameRequests', gameRequestDTO);
        if (response.status === 200) {
          this.$emit('update:gameRequests', [...this.gameRequests, response.data]);
          this.showAddForm = false;
          this.newGameRequest = {
            externalReview: '',
            gameId: '',
            requestorId: '',
          };
        }
      } catch (error) {
        console.error('Error creating game request:', error);
      }
    },
    async approveGameRequest(gameRequestId) {
      try {
        const response = await axios.put(`/gameRequests/${gameRequestId}/requestStatus`, null, {
          params: { status: 'approve' },
        });
        if (response.status === 200) {
          console.log('Game request approved');
          // Update the gameRequests list or notify the user
        }
      } catch (error) {
        console.error('Error approving game request:', error);
      }
    },
    async rejectGameRequest(gameRequestId) {
      try {
        const response = await axios.put(`/gameRequests/${gameRequestId}/requestStatus`, null, {
          params: { status: 'reject' },
        });
        if (response.status === 200) {
          console.log('Game request rejected');
          // Update the gameRequests list or notify the user
        }
      } catch (error) {
        console.error('Error rejecting game request:', error);
      }
    },
  }

};
</script>