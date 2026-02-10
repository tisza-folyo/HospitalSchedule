package com.hospital.backend.service.image;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.ImageMapper;
import com.hospital.backend.model.Appointment;
import com.hospital.backend.model.Image;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.ImageRepository;
import com.hospital.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ImageMapper imageMapper;

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, String pTaj) {
        Patient patient = patientRepository.findByTaj(pTaj).orElseThrow(() -> new ResourceNotFoundException("Patient"));
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setPatient(patient);

                String buildDwnUrl = "/hospital/images/download";
                String dwnUrl = buildDwnUrl + image.getImageId();
                image.setDwnUrl(dwnUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDwnUrl(buildDwnUrl + savedImage.getImageId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDwnUrl(savedImage.getDwnUrl());
                savedImageDto.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Appointment"));
        List<ImageDto> savedImageDto = new ArrayList<>();
        appointment.setSymptomImg(new ArrayList<>());
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                appointment.getSymptomImg().add(image);

                String buildDwnUrl = "/hospital/images/download";
                String dwnUrl = buildDwnUrl + image.getImageId();
                image.setDwnUrl(dwnUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDwnUrl(buildDwnUrl + savedImage.getImageId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDwnUrl(savedImage.getDwnUrl());
                savedImageDto.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void deleteImagesByTaj(String pTaj) {
        List<Image> images = imageRepository.findAllByPatient_Taj(pTaj);
        if (images.isEmpty()) {
            throw new ResourceNotFoundException("Image");
        }
        imageRepository.deleteAll(images);
    }

    @Override
    public void deleteImageByDwnUrl(String dwnUrl){
        Image image = imageRepository.findByDwnUrl(dwnUrl).orElseThrow(() -> new ResourceNotFoundException("Image"));
        imageRepository.delete(image);
    }

    @Override
    public List<ImageDto> getPatientImages(String pTaj){
        List<Image> images = imageRepository.findAllByPatient_Taj(pTaj);
        if (images.isEmpty()) throw new ResourceNotFoundException("Image");
        return imageMapper.toDtoList(images);
    }

    @Override
    public void updateImage(MultipartFile file, String dwnUrl) {
        Image image = imageRepository.findByDwnUrl(dwnUrl).orElseThrow(() -> new ResourceNotFoundException("Image"));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
