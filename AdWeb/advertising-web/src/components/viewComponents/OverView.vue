<script setup>
import StyleBoard from "../utilsComponents/StyleBoard.vue";
import { ref } from 'vue'
const activeName = ref('1')
</script>

<template>
  <div class="overview-page">
    <h2>概览</h2>
    <div class="styleBoard-container">
      <StyleBoard/>
      <p>网站访问人数面板</p>
    </div>
    <div class="overView-description-container">
      <el-collapse class="collapse-column" v-model="activeName" accordion>
        <el-collapse-item title="仪表盘" name="1">
          <div class="collapse-item">
            广告的数据分析页面展示了广告的详细数据，包括用户行为分析、广告效果趋势图以及不同广告位的对比分析。
          </div>
        </el-collapse-item>
        <el-collapse-item class="collapse-column" title="广告管理" name="2">
          <div class="collapse-item">
            广告管理页面允许用户创建、编辑和删除广告活动，支持批量操作和广告状态的实时更新。
          </div>
          <div class="collapse-item">
            发布广告页面提供了广告素材的上传功能，支持多种广告格式（如图片、视频、文字链），并可设置广告的投放时间和目标受众。
          </div>
        </el-collapse-item>
        <el-collapse-item class="collapse-column" title="用户管理" name="3">
          <div class="collapse-item">
            用户管理页面用于管理平台用户，包括用户权限设置、用户分组以及用户数据的导出功能。
          </div>
          <div class="collapse-item">
            用户申请广告页面展示了用户提交的广告申请，管理员可以审核申请并决定是否批准广告投放。
          </div>
        </el-collapse-item>
        <el-collapse-item class="collapse-column" title="关于" name="4">
          <div class="collapse-item">
            关于我们页面提供了平台的背景信息、团队介绍以及联系方式，帮助用户更好地了解我们的服务和愿景。
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>

  </div>
</template>

<style scoped>
.overview-page {
  text-align: center;
  height: fit-content;
  padding: 50px;
  background-color: #f0f0f0;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #333;
  margin-bottom: 20px;
}

p {
  color: #666;
}

/* 容器样式 */
.overView-description-container {
  max-width:100%;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}


/* 折叠项标题样式 */
.collapse-column {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  background-color: #f0f0f0;
  transition: background-color 0.3s ease;
}


/* 折叠项内容文本样式 */
.collapse-item {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 10px;
}

.collapse-item:last-child {
  margin-bottom: 0;
}

</style>