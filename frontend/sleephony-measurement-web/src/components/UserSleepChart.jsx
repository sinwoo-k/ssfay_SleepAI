import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import {
  ResponsiveContainer,
  LineChart,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Line,
} from "recharts";

const LEVEL_MAP = { REM: 0, NREM3: 1, NREM2: 2, NREM1: 3, AWAKE: 4 };
const REVERSE_LEVEL_MAP = ["REM", "NREM3", "NREM2", "NREM1", "AWAKE"];

const UserSleepChart = ({ selectUser, baseUrl }) => {
  const [isMeasured, setIsMeasured] = useState(false);

  const [data, setData] = useState([]);

  const timerRef = useRef(null);

  const getIsMeasuredUser = () => {
    axios
      .get(`${baseUrl}/preview/status/${selectUser}`)
      .then((res) => {
        setIsMeasured(res.data.results);
      })
      .catch((err) => {
        console.error(err);
      });
  };

  const getMeasuredData = () => {
    axios
      .get(`${baseUrl}/preview/sleep-levels/${selectUser}`)
      .then((res) => {
        console.log(res);
        const chartData = res.data.results.map((item) => ({
          time: new Date(item.time).toLocaleTimeString(),
          stage: LEVEL_MAP[item.level] ?? 0,
          rawLevel: item.level,
        }));
        setData(chartData);
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // selectUser가 변경되면 측정 상태 토글
  useEffect(() => {
    if (selectUser) {
      getIsMeasuredUser();
    }
  }, [selectUser]);

  // 측정 중이면 1초마다 더미 데이터 추가
  useEffect(() => {
    if (isMeasured) {
      getMeasuredData();
      timerRef.current = setInterval(() => {
        getMeasuredData();
      }, 150000);
    } else {
      clearInterval(timerRef.current);
    }
    // 언마운트 시 타이머 정리
    return () => clearInterval(timerRef.current);
  }, [isMeasured, selectUser]);

  return (
    <div className="chart-container">
      {!isMeasured ? (
        <p>측정 중인 사용자가 아닙니다.</p>
      ) : (
        <ResponsiveContainer width={"100%"} height={"100%"}>
          <LineChart data={data}>
            <Line
              type="monotone"
              dataKey="stage"
              stroke="#8884d8"
              strokeWidth={5}
              dot={false}
              isAnimationActive={false}
            />
            <CartesianGrid stroke="#ccc" strokeDasharray={"5 5"} />
            <XAxis dataKey="time" tick={{ fontSize: 12 }} />
            <YAxis
              domain={[0, 4]}
              tick={[0, 1, 2, 3, 4]}
              tickFormatter={(v) => REVERSE_LEVEL_MAP[v]}
            />
            <Tooltip
              labelFormatter={(label) => `시간: ${label}`}
              formatter={(value) => REVERSE_LEVEL_MAP[value]}
            />
          </LineChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default UserSleepChart;
