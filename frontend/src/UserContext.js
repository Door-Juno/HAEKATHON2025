import { createContext } from 'react';

const UserContext = createContext({
  username: '',
  userId: null, // 또는 0
});

export default UserContext;
