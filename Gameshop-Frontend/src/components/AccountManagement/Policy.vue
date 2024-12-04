<template>
  <div class="policies">
    <br />
    <div class="d-flex mb-3">
      <BFormInput
        v-model="searchQuery"
        placeholder="Search Policies by ID"
        class="me-2"
      />
      <BButton type="submit" class="search-btn" @click="searchPolicy">Search</BButton>
      <BButton
        variant="success"
        class="ms-auto save-info-btn"
        @click="showAddPolicyForm"
        v-if="userRole === 'manager'"
      >
        +
      </BButton>
      <br />
    </div>
    <div v-if="showAddForm" class="d-flex mb-3">
      <br />
      <BFormTextarea
        v-model="newPolicy.description"
        placeholder="Description"
        class="mb-2"
      />
      <BButton
        variant="secondary"
        @click="cancelAddPolicy"
        size="sm"
        class="delete-btn"
      >
        Cancel
      </BButton>
      <BButton
        variant="primary"
        @click="saveAddPolicy"
        size="sm"
        class="save-info-btn"
      >
        Save
      </BButton>
    </div>
    <BTable :items="filteredPolicies" :fields="fields">
      <template #cell(description)="data">
        <div v-if="selectedPolicy && selectedPolicy.id === data.item.id">
          <BFormInput v-model="selectedPolicy.description" class="mb-2" />
        </div>
        <div v-else>
          {{ data.item.description }}
        </div>
      </template>

      <!-- Actions Column Visible Only for Managers -->
      <template #cell(actions)="data">
        <div v-if="userRole === 'manager'">
          <div v-if="selectedPolicy && selectedPolicy.id === data.item.id" class="mt-3">
            <BButton
              variant="secondary"
              size="sm"
              @click="cancelEditPolicy"
              class="delete-btn"
            >
              Cancel
            </BButton>
            <BButton
              variant="primary"
              size="sm"
              @click="updatePolicy"
              class="save-info-btn"
            >
              Save
            </BButton>
          </div>
          <div v-else class="mt-3">
            <BButton
              size="sm"
              variant="primary"
              @click="editPolicy(data.item)"
              class="save-info-btn"
            >
              Edit
            </BButton>
            <BButton
              size="sm"
              variant="danger"
              @click="deletePolicy(data.item.id)"
              class="delete-btn"
            >
              Delete
            </BButton>
          </div>
        </div>
      </template>
    </BTable>
  </div>
</template>

<script>
import axios from 'axios';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
    // headers: {
    //     'Access-Control-Allow-Origin': frontendURL,
    // }
});
  

export default {
  name: "Policy",
  data() {
    return {
      userRole: localStorage.getItem('userRole'), 
      searchQuery: '',
      fields: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'description', label: 'Description' },
        ...(this.userRole === 'manager' ? [{ key: 'actions', label: 'Actions' }] : []), // Conditionally add actions
        //{ key: 'actions', label: 'Actions' },
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
  computed: {
        filteredPolicies() {
            return this.policies.filter(policy => {
                const searchTerm = this.searchQuery.toLowerCase();
          
                return policy.id.toString().includes(searchTerm);
               
            });        
        },
    },
  methods: {
    async fetchPolicies() {
    try {
      const response = await axiosClient.get("/accounts/policies", {
        headers: {
          Role: this.userRole, // Use the userRole from localStorage or data
        },
      });
      this.policies = response.data;
    } catch (error) {
      console.error("Error fetching policies:", error);
    }
  },
  async searchPolicy() {
    if (this.searchQuery) {
      try {
        const response = await axiosClient.get(
          `/accounts/policies/${this.searchQuery}`,
          {
            headers: {
              Role: this.userRole,
            },
          }
        );
        this.policies = [response.data];
      } catch (error) {
        console.error("Error fetching policy:", error);
      }
    } else {
      this.fetchPolicies();
    }
  },
  async viewPolicy(policyId) {
    try {
      const response = await axiosClient.get(`/accounts/policies/${policyId}`, {
        headers: {
          Role: this.userRole,
        },
      });
      const policy = response.data;
      alert(`Policy: ${policy.description}`); // Display the policy description in a simple alert
      // Alternatively, navigate to a new page or modal to display the policy
    } catch (error) {
      console.error("Error opening policy:", error);
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
        const role = localStorage.getItem("role"); 

        const response = await axios.post("/accounts/policies", { description: this.newPolicy.description }, {
            headers: {
                "Role": role
            }
        });

                
        //const response = await axiosClient.post('/accounts/policies', { description: this.newPolicy.description });
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
      const response = await axiosClient.put(
        `/accounts/policies/${this.selectedPolicy.id}`,
        { description: this.selectedPolicy.description },
        {
          headers: {
            "Role": this.userRole,
          },
        }
      );
      if (response.status === 200) {
        const index = this.policies.findIndex(policy => policy.id === this.selectedPolicy.id);
        if (index !== -1) {
          this.policies.splice(index, 1, response.data);
        }
        this.selectedPolicy = null;
      }
    } catch (error) {
      console.error('Error updating policy:', error);
    }
  },

    async deletePolicy(policyId) {
      
    try {
      await axiosClient.delete(`/accounts/policies/${policyId}`, {
        headers: {
          "Role": this.userRole,
        },
      });
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