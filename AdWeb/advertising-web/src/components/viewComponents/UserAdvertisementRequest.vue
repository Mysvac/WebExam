<script setup>
import { onMounted, ref } from 'vue';
import service from "../../utils/service.js";

const appliedAds = ref([]); // 存储申请到的广告资源

// 获取申请到的广告资源
async function fetchAppliedAds() {
  try {
    const response = await service.get('/api/applied-id-ads'); // 获取申请到的广告资源
    appliedAds.value = response.data.data;
    console.log('申请到的广告资源:', appliedAds.value);
  } catch (e) {
    console.error('获取申请到的广告资源失败:', e);
  }
}

// 生成 API 接口
function generateApiUrl(adId) {
  return `https://your-api-server.com/api/ad/${adId}`; // 替换为实际的 API 服务器地址
}

onMounted(() => {
  fetchAppliedAds(); // 初始化时获取申请到的广告资源
});
</script>

<template>
  <div>
    <el-table :data="appliedAds" style="width: 100%">
      <el-table-column prop="id" label="广告 ID" width="100" />
      <el-table-column prop="name" label="广告名称" width="200" />
      <el-table-column prop="description" label="广告描述" width="300" />
      <el-table-column label="API 接口" width="400">
        <template #default="scope">
          <el-input
              :value="generateApiUrl(scope.row.id)"
              readonly
              placeholder="API 接口"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<style scoped></style>