import React, { useState } from "react";

const SelectUser = ({ users, SetSelectUser }) => {
  return (
    <div className="user-select">
      <select
        name="user-select"
        id="user-select"
        onChange={(e) => {
          SetSelectUser(e.target.value);
        }}
        defaultValue={0}
      >
        <option value={0} disabled hidden>
          사용자를 선택하세요.
        </option>
        {users.map((v) => (
          <option key={`user-${v.userId}-${v.nickname}`} value={v.userId}>
            {v.nickname}
          </option>
        ))}
      </select>
    </div>
  );
};

export default SelectUser;
