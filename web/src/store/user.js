import $ from 'jquery'

export default{
  
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    rating: "",
    rank: "",
    is_login: false,
    pulling_info: true,
  },
  getters: {
  },
  mutations: {
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.rating = user.rating;
      state.rank = user.rank;
      state.is_login = user.is_login;
    },
    updateToken(state, token) {
      state.token = token;
    },
    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.token = "";
      state.rating = "";
      state.rank = "";
      state.is_login = false;
      state.pulling_info = false;
    },
    updatePullingInfo(state, pulling_info) {
      state.pulling_info = pulling_info;
    }
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/token/",
        type: "post",
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp) {
          
          if (resp.error_message === "success") {
            localStorage.setItem("token", resp.token);
            context.commit("updateToken", resp.token);
            data.success(resp);
          } else {
            
            data.error(resp);
          }
        },
        error(resp) {
          
          data.error(resp);
        }
      });
    },
    getinfo(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/info/",
        type: "get",
        headers: {
          Authorization: "Bearer " + context.state.token,
        },
        success(resp) {
          if (resp.error_message === "success")
          {
            context.commit("updateUser", {
              ...resp,
              is_login: true,
            })
            data.success(resp);
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        }
      });
    },
    logout(context) {
      localStorage.removeItem("token");
      context.commit("logout");
    }
  },
  modules: {
  }
}