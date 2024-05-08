<template>
    <div class="common-layout">
        <div class="common-layout">
            <el-container>
                <el-header>
                    <Header />
                </el-header>
                <el-container>
                    <el-aside width="200px">
                        <AsideMenu />
                    </el-aside>
                    <el-main style="display: flex;flex-direction: column;">
                        <el-card style="">
                            <h3>产品溯源链参与者</h3>
                            <el-table :data="participants" style="width: 100%">
                                <el-table-column prop="name" label="名称" width="180" />
                                <el-table-column prop="balance" label="账户余额" width="180" />
                                <el-table-column prop="privateKey" label="私钥" />
                                <el-table-column prop="publicKey" label="公钥" />
                            </el-table>
                        </el-card>
                        <div style="display: flex;flex-direction: row;margin-top: 2vh;">
                            <el-card style="width: 48%;">
                                <h3>当前区块链明细</h3>
                                <el-timeline>
                                    <div v-for="(item, index) in blockChain" :key="index">
                                        <el-timeline-item :timestamp="'第' + index + '区块'" placement="top">
                                            <el-card style="white-space: normal;">
                                                <h4>区块Hash值：{{ item.hash }}</h4>
                                                <h4>前一区块Hash值：{{ item.previousHash }}</h4>
                                                <h4>区块nonce值：{{ item.nonce }}</h4>
                                                <h4>区块时间戳：{{ item.timeStamp }}</h4>
                                            </el-card>
                                        </el-timeline-item>
                                    </div>
                                </el-timeline>
                            </el-card>
                            <el-card style="width: 48%;margin-left: 2vw;">
                                <h3>产品溯源明细</h3>
                                <el-timeline>
                                    <div v-for="(item, index) in transactions" :key="index">
                                        <el-timeline-item :timestamp="'第' + index + '笔交易'" placement="top">
                                            <el-card>
                                                <h4>交易信息：{{ item.info }}</h4>
                                                <h4>交易发起人：{{ item.senderName }}</h4>
                                                <h4>交易接收者：{{ item.receiverName }}</h4>
                                                <h4>交易金额：{{ item.value }}</h4>
                                            </el-card>
                                        </el-timeline-item>
                                    </div>
                                </el-timeline>
                            </el-card>
                        </div>
                    </el-main>
                </el-container>
            </el-container>
        </div>
    </div>
</template>

<script setup>
import AsideMenu from '@/components/menus/index.vue'
import Header from '@/components/header/index.vue'
import axios from 'axios'
import { onMounted, ref } from 'vue'
onMounted(() => {
    queryParticipants()
    queryChain()
    queryTransaction()
})
const participants = ref([])
const queryParticipants = async() => {
    let response = await axios.get('/api/blockChain/participants');
    participants.value = response.data.data
}

const blockChain = ref([])
const queryChain = async () => {
    let response = await axios.get('/api/blockChain');
    blockChain.value = response.data.data
}

const transactions = ref([])
const queryTransaction = async() => {
    let response = await axios.get('/api/blockChain/transactions');
    transactions.value = response.data.data
    console.log(transactions.value)
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