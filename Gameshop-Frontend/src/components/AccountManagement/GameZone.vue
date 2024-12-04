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

      <BTab title="Game Requests" >
        <GameRequestEmployee />
      </BTab>

      <BTab title="Pending Updates" >
        <PendingUpdates />
      </BTab>
    </BTabs>
  </div>
    
</template>


<script>
import {ref, onMounted, watch, computed} from "vue";
import {useRoute} from "vue-router";
import axios from 'axios';
import PendingUpdates from "./PendingUpdates.vue";
import GameRequestEmployee from "./GameRequestEmployee.vue";
import GameCategories from "./GameCategories.vue";

export default {
  name: "GameZone",
  components: {
    GameCategories
    },
  methods : {
    async fetchCategories() {
      // console.log("A");
      await this.$refs.GameCategories.fetchCategories();
    }
}

}
</script>

<style scoped src="../../assets/main.css">
</style>