<template>
    <div>
        <el-menu active-text-color="#ffd04b" background-color="#545c64" class="el-menu-vertical-demo"
            :default-active="activeIndex" text-color="#fff" @select="handleSelect"
            style="height: auto;position: fixed;margin-top: 5vh;border-radius: 0px 15px 15px 0px;width: 10vw;">
            <el-menu-item index="1" @click="directToProduce" style="border-radius: 0px 15px 15px 0px;width: 10vw;">
                <el-icon><icon-menu /></el-icon>
                <span>产品生产</span>
            </el-menu-item>
            <el-menu-item index="2" @click="directToTransport" style="border-radius: 0px 15px 15px 0px;width: 10vw;">
                <el-icon>
                    <document />
                </el-icon>
                <span>产品运输</span>
            </el-menu-item>
            <el-menu-item index="3" @click="directToTrade" style="border-radius: 0px 15px 15px 0px;width: 10vw;">
                <el-icon>
                    <setting />
                </el-icon>
                <span>产品交易</span>
            </el-menu-item>
            <el-menu-item index="4" @click="directToBlocks" style="border-radius: 0px 15px 15px 0px;width: 10vw;">
                <el-icon>
                    <Clock />
                </el-icon>
                <span>溯源明细</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script setup>
import {
    Document,
    Menu as IconMenu,
    Location,
    Setting,
    Clock,
} from '@element-plus/icons-vue'
import { generalStore } from '@/stores/counter'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
const router = useRouter()
const props = defineProps({
    activeIndex: Number
})
const activeIndex = ref(props.activeIndex)
const directToProduce = () => {
    router.push('/');
}
const directToTransport = () => {
    router.push('/transport');
}
const directToTrade = () => {
    router.push('/trade');
}
const directToBlocks = () => {
    router.push('/blocks');
}
</script>

<style scoped>
.motto {
    font-family: STFangsong;
    font-size: 1.1rem;
    margin-top: 4vh;
}
</style>