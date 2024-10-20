<template>
  <v-progress-circular v-if="loading" indeterminate color="primary"></v-progress-circular>
  <v-toolbar flat color="blue-lighten-4">
  <v-toolbar-title>Warenkorb</v-toolbar-title>
  </v-toolbar>
  Preis berechnen:
  <v-text-field v-model="books" label="Preis" readonly></v-text-field>
  <v-btn color="primary" @click="calculatePrice">Optimalen Preis berechnen</v-btn>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {useBookStore} from "@/stores/common"
const store = useBookStore()
let cart = reactive([])
let loading = ref(true)
loading.value = true
const books = ref("");

const  initializeCart = async() => {
  try {
    const storedCart = localStorage.getItem('cart');
    if(storedCart) {
      cart = JSON.parse(storedCart);
      await nextTick()
    }
  } catch (e) {
    console.error('Store error:', e)
  }
}

onMounted(async() => {
  await store.init();
 await initializeCart();
  loading.value = false;
})

const calculatePrice = async () => {
  try {
    await store.calculatePrice(cart);
    books.value = await store.price
    localStorage.setItem('cart', JSON.stringify({}));
  } catch (error) {
    console.error('Fehler beim Abrufen der Bücher:', error);
  }}
</script>
<style scoped></style>
