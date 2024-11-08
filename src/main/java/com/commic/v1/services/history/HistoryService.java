package com.commic.v1.services.history;

import com.commic.v1.entities.History;
import com.commic.v1.repositories.IHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryService implements IHistoryService {
    IHistoryRepository historyRepository;

    @Override
    public void deleteByChapterId(Integer id) {
        List<History> histories = historyRepository.findByChapterId(id);
        histories.forEach(history -> history.setIsDeleted(true));
        historyRepository.saveAll(histories);
    }
}
