package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.entity.BioData;
import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import com.c208.sleephony.domain.sleep.repositroy.BioRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepLevelRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepLevelService {

    private final SleepStagePredictor predictor;
    private final SleepLevelRepository sleepLevelRepository;
    private final BioRepository bioRepository;

    public List<SleepPredictionResult> predictAndSaveAll(List<BioData> dataList) {
        List<SleepLevel> entities = new ArrayList<>();
        List<SleepPredictionResult> results = new ArrayList<>();

        for (BioData data : dataList) {
            SleepPredictionResult result = predictor.predict(data);

            SleepLevel level = SleepLevel.builder()
                    .userId(data.getUserId())
                    .level(result.getLevel())
                    .score(result.getScore())
                    .measuredAt(data.getMeasuredAt())
                    .createdAt(LocalDateTime.now())
                    .build();

            entities.add(level);
            results.add(result);
        }

        sleepLevelRepository.saveAll(entities);
        return results;
    }

    public List<SleepPredictionResult> predictFromBioDataByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        System.out.println("userId = " + userId);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<BioData> dataList = bioRepository.findByUserIdAndMeasuredAtBetween(userId, start, end);

        return predictAndSaveAll(dataList);
    }

}
