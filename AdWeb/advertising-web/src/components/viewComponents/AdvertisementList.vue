<script setup>
import {onMounted, ref, watchEffect} from 'vue';
import service from "../../utils/service.js";
import AdvertisingTable from "../utilsComponents/advertisingTable.vue";
import AdvertisingSearch from "../utilsComponents/advertisingSearch.vue";

const tableData = ref([]);
const filteredTableData = ref([]);
const pagedTableData = ref([]); // 分页后的数据
const currentPage = ref(1); // 当前页码
const pageSize = ref(8); // 每页显示的条数

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

const filterTableData = (searchText = "") => {
  if (searchText === "") {
    filteredTableData.value = tableData.value;
  } else {
    filteredTableData.value = tableData.value.filter(row => {
      return (
          row.id.toString().includes(searchText) ||
          row.tag.toLowerCase().includes(searchText) || // 根据广告类型搜索
          row.distributor.toLowerCase().includes(searchText)
      );
    });
  }
  handlePageChange(); // 过滤后重新分页
};

// 处理分页逻辑
const handlePageChange = () => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  pagedTableData.value = filteredTableData.value.slice(start, end);
};

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page;
  handlePageChange();
};

// 处理每页显示条数变化
const handleSizeChange = (size) => {
  pageSize.value = size;
  handlePageChange();
};

onMounted(() => {
  fetchTableData();
});

watchEffect(() => {
  filterTableData();
});

watchEffect(() => {
  const interval = setInterval(fetchTableData, 10000);
  return () => clearInterval(interval); // 清除定时器
});
</script>

<template>
  <el-card class="card">
    <div class="header-row">
      <AdvertisingSearch @search="filterTableData"/>
      <div class="demo-pagination-block">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            layout="prev, pager, next, jumper"
            :total="filteredTableData.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <AdvertisingTable :data="pagedTableData"
                      :operation="false"/>
  </el-card>
</template>

<style scoped>
.card {
  border-radius: 20px;
  margin-bottom: 10px;
  margin-top: 10px;
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>