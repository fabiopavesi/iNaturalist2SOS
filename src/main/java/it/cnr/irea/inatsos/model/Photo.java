package it.cnr.irea.inatsos.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class Photo {
	@JsonProperty("id")
	private long photoId;
	@JsonProperty("user_id")
	private long userId;
	@JsonProperty("native_photo_id")
	private String nativePhotoId;
	@JsonProperty("square_url")
	private String squareUrl;
	@JsonProperty("thumb_url")
	private String thumbUrl;
	@JsonProperty("small_url")
	private String smallUrl;
	@JsonProperty("medium_url")
	private String mediumUrl;
	@JsonProperty("large_url")
	private String largeUrl;
	@JsonProperty("created_at")
	private Date createdAt;
	@JsonProperty("updated_at")
	private Date updatedAt;
	@JsonProperty("native_page_url")
	private String nativePageUrl;
	@JsonProperty("native_username")
	private String nativeUserName;
	@JsonProperty("native_realname")
	private String nativeRealName;
	private long licence;
	@JsonProperty("file_updated_at")
	private Date fileUpdatedAt;
	private String subtype;
	@JsonProperty("native_original_image_url")
	private String nativeOriginalImageUrl;
	@JsonProperty("license_code")
	private String licenceCode;
	private String attribution;
	
	public long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(long id) {
		this.photoId = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getNativePhotoId() {
		return nativePhotoId;
	}
	public void setNativePhotoId(String nativePhotoId) {
		this.nativePhotoId = nativePhotoId;
	}
	public String getSquareUrl() {
		return squareUrl;
	}
	public void setSquareUrl(String squareUrl) {
		this.squareUrl = squareUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getSmallUrl() {
		return smallUrl;
	}
	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	public String getMediumUrl() {
		return mediumUrl;
	}
	public void setMediumUrl(String mediumUrl) {
		this.mediumUrl = mediumUrl;
	}
	public String getLargeUrl() {
		return largeUrl;
	}
	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getNativePageUrl() {
		return nativePageUrl;
	}
	public void setNativePageUrl(String nativePageUrl) {
		this.nativePageUrl = nativePageUrl;
	}
	public String getNativeUserName() {
		return nativeUserName;
	}
	public void setNativeUserName(String nativeUserName) {
		this.nativeUserName = nativeUserName;
	}
	public String getNativeRealName() {
		return nativeRealName;
	}
	public void setNativeRealName(String nativeRealName) {
		this.nativeRealName = nativeRealName;
	}
	public long getLicence() {
		return licence;
	}
	public void setLicence(long licence) {
		this.licence = licence;
	}
	public Date getFileUpdatedAt() {
		return fileUpdatedAt;
	}
	public void setFileUpdatedAt(Date fileUpdatedAt) {
		this.fileUpdatedAt = fileUpdatedAt;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getNativeOriginalImageUrl() {
		return nativeOriginalImageUrl;
	}
	public void setNativeOriginalImageUrl(String nativeOriginalImageUrl) {
		this.nativeOriginalImageUrl = nativeOriginalImageUrl;
	}
	public String getLicenceCode() {
		return licenceCode;
	}
	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}
	public String getAttribution() {
		return attribution;
	}
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}
	
	
/*
 	{
 		"id":2548114,
 		"user_id":77202,
 		"native_photo_id":"2548114",
 		"square_url":"http://static.inaturalist.org/photos/2548114/square.jpg?1445323238",
 		"thumb_url":"http://static.inaturalist.org/photos/2548114/thumb.jpg?1445323238",
 		"small_url":"http://static.inaturalist.org/photos/2548114/small.jpg?1445323238",
 		"medium_url":"http://static.inaturalist.org/photos/2548114/medium.jpg?1445323238",
 		"large_url":"http://static.inaturalist.org/photos/2548114/large.jpg?1445323238",
 		"created_at":"2015-10-20T01:39:09.478-05:00",
 		"updated_at":"2015-10-20T01:40:52.432-05:00",
 		"native_page_url":"http://www.inaturalist.org/photos/2548114",
 		"native_username":"oggioniale",
 		"native_realname":"",
 		"license":3,
 		"file_updated_at":"2015-10-20T01:40:38.588-05:00",
 		"subtype":null,
 		"native_original_image_url":null,
 		"license_code":"CC-BY-NC-ND",
 		"attribution":"(c) oggioniale, some rights reserved (CC BY-NC-ND)"
 	}
 */
}
