<template>
    <div class="viewEmployee">
        <br>
        <BTable :items="employees" :fields="fields">
            <template #cell(actions)="data">
                <BButton size="sm" @click="editEmployee(data.item)" class="add-btn">Edit</BButton>
            </template>
        </BTable>
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
export default {
  name: "ViewEmployee",
  props: {
    promotion: {
      type: Object,
      required: true,
      validator: (value) =>
          'id' in value &&
          'username' in value &&
          'is_active' in value
          
    },
  },
  data(){
    return{
        fields: [
            {key: 'id', label: 'ID', sortable: true},
            { key: 'username', label: 'Username', sortable: true },
            { key: 'is_active', label: 'Active Status', sortable: true },
            { key: 'actions', label: '' },

        ],
        
        employees: [
            { id: 1, username: 'John Doe', is_active: true },
            { id: 2, username: 'Jane Smith', is_active: false },
        ],
        selectedEmployee: {
            id: null,
            username: '',
            is_active: false,
        },
    }
  }

};
</script>