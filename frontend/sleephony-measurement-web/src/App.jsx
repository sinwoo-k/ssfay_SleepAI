import { useEffect, useState } from "react";
import "./App.css";
import SelectUser from "./components/SelectUser";
import UserSleepChart from "./components/UserSleepChart";
import axios from "axios";

function App() {
  const [selectUser, SetSelectUser] = useState();
  const [users, setUsers] = useState([]);

  const baseUrl = "https://k12c208.p.ssafy.io/api";

  const getUsers = () => {
    axios
      .get(`${baseUrl}/preview/all`)
      .then((res) => {
        setUsers(res.data.results);
      })
      .catch((err) => {
        console.error(err);
      });
  };

  useEffect(() => {
    getUsers();
  }, []);

  return (
    <div className="app">
      <div className="container">
        {/* 사용자 선택 */}
        <SelectUser users={users} SetSelectUser={SetSelectUser} />
        {/* 수면 측정 */}
        <UserSleepChart selectUser={selectUser} baseUrl={baseUrl} />
      </div>
    </div>
  );
}

export default App;
