<script setup lang="ts">
import { ref, computed } from "vue";

const props = defineProps({
  isVisible: {
    type: Boolean,
    required: true,
  },
  onClose: {
    type: Function,
    required: true,
  },
  onAddMember: {
    type: Function,
    required: true,
  },
  allUsersID: {
    type: Array as () => string[],
    required: true,
  },
});

const searchQuery = ref<string>("");
const selectedUserId = ref<string | null>(null);
const errorMessage = ref<string | null>(null);

const closeModal = () => {
  searchQuery.value = "";
  selectedUserId.value = null;
  errorMessage.value = null;
  props.onClose();
};

const filteredUsers = computed(() => {
  errorMessage.value = "";
  return props.allUsersID.filter((user) =>
    user.toLowerCase().includes(searchQuery.value.toLowerCase()),
  );
});

const handleAddMember = async () => {
  if (selectedUserId.value) {
    try {
      await props.onAddMember(selectedUserId.value);
      closeModal();
    } catch (error: any) {
      if (error.message.includes("404")) {
        errorMessage.value = `User id: ${selectedUserId.value} does not exist`;
      } else if (
        error.message.includes("403") &&
        error.message.includes("already a member of team")
      ) {
        errorMessage.value = `User id: ${selectedUserId.value} is already added to the team`;
      } else {
        errorMessage.value = error.message;
      }
    }
  } else {
    errorMessage.value = "Please select a user to add.";
  }
};

const handleUserSelect = (user: string) => {
  selectedUserId.value = user;
  searchQuery.value = user;
};
</script>

<template>
  <section v-if="isVisible" class="modal">
    <article class="modal-content">
      <header class="modal-header">
        <h3>Select a User to Add to Your Team</h3>
      </header>

      <input
        v-model="searchQuery"
        type="text"
        placeholder="Search for a user by email"
        class="modal-input"
      />

      <ul v-if="filteredUsers.length > 0" class="user-list">
        <li
          v-for="user in filteredUsers"
          :key="user"
          :class="{ selected: user === selectedUserId }"
          @click="handleUserSelect(user)"
          class="user-item"
        >
          {{ user }}
        </li>
      </ul>
      <p v-else>No users found</p>

      <p class="error-message">{{ errorMessage }}</p>

      <footer class="modal-footer">
        <button @click="handleAddMember" class="modal-btn" type="submit">Add Member</button>
        <button @click="closeModal" class="modal-btn">Cancel</button>
      </footer>
    </article>
  </section>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2em;
  border-radius: 1em;
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 90%
}

.modal-header {
  margin-bottom: 1em;
}

.modal-input {
  padding: 0.5em;
  margin-bottom: 1em;
  width: 80%;
  font-size: 1em;
  border: 0.1em solid lightgray;
  border-radius: 0.5em;
}

.modal-btn {
  padding: 0.5em 1em;
  margin: 0.5em;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.4em;
  cursor: pointer;
}

.modal-btn:hover {
  opacity: 0.8;
}

.error-message {
  color: red;
  word-wrap: break-word;
  text-align: center;
}

.user-list {
  max-height: 15em;
  overflow-y: auto;
  width: 100%;
  padding: 0;
  list-style-type: none;
}

.user-item {
  padding: 0.5em;
  cursor: pointer;
  border: 0.1em solid lightgray;
  border-radius: 0.5em;
  margin-bottom: 0.5em;
}

.user-item:hover {
  background-color: lightgray;
}

.selected {
  background-color: #d3d3d3;
}

@media (max-width: 700px) {
  .modal-content {
    min-width: unset;
    margin: 1em;
  }
}
</style>
