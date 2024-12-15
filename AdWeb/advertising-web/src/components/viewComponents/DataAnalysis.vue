<script setup>
import AdvertisingPie from "../utilsComponents/advertisingPie.vue";
import {ref} from "vue";

const chartTitle = ref('广告来源分布');
const chartData = ref([
  {value: 335, name: 'Direct'},
  {value: 310, name: 'Email'},
  {value: 234, name: 'Ad Networks'},
  {value: 135, name: 'Video Ads'},
  {value: 1548, name: 'Search Engines'},
]);
</script>

<template>
  <div class="analysis-page">
    <div class="chart-container">
      <AdvertisingPie :title="chartTitle" :data="chartData"></AdvertisingPie>
    </div>
  </div>

</template>

<style scoped>
.analysis-page {
  text-align: center;
  height: 80%;
  padding: 50px;
  background-color: #f0f0f0;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chart-container {
  width: 600px;
  height: 400px;
  margin: auto;
}
</style>