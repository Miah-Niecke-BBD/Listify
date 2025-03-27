<script setup lang="ts">
const emit = defineEmits(["click"]);
</script>

<template>
  <button class="add-btn" @click="$emit('click')" aria-label="Add">
    <slot>+</slot>
  </button>
</template>

<style scoped>
.add-btn {
  margin: 0.2em;
  width: 1.5em;
  height: 1.5em;
  border-radius: 50%;
  background-color: var(--primary-color);
  color: white;
  font-size: 25pt;
  font-weight: bold;
  border: none;
  cursor: pointer;
  display: flex;
  justify-content: center;
  box-shadow: 0.2em 0.1em 0.7em rgba(0, 0, 0, 0.2);
}

.add-btn:hover {
  opacity: 0.8;
}
</style>
