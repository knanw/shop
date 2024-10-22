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
        <v-toolbar-title>Serien</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon color="primary" dark @click="createDialog">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-toolbar>
      <v-dialog v-model="dialog" max-width="600px" @click:outside="closeDialog(value)">
        <Series :form="selectedItem" @close-form="closeDialog(value)"/>
      </v-dialog>
    </template>

    <template #item.books="{ item }">
      <v-chip-group column>
        <v-chip v-for="book in item.books" :key="book.id" color="primary" dark>
          {{ book.title }}
        </v-chip>
      </v-chip-group>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue'
import {useSeriesStore} from "@/stores/common"
import type {SeriesDTO} from "@/types";
import Series from "~/components/Series.vue";

const store = useSeriesStore()
const items = computed(() => store.gets)

const emptySeriesDTO = (): SeriesDTO => ({
  id: null,
  name: null,
  genre: null,
  description: null,
  books: [],
});

let selectedItem = reactive(emptySeriesDTO());
let loading = ref(true)
loading.value = true
let dialog = ref(false)

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
  {title: 'Name', key: 'name'},
  {title: 'Genre', key: 'genre'},
  {title: 'Beschreibung', key: 'description'},
  {title: 'Bücher', key: 'books'},
 ])

function createDialog() {
  selectedItem = null
  dialog.value = true;
}

function reset() {
  selectedItem = emptySeriesDTO();
}

function closeDialog(value) {
  fetchData()
  dialog.value = false
  reset()
}

const goToDetails = (event: MouseEvent, item: any) => {
  selectedItem = items.value.find(o => o.id === item.internalItem.raw.id)
  dialog.value = true;
}

</script>