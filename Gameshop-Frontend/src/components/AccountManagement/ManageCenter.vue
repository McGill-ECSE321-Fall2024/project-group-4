<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Manage Center
        </div>
        <br>
        <BTabs content-class="mt-3" fill>
            <BTab title="Employees">
                <br>
                <!-- <div class="d-flex mb-3">
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
                    <BFormInput v-model="newEmployee.email" placeholder="Email" class="mb-2" />
                    <BFormSelect v-model="newEmployee.is_active" class="mb-2">
                        <BFormSelectOption :value="true">Active</BFormSelectOption>
                        <BFormSelectOption :value="false">Inactive</BFormSelectOption>
                    </BFormSelect>
                    <BButton variant="secondary" @click="cancelAdd" class="delete-btn">Cancel</BButton>
                    <BButton variant="primary" @click="saveAdd" class="save-info-btn">Save</BButton>
                </div>-->
                <ViewEmployee :employee="employee" /> 
                
            </BTab>
            <BTab title="Game Requests" >
                <br>
            </BTab>
            <BTab title="Policies" >
                <br>
                <BButton variant="success" class="ms-auto save-info-btn" @click="showAddPolicyForm">Add Policy</BButton>
                <div v-if="showAddPolicyForm" class="mb-3">
                    <BFormInput v-model="newPolicy.title" placeholder="Title" class="mb-2" />
                    <BFormTextarea v-model="newPolicy.description" placeholder="Description" class="mb-2" />
                    <BButton variant="secondary" @click="cancelAddPolicy" class="delete-btn">Cancel</BButton>
                    <BButton variant="primary" @click="saveAddPolicy" class="save-info-btn">Save</BButton>
                </div>
                <Policy :policy="policy" />
            </BTab>
            <BTab title="Promotions" >
                <br>
                <BButton variant="success" class="ms-auto save-info-btn" @click="showAddPromotionForm">Add Promotion</BButton>
                <div v-if="showAddPromotionForm" class="mb-3">
                    <BFormInput v-model="newPromotion.title" placeholder="Title" class="mb-2" />
                    <BFormTextarea v-model="newPromotion.description" placeholder="Description" class="mb-2" />
                    <BButton variant="secondary" @click="cancelAddPromotion" class="delete-btn">Cancel</BButton>
                    <BButton variant="primary" @click="saveAddPromotion" class="save-info-btn">Save</BButton>
                </div>
                <Promotion :promotion="promotion" />
            </BTab>
            <BTab title="Reviews" >
                <br>
            </BTab>
        </BTabs>

        <!-- Edit Employee Modal
    <BModal id="edit-employee-modal" ref="editEmployeeModal" title="Edit Employee">
      <div>
        <label for="employee-status">Status:</label>
        <BFormSelect id="employee-status" v-model="selectedEmployee.is_active">
          <BFormSelectOption :value="true">Active</BFormSelectOption>
          <BFormSelectOption :value="false">Inactive</BFormSelectOption>
        </BFormSelect>
      </div>
      <template #modal-footer="{ ok, cancel }">
        <BButton variant="secondary" @click="cancelEdit">Cancel</BButton>
        <BButton variant="primary" @click="saveEdit">Save</BButton>
      </template>
    </BModal> -->
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
import Promotion from './Promotion.vue';
import ViewEmployee from './ViewEmployee.vue';
import Policy from './Policy.vue';
import axios from 'axios';
const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
    // headers: {
    //     'Access-Control-Allow-Origin': frontendURL,
    // }
});
export default{
    data(){
        return{
            searchBy: 'username',
            newEmployee: {
                id: null,
                username: '',
                is_active: false,
            },
            showAddForm: false,
            showAddPolicyForm: false,
            showAddPromotionForm: false,
            newPolicy: {
                title: '',
                description: '',
            },
            newPromotion: {
                title: '',
                description: '',
            },
            selectedEmployee: null,
            searchQuery: '',
 
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
    methods:{
        editEmployee(employee){
            console.log('Edit employee:', employee);
        },
        cancelEdit() {
            this.$refs.editEmployeeModal.hide();
        },
        saveEdit() {
            const index = this.employees.findIndex(emp => emp.id === this.selectedEmployee.id);
            if (index !== -1) {
                this.employees.splice(index, 1, this.selectedEmployee);
            }
            this.$refs.editEmployeeModal.hide();
        },
        showAddEmployeeForm() {
            this.showAddForm = !this.showAddForm;
        },
        cancelAdd() {
            this.showAddForm = false;
            this.newEmployee = { username: '', is_active: true };
        },
        showAddPolicyForm() {
            this.showAddPolicyForm = true;
        },
        cancelAddPolicy() {
            this.showAddPolicyForm = false;
            this.newPolicy = { title: '', description: '' };
        },
        saveAddPolicy() {
            // Add logic to save the new policy
            this.showAddPolicyForm = false;
            this.newPolicy = { title: '', description: '' };
        },
        showAddPromotionForm() {
            this.showAddPromotionForm = true;
        },
        cancelAddPromotion() {
            this.showAddPromotionForm = false;
            this.newPromotion = { title: '', description: '' };
        },
        saveAddPromotion() {
            // Add logic to save the new promotion
            this.showAddPromotionForm = false;
            this.newPromotion = { title: '', description: '' };
        },
        
    }
}

</script>