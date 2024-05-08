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
                            <el-card style="width: 80%;">
                                <h3>您的身份是：交易商</h3>
                                <el-form :model="form" label-width="auto" style="max-width: 600px;margin-top: 5vh">
                                    <el-form-item label="商品名称">
                                        <el-input v-model="form.goods" />
                                    </el-form-item>
                                    <el-form-item label="交易信息">
                                        <el-input v-model="form.info" />
                                    </el-form-item>
                                    <el-form-item label="卖家">
                                        <el-input v-model="form.fromName" />
                                    </el-form-item>
                                    <el-form-item label="买家">
                                        <el-input v-model="form.toName" />
                                    </el-form-item>
                                    <el-form-item label="交易金额">
                                        <el-input v-model="form.value" />
                                    </el-form-item>
                                </el-form>
                                <el-button type="primary" @click="submit">创建交易</el-button>
                                <el-button type="primary" @click="reset">重置交易</el-button>
                            </el-card>
                            <el-card style="margin-left: 2vw;width: 18%">
                                <h3>
                                    余额查询
                                </h3>
                                <el-input v-model="name" placeholder="请输入账户名"></el-input>
                                <el-button type="primary" size="small" @click="queryBalance"
                                    style="margin-top: 2vh;">查询</el-button>
                                <el-container v-if="balance!=-1" style="margin-top: 2vh;">账户余额为：{{ balance
                                    }}</el-container>
                                <el-container v-else style="color: red;margin-top: 2vh;">账户不存在！</el-container>
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
                                <h4>交易信息：{{ block.value.transactions[0].info }}</h4>
                                <h4>交易金额：{{ block.value.transactions[0].value }}</h4>
                                <h4>交易发起人名称：{{ block.value.transactions[0].senderName }}</h4>
                                <h4>交易发起人密钥：{{ block.value.transactions[0].sender }}</h4>
                                <h4>交易接收者名称：{{ block.value.transactions[0].receiverName }}</h4>
                                <h4>交易接收者密钥：{{ block.value.transactions[0].receiver }}</h4>
                                <h4>交易签名：{{ block.value.transactions[0].signature }}</h4>
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
    goods: '',
    info: '',
    toName: '',
    fromName: '',
    value: '',
})
const name = ref("")
const balance = ref(0)
const block = reactive({})
const submit = async () => {

    let formData = new FormData();
    formData.append('info', form.info);
    formData.append('fromName', form.fromName);
    formData.append('toName', form.toName);
    formData.append('value', form.value); // 假设 value 是表单中的一个字段

    let response = await axios.post('/api/blockChain/block', formData);
    block.value = response.data.data
    console.log(block)
}
const queryBalance = async () => {

    let formData = new FormData();
    formData.append('name', name.value);
    let response = await axios.get('/api/blockChain/balance?name='+name.value);
    balance.value = response.data.data
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