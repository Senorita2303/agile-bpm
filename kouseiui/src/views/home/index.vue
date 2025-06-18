<template>
  <div v-if="pageLoading">
    <el-main>
      <el-card shadow="never" style="margin-top: 15px">
        <el-skeleton></el-skeleton>
      </el-card>
      <el-card shadow="never">
        <el-timeline>
			<el-timeline-item timestamp="2024/7" placement="top">
            <el-card shadow="hover">
              <h1>v2.6</h1><br/>
			  <h3>New features in the process:</h3>
              <p>No Candidate Strategy</p>
			  <p>Is the comment required?</p>
              <p>Circulation method after rejection</p>
              <p>Setting up the executor after rejection</p>
              <p>Configurable conditional branches</p>
              <p>Skip Transfer</p>
              <p>Process intervention: Super management jump</p>
			  <p>Process intervention: Super management restart</p>
              <p>Process intervention: termination of over-control</p>
			  <p>Process Intervention: Assignment</p>
			  <p>Process support import and export</p>
              <p>Many optimizations of process functions</p>

              <h3>New features in the Form Designer:</h3>
			  <p>New auxiliary controls: title component, Title, dividing line, prompt, external Iframe component</p>
              <p>New commonly used controls: rating, switch, etc.</p>
              <p>New advanced controls: signature, administrative area selection, data field controls</p>
			  <p>Added more than a dozen function calculation plug-ins</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2023/7" placement="top">
            <el-card shadow="hover">
              <h1>v2.5</h1><br/>
              <p>
                Personal Office:
                To-do list, CC, Circulation, Processing history, Initiate application, Application history, My draft
              </p>
              <p>Content management: Announcements, News</p>
              <p>
                Organization management: organization management, user management, role management,
                Position management. I have seen countless organizational structures in more than ten years of research and development, and this is the most streamlined and perfect design.
              </p>
              <p>
                Process Management:
                Form design, process design, process instance management, task management, system dialog management. (Currently based on AgileBPM's business components, if you feel it is not suitable, you can switch to other frameworks' process modules, but there is nothing that I like at the moment)
              </p>
              <p>
                System management: dictionary classification management, exception logs, system resources, system properties, common scripts, SMS and email notifications
              </p>
            </el-card>
          </el-timeline-item>
           
        </el-timeline>
      </el-card>
    </el-main>
  </div>
  <work></work>
</template>

<script lang="ts" setup>
  import { defineAsyncComponent, ref } from 'vue'
  const dashboard = ref<string>('0')
  const pageLoading = ref<boolean>(true)
  const work = defineAsyncComponent(() => import('./work/index.vue'))
</script>

<style></style>
