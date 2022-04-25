package com.example.restaurant.wishlist.service;

import com.example.restaurant.naver.NaverClient;
import com.example.restaurant.naver.dto.SearchImageReq;
import com.example.restaurant.naver.dto.SearchLocalReq;
import com.example.restaurant.naver.dto.SearchLocalRes;
import com.example.restaurant.wishlist.dto.WishListDto;
import com.example.restaurant.wishlist.entity.WishListEntity;
import com.example.restaurant.wishlist.repository.WishListRepoisotry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final NaverClient naverClient;

    private final WishListRepoisotry wishListRepoisotry;


    public WishListDto search(String query) {

        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        SearchLocalRes searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var localItem = searchLocalRes.getItems().stream().findFirst().get();

            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", " ");

            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);

            var searchImageRes = naverClient.searchImage(searchImageReq);

            if (searchImageRes.getTotal() > 0) {

                var imageItem = searchImageRes.getItems().stream().findFirst().get();

                var result = new WishListDto();
                result.setTitle(imageQuery);
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setRoadAddress(localItem.getRoadAddress());
                result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }
        }
        return new WishListDto();
    }

    public WishListDto add(WishListDto wishListDto) {
        var wishListEntity = dtoToEntity(wishListDto);
        var savedWishListEntity = wishListRepoisotry.save(wishListEntity);
        return entityToDto(savedWishListEntity);
    }

    private WishListEntity dtoToEntity(WishListDto wishListDto) {
        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setTitle(wishListDto.getTitle());
        wishListEntity.setCategory(wishListDto.getCategory());
        wishListEntity.setAddress(wishListDto.getAddress());
        wishListEntity.setRoadAddress(wishListDto.getRoadAddress());
        wishListEntity.setHomePageLink(wishListDto.getHomePageLink());
        wishListEntity.setImageLink(wishListDto.getImageLink());
        wishListEntity.setVisit(wishListDto.isVisit());
        wishListEntity.setVisitCount(wishListDto.getVisitCount());
        wishListEntity.setLastVisitDate(wishListDto.getLastVisitDate());
        return wishListEntity;
    }

    private WishListDto entityToDto(WishListEntity wishListEntity) {

        WishListDto wishListDto = new WishListDto();
        wishListDto.setIndex(wishListEntity.getIndex());
        wishListDto.setTitle(wishListEntity.getTitle());
        wishListDto.setCategory(wishListEntity.getCategory());
        wishListDto.setAddress(wishListEntity.getAddress());
        wishListDto.setRoadAddress(wishListEntity.getRoadAddress());
        wishListDto.setHomePageLink(wishListEntity.getHomePageLink());
        wishListDto.setImageLink(wishListEntity.getImageLink());
        wishListDto.setVisit(wishListEntity.isVisit());
        wishListDto.setVisitCount(wishListEntity.getVisitCount());
        wishListDto.setLastVisitDate(wishListEntity.getLastVisitDate());
        return wishListDto;
    }

    public List<WishListDto> findAll() {
        return wishListRepoisotry.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public void delete(int index) {
        wishListRepoisotry.deleteById(index);
    }

    public void addVisit(int index) {
        var wishItem = wishListRepoisotry.findById(index);
        if (wishItem.isPresent()) {
           var item = wishItem.get();
           item.setVisit(true);
           item.setVisitCount(item.getVisitCount() + 1);
           item.setLastVisitDate(LocalDateTime.now());
        }
    }
}
