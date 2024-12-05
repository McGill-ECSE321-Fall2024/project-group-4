<script>
import TheWelcome from '../components/TheWelcome.vue';

import {ref} from 'vue'

const slideInterval = ref(5000)
</script>

<template>
  <div class="mainContainer">
      <div class="display-6 my-3" align="center">
        Browse GameChest's Games Today
      </div>

      <template>
        <BCarousel :interval="slideInterval" controls indicators ride="carousel">
          <BCarouselSlide img-src="https://picsum.photos/1024/480/?image=19" />
          <BCarouselSlide img-src="https://picsum.photos/1024/480/?image=20" />
          <BCarouselSlide img-src="https://picsum.photos/1024/480/?image=21" />
        </BCarousel>

      </template>

  </div>

</template>