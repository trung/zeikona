package org.mdkt.zeikona.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ZAlbum {
	@Id
	private String id;
	private String name;
	private Integer photoCount;
	private Integer photoProcessedCount = 0;

	public ZAlbum() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(Integer photoCount) {
		this.photoCount = photoCount;
	}

	public void setPhotoProcessedCount(Integer photoProcessedCount) {
		this.photoProcessedCount = photoProcessedCount;
	}

	public Integer getPhotoProcessedCount() {
		return photoProcessedCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ZAlbum [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", photoCount=");
		builder.append(photoCount);
		builder.append(", photoProcessedCount=");
		builder.append(photoProcessedCount);
		builder.append("]");
		return builder.toString();
	}

}
