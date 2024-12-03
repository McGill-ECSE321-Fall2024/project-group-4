<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Manage Center
        </div>
        <br>
        <BTabs content-class="mt-3" fill>
            <BTab title="Employees">
                <br>
                <ViewEmployee :employees="employees" @update:employees="updateEmployees" /> 
                
            </BTab>
            <BTab title="Game Requests" >
                <br>
                <GameRequest :gameRequests="gameRequests"/>

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
import GameRequest from './GameRequest.vue';
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
    components: {
        Promotion,
        ViewEmployee,
        Policy,
        GameRequest,
    },
    data(){
        return{
            searchBy: 'username',
            newEmployee: {
                id: null,
                username: '',
                is_active: false,
            },
            gameRequests: [],
            employees: [],
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
    
    methods:{
        updateEmployees(newemployees){
            this.employees = newemployees;
        },
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