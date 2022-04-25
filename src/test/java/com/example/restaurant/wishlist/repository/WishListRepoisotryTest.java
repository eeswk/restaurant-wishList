package com.example.restaurant.wishlist.repository;

import com.example.restaurant.wishlist.entity.WishListEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WishListRepoisotryTest {

    @Autowired
    private WishListRepoisotry wishListRepoisotry;


    private WishListEntity create () {
        var wishList = new WishListEntity();
        wishList.setTitle("title");
        wishList.setCategory("category");
        wishList.setAddress("address");
        wishList.setRoadAddress("roadAddress");
        wishList.setHomePageLink("");
        wishList.setImageLink("");
        wishList.setVisit(false);
        wishList.setVisitCount(0);
        wishList.setLastVisitDate(null);
        return wishList;
    }

    @Test
    public void saveTest() {
        var wishList = create();
        var expected = wishListRepoisotry.save(wishList);

        Assertions.assertNotNull(expected);
        Assertions.assertEquals(1, expected.getIndex());
        Assertions.assertEquals(expected.getTitle(), wishList.getTitle());
    }


    @Test
    public void updateTest() {
        var wishList = create();
        var expected = wishListRepoisotry.save(wishList);

        expected.setTitle("title2");
        wishListRepoisotry.save(expected);

        Assertions.assertNotNull(expected);
        Assertions.assertEquals(1, expected.getIndex());
        Assertions.assertEquals("title2", wishList.getTitle());
        Assertions.assertEquals(1, wishListRepoisotry.findAll().size());
    }

    @Test
    public void findByIdTest() {
        var wishListEntity = create();
        var savedWishListEntity = wishListRepoisotry.save(wishListEntity);

        var expected = wishListRepoisotry.findById(1);

        Assertions.assertEquals(true, expected.isPresent());
        Assertions.assertEquals(1, expected.get().getIndex());
    }

    @Test
    public void deleteTest() {
        var wishListEntity = create();
        var savedWishListEntity = wishListRepoisotry.save(wishListEntity);

        wishListRepoisotry.deleteById(1);

        int count = wishListRepoisotry.findAll().size();

        Assertions.assertEquals(0, count);

    }

    @Test
    public void listAllTest() {
        var wishListEntity = create();
        wishListRepoisotry.save(wishListEntity);

        var wishListEntity2 = create();
        wishListRepoisotry.save(wishListEntity2);

        int count = wishListRepoisotry.findAll().size();

        Assertions.assertEquals(2, count);

    }

}