<template>
    <div class="policy">
        <br>
        <BTable :items="policies" :fields="fields">
        </BTable>
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
export default {
  name: "Policy",
  props: {
    promotion: {
      type: Object,
      required: true,
      validator: (value) =>
          'description' in value &&
          'id' in value 
    },
  },
  data(){
    return{
        fields: [
            { key: 'id', label: 'ID', sortable: true },
            { key: 'description', label: 'Description', sortable: true },
        ],
        policies: [
            { id: 1, description: 'Policy 1' },
            { id: 2, description: 'Policy 2' },
            { id: 3, description: 'Policy 3' },
            { id: 4, description: 'Policy 4' },
            { id: 5, description: 'Policy 5' },
        ]
    }
  }

};
</script>