package it.cnr.irea.inatsos.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT a FROM User a")
})
public class User {
	@Id
	private long id;
	private String login;
	private String name;
	@JsonProperty("created_at")
	private Date createdAt;
	@JsonProperty("updated_at")
	private Date updatedAt;
	@JsonProperty("time_zone")	
	private String timeZone;
	@Lob @Basic
	private String description;
	@JsonProperty("icon_file_name")	
	private String iconFileName;
	@JsonProperty("icon_content_type")	
	private String iconContentType;
	@JsonProperty("icon_file_size")	
	private long iconFileSize;
	@JsonProperty("life_list_id")	
	private long lifeListId;
	@JsonProperty("observations_count")	
	private long observationsCount;
	@JsonProperty("identifications_count")	
	private long identificationsCount;
	@JsonProperty("journal_posts_count")	
	private long journalPostsCount;
	@JsonProperty("life_list_taxa_count")	
	private long lifeListTaxaCount;
	@JsonProperty("icon_url")	
	private String iconUrl;
	@JsonProperty("icon_updated_at")	
	private Date iconUpdatedAt;
	private String uri;
	private String locale;
	@JsonProperty("site_id")	
	private long siteId;
	@JsonProperty("place_id")	
	private long placeId;
	private boolean spammer;
	@JsonProperty("spam_count")	
	private long spamCount;
	@ManyToOne
	private Harvest harvest;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIconFileName() {
		return iconFileName;
	}
	public void setIconFileName(String iconFileName) {
		this.iconFileName = iconFileName;
	}
	public String getIconContentType() {
		return iconContentType;
	}
	public void setIconContentType(String iconContentType) {
		this.iconContentType = iconContentType;
	}
	public long getIconFileSize() {
		return iconFileSize;
	}
	public void setIconFileSize(long iconFileSize) {
		this.iconFileSize = iconFileSize;
	}
	public long getLifeListId() {
		return lifeListId;
	}
	public void setLifeListId(long lifeListId) {
		this.lifeListId = lifeListId;
	}
	public long getObservationsCount() {
		return observationsCount;
	}
	public void setObservationsCount(long observationsCount) {
		this.observationsCount = observationsCount;
	}
	public long getIdentificationsCount() {
		return identificationsCount;
	}
	public void setIdentificationsCount(long identificationsCount) {
		this.identificationsCount = identificationsCount;
	}
	public long getJournalPostsCount() {
		return journalPostsCount;
	}
	public void setJournalPostsCount(long journalPostsCount) {
		this.journalPostsCount = journalPostsCount;
	}
	public long getLifeListTaxaCount() {
		return lifeListTaxaCount;
	}
	public void setLifeListTaxaCount(long lifeListTaxaCount) {
		this.lifeListTaxaCount = lifeListTaxaCount;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Date getIconUpdatedAt() {
		return iconUpdatedAt;
	}
	public void setIconUpdatedAt(Date iconUpdatedAt) {
		this.iconUpdatedAt = iconUpdatedAt;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public long getPlaceId() {
		return placeId;
	}
	public void setPlaceId(long placeId) {
		this.placeId = placeId;
	}
	public boolean isSpammer() {
		return spammer;
	}
	public void setSpammer(boolean spammer) {
		this.spammer = spammer;
	}
	public long getSpamCount() {
		return spamCount;
	}
	public void setSpamCount(long spamCount) {
		this.spamCount = spamCount;
	}
	public Harvest getHarvest() {
		return harvest;
	}
	public void setHarvest(Harvest harvest) {
		this.harvest = harvest;
	}
	
}
