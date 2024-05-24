<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center;">
      <thead>
        <tr>
          <th>玩家</th>
          <th>分数</th>
          <th>段位</th>
          <th>排名</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, i) in users" :key="user.id">
          <td style="text-align: center;">
            <img :src="user.photo" alt="" class="record-user-photo">
            &nbsp;
            <span class="record-user-username">{{ user.username }}</span>
          </td>
          <td>
            {{ user.rating }}
          </td>
          <td>
            {{ user.tierName }}
          </td>
          <td>
            {{ (get_current_page() - 1) * 10 + i + 1 }}
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation">
      <ul class="pagination" style="float: right; margin-right: 30px;">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
          <a class="page-link" href="#">
            {{ page.number }}
          </a>
        </li>
        
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>

import ContentField from '../../components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
import router from '../../router/index'

export default{
  components:{
    ContentField
  },
  setup() {
    const store = useStore();
    let users = ref([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);

    const get_current_page = () => {
      return current_page;
    }


    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_users / 10));

      if (page >= 1 && page <= max_pages) {
          pull_page(page);
      }
    }


    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_users / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; ++i) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    }


    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/ranklist/getlist/",
        data: {
          page,
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp.users;
          total_users = resp.users_count;
          update_pages();
        },
        error(resp) {
          console.log(resp);
        }
      })
    }
    pull_page(current_page);

    
    return {
      users,
      pages,
      click_page,
      get_current_page,
    }
  }
}
</script>

<style scoped>
img.record-user-photo{
  width: 4vh;
  border-radius: 50%;
}
</style>