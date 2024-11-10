package com.commic.v1.services.chapter;

import com.commic.v1.dto.responses.ChapterResponse;
import com.commic.v1.entities.Chapter;
import com.commic.v1.mapper.ChapterMapper;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.comment.ICommentServices;
import com.commic.v1.services.history.IHistoryService;
import com.commic.v1.services.rating.IRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChapterServiceImpl implements IChapterService {
    IChapterRepository chapterRepository;
    IChapterContentService contentService;
    IRatingService ratingService;
    IHistoryService historyService;
    ICommentServices commentServices;
    ChapterMapper chapterMapper;

    @Override
    public void deleteByBookId(Integer id) {
        List<Chapter> chapters = chapterRepository.findByBookId(id, Sort.unsorted());
        for (Chapter chapter : chapters) {
            chapter.setIsDeleted(true);

            commentServices.deleteByChapterId(chapter.getId());

            //rating
            ratingService.deleteByChapterId(chapter.getId());

            contentService.deleteByChapterId(chapter.getId());

            //chapter content
            historyService.deleteByChapterId(chapter.getId());
        }

        chapterRepository.saveAllAndFlush(chapters);
    }

    @Override
    public List<ChapterResponse> getChaptersByBookId(Integer bookId, Sort sort) {
        List<Chapter> list = chapterRepository.findByBookId(bookId, sort);
        return chapterMapper.toChapterDTOs(list);
    }
}
