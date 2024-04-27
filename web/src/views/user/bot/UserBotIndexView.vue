<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top: 20px;">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" width="100%;">
            <hr>
            <div class="card" style="text-align: center;">
              <div class="card-header">
                <span class="username" >{{$store.state.user.username}}</span>
              </div>
            </div>
            <hr>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top: 20px;">
          <div class="card-header">
            <span style="font-size: 150%;">我的Bot</span>
            <button type="button" class="btn btn-warning float-end" style="font-size: large;">创建Bot</button>
          </div>
          <div class="card-body"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>


export default{
  setup() {
    
  }
}
</script>

<style scoped>
.username {
  color:rgba(186, 194, 33);
  font-size: 150%;
  align-items: center;
}
</style>