<script setup>

import {ref} from 'vue'

import ad1 from '../assets/ad1.png';
import ad2 from '../assets/ad2.png';
import ad3 from '../assets/ad3.png';
import ad4 from '../assets/ad4.png';

const slideInterval = ref(2500)
</script>

<template>
  <div class="mainContainer">
      <div class="display-6 my-3" align="center">
        Browse GameChest's Games Today
      </div>
      
       
      <img :src="ad4" class="d-block w-100 rounded" alt="ad4" fit>

      <br>

  </div>

</template>


<style scoped>
.mainContainer {
  padding: 20px;
}

.rounded-carousel {
  border-radius: 15px;
  overflow: hidden;
}

.rounded-carousel .carousel-inner {
  border-radius: 15px;
}
</style>