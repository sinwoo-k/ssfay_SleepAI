package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.BioDataResponseDto;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.global.utils.AuthUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sleep")
@AllArgsConstructor
@Tag(name = "Sleep", description = "수면 관련 CRUD")
public class SleepController {

    private final SleepService sleepService;

    @PostMapping("bio-data")
    public ResponseEntity<?> saveDioData(@RequestBody BioDataRequestDto requestDto) {
        Integer userId = AuthUtil.getLoginUserId();
        sleepService.saveAll(requestDto,userId);
        // TODO: AI 서버에 수면 레벨 받아와서 저장하고 값 보내는거
        BioDataResponseDto response = new BioDataResponseDto(
                HttpStatus.OK.value(),
                "SU",
                "OK",
                "NREM1"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("start-measurement")
    public ResponseEntity<?> startMeasurement(@RequestBody StartMeasurementRequestDto requestDto) {
        try {
            Integer userId = AuthUtil.getLoginUserId();
            sleepService.startMeasurement(userId,requestDto.getStartedAt());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
