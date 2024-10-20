<template>
  <v-progress-circular v-if="loading" indeterminate color="primary"></v-progress-circular>

  <v-data-table v-else
                fixed-header
                density="compact"
                :headers="headers"
                item-key="item.id"
                :items="items"
                @click:row="goToDetails">

    <template #top>
      <v-toolbar flat color="blue-lighten-4">
        <v-toolbar-title>Bücher</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon color="primary" dark @click="createDialog">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-toolbar>
      <v-dialog v-model="dialog" max-width="600px" @click:outside="closeDialog(value)">
        <Book :form="selectedItem" @close-form="closeDialog(value)"/>
      </v-dialog>
    </template>

    <template #item.action="{ item }">
      <v-btn icon color="blue-lighten-4" @click.stop="addToCart(item)">
        <v-icon color="primary">mdi-basket</v-icon>
      </v-btn>
    </template>
  </v-data-table>

</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue'
import {useBookStore} from "@/stores/common"
import {useRouter} from "vue-router"
import type {BookDTO} from "@/types";

const router = useRouter()
const store = useBookStore()

const items = computed(() => store.gets)

const emptyBookDTO = (): BookDTO => ({
  id: null,
  title: null,
  author: null,
  isbn: null,
  description: null,
  publisher: null,
  publishedDate: null,
});

let selectedItem = reactive(emptyBookDTO());
let loading = ref(true)
loading.value = true
const search = ref('')
const selectedRowId = ref()
let dialog = ref(false)

let cart = []

onMounted(async () => {
  await fetchData()
})

async function fetchData() {
  try {
    await store.init()
    loading.value = false
  } catch (e) {
    console.error('Store error:', e)
  }
}

const headers = ref([
  {title: 'Id', key: 'id'},
  {title: 'Titel', key: 'title'},
  {title: 'Autor', key: 'author'},
  {title: 'Isbn', key: 'isbn'},
  {title: 'Herausgeber', key: 'publisher'},
  {title: 'Beschreibung', key: 'description'},
  {title: 'Veröffentlichungsdatum', key: 'publishedDate'},
  {title: 'Aktion', value: 'action', sortable: false }])

function createDialog() {
  selectedItem = emptyBookDTO()
  dialog.value = true
}

function reset() {
  selectedItem = emptyBookDTO()
}

function closeDialog(value) {
  fetchData()
  dialog.value = false
  reset()
}

const goToDetails = (event: MouseEvent, item: any) => {
  selectedItem = items.value.find(o => o.id === item.internalItem.raw.id)
  dialog.value = true
}

function created() {
  this.cart = JSON.parse(localStorage.getItem('cart')) || []
}

function addToCart(book) {
    this.cart.push(book);
    localStorage.setItem('cart', JSON.stringify(this.cart))
  }

function removeFromCart(book) {
  this.cart = cart.filter(item => item.id !== book.id)
  localStorage.setItem('cart', JSON.stringify(this.cart))
}
</script>