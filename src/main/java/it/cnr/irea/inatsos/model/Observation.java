package it.cnr.irea.inatsos.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
	@NamedQuery(name="Observation.findAll", query="SELECT o FROM Observation o"),
	@NamedQuery(name="Observation.removeAll", query="DELETE FROM Observation o"),
	@NamedQuery(name="Observation.findUsers", query="SELECT DISTINCT(o.userId) FROM Observation o")
})
public class Observation {
	@Id
	private long id;
	@JsonProperty("user_login")
	private String userLogin;
	@JsonProperty("place_guess")
	private String placeGuess;
    @JsonProperty("location_is_exact")
    private Boolean isLocationExact;
    @JsonProperty("quality_grade")
    private String qualityGrade;
    private double latitude;
    private double longitude;
    @JsonProperty("created_at")
    private Date createdAt;
    private String timeframe;
    @JsonProperty("species_guess")
    private String speciesGuess;
    @JsonProperty("observed_on")
    private Date observedOn;
    @JsonProperty("num_identification_disagreements")
    private int numIdentificationDisagreements;
    private Boolean delta;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("num_identification_agreements")
    private int numIdentificationAgreements;
    private String license;
    private String geoprivacy;
    @JsonProperty("positional_accuracy")
    private double positionalAccuracy;
    @JsonProperty("coordinates_obscured")
    private Boolean coordinatesObscured;
    
    @JsonProperty("taxon_id")
    private long taxonId;
    
    @JsonProperty("id_please")
    private Boolean idPlease;
    @JsonProperty("time_observed_at_utc")
    private Date timeObservedAtUTC;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("time_observed_at")
    private Date timeObservedAt;
    @JsonProperty("observed_on_string")
    private String observedOnString;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("time_zone")
    private String timeZone;
    @JsonProperty("out_of_range")
    private String outOfRange;
    @Lob @Basic
    private String description;
    @JsonProperty("user.login")
    private String userDotLogin;
    @JsonProperty("positioning_method")
    private String positioningMethod;
    @JsonProperty("map_scale")
    private String mapScale;
/*
    @JsonProperty("iconic_taxon_name")
    private String iconicTaxonName;
*/
    @JsonProperty("positioning_device")
    private String positioningDevice;
    
    @JsonProperty("iconic_taxon_id")
    private long iconicTaxonId;
    @ElementCollection
    @CollectionTable(name = "Observation_Photo", joinColumns = @JoinColumn(name = "observation_id"))
    @OrderColumn
    private List<Photo> photos;
    @Embedded
    @JsonProperty("iconic_taxon")
    private IconicTaxon iconicTaxon;
    
    @ManyToOne
    private Harvest harvest;
        
	private String squareUrl;
	private String thumbUrl;
	private String smallUrl;
	private String mediumUrl;
	private String largeUrl;

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
	public Harvest getHarvest() {
		return harvest;
	}
	public void setHarvest(Harvest harvest) {
		this.harvest = harvest;
	}
	@Override
	public String toString() {
		return "Observation [id=" + id + ", userLogin=" + userLogin + ", placeGuess=" + placeGuess
				+ ", isLocationExact=" + isLocationExact + ", qualityGrade=" + qualityGrade + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", createdAt=" + createdAt + ", timeframe=" + timeframe
				+ ", speciesGuess=" + speciesGuess + ", observedOn=" + observedOn + ", numIdentificationDisagreements="
				+ numIdentificationDisagreements + ", delta=" + delta + ", updatedAt=" + updatedAt
				+ ", numIdentificationAgreements=" + numIdentificationAgreements + ", license=" + license
				+ ", geoprivacy=" + geoprivacy + ", positionalAccuracy=" + positionalAccuracy + ", coordinatesObscured="
				+ coordinatesObscured + ", taxonId=" + taxonId + ", idPlease=" + idPlease + ", timeObservedAtUTC="
				+ timeObservedAtUTC + ", userId=" + userId + ", timeObservedAt=" + timeObservedAt
				+ ", observedOnString=" + observedOnString + ", shortDescription=" + shortDescription + ", timeZone="
				+ timeZone + ", outOfRange=" + outOfRange + ", description=" + description + ", userDotLogin="
				+ userDotLogin + ", positioningMethod=" + positioningMethod + ", mapScale=" + mapScale
				+ /* ", iconicTaxonName=" + iconicTaxonName + */ ", positioningDevice=" + positioningDevice
				+ /* ", iconicTaxonId=" + iconicTaxonId + ", photos=" + photos + ", iconicTaxon=" + iconicTaxon + */ "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getPlaceGuess() {
		return placeGuess;
	}
	public void setPlaceGuess(String placeGuess) {
		this.placeGuess = placeGuess;
	}
	public Boolean getIsLocationExact() {
		return isLocationExact;
	}
	public void setIsLocationExact(Boolean isLocationExact) {
		this.isLocationExact = isLocationExact;
	}
	public String getQualityGrade() {
		return qualityGrade;
	}
	public void setQualityGrade(String qualityGrade) {
		this.qualityGrade = qualityGrade;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	public String getSpeciesGuess() {
		return speciesGuess;
	}
	public void setSpeciesGuess(String speciesGuess) {
		this.speciesGuess = speciesGuess;
	}
	public Date getObservedOn() {
		return observedOn;
	}
	public void setObservedOn(Date observedOn) {
		this.observedOn = observedOn;
	}
	public int getNumIdentificationDisagreements() {
		return numIdentificationDisagreements;
	}
	public void setNumIdentificationDisagreements(int numIdentificationDisagreements) {
		this.numIdentificationDisagreements = numIdentificationDisagreements;
	}
	public Boolean getDelta() {
		return delta;
	}
	public void setDelta(Boolean delta) {
		this.delta = delta;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getNumIdentificationAgreements() {
		return numIdentificationAgreements;
	}
	public void setNumIdentificationAgreements(int numIdentificationAgreements) {
		this.numIdentificationAgreements = numIdentificationAgreements;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getGeoprivacy() {
		return geoprivacy;
	}
	public void setGeoprivacy(String geoprivacy) {
		this.geoprivacy = geoprivacy;
	}
	public double getPositionalAccuracy() {
		return positionalAccuracy;
	}
	public void setPositionalAccuracy(double positionalAccuracy) {
		this.positionalAccuracy = positionalAccuracy;
	}
	public Boolean getCoordinatesObscured() {
		return coordinatesObscured;
	}
	public void setCoordinatesObscured(Boolean coordinatesObscured) {
		this.coordinatesObscured = coordinatesObscured;
	}
	
	public long getTaxonId() {
		return taxonId;
	}
	public void setTaxonId(long taxonId) {
		this.taxonId = taxonId;
	}
	
	public Boolean getIdPlease() {
		return idPlease;
	}
	public void setIdPlease(Boolean idPlease) {
		this.idPlease = idPlease;
	}
	public Date getTimeObservedAtUTC() {
		return timeObservedAtUTC;
	}
	public void setTimeObservedAtUTC(Date timeObservedAtUTC) {
		this.timeObservedAtUTC = timeObservedAtUTC;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Date getTimeObservedAt() {
		return timeObservedAt;
	}
	public void setTimeObservedAt(Date timeObservedAt) {
		this.timeObservedAt = timeObservedAt;
	}
	public String getObservedOnString() {
		return observedOnString;
	}
	public void setObservedOnString(String observedOnString) {
		this.observedOnString = observedOnString;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getOutOfRange() {
		return outOfRange;
	}
	public void setOutOfRange(String outOfRange) {
		this.outOfRange = outOfRange;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserDotLogin() {
		return userDotLogin;
	}
	public void setUserDotLogin(String userDotLogin) {
		this.userDotLogin = userDotLogin;
	}
	public String getPositioningMethod() {
		return positioningMethod;
	}
	public void setPositioningMethod(String positioningMethod) {
		this.positioningMethod = positioningMethod;
	}
	public String getMapScale() {
		return mapScale;
	}
	public void setMapScale(String mapScale) {
		this.mapScale = mapScale;
	}
	/*
	public String getIconicTaxonName() {
		return iconicTaxonName;
	}
	public void setIconicTaxonName(String iconicTaxonName) {
		this.iconicTaxonName = iconicTaxonName;
	}
	*/
	public String getPositioningDevice() {
		return positioningDevice;
	}
	public void setPositioningDevice(String positioningDevice) {
		this.positioningDevice = positioningDevice;
	}
	public long getIconicTaxonId() {
		return iconicTaxonId;
	}
	public void setIconicTaxonId(long iconicTaxonId) {
		this.iconicTaxonId = iconicTaxonId;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	public IconicTaxon getIconicTaxon() {
		return iconicTaxon;
	}
	public void setIconicTaxon(IconicTaxon iconicTaxon) {
		this.iconicTaxon = iconicTaxon;
	}
    /*
    "iconic_taxon": {
        "name": "Mammalia",
        "ancestry": "48460/1/2",
        "rank": "class",
        "id": 40151,
        "rank_level": 50,
        "iconic_taxon_name": "Mammalia"
    },
    "user": {
        "login": "greatjon"
    },
    "photos": [],
	*/
	
}
