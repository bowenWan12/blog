package com.winds.bm.service.impl;

import com.winds.bm.entity.Log;
import com.winds.bm.repository.LogRepository;
import com.winds.bm.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Integer saveLog(Log log) {
        return logRepository.save(log).getId();
    }
}
