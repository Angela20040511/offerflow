package com.offerflow.mapper;

import com.offerflow.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FavoriteMapper {
    int insertFavorite(Favorite favorite);
    int deleteFavorite(@Param("userId") Long userId, @Param("jobId") Long jobId);
    long existsFavorite(@Param("userId") Long userId, @Param("jobId") Long jobId);
    long countMyFavoritePage(Map<String, Object> params);
    List<Map<String, Object>> selectMyFavoritePage(Map<String, Object> params);
}
