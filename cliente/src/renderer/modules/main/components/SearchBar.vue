<template>
    <div class="search-container">
      <div class="search-input-container">
        <input 
          type="text" 
          class="search-input" 
          placeholder="Buscar..." 
          v-model="searchQuery"
        />
        <span class="search-icon">🔍</span>
      </div>
      
      <div class="filter-options">
        <label class="filter-option">
          <input type="radio" name="filter" value="all" v-model="filterType" checked>
          <span class="filter-label">Todos</span>
        </label>
        <label class="filter-option">
          <input type="radio" name="filter" value="channels" v-model="filterType">
          <span class="filter-label">Canales</span>
        </label>
        <label class="filter-option">
          <input type="radio" name="filter" value="contacts" v-model="filterType">
          <span class="filter-label">Contactos</span>
        </label>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'SearchBar',
    data() {
      return {
        searchQuery: '',
        filterType: 'all'
      }
    }
  }
  </script>
  
  <style scoped>
  .search-container {
    padding: 0.8rem;
  }
  
  .search-input-container {
    position: relative;
    margin-bottom: 0.5rem;
  }
  
  .search-input {
    width: 100%;
    padding: 0.5rem 1rem;
    padding-right: 2.5rem;
    background-color: var(--primary-bg);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    color: var(--text-primary);
  }
  
  .search-input::placeholder {
    color: var(--text-secondary);
  }
  
  .search-icon {
    position: absolute;
    right: 0.8rem;
    top: 50%;
    transform: translateY(-50%);
    color: var(--text-secondary);
  }
  
  .filter-options {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
  }
  
  .filter-option {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  
  .filter-label {
    margin-left: 0.3rem;
    font-size: 0.8rem;
    color: var(--text-secondary);
  }
  
  input[type="radio"] {
    accent-color: var(--accent);
  }
  </style>