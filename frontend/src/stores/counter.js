import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const generalStore = defineStore('general', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  return { count, doubleCount, increment }
})
