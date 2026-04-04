package com.example.techMemo.url;

import com.example.techMemo.article.ArticleRepository;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.UrlMapper;
import com.example.techMemo.user.UserRepository;
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
    private final UserRepository userRepository;
    private final UrlMapper mapper;

    @Transactional
    public UrlResponse create(UrlRequest request) {
        Url url = mapper.toEntity(request);
        Url saved = repository.save(url);

        return mapper.toResponse(saved);
    }

    // 記事更新時にURLを全件入れ替え
    @Transactional
    public void updateAll(List<UrlRequest> requests, Article article) {
        // 既存URL削除はArticle側のorphanRemovalに任せる
        // ArticleService側でarticle.updateUrls()を呼ぶ設計にする
    }

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

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException("記事が見つかりません"));
    }

}
