<template>
    <div class="common-layout">
        <div class="common-layout">
            <el-container>
                <el-header>
                    <Header />
                </el-header>
                <el-container>
                    <el-aside width="200px">
                        <AsideMenu :active-index="1" />
                    </el-aside>
                    <el-main>
                        <el-container style="display: flex;flex-direction: row">
                            <el-card style="width: 60%;">
                                <h3>您的身份是：生产商</h3>
                                <el-form :model="form" label-width="auto" style="max-width: 600px;margin-top: 5vh">
                                    <el-form-item label="商品名称">
                                        <el-input v-model="form.goods" />
                                    </el-form-item>
                                    <el-form-item label="商品信息">
                                        <el-input v-model="form.info" />
                                    </el-form-item>
                                    <el-form-item label="生产商名称">
                                        <el-input v-model="form.fromName" />
                                    </el-form-item>
                                    <el-form-item label="生产后运输至">
                                        <el-input v-model="form.toName" />
                                    </el-form-item>
                                </el-form>
                                <el-button type="primary" @click="submit">创建工单</el-button>
                                <el-button type="primary" @click="reset">重置工单</el-button>
                            </el-card>
                            <el-card style="margin-left: 2vw;width: 38%">
                                <h3>
                                    您已生产
                                </h3>
                                <el-table :data="products" style="width: 100%">
                                    <el-table-column prop="name" label="产品名称" width="100"/>
                                    <el-table-column prop="date" label="生产日期"/>
                                </el-table>
                            </el-card>
                        </el-container>
                        <el-card style="margin-top: 5vh;">
                            <h3>当前区块明细<span style="color: red;">（仅做学习使用以展示）</span></h3>
                            <el-card style="margin-left: 2vw;" v-if="block.value">
                                <h4>区块Hash值：{{ block.value.hash }}</h4>
                                <h4>区块nonce值：{{ block.value.nonce }}</h4>
                                <h4>前一区块Hash值：{{ block.value.previousHash }}</h4>
                                <h4>区块时间戳：{{ block.value.timeStamp }}</h4>
                            </el-card>
                            <h3 v-if="block.value">区块交易信息明细</h3>
                            <el-card style="margin-left: 2vw;" v-if="block.value">
                                <h4>生产人名称：{{ block.value.transactions[0].senderName }}</h4>
                                <h4>交易发起人密钥：{{ block.value.transactions[0].sender }}</h4>
                                <h4>第一接收者名称：{{ block.value.transactions[0].receiverName }}</h4>
                                <h4>交易接收者密钥：{{ block.value.transactions[0].receiver }}</h4>
                                <h4>交易签名：{{ block.value.transactions[0].signature }}</h4>
                                <h4>交易信息：{{ block.value.transactions[0].info }}</h4>
                            </el-card>
                        </el-card>
                    </el-main>
                </el-container>
            </el-container>
        </div>
    </div>
</template>

<script setup>
import AsideMenu from '@/components/menus/index.vue'
import Header from '@/components/header/index.vue'
import { reactive, ref } from 'vue'
import axios from 'axios'
const form = reactive({
    goods:'',
    info: '',
    toName: '',
    fromName: '',
    value: 0,
})
const block = reactive({})
const products = ref([])
const formatTime = (time) => {
    const originalTime = "2024-05-08T09:49:19.673+00:00";
    const date = new Date(originalTime);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
const submit = async() => {

    // 获取本地存储的 goods 数组，如果不存在则创建一个空数组
    let goodsArray = JSON.parse(localStorage.getItem('goods')) || []
    // 将 form.goods 添加到数组后面
    goodsArray.push(form.goods)
    // 将更新后的数组重新存储到本地存储中
    localStorage.setItem('goods', JSON.stringify(goodsArray))

    let formData = new FormData();
    formData.append('info', form.info);
    formData.append('fromName', form.fromName);
    formData.append('toName', form.toName);
    formData.append('value', form.value); // 假设 value 是表单中的一个字段

    let response = await axios.post('/api/blockChain/block', formData);
    block.value = response.data.data

    let formData2 = new FormData();
    formData2.append('name', form.goods);
    let response2 = await axios.post('/api/blockChain/products', formData2);
    products.value = response2.data.data
    products.value.forEach(product => {
        product.date = formatTime(product.date)
    });
}
const reset = () => {
    Object.keys(form).forEach(key => {
        form[key] = ''
    })
    form.value = 0
}
</script>

<style scoped>
.common-layout {
    height: 100vh;
}

.el-header {
    padding: 0;
}
</style>