package com.commic.v1.services.chapter;

import com.commic.v1.dto.responses.ChapterContentResponse;
import com.commic.v1.entities.Chapter;
import com.commic.v1.entities.ChapterContent;
import com.commic.v1.mapper.ChapterMapper;
import com.commic.v1.repositories.IChapterContentRepository;
import com.commic.v1.repositories.IChapterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChapterContentServiceImpl implements IChapterContentService {
    IChapterRepository chapterRepository;
    ChapterMapper chapterMapper;
    IChapterContentRepository contentRepository;

    @Override
    public List<ChapterContentResponse> getChapterContent(Integer id) {
        //Cái này dùng để lấy hình ảnh của chapter đó
        List<ChapterContent> chapters = chapterRepository.findByChapterId(id);
        // Cái này là gọi chapter đó ra lưu thêm view vào
        Chapter chapter = chapterRepository.findById(id).get();
        if(chapter != null) {
            chapter.setView(chapter.getView() + 1);
            chapterRepository.save(chapter);
        }
        if (chapters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No chapters found for id: " + id);
        }
        return chapters.stream()
                .map(chapterMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByChapterId(Integer id) {
        List<ChapterContent> contents = contentRepository.findByChapterId(id);
        contents.forEach(content -> content.setIsDeleted(true));
        contentRepository.saveAllAndFlush(contents);

    }
}
