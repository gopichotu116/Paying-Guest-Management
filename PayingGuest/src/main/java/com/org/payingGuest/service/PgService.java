package com.org.payingGuest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.org.payingGuest.entity.Owner;
import com.org.payingGuest.entity.Pg;
import com.org.payingGuest.repository.PgRepository;

@Service
public class PgService {
	
	@Autowired
	private PgRepository pgRepo;
	
	public Pg getPg(Integer id) {
		return pgRepo.findById(id).orElse(null);
//		return pgRepo.findById(id).get();
	}
	
	public void savePg(String name, String location, String area, List<MultipartFile> outsideImages,
			List<MultipartFile> insideImages, MultipartFile rulesImage, MultipartFile rentCard,
			MultipartFile boardImage, String phno, String maps, String address, Owner owner) throws IOException {
		Pg pg = new Pg();
		pg.setName(name);
		pg.setLocation(location);
		pg.setArea(area);
		pg.setPhno(phno);
		pg.setMaps(maps);
		pg.setAddress(address);
		pg.setOwner(owner);
		// Convert and set image data
        pg.setOutsideImages(convertImages(outsideImages));
        pg.setInsideImages(convertImages(insideImages));
        
        pg.setRulesImage(rulesImage.getBytes());
        pg.setRentCard(rentCard.getBytes());
        pg.setBoardImage(boardImage.getBytes());
		
// Convert and set image data
// Add logic here to convert MultipartFile to byte[] and set it to the corresponding fields
		pgRepo.save(pg);
	}
	
	// Utility method to convert MultipartFile objects to byte arrays
    private List<byte[]> convertImages(List<MultipartFile> multipartFiles) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            images.add(file.getBytes());
        }
        return images;
    }

	public List<Pg> getPgsByOwnerId(Integer ownerId) {
		return pgRepo.findPgsByOwnerId(ownerId);
	}

	public void deletePgById(Integer id) {
		pgRepo.deleteById(id);
	}

	public Pg getById(Integer id) {
		return pgRepo.findById(id).orElse(null);
	}

}
