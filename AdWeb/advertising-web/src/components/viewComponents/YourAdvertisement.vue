<script setup>
import {onMounted, ref} from 'vue';
import AdvertisingTable from "../utilsComponents/advertisingTable.vue";
import service from "../../utils/service.js";
import AdvertisingSearch from "../utilsComponents/advertisingSearch.vue";


const tableData = ref([]);
const filteredTableData = ref([]);

async function fetchTableData() {
  try {
    const response = await service.post('/api/advertising-table-data');
    tableData.value = response.data.data;
    filterTableData();
  } catch (e) {
    console.error('获取表格数据失败:', e);
  }
}

const selectedIds = ref([]);

const handleSelectionChange = (ids) => {
  selectedIds.value = ids; // 更新选中的 id
};

async function deleteRows() {
  const length = selectedIds.value.length
  const ids = [];
  for (let i = 0; i < length; i++) {
    ids.push(selectedIds.value[i].id);
  }
  for (let i = 0; i < ids.length; i++) {
    await deleteRow(ids[i]);
  }
}

async function deleteRow(index) {
  try {
    console.log(index);
    const response = await service.post('/api/delete-advertising',
        {id: index});
    const json = response.data;
    if (json.code === 200) {
      tableData.value = tableData.value.filter(row => row.id !== index);
      filterTableData();
      console.log('广告删除成功:', response.data);
    } else {
      console.error('广告删除失败:', response.data.message);
    }
  } catch (e) {
    console.log(e);
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

const onAddItem = () => {
  console.log(1);
};

onMounted(() => {
  fetchTableData();
});
</script>

<template>
  <el-card class="card">
    <div class="header-row">
      <AdvertisingSearch @search="filterTableData"/>
      <el-button class="button" type="primary" @click="onAddItem">
        <i class="fa-solid fa-plus"></i>
      </el-button>
      <el-button class="button" type="primary" @click="deleteRows">
        shan
      </el-button>
    </div>
    <AdvertisingTable :data="filteredTableData"
                      :operation="true"
                      @deleteRow="deleteRow"
                      @selectionChange="handleSelectionChange"/>
  </el-card>

</template>

<style scoped>
.card {
  border-radius: 20px;
  margin-bottom: 10px;
  margin-top: 10px;
}
</style>