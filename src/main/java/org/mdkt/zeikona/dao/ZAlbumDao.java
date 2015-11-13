package org.mdkt.zeikona.dao;

import org.mdkt.zeikona.model.ZAlbum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZAlbumDao extends CrudRepository<ZAlbum, String> {

}
