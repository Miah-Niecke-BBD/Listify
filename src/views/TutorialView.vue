<script setup>
import Sidebar from '@/components/Sidebar.vue';


</script>

<template>
  <section id="body">
  <Sidebar />
  <section class="tutorial-container">
    
    <header class="tutorial-header">
      <h1>Welcome to Listify</h1>
      <p>Let’s walk you through the features to boost your productivity!</p>
    </header>

      <ol class="tutorial-list">
    <li class="card-list">
      <h2>1. Create a Team</h2>
      <p>
        Start by setting up a team for your workspace. This helps you manage tasks, projects, and collaboration in one central place.
      </p>
    </li>

    <li class="card-list">
      <h2>2. Invite Team Members</h2>
      <p>
        Add people to your team by sending invitations. Once they join, they’ll have access to the team’s projects and tasks.
      </p>
    </li>

    <li class="card-list">
      <h2>3. Create Projects Within a Team</h2>
      <p>
        Organize your team’s work by creating separate projects. Each project can represent a different goal, client, or workflow.
      </p>
    </li>

    <li class="card-list">
      <h2>4. Assign Members to Projects</h2>
      <p>
        You can assign specific team members to each project, making sure everyone sees only what’s relevant to their role.
      </p>
    </li>

    <li class="card-list">
      <h2>5. Use the Team Calendar</h2>
      <p>
        The calendar view shows all the tasks you're assigned to across the team. Filter by project or view everything in one place.
      </p>
    </li>

    <li class="card-list">
      <h2>6. Organize Tasks into Sections</h2>
      <p>
        Break down each project into sections (like “To Do”, “In Progress”, “Completed”) to keep tasks structured and easy to manage.
      </p>
    </li>

    <li class="card-list">
      <h2>7. Add Detailed Tasks</h2>
      <p>
        Inside each section, create tasks with a name, description, priority level, due date, and assigned project members to keep everything clear and actionable.
      </p>
    </li>
    </ol>


    <footer class="tutorial-footer">
      <p>Need help? Visit our FAQ or contact support.</p>
    </footer>
  </section>
</section>
</template>

<style scoped>
#body{
 display: flex;
 justify-content: left;
}
.tutorial-container {
  background: var(--background-color);
  padding: 3rem 1.5rem;
  font-family: 'Segoe UI', sans-serif;
}

.tutorial-header {
  text-align: center;
  margin-bottom: 3rem;
}

.tutorial-header h1 {
  color: var(--heading-purple);
  font-size: 2.2rem;
  margin-bottom: 0.5rem;
}

.tutorial-header p {
  color: var(--light-text-color);
  font-size: 1.05rem;
}

.tutorial-list {
  list-style: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.tutorial-list li {
  background: var(--faq-bg);
  padding: 1.5rem 2rem;
  border-radius: 1rem;
  box-shadow: 0em .4em 1em var(--card-bg );
  transition: transform 0.2s ease;
}

.tutorial-list h2 {
  color: var(--dark-purple);
  margin-bottom: 0.75rem;
  font-size: 1.2rem;
  background: var(--faq-bg);  
}

.tutorial-list p {
  color: var(--light-text-color);
  font-size: 1rem;
  line-height: 1.5;
  background: var(--faq-bg);
}

.tutorial-footer {
  text-align: center;
  margin-top: 3rem;
  font-size: 0.9rem;
  color: var(--light-text-color);
}
</style>
