import { ref } from "vue";
import { User } from "../../auth/types/interfaces";

const contacts = ref([]);

export function useContacts() {
  const setContacts = (contactsData: any) => {
    contacts.value = contactsData;
  };

  const clearContacts = () => {
    contacts.value = [];
  };

  return {
    contacts,
    setContacts,
    clearContacts,
  };
}
