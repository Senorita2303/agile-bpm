<template>
  <div class="router-err">
    <div class="router-err__icon">
      <img src="/img/404.png" />
    </div>
    <div class="router-err__content">
      <h2>No permission or page not found</h2>
      <p>
        The current page has no permission to access or a non-existent link has been opened. Please check the current account permissions and link accessibility.
      </p>
      <el-button type="primary" plain round @click="gohome">Return to home page</el-button>
      <el-button type="primary" plain round @click="gologin">
        Re-login
      </el-button>
      <el-button type="primary" round @click="goback">Return to previous page</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    methods: {
      gohome() {
        location.href = '#/'
      },
      goback() {
        this.$router.go(-1)
      },
      gologin() {
        this.$router.push('/login')
      },
    },
  }
</script>

<style scoped>
  .router-err {
    display: flex;
    width: 900px;
    margin: 50px auto;
    align-items: center;
  }
  .router-err__icon {
    width: 400px;
  }
  .router-err__icon img {
    width: 100%;
  }
  .router-err__content {
    flex: 1;
    padding: 40px;
  }
  .router-err__content h2 {
    font-size: 26px;
  }
  .router-err__content p {
    font-size: 14px;
    color: #999;
    margin: 15px 0 30px 0;
    line-height: 1.5;
  }

  @media (max-width: 992px) {
    .router-err {
      display: block;
      width: 100%;
      margin-top: 10px;
      text-align: center;
    }
    .router-err__icon {
      width: 280px;
      margin: 0 auto;
    }
  }
</style>
