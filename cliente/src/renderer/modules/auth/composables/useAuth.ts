import { ref, readonly } from 'vue';
import type { User } from '../types/interfaces';

const user = ref<User | null>(null);
const isLogging = ref(false);

export function useAuthUser() {
  const setUser = (userData: User) => {
    user.value = userData;
  };

  const clearUser = () => {
    user.value = null;
  };

  const login = () => {
    isLogging.value = true;
  };

  const logout = () => {
    isLogging.value = false;
    clearUser();
  };

  return {
    user,
    isLogging: readonly(isLogging),
    setUser,
    clearUser,
    login,
    logout
  };
}
