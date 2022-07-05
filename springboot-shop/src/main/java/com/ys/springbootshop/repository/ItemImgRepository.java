package com.ys.springbootshop.repository;

import com.ys.springbootshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : ysk
 */
@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);


    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}
