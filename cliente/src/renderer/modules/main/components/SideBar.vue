<template>
    <aside class="sidebar">
      <UserInfo />
      <SearchBar />
      
      <div class="sidebar-section">
        <h3 class="section-title">Canales</h3>
        <ChannelList />
      </div>
      
      <div class="sidebar-section">
        <h3 class="section-title">Contactos</h3>
        <ContactList />
      </div>
    </aside>
  </template>
  
  <script script setup lang="ts">
  import UserInfo from './UserInfo.vue';
  import SearchBar from './SearchBar.vue';
  import ChannelList from './ChannelList.vue';
  import ContactList from './ContactList.vue';
  
  </script>
  
  <style scoped>
  .sidebar {
    width: 260px;
    background-color: var(--secondary-bg);
    height: 100%;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    border-right: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .sidebar-section {
    padding: 0.5rem;
    margin-top: 1rem;
  }
  
  .section-title {
    color: var(--text-secondary);
    font-size: 0.9rem;
    text-transform: uppercase;
    margin-bottom: 0.5rem;
    padding-left: 0.5rem;
  }
  </style>