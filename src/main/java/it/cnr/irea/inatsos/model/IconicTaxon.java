package it.cnr.irea.inatsos.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class IconicTaxon {
	@JsonProperty("id")
	@Column(nullable=true)
	private long taxonMainId;
	private String name;
	private String ancestry;
	private String rank;
	@JsonProperty("rank_level")
	@Column(nullable=true)
	private long rankLevel = -1;
	@JsonProperty("iconic_taxon_name")
	private String iconicTaxonName;
	
	public long getTaxonMainId() {
		return taxonMainId;
	}
	public void setTaxonMainId(long id) {
		this.taxonMainId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAncestry() {
		return ancestry;
	}
	public void setAncestry(String ancestry) {
		this.ancestry = ancestry;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public long getRankLevel() {
		return rankLevel;
	}
	public void setRankLevel(long rankLevel) {
		this.rankLevel = rankLevel;
	}
	public String getIconicTaxonName() {
		return iconicTaxonName;
	}
	public void setIconicTaxonName(String iconicTaxonName) {
		this.iconicTaxonName = iconicTaxonName;
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
    */
}
