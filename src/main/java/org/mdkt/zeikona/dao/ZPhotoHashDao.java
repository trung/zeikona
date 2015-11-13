package org.mdkt.zeikona.dao;

import org.mdkt.zeikona.model.ZPhotoHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZPhotoHashDao extends CrudRepository<ZPhotoHash, Integer> {

}
