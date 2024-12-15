<template>
  <v-chart class="chart" :option="options" autoresize/>
</template>

<script setup>
import {use} from 'echarts/core';
import {CanvasRenderer} from 'echarts/renderers';
import {PieChart} from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components';
import VChart from 'vue-echarts';
import {ref, computed, defineProps} from 'vue';

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

const props = defineProps({
  data: Array,
  title: {
    type: String,
    default: ''
  }
});

const title = ref(props.title);

const data = ref(props.data);

const options = computed(() => {
  return {
    title: {
      text: title.value,
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: data.value.map(d => d.name),
    },
    series: [
      {
        name: title.value,
        type: 'pie',
        radius: '55%',
        center: ['50%', '60%'],
        data: data.value,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  }
})

</script>

<style scoped>
.chart {
  height: 300px;
}
</style>
