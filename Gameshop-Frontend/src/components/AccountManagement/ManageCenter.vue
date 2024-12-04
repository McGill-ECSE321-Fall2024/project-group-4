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
            <BTab title="Game Requests">
                <br>
                <GameRequestNew :gameRequests="gameRequests" />
            </BTab>
            <BTab title="Policies">
                <br />
                <Policy :policy="policy" />

                <!-- Form to add new policy -->
                <div v-if="showAddPolicyForm" class="add-policy-form mb-3">
                    
                    <BFormTextarea
                        v-model="newPolicy.description"
                        placeholder="Enter policy description"
                        class="mb-2"
                    />
                    <BButton
                        variant="primary"
                        @click="saveAddPolicy"
                        size="sm"
                        class="save-info-btn"
                    >
                        Save
                    </BButton>
                    <BButton
                        variant="secondary"
                        @click="cancelAddPolicy"
                        size="sm"
                        class="delete-btn"
                    >
                        Cancel
                    </BButton>
                </div>

                <!-- Button to trigger the add new policy form -->
                <BButton
                    variant="success"
                    class="ms-auto save-info-btn"
                    @click="toggleAddPolicyForm"
                >
                    + Add Policy
                </BButton>
            </BTab>
            <BTab title="Promotions" @click="fetchPromotions">
                <br />
                <BButton
                    variant="success"
                    class="ms-auto save-info-btn"
                    @click="showPromotionForm"
                >
                    Add Promotion
                </BButton>
                <div v-if="showAddPromotionForm" class="mb-4">
<<<<<<< HEAD
                    <form @submit.prevent="updatePromotion">
                    <b-form-group label="Discount" label-for="discount">
          <b-form-input
            id="discount"
            type="number"
            v-model="discount"
            required
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Start Date" label-for="startDate">
          <b-form-input
            id="startDate"
            type="date"
            v-model="startDate"
            required
          ></b-form-input>
        </b-form-group>
        <b-form-group label="End Date" label-for="endDate">
          <b-form-input
            id="endDate"
            type="date"
            v-model="endDate"
            required
          ></b-form-input>
        </b-form-group>
    </form>
                    <BButton variant="secondary" @click="cancelAddPromotion" class="delete-btn">Cancel</BButton>
                    <BButton variant="primary" @click="addPromotion" class="save-info-btn">Save</BButton>
=======
                    <!-- Promotion Form -->
>>>>>>> clara-frontEnd
                </div>
                <Promotion ref="Promotion" :promotion="promotion" />
            </BTab>
            <BTab title="Reviews">
                <br />
                <ReviewsManager :reviews="reviews" />
            </BTab>
        </BTabs>
    </div>
</template>

<script>
import Promotion from './Promotion.vue';
import ViewEmployee from './ViewEmployee.vue';
import Policy from './Policy.vue';
import GameRequestNew from './GameRequestNew.vue';
import axios from 'axios';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import dayjs from 'dayjs';
import ReviewsManager from "@/components/AccountManagement/ReviewsManager.vue";


const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
     headers: {
         'Access-Control-Allow-Origin': frontendURL,
     }
});


const format = (date) => {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return `Selected date is ${day}/${month}/${year}`;
}
export default{
    components: {
      ReviewsManager,
      VueDatePicker,
        VueDatePicker,
        Promotion,
        ViewEmployee,
        Policy,
        GameRequestNew,
    },
    data(){
        return{
            showAddPolicyForm: false, // Tracks whether the form is visible
            newPolicy: {
                description: "", 
            },
            searchBy: 'username',
            newEmployee: {
                id: null,
                username: '',
                is_active: false,
            },
            gameRequests: [],
            employees: [],
            showAddForm: false,
            showAddPromotionForm: false,
            newPromotion: {
                discount: '',
                description: '',
                startDate: '',
                endDate: '',
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
        // showAddPolicyForm() {
        //     this.showAddPolicyForm = !this.showAddPolicyForm;
        // },
         
        // Toggles the visibility of the add policy form
        toggleAddPolicyForm() {
            this.showAddPolicyForm = !this.showAddPolicyForm;
        },
        // Resets the form and hides it
        cancelAddPolicy() {
            this.showAddPolicyForm = false;
            this.newPolicy.description = ""; // Reset description field
        },

        async saveAddPolicy() {
            console.log("Saving policy");
            try {
                const response = await axiosClient.post("/accounts/policies", {
                    description: this.newPolicy.description, 
                }, {
                    headers: {
                        "Role": "MANAGER",
                        "Content-Type": "application/json",
                    },
                });

                if (response.status === 200) {
                    console.log("Policy saved:", response.data);
                    this.showAddPolicyForm = false;
                    this.newPolicy.description = ""; // Reset input field after successful save
                    this.fetchPolicies(); // Refresh the policies list
                } else {
                    console.error("Failed to save policy", response.data);
                    alert("Failed to save policy. Please try again.");
                }
            } catch (error) {
                console.error("Error saving policy:", error);
                alert("There was an error saving the policy. Please check the console for details.");
            }
        },
        async fetchPolicies() {
            try {
                const response = await axios.get("/accounts/policies");
                this.policies = response.data; 
            } catch (error) {
                console.error("Error fetching policies:", error);
            }
        },
        showPromotionForm() {
            console.log('showAddPromotionForm called');
            this.showAddPromotionForm = true;
        },
        cancelAddPromotion() {
            this.showAddPromotionForm = false;
            this.newPromotion = { discount: '', description: '' };
        },
        saveAddPromotion() {
            this.showAddPromotionForm = false;
            const date = new Date(this.endDate);
            const formattedDate1 = dayjs(date).format('YYYY-MM-DD');
            console.log(formattedDate1)
            this.newPromotion = { discount: 'A', description: 'A' };
            addPromotion();
        },


        async addPromotion() {
            let response = '';
            const formattedStartDate = dayjs(new Date(this.startDate)).format('YYYY-MM-DD');
            const formattedEndDate = dayjs(new Date(this.endDate)).format('YYYY-MM-DD');
            const proomtion = {
                    discount: this.discount,
                    startDate: formattedStartDate,
                    endDate:formattedEndDate,
                };
            try {
                response = await axiosClient.post("/promotions", proomtion);
                if (response.status === 200){
                    console.log(response)
                }

            }catch (error){
                alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
            }
            await this.$refs.Promotion.fetchPromotions();
        },

        async fetchPromotions() {
            await this.$refs.Promotion.fetchPromotions();
        }

    }
}

</script>

<style scoped src="../../assets/main.css">
  </style>