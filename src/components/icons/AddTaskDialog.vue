
<template>
    <section v-if="isOpen" class="dialog-overlay">
      <article class="dialog-container task-dialog">
        <header class="dialog-header">
          <h2 class="dialog-title">+ Add New Task</h2>
          <p class="dialog-subtitle">Create a new task with details. Click save when you're done.</p>
        </header>
        <form class="dialog-form" @submit.prevent="saveTask">
          <section class="form-group">
            <label for="taskTitle" class="form-label">Task Title</label>
            <input v-model="task.title" id="taskTitle" type="text" placeholder="Enter task title" required class="form-input">
          </section>
          <section class="form-group">
            <label for="taskDescription" class="form-label">Description</label>
            <textarea v-model="task.description" id="taskDescription" placeholder="Add details about this task" class="form-textarea"></textarea>
          </section>
          <section class="form-row">
            <section class="form-group">
              <label for="taskDueDate" class="form-label">Due Date</label>
              <input v-model="task.dueDate" id="taskDueDate" type="date" class="form-input">
            </section>
            <section class="form-group">
              <label for="taskPriority" class="form-label">Priority</label>
              <select v-model="task.priority" id="taskPriority" class="form-select">
                <option value="low">Low</option>
                <option value="medium">Medium</option>
                <option value="high">High</option>
              </select>
            </section>
          </section>
          <section class="form-group">
            <label for="taskProject" class="form-label">Project</label>
            <select v-model="task.project" id="taskProject" class="form-select">
              <option value="none">No Project</option>
              <option v-for="project in projects" :key="project.id" :value="project.id">{{ project.name }}</option>
            </select>
            <p class="form-help">Assign this task to a project (optional)</p>
          </section>
          <footer class="dialog-footer">
            <button @click="close" class="cancel-btn">Cancel</button>
            <button type="submit" class="save-btn">Add Task</button>
          </footer>
        </form>
      </article>
    </section>
  </template>
  
  <script>
  import { ref } from 'vue'
  
  export default {
    name: 'AddTaskDialog',
    props: {
      isOpen: Boolean,
      projects: Array
    },
    emits: ['close', 'save-task'],
    setup() {
      const task = ref({ title: '', description: '', dueDate: '', priority: 'medium', project: 'none' })
      return { task }
    },
    methods: {
      close() { this.$emit('close') },
      saveTask() {
        if (this.task.title.trim()) {
          this.$emit('save-task', { ...this.task })
          this.close()
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .dialog-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.5); z-index: 1000; display: flex; align-items: center; justify-content: center; animation: fadeIn 0.3s ease; }
  .dialog-container { background-color: var(--white-color); border-radius: 12px; width: 100%; max-width: 500px; max-height: 90vh; overflow-y: auto; box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.05); animation: slideIn 0.3s ease; }
  .dialog-header { padding: 24px 24px 0; margin-bottom: 16px; }
  .dialog-title { font-size: 18px; font-weight: 600; margin-bottom: 8px; color: var(--heading-purple); }
  .dialog-subtitle { color: var(--light-text-color); font-size: 14px; }
  .dialog-form { padding: 0 24px; display: flex; flex-direction: column; gap: 16px; }
  .form-group { display: flex; flex-direction: column; }
  .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
  .form-label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 8px; color: var(--heading-purple); }
  .form-input, .form-select, .form-textarea { width: 100%; padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 6px; font-size: 14px; }
  .form-textarea { resize: none; min-height: 80px; }
  .form-help { color: var(--light-text-color); font-size: 12px; margin-top: 4px; }
  .dialog-footer { display: flex; justify-content: flex-end; padding: 16px 24px; border-top: 1px solid #e5e7eb; margin-top: 24px; }
  .cancel-btn { margin-right: 8px; padding: 8px 16px; background-color: white; border: 1px solid #e5e7eb; border-radius: 6px; font-size: 14px; font-weight: 500; transition: background-color 0.2s ease; }
  .cancel-btn:hover { background-color: var(--button-hover-bg); }
  .save-btn { padding: 8px 16px; background-color: var(--primary-color); color: white; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; transition: background-color 0.2s ease; }
  .save-btn:hover { background-color: var(--dark-purple); }
  @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
  @keyframes slideIn { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
  @media (max-width: 768px) { .form-row { grid-template-columns: 1fr; } }
  </style>