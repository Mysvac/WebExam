<script setup>
import {onMounted, ref, watchEffect} from 'vue';
import service from "../../utils/service.js";
import AdvertisingTable from "../utilsComponents/advertisingTable.vue";
import AdvertisingSearch from "../utilsComponents/advertisingSearch.vue";

const tableData = ref([]);
const filteredTableData = ref([]);

// 获取表格数据
async function fetchTableData() {
  try {
    const response = await service.post('/api/advertising-table-data');
    tableData.value = response.data.data;
    filterTableData(); // 初始化过滤数据
  } catch (e) {
    console.error('获取表格数据失败:', e);
  }
}

const filterTableData = (searchText) => {
  if (searchText === "") {
    filteredTableData.value = tableData.value;
  } else {
    filteredTableData.value = tableData.value.filter(row => {
      return (
          row.id.toString().includes(searchText) ||
          row.tag.toLowerCase().includes(searchText) || // 根据广告类型搜索
          row.distributor.toLowerCase().includes(searchText))
    });
  }
};

onMounted(() => {
  fetchTableData();
});
watchEffect(() => {
  filterTableData();
})
watchEffect(() => {
  const interval = setInterval(fetchTableData, 10000);
  return () => clearInterval(interval); // 清除定时器
});

</script>

<template>
  <el-card class="card">
    <AdvertisingSearch @search="filterTableData"/>
    <AdvertisingTable :data="filteredTableData"
                      :operation="false"/>
  </el-card>
</template>

<style scoped>
.card {
  border-radius: 20px;
  margin-bottom: 10px;
  margin-top: 10px;
}

</style>