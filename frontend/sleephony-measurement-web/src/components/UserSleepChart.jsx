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

const STAGE_LABELS = {
  0: "AWAKE",
  1: "NREM1",
  2: "NREM2",
  3: "NREM3",
  4: "REM",
};

const UserSleepChart = ({ selectUser }) => {
  const [isMeasured, setIsMeasured] = useState(false);

  const [data, setData] = useState([]);

  const timerRef = useRef(null);

  // selectUser가 변경되면 측정 상태 토글
  useEffect(() => {
    if (selectUser) {
      setIsMeasured(true);
    } else {
      setIsMeasured(false);
      setData([]); // 초기화
    }
  }, [selectUser]);

  // 측정 중이면 1초마다 더미 데이터 추가
  useEffect(() => {
    if (isMeasured) {
      // 초기 데이터
      setData([{ time: new Date().toLocaleTimeString(), stage: 1 }]);
      timerRef.current = setInterval(() => {
        setData((prev) => {
          const next = [
            ...prev,
            {
              time: new Date().toLocaleTimeString(),
              // 0~3 랜덤
              stage: Math.floor(Math.random() * 5),
            },
          ];
          // 최대 20개만 남기기
          return next.slice(-20);
        });
      }, 1000);
    } else {
      clearInterval(timerRef.current);
    }
    // 언마운트 시 타이머 정리
    return () => clearInterval(timerRef.current);
  }, [isMeasured]);

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
              tickFormatter={(v) => STAGE_LABELS[v]}
            />
            <Tooltip
              labelFormatter={(label) => `시간: ${label}`}
              formatter={(value) => STAGE_LABELS[value]}
            />
          </LineChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default UserSleepChart;
