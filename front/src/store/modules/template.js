import api from '@/api/template';

const user = {
  state: {
    editTemplate: null
  },

  mutations: {
    SET_EDIT_TEMPLATE: (state, editTemplate) => {
      state.editTemplate = editTemplate;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_STRUCTURE: (state, structure) => {
      state.templateStructure = structure;
    }
  },

  actions: {
    // 更新 Structure
    updateStructure({ commit, structure }) {
      return new Promise(resolve => {
        commit('SET_STRUCTURE', structure);
        resolve();
      });
    },
    fetchEditTemplate({ commit, id }) {
      return new Promise(resolve => {
        api.get(id).then(response => {
          commit('SET_EDIT_TEMPLATE', response.data);
          resolve();
        })
      });
    }
  }
};

export default user;
