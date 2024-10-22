<template>
  <v-sheet width="500" class="mx-auto">
    <h2>Buch</h2>
    <v-form ref="form">
      <v-text-field v-if="isEditing" v-model="localForm.id" label="ID" ref="true" disabled></v-text-field>
      <v-text-field
          v-model="localForm.title"
          :counter="255"
          label="Titel"
          required></v-text-field>

      <v-text-field
          v-model="localForm.author"
          :counter="255"
          label="Autor"
          required></v-text-field>

      <v-text-field
          v-model="localForm.isbn"
          :counter="255"
          label="ISBN"
          required></v-text-field>

      <v-text-field
          v-model="localForm.description"
          :counter="255"
          label="Beschreibung"
          required></v-text-field>

      <v-text-field
          v-model="localForm.publisher"
          :counter="255"
          label="Herausgeber"
          required></v-text-field>

      <v-text-field
          v-model="localForm.publishedDate"
          :counter="255"
          label="Veröffentlichungsdatum"
          required></v-text-field>

      <div class="d-flex flex-column">
        <v-btn color="primary" class="mt-4" block @click="isEditing ? modify() : create()">
          {{ isEditing ? 'Speichern' : 'Erstellen' }}
        </v-btn>
        <v-btn class="custom-button mt-4" color="success" block @click="close">Abbrechen</v-btn>

        <v-btn v-if="isEditing" color="error" class="mt-4" block @click="remove()">Löschen</v-btn>
        <v-btn v-if="!isEditing" color="orange" class="custom-button mt-4" block @click="reset">Formular Zurücksetzen</v-btn>

      </div>
    </v-form>
  </v-sheet>
</template>

<script setup type="ts">
import {computed, onMounted, reactive} from "vue";
import {useBookStore, useSeriesStore} from "@/stores/common"

const store = useBookStore()
const seriesStore = useSeriesStore()
const series = computed(() => seriesStore.get)
const props = defineProps({
  form: {
    type: Object,
    default: () => ({ id: null,
      title: null,
      author: null,
      isbn: null,
      description: null,
      publisher: null,
      publishedDate: null}),
    required: false
  },
})
let localForm = reactive({ ...props.form });
const isEditing = computed(() => localForm.id !== undefined && localForm.id !== null)
const emit = defineEmits(['close-form'])

onMounted(async () => {
  await fetchData()
})

async function fetchData() {
  try {
    await store.init()
    await seriesStore.init()
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}

async function create() {
  await store.add({
    title: localForm.title,
    author:  localForm.author,
    isbn:  localForm.isbn,
    description:  localForm.description,
    publisher:  localForm.publisher,
    publishedDate:  localForm.publishedDate,
  })
  close()
}

function close() {
  emit('close-form', null)
}

function modify() {
  store.change(localForm)
  emit('close-form', null)
}

function remove() {
  store.remove(localForm.id)
  close()
}

function reset() {
  for (let key in localForm) {
    localForm[key] = null
  }
}
</script>
