<template>
    <button
      :type="type"
      class="ui-button"
      :class="[variant, size, { loading }]"
      :disabled="disabled || loading"
      @click="$emit('click', $event)"
    >
      <span v-if="loading" class="loader"></span>
      <span v-else class="button-content">
        <slot></slot>
      </span>
    </button>
  </template>
  
  <script setup lang="ts">
  defineProps({
    type: {
      type: String as () => 'button' | 'reset' | 'submit',
      default: 'button'
    },
    variant: {
      type: String,
      default: 'primary',
      validator: (value: string) => ['primary', 'secondary', 'text', 'success', 'error', 'warning'].includes(value)
    },
    size: {
      type: String,
      default: 'medium',
      validator: (value: string) => ['small', 'medium', 'large'].includes(value)
    },
    disabled: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    }
  })
  
  defineEmits(['click'])
  </script>
  
  <style scoped>
  @import '../../assets/styles/colors.css';
  
  .ui-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border: none;
    border-radius: 10px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
  }
  
  /* Variants */
  .ui-button.primary {
    background-color: var(--accent);
    color: white;
  }
  
  .ui-button.primary:hover:not(:disabled) {
    background-color: #4752c4;
    transform: translateY(-2px);
  }
  
  .ui-button.secondary {
    background-color: #313338;
    color: var(--text-primary);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .ui-button.secondary:hover:not(:disabled) {
    background-color: #3a3c42;
    transform: translateY(-2px);
  }
  
  .ui-button.text {
    background-color: transparent;
    color: var(--accent);
    padding: 0.5rem;
  }
  
  .ui-button.text:hover:not(:disabled) {
    background-color: rgba(88, 101, 242, 0.1);
  }
  
  .ui-button.success {
    background-color: var(--success);
    color: white;
  }
  
  .ui-button.error {
    background-color: var(--error);
    color: white;
  }
  
  .ui-button.warning {
    background-color: var(--warning);
    color: white;
  }
  
  /* Sizes */
  .ui-button.small {
    padding: 0.5rem 1rem;
    font-size: 0.85rem;
  }
  
  .ui-button.medium {
    padding: 0.9rem 1.5rem;
    font-size: 1rem;
  }
  
  .ui-button.large {
    padding: 1.1rem 2rem;
    font-size: 1.1rem;
  }
  
  /* States */
  .ui-button:active:not(:disabled) {
    transform: translateY(0);
  }
  
  .ui-button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  .ui-button.loading {
    cursor: wait;
  }
  
  .loader {
    display: inline-block;
    width: 20px;
    height: 20px;
    border: 3px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top-color: white;
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }
  
  @media (prefers-reduced-motion) {
    .ui-button:hover:not(:disabled) {
      transform: none;
    }
  }
  </style>
  