<template>
    <div class="form-group">
      <label v-if="label" :for="id">{{ label }}</label>
      <div class="input-wrapper">
        <span v-if="icon" class="input-icon">{{ icon }}</span>
        <input
          :id="id"
          :type="type"
          :value="modelValue"
          @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
          :placeholder="placeholder"
          :required="required"
          :autocomplete="autocomplete"
          :class="{ 'with-icon': icon, 'with-action': hasSlot }"
        />
        <slot></slot>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { computed, useSlots } from 'vue'
  
  const props = defineProps({
    id: {
      type: String,
      required: true
    },
    modelValue: {
      type: String,
      default: ''
    },
    label: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'text'
    },
    placeholder: {
      type: String,
      default: ''
    },
    icon: {
      type: String,
      default: ''
    },
    required: {
      type: Boolean,
      default: false
    },
    autocomplete: {
      type: String,
      default: ''
    }
  })
  
  const emit = defineEmits(['update:modelValue'])
  
  const hasSlot = computed(() => !!slots.default)
  const slots = useSlots()
  </script>
  
  <style scoped>
  @import '../../assets/styles/colors.css';

  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  label {
    color: var(--text-primary);
    font-size: 0.9rem;
    font-weight: 500;
  }
  
  .input-wrapper {
    position: relative;
    display: flex;
    align-items: center;
  }
  
  .input-icon {
    position: absolute;
    left: 12px;
    font-size: 0.9rem;
    opacity: 0.7;
  }
  
  input {
    width: 100%;
    padding: 0.9rem;
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 10px;
    background-color: #313338;
    color: var(--text-primary);
    transition: all 0.3s ease;
    font-size: 0.95rem;
  }
  
  input.with-icon {
    padding-left: 2.5rem;
  }
  
  input.with-action {
    padding-right: 2.5rem;
  }
  
  input:focus {
    outline: none;
    border-color: var(--accent);
    box-shadow: 0 0 0 2px rgba(88, 101, 242, 0.2);
  }
  
  input::placeholder {
    color: var(--text-secondary);
    opacity: 0.7;
  }
  </style>