<template>
    <div class="promotion">
        <br>
        <BTable :items="promotions" :fields="fields2">
        </BTable>
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
export default {
  name: "Promotion",
  props: {
    promotion: {
      type: Object,
      required: true,
      validator: (value) =>
          'discount' in value &&
          'startDate' in value &&
          'endDate' in value &&
          'promotionId' in value
    },
  },
  data(){
    return{
        promotions: [
            { promotionId: 1, game: 'Game1', discount: 10, start_date: '2021-10-01', end_date: '2021-10-31' },
            { promotionId: 2, game: 'Game2', discount: 20, start_date: '2021-10-01', end_date: '2021-10-31' },
        ],
        fields2: [
            {key: 'promotionId', label: 'ID', sortable: true},
            {key: 'game', label: 'Game', sortable: true},
            { key: 'discount', label: 'Discount', sortable: true },
            { key: 'start_date', label: 'Start Date', sortable: true },
            { key: 'end_date', label: 'End Date', sortable: true },
            { key: 'actions', label: '' },
        ],
    }
  }

};
</script>