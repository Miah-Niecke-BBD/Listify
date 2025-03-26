<script setup>
// No logic needed
</script>

<template>
  <section class="tutorial-section">
    <header class="tutorial-header">
      <h1>Welcome to Your Task Manager</h1>
      <p class="subtitle">Let’s walk you through the features to boost your productivity!</p>
    </header>

    <article class="tutorial-card">
      <h2>1. Creating a Task</h2>
      <p>
        Click the <strong class="highlight">"Add Task"</strong> button to start. 
        Enter a title, optional description, and a deadline if needed.
      </p>
    </article>

    <article class="tutorial-card">
      <h2>2. Managing Tasks</h2>
      <p>
        You can edit, delete, or mark a task as complete anytime. 
        Tap the icons next to each task card to manage them.
      </p>
    </article>

    <article class="tutorial-card">
      <h2>3. Organize by Categories</h2>
      <p>
        Use categories or tags to group tasks by priority, project, or type. 
        This keeps your list clean and focused.
      </p>
    </article>

    <article class="tutorial-card">
      <h2>4. Get Reminders</h2>
      <p>
        Enable notifications to stay on top of your deadlines. 
        You’ll get gentle nudges so you never miss a thing.
      </p>
    </article>

    <footer class="tutorial-footer">
      <p>Need help? Visit our FAQ or contact support.</p>
    </footer>
  </section>
</template>

<style scoped>

.tutorial-section {
  background-color: var(--background-color);
  padding: 2rem;
}

.tutorial-header {
  text-align: center;
  color: var(--heading-purple);
  margin-bottom: 2rem;
}

.tutorial-header h1 {
  font-size: 2rem;
  margin: 0 0 0.5rem 0;
}

.subtitle {
  color: var(--light-text-color);
  font-size: 1rem;
  margin: 0;
}

.tutorial-card {
  background-color: var(--faq-bg);
  box-shadow: 1em 1em 1em 1em black;
  border-radius: 1rem;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.tutorial-card h2 {
  color: var(--dark-purple);
  font-size: 1.25rem;
  margin: 0 0 0.5rem 0;
}

.tutorial-card p {
background-color: red;
  color: red;
  margin: 0;
}

.highlight {
  color: var(--primary-color);
}

.tutorial-footer {
  text-align: center;
  margin-top: 2rem;
}

.tutorial-footer p {
  color: var(--light-text-color);
  font-size: 0.9rem;
  margin: 0;
}
</style>
