package com.example.techMemo.url;

import com.example.techMemo.article.ArticleRepository;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.UrlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlRepository repository;
    private final ArticleRepository articleRepository;
    private final UrlMapper mapper;

    // URL追加（記事作成時に一緒に使う）
    @Transactional
    public void createAll(List<UrlRequest> requests, Article article) {
        requests.forEach(req -> {
            Url url = Url.builder()
                         .article(article)
                         .url(req.url())
                         .title(req.title())
                         .build();
            repository.save(url);
        });
    }

    @Transactional
    public UrlResponse update(Long urlId, UrlRequest request) {
        Url url = getUrlById(urlId);
        url.update(request.url(), request.title());
        return mapper.toResponse(url);
    }

    // URLエンティティのリストを生成するだけ（保存はしない）
    public List<Url> buildUrls(List<UrlRequest> requests, Article article) {
        return requests.stream()
                       .map(req -> Url.builder()
                                      .article(article)
                                      .url(req.url())
                                      .title(req.title())
                                      .build())
                       .toList();
    }

    // URL削除
    @Transactional
    public void delete(Long urlId) {
        Url url = getUrlById(urlId);
        repository.delete(url);
    }

    private Url getUrlById(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResourceNotFoundException("URLが見つかりません"));
    }

}
