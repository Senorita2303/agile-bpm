<template>
  <div class="ab-form-design">
    <ab-form-design
      :custom-fields="customFields"
      :options="options"
      style="height: 100vh"
      @submit="handleSubmit"
    />
  </div>
</template>
<script>
  import { createApp } from 'vue'
  export default {
    name: 'App',
    data() {
      return {
        customFields: [
          {
            title: 'Dividing line',
            component: 'el-divider', //ele Dividing line
            span: 24,
            icon: 'el-icon-eleme',
            tips: 'Look at me: How to use custom attributes?',
            labelWidth: '0px',
            params: {
              html: '<h3 style="color:red">Dividing line title</h3>',
              contentPosition: 'left',
            },
          },
          {
            title: 'Tab page',
            component: 'el-tab', //ele Dividing line
            span: 24,
            icon: 'el-icon-eleme',
            tips: '',
            labelWidth: '0px',
            params: {
              html: '<h3 style="color:red">Dividing line title</h3>',
              contentPosition: 'left',
            },
          },
          {
            title: 'Warning',
            component: 'el-alert',
            labelWidth: '0px',
            span: 24,
            icon: 'el-icon-warning',
            tips: 'Look at me: How to use custom events?',
            params: {
              title: 'Warning warning warning warning',
              type: 'success',
            },
            event: {
              close: () => {
                console.log('alert close event')
              },
            },
          },
        ],
        options: `{
        column: [
          {
            label: 'Preview and view the linkage of the fields below',
            span: 24,
            labelPosition: 'top',
            labelWidth: 120
          },
          {
            type: 'switch',
            label: 'Control visibility',
            span: 24,
            display: true,
            value: '0',
            dicData: [
              {
                label: '',
                value: '0'
              },
              {
                label: '',
                value: '1'
              }
            ],
            prop: '1629695558953_19833',
            change: ({ value }) => {
              const input = this.findObject(this.option.column, 'input')
              if (input == -1) return
              if (value == 0) {
                input.display = false
              } else {
                input.display = true
              }
            }
          },
          {
            type: 'input',
            label: 'Single line text',
            span: 24,
            display: true,
            prop: 'input'
          },
          {
            type: 'select',
            label: 'Value changed,
            dicData: [
              {
                label: 'One day',
                value: '1'
              },
              {
                label: 'Two days',
                value: '2'
              },
              {
                label: 'Three days',
                value: '3'
              }
            ],
            cascader: [],
            span: 24,
            display: true,
            props: {
              label: 'label',
              value: 'value'
            },
            prop: '1629695859796_23456',
            change: ({ value }) => {
              let text = value
              if (value) {
                text = 'Leave ' + value + ' days'
              }
              this.form.input2 = value
            }
          },
          {
            type: 'input',
            label: 'Single line text',
            span: 24,
            display: true,
            prop: 'input2'
          }
        ],
      }`,
      }
    },
    methods: {
      handleSubmit(val) {
        this.$message.success('View console')
        console.log(val)
      },
    },
  }
</script>

<style>
  .ab-form-design .el-form-item__content {
    flex-wrap: wrap-reverse !important;
  }
  #app {
    height: 100%;
    min-height: 100%;
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    color: #2c3e50;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
  body {
    margin: 0;
  }
</style>
