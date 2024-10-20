<template>
  <v-sheet width="500" class="mx-auto" v-if="!loading">
    <h2>Serie</h2>
    <v-form ref="form">
      <v-text-field v-if="isEditing" v-model="localForm.id" label="ID" ref="true" disabled></v-text-field>
      <v-text-field
          v-model="localForm.name"
          :counter="255"
          label="Name"
          required></v-text-field>

      <v-text-field
          v-model="localForm.genre"
          :counter="255"
          label="Genre"
          required></v-text-field>

      <v-text-field
          v-model="localForm.description"
          :counter="255"
          label="Beschreibung"
          required></v-text-field>

      <v-select
          v-if="loading === false"
          v-model="localForm.books"
          :items="books"
          item-key="id"
          item-value="id"
          item-title="title"
          label="Bücher"
          placeholder="Bücher auswählen"
          clearable
          chips
          multiple
          return-object>
      </v-select>

      <div class="d-flex flex-column">
        <v-btn color="primary" class="mt-4" block @click="isEditing ? modify() : create()">
          {{ isEditing ? 'Speichern' : 'Erstellen' }}
        </v-btn>
        <v-btn v-if="!isEditing" class="custom-button mt-4" color="orange" block @click="reset">Formular Zurücksetzen</v-btn>

        <v-btn v-if="isEditing" color="error" class="mt-4" block @click="remove()">Löschen</v-btn>
        <v-btn class="custom-button mt-4" color="success" block @click="close">Abbrechen</v-btn>

      </div>
    </v-form>
  </v-sheet>
</template>

<script setup type="ts">
import {computed, onMounted, reactive, ref} from "vue";
import {useBookStore, useSeriesStore} from "@/stores/common"

const store = useSeriesStore()
const bookStore = useBookStore()

const books = computed(() => bookStore.gets)
let loading = ref(true)
const props = defineProps({
  form: {
    type: Object,
    default: () => ({ id: null,
      name: null,
      genre: null,
      description: null,
      books:[]}),
    required: false
  },
})
let localForm = reactive({ ...props.form });
const isEditing = computed(() => localForm.id !== undefined && localForm.id !== null)
const emit = defineEmits(['close-form'])

onMounted(async () => {
  await fetchData()
  loading.value = false
})

async function fetchData() {
  try {
    await store.init()
    await bookStore.init()
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}

async function create() {
  await store.add({
    name: localForm.name,
    genre:  localForm.genre,
    description:  localForm.description,
    description:  localForm.description,
    books: localForm.books,
  })
  close()
}

function close() {
  emit('close-form', null)
}

function modify() {
  store.change(localForm)
  close()
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
