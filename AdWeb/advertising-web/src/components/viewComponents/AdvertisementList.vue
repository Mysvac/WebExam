<script setup>
import {onMounted, ref, watchEffect} from 'vue';
import service from "../../utils/service.js";

const input = ref("");
const tableData = ref([]);
const filteredTableData = ref([]); // 过滤后的表格数据

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

const filterTag = (value, row) => {
  return row.tag === value;
};

const filterTableData = () => {
  const searchText = input.value.trim().toLowerCase(); // 获取输入框内容并转换为小写
  if (searchText === "") {
    filteredTableData.value = tableData.value;
  } else {
    filteredTableData.value = tableData.value.filter(row => {
      return (
          row.id.toString().includes(searchText) ||
          row.tag.toLowerCase().includes(searchText) || // 根据广告类型搜索
          row.distributor.toLowerCase().includes(searchText) || // 根据发布商搜索
          row.cost.toString().includes(searchText))// 根据广告价格搜索);
    });
  }
};
onMounted(() => {
  fetchTableData();
});
watchEffect(()=>{
  filterTableData();
})
watchEffect(() => {
  const interval = setInterval(fetchTableData, 10000);
  return () => clearInterval(interval); // 清除定时器
});

</script>

<template>
  <el-card class="card">
    <div class="search">
      <i class="fa-solid fa-magnifying-glass"></i>
      <el-input
          v-model="input"
          style="width: 240px; margin-left: 10px"
          placeholder="Please input"
          clearable
      />
    </div>
    <el-table class="table" :data="filteredTableData">
      <el-table-column class="table-item" prop="id" label="广告序号" width="120px" sortable/>
      <el-table-column class="table-item" prop="tag" label="广告类型" width="100px"
                       :filters="[
                         { text: '电子产品', value: '电子产品' },
                         { text: '家居用品', value: '家居用品' },
                         { text: '服装服饰', value: '服装服饰' },
                         { text: '美妆护肤', value: '美妆护肤' },
                         { text: '食品饮料', value: '食品饮料' },
                         { text: '汽车交通', value: '汽车交通' },
                         { text: '旅游出行', value: '旅游出行' },
                       ]"
                       :filter-method="filterTag"
                       filter-placement="bottom-end"
      />
      <el-table-column class="table-item" prop="description" label="广告描述"/>
      <el-table-column class="table-item" prop="distributor" label="发布商" width="100px"/>
      <el-table-column class="table-item" prop="cost" label="广告价格" width="120px" sortable/>
    </el-table>
  </el-card>
</template>

<style scoped>
.card {
  border-radius: 20px;
  margin-bottom: 10px;
  margin-top: 10px;
}

.table {
  height: 80%;
  padding-bottom: 20px;
  overflow: hidden; /* 防止内容溢出 */
  background-color: #ffffff; /* 表格背景颜色 */
}

.search {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

</style>