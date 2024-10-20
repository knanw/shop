<template>
  <v-app>
    <v-app-bar app color="primary" elevated>
      <v-app-bar-nav-icon @click.stop="toggleMenu"/>
      <v-toolbar-title class="text-uppercase" style="color: white">
        <span class="font-weight-thin">Book</span>
        <span class="font-weight-bold"> Webshop</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <span><a href="https://knanw.github.io/" target="_blank" rel="noopener" class=" mr-5">
        <v-icon color="white">mdi-information</v-icon></a></span>
    </v-app-bar>

    <v-navigation-drawer v-model="isMenuOpen" app color="primary">
      <v-list>
        <v-list-item v-for="link in links" :key="link.to" :to="link.to">
          <v-list-item-action>
            <v-icon class="white--text">{{ link.icon }}</v-icon>
            <v-list-item-title class="white--text pl-5">{{ link.label }}</v-list-item-title>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import {ref} from "vue";

const isMenuOpen = ref(false);
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const links = [
  {label: "Home", icon: "mdi-home", to: "/"},
  {label: "Book", icon: "mdi-book-open-page-variant-outline", to: "/book"},
  {label: "Series", icon: "mdi-bookshelf", to: "/series"},
  {label: "Cart", icon: "mdi-cart", to: "/cart"},
  {label: "Contact", icon: "mdi-account-tie", to: "/contact"},
]
</script>