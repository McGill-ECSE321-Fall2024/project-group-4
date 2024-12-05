<template>
  <div class="mainContainer">
    <div class="display-6 my-3" align="center">
      Game Zone
    </div>
    <br>
    <BTabs content-class="mt-3" fill>
      <BTab title="Game Categories" @click="fetchCategories">
        <GameCategories ref="GameCategories" />
      </BTab>

      <BTab title="Game Requests"  v-if="this.userRole==='employee'">
        <GameRequestEmployee />
      </BTab>

      <BTab title="Update Game" >
        <UpdateGame />
      </BTab>
    </BTabs>
  </div>
    
</template>


<script>
import UpdateGame from "./PendingUpdates.vue";
import GameRequestEmployee from "./GameRequestEmployee.vue";
import GameCategories from "./GameCategories.vue";

export default {
  data() {
    return {
      userRole : ''
    }
    
  },
  name: "GameZone",
  components: {
    GameCategories,
    GameRequestEmployee,
    UpdateGame
    },
  methods : {
    async fetchCategories() {
      // console.log("A");
      await this.$refs.GameCategories.fetchCategories();
    }
},async mounted() {
    this.userRole = localStorage.getItem('userRole');
}

}
</script>

<style scoped src="../../assets/main.css">
</style>