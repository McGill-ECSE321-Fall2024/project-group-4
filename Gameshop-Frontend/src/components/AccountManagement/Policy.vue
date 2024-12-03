<template>
  <div class="policies">
    <br>
    <div class="d-flex mb-3">
      <BFormInput
        v-model="searchQuery"
        placeholder="Search Policies by ID"
        class="me-2"
      />
      <BButton type="submit" class="search-btn" @click="searchPolicy">Search</BButton>
      <BButton variant="success" class="ms-auto save-info-btn" @click="showAddPolicyForm">+</BButton>
      <br>
    </div>
    <div v-if="showAddForm" class="d-flex mb-3">
      <br>
      <BFormTextarea v-model="newPolicy.description" placeholder="Description" class="mb-2" />
      <BButton variant="secondary" @click="cancelAddPolicy" class="delete-btn">Cancel</BButton>
      <BButton variant="primary" @click="saveAddPolicy" class="save-info-btn">Save</BButton>
    </div>
    <BTable :items="policies" :fields="fields">
      <template #cell(description)="data">
        <div v-if="selectedPolicy && selectedPolicy.id === data.item.id">
          <BFormInput v-model="selectedPolicy.description" class="mb-2" />
        </div>
        <div v-else>
          {{ data.item.description }}
        </div>
      </template>
      <template #cell(actions)="data">
        
        <div v-if="selectedPolicy && selectedPolicy.id === data.item.id" class="mt-3">
          <BButton variant="secondary" size="sm" @click="cancelEditPolicy" class="delete-btn">Cancel</BButton>
          <BButton variant="primary" size="sm" @click="updatePolicy" class="save-info-btn">Save</BButton>
        </div>
        <div v-else  class="mt-3">
          <BButton size="sm" variant="primary" @click="editPolicy(data.item)" class="save-info-btn">Edit</BButton>
          <BButton size="sm" variant="danger" @click="deletePolicy(data.item.id)" class="delete-btn">Delete</BButton>
        </div>
      </template>
    </BTable>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Policy",
  data() {
    return {
      searchQuery: '',
      fields: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'description', label: 'Description' },
        { key: 'actions', label: 'Actions' },
      ],
      policies: [
        {
          id: 1,
          description: 'Policy 1',
        },
        {
          id: 2,
          description: 'Policy 2',
        },
      ],
      showAddForm: false,
      newPolicy: {
        description: '',
      },
      selectedPolicy: null,
    };
  },
  methods: {
    async fetchPolicies() {
      try {
        const response = await axios.get('/policies');
        this.policies = response.data;
      } catch (error) {
        console.error('Error fetching policies:', error);
      }
    },
    async searchPolicy() {
      try {
        const response = await axios.get(`/policies/${this.searchQuery}`);
        this.policies = [response.data];
      } catch (error) {
        console.error('Error fetching policy by ID:', error);
      }
    },
    showAddPolicyForm() {
      this.showAddForm = !this.showAddForm;
    },
    cancelAddPolicy() {
      this.showAddForm = false;
      this.newPolicy = { description: '' };
    },
    async saveAddPolicy() {
      try {
        const response = await axios.post('/accounts/policies', this.newPolicy.description);
        if (response.status === 200) {
          this.policies.push(response.data);
          this.showAddForm = false;
          this.newPolicy = { description: '' };
        }
      } catch (error) {
        console.error('Error creating policy:', error);
      }
    },
    
    async updatePolicy() {
      try {
        const response = await axios.put(`/accounts/policies/${this.selectedPolicy.id}`, this.newPolicy.description);
        if (response.status === 200) {
          const index = this.policies.findIndex(policy => policy.id === this.selectedPolicy.id);
          if (index !== -1) {
            this.policies.splice(index, 1, response.data);
          }
          this.showAddForm = false;
          this.newPolicy = { description: '' };
          this.selectedPolicy = null;
        }
      } catch (error) {
        console.error('Error updating policy:', error);
      }
    },
    async deletePolicy(policyId) {
      try {
        await axios.delete(`/policies/${policyId}`);
        this.policies = this.policies.filter(policy => policy.id !== policyId);
      } catch (error) {
        console.error('Error deleting policy:', error);
      }
    },
    editPolicy(policy) {
      this.selectedPolicy = { ...policy };
    },
    cancelEditPolicy() {
      this.selectedPolicy = null;
    },
  },
  mounted() {
    this.fetchPolicies();
  },
};
</script>

<style scoped src="../../assets/main.css">
</style>