<template>
  <el-form
      ref="ruleFormRef"
      style="max-width: 600px"
      :model="ruleForm"
      :rules="rules"
      label-width="auto"
      class="demo-ruleForm"
      :size="'large'"
      status-icon
  >
    <!-- 广告类型 -->
    <el-form-item label="广告类型" prop="tag">
      <el-select v-model="ruleForm.tag" placeholder="请选择广告类型">
        <el-option label="电子产品" value="电子产品"/>
        <el-option label="家居用品" value="家居用品"/>
        <el-option label="服装服饰" value="服装服饰"/>
        <el-option label="美妆护肤" value="美妆护肤"/>
        <el-option label="食品饮料" value="食品饮料"/>
        <el-option label="汽车交通" value="汽车交通"/>
        <el-option label="旅游出行" value="旅游出行"/>
      </el-select>
    </el-form-item>

    <!-- 发布商 -->
    <el-form-item label="广告标题" prop="title">
      <el-input v-model="ruleForm.title"/>
    </el-form-item>
    <!-- 广告描述 -->
    <el-form-item label="广告描述" prop="description">
      <el-input v-model="ruleForm.description" type="textarea"/>
    </el-form-item>

    <!-- 发布商 -->
    <el-form-item label="发布商" prop="distributor">
      <el-input v-model="ruleForm.distributor"/>
    </el-form-item>

    <!-- 广告价格 -->
    <el-form-item label="广告价格" prop="cost">
      <el-input v-model="ruleForm.cost"/>
    </el-form-item>

    <!-- 文件类型选择 -->
    <el-form-item label="文件类型" prop="fileType">
      <el-select v-model="ruleForm.fileType" placeholder="请选择文件类型">
        <el-option label="图片" value="image"/>
        <el-option label="视频" value="video"/>
        <el-option label="文档" value="document"/>
      </el-select>
    </el-form-item>

    <!-- 文件上传 -->
    <el-form-item label="广告资源" prop="file">
      <el-upload
          class="upload-demo"
          :action="fileUploadedPath"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeUpload"
          :file-list="fileList"
          :limit="1"
      >
        <el-button type="primary">点击上传</el-button>
        <template #tip>
          <div class="el-upload__tip">
            只能上传一个文件，且文件类型必须与选择的文件类型匹配
          </div>
        </template>
      </el-upload>
    </el-form-item>

    <!-- 操作按钮 -->
    <el-form-item>
      <el-button type="primary" @click="submitForm">
        创建
      </el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from "element-plus";
import service from "../../utils/service.js";
import printJsonToConsole from "../../utils/printJsonToConsole.js";

// 表单引用
const ruleFormRef = ref()
const fileUploadedPath = ref('http://localhost:8080/api/advertising-file-upload');
// 表单数据
const ruleForm = reactive({
  tag: '',
  title:'',
  description: '',
  distributor: '',
  cost: '',
  fileType: '', // 文件类型
  file: null, // 上传的文件
})

// 文件列表
const fileList = ref([])

// 表单验证规则
const rules = {
  tag: [
    {required: true, message: '请选择广告类型', trigger: 'change'},
  ],
  title: [
    {required: true, message: '请输入广告标题', trigger: 'blur'},
  ],
  description: [
    {required: true, message: '请输入广告描述', trigger: 'blur'},
  ],
  distributor: [
    {required: true, message: '请输入发布商', trigger: 'blur'},
  ],
  cost: [
    {required: true, message: '请输入广告价格', trigger: 'blur'},
  ],
  fileType: [
    {required: true, message: '请选择文件类型', trigger: 'change'},
  ],
  file: [
    {required: false, message: '请上传广告资源', trigger: 'blur'},
  ],
}

const fileId = ref(-1);
// 上传成功回调
const handleUploadSuccess = (response, file) => {
  if(response.code === 200){
    console.log('文件上传成功:');
    fileId.value = response.data.index;
  }
  ruleForm.file = file
}

// 上传失败回调
const handleUploadError = (error) => {
  console.error('文件上传失败:', error)
}

// 上传前检查
const beforeUpload = (file) => {
  const allowedTypes = {
    image: ['image/jpeg', 'image/png', 'image/gif'],
    video: ['video/mp4', 'video/avi'],
    document: ['application/pdf', 'application/txt'],
  }

  const fileType = ruleForm.fileType
  const allowedExtensions = allowedTypes[fileType] || []

  if (!allowedExtensions.includes(file.type)) {
    ElMessage.error(`文件类型不匹配，请上传 ${fileType} 类型的文件`)
    return false
  }

  return true
}

// 提交表单
const submitForm = () => {
  if (!ruleFormRef.value) return
  ruleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await service.post('http://localhost:8080/api/upload-advertising', {
          tag: ruleForm.tag,
          title:ruleForm.title,
          description: ruleForm.description,
          distributor: ruleForm.distributor,
          cost: ruleForm.cost,
          fileId: fileId.value // 只上传文件名
        });
        const jsonData = response.data
        if (jsonData.code === 200) {
          const json = jsonData.data;
          printJsonToConsole(json);
        } else {
          console.log('失败');
        }
      } catch (e) {
        console.log(e.message);
      }
    } else {
      console.log('表单验证失败!')
    }
  })
}

// 重置表单
const resetForm = () => {
  if (!ruleFormRef.value) return
  ruleFormRef.value.resetFields()
  fileList.value = [] // 清空文件列表
}
</script>

<style scoped>
.upload-demo {
  width: 100%;
}
</style>