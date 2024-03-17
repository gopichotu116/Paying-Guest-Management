package com.org.payingGuest.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
public class Owner implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	public String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String phno;
	
	@Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;
	
	public Owner(String name, String email, String password, String phno) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phno = phno;
	}
	
}
