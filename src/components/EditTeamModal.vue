<script setup lang="ts">
import { ref } from "vue";

const props = defineProps({
  isVisible: {
    type: Boolean,
    required: true,
  },
  mode: {
    type: String,
    required: true, // "addMember" or "updateTeamName"
  },
  onClose: {
    type: Function,
    required: true,
  },
  onAddMember: {
    type: Function,
    required: false,
  },
  onUpdateTeamName: {
    type: Function,
    required: false,
  },
});

const newMemberId = ref<number | null>(null);
const newTeamName = ref<string>("");

const closeModal = () => {
  newMemberId.value = null;
  newTeamName.value = "";
  props.onClose();
};

const handleAction = () => {
  if (props.mode === "updateTeamName" && props.onUpdateTeamName) {
    props.onUpdateTeamName(newTeamName.value);
    closeModal();
  } else if (props.mode === "addMember" && props.onAddMember) {
    props.onAddMember(newMemberId.value);
    closeModal();
  }
};
</script>

<template>
  <section v-if="isVisible" class="modal">
    <article class="modal-content">
      <header class="modal-header">
        <h3 v-if="mode === 'addMember'">Enter User ID to Add to Your Team</h3>
        <h3 v-else>Update Team Name</h3>
      </header>

      <input
        v-if="mode === 'addMember'"
        v-model="newMemberId"
        type="text"
        placeholder="Enter User ID"
        class="modal-input"
        required
      />
      <input
        v-else
        v-model="newTeamName"
        type="text"
        placeholder="New Team Name"
        maxlength="100"
        class="modal-input"
        required
      />

      <footer class="modal-footer">
        <button @click="handleAction" class="modal-btn" type="submit">
          {{ mode === "addMember" ? "Add Member" : "Update Name" }}
        </button>
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
  min-width: 30em;
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

@media (max-width: 700px) {
  .modal-content {
    min-width: unset;
    margin: 1em;
  }
}
</style>
