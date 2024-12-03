<template>
    <div class="viewEmployee">
        <br>
        <div class="d-flex mb-3">
            <BFormSelect v-model="searchBy" class="me-2 w-auto">
                <BFormSelectOption value="username">Username</BFormSelectOption>
                <BFormSelectOption value="id">ID</BFormSelectOption>
            </BFormSelect>
            <BFormInput
                v-model="searchQuery"
                placeholder="Search employees"
                class="me-2"
            />
            <BButton type="submit" class="search-btn">Search</BButton>
            <BButton variant="success" class="ms-auto save-info-btn" @click="showAddEmployeeForm">+</BButton>
        </div>
        <div v-if="showAddForm" class="mb-3">
            <BFormInput v-model="newEmployee.username" placeholder="Username" class="mb-2" />
            <BFormInput v-model="newEmployee.password" placeholder="Password" class="mb-2" />
            <BFormSelect v-model="newEmployee.isActive" class="mb-2">
                <BFormSelectOption :value="true">Active</BFormSelectOption>
                <BFormSelectOption :value="false">Inactive</BFormSelectOption>
            </BFormSelect>
            <BButton variant="secondary" @click="cancelAdd" class="delete-btn">Cancel</BButton>
            <BButton variant="primary" @click="saveAddEmployee" class="save-info-btn">Save</BButton>
        </div>
    <BTable :items="filteredEmployees" :fields="fields">
        <!-- <template #cell(actions)="data">
            <BButton size="sm" @click="editEmployee(data.item)" class="add-btn">Edit</BButton>
        </template> -->
        <template #cell(username)="data">
        <div v-if="selectedEmployee && selectedEmployee.id === data.item.id">
          <BFormInput v-model="selectedEmployee.username" class="mb-2" />
        </div>
        <div v-else>
          {{ data.item.username }}
        </div>
      </template>
      <template #cell(isActive)="data">
        <div v-if="selectedEmployee && selectedEmployee.id === data.item.id">
          <BFormSelect v-model="selectedEmployee.isActive" class="mb-2">
            <BFormSelectOption :value="true">Active</BFormSelectOption>
            <BFormSelectOption :value="false">Inactive</BFormSelectOption>
          </BFormSelect>
        </div>
        <div v-else>
          {{ data.item.isActive ? 'Active' : 'Inactive' }}
        </div>
      </template>

        <template #cell(actions)="data">
            <div v-if="selectedEmployee && selectedEmployee.id === data.item.id" class="mt-3">
                <BButton variant="secondary" size="sm" @click="cancelEditEmployee" class="delete-btn">Cancel</BButton>
                <BButton variant="primary" size="sm" @click="saveEditEmployee" class="save-info-btn">Save</BButton>                  
            </div>
            <div v-else  class="mt-3">
                <BButton size="sm" variant="primary" @click="editEmployee(data.item)" class="save-info-btn">Edit</BButton>
            </div>
      </template>
    </BTable>

    
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
import axios from 'axios';
import { computed } from 'vue';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
let oldUsername = '';
export default {
  name: "ViewEmployee",
  props: {
    employees: {
      type: Array,
      required: true,
    },
  },
  
  data(){
    return{
        fields: [
            {key: 'id', label: 'ID', sortable: true},
            { key: 'username', label: 'Username', sortable: true },
            { key: 'isActive', label: 'Active Status', sortable: true },
            { key: 'actions', label: '' },

        ],
        
        employees: [],
        selectedEmployee: {
            id: null,
            username: '',
            isActive: null,
        },
        newEmployee: {
            username: '',
            password: '',
            isActive: false,
            refundRequests: [],
        },
        searchQuery: '',
        searchBy: 'username', //default for search
        selectedEmployee: null,
        showAddForm: false,
    }
  },
  computed: {
        filteredEmployees() {
            return this.employees.filter(employee => {
                const searchTerm = this.searchQuery.toLowerCase();
                if (this.searchBy === 'username') {
                return employee.username.toLowerCase().includes(searchTerm);
                } else if (this.searchBy === 'id') {
                return employee.id.toString().includes(searchTerm);
                }
                return false;
            });        
        },
    },
    async created() {
        try {
            const response = await axiosClient.get(`/accounts/employees`);
            this.employees = response.data;
        } catch (error) {
            alert(`Error fetching data: ${error}`);
        }
    },
    methods:{
        cancelEditEmployee() {
            this.selectedEmployee = null;
        },
        async saveEditEmployee() {
        try {
            let response = await axiosClient.put(`/accounts/employees/${this.selectedEmployee.id}/active/${this.selectedEmployee.isActive}`);
            if (response.status === 200 && !(this.oldUsername === this.selectedEmployee.username)) {
                response = await axiosClient.put(`/accounts/employees/${this.oldUsername}/username/${this.selectedEmployee.username}`);
            }
            if (response.status === 200) {
                const index = this.employees.findIndex(emp => emp.id === this.selectedEmployee.id);
                if (index !== -1) {
                    this.employees.splice(index, 1, response.data);
                }
                this.selectedEmployee = null;
            }
        } catch (error) {
            console.error('Error updating employee:', error);
        }
        },
        async searchEmployees(){
            if (this.searchBy === 'username' && this.searchQuery) {
                try {
                const response = await axiosClient.get(`/accounts/employees/username/${this.searchQuery}`);
                this.employees = [response.data];
                } catch (error) {
                    console.error('Error fetching employee by username:', error);
                }
            } else if (this.searchBy === 'id' && this.searchQuery) {
                try {
                const response = await axiosClient.get(`/accounts/employees/id/${this.searchQuery}`);
                this.employees = [response.data];
                } catch (error) {
                    console.error('Error fetching employee by ID:', error);
                }
            }
            else {
                // Handle search by ID or other criteria
                this.filteredEmployees();
            }
        },
        editEmployee(employee){
            this.selectedEmployee = { ...employee };
            this.oldUsername = this.selectedEmployee.username;
        },
        cancelEditEmployee() {
            this.selectedEmployee = null;
        },
        showAddEmployeeForm() {
            this.showAddForm = !this.showAddForm;
        },
        cancelAdd() {
            this.showAddForm = false;
            this.newEmployee = { username: '', isActive: true };
        },
        
        clearInputs(){
            this.username = null;
            this.password = null;
            this.isActive = null;
        },
        setUsername(username){
            localStorage.setItem('username', username);
        },
        setAccountId(accountId){
            localStorage.setItem('accountId', accountId);
        },
        async saveAddEmployee(){
            let response='';
            const credentials = {
                username: this.newEmployee.username,
                password: this.newEmployee.password,
                isActive: this.newEmployee.isActive,
                refundRequests: []
            };
            try {
                response = await axiosClient.post('/accounts/employees', credentials);
                console.log(response.data);

                console.log(response.status)
                if (response.status === 200) {
                    this.newEmployee = response.data;
                    this.clearInputs();
                    this.showAddForm = false;
                    this.$router.go();
                }
            } catch (error) {
                console.error('Error creating employee:', error);
            }
        }
        
    },

};
</script>