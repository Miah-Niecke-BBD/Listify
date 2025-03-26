<script setup lang="ts">
import "@/assets/base.css";
import { ref, defineProps, defineEmits } from "vue";
import type { PropType } from "vue";
import type { TeamMember } from "@/models/TeamMember";

const props = defineProps({
  projectName: String,
  projectMembers: {
    type: Array as PropType<TeamMember[]>,
    required: true,
  },
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
});

const emit = defineEmits(["assignMember", "unassignMember"]);

const isMemberInProject = (memberId: number): boolean => {
  return props.projectMembers.some((member) => member.id === memberId);
};

const onMemberToggle = (memberId: number, isChecked: boolean) => {
  if (isChecked) {
    emit("assignMember", memberId);
  } else {
    emit("unassignMember", memberId);
  }
};

const closeModal = () => {
  props.onClose();
};
</script>

<template>
  <section v-if="isVisible" class="project-members-modal">
    <article class="modal-content">
      <h3 class="modal-header">Manage Project Members</h3>
      <p>Select team members to assign to the "{{ projectName }}" project</p>
      <ul class="project-members-list">
        <ol v-for="member in teamMembers" :key="member.id" class="member-item">
          <input
            v-if="!member.isLeader"
            type="checkbox"
            :checked="isMemberInProject(member.id)"
            @change="
              ($event) => onMemberToggle(member.id, ($event.target as HTMLInputElement).checked)
            "
            :id="'member-' + member.id"
          />

          <section class="member-details">
            <p class="member-name">{{ member.name }}</p>
            <p class="member-id">ID: {{ member.id }}</p>
          </section>
        </ol>
      </ul>
      <footer class="modal-footer">
        <button @click="closeModal" class="modal-btn">Cancel</button>
        <button class="modal-btn">OK</button>
      </footer>
    </article>
  </section>
</template>

<style scoped>
.project-members-modal {
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
  min-width: 30em;
}

.project-members-list {
  display: flex;
  flex-direction: column;
  list-style: none;
  padding: 0;
  margin: 1em;
  width: 100%;
  overflow-y: auto;
  max-height: 60vh;
}

.member-item {
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
.project-members-modal input[type="checkbox"] {
  accent-color: var(--primary-color);
}
</style>
