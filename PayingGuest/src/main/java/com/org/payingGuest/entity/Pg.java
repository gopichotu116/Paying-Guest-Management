package com.org.payingGuest.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
public class Pg implements Serializable{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private String area;

    @ElementCollection
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> outsideImages;

    @ElementCollection
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> insideImages;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] rulesImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] rentCard;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] boardImage;

    private String phno;
    
    private String maps;
    
    private String address;
    
    @JoinColumn(name = "owner_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Owner owner;

	public Pg(String name, String location, String area, List<byte[]> outsideImages, List<byte[]> insideImages,
			byte[] rulesImage, byte[] rentCard, byte[] boardImage, String phno, String maps, String address,
			Owner owner) {
		super();
		this.name = name;
		this.location = location;
		this.area = area;
		this.outsideImages = outsideImages;
		this.insideImages = insideImages;
		this.rulesImage = rulesImage;
		this.rentCard = rentCard;
		this.boardImage = boardImage;
		this.phno = phno;
		this.maps = maps;
		this.address = address;
		this.owner = owner;
	}
    
    
}

