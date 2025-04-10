<template>
    <label class="checkbox-container">
      <input 
        type="checkbox"
        :checked="modelValue"
        @change="(e) => $emit('update:modelValue', (e.target as HTMLInputElement).checked)" />
      >
      <span class="checkmark"></span>
      <span><slot></slot></span>
    </label>
  </template>
  
  <script setup lang="ts">
  defineProps({
    modelValue: {
      type: Boolean,
      default: false
    }
  })
  
  defineEmits(['update:modelValue'])
  </script>
  
  <style scoped>
  @import '../../assets/styles/colors.css';

  .checkbox-container {
    display: flex;
    align-items: center;
    cursor: pointer;
    font-size: 0.9rem;
    color: var(--text-secondary);
    user-select: none;
  }
  
  .checkbox-container input {
    position: absolute;
    opacity: 0;
    width: auto;
  }
  
  .checkmark {
    height: 18px;
    width: 18px;
    background-color: #313338;
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    margin-right: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s ease;
  }
  
  .checkbox-container:hover .checkmark {
    border-color: var(--accent);
  }
  
  .checkbox-container input:checked ~ .checkmark {
    background-color: var(--accent);
    border-color: var(--accent);
  }
  
  .checkmark:after {
    content: "✓";
    color: white;
    display: none;
    font-size: 0.75rem;
  }
  
  .checkbox-container input:checked ~ .checkmark:after {
    display: block;
  }
  </style>