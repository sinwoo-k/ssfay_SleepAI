import React, { useState } from "react";

const SelectUser = ({ selectUser, SetSelectUser }) => {
  return (
    <div className="user-select">
      <select
        name="user-select"
        id="user-select"
        onChange={(e) => {
          SetSelectUser(e.target.value);
          console.log(e.target.value);
        }}
      >
        <option value="사용자1">사용자1</option>
        <option value="사용자2">사용자2</option>
        <option value="사용자3">사용자3</option>
      </select>
    </div>
  );
};

export default SelectUser;
