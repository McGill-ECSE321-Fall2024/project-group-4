<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Manage Center
        </div>
        <br>
        <BTabs content-class="mt-3" fill>
            <BTab title="Employees">
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
                </div>
                <ViewEmployee :employee="employee" />
                
            </BTab>
            <BTab title="Game Requests" >
                <br>
            </BTab>
            <BTab title="Policies" >
                <br>
            </BTab>
            <BTab title="Promotions" >
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

export default{
    data(){
        return{
            searchBy: 'username',
            

        }
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
    }
}

</script>