package com.cinepax.mg.Repository;

import com.cinepax.mg.Model.Content;
import com.cinepax.mg.view.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Integer> {

    @Query(value = "SELECT key,fr_content as content FROM content",nativeQuery = true)
    public List<Language> getContentFrench();

    @Query(value = "SELECT key,ch_content as content FROM content",nativeQuery = true)
    public List<Language> getContentChinois();

    @Query(value = "SELECT key,gr_content as content FROM content",nativeQuery = true)
    public List<Language> getContentAllemand();
}
