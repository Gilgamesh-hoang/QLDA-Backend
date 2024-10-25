package com.commic.v1.services.chapter;

import com.commic.v1.entities.Chapter;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.services.comment.ICommentServices;
import com.commic.v1.services.history.IHistoryService;
import com.commic.v1.services.rating.IRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
}
