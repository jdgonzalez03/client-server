import { ref, provide, inject } from 'vue';

const ToastSymbol = Symbol('toast-service');

export function provideToast() {
  const toastRef = ref(null);
  
  const showToast = ({ title, message, type }) => {
    if (toastRef.value) {
      toastRef.value.addToast({ title, message, type });
    }
  };
  
  const setToastRef = (ref) => {
    toastRef.value = ref;
  };

  const toast = {
    showMessage: (title, message) => showToast({ title, message, type: 'message' }),
    showInfo: (title, message) => showToast({ title, message, type: 'info' }),
    showSuccess: (title, message) => showToast({ title, message, type: 'success' }),
    showWarning: (title, message) => showToast({ title, message, type: 'warning' }),
    showError: (title, message) => showToast({ title, message, type: 'error' }),
    setToastRef
  };

  provide(ToastSymbol, toast);
  
  return toast;
}

export function useToast() {
  const toast = inject(ToastSymbol);
  
  if (!toast) {
    throw new Error('Toast service has not been provided');
  }
  
  return toast;
}