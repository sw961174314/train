import { createStore } from 'vuex'

export default createStore({
  state: {
    member: {}
  },
  getters: {
  },
  mutations: {
    setMember(state, member) {
      // 赋值给state的member
      state.member = member
    },
  },
  actions: {
  },
  modules: {
  }
})
