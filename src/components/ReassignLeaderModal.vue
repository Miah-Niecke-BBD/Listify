<script setup lang="ts">
import "@/assets/base.css";
import { defineProps, ref } from "vue";
import type { PropType } from "vue";
import type { TeamMember } from "@/models/TeamMember";

const props = defineProps({
  teamName: String,
  teamMembers: {
    type: Array as PropType<TeamMember[]>,
    required: true,
  },
  isVisible: {
    type: Boolean,
    required: true,
  },
  onClose: {
    type: Function as PropType<() => void>,
    required: true,
  },
  changeTeamLeader: {
    type: Function,
    required: true,
  },
});

const selectedLeader = ref<string | null>(null);

const closeModal = () => {
  props.onClose();
  selectedLeader.value = null;
};

const updateTeamLeader = () => {
  props.changeTeamLeader(selectedLeader.value?.trim());
  window.location.reload();
};
</script>

<template>
  <section v-if="isVisible" class="reassign-leader-modal">
    <article class="modal-content">
      <h3 class="modal-header">Reassign Team Leader</h3>
      <p>Select a new team leader for the "{{ teamName }}" project</p>
      <ul class="team-members-list">
        <li v-for="member in teamMembers" :key="member.githubID" class="team-member-item">
          <input
            v-if="!member.teamLeader"
            type="radio"
            :value="member.githubID"
            v-model="selectedLeader"
            :id="'member-' + member.githubID"
          />
          <svg
            v-if="member.teamLeader"
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="#d4af37"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M3 10l4.5 4L12 4l4.5 10 4.5-4v10H3V10z" />
            <path d="M2 20h20" />
          </svg>

          <section class="member-details">
            <p class="member-name">{{ member.githubID }}</p>
          </section>
        </li>
      </ul>
      <footer class="modal-footer">
        <button @click="closeModal" class="modal-btn">Cancel</button>
        <button class="modal-btn" @click="updateTeamLeader">OK</button>
      </footer>
    </article>
  </section>
</template>

<style scoped>
.reassign-leader-modal {
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

.modal-header {
  margin-bottom: 1em;
}

.team-members-list {
  display: flex;
  flex-direction: column;
  list-style: none;
  padding: 0;
  margin: 1em;
  width: 100%;
  overflow-y: auto;
  max-height: 60vh;
}

.team-member-item {
  display: flex;
  gap: 1em;
  padding: 0.5em;
  border-bottom: 0.1em solid lightgrey;
  margin-bottom: 1em;
}

.member-details {
  display: flex;
  flex-direction: column;
}

.member-name {
  font-weight: bold;
}

.member-id {
  font-size: 10pt;
  color: var(--dark-purple);
}

.leader-label {
  font-weight: bold;
  color: #d4af37;
  margin-top: 0.5em;
}

.reassign-leader-modal input[type="radio"] {
  accent-color: var(--primary-color);
}
@media (max-width: 700px) {
  .modal-content {
    min-width: unset;
    margin: 1em;
  }
}
</style>
