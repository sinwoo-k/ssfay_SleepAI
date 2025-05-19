import { useState } from "react";
import "./App.css";
import SelectUser from "./components/SelectUser";
import UserSleepChart from "./components/UserSleepChart";

function App() {
  const [selectUser, SetSelectUser] = useState();

  return (
    <div className="app">
      <div className="container">
        {/* 사용자 선택 */}
        <SelectUser selectUser={selectUser} SetSelectUser={SetSelectUser} />
        {/* 수면 측정 */}
        <UserSleepChart selectUser={selectUser} />
      </div>
    </div>
  );
}

export default App;
