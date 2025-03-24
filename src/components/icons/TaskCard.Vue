
<template>
    <article class="task-card" :class="{ 'completed': task.completed }">
      <section class="task-content">
        <button @click="toggleCompletion" class="task-toggle">
          {{ task.completed ? '✓' : '' }}
        </button>
        <section class="task-details">
          <h3 class="task-title" :class="{ 'completed': task.completed }">{{ task.title }}</h3>
          <p v-if="task.description" class="task-description">{{ task.description }}</p>
          <section class="task-meta">
            <span v-if="task.dueDate" class="task-date">{{ formatDate(task.dueDate) }}</span>
            <span v-if="task.priority" class="task-priority" :class="task.priority">{{ task.priority }}</span>
          </section>
        </section>
      </section>
    </article>
  </template>
  
  <script>
  export default {
    name: 'TaskCard',
    props: {
      task: { type: Object, required: true }
    },
    emits: ['toggle-completion'],
    methods: {
      toggleCompletion() {
        this.$emit('toggle-completion', this.task.id)
      },
      formatDate(dateString) {
        return dateString ? new Date(dateString).toLocaleDateString() : ''
      }
    }
  }
  </script>
  
  <style scoped>
  .task-card { border-radius: 8px; border: 1px solid #e5e7eb; padding: 16px; transition: background-color 0.2s; background-color: var(--white-color); }
  .task-card.completed { border-color: #bbf7d0; background-color: var(--card-bg); }
  .task-content { display: flex; align-items: flex-start; gap: 12px; }
  .task-toggle { margin-top: 2px; display: flex; height: 20px; width: 20px; flex-shrink: 0; align-items: center; justify-content: center; border-radius: 9999px; border: 1px solid #e5e7eb; background-color: transparent; font-size: 12px; font-weight: bold; }
  .task-details { display: grid; gap: 4px; }
  .task-title { font-size: 14px; font-weight: 500; color: var(--heading-purple); }
  .task-title.completed { text-decoration: line-through; color: var(--light-text-color); }
  .task-description { font-size: 12px; color: var(--light-text-color); display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; max-height: 3em; }
  .task-meta { display: flex; align-items: center; gap: 8px; margin-top: 4px; }
  .task-date { display: inline-flex; align-items: center; border-radius: 9999px; border: 1px solid #e5e7eb; padding: 2px 8px; font-size: 12px; font-weight: 600; }
  .task-priority { display: inline-flex; align-items: center; border-radius: 9999px; padding: 2px 8px; font-size: 12px; font-weight: 600; }
  .task-priority.high { background-color: #fee2e2; color: #b91c1c; }
  .task-priority.medium { background-color: var(--faq-bg); color: var(--dark-purple); }
  .task-priority.low { background-color: #dbeafe; color: var(--primary-color); }
  </style>