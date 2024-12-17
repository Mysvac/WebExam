<script setup>
// 定义 props
import {ref} from 'vue';

const propsTable = defineProps({
  data: {
    type: Array,
    required: true,
  },
  operation: {
    type: Boolean,
    required: true,
  }
});

const filterTag = (value, row) => {
  return row.tag === value;
};
const emit = defineEmits(['deleteRow', 'selectIds']);
const deleteRow = (id) => {
  emit('deleteRow', id); // 触发 deleteRow 事件，传递 id
};

const selectedIds = ref([]);
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(row => row.id);
  emit('selectIds', selectedIds.value);
};

const getStatusColor = (status) => {
  switch (status) {
    case "未发布":
      return "gray"; // 未发布为灰色
    case "已发布":
      return "green"; // 已发布为绿色
    case "审核中":
      return "red"; // 审核中为红色
    default:
      return "black"; // 默认颜色为黑色
  }
};
</script>

<template>
  <el-table class="table"
            :row-style="{'height':'100px'}"
            :data="propsTable.data"
            :type="propsTable.operation ? 'selection' : ''"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"/>
    <el-table-column class="table-item" prop="id" label="广告序号" width="120px" sortable/>
    <el-table-column class="table-item" prop="status" label="状态" width="120px"
                     v-if="propsTable.operation">
      <template #default="scope">
        <span
            :style="{
            color: getStatusColor(scope.row.status),
            fontSize:'16px',
            fontWeight: 'bold',
          }"
        >
          {{ scope.row.status }}
        </span>
      </template>
    </el-table-column>
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
    <el-table-column label="操作" width="100px" v-if="propsTable.operation">
      <template #default="scope">
        <el-button type="danger" size="large" @click="deleteRow(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.table {
  height: 80%;
  padding-bottom: 20px;
  overflow: hidden; /* 防止内容溢出 */
  background-color: #ffffff; /* 表格背景颜色 */
}
</style>